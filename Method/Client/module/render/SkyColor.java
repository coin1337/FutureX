package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.ColorUtils;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;

public class SkyColor extends Module {
   Setting Color;

   public SkyColor() {
      super("FogColor", 0, Category.RENDER, "FogColor");
      this.Color = Main.setmgr.add(new Setting("Color", this, 0.22D, 1.0D, 1.0D, 1.0D));
   }

   public void fogDensity(FogDensity event) {
      event.setDensity(0.0F);
      event.setCanceled(true);
   }

   public void fogColor(FogColors event) {
      mc.field_71460_t.field_175080_Q = ColorUtils.colorcalc(this.Color.getcolor(), 16) / 255.0F;
      mc.field_71460_t.field_175082_R = ColorUtils.colorcalc(this.Color.getcolor(), 8) / 255.0F;
      mc.field_71460_t.field_175081_S = ColorUtils.colorcalc(this.Color.getcolor(), 0) / 255.0F;
      event.setRed(ColorUtils.colorcalc(this.Color.getcolor(), 16));
      event.setGreen(ColorUtils.colorcalc(this.Color.getcolor(), 8));
      event.setBlue(ColorUtils.colorcalc(this.Color.getcolor(), 0));
   }
}
