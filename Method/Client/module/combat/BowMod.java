package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.ValidUtils;
import Method.Client.utils.visual.ChatUtils;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class BowMod extends Module {
   public Setting BowSpam;
   public Setting PacketSpam;
   public Setting AimBot;
   public Setting walls;
   public Setting yaw;
   public Setting FOV;
   public Setting KickBow;
   public EntityLivingBase target;
   public float rangeAimVelocity;

   public BowMod() {
      super("BowMod", 0, Category.COMBAT, "BowMod");
      this.BowSpam = Main.setmgr.add(new Setting("BowSpam", this, false));
      this.PacketSpam = Main.setmgr.add(new Setting("PacketSpam", this, false, this.BowSpam, 2));
      this.AimBot = Main.setmgr.add(new Setting("AimBot", this, false));
      this.walls = Main.setmgr.add(new Setting("walls", this, false, this.AimBot, 6));
      this.yaw = Main.setmgr.add(new Setting("Yaw", this, 22.0D, 0.0D, 50.0D, false, this.AimBot, 4));
      this.FOV = Main.setmgr.add(new Setting("FOV", this, 90.0D, 1.0D, 180.0D, true, this.AimBot, 5));
      this.KickBow = Main.setmgr.add(new Setting("KickBow", this, false));
      this.rangeAimVelocity = 0.0F;
   }

   public void onDisable() {
      this.target = null;
      super.onDisable();
   }

   public void onClientTick(ClientTickEvent event) {
      mc.field_71439_g.field_71071_by.func_70448_g();
      if (mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow) {
         if (mc.field_71474_y.field_74313_G.func_151470_d()) {
            if (this.KickBow.getValBoolean() && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow && mc.field_71439_g.func_184587_cr() && mc.field_71439_g.func_184612_cw() >= 25) {
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItem(mc.field_71439_g.func_184600_cs()));
               mc.field_71439_g.func_184597_cx();
               if (this.findShulker() != -1) {
                  this.changeItem(this.findShulker());
               } else {
                  ChatUtils.message("No shulker found in hotbar, not switching...");
                  this.toggle();
               }
            }

            if (this.AimBot.getValBoolean()) {
               this.target = this.getClosestEntity();
               if (this.target == null) {
                  return;
               }

               float rangeCharge = (float)mc.field_71439_g.func_184605_cv();
               this.rangeAimVelocity = rangeCharge / 20.0F;
               this.rangeAimVelocity = (this.rangeAimVelocity * this.rangeAimVelocity + this.rangeAimVelocity * 2.0F) / 3.0F;
               this.rangeAimVelocity = 1.0F;
               double posX = this.target.field_70165_t - mc.field_71439_g.field_70165_t;
               double posY = this.target.field_70163_u + (double)this.target.func_70047_e() - 0.15D - mc.field_71439_g.field_70163_u - (double)mc.field_71439_g.func_70047_e();
               double posZ = this.target.field_70161_v - mc.field_71439_g.field_70161_v;
               double y2 = Math.sqrt(posX * posX + posZ * posZ);
               float g = 0.006F;
               float tmp = (float)((double)(this.rangeAimVelocity * this.rangeAimVelocity * this.rangeAimVelocity * this.rangeAimVelocity) - (double)g * ((double)g * y2 * y2 + 2.0D * posY * (double)(this.rangeAimVelocity * this.rangeAimVelocity)));
               float pitch = (float)(-Math.toDegrees(Math.atan(((double)(this.rangeAimVelocity * this.rangeAimVelocity) - Math.sqrt((double)tmp)) / ((double)g * y2))));
               float[] rot = Utils.getNeededRotations(this.target.func_174791_d(), (float)this.yaw.getValDouble(), 0.0F);
               mc.field_71439_g.field_70177_z = rot[0];
               mc.field_71439_g.field_70125_A = pitch;
            }

            if (this.BowSpam.getValBoolean() && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow && mc.field_71439_g.func_184587_cr() && mc.field_71439_g.func_184612_cw() >= 3) {
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
               if (this.PacketSpam.getValBoolean()) {
                  for(int var1 = 0; var1 < 10; ++var1) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayer(true));
                  }
               }

               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItem(mc.field_71439_g.func_184600_cs()));
               mc.field_71439_g.func_184597_cx();
            }

         }
      }
   }

   public int findShulker() {
      byte b = -1;

      for(byte b1 = 0; b1 < 9; ++b1) {
         ItemStack itemStack = (ItemStack)mc.field_71439_g.field_71071_by.field_70462_a.get(b1);
         if (itemStack.func_77973_b() instanceof ItemShulkerBox) {
            b = b1;
         }
      }

      return b;
   }

   public void changeItem(int paramInt) {
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(paramInt));
      mc.field_71439_g.field_71071_by.field_70461_c = paramInt;
   }

   public boolean check(EntityLivingBase entity) {
      if (this.Checks(entity, this.FOV)) {
         return false;
      } else if (!ValidUtils.pingCheck(entity)) {
         return false;
      } else {
         return !this.walls.getValBoolean() ? mc.field_71439_g.func_70685_l(entity) : true;
      }
   }

   boolean Checks(EntityLivingBase entity, Setting fov) {
      if (entity instanceof EntityArmorStand) {
         return true;
      } else if (!ValidUtils.isNoScreen()) {
         return true;
      } else if (entity == mc.field_71439_g) {
         return true;
      } else if (entity.field_70128_L) {
         return true;
      } else if (ValidUtils.isBot(entity)) {
         return true;
      } else if (ValidUtils.isFriendEnemy(entity)) {
         return true;
      } else if (!ValidUtils.isInAttackFOV(entity, (int)fov.getValDouble())) {
         return true;
      } else {
         return !(entity instanceof EntityPlayer);
      }
   }

   EntityLivingBase getClosestEntity() {
      EntityLivingBase closestEntity = null;
      Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

      while(true) {
         EntityLivingBase entity;
         do {
            do {
               Object o;
               do {
                  do {
                     if (!var2.hasNext()) {
                        return closestEntity;
                     }

                     o = var2.next();
                  } while(!(o instanceof EntityLivingBase));
               } while(o instanceof EntityArmorStand);

               entity = (EntityLivingBase)o;
            } while(!this.check(entity));
         } while(closestEntity != null && !(mc.field_71439_g.func_70032_d(entity) < mc.field_71439_g.func_70032_d(closestEntity)));

         closestEntity = entity;
      }
   }
}
