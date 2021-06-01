package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Velocity extends Module {
   Setting mode;
   Setting XMult;
   Setting YMult;
   Setting ZMult;
   Setting onPacket;
   Setting CancelPacket;
   Setting Super;
   Setting Pushspeed;
   Setting Pushstart;
   private double motionX;
   private double motionZ;
   private final TimerUtils timer;

   public Velocity() {
      super("Velocity", 0, Category.COMBAT, "Velocity");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Simple", new String[]{"Simple", "AAC", "Fast", "YPort", "AAC4Flag", "Pull", "Airmove", "HurtPacket"}));
      this.XMult = Main.setmgr.add(new Setting("XMultipl", this, 0.0D, 0.0D, 10.0D, false, this.mode, "Simple", 1));
      this.YMult = Main.setmgr.add(new Setting("YMultipl", this, 0.0D, 0.0D, 10.0D, false, this.mode, "Simple", 2));
      this.ZMult = Main.setmgr.add(new Setting("ZMultipl", this, 0.0D, 0.0D, 10.0D, false, this.mode, "Simple", 3));
      this.onPacket = Main.setmgr.add(new Setting("Only Packet", this, true, this.mode, "Simple", 4));
      this.CancelPacket = Main.setmgr.add(new Setting("CancelPacket", this, true, this.mode, "Simple", 5));
      this.Super = Main.setmgr.add(new Setting("Super", this, true, this.mode, "Pull", 1));
      this.Pushspeed = Main.setmgr.add(new Setting("Pushspeed", this, 0.25D, 1.0E-4D, 0.4D, false, this.mode, "Airmove", 2));
      this.Pushstart = Main.setmgr.add(new Setting("Pushstart", this, 8.0D, 2.0D, 9.0D, false, this.mode, "Airmove", 3));
      this.timer = new TimerUtils();
   }

   public void onClientTick(ClientTickEvent event) {
      EntityPlayerSP var10000;
      if (this.mode.getValString().equalsIgnoreCase("AAC")) {
         if (mc.field_71439_g.field_70737_aN > 0 && mc.field_71439_g.field_70737_aN <= 7) {
            var10000 = mc.field_71439_g;
            var10000.field_70159_w *= 0.5D;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= 0.5D;
         }

         if (mc.field_71439_g.field_70737_aN > 0 && mc.field_71439_g.field_70737_aN < 6) {
            mc.field_71439_g.field_70159_w = 0.0D;
            mc.field_71439_g.field_70179_y = 0.0D;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Fast") && mc.field_71439_g.field_70737_aN < 9 && !mc.field_71439_g.field_70122_E) {
         double yaw = (double)mc.field_71439_g.field_70759_as;
         yaw = Math.toRadians(yaw);
         double dX = -Math.sin(yaw) * 0.08D;
         double dZ = Math.cos(yaw) * 0.08D;
         if (mc.field_71439_g.func_110143_aJ() >= 6.0F) {
            mc.field_71439_g.field_70159_w = dX;
            mc.field_71439_g.field_70179_y = dZ;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Simple") && !this.onPacket.getValBoolean() && mc.field_71439_g.field_70737_aN > 0 && mc.field_71439_g.field_70143_R < 3.0F && this.timer.isDelay(100L)) {
         if (Utils.isMovinginput()) {
            var10000 = mc.field_71439_g;
            var10000.field_70159_w *= this.XMult.getValDouble();
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= this.ZMult.getValDouble();
         } else {
            var10000 = mc.field_71439_g;
            var10000.field_70159_w *= this.XMult.getValDouble() + 0.2D;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= this.ZMult.getValDouble() + 0.2D;
         }

         var10000 = mc.field_71439_g;
         var10000.field_70181_x -= this.YMult.getValDouble();
         var10000 = mc.field_71439_g;
         var10000.field_70181_x += this.YMult.getValDouble();
         this.timer.setLastMS();
      }

      if (this.mode.getValString().equalsIgnoreCase("AAC4Flag") && (mc.field_71439_g.field_70737_aN == 3 || mc.field_71439_g.field_70737_aN == 4)) {
         double[] directionSpeedVanilla = Utils.directionSpeed(0.05D);
         mc.field_71439_g.field_70159_w = directionSpeedVanilla[0];
         mc.field_71439_g.field_70179_y = directionSpeedVanilla[1];
      }

      if (this.mode.getValString().equalsIgnoreCase("Pull")) {
         if (mc.field_71439_g.field_70737_aN == 9) {
            this.motionX = mc.field_71439_g.field_70159_w;
            this.motionZ = mc.field_71439_g.field_70179_y;
         }

         if (this.Super.getValBoolean()) {
            if (mc.field_71439_g.field_70737_aN == 8) {
               mc.field_71439_g.field_70159_w = -this.motionX * 0.45D;
               mc.field_71439_g.field_70179_y = -this.motionZ * 0.45D;
            }
         } else if (mc.field_71439_g.field_70737_aN == 4) {
            mc.field_71439_g.field_70159_w = -this.motionX * 0.6D;
            mc.field_71439_g.field_70179_y = -this.motionZ * 0.6D;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Airmove")) {
         if (mc.field_71439_g.field_70737_aN == 9) {
            this.motionX = mc.field_71439_g.field_70159_w;
            this.motionZ = mc.field_71439_g.field_70179_y;
         } else if ((double)mc.field_71439_g.field_70737_aN == this.Pushstart.getValDouble() - 1.0D) {
            var10000 = mc.field_71439_g;
            var10000.field_70159_w *= -this.Pushspeed.getValDouble();
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= -this.Pushspeed.getValDouble();
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("HurtPacket") && mc.field_71439_g.field_70172_ad > 18) {
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 12.0D, mc.field_71439_g.field_70161_v, false));
      }

      super.onClientTick(event);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.mode.getValString().equalsIgnoreCase("Simple") && this.onPacket.getValBoolean()) {
         SPacketEntityVelocity packet2;
         if (this.CancelPacket.getValBoolean()) {
            if (packet instanceof SPacketEntityVelocity) {
               packet2 = (SPacketEntityVelocity)packet;
               return packet2.func_149412_c() != mc.field_71439_g.func_145782_y();
            }

            if (packet instanceof SPacketExplosion && this.YMult.getValDouble() == 0.0D && this.XMult.getValDouble() == 0.0D && this.ZMult.getValDouble() == 0.0D) {
               return false;
            }

            return true;
         }

         if (this.timer.isDelay(100L)) {
            if (packet instanceof SPacketEntityVelocity) {
               packet2 = (SPacketEntityVelocity)packet;
               packet2.field_149416_c = (int)((double)packet2.field_149416_c * this.YMult.getValDouble());
               packet2.field_149415_b = (int)((double)packet2.field_149415_b * this.XMult.getValDouble());
               packet2.field_149414_d = (int)((double)packet2.field_149414_d * this.ZMult.getValDouble());
            }

            if (packet instanceof SPacketExplosion) {
               SPacketExplosion packet2 = (SPacketExplosion)packet;
               packet2.field_149153_g = (float)((double)packet2.field_149153_g * this.YMult.getValDouble());
               packet2.field_149152_f = (float)((double)packet2.field_149152_f * this.XMult.getValDouble());
               packet2.field_149159_h = (float)((double)packet2.field_149159_h * this.ZMult.getValDouble());
            }

            this.timer.setLastMS();
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("YPort") && mc.field_71439_g.field_70737_aN >= 8) {
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70142_S, mc.field_71439_g.field_70137_T + 2.0D, mc.field_71439_g.field_70136_U);
         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_70181_x -= 0.3D;
         var10000 = mc.field_71439_g;
         var10000.field_70159_w *= 0.8D;
         var10000 = mc.field_71439_g;
         var10000.field_70179_y *= 0.8D;
      }

      return true;
   }
}
