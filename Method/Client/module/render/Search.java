package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Screens.Custom.Search.SearchGuiSettings;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.Executer;
import Method.Client.utils.visual.RenderUtils;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange.BlockUpdateData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.ChunkEvent.Load;

public class Search extends Module {
   Setting OverlayColor;
   Setting Mode;
   Setting LineWidth;
   Setting Gui;
   private final SearchGuiSettings blocks;
   public static ArrayList<String> blockNames;
   private final Long2ObjectArrayMap<Search.MyChunk> chunks;
   private final Search.Pool<Search.MyBlock> blockPool;
   private final LongList toRemove;
   private final MutableBlockPos blockPos;

   public Search() {
      super("Search", 0, Category.RENDER, "Search");
      this.OverlayColor = Main.setmgr.add(new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 0.42D));
      this.Mode = Main.setmgr.add(new Setting("block Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.8D, 0.0D, 3.0D, false));
      this.Gui = Main.setmgr.add(new Setting("Gui", this, Main.Search));
      this.blocks = new SearchGuiSettings(new Block[]{Blocks.field_150477_bB, Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z, Blocks.field_150415_aT, Blocks.field_150381_bn, Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA});
      this.chunks = new Long2ObjectArrayMap();
      this.blockPool = new Search.Pool(() -> {
         return new Search.MyBlock();
      });
      this.toRemove = new LongArrayList();
      this.blockPos = new MutableBlockPos();
   }

   public void setup() {
      Executer.init();
   }

   public void onEnable() {
      blockNames = new ArrayList(SearchGuiSettings.getBlockNames());
      Executer.init();
      this.searchViewDistance();
   }

   public void onDisable() {
      ObjectIterator var1 = this.chunks.values().iterator();

      while(var1.hasNext()) {
         Search.MyChunk chunk = (Search.MyChunk)var1.next();
         chunk.dispose();
      }

      this.chunks.clear();
   }

   private void searchViewDistance() {
      int viewDist = mc.field_71474_y.field_151451_c;

      for(int x = mc.field_71439_g.field_70176_ah - viewDist; x <= mc.field_71439_g.field_70176_ah + viewDist; ++x) {
         for(int z = mc.field_71439_g.field_70164_aj - viewDist; z <= mc.field_71439_g.field_70164_aj + viewDist; ++z) {
            if (mc.field_71441_e.func_190526_b(x, z)) {
               this.searchChunk(mc.field_71441_e.func_72964_e(x, z));
            }
         }
      }

   }

   public void ChunkeventLOAD(Load event) {
      this.searchChunk(event.getChunk());
   }

   private void searchChunk(Chunk chunk) {
      Executer.execute(() -> {
         Search.MyChunk myChunk = new Search.MyChunk(chunk.func_76632_l().field_77276_a, chunk.func_76632_l().field_77275_b);

         for(int x = chunk.func_76632_l().func_180334_c(); x <= chunk.func_76632_l().func_180332_e(); ++x) {
            for(int z = chunk.func_76632_l().func_180333_d(); z <= chunk.func_76632_l().func_180330_f(); ++z) {
               for(int y = 0; y < 256; ++y) {
                  this.blockPos.func_181079_c(x, y, z);
                  if (this.isVisible(chunk.func_177435_g(this.blockPos).func_177230_c())) {
                     myChunk.add(this.blockPos, false);
                  }
               }
            }
         }

         synchronized(this.chunks) {
            if (myChunk.blocks.size() > 0) {
               this.chunks.put(ChunkPos.func_77272_a(chunk.func_76632_l().field_77276_a, chunk.func_76632_l().field_77275_b), myChunk);
            }

         }
      });
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketBlockChange) {
         SPacketBlockChange packet2 = (SPacketBlockChange)packet;
         this.onBlockUpdate(packet2.func_179827_b(), packet2.field_148883_d);
      }

      if (packet instanceof SPacketBlockAction) {
         SPacketBlockAction packet2 = (SPacketBlockAction)packet;
         this.onBlockUpdate(packet2.func_179825_a(), packet2.func_148868_c().func_176223_P());
      }

      if (packet instanceof SPacketMultiBlockChange) {
         SPacketMultiBlockChange packet2 = (SPacketMultiBlockChange)packet;
         BlockUpdateData[] var4 = packet2.func_179844_a();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            BlockUpdateData changedBlock = var4[var6];
            this.onBlockUpdate(changedBlock.func_180090_a(), changedBlock.func_180088_c());
         }
      }

      return true;
   }

   public void onBlockUpdate(BlockPos blockPos, IBlockState blockState) {
      Executer.execute(() -> {
         int chunkX = blockPos.func_177958_n() >> 4;
         int chunkZ = blockPos.func_177952_p() >> 4;
         long key = ChunkPos.func_77272_a(chunkX, chunkZ);
         synchronized(this.chunks) {
            if (this.isVisible(blockState.func_177230_c())) {
               ((Search.MyChunk)this.chunks.computeIfAbsent(key, (aLong) -> {
                  return new Search.MyChunk(chunkX, chunkZ);
               })).add(blockPos, true);
            } else {
               Search.MyChunk chunk = (Search.MyChunk)this.chunks.get(key);
               if (chunk != null) {
                  chunk.remove(blockPos);
               }
            }

         }
      });
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      synchronized(this.chunks) {
         this.toRemove.clear();
         LongIterator var3 = this.chunks.keySet().iterator();

         long key;
         while(var3.hasNext()) {
            key = (Long)var3.next();
            Search.MyChunk chunk = (Search.MyChunk)this.chunks.get(key);
            if (chunk.shouldBeDeleted()) {
               this.toRemove.add(key);
            } else {
               chunk.render();
            }
         }

         LongListIterator var9 = this.toRemove.iterator();

         while(var9.hasNext()) {
            key = (Long)var9.next();
            this.chunks.remove(key);
         }

      }
   }

   private boolean isVisible(Block block) {
      String name = getName(block);
      int index = Collections.binarySearch(blockNames, name);
      return index >= 0;
   }

   public static String getName(Block block) {
      return "" + Block.field_149771_c.func_177774_c(block);
   }

   public interface Producer<T> {
      T create();
   }

   public static class Pool<T> {
      private final List<T> items = new ArrayList();
      private final Search.Producer<T> producer;

      public Pool(Search.Producer<T> producer) {
         this.producer = producer;
      }

      public T get() {
         return this.items.size() > 0 ? this.items.remove(this.items.size() - 1) : this.producer.create();
      }

      public void free(T obj) {
         this.items.add(obj);
      }
   }

   private class MyBlock {
      private int x;
      private int y;
      private int z;

      private MyBlock() {
      }

      public void set(BlockPos blockPos) {
         this.x = blockPos.func_177958_n();
         this.y = blockPos.func_177956_o();
         this.z = blockPos.func_177952_p();
      }

      public void render() {
         RenderUtils.RenderBlock(Search.this.Mode.getValString(), RenderUtils.Standardbb(new BlockPos(this.x, this.y, this.z)), Search.this.OverlayColor.getcolor(), Search.this.LineWidth.getValDouble());
      }

      public boolean equals(BlockPos blockPos) {
         return this.x == blockPos.func_177958_n() && this.y == blockPos.func_177956_o() && this.z == blockPos.func_177952_p();
      }

      // $FF: synthetic method
      MyBlock(Object x1) {
         this();
      }
   }

   private class MyChunk {
      private final int x;
      private final int z;
      private final List<Search.MyBlock> blocks = new ArrayList();

      public MyChunk(int x, int z) {
         this.x = x;
         this.z = z;
      }

      public void add(BlockPos blockPos, boolean checkForDuplicates) {
         if (checkForDuplicates) {
            Iterator var3 = this.blocks.iterator();

            while(var3.hasNext()) {
               Search.MyBlock block = (Search.MyBlock)var3.next();
               if (block.equals(blockPos)) {
                  return;
               }
            }
         }

         Search.MyBlock blockx = (Search.MyBlock)Search.this.blockPool.get();
         blockx.set(blockPos);
         this.blocks.add(blockx);
      }

      public void remove(BlockPos blockPos) {
         for(int i = 0; i < this.blocks.size(); ++i) {
            Search.MyBlock block = (Search.MyBlock)this.blocks.get(i);
            if (block.equals(blockPos)) {
               this.blocks.remove(i);
               return;
            }
         }

      }

      public boolean shouldBeDeleted() {
         int viewDist = Search.mc.field_71474_y.field_151451_c + 1;
         return this.x > Search.mc.field_71439_g.field_70176_ah + viewDist || this.x < Search.mc.field_71439_g.field_70176_ah - viewDist || this.z > Search.mc.field_71439_g.field_70164_aj + viewDist || this.z < Search.mc.field_71439_g.field_70164_aj - viewDist;
      }

      public void render() {
         Iterator var1 = this.blocks.iterator();

         while(var1.hasNext()) {
            Search.MyBlock block = (Search.MyBlock)var1.next();
            block.render();
         }

      }

      public void dispose() {
         Iterator var1 = this.blocks.iterator();

         while(var1.hasNext()) {
            Search.MyBlock block = (Search.MyBlock)var1.next();
            Search.this.blockPool.free(block);
         }

         this.blocks.clear();
      }
   }
}
