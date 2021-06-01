package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Xcarry extends Module {
   Setting Packetclose;

   public Xcarry() {
      super("Xcarry", 0, Category.PLAYER, "Xcarry or SecretClose!");
      this.Packetclose = Main.setmgr.add(new Setting("Fake close", this, false));
   }

   public void onDisable() {
      super.onDisable();
      if (mc.field_71441_e != null) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketCloseWindow(mc.field_71439_g.field_71069_bz.field_75152_c));
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.Packetclose.getValBoolean() && packet instanceof CPacketEntityAction) {
         CPacketEntityAction pac = (CPacketEntityAction)packet;
         if (pac.func_180764_b().equals(Action.OPEN_INVENTORY)) {
            return false;
         }
      }

      return !(packet instanceof CPacketCloseWindow);
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71462_r instanceof GuiInventory) {
         mc.field_71442_b.func_78765_e();
      }

   }
}
