package Method.Client.utils.proxy.renderers;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderSkeleton extends RenderSkeleton {
   public ModRenderSkeleton(RenderManager renderManagerIn) {
      super(renderManagerIn);
      this.field_177097_h.remove(4);
      this.func_177094_a(new ModLayerBipedArmor(this) {
         protected void initArmor() {
            this.modelLeggings = new ModelSkeleton(0.5F, true);
            this.modelArmor = new ModelSkeleton(1.0F, true);
         }
      });
   }
}
