package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ChunkBorder extends Module {
   Setting OverlayColor;
   Setting Mode;
   Setting LineWidth;
   Setting Height;
   Setting FollowPlayer;

   public ChunkBorder() {
      super("ChunkBorder", 0, Category.RENDER, "ChunkBorder");
      this.OverlayColor = Main.setmgr.add(new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 0.52D));
      this.Mode = Main.setmgr.add(new Setting("Chunk Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.Height = Main.setmgr.add(new Setting("Height", this, 0.0D, 0.0D, 255.0D, true));
      this.FollowPlayer = Main.setmgr.add(new Setting("FollowPlayer", this, true));
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      Chunk chunk = mc.field_71441_e.func_175726_f(mc.field_71439_g.func_180425_c());
      double renderPosX = (double)(chunk.field_76635_g * 16) - mc.func_175598_ae().field_78730_l;
      double renderPosY = -mc.func_175598_ae().field_78731_m;
      double renderPosZ = (double)(chunk.field_76647_h * 16) - mc.func_175598_ae().field_78728_n;
      AxisAlignedBB bb1;
      if (this.FollowPlayer.getValBoolean()) {
         bb1 = new AxisAlignedBB(renderPosX, renderPosY + mc.field_71439_g.field_70163_u, renderPosZ, renderPosX + 16.0D, renderPosY + 1.0D + mc.field_71439_g.field_70163_u, renderPosZ + 16.0D);
      } else {
         bb1 = new AxisAlignedBB(renderPosX, renderPosY + this.Height.getValDouble(), renderPosZ, renderPosX + 16.0D, renderPosY + 1.0D + this.Height.getValDouble(), renderPosZ + 16.0D);
      }

      RenderUtils.RenderBlock(this.Mode.getValString(), bb1, this.OverlayColor.getcolor(), this.LineWidth.getValDouble());
   }
}
