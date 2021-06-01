package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Wrapper;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Step extends Module {
   public int ticks = 0;
   Setting mode;
   Setting Height;
   Setting Entity;
   Setting Timer;

   public Step() {
      super("Step", 0, Category.MOVEMENT, "Allows you to step up.");
      this.mode = Main.setmgr.add(new Setting("STEP", this, "Vanilla", new String[]{"Vanilla", "ACC", "Packet", "FastAAC", "NCP", "Hop", "SPAM", "Step"}));
      this.Height = Main.setmgr.add(new Setting("Height", this, 1.0D, 0.5D, 4.0D, true));
      this.Entity = Main.setmgr.add(new Setting("Entity", this, true));
      this.Timer = Main.setmgr.add(new Setting("Timer", this, true, this.mode, "Packet", 3));
   }

   public void onEnable() {
      this.ticks = 0;
      super.onEnable();
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.Entity.getValBoolean() && mc.field_71439_g.func_184187_bx() != null && mc.field_71439_g.func_184187_bx().field_70138_W != (float)((int)this.Height.getValDouble())) {
         mc.field_71439_g.func_184187_bx().field_70138_W = (float)((int)this.Height.getValDouble());
      }

      if (this.mode.getValString().equalsIgnoreCase("Step")) {
         if (mc.field_71439_g.field_70123_F && mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.func_70664_aZ();
         }

         if (mc.field_71439_g.field_70123_F && mc.field_71439_g.field_70122_E && mc.field_71439_g.field_70163_u + 1.065D < mc.field_71439_g.field_70163_u) {
            mc.field_71439_g.func_70016_h(mc.field_71439_g.field_70159_w, -0.1D, mc.field_71439_g.field_70179_y);
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("FastAAC")) {
         BlockPos pos1 = new BlockPos(mc.field_71439_g.field_70165_t + 1.0D, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
         BlockPos pos2 = new BlockPos(mc.field_71439_g.field_70165_t - 1.0D, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
         BlockPos pos3 = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v + 1.0D);
         BlockPos pos4 = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v - 1.0D);
         Block block1 = mc.field_71441_e.func_180495_p(pos1).func_177230_c();
         Block block2 = mc.field_71441_e.func_180495_p(pos2).func_177230_c();
         Block block3 = mc.field_71441_e.func_180495_p(pos3).func_177230_c();
         Block block4 = mc.field_71441_e.func_180495_p(pos4).func_177230_c();
         if (mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d() && mc.field_71439_g.field_70123_F && (block1 == Blocks.field_150350_a || block2 == Blocks.field_150350_a || block3 == Blocks.field_150350_a || block4 == Blocks.field_150350_a)) {
            if (mc.field_71439_g.field_70122_E) {
               mc.field_71439_g.func_70664_aZ();
               mc.field_71439_g.field_70181_x = 0.386D;
            } else {
               toFwd(0.26D);
            }
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Spam")) {
         this.Spam();
      }

      if (this.mode.getValString().equalsIgnoreCase("Packet")) {
         if (mc.field_71439_g.func_70090_H() || mc.field_71439_g.func_180799_ab() || mc.field_71439_g.func_70617_f_() || mc.field_71474_y.field_74314_A.func_151470_d()) {
            return;
         }

         if (this.Timer.getValBoolean()) {
            if (this.ticks == 0) {
               mc.field_71428_T.field_194149_e = 50.0F;
            } else {
               --this.ticks;
            }
         }

         if (mc.field_71439_g != null && mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_70617_f_()) {
            for(double y = 0.0D; y < this.Height.getValDouble() + 0.5D; y += 0.01D) {
               if (!mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, -y, 0.0D)).isEmpty()) {
                  mc.field_71439_g.field_70181_x = -10.0D;
                  break;
               }
            }
         }

         double[] dir = Utils.directionSpeed(0.1D);
         boolean twofive = false;
         boolean two = false;
         boolean onefive = false;
         boolean one = false;
         if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.6D, dir[1])).isEmpty() && !mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.4D, dir[1])).isEmpty()) {
            twofive = true;
         }

         if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.1D, dir[1])).isEmpty() && !mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.9D, dir[1])).isEmpty()) {
            two = true;
         }

         if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.6D, dir[1])).isEmpty() && !mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.4D, dir[1])).isEmpty()) {
            onefive = true;
         }

         if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.0D, dir[1])).isEmpty() && !mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 0.6D, dir[1])).isEmpty()) {
            one = true;
         }

         if (mc.field_71439_g.field_70123_F && (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) && mc.field_71439_g.field_70122_E) {
            int var10;
            double v;
            double[] twoFiveOffset;
            double[] var21;
            int var22;
            if (one && this.Height.getValDouble() >= 1.0D) {
               twoFiveOffset = new double[]{0.42D, 0.753D};
               var21 = twoFiveOffset;
               var22 = twoFiveOffset.length;

               for(var10 = 0; var10 < var22; ++var10) {
                  v = var21[var10];
                  mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + v, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
               }

               if (this.Timer.getValBoolean()) {
                  mc.field_71428_T.field_194149_e = 83.33333F;
               }

               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
               this.ticks = 1;
            }

            if (onefive && this.Height.getValDouble() >= 1.5D) {
               twoFiveOffset = new double[]{0.42D, 0.75D, 1.0D, 1.16D, 1.23D, 1.2D};
               var21 = twoFiveOffset;
               var22 = twoFiveOffset.length;

               for(var10 = 0; var10 < var22; ++var10) {
                  v = var21[var10];
                  mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + v, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
               }

               if (this.Timer.getValBoolean()) {
                  mc.field_71428_T.field_194149_e = 142.85715F;
               }

               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.5D, mc.field_71439_g.field_70161_v);
               this.ticks = 1;
            }

            if (two && this.Height.getValDouble() >= 2.0D) {
               twoFiveOffset = new double[]{0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D};
               var21 = twoFiveOffset;
               var22 = twoFiveOffset.length;

               for(var10 = 0; var10 < var22; ++var10) {
                  v = var21[var10];
                  mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + v, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
               }

               if (this.Timer.getValBoolean()) {
                  mc.field_71428_T.field_194149_e = 200.0F;
               }

               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 2.0D, mc.field_71439_g.field_70161_v);
               this.ticks = 2;
            }

            if (twofive && this.Height.getValDouble() >= 2.5D) {
               twoFiveOffset = new double[]{0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D};
               var21 = twoFiveOffset;
               var22 = twoFiveOffset.length;

               for(var10 = 0; var10 < var22; ++var10) {
                  v = var21[var10];
                  mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + v, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
               }

               if (this.Timer.getValBoolean()) {
                  mc.field_71428_T.field_194149_e = 333.3333F;
               }

               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 2.5D, mc.field_71439_g.field_70161_v);
               this.ticks = 2;
            }
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("ACC")) {
         EntityPlayerSP player = mc.field_71439_g;
         if (player.field_70123_F) {
            switch(this.ticks) {
            case 0:
               if (player.field_70122_E) {
                  player.func_70664_aZ();
               }
               break;
            case 7:
               player.field_70181_x = 0.0D;
               break;
            case 8:
               if (!player.field_70122_E) {
                  player.func_70107_b(player.field_70165_t, player.field_70163_u + 1.0D, player.field_70161_v);
               }
            }

            ++this.ticks;
         } else {
            this.ticks = 0;
         }
      } else if (this.mode.getValString().equalsIgnoreCase("Vanilla")) {
         mc.field_71439_g.field_70138_W = (float)this.Height.getValDouble();
      } else if (this.mode.getValString().equalsIgnoreCase("NCP")) {
         if (mc.field_71439_g.field_70123_F && mc.field_71439_g.field_70122_E && mc.field_71439_g.field_70124_G && mc.field_71439_g.field_70132_H) {
            this.StepRun();
         }
      } else if (this.mode.getValString().equalsIgnoreCase("Hop") && mc.field_71474_y.field_74314_A.field_74513_e && mc.field_71439_g.field_70123_F) {
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70142_S, (double)mc.field_71439_g.field_70117_cu + mc.field_71439_g.field_70137_T + 0.09000000357627869D, (double)mc.field_71439_g.field_70116_cv + mc.field_71439_g.field_70161_v);
      }

      super.onClientTick(event);
   }

   public double get_n_normal() {
      mc.field_71439_g.field_70138_W = 0.5F;
      double max_y = -1.0D;
      AxisAlignedBB grow = mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, 0.05D, 0.0D).func_186662_g(0.05D);
      if (!mc.field_71441_e.func_184144_a(mc.field_71439_g, grow.func_72317_d(0.0D, 2.0D, 0.0D)).isEmpty()) {
         return 100.0D;
      } else {
         Iterator var4 = mc.field_71441_e.func_184144_a(mc.field_71439_g, grow).iterator();

         while(var4.hasNext()) {
            AxisAlignedBB aabb = (AxisAlignedBB)var4.next();
            if (aabb.field_72337_e > max_y) {
               max_y = aabb.field_72337_e;
            }
         }

         return max_y - mc.field_71439_g.field_70163_u;
      }
   }

   public static void toFwd(double speed) {
      float yaw = mc.field_71439_g.field_70177_z * 0.017453292F;
      EntityPlayerSP var10000 = mc.field_71439_g;
      var10000.field_70159_w -= (double)MathHelper.func_76126_a(yaw) * speed;
      var10000 = mc.field_71439_g;
      var10000.field_70179_y += (double)MathHelper.func_76134_b(yaw) * speed;
   }

   private void Spam() {
      if (mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70617_f_() && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab() && !mc.field_71439_g.field_71158_b.field_78901_c && !mc.field_71439_g.field_70145_X && (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F)) {
         double n = this.get_n_normal();
         if (n < 0.0D || n > 2.0D) {
            return;
         }

         if (n == 2.0D) {
            this.Sendpos(0.42D);
            this.Sendpos(0.78D);
            this.Sendpos(0.63D);
            this.Sendpos(0.51D);
            this.Sendpos(0.9D);
            this.Sendpos(1.21D);
            this.Sendpos(1.45D);
            this.Sendpos(1.43D);
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 2.0D, mc.field_71439_g.field_70161_v);
         }

         if (n == 1.5D) {
            this.Sendpos(0.41999998688698D);
            this.Sendpos(0.7531999805212D);
            this.Sendpos(1.00133597911214D);
            this.Sendpos(1.16610926093821D);
            this.Sendpos(1.24918707874468D);
            this.Sendpos(1.1707870772188D);
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
         }

         if (n == 1.0D) {
            this.Sendpos(0.41999998688698D);
            this.Sendpos(0.7531999805212D);
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
         }
      }

   }

   private void Sendpos(double pos) {
      mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + pos, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
   }

   private void StepRun() {
      mc.field_71439_g.func_70031_b(true);
      Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.42D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
      mc.field_71439_g.func_70031_b(true);
      Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.753D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
      mc.field_71439_g.func_70031_b(true);
      mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
      mc.field_71439_g.func_70031_b(true);
   }

   public void onDisable() {
      mc.field_71439_g.field_70138_W = 0.5F;
      mc.field_71428_T.field_194149_e = 50.0F;
      super.onDisable();

      try {
         if (mc.field_71439_g.func_184187_bx() != null) {
            mc.field_71439_g.func_184187_bx().field_70138_W = 1.0F;
         }
      } catch (Exception var2) {
      }

   }
}
