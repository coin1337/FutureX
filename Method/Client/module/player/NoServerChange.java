package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import java.util.Objects;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetSlot;

public class NoServerChange extends Module {
   Setting Inventory;
   Setting Rotate;

   public NoServerChange() {
      super("NoServerChange", 0, Category.PLAYER, "NoServerChange");
      this.Inventory = Main.setmgr.add(new Setting("Held Item Change", this, false));
      this.Rotate = Main.setmgr.add(new Setting("Rotate", this, true));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && packet instanceof SPacketSetSlot && this.Inventory.getValBoolean()) {
         int currentSlot = mc.field_71439_g.field_71071_by.field_70461_c;
         SPacketSetSlot packet2 = (SPacketSetSlot)packet;
         if (packet2.func_149173_d() != currentSlot) {
            ((NetworkManager)Objects.requireNonNull(mc.field_71453_ak)).func_179290_a(new CPacketHeldItemChange(currentSlot));
            MC.field_71474_y.field_74313_G.field_74513_e = true;
            return false;
         }
      }

      if (packet instanceof SPacketPlayerPosLook && this.Rotate.getValBoolean()) {
         SPacketPlayerPosLook packet3 = (SPacketPlayerPosLook)packet;
         if (mc.field_71439_g != null) {
            packet3.field_148936_d = mc.field_71439_g.field_70177_z;
            packet3.field_148937_e = mc.field_71439_g.field_70125_A;
         }
      }

      return true;
   }
}
