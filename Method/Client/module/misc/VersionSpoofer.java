package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.realms.RealmsSharedConstants;

public class VersionSpoofer extends Module {
   Setting mode;

   public VersionSpoofer() {
      super("VersionSpoofer", 0, Category.MISC, "Version Spoofer");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "1.12.2", new String[]{"1.7.10", "1.8.9", "1.9", "1.12.2", "1.13", "1.14.4", "1.15.1"}));
   }

   public void onEnable() {
      RealmsSharedConstants.NETWORK_PROTOCOL_VERSION = this.version();
   }

   public boolean onDisablePacket(Object packet, Connection.Side side) {
      if (packet instanceof C00Handshake) {
         ((C00Handshake)packet).field_149600_a = this.version();
      }

      return true;
   }

   private int version() {
      if (this.mode.getValString().equalsIgnoreCase("1.7.10")) {
         return 5;
      } else if (this.mode.getValString().equalsIgnoreCase("1.8.9")) {
         return 47;
      } else if (this.mode.getValString().equalsIgnoreCase("1.9")) {
         return 107;
      } else if (this.mode.getValString().equalsIgnoreCase("1.12.2")) {
         return 340;
      } else if (this.mode.getValString().equalsIgnoreCase("1.13")) {
         return 393;
      } else if (this.mode.getValString().equalsIgnoreCase("1.14.4")) {
         return 498;
      } else {
         return this.mode.getValString().equalsIgnoreCase("1.15.1") ? 575 : 340;
      }
   }
}
