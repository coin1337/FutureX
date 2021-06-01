package Method.Client.module.command;

import Method.Client.managers.CommandManager;
import Method.Client.utils.visual.ChatUtils;
import java.util.Iterator;

public class Help extends Command {
   public Help() {
      super("help");
   }

   public void runCommand(String s, String[] args) {
      Iterator var3 = CommandManager.commands.iterator();

      while(var3.hasNext()) {
         Command cmd = (Command)var3.next();
         if (cmd != this) {
            ChatUtils.message(cmd.getSyntax().replace("<", "<§9").replace(">", "§7>") + " - " + cmd.getDescription());
         }
      }

   }

   public String getDescription() {
      return "Lists all commands.";
   }

   public String getSyntax() {
      return "help";
   }
}
