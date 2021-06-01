package Method.Client.module.command;

import Method.Client.managers.FileManager;
import Method.Client.utils.visual.ChatUtils;
import java.awt.Desktop;

public class OpenDir extends Command {
   public OpenDir() {
      super("Opendir");
   }

   public void runCommand(String s, String[] args) {
      try {
         Desktop.getDesktop().open(FileManager.SaveDir);
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Open Dir";
   }

   public String getSyntax() {
      return "OpenDir";
   }
}
