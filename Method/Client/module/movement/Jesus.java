package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.system.Connection;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Jesus extends Module {
   Setting mode;
   Setting offset;
   Setting Blockdist;
   Setting NoDrown;
   public TimerUtils Delayer;
   int noDown;
   int start;
   int cooldownSpeed;

   public Jesus() {
      super("Jesus", 0, Category.MOVEMENT, "Jesus, Walk on water");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Solid", new String[]{"Solid", "BOUNCE", "FrostWalker", "BunnyHop", "Aac"}));
      this.offset = Main.setmgr.add(new Setting("offset", this, 0.05D, 0.0D, 0.9D, false));
      this.Blockdist = Main.setmgr.add(new Setting("Top Water", this, false, this.mode, "FrostWalker", 2));
      this.NoDrown = Main.setmgr.add(new Setting("NoDrown", this, false));
      this.Delayer = new TimerUtils();
   }

   public void onEnable() {
      this.noDown = 0;
   }

   public void onDisable() {
      this.noDown = 0;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (!this.NoDrown.getValBoolean()) {
         return true;
      } else {
         return !(packet instanceof CPacketPlayerAbilities) || !this.canSave();
      }
   }

   private boolean canSave() {
      boolean swinging = mc.field_71439_g.field_82175_bq;
      Vec3d prevmotion = new Vec3d(mc.field_71439_g.field_70159_w, mc.field_71439_g.field_70181_x, mc.field_71439_g.field_70179_y);
      boolean moving = prevmotion.field_72450_a != 0.0D || !mc.field_71439_g.field_70124_G || mc.field_71474_y.field_74314_A.func_151468_f() || prevmotion.field_72449_c != 0.0D;
      mc.field_71439_g.field_70171_ac = false;
      return mc.field_71439_g.func_70090_H() && !swinging && !moving;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Aac") && mc.field_71439_g.func_70090_H()) {
         ++this.start;
         if (this.start < 4) {
            return;
         }

         ++this.noDown;
         ++this.cooldownSpeed;
         mc.field_71474_y.field_74314_A.field_74513_e = this.noDown < 2;
         mc.field_71474_y.field_74314_A.field_74513_e = true;
         if ((float)this.noDown >= 3.5F) {
            this.noDown = 0;
         }

         if (this.cooldownSpeed >= 3) {
            EntityPlayerSP var10000 = mc.field_71439_g;
            var10000.field_70159_w *= 1.1699999570846558D;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= 1.1699999570846558D;
            this.cooldownSpeed = 0;
            mc.field_71474_y.field_74314_A.field_74513_e = false;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("BunnyHop") && mc.field_71439_g.func_70090_H()) {
         mc.field_71439_g.field_70747_aH = 0.1F;
         mc.field_71439_g.field_70181_x = 0.42D;
      }

      if (this.mode.getValString().equalsIgnoreCase("FrostWalker") && this.Delayer.isDelay(200L)) {
         Iterator var2 = BlockPos.func_191532_a((int)mc.field_71439_g.field_70165_t - 3, (int)mc.field_71439_g.field_70163_u - 2, (int)mc.field_71439_g.field_70161_v - 3, (int)mc.field_71439_g.field_70165_t + 3, (int)mc.field_71439_g.field_70163_u + 2, (int)mc.field_71439_g.field_70161_v + 3).iterator();

         label95:
         while(true) {
            BlockPos b;
            do {
               do {
                  if (!var2.hasNext()) {
                     this.Delayer.setLastMS();
                     break label95;
                  }

                  b = (BlockPos)var2.next();
               } while(mc.field_71441_e.func_180495_p(b).func_177230_c() != Blocks.field_150355_j && mc.field_71441_e.func_180495_p(b).func_177230_c() != Blocks.field_150358_i && mc.field_71441_e.func_180495_p(b).func_177230_c() != Blocks.field_150353_l && mc.field_71441_e.func_180495_p(b).func_177230_c() != Blocks.field_150356_k);
            } while(this.Blockdist.getValBoolean() && mc.field_71441_e.func_180495_p(b.func_177984_a()).func_177230_c() == Blocks.field_150350_a);

            mc.field_71441_e.func_175656_a(b, Blocks.field_185778_de.func_176223_P());
            mc.field_71441_e.func_175684_a(b, Blocks.field_185778_de, MathHelper.func_76136_a(mc.field_71439_g.func_70681_au(), 6, 12));
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Solid")) {
         BlockPos blockPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
         Block block = mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
         if (block.func_176194_O().func_177622_c() == Blocks.field_150355_j || block.func_176194_O().func_177622_c() == Blocks.field_150358_i || block.func_176194_O().func_177622_c() == Blocks.field_150353_l || block.func_176194_O().func_177622_c() == Blocks.field_150356_k || mc.field_71439_g.func_70090_H()) {
            mc.field_71439_g.field_70181_x = 0.0D;
            mc.field_71439_g.field_70747_aH = 0.1F;
            if (mc.field_71439_g.field_70173_aa % 2 == 0) {
               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 2.8471E-6D, mc.field_71439_g.field_70161_v);
            } else {
               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 2.8471E-6D, mc.field_71439_g.field_70161_v);
            }

            mc.field_71439_g.field_70122_E = true;
         }
      }

      super.onClientTick(event);
      if (this.mode.getValString().equalsIgnoreCase("BOUNCE") && !mc.field_71439_g.func_70093_af() && !mc.field_71439_g.field_70145_X && !mc.field_71474_y.field_74314_A.func_151470_d() && isOnLiquid(this.offset.getValDouble())) {
         mc.field_71439_g.field_70181_x = 0.10000000149011612D;
      }

   }

   public static boolean isOnLiquid(double offset) {
      if (mc.field_71439_g.field_70143_R >= 3.0F) {
         return false;
      } else {
         AxisAlignedBB bb = mc.field_71439_g.func_184187_bx() != null ? mc.field_71439_g.func_184187_bx().func_174813_aQ().func_191195_a(0.0D, 0.0D, 0.0D).func_72317_d(0.0D, -offset, 0.0D) : mc.field_71439_g.func_174813_aQ().func_191195_a(0.0D, 0.0D, 0.0D).func_72317_d(0.0D, -offset, 0.0D);
         boolean onLiquid = false;
         int y = (int)bb.field_72338_b;

         for(int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d + 1.0D); ++x) {
            for(int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f + 1.0D); ++z) {
               Block block = mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
               if (block != Blocks.field_150350_a) {
                  if (!(block instanceof BlockLiquid)) {
                     return false;
                  }

                  onLiquid = true;
               }
            }
         }

         return onLiquid;
      }
   }
}
