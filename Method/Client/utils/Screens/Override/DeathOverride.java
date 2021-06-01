package Method.Client.utils.Screens.Override;

import Method.Client.module.ModuleManager;
import Method.Client.module.misc.Ghost;
import Method.Client.utils.Screens.Screen;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;

public class DeathOverride extends Screen {
   public static boolean isDead;
   public static boolean Override = false;

   public void GuiOpen(GuiOpenEvent event) {
      boolean host = ModuleManager.getModuleByName("Ghost").isToggled();
      if (host && event.getGui() instanceof GuiGameOver) {
         event.setGui((GuiScreen)null);
         isDead = true;
         mc.field_71439_g.func_70606_j((float)Ghost.health.getValDouble() / 2.0F);
      }

   }
}
