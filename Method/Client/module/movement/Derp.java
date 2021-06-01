package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.Patcher.Events.PlayerMoveEvent;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class Derp extends Module {
   Setting Silent;
   Setting Yaw;
   Setting Pitch;
   Setting illegal;

   public Derp() {
      super("Derp", 0, Category.MOVEMENT, "Derp");
      this.Silent = Main.setmgr.add(new Setting("Silent", this, true));
      this.Yaw = Main.setmgr.add(new Setting("Yaw", this, true));
      this.Pitch = Main.setmgr.add(new Setting("Pitch", this, true));
      this.illegal = Main.setmgr.add(new Setting("illegal Range?", this, false));
   }

   public void onEnable() {
      if (this.illegal.getValBoolean()) {
         ChatUtils.warning("Going beyond max normally possible");
      }

   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      int doubleit = this.illegal.getValBoolean() ? 2 : 1;
      if (!this.Silent.getValBoolean()) {
         if (this.Yaw.getValBoolean()) {
            mc.field_71439_g.field_70177_z = (float)ThreadLocalRandom.current().nextDouble((double)(-180 * doubleit), (double)(180 * doubleit));
         }

         if (this.Pitch.getValBoolean()) {
            mc.field_71439_g.field_70125_A = (float)ThreadLocalRandom.current().nextDouble((double)(-90 * doubleit), (double)(90 * doubleit));
         }
      }

   }

   public void onPlayerMove(PlayerMoveEvent event) {
      mc.field_71439_g.field_70759_as = (float)Utils.random(-180, 180);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.Silent.getValBoolean()) {
         int doubleit = this.illegal.getValBoolean() ? 2 : 1;
         if ((packet instanceof Rotation || packet instanceof PositionRotation) && side == Connection.Side.OUT) {
            CPacketPlayer packet2 = (CPacketPlayer)packet;
            if (this.Pitch.getValBoolean()) {
               packet2.field_149473_f = (float)Utils.random(-180 * doubleit, 180 * doubleit);
            }

            if (this.Yaw.getValBoolean()) {
               packet2.field_149476_e = (float)Utils.random(-90 * doubleit, 90 * doubleit);
            }
         }
      }

      return true;
   }
}
