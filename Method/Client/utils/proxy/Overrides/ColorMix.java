package Method.Client.utils.proxy.Overrides;

import Method.Client.module.render.ArmorRender;
import Method.Client.utils.proxy.renderers.ModRenderArmorStand;
import Method.Client.utils.proxy.renderers.ModRenderBoat;
import Method.Client.utils.proxy.renderers.ModRenderGiantZombie;
import Method.Client.utils.proxy.renderers.ModRenderHusk;
import Method.Client.utils.proxy.renderers.ModRenderItem;
import Method.Client.utils.proxy.renderers.ModRenderPigZombie;
import Method.Client.utils.proxy.renderers.ModRenderPlayer;
import Method.Client.utils.proxy.renderers.ModRenderSkeleton;
import Method.Client.utils.proxy.renderers.ModRenderStray;
import Method.Client.utils.proxy.renderers.ModRenderWitherSkeleton;
import Method.Client.utils.proxy.renderers.ModRenderZombie;
import Method.Client.utils.proxy.renderers.ModRenderZombieVillager;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderPotion;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;

public class ColorMix {
   public static ModRenderItem modRenderItem;

   public static void replaceRenderers() {
      Minecraft mc = Minecraft.func_71410_x();

      try {
         modRenderItem = new ModRenderItem(mc.field_175621_X, mc.field_175617_aL);
         mc.field_175621_X = modRenderItem;
         mc.field_175620_Y.field_178112_h = modRenderItem;
         mc.field_175616_W.field_178637_m = new ModRenderPlayer(mc.field_175616_W);
         mc.field_175616_W.field_178636_l.put("default", new ModRenderPlayer(mc.field_175616_W));
         mc.field_175616_W.field_178636_l.put("slim", new ModRenderPlayer(mc.field_175616_W, true));
      } catch (IllegalArgumentException var2) {
         var2.printStackTrace();
      }

      mc.field_175616_W.field_78729_o.put(EntityBoat.class, new ModRenderBoat(mc.field_175616_W));
      mc.field_175616_W.field_78729_o.put(EntityItemFrame.class, new RenderItemFrame(mc.field_175616_W, modRenderItem));
      mc.field_175616_W.field_78729_o.put(EntitySnowball.class, new RenderSnowball(mc.field_175616_W, Items.field_151126_ay, modRenderItem));
      mc.field_175616_W.field_78729_o.put(EntityEnderPearl.class, new RenderSnowball(mc.field_175616_W, Items.field_151079_bi, modRenderItem));
      mc.field_175616_W.field_78729_o.put(EntityEnderEye.class, new RenderSnowball(mc.field_175616_W, Items.field_151061_bv, modRenderItem));
      mc.field_175616_W.field_78729_o.put(EntityEgg.class, new RenderSnowball(mc.field_175616_W, Items.field_151110_aK, modRenderItem));
      mc.field_175616_W.field_78729_o.put(EntityPotion.class, new RenderPotion(mc.field_175616_W, modRenderItem));
      mc.field_175616_W.field_78729_o.put(EntityExpBottle.class, new RenderSnowball(mc.field_175616_W, Items.field_151062_by, modRenderItem));
      mc.field_175616_W.field_78729_o.put(EntityFireworkRocket.class, new RenderSnowball(mc.field_175616_W, Items.field_151152_bP, modRenderItem));
      mc.field_175616_W.field_78729_o.put(EntityItem.class, new RenderEntityItem(mc.field_175616_W, modRenderItem));
      mc.field_175616_W.field_78729_o.put(EntitySkeleton.class, new ModRenderSkeleton(mc.field_175616_W));
      mc.field_175616_W.field_78729_o.put(EntityWitherSkeleton.class, new ModRenderWitherSkeleton(mc.field_175616_W));
      mc.field_175616_W.field_78729_o.put(EntityStray.class, new ModRenderStray(mc.field_175616_W));
      mc.field_175616_W.field_78729_o.put(EntityZombie.class, new ModRenderZombie(mc.field_175616_W));
      mc.field_175616_W.field_78729_o.put(EntityHusk.class, new ModRenderHusk(mc.field_175616_W));
      mc.field_175616_W.field_78729_o.put(EntityZombieVillager.class, new ModRenderZombieVillager(mc.field_175616_W));
      mc.field_175616_W.field_78729_o.put(EntityGiantZombie.class, new ModRenderGiantZombie(mc.field_175616_W, 6.0F));
      mc.field_175616_W.field_78729_o.put(EntityPigZombie.class, new ModRenderPigZombie(mc.field_175616_W));
      mc.field_175616_W.field_78729_o.put(EntityArmorStand.class, new ModRenderArmorStand(mc.field_175616_W));
      ((IReloadableResourceManager)((IReloadableResourceManager)mc.func_110442_L())).func_110542_a(modRenderItem);
   }

   public static int getColorForEnchantment(Map<Enchantment, Integer> enchMap) {
      if (ArmorRender.CustomColor.getValBoolean()) {
         return ArmorRender.Color.getcolor();
      } else if (!ArmorRender.enableColoredGlint.getValBoolean()) {
         return -8372020;
      } else {
         int alpha = 1711276032;
         if (enchMap.containsKey(Enchantments.field_180312_n)) {
            return alpha | ArmorRender.BANE_OF_ARTHROPODS;
         } else if (enchMap.containsKey(Enchantments.field_77334_n)) {
            return alpha | ArmorRender.FIRE_ASPECT;
         } else if (enchMap.containsKey(Enchantments.field_180313_o)) {
            return alpha | ArmorRender.KNOCKBACK;
         } else if (enchMap.containsKey(Enchantments.field_185304_p)) {
            return alpha | ArmorRender.LOOTING;
         } else if (enchMap.containsKey(Enchantments.field_185302_k)) {
            return alpha | ArmorRender.SHARPNESS;
         } else if (enchMap.containsKey(Enchantments.field_185303_l)) {
            return alpha | ArmorRender.SMITE;
         } else if (enchMap.containsKey(Enchantments.field_191530_r)) {
            return alpha | ArmorRender.SWEEPING;
         } else if (enchMap.containsKey(Enchantments.field_185307_s)) {
            return alpha | ArmorRender.UNBREAKING;
         } else if (enchMap.containsKey(Enchantments.field_185311_w)) {
            return alpha | ArmorRender.FLAME;
         } else if (enchMap.containsKey(Enchantments.field_185312_x)) {
            return alpha | ArmorRender.INFINITY;
         } else if (enchMap.containsKey(Enchantments.field_185309_u)) {
            return alpha | ArmorRender.POWER;
         } else if (enchMap.containsKey(Enchantments.field_185310_v)) {
            return alpha | ArmorRender.PUNCH;
         } else if (enchMap.containsKey(Enchantments.field_185305_q)) {
            return alpha | ArmorRender.EFFICIENCY;
         } else if (enchMap.containsKey(Enchantments.field_185308_t)) {
            return alpha | ArmorRender.FORTUNE;
         } else if (enchMap.containsKey(Enchantments.field_185306_r)) {
            return alpha | ArmorRender.SILK_TOUCH;
         } else if (enchMap.containsKey(Enchantments.field_151370_z)) {
            return alpha | ArmorRender.LUCK_OF_THE_SEA;
         } else if (enchMap.containsKey(Enchantments.field_151369_A)) {
            return alpha | ArmorRender.LURE;
         } else if (enchMap.containsKey(Enchantments.field_185299_g)) {
            return alpha | ArmorRender.AQUA_AFFINITY;
         } else if (enchMap.containsKey(Enchantments.field_185297_d)) {
            return alpha | ArmorRender.BLAST_PROTECTION;
         } else if (enchMap.containsKey(Enchantments.field_185300_i)) {
            return alpha | ArmorRender.DEPTH_STRIDER;
         } else if (enchMap.containsKey(Enchantments.field_180309_e)) {
            return alpha | ArmorRender.FEATHER_FALLING;
         } else if (enchMap.containsKey(Enchantments.field_77329_d)) {
            return alpha | ArmorRender.FIRE_PROTECTION;
         } else if (enchMap.containsKey(Enchantments.field_185301_j)) {
            return alpha | ArmorRender.FROST_WALKER;
         } else if (enchMap.containsKey(Enchantments.field_185296_A)) {
            return alpha | ArmorRender.MENDING;
         } else if (enchMap.containsKey(Enchantments.field_180308_g)) {
            return alpha | ArmorRender.PROJECTILE_PROTECTION;
         } else if (enchMap.containsKey(Enchantments.field_180310_c)) {
            return alpha | ArmorRender.PROTECTION;
         } else if (enchMap.containsKey(Enchantments.field_185298_f)) {
            return alpha | ArmorRender.RESPIRATION;
         } else if (enchMap.containsKey(Enchantments.field_92091_k)) {
            return alpha | ArmorRender.THORNS;
         } else if (enchMap.containsKey(Enchantments.field_190940_C)) {
            return alpha | ArmorRender.VANISHING_CURSE;
         } else {
            return enchMap.containsKey(Enchantments.field_190941_k) ? alpha | ArmorRender.BINDING_CURSE : -8372020;
         }
      }
   }

   public static float alphaFromColor() {
      return 0.32F;
   }

   public static float redFromColor(int parColor) {
      return (float)(parColor >> 16 & 255) / 255.0F;
   }

   public static float greenFromColor(int parColor) {
      return (float)(parColor >> 8 & 255) / 255.0F;
   }

   public static float blueFromColor(int parColor) {
      return (float)(parColor & 255) / 255.0F;
   }
}
