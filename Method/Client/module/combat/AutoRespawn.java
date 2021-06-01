package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoRespawn extends Module {
   Setting DeathCoords;
   Setting Delay;
   private TimerUtils timer;
   boolean canrespawn;

   public AutoRespawn() {
      super("AutoRespawn", 0, Category.COMBAT, "AutoRespawn");
      this.DeathCoords = Main.setmgr.add(new Setting("DeathCoords", this, true));
      this.Delay = Main.setmgr.add(new Setting("Delay", this, 2.0D, 0.0D, 50.0D, true));
      this.timer = new TimerUtils();
      this.canrespawn = false;
   }

   public void onClientTick(ClientTickEvent event) {
      if (Wrapper.mc.field_71462_r instanceof GuiGameOver) {
         if (!this.canrespawn) {
            this.timer.reset();
            this.canrespawn = true;
         }

         if (this.timer.hasReached((float)(this.Delay.getValDouble() * 1000.0D))) {
            this.timer.reset();
            mc.field_71439_g.func_71004_bE();
            Wrapper.mc.func_147108_a((GuiScreen)null);
            if (this.DeathCoords.getValBoolean()) {
               ChatUtils.message(String.format("you have died at x %d y %d z %d", (int)mc.field_71439_g.field_70165_t, (int)mc.field_71439_g.field_70163_u, (int)mc.field_71439_g.field_70161_v));
            }

            this.canrespawn = false;
         }
      }

   }
}
