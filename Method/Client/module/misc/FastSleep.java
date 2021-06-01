package Method.Client.module.misc;

import Method.Client.module.Category;
import Method.Client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FastSleep extends Module {
   public FastSleep() {
      super("FastSleep", 0, Category.MISC, "Fast Sleep");
   }

   public void onClientTick(ClientTickEvent event) {
      EntityPlayerSP player = mc.field_71439_g;
      if (player.func_70608_bn() && player.func_71060_bI() > 10) {
         player.field_71174_a.func_147297_a(new CPacketEntityAction(player, Action.STOP_SLEEPING));
      }

   }
}
