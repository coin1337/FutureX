package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import java.util.Random;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Timer extends Module {
   public Setting Speed;
   public Setting OnMove;
   public Setting mode;
   public Setting RandomTiming;
   TimerUtils timer;
   public boolean switcheraro;
   Random randomno;

   public Timer() {
      super("Timer", 0, Category.PLAYER, "Timer");
      this.Speed = Main.setmgr.add(new Setting("Speed", this, 2.0D, 0.1D, 5.0D, false));
      this.OnMove = Main.setmgr.add(new Setting("OnMove", this, true));
      this.mode = Main.setmgr.add(new Setting("Timer Mode", this, "Vanilla", new String[]{"Vanilla", "Even", "Odd", "Random", "PerSec"}));
      this.RandomTiming = Main.setmgr.add(new Setting("Time per sec", this, 0.5D, 0.0D, 5.0D, false, this.mode, "PerSec", 3));
      this.timer = new TimerUtils();
      this.switcheraro = false;
      this.randomno = new Random();
   }

   public void onDisable() {
      this.setTickLength(50.0F);
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Vanilla")) {
         if (!this.OnMove.getValBoolean()) {
            this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
         }

         if (this.OnMove.getValBoolean() && Utils.isMoving(mc.field_71439_g)) {
            this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Random")) {
         if (this.randomno.nextBoolean()) {
            if (!this.OnMove.getValBoolean()) {
               this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
            }

            if (this.OnMove.getValBoolean() && Utils.isMoving(mc.field_71439_g)) {
               this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
            }
         } else {
            this.setTickLength(50.0F);
         }
      } else if (this.mode.getValString().equalsIgnoreCase("PerSec") && this.timer.isDelay((long)(this.RandomTiming.getValDouble() * 1000.0D))) {
         this.switcheraro = !this.switcheraro;
         if (this.switcheraro) {
            if (!this.OnMove.getValBoolean()) {
               this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
            }

            if (this.OnMove.getValBoolean() && Utils.isMoving(mc.field_71439_g)) {
               this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
            }
         } else {
            this.setTickLength(50.0F);
         }

         this.timer.setLastMS();
      }

      if (this.mode.getValString().equalsIgnoreCase("Even")) {
         if (mc.field_71439_g.field_70173_aa % 2 == 0) {
            if (!this.OnMove.getValBoolean()) {
               this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
            }

            if (this.OnMove.getValBoolean() && Utils.isMoving(mc.field_71439_g)) {
               this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
            }
         } else {
            this.setTickLength(50.0F);
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Odd")) {
         if (mc.field_71439_g.field_70173_aa % 2 != 0) {
            if (!this.OnMove.getValBoolean()) {
               this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
            }

            if (this.OnMove.getValBoolean() && Utils.isMoving(mc.field_71439_g)) {
               this.setTickLength((float)(50.0D / this.Speed.getValDouble()));
            }
         } else {
            this.setTickLength(50.0F);
         }
      }

      if (this.OnMove.getValBoolean() && !Utils.isMoving(mc.field_71439_g)) {
         this.setTickLength(50.0F);
      }

   }

   private void setTickLength(float tickLength) {
      mc.field_71428_T.field_194149_e = 1.0F * tickLength;
   }
}
