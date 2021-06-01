package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PortalMod extends Module {
   Setting gui;
   Setting god;

   public PortalMod() {
      super("PortalMod", 0, Category.PLAYER, "PortalMod");
      this.gui = Main.setmgr.add(new Setting("gui", this, true));
      this.god = Main.setmgr.add(new Setting("god", this, true));
   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      if (this.gui.getValBoolean()) {
         mc.field_71439_g.field_71087_bX = false;
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.god.getValBoolean()) {
         return !(packet instanceof CPacketConfirmTeleport);
      } else {
         return true;
      }
   }
}
