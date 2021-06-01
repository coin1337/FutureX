package Method.Client.utils.proxy.renderers;

import Method.Client.module.movement.BoatFly;
import Method.Client.utils.visual.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;

public class ModRenderBoat extends RenderBoat {
   public ModRenderBoat(RenderManager renderManager) {
      super(renderManager);
   }

   public void func_76986_a(EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks) {
      if (BoatFly.Boatblend.getValBoolean()) {
         GlStateManager.func_179147_l();
      }

      if (!BoatFly.BoatRender.getValString().equalsIgnoreCase("Vanish")) {
         if (BoatFly.BoatRender.getValString().equalsIgnoreCase("Rainbow")) {
            ColorUtils.glColor(ColorUtils.rainbow().getRGB());
         }

         super.func_76986_a(entity, x, y, z, entityYaw, partialTicks);
         if (BoatFly.Boatblend.getValBoolean()) {
            GlStateManager.func_179084_k();
         }

      }
   }
}
