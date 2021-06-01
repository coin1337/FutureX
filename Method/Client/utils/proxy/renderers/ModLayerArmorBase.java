package Method.Client.utils.proxy.renderers;

import Method.Client.module.render.ArmorRender;
import Method.Client.utils.proxy.Overrides.ColorMix;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ModLayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {
   protected static final ResourceLocation RES_ITEM_GLINT_RUNE = new ResourceLocation("futurex", "enchanted_item_glint_rune.png");
   protected static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("futurex", "enchanted_item_glint.png");
   protected T modelLeggings;
   protected T modelArmor;
   private final RenderLivingBase<?> renderer;
   private boolean skipRenderGlint;
   private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

   public ModLayerArmorBase(RenderLivingBase<?> rendererIn) {
      this.renderer = rendererIn;
      this.initArmor();
   }

   public void func_177141_a(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
      this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
      this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
      this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
   }

   public boolean func_177142_b() {
      return false;
   }

   private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn) {
      ItemStack itemstack = entityLivingBaseIn.func_184582_a(slotIn);
      if (ArmorRender.RenderArmor.getValBoolean() && itemstack.func_77973_b() instanceof ItemArmor) {
         ItemArmor itemarmor = (ItemArmor)itemstack.func_77973_b();
         if (itemarmor.func_185083_B_() == slotIn) {
            T model = this.getModelFromSlot(slotIn);
            model = this.getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, model);
            model.func_178686_a(this.renderer.func_177087_b());
            model.func_78086_a(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.setModelSlotVisible(model, slotIn);
            this.renderer.func_110776_a(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, (String)null));
            float alpha = 1.0F;
            float colorR = 1.0F;
            float colorG = 1.0F;
            float colorB = 1.0F;
            if (itemarmor.hasOverlay(itemstack)) {
               int itemColor = itemarmor.func_82814_b(itemstack);
               float itemRed = (float)(itemColor >> 16 & 255) / 255.0F;
               float itemGreen = (float)(itemColor >> 8 & 255) / 255.0F;
               float itemBlue = (float)(itemColor & 255) / 255.0F;
               GlStateManager.func_179131_c(colorR * itemRed, colorG * itemGreen, colorB * itemBlue, alpha);
               model.func_78088_a(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
               this.renderer.func_110776_a(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
            }

            GlStateManager.func_179131_c(colorR, colorG, colorB, alpha);
            model.func_78088_a(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (!this.skipRenderGlint && itemstack.func_77962_s()) {
               renderEnchantedGlint(this.renderer, entityLivingBaseIn, model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, ColorMix.getColorForEnchantment(EnchantmentHelper.func_82781_a(itemstack)));
            }
         }
      }

   }

   public T getModelFromSlot(EntityEquipmentSlot slotIn) {
      return this.isLegSlot(slotIn) ? this.modelLeggings : this.modelArmor;
   }

   private boolean isLegSlot(EntityEquipmentSlot slotIn) {
      return slotIn == EntityEquipmentSlot.LEGS;
   }

   public static void renderEnchantedGlint(RenderLivingBase<?> parRenderLivingBase, EntityLivingBase parEntityLivingBase, ModelBase model, float parLimbSwing, float parLimbSwingAmount, float parPartialTicks, float parAgeInTicks, float parHeadYaw, float parHeadPitch, float parScale, int parColor) {
      float f = (float)parEntityLivingBase.field_70173_aa + parPartialTicks;
      if (ArmorRender.useRuneTexture.getValBoolean()) {
         parRenderLivingBase.func_110776_a(RES_ITEM_GLINT_RUNE);
      } else {
         parRenderLivingBase.func_110776_a(RES_ITEM_GLINT);
      }

      Minecraft.func_71410_x().field_71460_t.func_191514_d(true);
      GlStateManager.func_179147_l();
      GlStateManager.func_179143_c(514);
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179131_c(ColorMix.redFromColor(parColor), ColorMix.greenFromColor(parColor), ColorMix.blueFromColor(parColor), ColorMix.alphaFromColor());

      for(int i = 0; i < 2; ++i) {
         GlStateManager.func_179140_f();
         GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.DST_ALPHA);
         GlStateManager.func_179131_c(ColorMix.redFromColor(parColor), ColorMix.greenFromColor(parColor), ColorMix.blueFromColor(parColor), ColorMix.alphaFromColor());
         GlStateManager.func_179128_n(5890);
         GlStateManager.func_179096_D();
         GlStateManager.func_179152_a(3.0F, 3.0F, 3.0F);
         GlStateManager.func_179114_b(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.func_179109_b(0.0F, f * (0.001F + (float)i * 0.003F) * 5.0F, 0.0F);
         GlStateManager.func_179128_n(5888);
         model.func_78088_a(parEntityLivingBase, parLimbSwing, parLimbSwingAmount, parAgeInTicks, parHeadYaw, parHeadPitch, parScale);
         GlStateManager.func_187401_a(SourceFactor.ONE, DestFactor.ZERO);
      }

      GlStateManager.func_179128_n(5890);
      GlStateManager.func_179096_D();
      GlStateManager.func_179128_n(5888);
      GlStateManager.func_179145_e();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179143_c(515);
      GlStateManager.func_179084_k();
      Minecraft.func_71410_x().field_71460_t.func_191514_d(false);
   }

   protected abstract void initArmor();

   protected abstract void setModelSlotVisible(T var1, EntityEquipmentSlot var2);

   protected T getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, T model) {
      return model;
   }

   public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type) {
      ItemArmor item = (ItemArmor)stack.func_77973_b();
      String texture = item.func_82812_d().func_179242_c();
      String domain = "minecraft";
      int idx = texture.indexOf(58);
      if (idx != -1) {
         domain = texture.substring(0, idx);
         texture = texture.substring(idx + 1);
      }

      String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, this.isLegSlot(slot) ? 2 : 1, type == null ? "" : String.format("_%s", type));
      s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
      ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get(s1);
      if (resourcelocation == null) {
         resourcelocation = new ResourceLocation(s1);
         ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
      }

      return resourcelocation;
   }
}
