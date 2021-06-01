package Method.Client.utils.Screens.Override;

import Method.Client.utils.Screens.Screen;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;

public class ConnectingInsert extends Screen {
   boolean IsguiConnecting = false;
   double starttime;
   public static ServerData Currentserver;

   public void DrawScreenEvent(DrawScreenEvent event) {
      if (event.getGui() instanceof GuiConnecting && this.IsguiConnecting) {
         ServerAddress serveraddress = ServerAddress.func_78860_a(Currentserver.field_78845_b);
         event.getGui().func_73732_a(event.getGui().field_146289_q, "Connecting to: " + Currentserver.field_78845_b + " Port: " + serveraddress.func_78864_b(), event.getGui().field_146294_l / 2, event.getGui().field_146295_m / 2 - 10, 11184810);
         event.getGui().func_73732_a(event.getGui().field_146289_q, "Time Taken: " + ChatFormatting.GOLD + ((double)System.currentTimeMillis() - this.starttime) + ChatFormatting.RESET + " ms", event.getGui().field_146294_l / 2, event.getGui().field_146295_m / 2 - 30, 11184810);
      }

   }

   public void GuiScreenEventInit(Post event) {
      if (event.getGui() instanceof GuiConnecting) {
         this.starttime = (double)System.currentTimeMillis();
         this.IsguiConnecting = true;
         Currentserver = mc.func_147104_D();
      } else {
         this.IsguiConnecting = false;
      }

   }
}
