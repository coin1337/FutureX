package Method.Client.utils.Screens.Override;

import Method.Client.utils.Screens.Screen;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent.Unload;

public class DisconnectedInsert extends Screen {
   public static ServerData lastserver;
   public static boolean Autolog = false;
   public static boolean doPing = false;
   public static boolean Connect = false;
   public static int ping = 0;

   public void onWorldUnload(Unload event) {
      ServerData data = mc.func_147104_D();
      if (data != null) {
         lastserver = data;
      }

   }

   public void GuiScreenEventPost(Post event) {
      if (event.getGui() instanceof GuiDisconnected) {
         if (event.getButton().field_146127_k == 997) {
            doPing = true;
         }

         if (event.getButton().field_146127_k == 999) {
            ServerAddress serveraddress = ServerAddress.func_78860_a(lastserver.field_78845_b);
            mc.func_147108_a(new GuiConnecting(new GuiMainMenu(), mc, lastserver.field_78845_b, serveraddress.func_78864_b()));
         }

         if (event.getButton().field_146127_k == 998) {
            Autolog = !Autolog;
            double Starttime = (double)(System.currentTimeMillis() + 5000L);
            (new Thread(() -> {
               while(true) {
                  try {
                     if (Starttime >= (double)System.currentTimeMillis() && Autolog) {
                        Connect = false;
                        event.getButton().field_146126_j = "Auto " + TextFormatting.GOLD + (Starttime - (double)System.currentTimeMillis()) + TextFormatting.RESET + "ms Relog";
                        continue;
                     }

                     Connect = true;
                  } catch (Exception var4) {
                  }

                  return;
               }
            })).start();
         }
      }

   }

   public void DrawScreenEvent(DrawScreenEvent event) {
      if (event.getGui() instanceof GuiDisconnected) {
         ServerAddress serveraddress;
         if (Autolog && Connect) {
            Connect = false;
            serveraddress = ServerAddress.func_78860_a(lastserver.field_78845_b);
            mc.func_147108_a(new GuiConnecting(new GuiMainMenu(), mc, lastserver.field_78845_b, serveraddress.func_78864_b()));
         }

         serveraddress = ServerAddress.func_78860_a(lastserver.field_78845_b);
         event.getGui().func_73732_a(event.getGui().field_146289_q, lastserver.field_78845_b + " Port: " + serveraddress.func_78864_b(), event.getGui().field_146294_l / 2, event.getGui().field_146295_m / 2 - 50, 11184810);
         event.getGui().func_73732_a(event.getGui().field_146289_q, "Ping: " + ping, event.getGui().field_146294_l / 2, event.getGui().field_146295_m / 2 - 65, 11184810);
         if (doPing) {
            doPing = false;

            try {
               String hostAddress = lastserver.field_78845_b;
               int port = serveraddress.func_78864_b();
               long timeToRespond = 0L;
               InetAddress inetAddress = InetAddress.getByName(hostAddress);
               InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
               SocketChannel sc = SocketChannel.open();
               sc.configureBlocking(true);
               Date start = new Date();
               if (sc.connect(socketAddress)) {
                  Date stop = new Date();
                  timeToRespond = stop.getTime() - start.getTime();
               }

               ping = (int)timeToRespond;
            } catch (IOException var12) {
            }
         }
      }

   }

   public void GuiScreenEventInit(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post event) {
      if (event.getGui() instanceof GuiConnecting) {
         lastserver = mc.func_147104_D();
      }

      if (event.getGui() instanceof GuiDisconnected) {
         event.getButtonList().add(new GuiButton(999, event.getGui().field_146294_l / 2 - 100, Math.min(event.getGui().field_146295_m / 2 + 10 + event.getGui().field_146289_q.field_78288_b, event.getGui().field_146295_m - 30) + 20, 200, 20, "Relog"));
         event.getButtonList().add(new GuiButton(998, event.getGui().field_146294_l / 2 - 100, Math.min(event.getGui().field_146295_m / 2 + 10 + event.getGui().field_146289_q.field_78288_b, event.getGui().field_146295_m - 30) + 40, 200, 20, "Auto 5s Relog"));
         event.getButtonList().add(new GuiButton(997, event.getGui().field_146294_l / 2 - 100, Math.min(event.getGui().field_146295_m / 2 + 10 + event.getGui().field_146289_q.field_78288_b, event.getGui().field_146295_m - 30) + 60, 200, 20, "Ping"));
         if (Autolog) {
            Autolog = false;
            GuiButton button = null;

            try {
               Iterator var3 = event.getButtonList().iterator();

               while(var3.hasNext()) {
                  GuiButton guiButton = (GuiButton)var3.next();
                  if (guiButton.field_146127_k == 998) {
                     button = guiButton;
                     ((GuiDisconnected)event.getGui()).func_146284_a(guiButton);
                  }
               }
            } catch (IOException var5) {
               var5.printStackTrace();
            }

            MinecraftForge.EVENT_BUS.post(new Post(event.getGui(), button, event.getGui().field_146292_n));
         }
      }

   }
}
