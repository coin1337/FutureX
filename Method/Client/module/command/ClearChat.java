package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;

public class ClearChat extends Command {
   public ClearChat() {
      super("Clear");
   }

   public void runCommand(String s, String[] args) {
      try {
         mc.field_71456_v.func_146158_b().func_146231_a(true);
         ChatUtils.message("Cleared Chat");
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Clears Chat";
   }

   public String getSyntax() {
      return "Clear";
   }
}
