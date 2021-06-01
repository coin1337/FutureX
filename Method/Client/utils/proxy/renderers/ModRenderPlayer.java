package Method.Client.utils.proxy.renderers;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderPlayer extends RenderPlayer {
   public ModRenderPlayer(RenderManager renderManager) {
      this(renderManager, false);
   }

   public ModRenderPlayer(RenderManager renderManager, boolean b) {
      super(renderManager, b);
      this.field_177097_h.remove(0);
      this.func_177094_a(new ModLayerBipedArmor(this));
   }
}
