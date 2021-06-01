package Method.Client.module.player;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;

public class AutoFish extends Module {
   public AutoFish() {
      super("AutoFish", 0, Category.PLAYER, "AutoFish");
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketSoundEffect) {
         SPacketSoundEffect packet2 = (SPacketSoundEffect)packet;
         if (packet2.func_186978_a().equals(SoundEvents.field_187609_F)) {
            (new Thread(() -> {
               try {
                  mc.field_71442_b.func_187101_a(mc.field_71439_g, mc.field_71439_g.field_70170_p, EnumHand.MAIN_HAND);
                  Thread.sleep(300L);
                  mc.field_71442_b.func_187101_a(mc.field_71439_g, mc.field_71439_g.field_70170_p, EnumHand.MAIN_HAND);
               } catch (Exception var1) {
               }

            })).start();
         }
      }

      return true;
   }
}
