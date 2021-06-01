package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;

public class ModSettings extends Module {
   public static Setting Spherelines;
   public static Setting SphereDist;
   public static Setting Rendernonsee;
   public static Setting ShowErrors;
   public static Setting GuiSpeed;

   public ModSettings() {
      super("ModSettings", 0, Category.MISC, "Mod Settings for other modules");
      Main.setmgr.add(Spherelines = new Setting("Shapelines", this, 10.0D, 0.0D, 20.0D, true));
      Main.setmgr.add(SphereDist = new Setting("ShapeDist", this, 10.0D, 0.0D, 20.0D, true));
      Main.setmgr.add(GuiSpeed = new Setting("GuiSpeed", this, 20.0D, 0.0D, 50.0D, true));
      Main.setmgr.add(Rendernonsee = new Setting("Unseen Render", this, false));
      Main.setmgr.add(ShowErrors = new Setting("ShowErrors", this, false));
   }
}
