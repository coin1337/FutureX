package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class Head extends Command {
   public Head() {
      super("Head");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (!mc.field_71439_g.func_184812_l_()) {
            ChatUtils.warning("You must be in creative mode.");
         }

         if (args.length < 1) {
            ChatUtils.error("Invalid syntax.");
            return;
         }

         if (args.length > 1) {
            ChatUtils.warning("Too many arguments.");
         }

         ItemStack stack = mc.field_71439_g.field_71071_by.func_70448_g();
         if (!stack.func_190926_b() && Item.func_150891_b(stack.func_77973_b()) == 397 && stack.func_77960_j() == 3) {
            stack.func_77983_a("SkullOwner", new NBTTagString(args[0]));
            updateSlot(36 + mc.field_71439_g.field_71071_by.field_70461_c, stack);
            ChatUtils.message("Head's owner changed to §7" + args[0] + "§e.");
            return;
         }

         ItemStack newStack = new ItemStack(Item.func_150899_d(397), 1, 3);
         newStack.func_77983_a("SkullOwner", new NBTTagString(args[0]));
         Give.updateFirstEmptySlot(newStack);
         ChatUtils.message("Given head of player §7" + args[0] + "§e to §7" + mc.field_71439_g.func_70005_c_() + "§e.");
      } catch (Exception var5) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public static void updateSlot(int slot, ItemStack stack) {
      ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketCreativeInventoryAction(slot, stack));
   }

   public String getDescription() {
      return "Head to Hand";
   }

   public String getSyntax() {
      return "Head <Player>";
   }
}
