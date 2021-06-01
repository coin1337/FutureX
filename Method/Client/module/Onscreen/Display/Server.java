package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Server extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting Shadow;
   static Setting background;
   static Setting FontSize;

   public Server() {
      super("Server", 0, Category.ONSCREEN, "Server");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 170.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("ServerSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("ServerSET", false);
   }

   public static class ServerRUN extends PinableFrame {
      public ServerRUN() {
         super("ServerSET", new String[0], (int)Server.ypos.getValDouble(), (int)Server.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Server.xpos, Server.ypos, Server.Frame, Server.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Server.xpos, Server.ypos, Server.Frame, Server.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         String brand = this.mc.func_147104_D() == null ? "Vanilla" : this.mc.func_147104_D().field_82822_g;
         this.fontSelect(Server.Frame, brand, (float)this.getX(), (float)(this.getY() + 10), Server.TextColor.getcolor(), Server.Shadow.getValBoolean());
         if (Server.background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Server.Frame, brand), this.y + 20, Server.BgColor.getcolor());
         }

         super.onRenderGameOverlay(event);
      }
   }
}
