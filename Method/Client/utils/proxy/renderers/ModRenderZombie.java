package Method.Client.utils.proxy.renderers;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderZombie extends RenderZombie {
   public ModRenderZombie(RenderManager renderManagerIn) {
      super(renderManagerIn);
      this.field_177097_h.remove(3);
      ModLayerBipedArmor layerbipedarmor = new ModLayerBipedArmor(this) {
         protected void initArmor() {
            this.modelLeggings = new ModelZombie(0.5F, true);
            this.modelArmor = new ModelZombie(1.0F, true);
         }
      };
      this.func_177094_a(layerbipedarmor);
   }
}
