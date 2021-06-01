package Method.Client.utils.proxy.renderers;

import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderArmorStand extends RenderArmorStand {
   public ModRenderArmorStand(RenderManager manager) {
      super(manager);
      ModLayerBipedArmor layerbipedarmor = new ModLayerBipedArmor(this) {
         protected void initArmor() {
            this.modelLeggings = new ModelArmorStandArmor(0.5F);
            this.modelArmor = new ModelArmorStandArmor(1.0F);
         }
      };
      this.func_177094_a(layerbipedarmor);
   }
}
