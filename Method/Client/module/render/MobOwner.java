package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.PlayerIdentity;
import java.util.Iterator;
import java.util.LinkedHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class MobOwner extends Module {
   Setting Speedh;
   Setting Jumph;
   Setting Hpm;
   public static LinkedHashMap<String, PlayerIdentity> identityCacheMap = new LinkedHashMap();

   public MobOwner() {
      super("MobOwner", 0, Category.RENDER, "MobOwner");
      this.Speedh = Main.setmgr.add(new Setting("Speed horse", this, false));
      this.Jumph = Main.setmgr.add(new Setting("Jump Horse", this, false));
      this.Hpm = Main.setmgr.add(new Setting("Hp", this, false));
   }

   public static PlayerIdentity getPlayerIdentity(String UUID) {
      return identityCacheMap.containsKey(UUID) ? (PlayerIdentity)identityCacheMap.get(UUID) : new PlayerIdentity(UUID);
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      Iterator var2 = mc.field_71441_e.func_72910_y().iterator();

      while(var2.hasNext()) {
         Entity entity = (Entity)var2.next();
         String Speed;
         if (entity instanceof EntityTameable) {
            EntityTameable tameableEntity = (EntityTameable)entity;
            if (tameableEntity.func_70909_n() && tameableEntity.func_184753_b() != null) {
               tameableEntity.func_174805_g(true);
               Speed = this.Hpm.getValBoolean() ? "\n" + ((EntityTameable)entity).func_110143_aJ() : "";
               PlayerIdentity identity = getPlayerIdentity(tameableEntity.func_184753_b().toString());
               tameableEntity.func_96094_a("Owned by " + identity.getDisplayName() + Speed);
            }
         }

         if (entity instanceof AbstractHorse) {
            AbstractHorse tameableEntity = (AbstractHorse)entity;
            if (tameableEntity.func_110248_bS() && tameableEntity.func_184780_dh() != null) {
               Speed = this.Speedh.getValBoolean() ? " Speed: " + (double)((AbstractHorse)entity).func_70689_ay() * 43.17D : "";
               String Hp = this.Hpm.getValBoolean() ? " HP: " + ((AbstractHorse)entity).func_110143_aJ() : "";
               String Jump = this.Jumph.getValBoolean() ? " Jump: " + (-0.1817584952D * Math.pow(((AbstractHorse)entity).func_110215_cj(), 3.0D) + 3.689713992D * Math.pow(((AbstractHorse)entity).func_110215_cj(), 2.0D) + 2.128599134D * ((AbstractHorse)entity).func_110215_cj() - 0.343930367D) : "";
               tameableEntity.func_174805_g(true);
               PlayerIdentity identity = getPlayerIdentity(tameableEntity.func_184780_dh().toString());
               tameableEntity.func_96094_a("Owned by " + identity.getDisplayName() + Speed + Jump + Hp);
            }
         }
      }

      super.onRenderWorldLast(event);
   }
}
