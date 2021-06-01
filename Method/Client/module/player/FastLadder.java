package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Wrapper;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FastLadder extends Module {
   Setting mode;

   public FastLadder() {
      super("FastLadder", 0, Category.PLAYER, "Climb Faster");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Simple", new String[]{"Simple", "DOWN", "Skip"}));
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71439_g.func_70617_f_() && (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F)) {
         if (this.mode.getValString().equalsIgnoreCase("Skip") && mc.field_71439_g.func_70617_f_()) {
            mc.field_71439_g.func_70031_b(true);
            mc.field_71439_g.field_70122_E = true;
         }

         if (this.mode.getValString().equalsIgnoreCase("DOWN")) {
            while(mc.field_71439_g.func_70617_f_() && mc.field_71439_g.field_70181_x != 0.0D) {
               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)(mc.field_71439_g.field_70181_x > 0.0D ? 1 : -1), mc.field_71439_g.field_70161_v);
               Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, true));
            }
         }

         if (this.mode.getValString().equalsIgnoreCase("simple")) {
            mc.field_71439_g.field_70181_x = 0.16999999433755875D;
         }

         super.onClientTick(event);
      }
   }
}
