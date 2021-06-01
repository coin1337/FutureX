package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FastFall extends Module {
   Setting speed;
   Setting timer;

   public FastFall() {
      super("FastFall", 0, Category.MOVEMENT, "Fast Fall");
      this.speed = Main.setmgr.add(new Setting("Speed", this, 0.1D, 0.1D, 4.0D, false));
      this.timer = Main.setmgr.add(new Setting("timer", this, false));
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.timer.getValBoolean() && mc.field_71439_g.field_70122_E) {
         this.setTickLength(50.0F);
      }

      if (!mc.field_71439_g.func_184613_cA() && !mc.field_71439_g.field_71075_bZ.field_75100_b) {
         boolean b = !mc.field_71441_e.func_175623_d(mc.field_71439_g.func_180425_c().func_177982_a(0, -1, 0)) || !mc.field_71441_e.func_175623_d(mc.field_71439_g.func_180425_c().func_177982_a(0, -2, 0));
         if (!mc.field_71439_g.field_70122_E && !b) {
            if (this.timer.getValBoolean() && !mc.field_71439_g.field_70122_E) {
               this.setTickLength((float)(50.0D / this.speed.getValDouble()));
            } else {
               mc.field_71439_g.field_70181_x = -this.speed.getValDouble();
            }
         }

      }
   }

   private void setTickLength(float tickLength) {
      mc.field_71428_T.field_194149_e = 1.0F * tickLength;
   }

   public void onDisable() {
      this.setTickLength(50.0F);
   }
}
