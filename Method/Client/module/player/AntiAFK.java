package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Wrapper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiAFK extends Module {
   boolean switcheraro;
   Setting spin;
   Setting delay;
   Setting swing;
   Setting walk;
   TimerUtils timer;

   public AntiAFK() {
      super("AntiAFK", 0, Category.PLAYER, "Preforms action to not be kicked!");
      this.spin = Main.setmgr.add(new Setting("spin", this, false));
      this.delay = Main.setmgr.add(new Setting("delay", this, 1.0D, 0.0D, 60.0D, false));
      this.swing = Main.setmgr.add(new Setting("swing", this, true));
      this.walk = Main.setmgr.add(new Setting("walk", this, false));
      this.timer = new TimerUtils();
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.timer.isDelay((long)(this.delay.getValDouble() * 1000.0D))) {
         this.switcheraro = !this.switcheraro;
         if (this.switcheraro) {
            if (this.spin.getValBoolean()) {
               Wrapper.INSTANCE.sendPacket(new Rotation((float)Utils.random(-160, 160), (float)Utils.random(-160, 160), true));
            }

            if (this.swing.getValBoolean()) {
               Wrapper.INSTANCE.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            }

            if (this.walk.getValBoolean()) {
               int c = Utils.random(0, 10);
               if (c == 4) {
                  KeyBinding.func_74510_a(mc.field_71474_y.field_74351_w.func_151463_i(), true);
               }

               if (c == 1) {
                  KeyBinding.func_74510_a(mc.field_71474_y.field_74368_y.func_151463_i(), true);
               }

               if (c == 2) {
                  KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), true);
               }

               if (c == 3) {
                  KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), true);
               }
            }
         } else if (this.walk.getValBoolean()) {
            KeyBinding.func_74510_a(mc.field_71474_y.field_74351_w.func_151463_i(), false);
            KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), false);
            KeyBinding.func_74510_a(mc.field_71474_y.field_74368_y.func_151463_i(), false);
            KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), false);
         }

         this.timer.setLastMS();
      }

   }
}
