package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;

public class BossStack extends Module {
   Setting mode;
   Setting scale;
   private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");

   public BossStack() {
      super("BossStack", 0, Category.RENDER, "BossStack");
      this.mode = Main.setmgr.add(new Setting("Stack mode", this, "Stack", new String[]{"Stack", "Minimize"}));
      this.scale = Main.setmgr.add(new Setting("Scale", this, 0.5D, 0.5D, 4.0D, false));
   }

   public void RenderGameOverLayPost(Post event) {
      if (event.getType() == ElementType.BOSSINFO) {
         Map map;
         int i;
         String text;
         if (this.mode.getValString().equalsIgnoreCase("Minimize")) {
            map = mc.field_71456_v.func_184046_j().field_184060_g;
            if (map == null) {
               return;
            }

            ScaledResolution scaledresolution = new ScaledResolution(mc);
            int i = scaledresolution.func_78326_a();
            i = 12;

            BossInfoClient info;
            for(Iterator var6 = map.entrySet().iterator(); var6.hasNext(); i = this.Collapse(event, i, i, info, text)) {
               Entry<UUID, BossInfoClient> entry = (Entry)var6.next();
               info = (BossInfoClient)entry.getValue();
               text = info.func_186744_e().func_150254_d();
            }
         } else if (this.mode.getValString().equalsIgnoreCase("Stack")) {
            map = mc.field_71456_v.func_184046_j().field_184060_g;
            HashMap<String, Pair<BossInfoClient, Integer>> to = new HashMap();

            String s;
            Pair p;
            for(Iterator var13 = map.entrySet().iterator(); var13.hasNext(); to.put(s, p)) {
               Entry<UUID, BossInfoClient> entry = (Entry)var13.next();
               s = ((BossInfoClient)entry.getValue()).func_186744_e().func_150254_d();
               if (to.containsKey(s)) {
                  p = (Pair)to.get(s);
                  p = new Pair(p.getKey(), (Integer)p.getValue() + 1);
               } else {
                  p = new Pair(entry.getValue(), 1);
               }
            }

            ScaledResolution scaledresolution = new ScaledResolution(mc);
            i = scaledresolution.func_78326_a();
            int j = 12;

            BossInfoClient info;
            for(Iterator var20 = to.entrySet().iterator(); var20.hasNext(); j = this.Collapse(event, i, j, info, text)) {
               Entry<String, Pair<BossInfoClient, Integer>> entry = (Entry)var20.next();
               text = (String)entry.getKey();
               info = (BossInfoClient)((Pair)entry.getValue()).getKey();
               int a = (Integer)((Pair)entry.getValue()).getValue();
               text = text + " x" + a;
            }
         }

      }
   }

   public void onRenderPre(Pre event) {
      if (event.getType() == ElementType.BOSSINFO) {
         event.setCanceled(true);
      }

   }

   private int Collapse(Post event, int i, int j, BossInfoClient info, String text) {
      int k = (int)((double)i / this.scale.getValDouble() / 2.0D - 91.0D);
      GlStateManager.func_179139_a(this.scale.getValDouble(), this.scale.getValDouble(), 1.0D);
      if (!event.isCanceled()) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         mc.func_110434_K().func_110577_a(GUI_BARS_TEXTURES);
         mc.field_71456_v.func_184046_j().func_184052_a(k, j, info);
         mc.field_71466_p.func_175063_a(text, (float)((double)i / this.scale.getValDouble() / 2.0D - (double)(mc.field_71466_p.func_78256_a(text) / 2)), (float)(j - 9), 16777215);
      }

      GlStateManager.func_179139_a(1.0D / this.scale.getValDouble(), 1.0D / this.scale.getValDouble(), 1.0D);
      j += 10 + mc.field_71466_p.field_78288_b;
      return j;
   }
}
