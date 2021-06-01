package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.util.Objects;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Ping extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting background;
   static Setting xpos;
   static Setting Shadow;
   static Setting Frame;
   static Setting ypos;
   static Setting FontSize;

   public Ping() {
      super("Ping", 0, Category.ONSCREEN, "Ping");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c / 2 + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 130.0D, -20.0D, (double)(mc.field_71440_d / 2 + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("PingSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("PingSET", false);
   }

   public static class PingRUN extends PinableFrame {
      public PingRUN() {
         super("PingSET", new String[0], (int)Ping.ypos.getValDouble(), (int)Ping.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Ping.xpos, Ping.ypos, Ping.Frame, Ping.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Ping.xpos, Ping.ypos, Ping.Frame, Ping.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         if (this.mc.field_71441_e != null && this.mc.field_71439_g != null) {
            NetworkPlayerInfo playerInfo = ((NetHandlerPlayClient)Objects.requireNonNull(this.mc.func_147114_u())).func_175102_a(this.mc.field_71439_g.func_110124_au());
            String ping = "MS: " + playerInfo.func_178853_c();
            this.fontSelect(Ping.Frame, ping, (float)this.getX(), (float)(this.getY() + 10), Ping.TextColor.getcolor(), Ping.Shadow.getValBoolean());
            if (Ping.background.getValBoolean()) {
               Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Ping.Frame, ping), this.y + 20, Ping.BgColor.getcolor());
            }

            super.onRenderGameOverlay(event);
         }
      }
   }
}
