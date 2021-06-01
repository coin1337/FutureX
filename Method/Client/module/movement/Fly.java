package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Fly extends Module {
   int clock = 0;
   private boolean aac;
   private double aad;
   Setting mode;
   Setting speed;
   Setting speed2;
   Setting speed3;

   public Fly() {
      super("Fly", 0, Category.MOVEMENT, "Fly me to the skys");
      this.mode = Main.setmgr.add(new Setting("Fly Mode", this, "Vanilla", new String[]{"Vanilla", "Motion", "Tp", "Servers", "NPacket", "BPacket", "CubeCraft", "Old AAC", "Rewinside", "Clicktp", "AAC"}));
      this.speed = Main.setmgr.add(new Setting("Speed", this, 1.0D, 0.5D, 8.0D, false, this.mode, "Vanilla", 1));
      this.speed2 = Main.setmgr.add(new Setting("NSpeed", this, 1.5D, 0.5D, 5.0D, false, this.mode, "Tp", 1));
      this.speed3 = Main.setmgr.add(new Setting("Speed3", this, 3.0D, 0.5D, 5.0D, false, this.mode, "Motion", 1));
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("AAC")) {
         mc.field_71439_g.func_70031_b(false);
         if (mc.field_71439_g.field_70143_R >= 4.0F && !this.aac) {
            this.aac = true;
            this.aad = mc.field_71439_g.field_70163_u + 3.0D;
            mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, true));
         }

         mc.field_71439_g.field_71075_bZ.field_75100_b = false;
         if (this.aac) {
            if (mc.field_71439_g.field_70122_E) {
               this.aac = false;
            }

            if (mc.field_71439_g.field_70163_u < this.aad) {
               mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, true));
               if (mc.field_71474_y.field_74311_E.field_74513_e) {
                  this.aad -= 2.0D;
               } else if (mc.field_71474_y.field_74311_E.field_74513_e && mc.field_71439_g.field_70163_u < this.aad + 0.8D) {
                  this.aad += 2.0D;
               } else {
                  mc.field_71439_g.field_70181_x = 0.7D;
                  this.utils(0.8F);
               }
            }
         } else {
            mc.field_71439_g.field_71075_bZ.field_75100_b = false;
         }
      }

      double y1;
      if (this.mode.getValString().equalsIgnoreCase("Clicktp") && mc.field_71474_y.field_74312_F.field_74513_e) {
         mc.field_71439_g.field_71075_bZ.field_75100_b = true;
         y1 = (double)mc.field_71439_g.field_70177_z;
         float increment = 8.5F;
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t + Math.sin(Math.toRadians(-y1)) * (double)increment, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + Math.cos(Math.toRadians(-y1)) * (double)increment);
      }

      double speed;
      if (this.mode.getValString().equalsIgnoreCase("CubeCraft") && mc.field_71439_g != null && mc.field_71441_e != null) {
         y1 = 1.0D;
         speed = this.getPosForSetPosX(y1);
         double z = this.getPosForSetPosZ(y1);
         mc.field_71439_g.field_70181_x = -0.25D;
         if (mc.field_71439_g.field_70143_R >= 0.8F) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t + speed, mc.field_71439_g.field_70163_u + ((double)mc.field_71439_g.field_70143_R - 0.15D), mc.field_71439_g.field_70161_v + z);
            mc.field_71439_g.field_70143_R = 0.0F;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Old AAC") && mc.field_71441_e != null && mc.field_71439_g != null && mc.field_71439_g.field_70143_R > 0.0F) {
         mc.field_71439_g.field_70181_x = mc.field_71439_g.field_70173_aa % 2 == 0 ? 0.1D : 0.0D;
      }

      if (this.mode.getValString().equalsIgnoreCase("Rewinside") && mc.field_71439_g != null && mc.field_71441_e != null) {
         mc.field_71474_y.field_74370_x.field_74513_e = false;
         mc.field_71474_y.field_74366_z.field_74513_e = false;
         mc.field_71474_y.field_74368_y.field_74513_e = false;
         mc.field_71474_y.field_74314_A.field_74513_e = false;
         mc.field_71474_y.field_151444_V.field_74513_e = false;
         mc.field_71439_g.func_70031_b(false);
         mc.field_71439_g.field_70181_x = 0.0D;
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0E-10D, mc.field_71439_g.field_70161_v);
         mc.field_71439_g.field_70122_E = true;
         if (mc.field_71439_g.field_70173_aa % 3 == 0) {
            mc.field_71439_g.field_71174_a.func_147297_a(new PositionRotation(mc.field_71439_g.field_70165_t + mc.field_71439_g.field_70159_w, mc.field_71439_g.field_70163_u - 1.0E-10D, mc.field_71439_g.field_70161_v + mc.field_71439_g.field_70179_y, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
         }
      }

      float strafe;
      EntityPlayerSP player;
      if (this.mode.getValString().equalsIgnoreCase("Tp")) {
         player = mc.field_71439_g;
         player.field_71075_bZ.field_75100_b = false;
         strafe = (float)(this.speed2.getValDouble() * 0.5D);
         double[] directionSpeedVanilla = Utils.directionSpeed((double)strafe);
         if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
            player.func_70107_b(player.field_70165_t + directionSpeedVanilla[0], player.field_70163_u, player.field_70161_v + directionSpeedVanilla[1]);
         }

         player.field_70159_w = 0.0D;
         player.field_70179_y = 0.0D;
         player.field_70181_x = 0.0D;
         player.func_70016_h(0.0D, 0.0D, 0.0D);
         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
            player.func_70107_b(player.field_70165_t, player.field_70163_u + this.speed2.getValDouble(), player.field_70161_v);
            player.field_70181_x = 0.0D;
         }

         if (mc.field_71474_y.field_74311_E.func_151470_d() && !player.field_70122_E) {
            player.func_70107_b(player.field_70165_t, player.field_70163_u - this.speed2.getValDouble(), player.field_70161_v);
         }
      }

      float forward;
      if (this.mode.getValString().equalsIgnoreCase("Motion")) {
         forward = (float)(this.speed3.getValDouble() * 0.6000000238418579D);
         double[] directionSpeedVanilla = Utils.directionSpeed((double)forward);
         if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
            mc.field_71439_g.field_70159_w = directionSpeedVanilla[0];
            mc.field_71439_g.field_70179_y = directionSpeedVanilla[1];
         }

         mc.field_71439_g.field_70181_x = 0.0D;
         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
            mc.field_71439_g.field_70181_x = this.speed3.getValDouble();
         }

         if (mc.field_71474_y.field_74311_E.func_151470_d() && !mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.field_70181_x = -this.speed3.getValDouble();
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Vanilla")) {
         player = mc.field_71439_g;
         player.field_71075_bZ.field_75100_b = false;
         player.field_70159_w = 0.0D;
         player.field_70181_x = 0.0D;
         player.field_70179_y = 0.0D;
         player.field_70747_aH = (float)this.speed.getValDouble();
         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
            player.field_70181_x += this.speed.getValDouble();
         }

         if (mc.field_71474_y.field_74311_E.func_151470_d()) {
            player.field_70181_x -= this.speed.getValDouble();
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Servers")) {
         mc.field_71439_g.field_70181_x = 0.0D;
         if (mc.field_71439_g.field_70173_aa % 3 == 0) {
            speed = mc.field_71439_g.field_70163_u - 1.0E-10D;
         }

         y1 = mc.field_71439_g.field_70163_u + 1.0E-10D;
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, y1, mc.field_71439_g.field_70161_v);
      }

      if (this.mode.getValString().equalsIgnoreCase("NPacket")) {
         forward = 0.0F;
         strafe = 0.0F;
         EntityPlayerSP player = mc.field_71439_g;
         float var5 = MathHelper.func_76126_a(mc.field_71439_g.field_70177_z * 3.1415927F / 180.0F);
         float var6 = MathHelper.func_76134_b(mc.field_71439_g.field_70177_z * 3.1415927F / 180.0F);
         if (mc.field_71474_y.field_74351_w.field_74513_e) {
            forward += 0.1F;
         }

         if (mc.field_71474_y.field_74368_y.field_74513_e) {
            forward -= 0.1F;
         }

         if (mc.field_71474_y.field_74370_x.field_74513_e) {
            strafe += 0.01F;
         }

         if (mc.field_71474_y.field_74366_z.field_74513_e) {
            strafe -= 0.01F;
         }

         if (!mc.field_71439_g.field_70128_L) {
            this.Movement(forward, strafe, player, var5, var6);
            this.SendPacket();
         }

         mc.field_71439_g.field_70181_x = 0.0D;
         ++this.clock;
         if (this.clock >= 12) {
            this.clock = 0;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("BPacket")) {
         forward = 0.0F;
         strafe = 0.0F;
         speed = 0.2D;
         EntityPlayerSP player = mc.field_71439_g;
         float var5 = MathHelper.func_76126_a(mc.field_71439_g.field_70177_z * 3.1415927F / 180.0F);
         float var6 = MathHelper.func_76134_b(mc.field_71439_g.field_70177_z * 3.1415927F / 180.0F);
         if (mc.field_71474_y.field_74351_w.field_74513_e) {
            forward += 0.1F;
         }

         if (mc.field_71474_y.field_74368_y.field_74513_e) {
            forward -= 0.1F;
         }

         if (mc.field_71474_y.field_74370_x.field_74513_e) {
            strafe += 0.01F;
         }

         if (mc.field_71474_y.field_74366_z.field_74513_e) {
            strafe -= 0.01F;
         }

         if (!mc.field_71439_g.field_70128_L) {
            this.Movement(forward, strafe, player, var5, var6);
         }

         mc.field_71439_g.field_70181_x = 0.0D;
         ++this.clock;
         if (this.clock >= 2) {
            this.SendPacket();
            this.clock = 0;
         }
      }

      super.onClientTick(event);
   }

   public void utils(float speed) {
      mc.field_71439_g.field_70159_w = -(Math.sin((double)this.aan()) * (double)speed);
      mc.field_71439_g.field_70179_y = Math.cos((double)this.aan()) * (double)speed;
   }

   public float aan() {
      float var1 = mc.field_71439_g.field_70177_z;
      if (mc.field_71439_g.field_191988_bg < 0.0F) {
         var1 += 180.0F;
      }

      float forward = 1.0F;
      if (mc.field_71439_g.field_191988_bg < 0.0F) {
         forward = -0.5F;
      } else if (mc.field_71439_g.field_191988_bg > 0.0F) {
         forward = 0.5F;
      }

      if (mc.field_71439_g.field_70702_br > 0.0F) {
         var1 -= 90.0F * forward;
      }

      if (mc.field_71439_g.field_70702_br < 0.0F) {
         var1 += 90.0F * forward;
      }

      var1 *= 0.017453292F;
      return var1;
   }

   private void Movement(float forward, float strafe, EntityPlayerSP player, float var5, float var6) {
      mc.field_71439_g.field_70159_w = player.field_70165_t * 5.01E-8D;
      double speed = 2.7999100260353087D;
      mc.field_71439_g.field_70179_y = player.field_70161_v * 5.01E-8D;
      mc.field_71439_g.field_70179_y = player.field_70161_v * 5.01E-8D;
      this.Runmove(player);
      this.Runmove(player);
      mc.field_71439_g.field_70159_w = player.field_70165_t * 5.01E-8D;
      double motionX = (double)(strafe * var6 - forward * var5) * speed;
      double motionZ = (double)(forward * var6 + strafe * var5) * speed;
      mc.field_71439_g.field_70159_w = motionX;
      mc.field_71439_g.field_70179_y = motionZ;
      if (!mc.field_71474_y.field_74314_A.field_74513_e) {
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.03948584D, mc.field_71439_g.field_70161_v);
      }

   }

   private void Runmove(EntityPlayerSP player) {
      mc.field_71439_g.field_70179_y = player.field_70161_v * 5.01E-8D;
      mc.field_71439_g.field_70159_w = player.field_70165_t * 5.01E-8D;
      mc.field_71439_g.field_70179_y = player.field_70161_v * 5.01E-8D;
      mc.field_71439_g.field_70159_w = player.field_70165_t * 5.01E-8D;
      mc.field_71439_g.field_70179_y = player.field_70161_v * 5.01E-8D;
      mc.field_71439_g.field_70159_w = player.field_70165_t * 5.01E-8D;
      mc.field_71439_g.field_70179_y = player.field_70161_v * 5.01E-8D;
      mc.field_71439_g.field_70159_w = player.field_70165_t * 5.01E-8D;
      mc.field_71439_g.field_70179_y = player.field_70161_v * 5.01E-8D;
      mc.field_71439_g.field_70159_w = player.field_70165_t * 5.01E-8D;
   }

   private void SendPacket() {
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionRotation(mc.field_71439_g.field_70165_t + mc.field_71439_g.field_70159_w, mc.field_71439_g.field_70163_u + (mc.field_71474_y.field_74314_A.func_151470_d() ? 0.0621D : 0.0D) - (mc.field_71474_y.field_74311_E.func_151470_d() ? 0.0621D : 0.0D), mc.field_71439_g.field_70161_v + mc.field_71439_g.field_70179_y, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false));
      mc.field_71439_g.field_71174_a.func_147297_a(new PositionRotation(mc.field_71439_g.field_70165_t + mc.field_71439_g.field_70159_w, mc.field_71439_g.field_70163_u - 999.0D, mc.field_71439_g.field_70161_v + mc.field_71439_g.field_70179_y, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
   }

   public double getPosForSetPosX(double value) {
      double yaw = Math.toRadians((double)mc.field_71439_g.field_70177_z);
      double x = -Math.sin(yaw) * value;
      return x;
   }

   public double getPosForSetPosZ(double value) {
      double yaw = Math.toRadians((double)mc.field_71439_g.field_70177_z);
      double z = Math.cos(yaw) * value;
      return z;
   }

   public void onDisable() {
      mc.field_71439_g.field_71075_bZ.field_75100_b = false;
      super.onDisable();
   }
}
