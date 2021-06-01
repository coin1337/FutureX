package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import java.util.Objects;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiCrash extends Module {
   Setting slime;
   Setting offhand;
   Setting Sound;

   public AntiCrash() {
      super("AntiCrash", 0, Category.MISC, "Anti Crash");
      this.slime = Main.setmgr.add(new Setting("slime", this, true));
      this.offhand = Main.setmgr.add(new Setting("offhand", this, true));
      this.Sound = Main.setmgr.add(new Setting("Sound", this, true));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.IN && packet instanceof SPacketSoundEffect && this.offhand.getValBoolean()) {
         return ((SPacketSoundEffect)packet).func_186978_a() != SoundEvents.field_187719_p;
      } else if (packet instanceof SPacketSoundEffect && this.Sound.getValBoolean()) {
         SPacketSoundEffect packet2 = (SPacketSoundEffect)packet;
         return packet2.func_186977_b() != SoundCategory.PLAYERS || packet2.func_186978_a() != SoundEvents.field_187719_p;
      } else {
         return true;
      }
   }

   public void onClientTick(ClientTickEvent event) {
      if (Objects.nonNull(mc.field_71441_e) && this.slime.getValBoolean()) {
         mc.field_71441_e.field_72996_f.forEach((e) -> {
            if (e instanceof EntitySlime) {
               EntitySlime slime = (EntitySlime)e;
               if (slime.func_70809_q() > 4) {
                  mc.field_71441_e.func_72900_e(e);
               }
            }

         });
      }

   }
}
