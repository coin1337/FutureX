package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.visual.Executer;
import Method.Client.utils.visual.RenderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class HoleEsp extends Module {
   Setting Radius;
   Setting Void;
   Setting Bedrock;
   Setting obby;
   Setting Burrow;
   Setting OwnHole;
   Setting Timer;
   Setting Mode;
   Setting LineWidth;
   Setting BurrowDetect;
   Vec3i playerPos;
   TimerUtils timer;
   public final List<Hole> holes;

   public HoleEsp() {
      super("HoleEsp", 0, Category.COMBAT, "HoleEsp");
      this.Radius = Main.setmgr.add(new Setting("Radius", this, 8.0D, 0.0D, 32.0D, true));
      this.Void = Main.setmgr.add(new Setting("Void", this, 0.85D, 1.0D, 1.0D, 0.75D));
      this.Bedrock = Main.setmgr.add(new Setting("Bedrock", this, 0.55D, 1.0D, 1.0D, 0.75D));
      this.obby = Main.setmgr.add(new Setting("obby", this, 0.22D, 1.0D, 1.0D, 0.75D));
      this.Burrow = Main.setmgr.add(new Setting("Burrow", this, 0.4D, 1.0D, 1.0D, 0.75D));
      this.OwnHole = Main.setmgr.add(new Setting("Ignore Own", this, true));
      this.Timer = Main.setmgr.add(new Setting("Timer", this, 250.0D, 0.0D, 500.0D, true));
      this.Mode = Main.setmgr.add(new Setting("Hole Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.BurrowDetect = Main.setmgr.add(new Setting("Burrow Detect", this, true));
      this.timer = new TimerUtils();
      this.holes = new ArrayList();
   }

   public void onEnable() {
      Executer.init();
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.timer.isDelay((long)this.Timer.getValDouble()) && !this.Mode.getValString().equalsIgnoreCase("None")) {
         this.playerPos = new Vec3i(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
         this.holes.clear();
         Executer.execute(() -> {
            BlockPos blockPos;
            if (this.BurrowDetect.getValBoolean()) {
               Iterator var1 = mc.field_71441_e.field_72996_f.iterator();

               while(var1.hasNext()) {
                  Entity entity = (Entity)var1.next();
                  if (entity instanceof EntityPlayer) {
                     EntityPlayer entityPlayer = (EntityPlayer)entity;
                     if (this.isInBurrow(entityPlayer)) {
                        blockPos = new BlockPos(entityPlayer);
                        this.holes.add(new Hole(blockPos.field_177962_a, blockPos.field_177960_b, blockPos.field_177961_c, blockPos, Hole.HoleTypes.Burrow, false));
                     }
                  }
               }
            }

            for(int x = (int)((double)this.playerPos.func_177958_n() - this.Radius.getValDouble()); (double)x < (double)this.playerPos.func_177958_n() + this.Radius.getValDouble(); ++x) {
               for(int z = (int)((double)this.playerPos.func_177952_p() - this.Radius.getValDouble()); (double)z < (double)this.playerPos.func_177952_p() + this.Radius.getValDouble(); ++z) {
                  for(int y = this.playerPos.func_177956_o() + 6; y > this.playerPos.func_177956_o() - 6; --y) {
                     blockPos = new BlockPos(x, y, z);
                     if (!this.OwnHole.getValBoolean() || !(mc.field_71439_g.func_174818_b(blockPos) <= 1.0D)) {
                        Hole.HoleTypes l_Type = isHoleValid(mc.field_71441_e.func_180495_p(blockPos), blockPos);
                        if (l_Type != Hole.HoleTypes.None) {
                           if (l_Type == Hole.HoleTypes.Void) {
                              this.holes.add(new Hole(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), blockPos, Hole.HoleTypes.Void, true));
                           } else {
                              IBlockState downBlockState = mc.field_71441_e.func_180495_p(blockPos.func_177977_b());
                              if (downBlockState.func_177230_c() == Blocks.field_150350_a) {
                                 BlockPos downPos = blockPos.func_177977_b();
                                 l_Type = isHoleValid(downBlockState, blockPos);
                                 if (l_Type != Hole.HoleTypes.None) {
                                    this.holes.add(new Hole(downPos.func_177958_n(), downPos.func_177956_o(), downPos.func_177952_p(), downPos, l_Type, true));
                                 }
                              } else {
                                 this.holes.add(new Hole(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), blockPos, l_Type, false));
                              }
                           }
                        }
                     }
                  }
               }
            }

         });
         this.timer.setLastMS();
      }

   }

   private boolean isInBurrow(EntityPlayer entityPlayer) {
      BlockPos playerPos = new BlockPos(Math.floor(entityPlayer.field_70165_t + 0.5D), entityPlayer.field_70163_u, Math.floor(entityPlayer.field_70161_v + 0.5D));
      return MC.field_71441_e.func_180495_p(playerPos).func_177230_c() == Blocks.field_150343_Z || MC.field_71441_e.func_180495_p(playerPos).func_177230_c() == Blocks.field_150477_bB || MC.field_71441_e.func_180495_p(playerPos).func_177230_c() == Blocks.field_150467_bQ;
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (!this.Mode.getValString().equalsIgnoreCase("None")) {
         Iterator var2 = this.holes.iterator();

         while(var2.hasNext()) {
            Hole hole = (Hole)var2.next();
            double renderPosX = (double)hole.func_177958_n() - mc.func_175598_ae().field_78730_l;
            double renderPosY = (double)hole.func_177956_o() - mc.func_175598_ae().field_78731_m;
            double renderPosZ = (double)hole.func_177952_p() - mc.func_175598_ae().field_78728_n;
            AxisAlignedBB bb = new AxisAlignedBB(renderPosX, renderPosY, renderPosZ, renderPosX + 1.0D, renderPosY + (double)(hole.isTall() ? 2 : 1), renderPosZ + 1.0D);
            RenderUtils.RenderBlock(this.Mode.getValString(), bb, hole.GetHoleType() == Hole.HoleTypes.Bedrock ? this.Bedrock.getcolor() : (hole.GetHoleType() == Hole.HoleTypes.Obsidian ? this.obby.getcolor() : (hole.GetHoleType() == Hole.HoleTypes.Burrow ? this.Burrow.getcolor() : this.Void.getcolor())), this.LineWidth.getValDouble());
         }
      }

   }

   public static Hole.HoleTypes isHoleValid(IBlockState blockState, BlockPos blockPos) {
      if (blockState.func_177230_c() != Blocks.field_150350_a) {
         return Hole.HoleTypes.None;
      } else if (blockState.func_177230_c() == Blocks.field_150350_a && blockPos.field_177960_b == 0) {
         return Hole.HoleTypes.Void;
      } else if (mc.field_71441_e.func_180495_p(blockPos.func_177984_a()).func_177230_c() != Blocks.field_150350_a) {
         return Hole.HoleTypes.None;
      } else if (mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2)).func_177230_c() != Blocks.field_150350_a) {
         return Hole.HoleTypes.None;
      } else if (mc.field_71441_e.func_180495_p(blockPos.func_177977_b()).func_177230_c() == Blocks.field_150350_a) {
         return Hole.HoleTypes.None;
      } else {
         BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e()};
         boolean AllBedrock = true;
         BlockPos[] var4 = touchingBlocks;
         int var5 = touchingBlocks.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            BlockPos touching = var4[var6];
            IBlockState touchingState = mc.field_71441_e.func_180495_p(touching);
            if (touchingState.func_177230_c() == Blocks.field_150350_a || !touchingState.func_185913_b()) {
               return Hole.HoleTypes.None;
            }

            if (touchingState.func_177230_c() == Blocks.field_150343_Z) {
               AllBedrock = false;
            } else if (touchingState.func_177230_c() != Blocks.field_150357_h) {
               return Hole.HoleTypes.None;
            }
         }

         return AllBedrock ? Hole.HoleTypes.Bedrock : Hole.HoleTypes.Obsidian;
      }
   }
}
