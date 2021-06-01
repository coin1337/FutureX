package Method.Client.utils.SeedViewer;

import Method.Client.utils.system.Wrapper;
import java.util.Random;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class WorldLoader {
   public static IChunkGenerator ChunkGenerator;
   public static long seed = 44776655L;
   public static boolean GenerateStructures = true;
   public static AwesomeWorld fakeworld;
   public static Random rand;

   public static void setup() {
      WorldSettings worldSettings = new WorldSettings(seed, GameType.SURVIVAL, GenerateStructures, false, WorldType.field_77137_b);
      WorldInfo worldInfo = new WorldInfo(worldSettings, "FakeWorld");
      worldInfo.func_176128_f(true);
      fakeworld = new AwesomeWorld(worldInfo);
      if (Wrapper.mc.field_71439_g.field_71093_bK == -1) {
         ChunkGenerator = new ChunkGeneratorHell(fakeworld, fakeworld.func_72912_H().func_76089_r(), seed);
      } else {
         ChunkGenerator = fakeworld.field_73011_w.func_186060_c();
      }

   }

   public static Chunk CreateChunk(int x, int z, int dis) {
      if (dis == -1 && !(ChunkGenerator instanceof ChunkGeneratorHell)) {
         ChunkGenerator = new ChunkGeneratorHell(fakeworld, fakeworld.func_72912_H().func_76089_r(), seed);
      }

      Chunk Testchunk;
      if (!fakeworld.func_190526_b(x, z)) {
         Testchunk = ChunkGenerator.func_185932_a(x, z);
      } else {
         Testchunk = fakeworld.func_72964_e(x, z);
      }

      fakeworld.getChunkProvider().field_73236_b.put(ChunkPos.func_77272_a(x, z), Testchunk);
      Testchunk.func_76631_c();
      populate(fakeworld.getChunkProvider(), ChunkGenerator, x, z);
      return Testchunk;
   }

   public static void populate(IChunkProvider chunkProvider, IChunkGenerator chunkGenrator, int x, int z) {
      Chunk chunk = chunkProvider.func_186026_b(x, z - 1);
      Chunk chunk1 = chunkProvider.func_186026_b(x + 1, z);
      Chunk chunk2 = chunkProvider.func_186026_b(x, z + 1);
      Chunk chunk3 = chunkProvider.func_186026_b(x - 1, z);
      if (chunk1 != null && chunk2 != null && chunkProvider.func_186026_b(x + 1, z + 1) != null) {
         Awesomepopulate(chunkGenrator, fakeworld, x, z);
      }

      if (chunk3 != null && chunk2 != null && chunkProvider.func_186026_b(x - 1, z + 1) != null) {
         Awesomepopulate(chunkGenrator, fakeworld, x - 1, z);
      }

      if (chunk != null && chunk1 != null && chunkProvider.func_186026_b(x + 1, z - 1) != null) {
         Awesomepopulate(chunkGenrator, fakeworld, x, z - 1);
      }

      if (chunk != null && chunk3 != null) {
         Chunk chunk4 = chunkProvider.func_186026_b(x - 1, z - 1);
         if (chunk4 != null) {
            Awesomepopulate(chunkGenrator, fakeworld, x - 1, z - 1);
         }
      }

   }

   private static void Awesomepopulate(IChunkGenerator overworldChunkGen, AwesomeWorld fakeworld, int x, int z) {
      Chunk testchunk = fakeworld.func_72964_e(x, z);
      if (testchunk.func_177419_t()) {
         if (overworldChunkGen.func_185933_a(testchunk, x, z)) {
            testchunk.func_76630_e();
         }
      } else {
         testchunk.func_150809_p();
         overworldChunkGen.func_185931_b(x, z);
         testchunk.func_76630_e();
      }

   }

   public static void event(Populate event) {
      event.setResult(Result.ALLOW);
   }

   public static void DecorateBiomeEvent(Decorate event) {
      event.setResult(Result.ALLOW);
   }
}
