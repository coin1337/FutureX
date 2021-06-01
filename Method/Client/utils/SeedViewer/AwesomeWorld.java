package Method.Client.utils.SeedViewer;

import Method.Client.utils.system.Wrapper;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

public class AwesomeWorld extends World {
   private ChunkProviderClient clientChunkProvider;

   public ChunkProviderClient getChunkProvider() {
      return (ChunkProviderClient)super.func_72863_F();
   }

   protected AwesomeWorld(WorldInfo worldInfo) {
      super(new SaveHandlerMP(), worldInfo, DimensionManager.createProviderFor(0), Wrapper.mc.field_71424_I, true);
      this.func_72912_H().func_176144_a(EnumDifficulty.PEACEFUL);
      this.field_73011_w.func_76558_a(this);
      this.func_175652_B(new BlockPos(8, 64, 8));
      this.field_73020_y = this.func_72970_h();
      this.field_72988_C = new SaveDataMemoryStorage();
      this.func_72966_v();
      this.func_72947_a();
      this.initCapabilities();
   }

   protected IChunkProvider func_72970_h() {
      this.clientChunkProvider = new ChunkProviderClient(this);
      return this.clientChunkProvider;
   }

   protected boolean func_175680_a(int x, int z, boolean allowEmpty) {
      return allowEmpty || !this.getChunkProvider().func_186025_d(x, z).func_76621_g();
   }
}
