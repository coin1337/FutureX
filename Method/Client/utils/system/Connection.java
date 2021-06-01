package Method.Client.utils.system;

import Method.Client.utils.EventsHandler;
import Method.Client.utils.visual.ChatUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;

public class Connection extends ChannelDuplexHandler {
   private final EventsHandler eventHandler;

   public Connection(EventsHandler eventHandler) {
      this.eventHandler = eventHandler;

      try {
         ChannelPipeline pipeline = ((NetHandlerPlayClient)Objects.requireNonNull(Wrapper.INSTANCE.mc().func_147114_u())).func_147298_b().channel().pipeline();
         pipeline.addBefore("packet_handler", "PacketHandler", this);
      } catch (Exception var3) {
         ChatUtils.error("Connection: Error on attaching");
         var3.printStackTrace();
      }

   }

   public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
      if (this.eventHandler.onPacket(packet, Connection.Side.IN)) {
         super.channelRead(ctx, packet);
      }
   }

   public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
      if (this.eventHandler.onPacket(packet, Connection.Side.OUT)) {
         super.write(ctx, packet, promise);
      }
   }

   public static enum Side {
      IN,
      OUT;
   }
}
