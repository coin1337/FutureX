package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.clickgui.ClickGui;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import java.util.ArrayList;

public class GuiModule extends Module {
   public ClickGui clickgui;
   public static Setting Frame;
   public static Setting Button;
   public static Setting Subcomponents;
   public static Setting Animations;
   public static Setting Framecolor;
   public static Setting Background;
   public static Setting innercolor;
   public static Setting Hover;
   public static Setting Anispeed;
   public static Setting ColorAni;
   public static Setting Highlight;
   public static Setting border;

   public GuiModule() {
      super("ClickGUI", 54, Category.MISC, "Settings for the Clickgui");
      ArrayList<String> options = new ArrayList();
      options.add("Arial");
      options.add("Impact");
      options.add("Times");
      options.add("MC");
      Main.setmgr.add(Frame = new Setting("Frame_Font", this, "Times", options));
      Main.setmgr.add(Button = new Setting("Button_Font", this, "Times", options));
      Main.setmgr.add(Subcomponents = new Setting("Sub_Font", this, "Times", options));
      Main.setmgr.add(Framecolor = new Setting("Frame", this, 0.0D, 0.7D, 0.65D, 0.7D));
      Main.setmgr.add(Background = new Setting("Background", this, 0.0D, 1.0D, 0.01D, 0.22D));
      Main.setmgr.add(Hover = new Setting("Hover", this, 0.0D, 1.0D, 0.01D, 0.1D));
      Main.setmgr.add(ColorAni = new Setting("ColorAni", this, 0.0D, 1.0D, 0.5D, 0.4D));
      Main.setmgr.add(innercolor = new Setting("innercolor", this, 0.68D, 0.35D, 0.05D, 0.3D));
      Main.setmgr.add(border = new Setting("border", this, false));
      Main.setmgr.add(Highlight = new Setting("Border Color", this, 0.0D, 1.0D, 1.0D, 0.88D, border, 9));
      Main.setmgr.add(Animations = new Setting("Animations", this, true));
      Main.setmgr.add(Anispeed = new Setting("ButtonSpeed", this, 1.8D, 0.0D, 3.0D, false));
   }

   public void setup() {
      this.visible = false;
   }

   public void onEnable() {
      mc.func_147108_a(Main.ClickGui);
      this.toggle();
      super.onEnable();
   }
}
