package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.Patcher.Events.PlayerMoveEvent;
import java.util.Objects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Strafe extends Module {
   Setting mode;
   Setting jump;
   Setting extraYBoost;
   Setting jumpDetect;
   Setting speedDetect;
   Setting multiplier;
   Setting ground;
   int waitCounter;
   int forward;
   private double motionSpeed;
   private int currentState;
   private double prevDist;

   public Strafe() {
      super("Strafe", 0, Category.COMBAT, "Strafe");
      this.mode = Main.setmgr.add(new Setting("Strafe Mode", this, "Vanilla", new String[]{"Vanilla", "Fast", "Bypass"}));
      this.jump = Main.setmgr.add(new Setting("FastJump", this, true, this.mode, "Bypass", 1));
      this.extraYBoost = Main.setmgr.add(new Setting("extraYBoost", this, 0.0D, 0.0D, 1.0D, false, this.mode, "Fast", 1));
      this.jumpDetect = Main.setmgr.add(new Setting("jumpDetect", this, false, this.mode, "Fast", 1));
      this.speedDetect = Main.setmgr.add(new Setting("speedDetect", this, false, this.mode, "Fast", 1));
      this.multiplier = Main.setmgr.add(new Setting("multiplier", this, 1.0D, 0.0D, 1.0D, false, this.mode, "Fast", 1));
      this.ground = Main.setmgr.add(new Setting("ground", this, true, this.mode, "Vanilla", 1));
      this.waitCounter = 0;
      this.forward = 1;
   }

   public void onDisable() {
      mc.field_71428_T.field_194149_e = 50.0F;
      super.onEnable();
   }

   private double getBaseMotionSpeed() {
      double v = 0.272D;
      if (mc.field_71439_g.func_70644_a(MobEffects.field_76424_c) && this.speedDetect.getValBoolean()) {
         int amplifier = ((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c();
         v *= 1.0D + 0.2D * (double)amplifier;
      }

      return v;
   }

   private static int calcl(float var0) {
      float var2;
      return (var2 = var0 - 0.0F) == 0.0F ? 0 : (var2 < 0.0F ? -1 : 1);
   }

   private static int Call(double var0) {
      double var4;
      return (var4 = var0 - 0.0D) == 0.0D ? 0 : (var4 < 0.0D ? -1 : 1);
   }

   public void onPlayerMove(PlayerMoveEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Fast")) {
         switch(this.currentState) {
         case 0:
            ++this.currentState;
            this.prevDist = 0.0D;
            break;
         case 1:
         default:
            if ((mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, mc.field_71439_g.field_70181_x, 0.0D)).size() > 0 || mc.field_71439_g.field_70124_G) && this.currentState > 0) {
               if (calcl(mc.field_71439_g.field_191988_bg) == 0 && calcl(mc.field_71439_g.field_70702_br) == 0) {
                  this.currentState = 0;
               } else {
                  this.currentState = 1;
               }
            }

            this.motionSpeed = this.prevDist - this.prevDist / 159.0D;
            break;
         case 2:
            double v = 0.40123128D + this.extraYBoost.getValDouble();
            if ((calcl(mc.field_71439_g.field_191988_bg) != 0 || calcl(mc.field_71439_g.field_70702_br) != 0) && mc.field_71439_g.field_70122_E) {
               if (mc.field_71439_g.func_70644_a(MobEffects.field_76430_j) && this.jumpDetect.getValBoolean()) {
                  v += (double)((float)(((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76430_j))).func_76458_c() + 1) * 0.1F);
               }

               mc.field_71439_g.field_70181_x = v;
               this.motionSpeed *= 2.149D;
            }
            break;
         case 3:
            this.motionSpeed = this.prevDist - 0.76D * (this.prevDist - this.getBaseMotionSpeed());
         }

         this.motionSpeed = Math.max(this.motionSpeed, this.getBaseMotionSpeed());
         double v1 = (double)mc.field_71439_g.field_71158_b.field_192832_b;
         double v2 = (double)mc.field_71439_g.field_71158_b.field_78902_a;
         double v3 = (double)mc.field_71439_g.field_70177_z;
         if (Call(v1) == 0 && Call(v2) == 0) {
            mc.field_71439_g.field_70159_w = 0.0D;
            mc.field_71439_g.field_70179_y = 0.0D;
         }

         if (Call(v1) != 0 && Call(v2) != 0) {
            v1 *= Math.sin(0.7853981633974483D);
            v2 *= Math.cos(0.7853981633974483D);
         }

         mc.field_71439_g.field_70159_w = (v1 * this.motionSpeed * -Math.sin(Math.toRadians(v3)) + v2 * this.motionSpeed * Math.cos(Math.toRadians(v3))) * this.multiplier.getValDouble() * 0.99D;
         mc.field_71439_g.field_70179_y = (v1 * this.motionSpeed * Math.cos(Math.toRadians(v3)) - v2 * this.motionSpeed * -Math.sin(Math.toRadians(v3))) * this.multiplier.getValDouble() * 0.99D;
         ++this.currentState;
      }

   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Fast")) {
         this.prevDist = Math.sqrt((mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q) * (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q) + (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s) * (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s));
      }

      if (Utils.isMoving(mc.field_71439_g) && this.mode.getValString().equalsIgnoreCase("Vanilla")) {
         if (mc.field_71439_g.func_70093_af() || mc.field_71439_g.func_70617_f_() || mc.field_71439_g.field_70134_J || mc.field_71439_g.func_180799_ab() || mc.field_71439_g.func_70090_H() || mc.field_71439_g.field_71075_bZ.field_75100_b) {
            return;
         }

         if (!this.ground.getValBoolean() && mc.field_71439_g.field_70122_E) {
            return;
         }

         float playerSpeed = 0.2873F;
         if (mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            int amplifier = ((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c();
            playerSpeed *= 1.0F + 0.2F * (float)(amplifier + 1);
         }

         if (mc.field_71439_g.field_71158_b.field_192832_b == 0.0F && mc.field_71439_g.field_71158_b.field_78902_a == 0.0F) {
            mc.field_71439_g.field_70159_w = 0.0D;
            mc.field_71439_g.field_70179_y = 0.0D;
         } else {
            Utils.directionSpeed((double)playerSpeed);
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Bypass")) {
         mc.field_71428_T.field_194149_e = 45.5F;
         boolean boost = Math.abs(mc.field_71439_g.field_70759_as - mc.field_71439_g.field_70177_z) < 90.0F;
         if ((this.isPlayerTryingMoveForward() || this.isPlayerTryingStrafe()) && mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.field_70181_x = 0.4D;
         }

         if (mc.field_71439_g.field_191988_bg != 0.0F) {
            if (!mc.field_71439_g.func_70051_ag()) {
               mc.field_71439_g.func_70031_b(true);
            }

            float yaw = mc.field_71439_g.field_70177_z;
            if (mc.field_71439_g.field_191988_bg > 0.0F) {
               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F) {
                  yaw += mc.field_71439_g.field_71158_b.field_78902_a > 0.0F ? -45.0F : 45.0F;
               }

               this.forward = 1;
               mc.field_71439_g.field_191988_bg = 1.5F;
               mc.field_71439_g.field_70702_br = 1.5F;
            } else if (mc.field_71439_g.field_191988_bg < 0.0F) {
               if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F) {
                  yaw += mc.field_71439_g.field_71158_b.field_78902_a > 0.0F ? 45.0F : -45.0F;
               }

               this.forward = -1;
               mc.field_71439_g.field_191988_bg = -1.5F;
               mc.field_71439_g.field_70702_br = 1.5F;
            }

            if (mc.field_71439_g.field_70122_E) {
               float f = (float)Math.toRadians((double)yaw);
               if (this.jump.getValBoolean() && mc.field_71474_y.field_74314_A.func_151468_f()) {
                  this.Move(f);
               }
            } else {
               if (this.waitCounter < 1) {
                  ++this.waitCounter;
                  return;
               }

               this.waitCounter = 0;
               double currentSpeed = Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y);
               double speed = boost ? 1.05D : 1.025D;
               if (mc.field_71439_g.field_70181_x < 0.0D) {
                  speed = 1.0D;
               }

               double direction = Math.toRadians((double)yaw);
               mc.field_71439_g.field_70159_w = -Math.sin(direction) * speed * currentSpeed * (double)this.forward;
               mc.field_71439_g.field_70179_y = Math.cos(direction) * speed * currentSpeed * (double)this.forward;
            }
         }

         if (!this.isPlayerTryingMoveForward() && !this.isPlayerTryingStrafe()) {
            mc.field_71439_g.field_70159_w = 0.0D;
            mc.field_71439_g.field_70179_y = 0.0D;
         }
      }

      super.onClientTick(event);
   }

   private void Move(float f) {
      if (this.jump.getValBoolean()) {
         mc.field_71439_g.field_70181_x = 0.4D;
      }

      EntityPlayerSP var10000 = mc.field_71439_g;
      var10000.field_70159_w -= (double)(MathHelper.func_76126_a(f) * 0.2F) * (double)this.forward;
      var10000 = mc.field_71439_g;
      var10000.field_70179_y += (double)(MathHelper.func_76134_b(f) * 0.2F) * (double)this.forward;
   }

   public boolean isPlayerTryingMoveForward() {
      return mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d();
   }

   public boolean isPlayerTryingStrafe() {
      return mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d();
   }
}
