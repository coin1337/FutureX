package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.RenderUtils;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ItemESP extends Module {
   Setting OverlayColor;
   Setting Mode;
   Setting Glow;
   Setting LifeSpan;
   Setting Nametag;
   Setting LineWidth;
   Setting Scale;

   public ItemESP() {
      super("ItemESP", 0, Category.RENDER, "Droped item glow");
      this.OverlayColor = Main.setmgr.add(new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      this.Mode = Main.setmgr.add(new Setting("Drop Mode", this, "Outline", this.BlockEspOptions()));
      this.Glow = Main.setmgr.add(new Setting("Glow", this, false));
      this.LifeSpan = Main.setmgr.add(new Setting("Thrower", this, true));
      this.Nametag = Main.setmgr.add(new Setting("Nametag", this, true));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.Scale = Main.setmgr.add(new Setting("Scale", this, 0.1D, 0.0D, 1.0D, false));
   }

   public void onDisable() {
      Iterator var1 = mc.field_71441_e.field_72996_f.iterator();

      while(var1.hasNext()) {
         Object object = var1.next();
         if (object instanceof EntityItem) {
            Entity item = (Entity)object;
            item.func_184195_f(false);
         }
      }

   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

      while(var2.hasNext()) {
         Object object = var2.next();
         if (object instanceof EntityItem) {
            EntityItem item = (EntityItem)object;
            double S = this.Scale.getValDouble();
            RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Boundingbb(item, -S, 0.2D - S, -S, S, 0.2D + S, S), this.OverlayColor.getcolor(), this.LineWidth.getValDouble());
            if (this.Nametag.getValBoolean()) {
               RenderUtils.SimpleNametag(item.func_174791_d(), item.func_92059_d().func_82833_r() + " x" + item.func_92059_d().func_190916_E() + (this.LifeSpan.getValBoolean() ? " " + (item.lifespan - item.field_70292_b) / 20 + " S" : ""));
            }

            item.func_184195_f(this.Glow.getValBoolean());
         }
      }

      super.onRenderWorldLast(event);
   }
}
