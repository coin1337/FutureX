package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.text.DecimalFormat;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Angles extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting background;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting Decimal;
   static Setting Shadow;
   static Setting FontSize;
   static Setting TrueYAW;
   static Setting Names;

   public Angles() {
      super("Angles", 0, Category.ONSCREEN, "Angles");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TrueYAW = new Setting("TrueYAW", this, false));
      Main.setmgr.add(Names = new Setting("Names", this, true));
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BgColor", this, 0.22D, 0.88D, 0.22D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 220.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
      Main.setmgr.add(Decimal = new Setting("Decimal", this, 2.0D, 0.0D, 5.0D, true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
   }

   public void onEnable() {
      PinableFrame.Toggle("AnglesSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("AnglesSET", false);
   }

   public static class AnglesRUN extends PinableFrame {
      DecimalFormat decimalFormat = new DecimalFormat("0.00");

      public AnglesRUN() {
         super("AnglesSET", new String[0], (int)Angles.ypos.getValDouble(), (int)Angles.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Angles.xpos, Angles.ypos, Angles.Frame, Angles.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Angles.xpos, Angles.ypos, Angles.Frame, Angles.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         if (Angles.background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Angles.Frame, this.getCoords()), this.y + 20, Angles.background.getcolor());
         }

         this.fontSelect(Angles.Frame, this.getCoords(), (float)this.getX(), (float)(this.getY() + 10), Angles.TextColor.getcolor(), Angles.Shadow.getValBoolean());
         if (Angles.background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Angles.Frame, this.getCoords()), this.y + 22, Angles.BgColor.getcolor());
         }

         super.onRenderGameOverlay(event);
      }

      private String getCoords() {
         this.decimalFormat = this.getDecimalFormat((int)Angles.Decimal.getValDouble());
         String Pitch = this.decimalFormat.format((double)this.mc.field_71439_g.field_70125_A);
         String Yaw;
         if (!Angles.TrueYAW.getValBoolean()) {
            Yaw = this.decimalFormat.format((double)(this.mc.field_71439_g.field_70177_z % 360.0F));
         } else {
            Yaw = this.decimalFormat.format((double)this.mc.field_71439_g.field_70177_z);
         }

         return Angles.Names.getValBoolean() ? "Pitch " + Pitch + ", Yaw " + Yaw : Pitch + ", " + Yaw;
      }
   }
}
