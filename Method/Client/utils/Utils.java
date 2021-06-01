package Method.Client.utils;

import Method.Client.managers.Setting;
import Method.Client.utils.system.Wrapper;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Utils {
   public static Field pressed;
   private static Field modifiersField;
   public static float[] rotationsToBlock = null;
   private static final Random RANDOM = new Random();
   public static List<Block> emptyBlocks;
   public static List<Block> rightclickableBlocks;

   public static double[] directionSpeed(double speed) {
      float forward = Wrapper.INSTANCE.mc().field_71439_g.field_71158_b.field_192832_b;
      float side = Wrapper.INSTANCE.mc().field_71439_g.field_71158_b.field_78902_a;
      float yaw = Wrapper.INSTANCE.mc().field_71439_g.field_70126_B + (Wrapper.INSTANCE.mc().field_71439_g.field_70177_z - Wrapper.INSTANCE.mc().field_71439_g.field_70126_B) * Wrapper.INSTANCE.mc().func_184121_ak();
      if (forward != 0.0F) {
         if (side > 0.0F) {
            yaw += (float)(forward > 0.0F ? -45 : 45);
         } else if (side < 0.0F) {
            yaw += (float)(forward > 0.0F ? 45 : -45);
         }

         side = 0.0F;
         if (forward > 0.0F) {
            forward = 1.0F;
         } else if (forward < 0.0F) {
            forward = -1.0F;
         }
      }

      double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double posX = (double)forward * speed * cos + (double)side * speed * sin;
      double posZ = (double)forward * speed * sin - (double)side * speed * cos;
      return new double[]{posX, posZ};
   }

   public static float[] getNeededRotations(Vec3d vec, float yaw, float pitch) {
      Vec3d eyesPos = getEyesPos();
      double diffX = vec.field_72450_a - eyesPos.field_72450_a;
      double diffY = vec.field_72448_b - eyesPos.field_72448_b;
      double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float rotationYaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float rotationPitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{updateRotation(Wrapper.INSTANCE.player().field_70177_z, rotationYaw, yaw / 4.0F), updateRotation(Wrapper.INSTANCE.player().field_70125_A, rotationPitch, pitch / 4.0F)};
   }

   public static float[] calcAngle(Vec3d from, Vec3d to) {
      double difX = to.field_72450_a - from.field_72450_a;
      double difY = (to.field_72448_b - from.field_72448_b) * -1.0D;
      double difZ = to.field_72449_c - from.field_72449_c;
      double dist = (double)MathHelper.func_76133_a(difX * difX + difZ * difZ);
      return new float[]{(float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(difY, dist)))};
   }

   public static float updateRotation(float PlayerRotation, float Modified, float MaxValueAccepted) {
      float degrees = MathHelper.func_76142_g(Modified - PlayerRotation);
      if (MaxValueAccepted != 0.0F) {
         if (degrees > MaxValueAccepted) {
            degrees = MaxValueAccepted;
         }

         if (degrees < -MaxValueAccepted) {
            degrees = -MaxValueAccepted;
         }
      }

      return PlayerRotation + degrees;
   }

   public static boolean isBlockEmpty(BlockPos pos) {
      try {
         if (emptyBlocks.contains(Wrapper.mc.field_71441_e.func_180495_p(pos).func_177230_c())) {
            AxisAlignedBB box = new AxisAlignedBB(pos);
            Iterator var2 = Wrapper.mc.field_71441_e.field_72996_f.iterator();

            Entity entity;
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               entity = (Entity)var2.next();
            } while(!(entity instanceof EntityLivingBase) && !box.func_72326_a(entity.func_174813_aQ()));

            return true;
         }
      } catch (Exception var4) {
      }

      return false;
   }

   public static boolean trytoplace(BlockPos target_pos) {
      boolean should_try_place = true;
      if (!Wrapper.mc.field_71441_e.func_180495_p(target_pos).func_185904_a().func_76222_j()) {
         should_try_place = false;
      }

      Iterator var2 = Wrapper.mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(target_pos)).iterator();

      while(var2.hasNext()) {
         Entity entity = (Entity)var2.next();
         if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
            should_try_place = false;
            break;
         }
      }

      return should_try_place;
   }

   public static Vec3d interpolateEntity(Entity entity, float time) {
      return new Vec3d(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)time, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)time, entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)time);
   }

   public static boolean placeBlock(BlockPos pos, boolean rotate, Setting s) {
      if (isBlockEmpty(pos)) {
         EnumFacing[] facings = EnumFacing.values();
         EnumFacing[] var4 = facings;
         int var5 = facings.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumFacing f = var4[var6];
            Block neighborBlock = Wrapper.mc.field_71441_e.func_180495_p(pos.func_177972_a(f)).func_177230_c();
            Vec3d vec = new Vec3d((double)pos.func_177958_n() + 0.5D + (double)f.func_82601_c() * 0.5D, (double)pos.func_177956_o() + 0.5D + (double)f.func_96559_d() * 0.5D, (double)pos.func_177952_p() + 0.5D + (double)f.func_82599_e() * 0.5D);
            if (!emptyBlocks.contains(neighborBlock) && Wrapper.mc.field_71439_g.func_174824_e(Wrapper.mc.func_184121_ak()).func_72438_d(vec) <= 4.25D) {
               float[] rot = new float[]{Wrapper.mc.field_71439_g.field_70177_z, Wrapper.mc.field_71439_g.field_70125_A};
               if (rotate) {
                  float[] array = getNeededRotations(vec, 0.0F, 0.0F);
                  Wrapper.mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(array[0], array[1], Wrapper.mc.field_71439_g.field_70122_E));
               }

               if (rightclickableBlocks.contains(neighborBlock)) {
                  Wrapper.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(Wrapper.mc.field_71439_g, Action.START_SNEAKING));
               }

               Wrapper.mc.field_71442_b.func_187099_a(Wrapper.mc.field_71439_g, Wrapper.mc.field_71441_e, pos.func_177972_a(f), f.func_176734_d(), new Vec3d(pos), EnumHand.MAIN_HAND);
               if (rightclickableBlocks.contains(neighborBlock)) {
                  Wrapper.mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(Wrapper.mc.field_71439_g, Action.STOP_SNEAKING));
               }

               if (rotate) {
                  Wrapper.mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(rot[0], rot[1], Wrapper.mc.field_71439_g.field_70122_E));
               }

               if (s.getValString().equalsIgnoreCase("Mainhand") || s.getValString().equalsIgnoreCase("Both")) {
                  Wrapper.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
               }

               if (s.getValString().equalsIgnoreCase("Offhand") || s.getValString().equalsIgnoreCase("Both")) {
                  Wrapper.mc.field_71439_g.func_184609_a(EnumHand.OFF_HAND);
               }

               return true;
            }
         }
      }

      return false;
   }

   public static Utils.ValidResult valid(BlockPos pos) {
      if (!Wrapper.mc.field_71441_e.func_72855_b(new AxisAlignedBB(pos))) {
         return Utils.ValidResult.NoEntityCollision;
      } else if (!checkForNeighbours(pos)) {
         return Utils.ValidResult.NoNeighbors;
      } else {
         IBlockState blockState = Wrapper.mc.field_71441_e.func_180495_p(pos);
         if (blockState.func_177230_c() != Blocks.field_150350_a) {
            return Utils.ValidResult.AlreadyBlockThere;
         } else {
            BlockPos[] l_Blocks = new BlockPos[]{pos.func_177978_c(), pos.func_177968_d(), pos.func_177974_f(), pos.func_177976_e(), pos.func_177984_a(), pos.func_177977_b()};
            BlockPos[] var3 = l_Blocks;
            int var4 = l_Blocks.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               BlockPos l_Pos = var3[var5];
               IBlockState l_State2 = Wrapper.mc.field_71441_e.func_180495_p(l_Pos);
               if (l_State2.func_177230_c() != Blocks.field_150350_a) {
                  EnumFacing[] var8 = EnumFacing.values();
                  int var9 = var8.length;

                  for(int var10 = 0; var10 < var9; ++var10) {
                     EnumFacing side = var8[var10];
                     BlockPos neighbor = pos.func_177972_a(side);
                     if (Wrapper.mc.field_71441_e.func_180495_p(neighbor).func_177230_c().func_176209_a(Wrapper.mc.field_71441_e.func_180495_p(neighbor), false)) {
                        return Utils.ValidResult.Ok;
                     }
                  }
               }
            }

            return Utils.ValidResult.NoNeighbors;
         }
      }
   }

   public static boolean checkForNeighbours(BlockPos blockPos) {
      if (!hasNeighbour(blockPos)) {
         EnumFacing[] var1 = EnumFacing.values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EnumFacing side = var1[var3];
            BlockPos neighbour = blockPos.func_177972_a(side);
            if (hasNeighbour(neighbour)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private static boolean hasNeighbour(BlockPos blockPos) {
      EnumFacing[] var1 = EnumFacing.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFacing side = var1[var3];
         BlockPos neighbour = blockPos.func_177972_a(side);
         if (!Wrapper.mc.field_71441_e.func_180495_p(neighbour).func_185904_a().func_76222_j()) {
            return true;
         }
      }

      return false;
   }

   public static int random(int min, int max) {
      return RANDOM.nextInt(max - min) + min;
   }

   public static void faceVectorPacket(Vec3d vec) {
      float[] rotations = getNeededRotations(vec, 0.0F, 0.0F);
      EntityPlayerSP pl = Minecraft.func_71410_x().field_71439_g;
      float preYaw = pl.field_70177_z;
      float prePitch = pl.field_70125_A;
      pl.field_70177_z = rotations[0];
      pl.field_70125_A = rotations[1];
      pl.func_175161_p();
      pl.field_70177_z = preYaw;
      pl.field_70125_A = prePitch;
   }

   public static boolean isMoving(Entity e) {
      return e.field_70159_w != 0.0D && e.field_70179_y != 0.0D && e.field_70181_x != 0.0D;
   }

   public static boolean isMovinginput() {
      return Wrapper.INSTANCE.mc().field_71439_g.field_71158_b.field_192832_b != 0.0F || Wrapper.INSTANCE.mc().field_71439_g.field_71158_b.field_78902_a != 0.0F;
   }

   public static boolean canBeClicked(BlockPos pos) {
      return Wrapper.INSTANCE.world().func_180495_p(pos).func_177230_c().func_176209_a(Wrapper.INSTANCE.world().func_180495_p(pos), false);
   }

   public static boolean isLiving(Entity e) {
      return e instanceof EntityLivingBase;
   }

   public static boolean isPassive(Entity e) {
      if (e instanceof EntityWolf && ((EntityWolf)e).func_70919_bu()) {
         return false;
      } else if (!(e instanceof EntityAgeable) && !(e instanceof EntityAmbientCreature) && !(e instanceof EntitySquid)) {
         return e instanceof EntityIronGolem && ((EntityIronGolem)e).func_70643_av() == null;
      } else {
         return true;
      }
   }

   public static Vec3d getEyesPos() {
      return new Vec3d(Wrapper.INSTANCE.player().field_70165_t, Wrapper.INSTANCE.player().field_70163_u + (double)Wrapper.INSTANCE.player().func_70047_e(), Wrapper.INSTANCE.player().field_70161_v);
   }

   public static void faceVectorPacketInstant(Vec3d vec) {
      rotationsToBlock = getNeededRotations(vec, 0.0F, 0.0F);
   }

   public static void teleportToPosition(double[] startPosition, double[] endPosition, double setOffset, double slack, boolean extendOffset, boolean onGround) {
      boolean wasSneaking = false;
      if (Wrapper.INSTANCE.player().func_70093_af()) {
         wasSneaking = true;
      }

      double startX = startPosition[0];
      double startY = startPosition[1];
      double startZ = startPosition[2];
      double endX = endPosition[0];
      double endY = endPosition[1];
      double endZ = endPosition[2];
      double distance = Math.abs(startX - startY) + Math.abs(startY - endY) + Math.abs(startZ - endZ);

      for(int count = 0; distance > slack; ++count) {
         distance = Math.abs(startX - endX) + Math.abs(startY - endY) + Math.abs(startZ - endZ);
         if (count > 120) {
            break;
         }

         double offset = extendOffset && (count & 1) == 0 ? setOffset + 0.15D : setOffset;
         double diffX = startX - endX;
         double diffY = startY - endY;
         double diffZ = startZ - endZ;
         double min = Math.min(Math.abs(diffX), offset);
         if (diffX < 0.0D) {
            startX += min;
         }

         if (diffX > 0.0D) {
            startX -= min;
         }

         double min2 = Math.min(Math.abs(diffY), offset);
         if (diffY < 0.0D) {
            startY += min2;
         }

         if (diffY > 0.0D) {
            startY -= min2;
         }

         double min1 = Math.min(Math.abs(diffZ), offset);
         if (diffZ < 0.0D) {
            startZ += min1;
         }

         if (diffZ > 0.0D) {
            startZ -= min1;
         }

         if (wasSneaking) {
            Wrapper.INSTANCE.sendPacket(new CPacketEntityAction(Wrapper.INSTANCE.player(), Action.STOP_SNEAKING));
         }

         ((NetHandlerPlayClient)Objects.requireNonNull(Wrapper.INSTANCE.mc().func_147114_u())).func_147298_b().func_179290_a(new Position(startX, startY, startZ, onGround));
      }

      if (wasSneaking) {
         Wrapper.INSTANCE.sendPacket(new CPacketEntityAction(Wrapper.INSTANCE.player(), Action.START_SNEAKING));
      }

   }

   public static boolean isBlockMaterial(BlockPos blockPos, Block block) {
      return Wrapper.INSTANCE.world().func_180495_p(blockPos).func_177230_c() == Blocks.field_150350_a;
   }

   public static String getPlayerName(EntityPlayer player) {
      player.func_146103_bH();
      return player.func_146103_bH().getName();
   }

   public static String getEntityNameColor(EntityLivingBase entity) {
      String name = entity.func_145748_c_().func_150254_d();
      if (name.contains("§")) {
         if (name.contains("§1")) {
            return "§1";
         }

         if (name.contains("§2")) {
            return "§2";
         }

         if (name.contains("§3")) {
            return "§3";
         }

         if (name.contains("§4")) {
            return "§4";
         }

         if (name.contains("§5")) {
            return "§5";
         }

         if (name.contains("§6")) {
            return "§6";
         }

         if (name.contains("§7")) {
            return "§7";
         }

         if (name.contains("§8")) {
            return "§8";
         }

         if (name.contains("§9")) {
            return "§9";
         }

         if (name.contains("§0")) {
            return "§0";
         }

         if (name.contains("§e")) {
            return "§e";
         }

         if (name.contains("§d")) {
            return "§d";
         }

         if (name.contains("§a")) {
            return "§a";
         }

         if (name.contains("§b")) {
            return "§b";
         }

         if (name.contains("§c")) {
            return "§c";
         }

         if (name.contains("§f")) {
            return "§f";
         }
      }

      return "null";
   }

   public static boolean checkScreen() {
      return !(Wrapper.INSTANCE.mc().field_71462_r instanceof GuiContainer) && !(Wrapper.INSTANCE.mc().field_71462_r instanceof GuiChat) && Wrapper.INSTANCE.mc().field_71462_r == null;
   }

   public static float getPitch(Entity entity) {
      double y = entity.field_70163_u - Wrapper.INSTANCE.player().field_70163_u;
      y /= (double)Wrapper.INSTANCE.player().func_70032_d(entity);
      double pitch = Math.asin(y) * 57.29577951308232D;
      pitch = -pitch;
      return (float)pitch;
   }

   public static float getYaw(Entity entity) {
      double x = entity.field_70165_t - Wrapper.INSTANCE.player().field_70165_t;
      double z = entity.field_70161_v - Wrapper.INSTANCE.player().field_70161_v;
      double yaw = Math.atan2(x, z) * 57.29577951308232D;
      yaw = -yaw;
      return (float)yaw;
   }

   public static float getDirection() {
      float var1 = Wrapper.INSTANCE.player().field_70177_z;
      if (Wrapper.INSTANCE.player().field_191988_bg < 0.0F) {
         var1 += 180.0F;
      }

      float forward = 1.0F;
      if (Wrapper.INSTANCE.player().field_191988_bg < 0.0F) {
         forward = -0.5F;
      } else if (Wrapper.INSTANCE.player().field_191988_bg > 0.0F) {
         forward = 0.5F;
      }

      if (Wrapper.INSTANCE.player().field_70702_br > 0.0F) {
         var1 -= 90.0F * forward;
      }

      if (Wrapper.INSTANCE.player().field_70702_br < 0.0F) {
         var1 += 90.0F * forward;
      }

      var1 *= 0.017453292F;
      return var1;
   }

   public static int getDistanceFromMouse(EntityLivingBase entity) {
      float[] neededRotations = getNeededRotations(entity.func_174791_d(), 0.0F, 0.0F);
      float neededYaw = Wrapper.INSTANCE.player().field_70177_z - neededRotations[0];
      float neededPitch = Wrapper.INSTANCE.player().field_70125_A - neededRotations[1];
      float distanceFromMouse = MathHelper.func_76129_c(neededYaw * neededYaw + neededPitch * neededPitch * 2.0F);
      return (int)distanceFromMouse;
   }

   static {
      emptyBlocks = Arrays.asList(Blocks.field_150350_a, Blocks.field_150356_k, Blocks.field_150353_l, Blocks.field_150358_i, Blocks.field_150355_j, Blocks.field_150395_bd, Blocks.field_150431_aC, Blocks.field_150329_H, Blocks.field_150480_ab);
      rightclickableBlocks = Arrays.asList(Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB, Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA, Blocks.field_150467_bQ, Blocks.field_150471_bO, Blocks.field_150430_aB, Blocks.field_150441_bU, Blocks.field_150413_aR, Blocks.field_150416_aS, Blocks.field_150455_bV, Blocks.field_180390_bo, Blocks.field_180391_bp, Blocks.field_180392_bq, Blocks.field_180386_br, Blocks.field_180385_bs, Blocks.field_180387_bt, Blocks.field_150382_bo, Blocks.field_150367_z, Blocks.field_150409_cd, Blocks.field_150442_at, Blocks.field_150323_B, Blocks.field_150421_aI, Blocks.field_150461_bJ, Blocks.field_150324_C, Blocks.field_150460_al, Blocks.field_180413_ao, Blocks.field_180414_ap, Blocks.field_180412_aq, Blocks.field_180411_ar, Blocks.field_180410_as, Blocks.field_180409_at, Blocks.field_150414_aQ, Blocks.field_150381_bn, Blocks.field_150380_bt, Blocks.field_150438_bZ, Blocks.field_185776_dc, Blocks.field_150483_bI, Blocks.field_185777_dd, Blocks.field_150462_ai);

      try {
         modifiersField = Field.class.getDeclaredField("modifiers");
         modifiersField.setAccessible(true);
      } catch (Exception var1) {
      }

   }

   public static enum ValidResult {
      NoEntityCollision,
      AlreadyBlockThere,
      NoNeighbors,
      Ok;
   }
}
