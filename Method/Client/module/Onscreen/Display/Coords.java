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

public final class Coords extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting background;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting Decimal;
   static Setting Shadow;
   static Setting FontSize;

   public Coords() {
      super("Coords", 0, Category.ONSCREEN, "Coords");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BgColor", this, 0.22D, 0.88D, 0.22D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c / 2 + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 30.0D, -20.0D, (double)(mc.field_71440_d / 2 + 40), true));
      Main.setmgr.add(Decimal = new Setting("Decimal", this, 2.0D, 0.0D, 5.0D, true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
   }

   public void onEnable() {
      PinableFrame.Toggle("CoordsSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("CoordsSET", false);
   }

   public static class CoordsRUN extends PinableFrame {
      DecimalFormat decimalFormat = new DecimalFormat("0.00");

      public CoordsRUN() {
         super("CoordsSET", new String[0], (int)Coords.ypos.getValDouble(), (int)Coords.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Coords.xpos, Coords.ypos, Coords.Frame, Coords.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Coords.xpos, Coords.ypos, Coords.Frame, Coords.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         if (Coords.background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Coords.Frame, this.getCoords()), this.y + 20, Coords.background.getcolor());
         }

         this.fontSelect(Coords.Frame, this.getCoords(), (float)this.getX(), (float)(this.getY() + 10), Coords.TextColor.getcolor(), Coords.Shadow.getValBoolean());
         super.onRenderGameOverlay(event);
      }

      private String getCoords() {
         this.decimalFormat = this.getDecimalFormat((int)Coords.Decimal.getValDouble());
         String x = this.decimalFormat.format(this.mc.field_71439_g.field_70165_t);
         String y = this.decimalFormat.format(this.mc.field_71439_g.field_70163_u);
         String z = this.decimalFormat.format(this.mc.field_71439_g.field_70161_v);
         String coords = x + ", " + y + ", " + z + ChatFormatting.GRAY;
         return coords;
      }
   }
}
