package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Hunger extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting Background;
   static Setting Shadow;
   static Setting FontSize;

   public Hunger() {
      super("Hunger", 0, Category.ONSCREEN, "Hunger");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Background = new Setting("Background", this, false));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 100.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("HungerSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("HungerSET", false);
   }

   public static class HungerRUN extends PinableFrame {
      public static Field foodExhaustionLevel;

      public HungerRUN() {
         super("HungerSET", new String[0], (int)Hunger.ypos.getValDouble(), (int)Hunger.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Hunger.xpos, Hunger.ypos, Hunger.Frame, Hunger.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Hunger.xpos, Hunger.ypos, Hunger.Frame, Hunger.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         DecimalFormat dc = new DecimalFormat("#.##");
         String cow = "Hunger: " + this.mc.field_71439_g.func_71024_bL().func_75116_a() + " Saturation: " + dc.format((double)this.mc.field_71439_g.func_71024_bL().func_75115_e());
         this.fontSelect(Hunger.Frame, cow, (float)this.getX(), (float)(this.getY() + 10), Hunger.TextColor.getcolor(), Hunger.Shadow.getValBoolean());
         if (Hunger.Background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Hunger.Frame, cow), this.y + 20, Hunger.BgColor.getcolor());
         }

         super.onRenderGameOverlay(event);
      }
   }
}
