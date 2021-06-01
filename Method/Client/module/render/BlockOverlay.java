package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class BlockOverlay extends Module {
   Setting OverlayColor;
   Setting Mode;
   Setting LineWidth;

   public BlockOverlay() {
      super("BlockOverlay", 0, Category.RENDER, "BlockOverlay");
      this.OverlayColor = Main.setmgr.add(new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 0.62D));
      this.Mode = Main.setmgr.add(new Setting("Hole Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (mc.field_71476_x != null) {
         if (Block.func_149682_b(mc.field_71441_e.func_180495_p(mc.field_71476_x.func_178782_a()).func_177230_c()) != 0) {
            if (mc.field_71476_x.field_72313_a == Type.BLOCK) {
               BlockPos blockPos = mc.field_71476_x.func_178782_a();
               RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Standardbb(blockPos), this.OverlayColor.getcolor(), this.LineWidth.getValDouble());
            }

            super.onRenderWorldLast(event);
         }
      }
   }
}
