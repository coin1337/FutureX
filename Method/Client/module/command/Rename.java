package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.item.ItemStack;

public class Rename extends Command {
   public Rename() {
      super("Rename");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (args.length < 1) {
            ChatUtils.error("Invalid syntax.");
            return;
         }

         ItemStack stack = mc.field_71439_g.field_71071_by.func_70448_g();
         if (stack.func_190926_b()) {
            ChatUtils.error("You must hold an item in your hand.");
            return;
         }

         StringBuilder name = new StringBuilder(args[0]);

         for(int i = 1; i < args.length; ++i) {
            name.append(" ").append(args[i]);
         }

         name = new StringBuilder(name.toString().replace('&', '§').replace("§§", "&"));
         if (!mc.field_71439_g.func_184812_l_()) {
            ChatUtils.warning("You must be in creative mode!");
         }

         stack.func_151001_c(name.toString());
         Nbt.updateSlot(36 + mc.field_71439_g.field_71071_by.field_70461_c, stack);
         ChatUtils.message("Item's name changed to §7" + name + "§e.");
      } catch (Exception var6) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Rename item!";
   }

   public String getSyntax() {
      return "Rename <Name>";
   }
}
