package Method.Client.module.misc;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketResourcePackStatus.Action;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class AntiHandShake extends Module {
   public AntiHandShake() {
      super("AntiHandShake", 0, Category.MISC, "No Mod List sent on login");
   }

   public boolean onDisablePacket(Object packet, Connection.Side side) {
      if (packet instanceof CPacketResourcePackStatus) {
         ((CPacketResourcePackStatus)packet).field_179719_b = Action.SUCCESSFULLY_LOADED;
      }

      if (side == Connection.Side.OUT) {
         if (packet instanceof FMLProxyPacket && !mc.func_71356_B()) {
            return false;
         }

         if (packet instanceof CPacketCustomPayload && !mc.func_71356_B()) {
            CPacketCustomPayload packet2 = (CPacketCustomPayload)packet;
            if (packet2.func_149559_c().equals("MC|Brand")) {
               packet2.field_149561_c = (new PacketBuffer(Unpooled.buffer())).func_180714_a("vanilla");
            }
         }
      }

      return true;
   }
}
