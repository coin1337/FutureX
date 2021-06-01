package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.BlockUtils;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import Method.Client.utils.visual.RenderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Scaffold extends Module {
   BlockPos blockDown1 = null;
   BlockPos blockDown2 = null;
   BlockPos blockDown3 = null;
   Setting Towermode;
   Setting Placecolor;
   Setting TimerVal;
   Setting radius;
   Setting Sprint;
   Setting Towerspeed;
   Setting TowerDelay;
   Setting TowerFeet;
   Setting SneakPlace;
   Setting DrawMode;
   Setting LineWidth;
   private final TimerUtils timer;

   public Scaffold() {
      super("Scaffold", 0, Category.MOVEMENT, "Scaffolds");
      this.Towermode = Main.setmgr.add(new Setting("Towermode", this, "Tower", new String[]{"Tower", "Onjump", "Timer", "ACC", "NCP", "Spartan", "TP", "Long", "None"}));
      this.Placecolor = Main.setmgr.add(new Setting("Placecolor", this, 0.0D, 1.0D, 1.0D, 0.22D));
      this.TimerVal = Main.setmgr.add(new Setting("TimerVal", this, 1.0D, 0.0D, 3.0D, false, this.Towermode, "Timer", 8));
      this.radius = Main.setmgr.add(new Setting("Radius", this, 0.0D, 0.0D, 5.0D, true));
      this.Sprint = Main.setmgr.add(new Setting("Sprint place", this, false));
      this.Towerspeed = Main.setmgr.add(new Setting("Towerspeed", this, 1.0D, 0.0D, 1.0D, false, this.Towermode, "Tower", 8));
      this.TowerDelay = Main.setmgr.add(new Setting("TowerDelay", this, 100.0D, 0.0D, 1000.0D, true, this.Towermode, "Tower", 9));
      this.TowerFeet = Main.setmgr.add(new Setting("Tower Feet Look", this, true));
      this.SneakPlace = Main.setmgr.add(new Setting("SneakPlace", this, true));
      this.DrawMode = Main.setmgr.add(new Setting("Hole Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.timer = new TimerUtils();
   }

   public void onDisable() {
      mc.field_71428_T.field_194149_e = 50.0F;
      super.onDisable();
   }

   public void onEnable() {
      mc.field_71428_T.field_194149_e = 50.0F;
      super.onEnable();
   }

   public void onPlayerTick(PlayerTickEvent event) {
      int newSlot = this.findSlotWithBlock();
      if (newSlot != -1) {
         this.Custom(newSlot);
      } else {
         ChatUtils.error("No blocks found in hotbar!");
         this.toggle();
      }

      if (this.TowerFeet.getValBoolean() && !this.Towermode.getValString().equalsIgnoreCase("None")) {
         Wrapper.INSTANCE.sendPacket(new Rotation(mc.field_71439_g.field_70177_z, 90.0F, mc.field_71439_g.field_70122_E));
      }

      if (this.Towermode.getValString().equalsIgnoreCase("Tower") && this.timer.isDelay((long)this.TowerDelay.getValDouble()) && mc.field_71474_y.field_74314_A.func_151470_d()) {
         mc.field_71439_g.field_70181_x = -0.2800000011920929D;
         float towerMotion = 0.42F;
         mc.field_71439_g.func_70016_h(0.0D, 0.41999998688697815D * this.Towerspeed.getValDouble(), 0.0D);
         this.timer.setLastMS();
      }

      EntityPlayerSP var10000;
      if (this.Towermode.getValString().equalsIgnoreCase("Onjump")) {
         mc.field_71467_ac = 0;
         if (mc.field_71474_y.field_74314_A.func_151470_d() && mc.field_71439_g.field_70181_x < 0.0D) {
            var10000 = mc.field_71439_g;
            var10000.field_70181_x *= 1.48D;
            if (mc.field_71439_g.field_70122_E) {
               mc.field_71439_g.func_70637_d(false);
               mc.field_71439_g.func_70664_aZ();
            }
         }
      }

      double round;
      if (this.Towermode.getValString().equalsIgnoreCase("NCP")) {
         round = -2.0D;
         if (mc.field_71439_g.field_70122_E && mc.field_71474_y.field_74314_A.field_74513_e) {
            mc.field_71439_g.field_70181_x = 0.41999998688697815D;
         }

         if (mc.field_71439_g.field_70181_x < 0.1D && !(mc.field_71441_e.func_180495_p((new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)).func_177963_a(0.0D, round, 0.0D)).func_177230_c() instanceof BlockAir)) {
            mc.field_71439_g.field_70181_x = -10.0D;
         }
      }

      if (this.Towermode.getValString().equalsIgnoreCase("TP") && mc.field_71474_y.field_74314_A.func_151470_d() && mc.field_71439_g.field_70122_E) {
         var10000 = mc.field_71439_g;
         var10000.field_70181_x -= 0.2300000051036477D;
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.1D, mc.field_71439_g.field_70161_v);
      }

      if (this.Towermode.getValString().equalsIgnoreCase("Spartan")) {
         round = -2.0D;
         if (mc.field_71439_g.field_70122_E && mc.field_71474_y.field_74314_A.field_74513_e) {
            mc.field_71439_g.field_70181_x = 0.41999998688697815D;
         }

         if (mc.field_71439_g.field_70181_x < 0.0D && !(mc.field_71441_e.func_180495_p((new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)).func_177963_a(0.0D, round, 0.0D)).func_177230_c() instanceof BlockAir)) {
            mc.field_71439_g.field_70181_x = -10.0D;
         }
      }

      if (this.Towermode.getValString().equalsIgnoreCase("ACC")) {
         if (mc.field_71439_g.field_70122_E && mc.field_71474_y.field_74314_A.field_74513_e) {
            mc.field_71439_g.field_70181_x = 0.395D;
         }

         var10000 = mc.field_71439_g;
         var10000.field_70181_x -= 0.002300000051036477D;
      }

      if (this.Towermode.getValString().equalsIgnoreCase("Long") && mc.field_71474_y.field_74314_A.func_151470_d()) {
         if (Utils.isMoving(mc.field_71439_g)) {
            if (isOnGround(0.76D) && !isOnGround(0.75D) && mc.field_71439_g.field_70181_x > 0.23D && mc.field_71439_g.field_70181_x < 0.25D) {
               round = (double)Math.round(mc.field_71439_g.field_70163_u);
               mc.field_71439_g.field_70181_x = round - mc.field_71439_g.field_70163_u;
            }

            if (isOnGround(1.0E-4D)) {
               mc.field_71439_g.field_70181_x = 0.42D;
               var10000 = mc.field_71439_g;
               var10000.field_70159_w *= 0.9D;
               var10000 = mc.field_71439_g;
               var10000.field_70179_y *= 0.9D;
            } else if (mc.field_71439_g.field_70163_u >= (double)Math.round(mc.field_71439_g.field_70163_u) - 1.0E-4D && mc.field_71439_g.field_70163_u <= (double)Math.round(mc.field_71439_g.field_70163_u) + 1.0E-4D) {
               mc.field_71439_g.field_70181_x = 0.0D;
            }
         } else {
            mc.field_71439_g.field_70159_w = 0.0D;
            mc.field_71439_g.field_70179_y = 0.0D;
            mc.field_71439_g.field_70747_aH = 0.0F;
            round = mc.field_71439_g.field_70165_t;
            double y = mc.field_71439_g.field_70163_u - 1.0D;
            double z = mc.field_71439_g.field_70161_v;
            BlockPos blockBelow = new BlockPos(round, y, z);
            if (mc.field_71441_e.func_180495_p(blockBelow).func_177230_c() == Blocks.field_150350_a) {
               mc.field_71439_g.field_70181_x = 0.4196D;
               var10000 = mc.field_71439_g;
               var10000.field_70159_w *= 0.75D;
               var10000 = mc.field_71439_g;
               var10000.field_70179_y *= 0.75D;
            }
         }
      }

      if (this.Towermode.getValString().equalsIgnoreCase("Timer")) {
         if (!mc.field_71439_g.field_70122_E) {
            mc.field_71428_T.field_194149_e = (float)(50.0D / this.TimerVal.getValDouble());
         }

         mc.field_71467_ac = 0;
         if (mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.field_70181_x = 0.3932D;
            mc.field_71428_T.field_194149_e = 50.0F;
         }
      }

   }

   public static boolean isOnGround(double height) {
      return !mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, -height, 0.0D)).isEmpty();
   }

   public void XZmodify(double x, double z) {
      mc.field_71439_g.field_70159_w = x;
      mc.field_71439_g.field_70179_y = z;
   }

   private void Custom(int NewSlot) {
      int StartingItem = mc.field_71439_g.field_71071_by.field_70461_c;
      mc.field_71439_g.field_71071_by.field_70461_c = NewSlot;
      if (mc.field_71474_y.field_151444_V.func_151470_d() && this.Sprint.getValBoolean()) {
         float X = MathHelper.func_76126_a((float)Math.toRadians((double)mc.field_71439_g.field_70177_z)) * 0.03F;
         float Z = MathHelper.func_76134_b((float)Math.toRadians((double)mc.field_71439_g.field_70177_z)) * 0.03F;
         if (mc.field_71474_y.field_74351_w.func_151470_d()) {
            this.XZmodify((double)(-X), (double)Z);
         }

         if (mc.field_71474_y.field_74368_y.func_151470_d()) {
            this.XZmodify((double)X, (double)(-Z));
         }

         if (mc.field_71474_y.field_74370_x.func_151470_d()) {
            this.XZmodify((double)X, (double)Z);
         }

         if (mc.field_71474_y.field_74366_z.func_151470_d()) {
            this.XZmodify((double)(-X), (double)(-Z));
         }

         this.blockDown1 = (new BlockPos(mc.field_71439_g)).func_177979_c(2);
         if (mc.field_71441_e.func_180495_p(this.blockDown1).func_185904_a().func_76222_j()) {
            this.Blockplace(EnumHand.MAIN_HAND, this.blockDown1);
         }

         if (Math.abs(mc.field_71439_g.field_70159_w) > 0.03D && mc.field_71441_e.func_180495_p(new BlockPos((double)this.blockDown1.func_177958_n() + mc.field_71439_g.field_70159_w / Math.abs(mc.field_71439_g.field_70159_w), (double)(this.blockDown1.func_177956_o() - 1), (double)this.blockDown1.func_177952_p())).func_185904_a().func_76222_j()) {
            this.Blockplace(EnumHand.MAIN_HAND, new BlockPos((double)this.blockDown1.func_177958_n() + mc.field_71439_g.field_70159_w / Math.abs(mc.field_71439_g.field_70159_w), (double)(this.blockDown1.func_177956_o() - 1), (double)this.blockDown1.func_177952_p()));
         } else if (Math.abs(mc.field_71439_g.field_70179_y) > 0.03D && mc.field_71441_e.func_180495_p(new BlockPos((double)this.blockDown1.func_177958_n(), (double)(this.blockDown1.func_177956_o() - 1), (double)this.blockDown1.func_177952_p() + mc.field_71439_g.field_70179_y / Math.abs(mc.field_71439_g.field_70179_y))).func_185904_a().func_76222_j()) {
            this.Blockplace(EnumHand.MAIN_HAND, new BlockPos((double)this.blockDown1.func_177958_n(), (double)(this.blockDown1.func_177956_o() - 1), (double)this.blockDown1.func_177952_p() + mc.field_71439_g.field_70179_y / Math.abs(mc.field_71439_g.field_70179_y)));
         }

      } else if (this.radius.getValDouble() == 0.0D) {
         this.blockDown2 = (new BlockPos(mc.field_71439_g)).func_177977_b();
         if (mc.field_71441_e.func_180495_p(this.blockDown2).func_185904_a().func_76222_j() && !mc.field_71439_g.func_174813_aQ().func_72326_a((new AxisAlignedBB(this.blockDown2)).func_72321_a(0.05D, 0.05D, 0.05D))) {
            this.Blockplace(EnumHand.MAIN_HAND, this.blockDown2);
         }

         if (Math.abs(mc.field_71439_g.field_70159_w) > 0.033D && mc.field_71441_e.func_180495_p(new BlockPos((double)this.blockDown2.func_177958_n() + mc.field_71439_g.field_70159_w / Math.abs(mc.field_71439_g.field_70159_w), (double)this.blockDown2.func_177956_o(), (double)this.blockDown2.func_177952_p())).func_185904_a().func_76222_j()) {
            this.Blockplace(EnumHand.MAIN_HAND, new BlockPos((double)this.blockDown2.func_177958_n() + mc.field_71439_g.field_70159_w / Math.abs(mc.field_71439_g.field_70159_w), (double)this.blockDown2.func_177956_o(), (double)this.blockDown2.func_177952_p()));
         } else if (Math.abs(mc.field_71439_g.field_70179_y) > 0.033D && mc.field_71441_e.func_180495_p(new BlockPos((double)this.blockDown2.func_177958_n(), (double)this.blockDown2.func_177956_o(), (double)this.blockDown2.func_177952_p() + mc.field_71439_g.field_70179_y / Math.abs(mc.field_71439_g.field_70179_y))).func_185904_a().func_76222_j()) {
            this.Blockplace(EnumHand.MAIN_HAND, new BlockPos((double)this.blockDown2.func_177958_n(), (double)this.blockDown2.func_177956_o(), (double)this.blockDown2.func_177952_p() + mc.field_71439_g.field_70179_y / Math.abs(mc.field_71439_g.field_70179_y)));
         }

      } else {
         ArrayList<BlockPos> WidePlace = new ArrayList();

         for(int i = (int)(-this.radius.getValDouble()); (double)i <= this.radius.getValDouble(); ++i) {
            for(int j = (int)(-this.radius.getValDouble()); (double)j <= this.radius.getValDouble(); ++j) {
               WidePlace.add(new BlockPos(mc.field_71439_g.field_70165_t + (double)i, mc.field_71439_g.field_70163_u - 1.0D, mc.field_71439_g.field_70161_v + (double)j));
            }
         }

         Iterator var7 = WidePlace.iterator();

         while(var7.hasNext()) {
            BlockPos blockPos3 = (BlockPos)var7.next();
            if (mc.field_71441_e.func_180495_p(blockPos3).func_185904_a().func_76222_j()) {
               this.blockDown3 = blockPos3;
               this.Blockplace(EnumHand.MAIN_HAND, blockPos3);
            }
         }

         mc.field_71439_g.field_71071_by.field_70461_c = StartingItem;
      }
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      try {
         if (this.blockDown1 != null) {
            RenderUtils.RenderBlock(this.DrawMode.getValString(), RenderUtils.Standardbb(this.blockDown1), this.Placecolor.getcolor(), this.LineWidth.getValDouble());
         }

         if (this.blockDown2 != null && this.radius.getValDouble() == 0.0D) {
            RenderUtils.RenderBlock(this.DrawMode.getValString(), RenderUtils.Standardbb(this.blockDown2), this.Placecolor.getcolor(), this.LineWidth.getValDouble());
         }

         if (this.blockDown3 != null) {
            RenderUtils.RenderBlock(this.DrawMode.getValString(), RenderUtils.Standardbb(this.blockDown3), this.Placecolor.getcolor(), this.LineWidth.getValDouble());
         }
      } catch (Exception var3) {
      }

   }

   public int findSlotWithBlock() {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack.func_77973_b() instanceof ItemBlock) {
            Block block = Block.func_149634_a(stack.func_77973_b()).func_176223_P().func_177230_c();
            if (block.func_149730_j(BlockUtils.getBlock((new BlockPos(mc.field_71439_g)).func_177977_b()).func_176223_P()) && block != Blocks.field_150354_m && block != Blocks.field_150351_n) {
               return i;
            }
         }
      }

      return -1;
   }

   public void Blockplace(EnumHand enumHand, BlockPos blockPos) {
      Vec3d vec3d = new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
      EnumFacing[] var4 = EnumFacing.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EnumFacing enumFacing = var4[var6];
         BlockPos offset = blockPos.func_177972_a(enumFacing);
         EnumFacing opposite = enumFacing.func_176734_d();
         if (Utils.canBeClicked(offset)) {
            Vec3d Vec3d = (new Vec3d(offset)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
            if (vec3d.func_72436_e(Vec3d) <= 18.0625D) {
               float[] array = Utils.getNeededRotations(Vec3d, 0.0F, 0.0F);
               mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(array[0], array[1], mc.field_71439_g.field_70122_E));
               if (this.SneakPlace.getValBoolean()) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
               }

               mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, offset, opposite, Vec3d, enumHand);
               mc.field_71439_g.func_184609_a(enumHand);
               if (this.SneakPlace.getValBoolean()) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
               }

               return;
            }
         }
      }

   }
}
