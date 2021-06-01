package Method.Client.module.command;

import Method.Client.module.misc.GuiPeek;
import Method.Client.module.misc.Shulkerspy;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;

public class Peek extends Command {
   public Peek() {
      super("Peek");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (args[0] != null) {
            String name = args[0].toLowerCase();
            if (!Shulkerspy.shulkerMap.containsKey(name.toLowerCase())) {
               ChatUtils.error("have not seen this player hold a shulkerbox. Check your spelling.");
               return;
            }

            IInventory inv = (IInventory)Shulkerspy.shulkerMap.get(name.toLowerCase());
            (new Thread(() -> {
               try {
                  Thread.sleep(100L);
               } catch (InterruptedException var2) {
               }

               mc.field_71439_g.func_71007_a(inv);
            })).start();
         } else {
            if (!(mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemShulkerBox)) {
               ChatUtils.error("You Have to hold a shulker box");
            }

            ItemStack itemStack = mc.field_71439_g.func_184614_ca();
            if (itemStack.func_77973_b() instanceof ItemShulkerBox) {
               ChatUtils.message("Opening your shulker box.");
               GuiPeek.Peekcode(itemStack, mc);
            }
         }
      } catch (Exception var5) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Peek into shukler!";
   }

   public String getSyntax() {
      return "Peek <Name>";
   }
}
