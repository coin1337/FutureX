package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.math.BigInteger;

public class Pitch extends Command {
   public Pitch() {
      super("Pitch");
   }

   public void runCommand(String s, String[] args) {
      try {
         long Pitch = (new BigInteger(args[0])).longValue();
         mc.field_71439_g.field_70125_A = (float)Pitch;
         ChatUtils.message("Pitch =" + Pitch);
      } catch (Exception var5) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Set Pitch";
   }

   public String getSyntax() {
      return "Pitch <Num>";
   }
}
