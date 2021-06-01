package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.entity.Entity;

public class Vanish extends Command {
   private static Entity vehicle;

   public Vanish() {
      super("Vanish");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (mc.field_71439_g.func_184187_bx() != null && vehicle == null) {
            vehicle = mc.field_71439_g.func_184187_bx();
            mc.field_71439_g.func_184210_p();
            mc.field_71441_e.func_73028_b(vehicle.func_145782_y());
            ChatUtils.message("Vehicle " + vehicle.func_70005_c_() + " removed.");
         } else if (vehicle != null) {
            vehicle.field_70128_L = false;
            mc.field_71441_e.func_73027_a(vehicle.func_145782_y(), vehicle);
            mc.field_71439_g.func_184205_a(vehicle, true);
            ChatUtils.message("Vehicle " + vehicle.func_70005_c_() + " created.");
            vehicle = null;
         } else {
            ChatUtils.message("No Vehicle.");
         }
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Vanish in a entity";
   }

   public String getSyntax() {
      return "Vanish  ";
   }
}
