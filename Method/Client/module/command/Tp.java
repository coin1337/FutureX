package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class Tp extends Command {
   public Tp() {
      super("Tp");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (args.length < 1) {
            ChatUtils.error("Invalid syntax.");
         } else {
            float yaw;
            if (args.length < 2) {
               EntityPlayer target = mc.field_71441_e.func_72924_a(args[0]);
               if (target == null) {
                  ChatUtils.error("Player §7" + args[0] + " §ccan not be found.");
                  return;
               }

               double x = target.field_70165_t;
               double y = target.field_70163_u;
               double z = target.field_70161_v;
               yaw = target.field_70125_A;
               float yaw = target.field_70177_z;
               mc.field_71439_g.func_70080_a(x, y, z, yaw, yaw);
               ChatUtils.message("Teleported §7" + mc.field_71439_g.func_70005_c_() + "§e to §9" + x + "§e, §9" + y + "§e, §9" + z + "§e.");
            } else {
               if (args.length >= 3) {
                  double x = mc.field_71439_g.field_70165_t;
                  double y = mc.field_71439_g.field_70163_u;
                  double z = mc.field_71439_g.field_70161_v;
                  float pitch = mc.field_71439_g.field_70125_A;
                  yaw = mc.field_71439_g.field_70177_z;

                  try {
                     x = parseMath(args[0], x);
                  } catch (NumberFormatException | NullPointerException var16) {
                     ChatUtils.error("§7" + args[0] + " §cis not a valid number.");
                     return;
                  }

                  try {
                     y = parseMath(args[1], y);
                  } catch (NumberFormatException | NullPointerException var15) {
                     ChatUtils.error("§7" + args[1] + " §cis not a valid number.");
                     return;
                  }

                  try {
                     z = parseMath(args[2], z);
                  } catch (NumberFormatException | NullPointerException var14) {
                     ChatUtils.error("§7" + args[2] + " §cis not a valid number.");
                     return;
                  }

                  if (args.length > 3) {
                     try {
                        yaw = (float)parseMath(args[3], (double)yaw);
                     } catch (NumberFormatException | NullPointerException var13) {
                        ChatUtils.error("§7" + args[3] + " §cis not a valid number.");
                        return;
                     }
                  }

                  if (args.length > 4) {
                     try {
                        pitch = (float)parseMath(args[4], (double)pitch);
                     } catch (NumberFormatException | NullPointerException var12) {
                        ChatUtils.error("§7" + args[4] + " §cis not a valid number.");
                        return;
                     }
                  }

                  if (args.length > 5) {
                     ChatUtils.warning("Too many arguments.");
                  }

                  mc.field_71439_g.func_70080_a(x, y, z, yaw, pitch);
                  ChatUtils.message("Teleported §7" + mc.field_71439_g.func_70005_c_() + "§e to §9" + x + "§e, §9" + y + "§e, §9" + z + "§e.");
                  return;
               }

               ChatUtils.error("Invalid syntax.");
            }
         }
      } catch (Exception var17) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public static void updateSlot(int slot, ItemStack stack) {
      ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketCreativeInventoryAction(slot, stack));
   }

   private static double parseMath(String input, double old) {
      if (input.length() < 1) {
         throw new NumberFormatException();
      } else if (input.charAt(0) == '~') {
         String coord;
         if (input.length() > 2 && input.charAt(1) == '+') {
            coord = input.substring(2);
            return old + Double.parseDouble(coord);
         } else if (input.length() > 2 && input.charAt(1) == '-') {
            coord = input.substring(2);
            return old - Double.parseDouble(coord);
         } else if (input.length() != 1) {
            throw new NumberFormatException();
         } else {
            return old;
         }
      } else {
         return Double.parseDouble(input);
      }
   }

   public String getDescription() {
      return "Tp to position or player";
   }

   public String getSyntax() {
      return "tp <<x> <y> <z> [yaw] [pitch]|<player>>";
   }
}
