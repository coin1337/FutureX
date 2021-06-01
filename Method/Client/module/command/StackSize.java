package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.item.ItemStack;

public class StackSize extends Command {
   public StackSize() {
      super("StackSize");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (!mc.field_71439_g.func_184812_l_()) {
            ChatUtils.error("Creative mode is required to use this command.");
            return;
         }

         ItemStack itemStack = mc.field_71439_g.func_184614_ca();
         if (itemStack.func_190926_b()) {
            ChatUtils.error("Please hold an item in your main hand to enchant.");
            return;
         }

         String id = args[0];
         int num = Integer.parseInt(id);
         itemStack.func_190920_e(num);

         assert itemStack.func_77978_p() != null;

         itemStack.func_77973_b().func_179215_a(itemStack.func_77978_p());
         ChatUtils.error("Set your stack size to " + num);
      } catch (Exception var6) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "StackSize";
   }

   public String getSyntax() {
      return "StackSize ";
   }
}
