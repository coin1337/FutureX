package Method.Client.utils.proxy.renderers;

import Method.Client.module.render.ArmorRender;
import Method.Client.utils.proxy.ClientProxy;
import Method.Client.utils.proxy.Overrides.ColorMix;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderItem extends RenderItem {
   private static final ResourceLocation RES_ITEM_GLINT_RUNE = new ResourceLocation("futurex", "textures/misc/enchanted_item_glint_rune.png");
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("futurex", "textures/misc/enchanted_item_glint.png");
   private final RenderItem originalRenderItem;

   public ModRenderItem(RenderItem parRenderItem, ModelManager modelManager) {
      super(Minecraft.func_71410_x().func_110434_K(), modelManager, Minecraft.func_71410_x().getItemColors());
      this.originalRenderItem = parRenderItem;
   }

   public void func_180454_a(ItemStack stack, IBakedModel model) {
      if (!stack.func_190926_b()) {
         if (!model.func_188618_c() && ClientProxy.Gl.isToggled()) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(-0.5F, -0.5F, -0.5F);
            this.renderModel(model, stack);
            if (stack.func_77962_s()) {
               this.renderEffect(model, ColorMix.getColorForEnchantment(EnchantmentHelper.func_82781_a(stack)));
            }

            GlStateManager.func_179121_F();
         } else {
            this.originalRenderItem.func_180454_a(stack, model);
         }
      }

   }

   private void renderEffect(IBakedModel model, int color) {
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179143_c(514);
      GlStateManager.func_179140_f();
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE);
      if (ArmorRender.useRuneTexture.getValBoolean()) {
         Minecraft.func_71410_x().func_110434_K().func_110577_a(RES_ITEM_GLINT_RUNE);
      } else {
         Minecraft.func_71410_x().func_110434_K().func_110577_a(RES_ITEM_GLINT);
      }

      GlStateManager.func_179128_n(5890);
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(16.0F, 16.0F, 16.0F);
      float f = (float)(Minecraft.func_71386_F() % 3000L) / 3000.0F / 8.0F;
      GlStateManager.func_179109_b(f, 0.0F, 0.0F);
      GlStateManager.func_179114_b(-50.0F, 0.0F, 0.0F, 1.0F);
      this.renderModel(model, color);
      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(16.0F, 16.0F, 16.0F);
      float f1 = (float)(Minecraft.func_71386_F() % 4873L) / 4873.0F / 8.0F;
      GlStateManager.func_179109_b(-f1, 0.0F, 0.0F);
      GlStateManager.func_179114_b(10.0F, 0.0F, 0.0F, 1.0F);
      this.renderModel(model, color);
      GlStateManager.func_179121_F();
      GlStateManager.func_179128_n(5888);
      GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.func_179145_e();
      GlStateManager.func_179143_c(515);
      GlStateManager.func_179132_a(true);
      Minecraft.func_71410_x().func_110434_K().func_110577_a(TextureMap.field_110575_b);
   }

   private void renderModel(IBakedModel model, ItemStack stack) {
      this.renderModel(model, -1, stack);
   }

   private void renderModel(IBakedModel model, int color) {
      this.renderModel(model, color, ItemStack.field_190927_a);
   }

   private void renderModel(IBakedModel model, int color, ItemStack stack) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_176599_b);
      EnumFacing[] var6 = EnumFacing.values();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         EnumFacing enumfacing = var6[var8];
         this.func_191970_a(bufferbuilder, model.func_188616_a((IBlockState)null, enumfacing, 0L), color, stack);
      }

      this.func_191970_a(bufferbuilder, model.func_188616_a((IBlockState)null, (EnumFacing)null, 0L), color, stack);
      tessellator.func_78381_a();
   }

   public void func_191970_a(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
      boolean flag = color == -1 && !stack.func_190926_b();
      int i = 0;

      for(int j = quads.size(); i < j; ++i) {
         BakedQuad bakedquad = (BakedQuad)quads.get(i);
         int k = color;
         if (flag && bakedquad.func_178212_b()) {
            k = Minecraft.func_71410_x().getItemColors().func_186728_a(stack, bakedquad.func_178211_c());
            if (EntityRenderer.field_78517_a) {
               k = TextureUtil.func_177054_c(k);
            }

            k |= -16777216;
         }

         LightUtil.renderQuadColor(renderer, bakedquad, k);
      }

   }

   public static ResourceLocation getResItemGlint() {
      return RES_ITEM_GLINT;
   }
}
