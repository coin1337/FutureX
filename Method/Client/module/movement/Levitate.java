package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Wrapper;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Levitate extends Module {
   Setting mode;
   private double startY;
   int counter;

   public Levitate() {
      super("Levitate", 0, Category.MOVEMENT, "Levitate");
      this.mode = Main.setmgr.add(new Setting("Fly Mode", this, "Normal", new String[]{"Normal", "Weird", "Old", "MoonWalk"}));
   }

   public void onClientTick(ClientTickEvent event) {
      EntityPlayerSP var10000;
      if (this.mode.getValString().equalsIgnoreCase("Moonwalk")) {
         if (mc.field_71439_g.field_70122_E && Wrapper.mc.field_71474_y.field_74314_A.func_151468_f()) {
            mc.field_71439_g.field_70181_x = 0.25D;
         } else if (mc.field_71439_g.field_70160_al && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_70617_f_() && !mc.field_71439_g.func_70055_a(Material.field_151587_i)) {
            mc.field_71439_g.field_70181_x = 1.0E-6D;
            EntityPlayerSP player = mc.field_71439_g;
            var10000 = mc.field_71439_g;
            var10000.field_70747_aH *= 1.21337F;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Normal")) {
         mc.field_71439_g.field_70181_x = 0.0D;
         if (mc.field_71474_y.field_74311_E.field_74513_e) {
            mc.field_71439_g.field_70181_x = -0.1D;
         }

         if (mc.field_71474_y.field_74314_A.field_74513_e) {
            mc.field_71439_g.field_70181_x = 0.1D;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Old") && !mc.field_71439_g.field_70122_E && mc.field_71474_y.field_74314_A.field_74513_e) {
         mc.field_71439_g.field_70181_x = mc.field_71439_g.field_70163_u < this.startY - 1.0D ? 0.2D : -0.05D;
      }

      if (this.mode.getValString().equalsIgnoreCase("Weird")) {
         ++this.counter;
         if ((double)this.counter > 3.2D) {
            mc.field_71474_y.field_74311_E.field_74513_e = true;
            var10000 = mc.field_71439_g;
            var10000.field_70159_w *= 1.2D;
            mc.field_71439_g.field_70739_aP = 1.0F;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= 1.2D;
            this.counter = 0;
         } else {
            ++this.counter;
         }

         if ((double)this.counter > 3.7D) {
            mc.field_71474_y.field_74311_E.field_74513_e = false;
            this.counter = 0;
         }

         mc.field_71439_g.field_70122_E = true;
         mc.field_71439_g.field_70181_x = 0.0D;
         var10000 = mc.field_71439_g;
         var10000.field_70159_w *= 0.2D;
         mc.field_71439_g.field_70739_aP = 1.0F;
         var10000 = mc.field_71439_g;
         var10000.field_70179_y *= 0.2D;
         mc.field_71439_g.func_174829_m();
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0E-9D, mc.field_71439_g.field_70161_v);
         if (mc.field_71439_g.field_70173_aa % 3 == 0 && mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.2D, mc.field_71439_g.field_70161_v)).func_177230_c() instanceof BlockAir) {
            mc.field_71439_g.field_71174_a.func_147297_a(new PositionRotation(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + -0.0D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
         }
      }

      super.onClientTick(event);
   }

   public void onEnable() {
      super.onEnable();
      this.startY = mc.field_71439_g.field_70163_u;
      if (this.mode.getValString().equalsIgnoreCase("Weird")) {
         mc.field_71439_g.field_70181_x = 0.42D;

         for(int i2 = 1; i2 < 4; ++i2) {
            mc.field_71439_g.field_70738_aO = 9;
            mc.field_71439_g.func_70057_ab();
            mc.field_71439_g.field_70143_R = 0.0F;
         }
      }

   }
}
