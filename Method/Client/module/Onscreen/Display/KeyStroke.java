package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class KeyStroke extends Module {
   static Setting TextColor;
   static Setting Presscolor;
   static Setting BgColor;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting Background;
   static Setting Shadow;
   static Setting Clicks;
   static Setting ClicksPerSec;
   static Setting FontSize;

   public KeyStroke() {
      super("KeyStroke", 0, Category.ONSCREEN, "KeyStroke");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.3D, 0.4D, 0.4D, 1.0D));
      Main.setmgr.add(Presscolor = new Setting("Presscolor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.5D, 0.3D, 0.22D));
      Main.setmgr.add(Background = new Setting("Background", this, false));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(Clicks = new Setting("Clicks", this, false));
      Main.setmgr.add(ClicksPerSec = new Setting("ClicksPerSec", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c / 2 + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 220.0D, -20.0D, (double)(mc.field_71440_d / 2 + 250), true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
   }

   public void onEnable() {
      PinableFrame.Toggle("KeyStrokeSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("KeyStrokeSET", false);
   }

   public static class KeyStrokeRUN extends PinableFrame {
      ArrayList<Double> clicks = new ArrayList();
      boolean startclick = false;

      public KeyStrokeRUN() {
         super("KeyStrokeSET", new String[0], (int)KeyStroke.ypos.getValDouble(), (int)KeyStroke.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, KeyStroke.xpos, KeyStroke.ypos, KeyStroke.Frame, KeyStroke.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, KeyStroke.xpos, KeyStroke.ypos, KeyStroke.Frame, KeyStroke.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         if (this.mc.field_71474_y.field_74312_F.field_74513_e) {
            this.startclick = true;
         } else if (this.startclick) {
            this.startclick = false;
            this.clicks.add((double)System.currentTimeMillis());
         }

         ArrayList<Double> rem = new ArrayList();
         Iterator var3 = this.clicks.iterator();

         while(var3.hasNext()) {
            Double click = (Double)var3.next();
            if (click + 1000.0D < (double)System.currentTimeMillis()) {
               rem.add(click);
            }
         }

         this.clicks.removeAll(rem);
         int black = (new Color(0, 0, 0, 40)).getRGB();
         int rain = KeyStroke.TextColor.getcolor();
         int white = KeyStroke.Presscolor.getcolor();
         this.fontSelect(KeyStroke.Frame, this.mc.field_71474_y.field_74351_w.getDisplayName(), (float)(this.x + 18), (float)this.y, this.mc.field_71474_y.field_74351_w.field_74513_e ? white : rain, KeyStroke.Shadow.getValBoolean());
         this.fontSelect(KeyStroke.Frame, this.mc.field_71474_y.field_74370_x.getDisplayName(), (float)this.x, (float)(this.y + 20), this.mc.field_71474_y.field_74370_x.field_74513_e ? white : rain, KeyStroke.Shadow.getValBoolean());
         this.fontSelect(KeyStroke.Frame, this.mc.field_71474_y.field_74368_y.getDisplayName(), (float)(this.x + 20), (float)(this.y + 20), this.mc.field_71474_y.field_74368_y.field_74513_e ? white : rain, KeyStroke.Shadow.getValBoolean());
         this.fontSelect(KeyStroke.Frame, this.mc.field_71474_y.field_74366_z.getDisplayName(), (float)(this.x + 40), (float)(this.y + 20), this.mc.field_71474_y.field_74366_z.field_74513_e ? white : rain, KeyStroke.Shadow.getValBoolean());
         if (KeyStroke.Clicks.getValBoolean()) {
            if (KeyStroke.Background.getValBoolean()) {
               Gui.func_73734_a(this.x, this.y + 40, this.x + 20, this.y + 20, this.mc.field_71474_y.field_74312_F.field_74513_e ? rain : black);
               Gui.func_73734_a(this.x + 20, this.y + 40, this.x + 40, this.y + 20, this.mc.field_71474_y.field_74313_G.field_74513_e ? rain : black);
            }

            this.fontSelect(KeyStroke.Frame, "LMB", (float)this.x, (float)(this.y + 40), this.mc.field_71474_y.field_74312_F.field_74513_e ? white : rain, KeyStroke.Shadow.getValBoolean());
            this.fontSelect(KeyStroke.Frame, "RMB", (float)(this.x + 30), (float)(this.y + 40), this.mc.field_71474_y.field_74313_G.field_74513_e ? white : rain, KeyStroke.Shadow.getValBoolean());
         }

         if (KeyStroke.ClicksPerSec.getValBoolean()) {
            this.fontSelect(KeyStroke.Frame, "Clicks: " + this.clicks.size(), (float)this.x, (float)(this.y + 60), this.mc.field_71474_y.field_74312_F.field_74513_e ? white : rain, KeyStroke.Shadow.getValBoolean());
         }

         if (KeyStroke.Background.getValBoolean()) {
            Gui.func_73734_a(this.x + 15, this.y, this.x + 25, this.y + 20, this.mc.field_71474_y.field_74351_w.field_74513_e ? rain : black);
            Gui.func_73734_a(this.x, this.y + 20, this.x + 10, this.y + 40, this.mc.field_71474_y.field_74370_x.field_74513_e ? rain : black);
            Gui.func_73734_a(this.x + 20, this.y + 20, this.x + 30, this.y + 40, this.mc.field_71474_y.field_74368_y.field_74513_e ? rain : black);
            Gui.func_73734_a(this.x + 40, this.y + 20, this.x + 50, this.y + 40, this.mc.field_71474_y.field_74366_z.field_74513_e ? rain : black);
         }

         super.onRenderGameOverlay(event);
      }
   }
}
