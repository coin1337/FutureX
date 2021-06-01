package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.text.DecimalFormat;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class PlayerSpeed extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting background;
   static Setting Shadow;
   static Setting Frame;
   static Setting xpos;
   static Setting ypos;
   static Setting Decimal;
   static Setting FontSize;
   static Setting MotionX;
   static Setting MotionY;
   static Setting MotionZ;

   public PlayerSpeed() {
      super("PlayerSpeed", 0, Category.ONSCREEN, "PlayerSpeed");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(MotionX = new Setting("MotionX", this, false));
      Main.setmgr.add(MotionY = new Setting("MotionY", this, false));
      Main.setmgr.add(MotionZ = new Setting("MotionZ", this, false));
      Main.setmgr.add(Decimal = new Setting("Decimal", this, 2.0D, 0.0D, 5.0D, true));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 150.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
   }

   public void onEnable() {
      PinableFrame.Toggle("SpeedSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("SpeedSET", false);
   }

   public static class SpeedRUN extends PinableFrame {
      DecimalFormat decimalFormat = new DecimalFormat("0.00");

      public SpeedRUN() {
         super("SpeedSET", new String[0], (int)PlayerSpeed.ypos.getValDouble(), (int)PlayerSpeed.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, PlayerSpeed.xpos, PlayerSpeed.ypos, PlayerSpeed.Frame, PlayerSpeed.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, PlayerSpeed.xpos, PlayerSpeed.ypos, PlayerSpeed.Frame, PlayerSpeed.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         this.decimalFormat = this.getDecimalFormat((int)PlayerSpeed.Decimal.getValDouble());
         double deltaX = this.mc.field_71439_g.field_70165_t - this.mc.field_71439_g.field_70169_q;
         double deltaZ = this.mc.field_71439_g.field_70161_v - this.mc.field_71439_g.field_70166_s;
         float tickRate = this.mc.field_71428_T.field_194149_e / 1000.0F;
         String bps = "BPS: " + this.decimalFormat.format((double)(MathHelper.func_76133_a(deltaX * deltaX + deltaZ * deltaZ) / tickRate));
         if (PlayerSpeed.MotionX.getValBoolean()) {
            bps = bps + " MotionX: " + this.decimalFormat.format(this.mc.field_71439_g.field_70159_w);
         }

         if (PlayerSpeed.MotionY.getValBoolean()) {
            bps = bps + " MotionY: " + this.decimalFormat.format(this.mc.field_71439_g.field_70181_x);
         }

         if (PlayerSpeed.MotionZ.getValBoolean()) {
            bps = bps + " MotionZ: " + this.decimalFormat.format(this.mc.field_71439_g.field_70179_y);
         }

         this.fontSelect(PlayerSpeed.Frame, bps, (float)this.getX(), (float)(this.getY() + 10), PlayerSpeed.TextColor.getcolor(), PlayerSpeed.Shadow.getValBoolean());
         if (PlayerSpeed.background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(PlayerSpeed.Frame, bps), this.y + 20, PlayerSpeed.BgColor.getcolor());
         }

         super.onRenderGameOverlay(event);
      }
   }
}
