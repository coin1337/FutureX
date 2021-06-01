package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.SeedViewer.WorldLoader;
import Method.Client.utils.visual.ChatUtils;
import Method.Client.utils.visual.RenderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.BlockNetherrack;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockVine;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class SeedViewer extends Module {
   Setting OverlayColor;
   Setting Mode;
   Setting LineWidth;
   Setting BlockLimit;
   Setting Fallingblock;
   Setting Liquid;
   Setting Tree;
   Setting Bush;
   Setting Distance;
   Setting LavaMix;
   Setting FalsePositive;
   Setting GrassSpread;
   private static ExecutorService executor;
   private static ExecutorService executor2;
   public int currentdis;
   private ArrayList<SeedViewer.ChunkData> chunks;
   private ArrayList<int[]> tobesearch;
   private final TimerUtils timer;

   public void setup() {
      executor = Executors.newSingleThreadExecutor();
      executor2 = Executors.newSingleThreadExecutor();
   }

   public SeedViewer() {
      super("SeedViewer", 0, Category.RENDER, "SeedViewer");
      this.OverlayColor = Main.setmgr.add(new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      this.Mode = Main.setmgr.add(new Setting("Hole Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.BlockLimit = Main.setmgr.add(new Setting("BlockLimit", this, 200.0D, 0.0D, 5000.0D, false));
      this.Fallingblock = Main.setmgr.add(new Setting("Falling block", this, false));
      this.Liquid = Main.setmgr.add(new Setting("Liquid", this, false));
      this.Tree = Main.setmgr.add(new Setting("Tree", this, false));
      this.Bush = Main.setmgr.add(new Setting("Bush", this, false));
      this.Distance = Main.setmgr.add(new Setting("Distance", this, 6.0D, 0.0D, 15.0D, true));
      this.LavaMix = Main.setmgr.add(new Setting("LavaMix", this, false));
      this.FalsePositive = Main.setmgr.add(new Setting("FalsePositive", this, false));
      this.GrassSpread = Main.setmgr.add(new Setting("GrassSpread", this, false));
      this.currentdis = 0;
      this.chunks = new ArrayList();
      this.tobesearch = new ArrayList();
      this.timer = new TimerUtils();
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.timer.isDelay(500L)) {
         if (mc.field_71439_g.field_71093_bK != this.currentdis) {
            ChatUtils.warning("You must reenable on dimension change!");
            this.toggle();
         }

         this.searchViewDistance();
         this.runviewdistance();
         this.timer.setLastMS();
      }

      int[] remove = null;
      Iterator var3 = this.tobesearch.iterator();

      while(var3.hasNext()) {
         int[] vec2d = (int[])var3.next();
         remove = vec2d;
         executor.execute(() -> {
            WorldLoader.CreateChunk(vec2d[0], vec2d[1], mc.field_71439_g.field_71093_bK);
         });
      }

      this.tobesearch.remove(remove);
   }

   public void onEnable() {
      if (mc.func_71356_B()) {
         ChatUtils.warning("Only for multiplayer");
         this.toggle();
      }

      if (WorldLoader.seed == 44776655L) {
         ChatUtils.message("Set Seed using the" + TextFormatting.GOLD + " @WorldSeed" + TextFormatting.RESET + " Command");
         this.toggle();
      } else {
         this.currentdis = mc.field_71439_g.field_71093_bK;
         executor = Executors.newSingleThreadExecutor();
         executor2 = Executors.newSingleThreadExecutor();
         WorldLoader.setup();
         ChatUtils.warning("Still Working on this.");
         this.chunks = new ArrayList();
         this.searchViewDistance();
      }
   }

   private void searchViewDistance() {
      executor.execute(() -> {
         for(int x = mc.field_71439_g.field_70176_ah - (int)this.Distance.getValDouble(); x <= mc.field_71439_g.field_70176_ah + (int)this.Distance.getValDouble(); ++x) {
            for(int z = mc.field_71439_g.field_70164_aj - (int)this.Distance.getValDouble(); z <= mc.field_71439_g.field_70164_aj + (int)this.Distance.getValDouble(); ++z) {
               if (this.havenotsearched(x, z) && mc.field_71441_e.func_190526_b(x, z)) {
                  boolean found = false;
                  Iterator var4 = this.tobesearch.iterator();

                  while(var4.hasNext()) {
                     int[] vec2d = (int[])var4.next();
                     if (vec2d[0] == x && vec2d[1] == z) {
                        found = true;
                        break;
                     }
                  }

                  if (!found) {
                     this.tobesearch.add(new int[]{x, z});
                  }
               }
            }
         }

      });
   }

   private void runviewdistance() {
      for(int x = mc.field_71439_g.field_70176_ah - (int)this.Distance.getValDouble(); x <= mc.field_71439_g.field_70176_ah + (int)this.Distance.getValDouble(); ++x) {
         for(int z = mc.field_71439_g.field_70164_aj - (int)this.Distance.getValDouble(); z <= mc.field_71439_g.field_70164_aj + (int)this.Distance.getValDouble(); ++z) {
            if (mc.field_71441_e.func_190526_b(x, z) && WorldLoader.fakeworld.func_190526_b(x, z) && WorldLoader.fakeworld.func_190526_b(x + 1, z) && WorldLoader.fakeworld.func_190526_b(x, z + 1) && WorldLoader.fakeworld.func_190526_b(x + 1, z + 1) && this.havenotsearched(x, z)) {
               SeedViewer.ChunkData data = new SeedViewer.ChunkData(new ChunkPos(x, z), false);
               this.searchChunk(mc.field_71441_e.func_72964_e(x, z), data);
               this.chunks.add(data);
            }
         }
      }

   }

   private boolean havenotsearched(int x, int z) {
      Iterator var3 = this.chunks.iterator();

      SeedViewer.ChunkData chunk;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         chunk = (SeedViewer.ChunkData)var3.next();
      } while(chunk.chunkPos.field_77276_a != x || chunk.chunkPos.field_77275_b != z);

      return false;
   }

   private void searchChunk(Chunk chunk, SeedViewer.ChunkData data) {
      executor2.execute(() -> {
         try {
            for(int x = chunk.func_76632_l().func_180334_c(); x <= chunk.func_76632_l().func_180332_e(); ++x) {
               for(int z = chunk.func_76632_l().func_180333_d(); z <= chunk.func_76632_l().func_180330_f(); ++z) {
                  for(int y = 0; y < 255; ++y) {
                     if (this.BlockFast(new BlockPos(x, y, z), WorldLoader.fakeworld.func_180495_p(new BlockPos(x, y, z)).func_177230_c(), chunk.func_186032_a(x, y, z).func_177230_c())) {
                        data.blocks.add(new BlockPos(x, y, z));
                     }
                  }
               }
            }

            data.Searched = true;
         } catch (Exception var6) {
         }

      });
   }

   private boolean BlockFast(BlockPos blockPos, Block FakeChunk, Block RealChunk) {
      if (RealChunk instanceof BlockSnow) {
         return false;
      } else if (FakeChunk instanceof BlockSnow) {
         return false;
      } else if (RealChunk instanceof BlockVine) {
         return false;
      } else if (FakeChunk instanceof BlockVine) {
         return false;
      } else {
         if (!this.Fallingblock.getValBoolean()) {
            if (RealChunk instanceof BlockFalling) {
               return false;
            }

            if (FakeChunk instanceof BlockFalling) {
               return false;
            }
         }

         if (!this.Liquid.getValBoolean()) {
            if (RealChunk instanceof BlockLiquid) {
               return false;
            }

            if (FakeChunk instanceof BlockLiquid) {
               return false;
            }

            if (mc.field_71441_e.func_180495_p(blockPos.func_177977_b()).func_177230_c() instanceof BlockLiquid) {
               return false;
            }

            if (mc.field_71441_e.func_180495_p(blockPos.func_177979_c(2)).func_177230_c() instanceof BlockLiquid) {
               return false;
            }
         }

         if (!this.Tree.getValBoolean()) {
            if (FakeChunk instanceof BlockGrass && this.Treeroots(blockPos)) {
               return false;
            }

            if (RealChunk instanceof BlockLog || RealChunk instanceof BlockLeaves) {
               return false;
            }

            if (FakeChunk instanceof BlockLog || FakeChunk instanceof BlockLeaves) {
               return false;
            }
         }

         if (!this.GrassSpread.getValBoolean()) {
            if (RealChunk instanceof BlockGrass && FakeChunk instanceof BlockDirt) {
               return false;
            }

            if (RealChunk instanceof BlockDirt && FakeChunk instanceof BlockGrass) {
               return false;
            }
         }

         if (!this.Bush.getValBoolean()) {
            if (RealChunk instanceof BlockBush) {
               return false;
            }

            if (RealChunk instanceof BlockReed) {
               return false;
            }

            if (FakeChunk instanceof BlockBush) {
               return false;
            }
         }

         if (!this.LavaMix.getValBoolean() && (RealChunk instanceof BlockObsidian || RealChunk.equals(Blocks.field_150347_e)) && this.Lavamix(blockPos)) {
            return false;
         } else {
            if (!this.FalsePositive.getValBoolean()) {
               if (FakeChunk instanceof BlockOre && (RealChunk instanceof BlockStone || RealChunk instanceof BlockMagma || RealChunk instanceof BlockNetherrack || RealChunk instanceof BlockDirt)) {
                  return false;
               }

               if (RealChunk instanceof BlockOre && (FakeChunk instanceof BlockStone || FakeChunk instanceof BlockMagma || FakeChunk instanceof BlockNetherrack || FakeChunk instanceof BlockDirt)) {
                  return false;
               }

               if (FakeChunk instanceof BlockRedstoneOre && (RealChunk instanceof BlockStone || RealChunk instanceof BlockDirt)) {
                  return false;
               }

               if (RealChunk instanceof BlockRedstoneOre && (FakeChunk instanceof BlockStone || FakeChunk instanceof BlockDirt)) {
                  return false;
               }

               if (FakeChunk instanceof BlockGlowstone && RealChunk instanceof BlockAir) {
                  return false;
               }

               if (RealChunk instanceof BlockGlowstone && FakeChunk instanceof BlockAir) {
                  return false;
               }

               if (FakeChunk instanceof BlockMagma && RealChunk instanceof BlockNetherrack) {
                  return false;
               }

               if (RealChunk instanceof BlockMagma && FakeChunk instanceof BlockNetherrack) {
                  return false;
               }

               if (RealChunk instanceof BlockFire || FakeChunk instanceof BlockFire) {
                  return false;
               }

               if (RealChunk instanceof BlockOre && FakeChunk instanceof BlockOre) {
                  return false;
               }

               if (RealChunk.func_149732_F().equals(Blocks.field_150418_aU.func_149732_F()) && FakeChunk instanceof BlockStone) {
                  return false;
               }

               if (FakeChunk instanceof BlockStone && RealChunk instanceof BlockDirt || FakeChunk instanceof BlockDirt && RealChunk instanceof BlockStone) {
                  return false;
               }

               if (!(FakeChunk instanceof BlockAir) && RealChunk instanceof BlockAir && !mc.field_71441_e.func_180495_p(blockPos).func_177230_c().func_149732_F().equals(RealChunk.func_149732_F())) {
                  return false;
               }
            }

            if (!FakeChunk.func_149732_F().equals(RealChunk.func_149732_F())) {
               return true;
            } else {
               return false;
            }
         }
      }
   }

   public boolean Treeroots(BlockPos b) {
      return mc.field_71441_e.func_180495_p(b.func_177984_a()).func_177230_c() instanceof BlockLog;
   }

   public boolean Lavamix(BlockPos b) {
      if (mc.field_71441_e.func_180495_p(b.func_177984_a()).func_177230_c() instanceof BlockLiquid) {
         return true;
      } else if (mc.field_71441_e.func_180495_p(b.func_177977_b()).func_177230_c() instanceof BlockLiquid) {
         return true;
      } else if (mc.field_71441_e.func_180495_p(b.func_177982_a(1, 0, 0)).func_177230_c() instanceof BlockLiquid) {
         return true;
      } else if (mc.field_71441_e.func_180495_p(b.func_177982_a(0, 0, 1)).func_177230_c() instanceof BlockLiquid) {
         return true;
      } else if (mc.field_71441_e.func_180495_p(b.func_177982_a(-1, 0, 0)).func_177230_c() instanceof BlockLiquid) {
         return true;
      } else {
         return mc.field_71441_e.func_180495_p(b.func_177982_a(0, 0, -1)).func_177230_c() instanceof BlockLiquid;
      }
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      try {
         int blocklimit = 0;
         ArrayList<SeedViewer.ChunkData> Remove = new ArrayList();
         Iterator var4 = this.chunks.iterator();

         label37:
         while(true) {
            SeedViewer.ChunkData chunk;
            do {
               if (!var4.hasNext()) {
                  this.chunks.removeAll(Remove);
                  break label37;
               }

               chunk = (SeedViewer.ChunkData)var4.next();
            } while(!chunk.Searched);

            if (mc.field_71439_g.func_70011_f((double)chunk.chunkPos.func_180332_e(), 100.0D, (double)chunk.chunkPos.func_180330_f()) > 2000.0D) {
               Remove.add(chunk);
            }

            for(Iterator var6 = chunk.blocks.iterator(); var6.hasNext(); ++blocklimit) {
               BlockPos block = (BlockPos)var6.next();
               if ((double)blocklimit > this.BlockLimit.getValDouble()) {
                  break;
               }

               RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Standardbb(new BlockPos(block.field_177962_a, block.field_177960_b, block.field_177961_c)), this.OverlayColor.getcolor(), this.LineWidth.getValDouble());
            }
         }
      } catch (Exception var8) {
      }

      super.onRenderWorldLast(event);
   }

   public static class ChunkData {
      private boolean Searched;
      public final List<BlockPos> blocks = new ArrayList();
      private ChunkPos chunkPos;

      public List<BlockPos> getBlocks() {
         return this.blocks;
      }

      public ChunkData(ChunkPos chunkPos, boolean Searched) {
         this.chunkPos = chunkPos;
         this.Searched = Searched;
      }
   }
}
