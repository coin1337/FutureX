package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.math.BigInteger;

public class Yaw extends Command {
   public Yaw() {
      super("Yaw");
   }

   public void runCommand(String s, String[] args) {
      try {
         long Yaw = (new BigInteger(args[0])).longValue();
         mc.field_71439_g.field_70177_z = (float)Yaw;
         ChatUtils.message("Yaw =" + Yaw);
      } catch (Exception var5) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Set Yaw";
   }

   public String getSyntax() {
      return "Yaw <Num>";
   }
}
