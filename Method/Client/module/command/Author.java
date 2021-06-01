package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;

public class Author extends Command {
   public Author() {
      super("Author");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (!mc.field_71439_g.field_71075_bZ.field_75098_d) {
            ChatUtils.error("Creative mode only.");
         }

         ItemStack heldItem = mc.field_71439_g.field_71071_by.func_70448_g();
         int heldItemID = Item.func_150891_b(heldItem.func_77973_b());
         int writtenBookID = Item.func_150891_b(Items.field_151164_bB);
         if (heldItemID != writtenBookID) {
            ChatUtils.error("You must hold a written book in your main hand.");
         } else {
            heldItem.func_77983_a("author", new NBTTagString(args[0]));
            ChatUtils.message("Author Changed! Open Inventory.");
         }
      } catch (Exception var6) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Change BookSign Author Creative Only";
   }

   public String getSyntax() {
      return "Author <author> ";
   }
}
