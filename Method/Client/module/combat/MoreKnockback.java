package Method.Client.module.combat;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;

public class MoreKnockback extends Module {
   public MoreKnockback() {
      super("MoreKnockback", 0, Category.COMBAT, "More Knockback");
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (mc.field_71439_g.field_70122_E && side == Connection.Side.OUT && packet instanceof CPacketUseEntity) {
         CPacketUseEntity attack = (CPacketUseEntity)packet;
         if (attack.func_149565_c() == Action.ATTACK) {
            Entity entity = mc.field_71441_e.func_73045_a(attack.field_149567_a);
            if (entity != mc.field_71439_g && entity != null && entity.func_70032_d(mc.field_71439_g) < 4.0F) {
               boolean oldSprint = mc.field_71439_g.func_70051_ag();
               Wrapper.INSTANCE.sendPacket(new CPacketEntityAction(mc.field_71439_g, net.minecraft.network.play.client.CPacketEntityAction.Action.STOP_SPRINTING));
               Wrapper.INSTANCE.sendPacket(new CPacketEntityAction(mc.field_71439_g, net.minecraft.network.play.client.CPacketEntityAction.Action.START_SPRINTING));
               mc.field_71439_g.func_70031_b(oldSprint);
            }
         }
      }

      return true;
   }
}
