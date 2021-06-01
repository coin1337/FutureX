package Method.Client.utils.proxy.renderers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class ModRenderHusk extends ModRenderZombie {
   private static final ResourceLocation HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");

   public ModRenderHusk(RenderManager p_i47204_1_) {
      super(p_i47204_1_);
   }

   protected void preRenderCallback(EntityZombie entitylivingbaseIn, float partialTickTime) {
      GlStateManager.func_179152_a(1.0625F, 1.0625F, 1.0625F);
      super.func_77041_b(entitylivingbaseIn, partialTickTime);
   }

   protected ResourceLocation func_110775_a(EntityZombie entity) {
      return HUSK_ZOMBIE_TEXTURES;
   }
}
