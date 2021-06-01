package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class Parkour extends Module {
   public TimerUtils timer = new TimerUtils();
   public TimerUtils Delayer = new TimerUtils();
   Setting Nearedge;
   Setting autodrop;
   Setting runsinglegap;
   Setting FullBlock;
   public static List<Block> pospoint5;
   public static List<Block> Replace;
   BlockPos playerPos;

   public Parkour() {
      super("Parkour", 0, Category.MOVEMENT, "Auto Parkour+");
      this.Nearedge = Main.setmgr.add(new Setting("Nearedge", this, 0.001D, 0.0D, 0.01D, false));
      this.autodrop = Main.setmgr.add(new Setting("autodrop", this, false));
      this.runsinglegap = Main.setmgr.add(new Setting("Run Single Gap", this, false));
      this.FullBlock = Main.setmgr.add(new Setting("All Full Blocks", this, false));
   }

   public void setup() {
      Replace = Arrays.asList(Blocks.field_150477_bB, Blocks.field_150434_aF, Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150411_aY, Blocks.field_150367_z, Blocks.field_150378_br, Blocks.field_150415_aT, Blocks.field_150381_bn, Blocks.field_185769_cV, Blocks.field_180396_cN, Blocks.field_150401_cl, Blocks.field_150400_ck, Blocks.field_150370_cb, Blocks.field_150372_bz, Blocks.field_150485_bF, Blocks.field_150487_bG, Blocks.field_150481_bH, Blocks.field_150387_bl, Blocks.field_150476_ad, Blocks.field_150446_ar, Blocks.field_150389_bf, Blocks.field_150390_bg);
      pospoint5 = Arrays.asList(Blocks.field_180405_aT, Blocks.field_180407_aO, Blocks.field_180408_aP, Blocks.field_180404_aQ, Blocks.field_180403_aR, Blocks.field_180406_aS, Blocks.field_180405_aT, Blocks.field_180390_bo, Blocks.field_180391_bp, Blocks.field_180392_bq, Blocks.field_180386_br, Blocks.field_180385_bs, Blocks.field_180387_bt, Blocks.field_150386_bk, Blocks.field_150463_bK);
   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      if (this.FullBlock.getValBoolean() && this.Delayer.isDelay(200L)) {
         Iterator var2 = BlockPos.func_191532_a((int)mc.field_71439_g.field_70165_t - 5, (int)mc.field_71439_g.field_70163_u - 5, (int)mc.field_71439_g.field_70161_v - 5, (int)mc.field_71439_g.field_70165_t + 5, (int)mc.field_71439_g.field_70163_u + 5, (int)mc.field_71439_g.field_70161_v + 5).iterator();

         while(var2.hasNext()) {
            BlockPos b = (BlockPos)var2.next();
            if (pospoint5.contains(mc.field_71441_e.func_180495_p(b).func_177230_c())) {
               BlockPos pos;
               for(pos = new BlockPos(b.field_177962_a, b.field_177960_b + 1, b.field_177961_c); pospoint5.contains(mc.field_71441_e.func_180495_p(pos).func_177230_c()); ++pos.field_177960_b) {
               }

               mc.field_71441_e.func_175656_a(pos, Blocks.field_185771_cX.func_176223_P());
            }

            if (Replace.contains(mc.field_71441_e.func_180495_p(b).func_177230_c())) {
               mc.field_71441_e.func_175656_a(b, Blocks.field_150440_ba.func_176223_P());
            }
         }

         this.Delayer.setLastMS();
      }

      double newX = mc.field_71439_g.field_70165_t;
      double newZ = mc.field_71439_g.field_70161_v;
      newX = mc.field_71439_g.field_70165_t > (double)Math.round(mc.field_71439_g.field_70165_t) ? (double)Math.round(mc.field_71439_g.field_70165_t) + 0.5D : newX;
      newX = mc.field_71439_g.field_70165_t < (double)Math.round(mc.field_71439_g.field_70165_t) ? (double)Math.round(mc.field_71439_g.field_70165_t) - 0.5D : newX;
      newZ = mc.field_71439_g.field_70161_v > (double)Math.round(mc.field_71439_g.field_70161_v) ? (double)Math.round(mc.field_71439_g.field_70161_v) + 0.5D : newZ;
      newZ = mc.field_71439_g.field_70161_v < (double)Math.round(mc.field_71439_g.field_70161_v) ? (double)Math.round(mc.field_71439_g.field_70161_v) - 0.5D : newZ;
      this.playerPos = new BlockPos(newX, mc.field_71439_g.field_70163_u, newZ);
      if (this.autodrop.getValBoolean() && mc.field_71439_g.field_70181_x < -0.01D && !mc.field_71439_g.field_70122_E) {
         for(int i = 0; i < 4; ++i) {
            if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b()).func_177230_c() != Blocks.field_150350_a) {
               EntityPlayerSP var10000 = mc.field_71439_g;
               var10000.field_70159_w -= mc.field_71439_g.field_70159_w / 555.0D;
               var10000 = mc.field_71439_g;
               var10000.field_70179_y -= mc.field_71439_g.field_70179_y / 555.0D;
               break;
            }

            this.playerPos = new BlockPos(newX, mc.field_71439_g.field_70163_u - (double)i, newZ);
         }
      }

      if (mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70093_af() && !mc.field_71474_y.field_74311_E.field_74513_e && mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, -0.5D, 0.0D).func_72321_a(-this.Nearedge.getValDouble(), 0.0D, -this.Nearedge.getValDouble())).isEmpty() && this.timer.isDelay(100L)) {
         if (this.runsinglegap.getValBoolean()) {
            switch(MathHelper.func_76128_c((double)(mc.field_71439_g.field_70177_z * 8.0F / 360.0F) + 0.5D) & 7) {
            case 0:
               if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177968_d()).func_177230_c() == Blocks.field_150350_a) {
                  this.jumpme();
               }
               break;
            case 1:
               if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177968_d()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177976_e()).func_177230_c() == Blocks.field_150350_a) {
                  this.jumpme();
               }
               break;
            case 2:
               if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177976_e()).func_177230_c() == Blocks.field_150350_a) {
                  this.jumpme();
               }
               break;
            case 3:
               if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177976_e()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177978_c()).func_177230_c() == Blocks.field_150350_a) {
                  this.jumpme();
               }
               break;
            case 4:
               if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177978_c()).func_177230_c() == Blocks.field_150350_a) {
                  this.jumpme();
               }
               break;
            case 5:
               if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177974_f()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177978_c()).func_177230_c() == Blocks.field_150350_a) {
                  this.jumpme();
               }
               break;
            case 6:
               if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177974_f()).func_177230_c() == Blocks.field_150350_a) {
                  this.jumpme();
               }
               break;
            case 7:
               if (mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177968_d()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(this.playerPos.func_177977_b().func_177974_f()).func_177230_c() == Blocks.field_150350_a) {
                  this.jumpme();
               }
            }
         } else {
            this.jumpme();
         }
      }

   }

   private void jumpme() {
      mc.field_71439_g.func_70664_aZ();
      this.timer.setLastMS();
   }
}
