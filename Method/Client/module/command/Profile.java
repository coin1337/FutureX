package Method.Client.module.command;

import Method.Client.clickgui.ClickGui;
import Method.Client.clickgui.component.Frame;
import Method.Client.module.ModuleManager;
import Method.Client.module.Profiles.Profiletem;
import Method.Client.utils.visual.ChatUtils;
import java.util.Iterator;

public class Profile extends Command {
   public Profile() {
      super("Profile");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (args[0].equalsIgnoreCase("Remove")) {
            ModuleManager.toggledModules.remove(ModuleManager.getModuleByName(args[1]));
            ModuleManager.modules.remove(ModuleManager.getModuleByName(args[1]));
            ChatUtils.message("Removed Profile" + args[1]);
         }

         if (args[0].equalsIgnoreCase("Add")) {
            ModuleManager.addModule(new Profiletem(args[1]));
            ChatUtils.message("Added Profile" + args[1]);
         }

         Iterator var3 = ClickGui.frames.iterator();

         while(var3.hasNext()) {
            Frame frame = (Frame)var3.next();
            if (frame.getName().equalsIgnoreCase("PROFILES")) {
               frame.updateRefresh();
            }
         }
      } catch (Exception var5) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Modify Profile Add/Remove";
   }

   public String getSyntax() {
      return "profile <Add/Remove> [Name]";
   }
}
