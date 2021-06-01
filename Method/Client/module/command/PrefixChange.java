package Method.Client.module.command;

import Method.Client.managers.CommandManager;
import Method.Client.utils.visual.ChatUtils;

public class PrefixChange extends Command {
   public PrefixChange() {
      super("PrefixChange");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (args[0].length() < 2) {
            CommandManager.cmdPrefix = args[0].charAt(0);
            ChatUtils.message("Prefix Changed");
         } else {
            ChatUtils.error("Prefix must be 1 length");
         }
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Prefix Changer";
   }

   public String getSyntax() {
      return "PrefixChange <Name>";
   }
}
