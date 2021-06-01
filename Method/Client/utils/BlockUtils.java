package Method.Client.utils;

import Method.Client.utils.system.Wrapper;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public final class BlockUtils {
   private static final Minecraft mc;
   public static LinkedList<BlockPos> blocks;

   public static Material getMaterial(BlockPos pos) {
      return getState(pos).func_185904_a();
   }

   public static IBlockState getState(BlockPos pos) {
      return mc.field_71441_e.func_180495_p(pos);
   }

   public static Block getBlock(BlockPos pos) {
      return getState(pos).func_177230_c();
   }

   public static boolean canBeClicked(BlockPos pos) {
      return getBlock(pos).func_176209_a(getState(pos), false);
   }

   public static float getHardness(BlockPos pos) {
      return getState(pos).func_185903_a(Wrapper.INSTANCE.player(), Wrapper.INSTANCE.world(), pos);
   }

   public static boolean breakBlockSimple(BlockPos pos) {
      EnumFacing side = null;
      EnumFacing[] sides = EnumFacing.values();
      Vec3d eyesPos = Utils.getEyesPos();
      Vec3d relCenter = getState(pos).func_185900_c(Wrapper.INSTANCE.world(), pos).func_189972_c();
      Vec3d center = (new Vec3d(pos)).func_178787_e(relCenter);
      Vec3d[] hitVecs = new Vec3d[sides.length];

      int i;
      for(i = 0; i < sides.length; ++i) {
         Vec3i dirVec = sides[i].func_176730_m();
         Vec3d relHitVec = new Vec3d(relCenter.field_72450_a * (double)dirVec.func_177958_n(), relCenter.field_72448_b * (double)dirVec.func_177956_o(), relCenter.field_72449_c * (double)dirVec.func_177952_p());
         hitVecs[i] = center.func_178787_e(relHitVec);
      }

      for(i = 0; i < sides.length; ++i) {
         if (Wrapper.INSTANCE.world().func_147447_a(eyesPos, hitVecs[i], false, true, false) == null) {
            side = sides[i];
            break;
         }
      }

      if (side == null) {
         double distanceSqToCenter = eyesPos.func_72436_e(center);

         for(int i = 0; i < sides.length; ++i) {
            if (!(eyesPos.func_72436_e(hitVecs[i]) >= distanceSqToCenter)) {
               side = sides[i];
               break;
            }
         }
      }

      if (side == null) {
         side = sides[0];
      }

      Utils.faceVectorPacket(hitVecs[side.ordinal()]);
      if (!Wrapper.INSTANCE.controller().func_180512_c(pos, side)) {
         return false;
      } else {
         Wrapper.INSTANCE.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
         return true;
      }
   }

   public static void breakBlocksPacketSpam(Iterable<BlockPos> blocks) {
      Vec3d eyesPos = Utils.getEyesPos();
      NetHandlerPlayClient connection = Wrapper.INSTANCE.player().field_71174_a;
      Iterator var3 = blocks.iterator();

      while(true) {
         while(var3.hasNext()) {
            BlockPos pos = (BlockPos)var3.next();
            Vec3d posVec = (new Vec3d(pos)).func_72441_c(0.5D, 0.5D, 0.5D);
            double distanceSqPosVec = eyesPos.func_72436_e(posVec);
            EnumFacing[] var8 = EnumFacing.values();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               EnumFacing side = var8[var10];
               Vec3d hitVec = posVec.func_178787_e((new Vec3d(side.func_176730_m())).func_186678_a(0.5D));
               if (!(eyesPos.func_72436_e(hitVec) >= distanceSqPosVec)) {
                  connection.func_147297_a(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, side));
                  connection.func_147297_a(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, side));
                  break;
               }
            }
         }

         return;
      }
   }

   static {
      mc = Wrapper.INSTANCE.mc();
      blocks = new LinkedList();
   }
}
