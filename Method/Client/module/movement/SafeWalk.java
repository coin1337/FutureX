package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Patcher.Events.PlayerMoveEvent;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {
   Setting mode;
   Setting EdgeStop;
   Setting SlowOnEdge;

   public SafeWalk() {
      super("SafeWalk", 0, Category.MOVEMENT, "SafeWalk, Safe ledge");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Sneak", new String[]{"Sneak", "Normal"}));
      this.EdgeStop = Main.setmgr.add(new Setting("Edge Stop", this, true, this.mode, "Normal", 2));
      this.SlowOnEdge = Main.setmgr.add(new Setting("Slow on Edge", this, false));
   }

   public void onPlayerMove(PlayerMoveEvent event) {
   }

   public void onPlayerTick(PlayerTickEvent event) {
      EntityPlayerSP var10000;
      if (this.mode.getValString().equalsIgnoreCase("Normal") && mc.field_71439_g.field_70122_E && !mc.field_71474_y.field_74314_A.func_151468_f() && !isCollidable(mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.func_174791_d().func_178787_e(new Vec3d(0.0D, -0.5D, 0.0D)))).func_177230_c())) {
         if (this.SlowOnEdge.getValBoolean()) {
            var10000 = mc.field_71439_g;
            var10000.field_70159_w -= mc.field_71439_g.field_70159_w;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y -= mc.field_71439_g.field_70179_y;
         }

         if (this.EdgeStop.getValBoolean()) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70169_q, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70166_s);
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Sneak")) {
         if (mc.field_71439_g.field_70122_E && !mc.field_71474_y.field_74314_A.func_151468_f() && !isCollidable(mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.func_174791_d().func_178787_e(new Vec3d(0.0D, -0.5D, 0.0D)))).func_177230_c())) {
            KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), true);
            if (this.SlowOnEdge.getValBoolean()) {
               var10000 = mc.field_71439_g;
               var10000.field_70159_w -= mc.field_71439_g.field_70159_w;
               var10000 = mc.field_71439_g;
               var10000.field_70179_y -= mc.field_71439_g.field_70179_y;
            }
         } else if (!Keyboard.isKeyDown(mc.field_71474_y.field_74311_E.func_151463_i())) {
            KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
         }
      }

   }

   public static boolean isCollidable(Block block) {
      return block != Blocks.field_150350_a && block != Blocks.field_185773_cZ && block != Blocks.field_150459_bM && block != Blocks.field_150330_I && block != Blocks.field_150398_cm && block != Blocks.field_150356_k && block != Blocks.field_150358_i && block != Blocks.field_150353_l && block != Blocks.field_150394_bc && block != Blocks.field_150388_bm && block != Blocks.field_150469_bN && block != Blocks.field_150393_bb && block != Blocks.field_150328_O && block != Blocks.field_150337_Q && block != Blocks.field_150429_aA && block != Blocks.field_150329_H && block != Blocks.field_150478_aa && block != Blocks.field_150437_az && block != Blocks.field_150327_N && block != Blocks.field_150395_bd && block != Blocks.field_150355_j && block != Blocks.field_150321_G && block != Blocks.field_150464_aj;
   }
}
