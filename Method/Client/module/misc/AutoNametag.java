package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.ChatUtils;
import java.util.Comparator;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoNametag extends Module {
   Setting Radius;
   Setting ReplaceOldNames;
   Setting AutoSwitch;
   Setting WithersOnly;

   public AutoNametag() {
      super("AutoNametag", 0, Category.MISC, "AutoNametag");
      this.Radius = Main.setmgr.add(new Setting("range", this, 4.0D, 0.0D, 10.0D, true));
      this.ReplaceOldNames = Main.setmgr.add(new Setting("ReplaceOldNames", this, true));
      this.AutoSwitch = Main.setmgr.add(new Setting("AutoSwitch", this, true));
      this.WithersOnly = Main.setmgr.add(new Setting("WithersOnly ", this, true));
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71462_r == null) {
         if (!(mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemNameTag)) {
            int i1 = -1;
            if (this.AutoSwitch.getValBoolean()) {
               for(int i = 0; i < 9; ++i) {
                  ItemStack item = mc.field_71439_g.field_71071_by.func_70301_a(i);
                  if (!item.func_190926_b() && item.func_77973_b() instanceof ItemNameTag && item.func_82837_s()) {
                     i1 = i;
                     mc.field_71439_g.field_71071_by.field_70461_c = i;
                     mc.field_71442_b.func_78765_e();
                     break;
                  }
               }
            }

            if (i1 == -1) {
               return;
            }
         }

         ItemStack name = mc.field_71439_g.func_184614_ca();
         if (name.func_82837_s()) {
            EntityLivingBase l_Entity = (EntityLivingBase)mc.field_71441_e.field_72996_f.stream().filter((p_Entity) -> {
               return this.IsValidEntity(p_Entity, name.func_82833_r());
            }).map((p_Entity) -> {
               return (EntityLivingBase)p_Entity;
            }).min(Comparator.comparing((p_Entity) -> {
               return mc.field_71439_g.func_70032_d(p_Entity);
            })).orElse((Object)null);
            if (l_Entity != null) {
               double[] lPos = calculateLookAt(l_Entity.field_70165_t, l_Entity.field_70163_u, l_Entity.field_70161_v, mc.field_71439_g);
               ChatUtils.message(String.format("Gave %s the nametag of %s", l_Entity.func_70005_c_(), name.func_82833_r()));
               mc.field_71439_g.field_70759_as = (float)lPos[0];
               ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketUseEntity(l_Entity, EnumHand.MAIN_HAND));
            }

         }
      }
   }

   public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
      double dirx = me.field_70165_t - px;
      double diry = me.field_70163_u - py;
      double dirz = me.field_70161_v - pz;
      double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
      dirx /= len;
      diry /= len;
      dirz /= len;
      double pitch = Math.asin(diry);
      double yaw = Math.atan2(dirz, dirx);
      pitch = pitch * 180.0D / 3.141592653589793D;
      yaw = yaw * 180.0D / 3.141592653589793D;
      yaw += 90.0D;
      return new double[]{yaw, pitch};
   }

   private boolean IsValidEntity(Entity entity, String pName) {
      if (!(entity instanceof EntityLivingBase)) {
         return false;
      } else if ((double)entity.func_70032_d(mc.field_71439_g) > this.Radius.getValDouble()) {
         return false;
      } else if (entity instanceof EntityPlayer) {
         return false;
      } else if (!entity.func_95999_t().isEmpty() && !this.ReplaceOldNames.getValBoolean()) {
         return false;
      } else if (this.ReplaceOldNames.getValBoolean() && !entity.func_95999_t().isEmpty() && entity.func_70005_c_().equals(pName)) {
         return false;
      } else {
         return !this.WithersOnly.getValBoolean() || entity instanceof EntityWither;
      }
   }
}
