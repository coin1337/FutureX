package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class SelfTrap extends Module {
   private BlockPos trap_pos;
   public Setting place_mode;
   public Setting rotate;
   public Setting Hand;

   public SelfTrap() {
      super("SelfTrap", 0, Category.COMBAT, "SelfTrap");
      this.place_mode = Main.setmgr.add(new Setting("cage", this, "Extra", new String[]{"Extra", "Face", "Normal", "Feet"}));
      this.rotate = Main.setmgr.add(new Setting("rotate", this, false));
      this.Hand = Main.setmgr.add(new Setting("Hand", this, "Mainhand", new String[]{"Mainhand", "Offhand", "Both", "None"}));
   }

   public void onClientTick(ClientTickEvent event) {
      Vec3d pos = Utils.interpolateEntity(mc.field_71439_g, mc.func_184121_ak());
      this.trap_pos = new BlockPos(pos.field_72450_a, pos.field_72448_b + 2.0D, pos.field_72449_c);
      if (this.is_trapped()) {
         this.toggle();
      } else {
         Utils.ValidResult result = Utils.valid(this.trap_pos);
         if (result != Utils.ValidResult.AlreadyBlockThere || mc.field_71441_e.func_180495_p(this.trap_pos).func_185904_a().func_76222_j()) {
            if (result == Utils.ValidResult.NoNeighbors) {
               BlockPos[] tests = new BlockPos[]{this.trap_pos.func_177978_c(), this.trap_pos.func_177968_d(), this.trap_pos.func_177974_f(), this.trap_pos.func_177976_e(), this.trap_pos.func_177984_a(), this.trap_pos.func_177977_b().func_177976_e()};
               BlockPos[] var5 = tests;
               int var6 = tests.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  BlockPos pos_ = var5[var7];
                  Utils.ValidResult result_ = Utils.valid(pos_);
                  if (result_ != Utils.ValidResult.NoNeighbors && result_ != Utils.ValidResult.NoEntityCollision && Utils.placeBlock(pos_, this.rotate.getValBoolean(), this.Hand)) {
                     return;
                  }
               }

            } else {
               Utils.placeBlock(this.trap_pos, this.rotate.getValBoolean(), this.Hand);
            }
         }
      }
   }

   public boolean is_trapped() {
      if (this.trap_pos == null) {
         return false;
      } else {
         IBlockState state = mc.field_71441_e.func_180495_p(this.trap_pos);
         return state.func_177230_c() != Blocks.field_150350_a && state.func_177230_c() != Blocks.field_150355_j && state.func_177230_c() != Blocks.field_150353_l;
      }
   }
}
