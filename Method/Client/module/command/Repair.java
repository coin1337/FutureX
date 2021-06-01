package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class Repair extends Command {
   public Repair() {
      super("Repair");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (!mc.field_71439_g.func_184812_l_()) {
            ChatUtils.warning("You must be in creative mode.");
         }

         if (args.length > 0) {
            ChatUtils.warning("Too many arguments.");
         }

         ItemStack stack = mc.field_71439_g.field_71071_by.func_70448_g();
         if (stack.func_190926_b()) {
            ChatUtils.error("You must hold an item in your hand.");
            return;
         }

         if (!stack.func_77984_f()) {
            ChatUtils.error("This item cannot take any damage.");
            return;
         }

         if (!stack.func_77951_h()) {
            ChatUtils.error("This item is not damaged.");
            return;
         }

         stack.func_77964_b(0);
         updateSlot(36 + mc.field_71439_g.field_71071_by.field_70461_c, stack);
         ChatUtils.message("Item §7" + stack.func_82833_r() + " §ehas been repaired.");
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public static void updateSlot(int slot, ItemStack stack) {
      ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketCreativeInventoryAction(slot, stack));
   }

   public String getDescription() {
      return "Repair Item In hand";
   }

   public String getSyntax() {
      return "Repair";
   }
}
