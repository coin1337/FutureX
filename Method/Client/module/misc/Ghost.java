package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Screens.Override.DeathOverride;
import Method.Client.utils.system.Wrapper;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketClientStatus.State;

public class Ghost extends Module {
   public static Setting health;

   public Ghost() {
      super("Ghost", 0, Category.MISC, "Move While dead");
   }

   public void setup() {
      Main.setmgr.add(health = new Setting("health", this, 40.0D, 0.0D, 40.0D, true));
   }

   public void onEnable() {
      DeathOverride.Override = true;
   }

   public void onDisable() {
      DeathOverride.Override = false;
      if (DeathOverride.isDead) {
         Wrapper.INSTANCE.sendPacket(new CPacketClientStatus(State.PERFORM_RESPAWN));
         DeathOverride.isDead = false;
      }

   }
}
