package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class Phase extends Module {
   Setting mode;
   Setting speed;
   Setting packets;
   private int resetNext;
   public static boolean Finalsep = false;
   public int vanillastage;

   public Phase() {
      super("Phase", 0, Category.MOVEMENT, "Phase through blocks");
      this.mode = Main.setmgr.add(new Setting("Phase Mode", this, "Noclip", new String[]{"Noclip", "Simple", "Destroy", "Glitch", "VClip", "NCPDEV", "HFC", "WinterLithe", "Sand", "Packet", "Skip", "Sneak", "Dpacket"}));
      this.speed = Main.setmgr.add(new Setting("speed", this, 10.0D, 0.1D, 2.0D, false));
      this.packets = Main.setmgr.add(new Setting("packets", this, 5.0D, 1.0D, 10.0D, true));
   }

   public void onEnable() {
      Finalsep = false;
      Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
      mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
      mc.field_71439_g.field_70145_X = true;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.mode.getValString().equalsIgnoreCase("Dpacket") && side == Connection.Side.OUT && mc.field_71439_g.field_70123_F && packet instanceof Position) {
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.08D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.08D, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
         double var2 = -Math.sin(Math.toRadians((double)mc.field_71439_g.field_70177_z));
         double var4 = Math.cos(Math.toRadians((double)mc.field_71439_g.field_70177_z));
         double var6 = (double)mc.field_71439_g.field_71158_b.field_192832_b * var2 + (double)mc.field_71439_g.field_71158_b.field_78902_a * var4;
         double var8 = (double)mc.field_71439_g.field_71158_b.field_192832_b * var4 - (double)mc.field_71439_g.field_71158_b.field_78902_a * var2;
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t + var6, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + var8, false));
         return false;
      } else {
         return !(packet instanceof CPacketPlayer) || packet instanceof Position || !this.mode.getValString().equalsIgnoreCase("noclip");
      }
   }

   public void onDisable() {
      this.vanillastage = 0;
      Finalsep = false;
      mc.field_71439_g.field_70181_x = 0.0D;
      Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
      mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
      mc.field_71439_g.field_70145_X = false;
      mc.field_71439_g.field_71075_bZ.field_75100_b = false;
   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Simple")) {
         MC.field_71439_g.field_70145_X = true;
         MC.field_71439_g.field_70143_R = 0.0F;
         MC.field_71439_g.field_70122_E = false;
         MC.field_71439_g.field_70747_aH = 0.32F;
         EntityPlayerSP var10000;
         if (MC.field_71474_y.field_74314_A.func_151470_d()) {
            var10000 = MC.field_71439_g;
            var10000.field_70181_x += 0.3199999928474426D;
         }

         if (MC.field_71474_y.field_74311_E.func_151470_d()) {
            var10000 = MC.field_71439_g;
            var10000.field_70181_x -= 0.3199999928474426D;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Sneak")) {
         mc.field_71439_g.field_70143_R = 0.0F;
         mc.field_71439_g.field_70122_E = true;
         if (mc.field_71474_y.field_74314_A.func_151468_f()) {
            mc.field_71439_g.func_70016_h(mc.field_71439_g.field_70159_w, 0.1D, mc.field_71439_g.field_70179_y);
         } else if (mc.field_71474_y.field_74311_E.func_151468_f()) {
            mc.field_71439_g.func_70024_g(0.0D, -0.1D, 0.0D);
         } else {
            mc.field_71439_g.func_70016_h(mc.field_71439_g.field_70159_w, 0.0D, mc.field_71439_g.field_70179_y);
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("noclip")) {
         if (mc.field_71439_g != null) {
            mc.field_71439_g.field_70145_X = false;
         }

         if (mc.field_71441_e != null && event.getEntity() == mc.field_71439_g) {
            mc.field_71439_g.field_70145_X = true;
            mc.field_71439_g.field_70122_E = false;
            mc.field_71439_g.field_70143_R = 0.0F;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("SAND") && mc.field_71474_y.field_74314_A.func_151470_d() && mc.field_71439_g.func_184187_bx() != null && mc.field_71439_g.func_184187_bx() instanceof EntityBoat) {
         EntityBoat boat = (EntityBoat)mc.field_71439_g.func_184187_bx();
         if (boat.field_70122_E) {
            boat.field_70181_x = 0.41999998688697815D;
         }
      }

      Vec3d dir;
      if (this.mode.getValString().equalsIgnoreCase("PACKET")) {
         dir = direction(mc.field_71439_g.field_70177_z);
         if (mc.field_71439_g.field_70122_E && mc.field_71439_g.field_70123_F) {
            mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t + dir.field_72450_a * 9.999999747378752E-6D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + dir.field_72449_c * 9.999999747378752E-5D, mc.field_71439_g.field_70122_E));
            mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t + dir.field_72450_a * 2.0D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + dir.field_72449_c * 2.0D, mc.field_71439_g.field_70122_E));
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("SKIP")) {
         dir = direction(mc.field_71439_g.field_70177_z);
         if (mc.field_71439_g.field_70122_E && mc.field_71439_g.field_70123_F) {
            mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
            mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t + dir.field_72450_a * 0.0010000000474974513D, mc.field_71439_g.field_70163_u + 0.10000000149011612D, mc.field_71439_g.field_70161_v + dir.field_72449_c * 0.0010000000474974513D, mc.field_71439_g.field_70122_E));
            mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t + dir.field_72450_a * 0.029999999329447746D, 0.0D, mc.field_71439_g.field_70161_v + dir.field_72449_c * 0.029999999329447746D, mc.field_71439_g.field_70122_E));
            mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t + dir.field_72450_a * 0.05999999865889549D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + dir.field_72449_c * 0.05999999865889549D, mc.field_71439_g.field_70122_E));
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("NCPDEV")) {
         mc.field_71439_g.field_70145_X = true;
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 1.0E-5D, mc.field_71439_g.field_70161_v);
         mc.field_71439_g.field_70181_x = -10.0D;
      }

      EnumFacing var4;
      if (this.mode.getValString().equalsIgnoreCase("Glitch")) {
         mc.field_71439_g.field_70145_X = true;
         var4 = mc.field_71439_g.func_174811_aO();
         mc.field_71439_g.field_70181_x = 0.0D;
         if (mc.field_71439_g.field_71158_b.field_78901_c) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 2.0D, mc.field_71439_g.field_70161_v);
         }

         if (mc.field_71439_g.field_71158_b.field_78899_d) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 1.0E-5D, mc.field_71439_g.field_70161_v);
            mc.field_71439_g.field_70181_x = -1000.0D;
         }

         if (Utils.isMoving(mc.field_71439_g)) {
            double[] directionSpeedVanilla;
            if (var4.func_176610_l().equals(EnumFacing.NORTH.func_176610_l())) {
               mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v - 0.001D);
               if (isInsideBlock() && mc.field_71439_g.field_70173_aa % 10 == 0) {
                  mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v - 0.2D);
                  directionSpeedVanilla = Utils.directionSpeed(1.5D);
                  mc.field_71439_g.field_70159_w = directionSpeedVanilla[0];
                  mc.field_71439_g.field_70179_y = directionSpeedVanilla[1];
               }
            }

            if (var4.func_176610_l().equals(EnumFacing.SOUTH.func_176610_l())) {
               mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + 0.001D);
               if (isInsideBlock() && mc.field_71439_g.field_70173_aa % 10 == 0) {
                  mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + 0.2D);
                  directionSpeedVanilla = Utils.directionSpeed(1.5D);
                  mc.field_71439_g.field_70159_w = directionSpeedVanilla[0];
                  mc.field_71439_g.field_70179_y = directionSpeedVanilla[1];
               }
            }

            if (var4.func_176610_l().equals(EnumFacing.WEST.func_176610_l())) {
               mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t - 0.001D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
               if (isInsideBlock() && mc.field_71439_g.field_70173_aa % 10 == 0) {
                  mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t - 0.2D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
                  directionSpeedVanilla = Utils.directionSpeed(1.5D);
                  mc.field_71439_g.field_70159_w = directionSpeedVanilla[0];
                  mc.field_71439_g.field_70179_y = directionSpeedVanilla[1];
               }
            }

            if (var4.func_176610_l().equals(EnumFacing.EAST.func_176610_l())) {
               mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t + 0.001D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
               if (isInsideBlock() && mc.field_71439_g.field_70173_aa % 10 == 0) {
                  mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t + 0.2D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
                  directionSpeedVanilla = Utils.directionSpeed(1.5D);
                  mc.field_71439_g.field_70159_w = directionSpeedVanilla[0];
                  mc.field_71439_g.field_70179_y = directionSpeedVanilla[1];
               }
            }
         }
      }

      double xOff;
      double var1;
      if (this.mode.getValString().equalsIgnoreCase("Sand")) {
         var1 = mc.field_71439_g.field_70165_t;
         double y = mc.field_71439_g.field_70163_u - 3.0D;
         xOff = mc.field_71439_g.field_70161_v;
         if (this.vanillastage == 0) {
            int i;
            for(i = 0; i < 100; ++i) {
               Wrapper.INSTANCE.sendPacket(new PositionRotation(var1, mc.field_71439_g.field_70163_u - 1.0001D, xOff, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, mc.field_71439_g.field_70122_E));
               mc.field_71439_g.func_70107_b(var1, mc.field_71439_g.field_70163_u - 1.0001D, xOff);
               Wrapper.INSTANCE.sendPacket(new PositionRotation(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, mc.field_71439_g.field_70122_E));
            }

            for(i = 0; i < 10; ++i) {
               Wrapper.INSTANCE.sendPacket(new PositionRotation(var1, y, xOff, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, mc.field_71439_g.field_70122_E));
               mc.field_71439_g.func_70107_b(var1, y, xOff);
               Wrapper.INSTANCE.sendPacket(new PositionRotation(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, mc.field_71439_g.field_70122_E));
               Wrapper.INSTANCE.sendPacket(new PositionRotation(var1, y, xOff, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, mc.field_71439_g.field_70122_E));
               mc.field_71439_g.func_70107_b(var1, y, xOff);
            }

            ++this.vanillastage;
         }

         if (this.vanillastage > 0) {
            mc.field_71439_g.field_70145_X = true;
            Finalsep = true;
         }
      }

      var1 = -this.speed.getValDouble();
      double[] directionSpeedVanilla;
      if (this.mode.getValString().equalsIgnoreCase("HFC")) {
         directionSpeedVanilla = Utils.directionSpeed(this.speed.getValDouble());
         mc.field_71439_g.field_70159_w = directionSpeedVanilla[0];
         mc.field_71439_g.field_70179_y = directionSpeedVanilla[1];
         var4 = mc.field_71439_g.func_174811_aO();
         if (Utils.isMoving(mc.field_71439_g)) {
            if (!mc.field_71439_g.field_70122_E && !isInsideBlock() && mc.field_71439_g.func_70093_af()) {
               mc.field_71439_g.field_70181_x = 0.0D;
            } else {
               mc.field_71439_g.field_70181_x = 100000.0D;
            }

            if (mc.field_71439_g.field_70173_aa % 2 == 0) {
               if (var4.func_176610_l().equals(EnumFacing.NORTH.func_176610_l())) {
                  mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, 0.0D, -3.0D);
               }

               if (var4.func_176610_l().equals(EnumFacing.SOUTH.func_176610_l())) {
                  mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, 0.0D, 3.0D);
               }

               if (var4.func_176610_l().equals(EnumFacing.WEST.func_176610_l())) {
                  mc.field_71439_g.func_174813_aQ().func_72317_d(-3.0D, 0.0D, 0.0D);
               }

               if (var4.func_176610_l().equals(EnumFacing.EAST.func_176610_l())) {
                  mc.field_71439_g.func_174813_aQ().func_72317_d(3.0D, 0.0D, 0.0D);
               }

               for(int i = 0; i < (int)this.packets.getValDouble(); ++i) {
                  if (var4.func_176610_l().equals(EnumFacing.NORTH.func_176610_l())) {
                     Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v - 3.0D, true));
                  }

                  if (var4.func_176610_l().equals(EnumFacing.SOUTH.func_176610_l())) {
                     Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + 3.0D, true));
                  }

                  if (var4.func_176610_l().equals(EnumFacing.WEST.func_176610_l())) {
                     Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t - 3.0D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, true));
                  }

                  if (var4.func_176610_l().equals(EnumFacing.EAST.func_176610_l())) {
                     Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t + 3.0D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, true));
                  }
               }
            } else {
               Wrapper.INSTANCE.sendPacket(new Position(5000.0D, mc.field_71439_g.field_70163_u, 5000.0D, true));
            }
         }

         mc.field_71439_g.field_70145_X = true;
      } else if (this.mode.getValString().equalsIgnoreCase("WinterLithe")) {
         if (isInsideBlock() && mc.field_71474_y.field_74314_A.field_74513_e || !isInsideBlock() && mc.field_71439_g.func_70046_E() != null && mc.field_71439_g.func_70046_E().field_72337_e > mc.field_71439_g.func_70046_E().field_72338_b && mc.field_71439_g.func_70093_af() && mc.field_71439_g.field_70123_F) {
            --this.resetNext;
            double mx = Math.cos(Math.toRadians((double)(mc.field_71439_g.field_70177_z + 90.0F)));
            double mz = Math.sin(Math.toRadians((double)(mc.field_71439_g.field_70177_z + 90.0F)));
            xOff = (double)mc.field_71439_g.field_191988_bg * 1.2D * mx + (double)mc.field_71439_g.field_191988_bg * 1.2D * mz;
            double zOff = (double)mc.field_71439_g.field_191988_bg * 1.2D * mz - (double)mc.field_71439_g.field_191988_bg * 1.2D * mx;
            if (isInsideBlock()) {
               this.resetNext = 1;
            }

            if (this.resetNext > 0) {
               mc.field_71439_g.func_174813_aQ().func_72317_d(xOff, 0.0D, zOff);
            }
         }
      } else if (this.mode.getValString().equalsIgnoreCase("Vclip")) {
         if (mc.field_71474_y.field_74314_A.field_74513_e) {
            mc.field_71439_g.field_70145_X = true;
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + this.speed.getValDouble(), mc.field_71439_g.field_70161_v);
            mc.field_71439_g.field_70181_x = this.speed.getValDouble();
         }

         if (mc.field_71474_y.field_74311_E.field_74513_e) {
            mc.field_71439_g.field_70145_X = true;
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + var1, mc.field_71439_g.field_70161_v);
            mc.field_71439_g.field_70181_x = var1;
         }
      } else if (this.mode.getValString().equalsIgnoreCase("MotionY") && Utils.isMoving(mc.field_71439_g)) {
         var4 = mc.field_71439_g.func_174811_aO();
         if (var4.func_176610_l().equals(EnumFacing.NORTH.func_176610_l())) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v - 3.0D);
         }

         if (var4.func_176610_l().equals(EnumFacing.SOUTH.func_176610_l())) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + 3.0D);
         }

         if (var4.func_176610_l().equals(EnumFacing.WEST.func_176610_l())) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t - 3.0D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
         }

         if (var4.func_176610_l().equals(EnumFacing.EAST.func_176610_l())) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t + 3.0D, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
         }

         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
         mc.field_71439_g.field_70181_x = -1000.0D;
         mc.field_71439_g.field_70145_X = true;
         directionSpeedVanilla = Utils.directionSpeed(this.speed.getValDouble());
         mc.field_71439_g.field_70159_w = directionSpeedVanilla[0];
         mc.field_71439_g.field_70179_y = directionSpeedVanilla[1];
      }

      if (this.mode.getValString().equalsIgnoreCase("destroy")) {
         directionSpeedVanilla = Utils.directionSpeed(1.0D);
         if (mc.field_71439_g.field_70123_F) {
            mc.field_71441_e.func_175655_b(new BlockPos(mc.field_71439_g.field_70165_t + directionSpeedVanilla[0], mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + directionSpeedVanilla[1]), false);
            mc.field_71441_e.func_175655_b(new BlockPos(mc.field_71439_g.field_70165_t + directionSpeedVanilla[0], mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v + directionSpeedVanilla[1]), false);
         }

         if (Utils.isMoving(mc.field_71439_g) && mc.field_71439_g.field_70122_E) {
            double[] directionSpeedVanilla = Utils.directionSpeed(0.23000000298023224D);
            mc.field_71439_g.field_70159_w = directionSpeedVanilla[0];
            mc.field_71439_g.field_70179_y = directionSpeedVanilla[1];
         }
      }

   }

   public boolean canPhase() {
      return !mc.field_71439_g.func_70617_f_() && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab();
   }

   public static boolean isInsideBlock() {
      for(int x = (int)((AxisAlignedBB)Objects.requireNonNull(mc.field_71439_g.func_70046_E())).field_72340_a; (double)x < mc.field_71439_g.func_70046_E().field_72336_d + 1.0D; ++x) {
         for(int y = (int)mc.field_71439_g.func_70046_E().field_72338_b; (double)y < mc.field_71439_g.func_70046_E().field_72337_e + 1.0D; ++y) {
            for(int z = (int)mc.field_71439_g.func_70046_E().field_72339_c; (double)z < mc.field_71439_g.func_70046_E().field_72334_f + 1.0D; ++z) {
               Block block = mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
               AxisAlignedBB boundingBox;
               if (!(block instanceof BlockAir) && (boundingBox = block.func_180646_a(mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)), mc.field_71441_e, new BlockPos(x, y, z))) != null && mc.field_71439_g.func_70046_E().func_72326_a(boundingBox)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static Vec3d direction(float yaw) {
      return new Vec3d(Math.cos(degToRad((double)(yaw + 90.0F))), 0.0D, Math.sin(degToRad((double)(yaw + 90.0F))));
   }

   public static double degToRad(double deg) {
      return deg * 0.01745329238474369D;
   }
}
