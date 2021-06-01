package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Mouse;

public class Teleport extends Module {
   Setting mode;
   Setting math;
   Setting Path;
   Setting Land;
   Setting TpMode;
   Setting LineWidth;
   public boolean passPacket;
   private BlockPos teleportPosition;
   private boolean canDraw;
   private int delay;
   float reach;

   public Teleport() {
      super("Teleport", 0, Category.MOVEMENT, "Teleport around");
      this.mode = Main.setmgr.add(new Setting("Tp Mode", this, "Reach", new String[]{"Reach", "Flight"}));
      this.math = Main.setmgr.add(new Setting("Speed", this, false));
      this.Path = Main.setmgr.add(new Setting("Path", this, 0.0D, 1.0D, 1.0D, 0.22D));
      this.Land = Main.setmgr.add(new Setting("Land", this, 0.22D, 1.0D, 1.0D, 0.22D));
      this.TpMode = Main.setmgr.add(new Setting("Tp Draw", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.passPacket = false;
      this.teleportPosition = null;
      this.reach = 0.0F;
   }

   public void onEnable() {
      if (this.mode.getValString().equalsIgnoreCase("Reach")) {
         this.reach = (float)mc.field_71439_g.func_110148_a(EntityPlayer.REACH_DISTANCE).func_111126_e();
      }

      super.onEnable();
   }

   public void onDisable() {
      if (this.mode.getValString().equalsIgnoreCase("Flight")) {
         mc.field_71439_g.field_70145_X = false;
         this.passPacket = false;
         this.teleportPosition = null;
      } else {
         this.canDraw = false;
         mc.field_71439_g.func_110148_a(EntityPlayer.REACH_DISTANCE).func_111128_a(500.0D);
         super.onDisable();
      }
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      return side == Connection.Side.OUT && this.mode.getValString().equalsIgnoreCase("Flight") && packet instanceof CPacketPlayer ? this.passPacket : true;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Flight")) {
         RayTraceResult object = Wrapper.INSTANCE.mc().field_71476_x;
         if (object != null) {
            EntityPlayerSP player = mc.field_71439_g;
            GameSettings settings = Wrapper.INSTANCE.mcSettings();
            if (!this.passPacket) {
               if (settings.field_74312_F.func_151470_d() && object.field_72313_a == Type.BLOCK) {
                  if (Utils.isBlockMaterial(object.func_178782_a(), Blocks.field_150350_a)) {
                     return;
                  }

                  this.teleportPosition = object.func_178782_a();
                  this.passPacket = true;
               }

            } else {
               player.field_70145_X = false;
               if (settings.field_74311_E.func_151470_d() && player.field_70122_E) {
                  this.Mathteleport();
               }

            }
         }
      } else {
         if ((!Mouse.isButtonDown(0) && Wrapper.INSTANCE.mc().field_71415_G || !Wrapper.INSTANCE.mc().field_71415_G) && mc.field_71439_g.func_184605_cv() == 0) {
            mc.field_71439_g.func_110148_a(EntityPlayer.REACH_DISTANCE).func_111128_a(100.0D);
            this.canDraw = true;
         } else {
            this.canDraw = false;
            mc.field_71439_g.func_110148_a(EntityPlayer.REACH_DISTANCE).func_111128_a((double)this.reach);
         }

         if (this.teleportPosition != null && this.delay == 0 && Mouse.isButtonDown(1)) {
            this.Mathteleport();
            this.delay = 5;
         }

         if (this.delay > 0) {
            --this.delay;
         }

         super.onClientTick(event);
      }
   }

   private void Mathteleport() {
      if (this.math.getValBoolean()) {
         double[] playerPosition = new double[]{mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v};
         double[] blockPosition = new double[]{(double)((float)this.teleportPosition.func_177958_n() + 0.5F), (double)this.teleportPosition.func_177956_o() + this.getOffset(mc.field_71441_e.func_180495_p(this.teleportPosition).func_177230_c(), this.teleportPosition) + 1.0D, (double)((float)this.teleportPosition.func_177952_p() + 0.5F)};
         Utils.teleportToPosition(playerPosition, blockPosition, 0.25D, 0.0D, true, true);
         mc.field_71439_g.func_70107_b(blockPosition[0], blockPosition[1], blockPosition[2]);
         this.teleportPosition = null;
      } else {
         double x = (double)this.teleportPosition.func_177958_n();
         double y = (double)(this.teleportPosition.func_177956_o() + 1);
         double z = (double)this.teleportPosition.func_177952_p();
         mc.field_71439_g.func_70107_b(x, y, z);

         for(int i = 0; i < 1; ++i) {
            Wrapper.INSTANCE.sendPacket(new Position(x, y, z, mc.field_71439_g.field_70122_E));
         }
      }

   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Flight")) {
         EntityPlayerSP player = mc.field_71439_g;
         GameSettings settings = Wrapper.INSTANCE.mcSettings();
         if (!this.passPacket) {
            player.field_70145_X = true;
            player.field_70143_R = 0.0F;
            player.field_70122_E = true;
            player.field_71075_bZ.field_75100_b = false;
            player.field_70159_w = 0.0D;
            player.field_70181_x = 0.0D;
            player.field_70179_y = 0.0D;
            float speed = 0.5F;
            if (settings.field_74314_A.func_151470_d()) {
               player.field_70181_x += (double)speed;
            }

            if (settings.field_74311_E.func_151470_d()) {
               player.field_70181_x -= (double)speed;
            }

            double d7 = (double)(player.field_70177_z + 90.0F);
            boolean flag4 = settings.field_74351_w.func_151470_d();
            boolean flag6 = settings.field_74368_y.func_151470_d();
            boolean flag8 = settings.field_74370_x.func_151470_d();
            boolean flag10 = settings.field_74366_z.func_151470_d();
            if (flag4) {
               if (flag8) {
                  d7 -= 45.0D;
               } else if (flag10) {
                  d7 += 45.0D;
               }
            } else if (flag6) {
               d7 += 180.0D;
               if (flag8) {
                  d7 += 45.0D;
               } else if (flag10) {
                  d7 -= 45.0D;
               }
            } else if (flag8) {
               d7 -= 90.0D;
            } else if (flag10) {
               d7 += 90.0D;
            }

            if (flag4 || flag8 || flag6 || flag10) {
               player.field_70159_w = Math.cos(Math.toRadians(d7));
               player.field_70179_y = Math.sin(Math.toRadians(d7));
            }
         }

         super.onLivingUpdate(event);
      }
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Flight")) {
         if (this.teleportPosition != null) {
            if (this.teleportPosition.func_177956_o() == (new BlockPos(mc.field_71439_g)).func_177977_b().func_177956_o()) {
               RenderUtils.RenderBlock(this.TpMode.getValString(), RenderUtils.Standardbb(this.teleportPosition), this.Path.getcolor(), this.LineWidth.getValDouble());
            } else {
               RenderUtils.RenderBlock(this.TpMode.getValString(), RenderUtils.Standardbb(this.teleportPosition), this.Land.getcolor(), this.LineWidth.getValDouble());
            }
         }
      } else {
         RayTraceResult object = Wrapper.INSTANCE.mc().field_71476_x;
         if (object != null) {
            object.func_178782_a();
            float offset;
            double[] mouseOverPos;
            if (this.canDraw) {
               for(offset = -2.0F; offset < 18.0F; ++offset) {
                  mouseOverPos = new double[]{(double)object.func_178782_a().func_177958_n(), (double)((float)object.func_178782_a().func_177956_o() + offset), (double)object.func_178782_a().func_177952_p()};
                  if (this.BlockTeleport(mouseOverPos)) {
                     break;
                  }
               }
            } else if (object.field_72308_g != null) {
               for(offset = -2.0F; offset < 18.0F; ++offset) {
                  mouseOverPos = new double[]{object.field_72308_g.field_70165_t, object.field_72308_g.field_70163_u + (double)offset, object.field_72308_g.field_70161_v};
                  if (this.BlockTeleport(mouseOverPos)) {
                     break;
                  }
               }
            } else {
               this.teleportPosition = null;
            }

            super.onRenderWorldLast(event);
         }
      }
   }

   private boolean BlockTeleport(double[] mouseOverPos) {
      BlockPos blockBelowPos = new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2]);
      if (this.canRenderBox(mouseOverPos)) {
         RenderUtils.RenderBlock(this.TpMode.getValString(), RenderUtils.Standardbb(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2])), this.Path.getcolor(), this.LineWidth.getValDouble());
         if (Wrapper.INSTANCE.mc().field_71415_G) {
            this.teleportPosition = blockBelowPos;
            return true;
         }

         this.teleportPosition = null;
      }

      return false;
   }

   public boolean canRenderBox(double[] mouseOverPos) {
      boolean canTeleport = false;
      Block blockBelowPos = mc.field_71441_e.func_180495_p(new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0D, mouseOverPos[2])).func_177230_c();
      Block blockPos = mc.field_71441_e.func_180495_p(new BlockPos(mouseOverPos[0], mouseOverPos[1], mouseOverPos[2])).func_177230_c();
      Block blockAbovePos = mc.field_71441_e.func_180495_p(new BlockPos(mouseOverPos[0], mouseOverPos[1] + 1.0D, mouseOverPos[2])).func_177230_c();
      boolean validBlockBelow = blockBelowPos.func_180646_a(mc.field_71441_e.func_180495_p(new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0D, mouseOverPos[2])), mc.field_71441_e, new BlockPos(mouseOverPos[0], mouseOverPos[1] - 1.0D, mouseOverPos[2])) != null;
      boolean validBlock = this.isValidBlock(blockPos);
      boolean validBlockAbove = this.isValidBlock(blockAbovePos);
      if (validBlockBelow && validBlock && validBlockAbove) {
         canTeleport = true;
      }

      return canTeleport;
   }

   public double getOffset(Block block, BlockPos pos) {
      IBlockState state = mc.field_71441_e.func_180495_p(pos);
      double offset = 0.0D;
      if (block instanceof BlockSlab && !((BlockSlab)block).func_176552_j()) {
         offset -= 0.5D;
      } else if (block instanceof BlockEndPortalFrame) {
         offset -= 0.20000000298023224D;
      } else if (block instanceof BlockBed) {
         offset -= 0.4399999976158142D;
      } else if (block instanceof BlockCake) {
         offset -= 0.5D;
      } else if (block instanceof BlockDaylightDetector) {
         offset -= 0.625D;
      } else if (!(block instanceof BlockRedstoneComparator) && !(block instanceof BlockRedstoneRepeater)) {
         if (!(block instanceof BlockChest) && block != Blocks.field_150477_bB) {
            if (block instanceof BlockLilyPad) {
               offset -= 0.949999988079071D;
            } else if (block == Blocks.field_150431_aC) {
               offset -= 0.875D;
               offset += (double)(0.125F * (float)((Integer)state.func_177229_b(BlockSnow.field_176315_a) - 1));
            } else if (this.isValidBlock(block)) {
               --offset;
            }
         } else {
            offset -= 0.125D;
         }
      } else {
         offset -= 0.875D;
      }

      return offset;
   }

   public boolean isValidBlock(Block block) {
      return block == Blocks.field_150427_aO || block == Blocks.field_150431_aC || block instanceof BlockTripWireHook || block instanceof BlockTripWire || block instanceof BlockDaylightDetector || block instanceof BlockRedstoneComparator || block instanceof BlockRedstoneRepeater || block instanceof BlockSign || block instanceof BlockAir || block instanceof BlockPressurePlate || block instanceof BlockTallGrass || block instanceof BlockFlower || block instanceof BlockMushroom || block instanceof BlockDoublePlant || block instanceof BlockReed || block instanceof BlockSapling || block == Blocks.field_150459_bM || block == Blocks.field_150464_aj || block == Blocks.field_150388_bm || block == Blocks.field_150469_bN || block == Blocks.field_150393_bb || block == Blocks.field_150394_bc || block == Blocks.field_150443_bT || block == Blocks.field_150445_bS || block == Blocks.field_150488_af || block instanceof BlockTorch || block == Blocks.field_150442_at || block instanceof BlockButton;
   }
}
