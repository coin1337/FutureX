package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.combat.TotemPop;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class NameTags extends Module {
   public static boolean toggled = false;
   public Setting Scale;
   public Setting armor;
   public Setting Background;
   public Setting TextColor;
   public Setting ScaleMode;
   public Setting Gamemode;
   public Setting Player;
   public Setting Ping;
   public Setting Mob;
   public Setting Hostile;
   public Setting Totem;
   public Setting Healthmode;

   public NameTags() {
      super("NameTags", 0, Category.RENDER, "NameTags");
      this.Scale = Main.setmgr.add(new Setting("Scale", this, 2.0D, 0.0D, 4.0D, false));
      this.armor = Main.setmgr.add(new Setting("armor", this, true));
      this.Background = Main.setmgr.add(new Setting("Background", this, 0.0D, 1.0D, 0.01D, 0.22D));
      this.TextColor = Main.setmgr.add(new Setting("Name", this, 0.0D, 1.0D, 1.0D, 1.0D));
      this.ScaleMode = Main.setmgr.add(new Setting("Armor Mode", this, "H", new String[]{"H", "V"}));
      this.Gamemode = Main.setmgr.add(new Setting("Gamemode", this, true));
      this.Player = Main.setmgr.add(new Setting("Player", this, true));
      this.Ping = Main.setmgr.add(new Setting("Ping", this, true));
      this.Mob = Main.setmgr.add(new Setting("Mob", this, false));
      this.Hostile = Main.setmgr.add(new Setting("Hostile", this, false));
      this.Totem = Main.setmgr.add(new Setting("Totem Pops", this, false));
      this.Healthmode = Main.setmgr.add(new Setting("Health Mode", this, "Number", new String[]{"Number", "Bar"}));
   }

   public void onDisable() {
      toggled = false;
   }

   public void onEnable() {
      toggled = true;
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

      while(true) {
         Entity object;
         do {
            if (!var2.hasNext()) {
               return;
            }

            object = (Entity)var2.next();
         } while((!this.Player.getValBoolean() || !(object instanceof EntityPlayer)) && (!this.Mob.getValBoolean() || !(object instanceof IAnimals)) && (!this.Hostile.getValBoolean() || !(object instanceof IMob)));

         assert object instanceof EntityLivingBase;

         if (object != mc.field_71439_g) {
            this.Runnametag((EntityLivingBase)object);
         }
      }
   }

   private void Runnametag(EntityLivingBase e) {
      double scale = Math.max(this.Scale.getValDouble() * (double)(mc.field_71439_g.func_70032_d(e) / 20.0F), 2.0D);
      StringBuilder healthBuilder = new StringBuilder();

      for(int i = 0; (float)i < e.func_110143_aJ(); ++i) {
         healthBuilder.append(ChatFormatting.GREEN + "|");
      }

      StringBuilder health = new StringBuilder(healthBuilder.toString());

      int i;
      for(i = 0; (float)i < MathHelper.func_76131_a(e.func_110139_bj(), 0.0F, e.func_110138_aP() - e.func_110143_aJ()); ++i) {
         health.append(ChatFormatting.RED + "|");
      }

      for(i = 0; (float)i < e.func_110138_aP() - (e.func_110143_aJ() + e.func_110139_bj()); ++i) {
         health.append(ChatFormatting.YELLOW + "|");
      }

      if (e.func_110139_bj() - (e.func_110138_aP() - e.func_110143_aJ()) > 0.0F) {
         health.append(ChatFormatting.BLUE + " +").append((int)(e.func_110139_bj() - (e.func_110138_aP() - e.func_110143_aJ())));
      }

      String tag = "";
      if (this.Totem.getValBoolean() && e instanceof EntityPlayer && ModuleManager.getModuleByName("TotemPop").isToggled()) {
         tag = tag + " T:" + TotemPop.getpops(e) + " ";
      }

      if (this.Ping.getValBoolean() && e instanceof EntityPlayer) {
         try {
            tag = tag + " " + (int)MathHelper.func_76131_a((float)((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_175102_a(e.func_110124_au()).func_178853_c(), 1.0F, 300.0F) + " P ";
         } catch (NullPointerException var13) {
         }
      }

      if (this.Gamemode.getValBoolean() && e instanceof EntityPlayer) {
         EntityPlayer entityPlayer = (EntityPlayer)e;
         if (entityPlayer.func_184812_l_()) {
            tag = tag + "[C]";
         }

         if (entityPlayer.func_175149_v()) {
            tag = tag + " [I]";
         }

         if (!entityPlayer.func_175142_cm() && !entityPlayer.func_175149_v()) {
            tag = tag + " [A]";
         }

         if (!entityPlayer.func_184812_l_() && !entityPlayer.func_175149_v() && entityPlayer.func_175142_cm()) {
            tag = tag + " [S]";
         }
      }

      if (this.Healthmode.getValString().equalsIgnoreCase("Number")) {
         this.Processtext(e.func_70005_c_() + " [" + (int)(e.func_110143_aJ() + e.func_110139_bj()) + "/" + (int)e.func_110138_aP() + "]" + tag, mc.field_71466_p.func_78256_a(e.func_70005_c_() + tag + "[]") / 2, this.TextColor, e, (double)e.field_70131_O + 0.5D * scale, scale);
      } else if (this.Healthmode.getValString().equalsIgnoreCase("Bar")) {
         this.Processtext(e.func_70005_c_() + tag, mc.field_71466_p.func_78256_a(e.func_70005_c_() + tag) / 2, this.TextColor, e, (double)e.field_70131_O + 0.5D * scale, scale);
         this.Processtext(health.toString(), mc.field_71466_p.func_78256_a(health.toString()) / 2, this.TextColor, e, (double)e.field_70131_O + 0.75D * scale, scale);
      }

      if (this.armor.getValBoolean()) {
         double c = 0.0D;
         double higher = this.Healthmode.getValString().equalsIgnoreCase("Bar") ? 0.25D : 0.0D;
         Iterator var11;
         ItemStack i;
         if (this.ScaleMode.getValString().equalsIgnoreCase("H")) {
            drawItem(e.field_70165_t, e.field_70163_u + (double)e.field_70131_O + (0.75D + higher) * scale, e.field_70161_v, -2.5D, 0.0D, scale, e.func_184614_ca());
            drawItem(e.field_70165_t, e.field_70163_u + (double)e.field_70131_O + (0.75D + higher) * scale, e.field_70161_v, 2.5D, 0.0D, scale, e.func_184592_cb());

            for(var11 = e.func_184193_aE().iterator(); var11.hasNext(); --c) {
               i = (ItemStack)var11.next();
               drawItem(e.field_70165_t, e.field_70163_u + (double)e.field_70131_O + (0.75D + higher) * scale, e.field_70161_v, c + 1.5D, 0.0D, scale, i);
            }
         } else if (this.ScaleMode.getValString().equalsIgnoreCase("V")) {
            drawItem(e.field_70165_t, e.field_70163_u + (double)e.field_70131_O + (0.75D + higher) * scale, e.field_70161_v, -1.25D, 0.0D, scale, e.func_184614_ca());
            drawItem(e.field_70165_t, e.field_70163_u + (double)e.field_70131_O + (0.75D + higher) * scale, e.field_70161_v, 1.25D, 0.0D, scale, e.func_184592_cb());
            var11 = e.func_184193_aE().iterator();

            while(var11.hasNext()) {
               i = (ItemStack)var11.next();
               if (i.func_190916_E() >= 1) {
                  drawItem(e.field_70165_t, e.field_70163_u + (double)e.field_70131_O + (0.75D + higher) * scale, e.field_70161_v, 0.0D, c, scale, i);
                  ++c;
               }
            }
         }
      }

   }

   private void Processtext(String s, int stringWidth, Setting getcolor, Entity entity, double rofl, double scale) {
      try {
         glSetup(entity.field_70165_t, entity.field_70163_u + rofl, entity.field_70161_v);
         GlStateManager.func_179139_a(-0.025D * scale, -0.025D * scale, 0.025D * scale);
         GlStateManager.func_179090_x();
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder bufferbuilder = tessellator.func_178180_c();
         bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
         bufferbuilder.func_181662_b((double)(-stringWidth - 1), -1.0D, 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
         bufferbuilder.func_181662_b((double)(-stringWidth - 1), 8.0D, 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
         bufferbuilder.func_181662_b((double)(stringWidth + 1), 8.0D, 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
         bufferbuilder.func_181662_b((double)(stringWidth + 1), -1.0D, 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F).func_181675_d();
         tessellator.func_78381_a();
         GlStateManager.func_179098_w();
         mc.field_71466_p.func_175063_a(s, (float)(-stringWidth), 0.0F, getcolor.getcolor());
         glCleanup();
      } catch (Exception var11) {
      }

   }

   public static void drawItem(double x, double y, double z, double offX, double offY, double scale, ItemStack item) {
      glSetup(x, y, z);
      GlStateManager.func_179139_a(0.4D * scale, 0.4D * scale, 0.0D);
      GlStateManager.func_179137_b(offX, offY, 0.0D);
      mc.field_175620_Y.func_187462_a(mc.field_71439_g, item, TransformType.NONE, false);
      GlStateManager.func_179098_w();
      GlStateManager.func_179140_f();
      GlStateManager.func_179152_a(-0.05F, -0.05F, 0.0F);

      try {
         int c;
         if (item.func_190916_E() > 0) {
            c = mc.field_71466_p.func_78256_a("x" + item.func_190916_E()) / 2;
            mc.field_71466_p.func_175063_a("x" + item.func_190916_E(), (float)(7 - c), 5.0F, 16777215);
         }

         GlStateManager.func_179152_a(0.85F, 0.85F, 0.85F);
         c = 0;

         for(Iterator var14 = EnchantmentHelper.func_82781_a(item).entrySet().iterator(); var14.hasNext(); --c) {
            Entry<Enchantment, Integer> m = (Entry)var14.next();
            int w1 = mc.field_71466_p.func_78256_a(I18n.func_135052_a(((Enchantment)m.getKey()).func_77320_a().substring(0, 2), new Object[0]) + (Integer)m.getValue() / 2);
            mc.field_71466_p.func_175063_a(I18n.func_135052_a(((Enchantment)m.getKey()).func_77320_a(), new Object[0]).substring(0, 2) + m.getValue(), (float)(-4 - w1 + 3), (float)(c * 10 - 1), m.getKey() != Enchantments.field_190940_C && m.getKey() != Enchantments.field_190941_k ? 16756960 : 16732240);
         }

         GlStateManager.func_179152_a(0.6F, 0.6F, 0.6F);
         String dur = item.func_77958_k() - item.func_77952_i() + "";
         int color = MathHelper.func_181758_c((float)(item.func_77958_k() - item.func_77952_i()) / (float)item.func_77958_k() / 3.0F, 1.0F, 1.0F);
         if (item.func_77984_f()) {
            mc.field_71466_p.func_175063_a(dur, (float)(-8 - dur.length() * 3), 15.0F, (new Color(color >> 16 & 255, color >> 8 & 255, color & 255)).getRGB());
         }
      } catch (Exception var17) {
      }

      glCleanup();
   }

   public static void glSetup(double x, double y, double z) {
      GlStateManager.func_179094_E();
      RenderManager renderManager = mc.func_175598_ae();
      GlStateManager.func_179137_b(x - mc.func_175598_ae().field_78730_l, y - mc.func_175598_ae().field_78731_m, z - mc.func_175598_ae().field_78728_n);
      GlStateManager.func_187432_a(0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-renderManager.field_78735_i, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(mc.field_71474_y.field_74320_O == 2 ? -renderManager.field_78732_j : renderManager.field_78732_j, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179140_f();
      GL11.glDisable(2929);
      GlStateManager.func_179147_l();
      GlStateManager.func_179120_a(770, 771, 1, 0);
   }

   public static void glCleanup() {
      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(2929);
      GlStateManager.func_179126_j();
      GL11.glTranslatef(-0.5F, 0.0F, 0.0F);
      GlStateManager.func_179121_F();
   }
}
