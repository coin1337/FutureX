package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

public class SchematicaNCP extends Module {
   private final TimerUtils timer = new TimerUtils();
   Setting KeepRotation;
   public float[] Rots;

   public SchematicaNCP() {
      super("PrinterBypass", 0, Category.PLAYER, "PrinterBypass");
      this.KeepRotation = Main.setmgr.add(new Setting("Keep Rotation", this, true));
   }

   public void onRightClickBlock(RightClickBlock event) {
      this.timer.setLastMS();
      float[] array = Utils.getNeededRotations(event.getHitVec(), 0.0F, 0.0F);
      mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(array[0], array[1], mc.field_71439_g.field_70122_E));
      this.Rots = array;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && this.KeepRotation.getValBoolean() && this.Rots != null && (packet instanceof Rotation || packet instanceof PositionRotation) && !this.timer.isDelay(4000L)) {
         CPacketPlayer packet2 = (CPacketPlayer)packet;
         packet2.field_149476_e = this.Rots[0];
         packet2.field_149473_f = this.Rots[1];
      }

      return true;
   }
}
