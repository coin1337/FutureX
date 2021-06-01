package Method.Client.utils.proxy.renderers;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class ModRenderStray extends ModRenderSkeleton {
   private static final ResourceLocation STRAY_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray.png");

   public ModRenderStray(RenderManager manager) {
      super(manager);
      this.func_177094_a(new LayerStrayClothing(this));
   }

   protected ResourceLocation func_110775_a(AbstractSkeleton entity) {
      return STRAY_SKELETON_TEXTURES;
   }
}
