package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiFall extends Module {
   private int state;
   private double fall;
   Setting mode;
   public TimerUtils timer;

   public AntiFall() {
      super("NoFall", 0, Category.MOVEMENT, "Take no fall damage");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Vanilla", new String[]{"Vanilla", "LAAC", "Hypixel", "SpoofGround", "NoGround", "AAC", "AAC3.3.15", "Spartan", "Quick", "NCP"}));
      this.timer = new TimerUtils();
   }

   public void onEnable() {
      super.onEnable();
      this.fall = 0.0D;
   }

   public double getDistanceToGround() {
      for(int var3 = 0; var3 < 256; ++var3) {
         BlockPos var4 = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - (double)var3, mc.field_71439_g.field_70161_v);
         if (mc.field_71441_e.func_180495_p(var4).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(var4).func_177230_c() != Blocks.field_150349_c && mc.field_71441_e.func_180495_p(var4).func_177230_c() != Blocks.field_150329_H && mc.field_71441_e.func_180495_p(var4).func_177230_c() != Blocks.field_150328_O && mc.field_71441_e.func_180495_p(var4).func_177230_c() != Blocks.field_150327_N) {
            double var1 = mc.field_71439_g.field_70163_u - (double)var4.func_177956_o();
            return var1 - 1.0D;
         }
      }

      return 256.0D;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("NCP")) {
         Block block = mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 6.0D, mc.field_71439_g.field_70161_v)).func_177230_c();
         Block block2 = mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 5.0D, mc.field_71439_g.field_70161_v)).func_177230_c();
         Block block3 = mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 4.0D, mc.field_71439_g.field_70161_v)).func_177230_c();
         if ((block != Blocks.field_150350_a || block2 != Blocks.field_150350_a || block3 != Blocks.field_150350_a) && mc.field_71439_g.field_70143_R > 2.0F) {
            mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.1D, mc.field_71439_g.field_70161_v, false));
            mc.field_71439_g.field_70181_x = -10.0D;
            mc.field_71439_g.field_70143_R = MathHelper.field_180189_a;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Vanilla") && mc.field_71439_g.field_70143_R > 2.0F) {
         Wrapper.INSTANCE.sendPacket(new CPacketPlayer(true));
      }

      if (this.mode.getValString().equalsIgnoreCase("Quick") && (double)mc.field_71439_g.field_70143_R > 3.1D) {
         if (this.getDistanceToGround() > 40.0D) {
            return;
         }

         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.5D, mc.field_71439_g.field_70161_v, true));
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.5D, mc.field_71439_g.field_70161_v, true));
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, -9.0D, mc.field_71439_g.field_70161_v, true));
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, true));
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + this.getDistanceToGround(), mc.field_71439_g.field_70161_v, true));
         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_70181_x += 0.3D;
      }

      if (this.mode.getValString().equalsIgnoreCase("LAAC") && mc.field_71439_g != null && mc.field_71441_e != null && mc.field_71439_g.field_70143_R > 2.0F && mc.field_71439_g.field_70173_aa % 6 == 0) {
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - (double)mc.field_71439_g.field_70143_R, mc.field_71439_g.field_70161_v);
      }

      if (this.mode.getValString().equalsIgnoreCase("AAC")) {
         if (mc.field_71439_g.field_70143_R > 2.0F) {
            Wrapper.INSTANCE.sendPacket(new CPacketPlayer(true));
            this.state = 2;
         } else if (this.state == 2 && mc.field_71439_g.field_70143_R < 2.0F) {
            mc.field_71439_g.field_70181_x = 0.1D;
            this.state = 3;
         }

         switch(this.state) {
         case 3:
            mc.field_71439_g.field_70181_x = 0.1D;
            this.state = 4;
            break;
         case 4:
            mc.field_71439_g.field_70181_x = 0.1D;
            this.state = 5;
            break;
         case 5:
            mc.field_71439_g.field_70181_x = 0.1D;
            this.state = 1;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("aac3.3.15") && mc.field_71439_g.field_70143_R > 2.0F) {
         if (!mc.func_71387_A()) {
            Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, Double.NaN, mc.field_71439_g.field_70161_v, false));
         }

         mc.field_71439_g.field_70143_R = -9999.0F;
      }

      if (this.mode.getValString().equalsIgnoreCase("spartan")) {
         this.timer.reset();
         if ((double)mc.field_71439_g.field_70143_R > 1.5D && this.timer.hasReached(10.0F)) {
            Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 10.0D, mc.field_71439_g.field_70161_v, true));
            Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 10.0D, mc.field_71439_g.field_70161_v, true));
            this.timer.reset();
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("hypixel")) {
         if (!mc.field_71439_g.field_70122_E) {
            if (mc.field_71439_g.field_70181_x < -0.08D) {
               this.fall -= mc.field_71439_g.field_70181_x;
            }

            if (this.fall > 2.0D) {
               this.fall = 0.0D;
               mc.field_71439_g.field_70122_E = false;
            }
         }

         this.fall = 0.0D;
      }

      super.onClientTick(event);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof CPacketPlayer) {
         CPacketPlayer playerPacket = (CPacketPlayer)packet;
         if (this.mode.getValString().equalsIgnoreCase("SpoofGround")) {
            playerPacket.field_149474_g = true;
         }

         if (this.mode.getValString().equalsIgnoreCase("NoGround")) {
            playerPacket.field_149474_g = false;
         }
      }

      return true;
   }
}
