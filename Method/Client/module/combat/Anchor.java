package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Anchor extends Module {
   Setting maxheight;
   Setting JumpOut;
   Setting Onerun;
   BlockPos playerPos;
   private final TimerUtils timer;
   private boolean WasJump;

   public Anchor() {
      super("Anchor", 0, Category.COMBAT, "Anchor to Holes");
      this.maxheight = Main.setmgr.add(this.maxheight = new Setting("max height", this, 15.0D, 0.0D, 255.0D, false));
      this.JumpOut = Main.setmgr.add(this.JumpOut = new Setting("JumpOut", this, true));
      this.Onerun = Main.setmgr.add(this.Onerun = new Setting("Run Once", this, true));
      this.timer = new TimerUtils();
      this.WasJump = false;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.WasJump) {
         if (this.timer.isDelay(1800L)) {
            this.timer.setLastMS();
            this.WasJump = false;
         }

      } else if (!(mc.field_71439_g.field_70163_u < 0.0D)) {
         if (!(mc.field_71439_g.field_70163_u > this.maxheight.getValDouble())) {
            double newX = mc.field_71439_g.field_70165_t;
            double newZ = mc.field_71439_g.field_70161_v;
            newX = mc.field_71439_g.field_70165_t > (double)Math.round(mc.field_71439_g.field_70165_t) ? (double)Math.round(mc.field_71439_g.field_70165_t) + 0.5D : newX;
            newX = mc.field_71439_g.field_70165_t < (double)Math.round(mc.field_71439_g.field_70165_t) ? (double)Math.round(mc.field_71439_g.field_70165_t) - 0.5D : newX;
            newZ = mc.field_71439_g.field_70161_v > (double)Math.round(mc.field_71439_g.field_70161_v) ? (double)Math.round(mc.field_71439_g.field_70161_v) + 0.5D : newZ;
            newZ = mc.field_71439_g.field_70161_v < (double)Math.round(mc.field_71439_g.field_70161_v) ? (double)Math.round(mc.field_71439_g.field_70161_v) - 0.5D : newZ;
            this.playerPos = new BlockPos(newX, mc.field_71439_g.field_70163_u, newZ);
            if (mc.field_71441_e.func_180495_p(this.playerPos).func_177230_c() == Blocks.field_150350_a) {
               if (this.JumpOut.getValBoolean() && mc.field_71474_y.field_74314_A.func_151468_f() && mc.field_71441_e.func_180495_p(this.playerPos.func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177968_d()).func_177230_c() != Blocks.field_150350_a) {
                  this.WasJump = true;
               } else {
                  double lMotionX;
                  double lMotionZ;
                  if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177968_d()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2)).func_177230_c() != Blocks.field_150350_a) {
                     lMotionX = Math.floor(mc.field_71439_g.field_70165_t) + 0.5D - mc.field_71439_g.field_70165_t;
                     lMotionZ = Math.floor(mc.field_71439_g.field_70161_v) + 0.5D - mc.field_71439_g.field_70161_v;
                     mc.field_71439_g.field_70159_w = lMotionX / 2.0D;
                     mc.field_71439_g.field_70179_y = lMotionZ / 2.0D;
                  } else if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2)).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2).func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2).func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2).func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(2).func_177968_d()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177979_c(3)).func_177230_c() != Blocks.field_150350_a) {
                     lMotionX = Math.floor(mc.field_71439_g.field_70165_t) + 0.5D - mc.field_71439_g.field_70165_t;
                     lMotionZ = Math.floor(mc.field_71439_g.field_70161_v) + 0.5D - mc.field_71439_g.field_70161_v;
                     mc.field_71439_g.field_70159_w = lMotionX / 2.0D;
                     mc.field_71439_g.field_70179_y = lMotionZ / 2.0D;
                  }

                  if (this.Onerun.getValBoolean()) {
                     this.toggle();
                  }

                  super.onClientTick(event);
               }
            }
         }
      }
   }
}
