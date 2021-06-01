package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Burrow extends Module {
   Setting mode;
   Setting glitchY;
   Setting tpHeight;
   Setting delay;
   Setting Rotate;
   Setting Instant;
   Setting Center;
   Setting CenterBypass;
   Setting OffGround;
   private final TimerUtils timer;

   public Burrow() {
      super("Burrow", 0, Category.COMBAT, "Burrow into hole");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "JUMP", new String[]{"JUMP", "GLITCH", "TP"}));
      this.glitchY = Main.setmgr.add(new Setting("glitchY", this, 0.5D, 0.1D, 1.5D, false, this.mode, "GLITCH", 1));
      this.tpHeight = Main.setmgr.add(new Setting("tpHeight", this, 0.5D, 0.0D, 10.0D, false, this.mode, "TP", 1));
      this.delay = Main.setmgr.add(new Setting("delay", this, 200.0D, 1.0D, 500.0D, false));
      this.Rotate = Main.setmgr.add(new Setting("Rotate", this, true));
      this.Instant = Main.setmgr.add(new Setting("Instant", this, true));
      this.Center = Main.setmgr.add(new Setting("Center", this, true));
      this.CenterBypass = Main.setmgr.add(new Setting("CenterBypass", this, true, this.Center, 5));
      this.OffGround = Main.setmgr.add(new Setting("OffGround", this, true));
      this.timer = new TimerUtils();
   }

   private int find_obi_in_hotbar() {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock) {
            Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
            if (block instanceof BlockObsidian) {
               return i;
            }
         }
      }

      return -1;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.timer.isDelay((long)this.delay.getValDouble()) && this.find_obi_in_hotbar() != -1) {
         double posy = mc.field_71439_g.field_70163_u;
         int current = mc.field_71439_g.field_71071_by.field_70461_c;
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(this.find_obi_in_hotbar()));
         mc.field_71439_g.field_71071_by.field_70461_c = this.find_obi_in_hotbar();
         BlockPos positionToPlaceAt = (new BlockPos(mc.field_71439_g.func_174791_d())).func_177977_b();
         if (this.place(positionToPlaceAt, mc)) {
            if (this.OffGround.getValBoolean()) {
               mc.field_71439_g.field_70122_E = false;
            }

            String var6 = this.mode.getValString();
            byte var7 = -1;
            switch(var6.hashCode()) {
            case 2684:
               if (var6.equals("TP")) {
                  var7 = 2;
               }
               break;
            case 2288686:
               if (var6.equals("JUMP")) {
                  var7 = 0;
               }
               break;
            case 2105114933:
               if (var6.equals("GLITCH")) {
                  var7 = 1;
               }
            }

            switch(var7) {
            case 0:
               mc.field_71439_g.func_70664_aZ();
               break;
            case 1:
               mc.field_71439_g.field_70181_x = this.glitchY.getValDouble();
               break;
            case 2:
               mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - this.tpHeight.getValDouble(), mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
            }
         }

         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(current));
         mc.field_71439_g.field_71071_by.field_70461_c = current;
         if (this.Instant.getValBoolean()) {
            mc.field_71439_g.field_70163_u = posy;
         }

         this.toggle();
      }

   }

   public void onEnable() {
      if (mc.field_71439_g != null) {
         if (this.find_obi_in_hotbar() != -1) {
            if (this.Center.getValBoolean()) {
               if (this.CenterBypass.getValBoolean()) {
                  double lMotionX = Math.floor(mc.field_71439_g.field_70165_t) + 0.5D - mc.field_71439_g.field_70165_t;
                  double lMotionZ = Math.floor(mc.field_71439_g.field_70161_v) + 0.5D - mc.field_71439_g.field_70161_v;
                  mc.field_71439_g.field_70159_w = lMotionX / 2.0D;
                  mc.field_71439_g.field_70179_y = lMotionZ / 2.0D;
               } else {
                  double[] newPos = new double[]{Math.floor(mc.field_71439_g.field_70165_t) + 0.5D, mc.field_71439_g.field_70163_u, Math.floor(mc.field_71439_g.field_70161_v) + 0.5D};
                  Position middleOfPos = new Position(newPos[0], newPos[1], newPos[2], mc.field_71439_g.field_70122_E);
                  if (!mc.field_71441_e.func_175623_d((new BlockPos(newPos[0], newPos[1], newPos[2])).func_177977_b()) && mc.field_71439_g.field_70165_t != middleOfPos.field_149479_a && mc.field_71439_g.field_70161_v != middleOfPos.field_149478_c) {
                     mc.field_71439_g.field_71174_a.func_147297_a(middleOfPos);
                     mc.field_71439_g.func_70107_b(newPos[0], newPos[1], newPos[2]);
                  }
               }
            }

            mc.field_71439_g.func_70664_aZ();
            this.timer.setLastMS();
         } else {
            ChatUtils.message("You dont have obsidian to use");
            this.toggle();
         }
      }

   }

   private EnumFacing calcSide(BlockPos pos) {
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing side = var2[var4];
         BlockPos sideOffset = pos.func_177972_a(side);
         IBlockState offsetState = mc.field_71441_e.func_180495_p(sideOffset);
         if (offsetState.func_177230_c().func_176209_a(offsetState, false) && !offsetState.func_185904_a().func_76222_j()) {
            return side;
         }
      }

      return null;
   }

   private boolean place(BlockPos pos, Minecraft mc) {
      Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
      EnumFacing direction = this.calcSide(pos);
      if (direction == null) {
         return false;
      } else {
         boolean activated = block.func_180639_a(mc.field_71441_e, pos, mc.field_71441_e.func_180495_p(pos), mc.field_71439_g, EnumHand.MAIN_HAND, direction, 0.0F, 0.0F, 0.0F);
         if (activated) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
         }

         EnumFacing otherSide = direction.func_176734_d();
         BlockPos sideOffset = pos.func_177972_a(direction);
         if (this.Rotate.getValBoolean()) {
            float[] angle = Utils.calcAngle(mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((double)((float)pos.func_177958_n() + 0.5F), (double)((float)pos.func_177956_o() + 0.5F), (double)((float)pos.func_177952_p() + 0.5F)));
            mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(angle[0], angle[1], mc.field_71439_g.field_70122_E));
         }

         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(sideOffset, otherSide, EnumHand.MAIN_HAND, 0.5F, 0.5F, 0.5F));
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketAnimation(EnumHand.MAIN_HAND));
         if (activated) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
         }

         return true;
      }
   }
}
