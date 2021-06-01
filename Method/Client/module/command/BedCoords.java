package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;

public class BedCoords extends Command {
   public BedCoords() {
      super("BedCoords");
   }

   public void runCommand(String s, String[] args) {
      try {
         ChatUtils.message(mc.field_71439_g.func_180470_cg().toString());
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "BedCoords";
   }

   public String getSyntax() {
      return "BedCoords ";
   }
}
