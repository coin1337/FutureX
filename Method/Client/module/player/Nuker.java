package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.BlockUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.RenderUtils;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Nuker extends Module {
   public Setting mode;
   public Setting distance;
   public Setting Drawbox;
   public Setting StopOnKick;
   public Setting allcolor;
   public Setting idcolor;
   Setting Drawmode;
   Setting LineWidth;
   public final ArrayDeque<Set<BlockPos>> prevBlocks;
   public BlockPos currentBlock;
   public float progress;
   public float prevProgress;
   public int id;

   public Nuker() {
      super("Nuker", 0, Category.PLAYER, "Nuker");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "All", new String[]{"ID", "All"}));
      this.distance = Main.setmgr.add(new Setting("Distance", this, 6.0D, 0.1D, 6.0D, false));
      this.Drawbox = Main.setmgr.add(new Setting("Draw box", this, true));
      this.StopOnKick = Main.setmgr.add(new Setting("StopOnKick", this, true));
      this.allcolor = Main.setmgr.add(new Setting("allcolor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      this.idcolor = Main.setmgr.add(new Setting("idcolor", this, 0.22D, 1.0D, 1.0D, 1.0D));
      this.Drawmode = Main.setmgr.add(new Setting("Hole Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.prevBlocks = new ArrayDeque();
   }

   public void onDisable() {
      if (this.currentBlock != null) {
         mc.field_71442_b.field_78778_j = true;
         Wrapper.INSTANCE.controller().func_78767_c();
         this.currentBlock = null;
      }

      this.prevBlocks.clear();
      this.id = 0;
      super.onDisable();
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71441_e == null && this.StopOnKick.getValBoolean() && this.isToggled()) {
         this.toggle();
      }

      this.currentBlock = null;
      Vec3d eyesPos = Utils.getEyesPos().func_178786_a(0.5D, 0.5D, 0.5D);
      BlockPos eyesBlock = new BlockPos(Utils.getEyesPos());
      double rangeSq = Math.pow(this.distance.getValDouble(), 2.0D);
      int blockRange = (int)Math.ceil(this.distance.getValDouble());
      Stream<BlockPos> stream = StreamSupport.stream(BlockPos.func_177980_a(eyesBlock.func_177982_a(blockRange, blockRange, blockRange), eyesBlock.func_177982_a(-blockRange, -blockRange, -blockRange)).spliterator(), true);
      stream = stream.filter((posx) -> {
         return eyesPos.func_72436_e(new Vec3d(posx)) <= rangeSq;
      }).filter(BlockUtils::canBeClicked).sorted(Comparator.comparingDouble((posx) -> {
         return eyesPos.func_72436_e(new Vec3d(posx));
      }));
      if (this.mode.getValString().equalsIgnoreCase("id")) {
         stream = stream.filter((posx) -> {
            return Block.func_149682_b(BlockUtils.getBlock(posx)) == this.id;
         });
      }

      List<BlockPos> blocks = (List)stream.collect(Collectors.toList());
      if (mc.field_71439_g.field_71075_bZ.field_75098_d) {
         Stream<BlockPos> stream2 = blocks.parallelStream();

         Set set;
         for(Iterator var13 = this.prevBlocks.iterator(); var13.hasNext(); stream2 = stream2.filter((posx) -> {
            return !set.contains(posx);
         })) {
            set = (Set)var13.next();
         }

         List<BlockPos> blocks2 = (List)stream2.collect(Collectors.toList());
         this.prevBlocks.addLast(new HashSet(blocks2));

         while(this.prevBlocks.size() > 5) {
            this.prevBlocks.removeFirst();
         }

         if (!blocks2.isEmpty()) {
            this.currentBlock = (BlockPos)blocks2.get(0);
         }

         Wrapper.INSTANCE.controller().func_78767_c();
         this.progress = 1.0F;
         this.prevProgress = 1.0F;
         BlockUtils.breakBlocksPacketSpam(blocks2);
      } else {
         Iterator var9 = blocks.iterator();

         while(var9.hasNext()) {
            BlockPos pos = (BlockPos)var9.next();
            if (BlockUtils.breakBlockSimple(pos)) {
               this.currentBlock = pos;
               break;
            }
         }

         if (this.currentBlock == null) {
            Wrapper.INSTANCE.controller().func_78767_c();
         }

         if (this.currentBlock != null && BlockUtils.getHardness(this.currentBlock) < 1.0F) {
            this.prevProgress = this.progress;
         }

         this.progress = mc.field_71442_b.field_78770_f;
         if (this.progress < this.prevProgress) {
            this.prevProgress = this.progress;
         } else {
            this.progress = 1.0F;
            this.prevProgress = 1.0F;
         }

         super.onClientTick(event);
      }
   }

   public void onLeftClickBlock(LeftClickBlock event) {
      if (this.mode.getValString().equalsIgnoreCase("id") && mc.field_71441_e.field_72995_K) {
         IBlockState blockState = BlockUtils.getState(event.getPos());
         this.id = Block.func_149682_b(blockState.func_177230_c());
      }

      super.onLeftClickBlock(event);
   }

   public void onWorldUnload(Unload event) {
      if (this.StopOnKick.getValBoolean()) {
         this.toggle();
      }

   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (this.currentBlock != null) {
         if (this.Drawbox.getValBoolean()) {
            if (this.mode.getValString().equalsIgnoreCase("all")) {
               RenderUtils.RenderBlock(this.Drawmode.getValString(), RenderUtils.Standardbb(this.currentBlock), this.allcolor.getcolor(), this.LineWidth.getValDouble());
            } else if (this.mode.getValString().equalsIgnoreCase("id")) {
               RenderUtils.RenderBlock(this.Drawmode.getValString(), RenderUtils.Standardbb(this.currentBlock), this.idcolor.getcolor(), this.LineWidth.getValDouble());
            }
         }

         super.onRenderWorldLast(event);
      }
   }
}
