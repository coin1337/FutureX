package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class BreakEsp extends Module {
   Setting ignoreSelf;
   Setting onlyObsi;
   Setting fade;
   Setting Mode;
   Setting LineWidth;
   Setting OverlayColor;
   Setting Distance;

   public BreakEsp() {
      super("BreakEsp", 0, Category.RENDER, "BreakEsp");
      this.ignoreSelf = Main.setmgr.add(new Setting("ignoreSelf", this, false));
      this.onlyObsi = Main.setmgr.add(new Setting("onlyObsi", this, false));
      this.fade = Main.setmgr.add(new Setting("fade", this, true));
      this.Mode = Main.setmgr.add(new Setting("Hole Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.OverlayColor = Main.setmgr.add(new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 0.56D));
      this.Distance = Main.setmgr.add(new Setting("Distance", this, 10.0D, 0.0D, 50.0D, false));
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      mc.field_71438_f.field_72738_E.forEach((var1x, destroyBlockProgress) -> {
         if (destroyBlockProgress != null && this.Distance.getValDouble() * 5.0D > mc.field_71439_g.func_174831_c(destroyBlockProgress.func_180246_b()) && (!this.ignoreSelf.getValBoolean() || mc.field_71441_e.func_73045_a(var1x) != mc.field_71439_g) && (!this.onlyObsi.getValBoolean() || mc.field_71441_e.func_180495_p(destroyBlockProgress.func_180246_b()).func_177230_c() == Blocks.field_150343_Z)) {
            AxisAlignedBB pos = RenderUtils.Standardbb(destroyBlockProgress.func_180246_b());
            if (this.fade.getValBoolean()) {
               pos = pos.func_186664_h((3.0D - (double)destroyBlockProgress.func_73106_e() / 2.6666666666666665D) / 9.0D);
            }

            RenderUtils.RenderBlock(this.Mode.getValString(), pos, this.OverlayColor.getcolor(), this.LineWidth.getValDouble());
         }

      });
      super.onRenderWorldLast(event);
   }
}
