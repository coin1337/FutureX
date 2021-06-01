package Method.Client.utils.proxy.renderers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class ModRenderWitherSkeleton extends ModRenderSkeleton {
   private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

   public ModRenderWitherSkeleton(RenderManager manager) {
      super(manager);
   }

   protected ResourceLocation func_110775_a(AbstractSkeleton entity) {
      return WITHER_SKELETON_TEXTURES;
   }

   protected void preRenderCallback(AbstractSkeleton entitylivingbaseIn, float partialTickTime) {
      GlStateManager.func_179152_a(1.2F, 1.2F, 1.2F);
   }
}
