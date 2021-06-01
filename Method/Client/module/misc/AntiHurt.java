package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.network.play.server.SPacketUpdateHealth;

public class AntiHurt extends Module {
   Setting mode;

   public AntiHurt() {
      super("AntiHurt", 0, Category.MISC, "Anti Hurt on some instance");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Packet", new String[]{"Death", "Packet"}));
   }

   public void onEnable() {
      if (this.mode.getValString().equalsIgnoreCase("Death") && mc.field_71439_g != null) {
         mc.field_71439_g.field_70128_L = true;
         mc.field_71474_y.field_74351_w.func_151468_f();
      }

   }

   public void onDisable() {
      mc.field_71439_g.field_70128_L = false;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.mode.getValString().equalsIgnoreCase("Packet")) {
         return !(packet instanceof SPacketUpdateHealth);
      } else {
         return true;
      }
   }
}
