package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Sprint extends Module {
   Setting Backwards;
   Setting Foodcheck;
   Setting ObstacleCheck;
   Setting InstarunTimer;
   Setting Fastspeedup;
   double quicktimerrun;
   boolean startedquickrun;

   public Sprint() {
      super("Sprint", 0, Category.MOVEMENT, "Always be running");
      this.Backwards = Main.setmgr.add(new Setting("Backwards", this, false));
      this.Foodcheck = Main.setmgr.add(new Setting("Food check", this, true));
      this.ObstacleCheck = Main.setmgr.add(new Setting("Obstical Check", this, true));
      this.InstarunTimer = Main.setmgr.add(new Setting("InstarunTimer", this, false, this.ObstacleCheck, 3));
      this.Fastspeedup = Main.setmgr.add(new Setting("Fastspeedup", this, 10.0D, 2.0D, 40.0D, true, this.ObstacleCheck, 4));
      this.quicktimerrun = 10.0D;
      this.startedquickrun = false;
   }

   private void setTickLength(float tickLength) {
      mc.field_71428_T.field_194149_e = 1.0F * tickLength;
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71439_g.func_71024_bL().func_75116_a() > 6 || !this.Foodcheck.getValBoolean()) {
         if (this.ObstacleCheck.getValBoolean()) {
            if (this.canSprint()) {
               mc.field_71439_g.func_70031_b(true);
            }
         } else {
            mc.field_71439_g.func_70031_b(true);
         }

         if (mc.field_71439_g.func_70051_ag()) {
            if (this.InstarunTimer.getValBoolean() && this.quicktimerrun > 0.0D) {
               this.startedquickrun = true;
               this.setTickLength(33.333F);
               --this.quicktimerrun;
               if (this.quicktimerrun < 1.0D) {
                  this.setTickLength(50.0F);
               }
            }
         } else {
            if (this.startedquickrun) {
               this.startedquickrun = false;
               this.setTickLength(50.0F);
            }

            this.quicktimerrun = this.Fastspeedup.getValDouble();
         }

         if (this.Backwards.getValBoolean() && !mc.field_71439_g.func_184613_cA() && Wrapper.mc.field_71474_y.field_74368_y.func_151470_d()) {
            if (mc.field_71439_g.field_191988_bg > 0.0F && !mc.field_71439_g.field_70123_F) {
               mc.field_71439_g.func_70031_b(true);
            }

            if (mc.field_71439_g.field_70122_E) {
               EntityPlayerSP var10000 = mc.field_71439_g;
               var10000.field_70159_w *= 1.092D;
               var10000 = mc.field_71439_g;
               var10000.field_70179_y *= 1.092D;
            }

            double sqrt = Math.sqrt(Math.pow(mc.field_71439_g.field_70159_w, 2.0D) + Math.pow(mc.field_71439_g.field_70179_y, 2.0D));
            double n = 0.6500000262260437D;
            if (sqrt > 0.6500000262260437D) {
               mc.field_71439_g.field_70159_w = mc.field_71439_g.field_70159_w / sqrt * 0.6500000262260437D;
               mc.field_71439_g.field_70179_y = mc.field_71439_g.field_70179_y / sqrt * 0.6500000262260437D;
            }
         }
      }

      super.onClientTick(event);
   }

   boolean canSprint() {
      if (!mc.field_71439_g.field_70122_E) {
         return false;
      } else if (mc.field_71439_g.func_70051_ag()) {
         return false;
      } else if (mc.field_71439_g.func_70617_f_()) {
         return false;
      } else if (mc.field_71439_g.func_70090_H()) {
         return false;
      } else if (mc.field_71439_g.func_180799_ab()) {
         return false;
      } else if (mc.field_71439_g.field_70123_F) {
         return false;
      } else if (mc.field_71439_g.field_191988_bg < 0.1F) {
         return false;
      } else if (mc.field_71439_g.func_70093_af()) {
         return false;
      } else if (mc.field_71439_g.func_71024_bL().func_75116_a() < 6) {
         return false;
      } else if (mc.field_71439_g.func_184218_aH()) {
         return false;
      } else {
         return !mc.field_71439_g.func_70644_a(MobEffects.field_76440_q);
      }
   }
}
