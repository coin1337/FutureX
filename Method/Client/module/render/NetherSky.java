package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class NetherSky extends Module {
   Setting mode;
   Setting OverlayColor;
   private static NetherSky.ISpaceRenderer skyboxSpaceRenderer;
   private boolean wasChanged;

   public void setup() {
      skyboxSpaceRenderer = new NetherSky.SkyboxSpaceRenderer();
   }

   public NetherSky() {
      super("NetherSky", 0, Category.RENDER, "NetherSky");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Glint", new String[]{"Glint", "Method"}));
      this.OverlayColor = Main.setmgr.add(new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 0.62D));
   }

   public void onEnable() {
      this.wasChanged = false;
   }

   public void onDisable() {
      this.disableBackgroundRenderer(mc.field_71439_g.field_70170_p);
   }

   public void onClientTick(ClientTickEvent event) {
      if (!this.wasChanged) {
         this.enableBackgroundRenderer(mc.field_71439_g.field_70170_p);
         this.wasChanged = true;
      }

   }

   public void onWorldLoad(Load event) {
      this.wasChanged = false;
   }

   public void onWorldUnload(Unload event) {
      this.wasChanged = false;
   }

   private void enableBackgroundRenderer(World world) {
      if (world.field_73011_w.func_186058_p() == DimensionType.NETHER) {
         world.field_73011_w.setSkyRenderer(new IRenderHandler() {
            public void render(float partialTicks, WorldClient world, Minecraft mc) {
               NetherSky.skyboxSpaceRenderer.render(NetherSky.this.mode);
            }
         });
      }

   }

   private void disableBackgroundRenderer(World world) {
      if (world.field_73011_w.func_186058_p() == DimensionType.NETHER) {
         world.field_73011_w.setSkyRenderer(new IRenderHandler() {
            public void render(float partialTicks, WorldClient world, Minecraft mc) {
            }
         });
      }

   }

   public interface ISpaceRenderer {
      void render(Setting var1);
   }

   public class SkyboxSpaceRenderer implements NetherSky.ISpaceRenderer {
      public void render(Setting mode) {
         GlStateManager.func_179106_n();
         GlStateManager.func_179118_c();
         GlStateManager.func_179147_l();
         GlStateManager.func_187428_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
         RenderHelper.func_74518_a();
         GlStateManager.func_179132_a(false);
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder bufferbuilder = tessellator.func_178180_c();

         for(int i = 0; i < 6; ++i) {
            if (mode.getValString().equalsIgnoreCase("Glint")) {
               NetherSky.mc.func_110434_K().func_110577_a(new ResourceLocation("futurex", "N.png"));
            }

            if (mode.getValString().equalsIgnoreCase("Method")) {
               NetherSky.mc.func_110434_K().func_110577_a(new ResourceLocation("futurex", "method.png"));
            }

            GlStateManager.func_179094_E();
            if (i == 1) {
               GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (i == 2) {
               GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
            }

            if (i == 3) {
               GlStateManager.func_179114_b(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if (i == 4) {
               GlStateManager.func_179114_b(90.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            if (i == 5) {
               GlStateManager.func_179114_b(-90.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.func_179114_b(90.0F, 0.0F, 1.0F, 0.0F);
            }

            bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181709_i);
            double size = 100.0D;
            float a = ColorUtils.colorcalc(NetherSky.this.OverlayColor.getcolor(), 24);
            float r = ColorUtils.colorcalc(NetherSky.this.OverlayColor.getcolor(), 16);
            float g = ColorUtils.colorcalc(NetherSky.this.OverlayColor.getcolor(), 8);
            float b = ColorUtils.colorcalc(NetherSky.this.OverlayColor.getcolor(), 0);
            bufferbuilder.func_181662_b(-size, -size, -size).func_187315_a(0.0D, 0.0D).func_181666_a(r, g, b, a).func_181675_d();
            bufferbuilder.func_181662_b(-size, -size, size).func_187315_a(0.0D, 1.0D).func_181666_a(r, g, b, a).func_181675_d();
            bufferbuilder.func_181662_b(size, -size, size).func_187315_a(1.0D, 1.0D).func_181666_a(r, g, b, a).func_181675_d();
            bufferbuilder.func_181662_b(size, -size, -size).func_187315_a(1.0D, 0.0D).func_181666_a(r, g, b, a).func_181675_d();
            tessellator.func_78381_a();
            GlStateManager.func_179121_F();
         }

         GlStateManager.func_179132_a(true);
         GlStateManager.func_179098_w();
         GlStateManager.func_179141_d();
         GlStateManager.func_179141_d();
      }
   }
}
