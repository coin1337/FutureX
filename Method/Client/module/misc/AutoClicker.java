package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoClicker extends Module {
   Setting Type;
   Setting Delay;
   Setting Hold;
   private final TimerUtils timer;

   public AutoClicker() {
      super("AutoClicker", 0, Category.MISC, "Auto Clicker");
      this.Type = Main.setmgr.add(new Setting("Click Mode", this, "Left", new String[]{"Left", "Right"}));
      this.Delay = Main.setmgr.add(new Setting("Delay", this, 0.2D, 0.0D, 5.0D, false));
      this.Hold = Main.setmgr.add(new Setting("Hold", this, false));
      this.timer = new TimerUtils();
   }

   public void onClientTick(ClientTickEvent event) {
      if (!this.Hold.getValBoolean()) {
         mc.field_71474_y.field_74312_F.field_74513_e = false;
         mc.field_71474_y.field_74313_G.field_74513_e = false;
      }

      if (this.Type.getValString().equalsIgnoreCase("Left") && this.timer.isDelay((long)this.Delay.getValDouble() * 1000L)) {
         mc.field_71474_y.field_74312_F.field_74513_e = true;
         this.timer.setLastMS();
      }

      if (this.Type.getValString().equalsIgnoreCase("Right") && this.timer.isDelay((long)this.Delay.getValDouble() * 1000L)) {
         mc.field_71474_y.field_74313_G.field_74513_e = true;
         this.timer.setLastMS();
      }

   }

   public void onDisable() {
      mc.field_71474_y.field_74312_F.field_74513_e = false;
      mc.field_71474_y.field_74313_G.field_74513_e = false;
   }
}
