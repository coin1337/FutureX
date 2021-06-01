package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.item.ItemStack;

public class Drop extends Command {
   public Drop() {
      super("Drop");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (args[0].equalsIgnoreCase("all")) {
            ClickType t = ClickType.THROW;

            for(int var2 = 9; var2 < 45; ++var2) {
               mc.field_71442_b.func_187098_a(0, var2, 1, t, mc.field_71439_g);
            }
         } else if (args[0].equalsIgnoreCase("Mob") && mc.field_71439_g.func_184187_bx() instanceof AbstractHorse && mc.field_71439_g.field_71070_bA instanceof ContainerHorseInventory) {
            for(int i = 2; i < 17; ++i) {
               ItemStack itemStack = (ItemStack)mc.field_71439_g.field_71070_bA.func_75138_a().get(i);
               if (!itemStack.func_190926_b() && itemStack.func_77973_b() != Items.field_190931_a) {
                  mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71070_bA.field_75152_c, i, 0, ClickType.PICKUP, mc.field_71439_g);
                  mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71070_bA.field_75152_c, -999, 0, ClickType.PICKUP, mc.field_71439_g);
               }
            }
         }

         if (args[0].equalsIgnoreCase("hand")) {
            mc.field_71439_g.func_71040_bB(true);
         }
      } catch (Exception var5) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Drops items";
   }

   public String getSyntax() {
      return "Drop <all/hand/Mob> ";
   }
}
