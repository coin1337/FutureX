package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.DecimalFormat;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class NetherCords extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting background;
   static Setting xpos;
   static Setting ypos;
   static Setting Shadow;
   static Setting Decimal;
   static Setting Frame;
   static Setting FontSize;

   public NetherCords() {
      super("NetherCords", 0, Category.ONSCREEN, "NetherCords");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 1.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c / 2 + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 120.0D, -20.0D, (double)(mc.field_71440_d / 2 + 40), true));
      Main.setmgr.add(Decimal = new Setting("Decimal", this, 2.0D, 0.0D, 5.0D, true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
   }

   public void onEnable() {
      PinableFrame.Toggle("NetherCordsSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("NetherCordsSET", false);
   }

   public static class NetherCordsRUN extends PinableFrame {
      DecimalFormat decimalFormat = new DecimalFormat("0.00");

      public NetherCordsRUN() {
         super("NetherCordsSET", new String[0], (int)NetherCords.ypos.getValDouble(), (int)NetherCords.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, NetherCords.xpos, NetherCords.ypos, NetherCords.Frame, NetherCords.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, NetherCords.xpos, NetherCords.ypos, NetherCords.Frame, NetherCords.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         this.fontSelect(NetherCords.Frame, this.getCoords(), (float)this.getX(), (float)(this.getY() + 10), NetherCords.TextColor.getcolor(), NetherCords.Shadow.getValBoolean());
         if (NetherCords.background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(NetherCords.Frame, this.getCoords()), this.y + 20, NetherCords.BgColor.getcolor());
         }

         super.onRenderGameOverlay(event);
      }

      private String getCoords() {
         this.decimalFormat = this.getDecimalFormat((int)NetherCords.Decimal.getValDouble());
         String x = this.decimalFormat.format(this.mc.field_71439_g.field_70165_t / 8.0D);
         String y = this.decimalFormat.format(this.mc.field_71439_g.field_70163_u);
         String z = this.decimalFormat.format(this.mc.field_71439_g.field_70161_v / 8.0D);
         String coords = x + ", " + y + ", " + z + ChatFormatting.GRAY;
         return coords;
      }
   }
}
