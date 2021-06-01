package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class Hat extends Command {
   public Hat() {
      super("Hat");
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

         ItemStack head = mc.field_71439_g.field_71071_by.func_70440_f(3);
         mc.field_71439_g.field_71071_by.field_70460_b.set(3, stack);
         updateSlot(5, stack);
         updateSlot(36 + mc.field_71439_g.field_71071_by.field_70461_c, head);
      } catch (Exception var5) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public static void updateSlot(int slot, ItemStack stack) {
      ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketCreativeInventoryAction(slot, stack));
   }

   public String getDescription() {
      return "Hand to head";
   }

   public String getSyntax() {
      return "Hat";
   }
}
