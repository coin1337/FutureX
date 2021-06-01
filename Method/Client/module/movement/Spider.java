package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Spider extends Module {
   Setting mode;
   Setting Speed;
   public boolean shouldjump;
   public float forcedYaw;
   public float forcedPitch;

   public Spider() {
      super("Spider", 0, Category.MOVEMENT, "Climb Walls");
      this.mode = Main.setmgr.add(new Setting("Spider mode", this, "Vanilla", new String[]{"NCP", "DEV", "Root", "Vanilla"}));
      this.Speed = Main.setmgr.add(new Setting("Speed", this, 0.2D, 0.0D, 1.0D, false, this.mode, "Vanilla", 1));
      this.shouldjump = true;
   }

   public void onEnable() {
      this.shouldjump = true;
   }

   public void onDisable() {
      this.shouldjump = true;
      mc.field_71439_g.field_70138_W = 0.5F;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Vanilla") && !mc.field_71439_g.func_70617_f_() && mc.field_71439_g.field_70123_F && mc.field_71439_g.field_70181_x < 0.2D) {
         mc.field_71439_g.field_70181_x = this.Speed.getValDouble();
      }

      if (this.mode.getValString().equalsIgnoreCase("Root") && mc.field_71439_g.field_70123_F && mc.field_71439_g.field_70173_aa % 4 == 0) {
         mc.field_71439_g.field_70181_x = 0.25D;
      }

      if (this.mode.getValString().equalsIgnoreCase("DEV") && mc.field_71439_g.field_70123_F) {
         if (mc.field_71439_g.field_70173_aa % 4 == 0) {
            mc.field_71439_g.field_70181_x = 0.5D;
         } else {
            mc.field_71439_g.field_70181_x = -0.01D;
         }
      }

      super.onClientTick(event);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.mode.getValString().equalsIgnoreCase("NCP")) {
         if (packet instanceof CPacketKeepAlive) {
            CPacketKeepAlive packet2 = (CPacketKeepAlive)packet;
            if (mc.field_71439_g.field_70123_F) {
               EntityPlayerSP var10000 = mc.field_71439_g;
               var10000.field_70159_w *= 0.0D;
               var10000 = mc.field_71439_g;
               var10000.field_70179_y *= 0.0D;
               if (this.shouldjump) {
                  mc.field_71439_g.func_70664_aZ();
                  this.shouldjump = false;
               }

               if (mc.field_71439_g.field_70143_R > 0.0F) {
                  mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.08D, mc.field_71439_g.field_70161_v);
                  mc.field_71474_y.field_74314_A.field_74513_e = false;
                  mc.field_71439_g.field_70122_E = true;
                  mc.field_71439_g.field_70138_W = 2.0F;
               } else {
                  mc.field_71439_g.field_70138_W = 0.5F;
               }
            } else {
               this.forcedYaw = mc.field_71439_g.field_70177_z;
               this.forcedPitch = mc.field_71439_g.field_70125_A;
               this.shouldjump = true;
               mc.field_71439_g.field_70138_W = 0.5F;
            }
         }

         if (packet instanceof CPacketPlayer) {
            CPacketPlayer packet2 = (CPacketPlayer)packet;
            packet2.field_149474_g = true;
            packet2.field_149476_e = this.forcedYaw;
            packet2.field_149473_f = this.forcedPitch;
         }
      }

      return true;
   }
}
