package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.math.BigInteger;

public class VClip extends Command {
   public VClip() {
      super("vclip");
   }

   public void runCommand(String s, String[] args) {
      try {
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)(new BigInteger(args[0])).longValue(), mc.field_71439_g.field_70161_v);
         ChatUtils.message("Height teleported to " + (new BigInteger(args[0])).longValue());
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Teleports you up/down.";
   }

   public String getSyntax() {
      return "vclip <height>";
   }
}
