package Method.Client.module.misc;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.server.SPacketSetSlot;

public class QuickCraft extends Module {
   public QuickCraft() {
      super("QuickCraft", 0, Category.MISC, "Quick Craft");
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.IN && packet instanceof SPacketSetSlot && ((SPacketSetSlot)packet).func_149173_d() == 0 && ((SPacketSetSlot)packet).func_149174_e().func_77973_b() != Items.field_190931_a && (mc.field_71462_r instanceof GuiInventory || mc.field_71462_r instanceof GuiCrafting)) {
         mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71070_bA.field_75152_c, 0, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
         mc.field_71442_b.func_78765_e();
      }

      return true;
   }
}
