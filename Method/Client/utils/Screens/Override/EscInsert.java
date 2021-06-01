package Method.Client.utils.Screens.Override;

import Method.Client.Main;
import Method.Client.utils.Screens.Screen;
import Method.Client.utils.system.WorldDownloader;
import Method.Client.utils.system.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;

public class EscInsert extends Screen {
   boolean Disconnect = false;
   ServerData lastserver;

   public void GuiScreenEventPre(Pre event) {
      if (event.getGui() instanceof GuiIngameMenu && event.getButton().field_146127_k == 1 && !(mc.field_71462_r instanceof GuiYesNo)) {
         mc.func_147108_a(new GuiYesNo(event.getGui(), "Disconnect", "Are you sure?", 1));
         this.Disconnect = true;
         event.setCanceled(true);
      }

   }

   public void GuiScreenEventInit(Post event) {
      if (event.getGui() instanceof GuiIngameMenu) {
         event.getButtonList().add(new GuiButton(554, event.getGui().field_146294_l / 2 - 150, event.getGui().field_146295_m / 4 + 32, 50, 20, "Relog"));
         event.getButtonList().add(new GuiButton(555, event.getGui().field_146294_l / 2 - 150, event.getGui().field_146295_m / 4 + 56, 50, 20, "Download"));
         event.getButtonList().add(new GuiButton(556, event.getGui().field_146294_l / 2 - 150, event.getGui().field_146295_m / 4 + 80, 50, 20, "ClickGui"));
      }

   }

   public void GuiScreenEventPost(net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post event) {
      if (event.getGui() instanceof GuiYesNo && this.Disconnect) {
         if (event.getButton().field_146127_k == 0) {
            this.Disconnect = false;
            mc.field_71441_e.func_72882_A();
            mc.func_71403_a((WorldClient)null);
            mc.func_147108_a(new GuiMainMenu());
         }

         if (event.getButton().field_146127_k == 1) {
            mc.func_147108_a(new GuiIngameMenu());
         }
      }

      if (event.getGui() instanceof GuiIngameMenu) {
         if (event.getButton().field_146127_k == 554 && !mc.func_71387_A()) {
            this.lastserver = mc.func_147104_D();
            this.Disconnect = false;
            mc.field_71441_e.func_72882_A();
            mc.func_71403_a((WorldClient)null);
            Wrapper.INSTANCE.mc().func_147108_a(new GuiMultiplayer(new GuiMainMenu()));
            ServerAddress serveraddress = ServerAddress.func_78860_a(this.lastserver.field_78845_b);
            mc.func_147108_a(new GuiConnecting(new GuiMainMenu(), mc, this.lastserver.field_78845_b, serveraddress.func_78864_b()));
         }

         if (event.getButton().field_146127_k == 555) {
            if (!WorldDownloader.Saving) {
               WorldDownloader.start();
            } else {
               WorldDownloader.stop();
            }
         }

         if (event.getButton().field_146127_k == 556) {
            mc.func_147108_a(Main.ClickGui);
         }
      }

   }
}
