package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Time extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting Worldtime;
   static Setting GameTime;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting Shadow;
   static Setting background;
   static Setting FontSize;

   public Time() {
      super("Time", 0, Category.ONSCREEN, "Time");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Worldtime = new Setting("Worldtime", this, true));
      Main.setmgr.add(GameTime = new Setting("GameTime", this, false));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c / 2 + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 190.0D, -20.0D, (double)(mc.field_71440_d / 2 + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("TimeSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("TimeSET", false);
   }

   public static class TimeRUN extends PinableFrame {
      public TimeRUN() {
         super("TimeSET", new String[0], (int)Time.ypos.getValDouble(), (int)Time.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Time.xpos, Time.ypos, Time.Frame, Time.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Time.xpos, Time.ypos, Time.Frame, Time.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         String time = "";
         if (Time.Worldtime.getValBoolean()) {
            time = (new SimpleDateFormat("h:mm a")).format(new Date()) + "\n  ";
         }

         if (Time.GameTime.getValBoolean()) {
            time = time + this.mc.field_71441_e.func_72820_D();
         }

         this.fontSelect(Time.Frame, time, (float)this.getX(), (float)(this.getY() + 10), Time.TextColor.getcolor(), Time.Shadow.getValBoolean());
         if (Time.background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Time.Frame, time), this.y + 20, Time.BgColor.getcolor());
         }

         super.onRenderGameOverlay(event);
      }
   }
}
