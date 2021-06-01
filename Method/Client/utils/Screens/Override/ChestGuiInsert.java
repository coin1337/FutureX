package Method.Client.utils.Screens.Override;

import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.player.ChestStealer;
import Method.Client.utils.Screens.Screen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post;

public class ChestGuiInsert extends Screen {
   public void GuiScreenEventPost(Post event) {
      if (event.getGui() instanceof GuiChest) {
         if (event.getButton().field_146127_k == 11209) {
            ChestStealer.Mode.setValString("Steal");
            toggle2();
         }

         if (event.getButton().field_146127_k == 11210) {
            ChestStealer.Mode.setValString("Store");
            toggle2();
         }

         if (event.getButton().field_146127_k == 11211) {
            ChestStealer.Mode.setValString("Drop");
            toggle2();
         }
      }

   }

   public void GuiScreenEventInit(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post event) {
      if (event.getGui() instanceof GuiChest) {
         event.getButtonList().add(new GuiButton(11209, event.getGui().field_146294_l / 2 + 100, event.getGui().field_146295_m / 2 - ((GuiChest)event.getGui()).getYSize() + 110, 50, 20, "Steal"));
         event.getButtonList().add(new GuiButton(11210, event.getGui().field_146294_l / 2 + 100, event.getGui().field_146295_m / 2 - ((GuiChest)event.getGui()).getYSize() + 130, 50, 20, "Store"));
         event.getButtonList().add(new GuiButton(11211, event.getGui().field_146294_l / 2 + 100, event.getGui().field_146295_m / 2 - ((GuiChest)event.getGui()).getYSize() + 150, 50, 20, "Drop"));
      }

   }

   private static void toggle2() {
      Module Chest = ModuleManager.getModuleByName("ChestStealer");
      if (Chest.isToggled()) {
         Chest.toggle();
      }

      Chest.toggle();
   }
}
