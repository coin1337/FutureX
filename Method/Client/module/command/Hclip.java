package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.math.BigInteger;

public class Hclip extends Command {
   public Hclip() {
      super("Hclip");
   }

   public void runCommand(String s, String[] args) {
      try {
         long dir = (new BigInteger(args[0])).longValue();
         long dir2 = (new BigInteger(args[1])).longValue();
         double y = mc.field_71439_g.field_70163_u;
         float yaw = mc.field_71439_g.field_70177_z;
         double newX = -Math.sin(Math.toRadians((double)yaw)) * (double)dir + mc.field_71439_g.field_70165_t;
         double newZ = Math.cos(Math.toRadians((double)yaw)) * (double)dir2 + mc.field_71439_g.field_70161_v;
         mc.field_71439_g.func_70107_b(newX, y, newZ);
         ChatUtils.message("Zoomed " + dir + " blocks.");
      } catch (Exception var14) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Teleports you In the H.";
   }

   public String getSyntax() {
      return "Hclip <X> <Z>";
   }
}
