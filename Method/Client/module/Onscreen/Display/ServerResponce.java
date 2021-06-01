package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.system.Connection;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class ServerResponce extends Module {
   static Setting Delay;
   static Setting TextColor;
   static Setting BgColor;
   static Setting xpos;
   static Setting ypos;
   static Setting Shadow;
   static Setting Frame;
   static Setting FontSize;
   static Setting Background;
   private static long serverLastUpdated = 0L;

   public ServerResponce() {
      super("ServerResponce", 0, Category.ONSCREEN, "ServerResponce");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(Delay = new Setting("Delay", this, 1.0D, 1.0D, 10.0D, true));
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(Background = new Setting("Background", this, false));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c / 2 + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 180.0D, -20.0D, (double)(mc.field_71440_d / 2 + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("ServerResponceSET", true);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.IN) {
         serverLastUpdated = System.currentTimeMillis();
      }

      return true;
   }

   public void onDisable() {
      PinableFrame.Toggle("ServerResponceSET", false);
   }

   public static class ServerResponceRUN extends PinableFrame {
      String text = "Server Not Responding! ";
      private static long startTime = 0L;

      public ServerResponceRUN() {
         super("ServerResponceSET", new String[0], (int)ServerResponce.ypos.getValDouble(), (int)ServerResponce.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, ServerResponce.xpos, ServerResponce.ypos, ServerResponce.Frame, ServerResponce.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, ServerResponce.xpos, ServerResponce.ypos, ServerResponce.Frame, ServerResponce.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         if (!this.mc.func_71356_B()) {
            if (this.mc.field_71462_r == null || this.mc.field_71462_r instanceof GuiChat) {
               if (ServerResponce.Delay.getValDouble() * 1000.0D <= (double)(System.currentTimeMillis() - ServerResponce.serverLastUpdated)) {
                  if (this.shouldPing()) {
                     if (isDown("1.1.1.1", 80, 1111)) {
                        this.text = "Your internet is offline! ";
                     } else {
                        this.text = "Server Not Responding! ";
                     }
                  }

                  this.text = this.text.replaceAll("! .*", "! " + this.timeDifference() + "s");
                  if (ServerResponce.Background.getValBoolean()) {
                     Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(ServerResponce.Frame, this.text), this.y + 20, ServerResponce.BgColor.getcolor());
                  }

                  this.fontSelect(ServerResponce.Frame, this.text, (float)this.getX(), (float)(this.getY() + 10), ServerResponce.TextColor.getcolor(), ServerResponce.Shadow.getValBoolean());
                  super.onRenderGameOverlay(event);
               }
            }
         }
      }

      private double timeDifference() {
         return (double)(System.currentTimeMillis() - ServerResponce.serverLastUpdated) / 1000.0D;
      }

      public static boolean isDown(String host, int port, int timeout) {
         try {
            Socket socket = new Socket();
            Throwable var4 = null;

            boolean var5;
            try {
               socket.connect(new InetSocketAddress(host, port), timeout);
               var5 = false;
            } catch (Throwable var15) {
               var4 = var15;
               throw var15;
            } finally {
               if (socket != null) {
                  if (var4 != null) {
                     try {
                        socket.close();
                     } catch (Throwable var14) {
                        var4.addSuppressed(var14);
                     }
                  } else {
                     socket.close();
                  }
               }

            }

            return var5;
         } catch (IOException var17) {
            return true;
         }
      }

      private boolean shouldPing() {
         if (startTime == 0L) {
            startTime = System.currentTimeMillis();
         }

         if (startTime + 1000L <= System.currentTimeMillis()) {
            startTime = System.currentTimeMillis();
            return true;
         } else {
            return false;
         }
      }
   }
}
