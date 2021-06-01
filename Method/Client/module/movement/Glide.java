package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.BlockUtils;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.system.Wrapper;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Glide extends Module {
   Setting Mode;
   Setting Damage;
   Setting fallSpeed;
   Setting moveSpeed;
   Setting minHeight;
   Setting ypos;
   Setting ymotion;
   Setting MotionY;
   TimerUtils timer;

   public Glide() {
      super("Glide", 0, Category.MOVEMENT, "Glide");
      this.Mode = Main.setmgr.add(new Setting("Mode", this, "Falling", new String[]{"Falling", "Constant", "Flat", "ACC", "NCP", "Matrix", "Simple", "Randomadd"}));
      this.Damage = Main.setmgr.add(new Setting("Damage", this, false));
      this.fallSpeed = Main.setmgr.add(new Setting("fallSpeed", this, 0.25D, 0.005D, 0.25D, false, this.Mode, "Falling", 2));
      this.moveSpeed = Main.setmgr.add(new Setting("moveSpeed", this, 1.0D, 0.5D, 5.0D, false));
      this.minHeight = Main.setmgr.add(new Setting("minHeight", this, 0.0D, 0.0D, 2.0D, false, this.Mode, "Falling", 2));
      this.ypos = Main.setmgr.add(new Setting("ypos", this, 1.0D, 1.0D, 5.0D, false, this.Mode, "Randomadd", 2));
      this.ymotion = Main.setmgr.add(new Setting("ymotion", this, 1.0D, 1.0D, 5.0D, false, this.Mode, "Randomadd", 2));
      this.MotionY = Main.setmgr.add(new Setting("MotionY", this, 12.0D, 0.0D, 100.0D, false, this.Mode, "Constant", 2));
      this.timer = new TimerUtils();
   }

   public void onEnable() {
      if (this.Damage.getValBoolean()) {
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 6.0D, mc.field_71439_g.field_70161_v, true));
         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_70159_w *= 0.2D;
         var10000 = mc.field_71439_g;
         var10000.field_70179_y *= 0.2D;
         mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
      }

      if (this.Mode.getValString().equalsIgnoreCase("Flat")) {
         mc.field_71439_g.field_70181_x = 0.19D;
      }

      super.onEnable();
   }

   public void onClientTick(ClientTickEvent event) {
      EntityPlayerSP player = mc.field_71439_g;
      EntityPlayerSP var10000;
      if (this.Mode.getValString().equalsIgnoreCase("Randomadd") && mc.field_71439_g.field_70181_x < -0.01D && !mc.field_71439_g.field_70122_E) {
         double firstrandom = Math.random() / this.ymotion.getValDouble() / 10.0D;
         double secondrandom = Math.random() / this.ypos.getValDouble() / 10.0D;
         if (this.ymotion.getValDouble() > 0.0D) {
            var10000 = mc.field_71439_g;
            var10000.field_70181_x += firstrandom;
         }

         if (this.ypos.getValDouble() > 0.0D) {
            var10000 = mc.field_71439_g;
            var10000.field_70163_u += secondrandom;
         }
      }

      if (this.Mode.getValString().equalsIgnoreCase("Constant")) {
         mc.field_71439_g.field_70181_x = -this.MotionY.getValDouble() / 60.0D;
      }

      if (this.Mode.getValString().equalsIgnoreCase("Simple")) {
         if (mc.field_71474_y.field_74314_A.field_74513_e) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.009999999776482582D, mc.field_71439_g.field_70161_v);
         }

         mc.field_71439_g.field_70181_x = -0.10000000149011612D;
         if (mc.field_71474_y.field_74311_E.field_74513_e) {
            mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.5D, mc.field_71439_g.field_70161_v);
         }
      }

      if (this.Mode.getValString().equalsIgnoreCase("Matrix") && mc.field_71439_g.field_70143_R > 3.0F) {
         if (mc.field_71439_g.field_70173_aa % 3 == 0) {
            mc.field_71439_g.field_70181_x = -0.1D;
         }

         if (mc.field_71439_g.field_70173_aa % 4 == 0) {
            mc.field_71439_g.field_70181_x = -0.2D;
         }
      }

      if (this.Mode.getValString().equalsIgnoreCase("NCP")) {
         mc.field_71439_g.field_70122_E = true;
         mc.field_71439_g.field_71075_bZ.field_75100_b = true;
         tpRel(mc.field_71439_g.field_70159_w, mc.field_71439_g.field_70181_x = -0.0222D, mc.field_71439_g.field_70179_y);
         tpPacket(mc.field_71439_g.field_70159_w, mc.field_71439_g.field_70181_x - 9.0D, mc.field_71439_g.field_70179_y);
      }

      if (this.Mode.getValString().equalsIgnoreCase("Flat")) {
         if (!player.field_71075_bZ.field_75100_b && player.field_70143_R > 0.0F && !player.func_70093_af()) {
            player.field_70181_x = 0.0D;
         }

         if (Wrapper.INSTANCE.mcSettings().field_74311_E.func_151470_d()) {
            player.field_70181_x = -0.11D;
         }

         if (Wrapper.INSTANCE.mcSettings().field_74314_A.func_151470_d()) {
            ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_FALL_FLYING));
            player.field_70122_E = false;
         }

         if (this.timer.delay(50.0F)) {
            player.field_70122_E = false;
            this.timer.setLastMS();
         }
      }

      if (this.Mode.getValString().equalsIgnoreCase("ACC")) {
         if (mc.field_71439_g.field_70181_x < 0.0D && mc.field_71439_g.field_70160_al || mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.field_70181_x = -0.125D;
            var10000 = mc.field_71439_g;
            var10000.field_70747_aH *= 1.01227F;
            mc.field_71439_g.field_70145_X = true;
            mc.field_71439_g.field_70143_R = 0.0F;
            mc.field_71439_g.field_70122_E = true;
            var10000 = mc.field_71439_g;
            var10000.field_70702_br = (float)((double)var10000.field_70702_br + 0.800000011920929D * this.moveSpeed.getValDouble());
            var10000 = mc.field_71439_g;
            var10000.field_70747_aH += 0.2F;
            mc.field_71439_g.field_70133_I = true;
         }
      } else if (this.Mode.getValString().equalsIgnoreCase("Falling")) {
         World world = mc.field_71441_e;
         if (!player.field_70160_al || player.func_70090_H() || player.func_180799_ab() || player.func_70617_f_() || player.field_70181_x >= 0.0D) {
            return;
         }

         if (this.minHeight.getValDouble() > 0.0D) {
            AxisAlignedBB box = player.func_174813_aQ();
            box = box.func_111270_a(box.func_72317_d(0.0D, -this.minHeight.getValDouble(), 0.0D));
            if (world.func_184143_b(box)) {
               return;
            }

            BlockPos min = new BlockPos(new Vec3d(box.field_72340_a, box.field_72338_b, box.field_72339_c));
            BlockPos max = new BlockPos(new Vec3d(box.field_72336_d, box.field_72337_e, box.field_72334_f));
            Stream<BlockPos> stream = StreamSupport.stream(BlockPos.func_177980_a(min, max).spliterator(), true);
            if (stream.map(BlockUtils::getBlock).anyMatch((b) -> {
               return b instanceof BlockLiquid;
            })) {
               return;
            }
         }

         player.field_70181_x = Math.max(player.field_70181_x, -this.fallSpeed.getValDouble());
         player.field_70747_aH = (float)((double)player.field_70747_aH * this.moveSpeed.getValDouble());
      }

      super.onClientTick(event);
   }

   public static void tpRel(double x, double y, double z) {
      EntityPlayerSP player = mc.field_71439_g;
      player.func_70107_b(player.field_70165_t + x, player.field_70163_u + y, player.field_70161_v + z);
   }

   public static void tpPacket(double x, double y, double z) {
      EntityPlayerSP player = mc.field_71439_g;
      Wrapper.INSTANCE.sendPacket(new Position(player.field_70165_t + x, player.field_70163_u + y, player.field_70161_v + z, false));
      Wrapper.INSTANCE.sendPacket(new Position(player.field_70165_t, player.field_70163_u, player.field_70161_v, false));
      Wrapper.INSTANCE.sendPacket(new Position(player.field_70165_t, player.field_70163_u, player.field_70161_v, true));
   }
}
