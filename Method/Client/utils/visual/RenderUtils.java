package Method.Client.utils.visual;

import Method.Client.module.misc.ModSettings;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class RenderUtils {
   protected static Minecraft mc = Minecraft.func_71410_x();
   private static final ICamera camera = new Frustum();

   public static void OpenGl() {
      GlStateManager.func_179094_E();
      GlStateManager.func_179147_l();
      GlStateManager.func_179097_i();
      GlStateManager.func_179090_x();
      GlStateManager.func_179140_f();
      GlStateManager.func_179129_p();
      GlStateManager.func_179132_a(false);
      GL11.glHint(3154, 4354);
      GlStateManager.func_179120_a(770, 771, 0, 1);
      GL11.glEnable(2848);
      GL11.glEnable(34383);
   }

   public static void ReleaseGl() {
      GlStateManager.func_179098_w();
      GlStateManager.func_179126_j();
      GlStateManager.func_179089_o();
      GlStateManager.func_179145_e();
      GlStateManager.func_179084_k();
      GlStateManager.func_179121_F();
      GlStateManager.func_179132_a(true);
      GL11.glHint(3154, 4352);
      GL11.glDisable(2848);
      GL11.glDisable(34383);
      GlStateManager.func_179103_j(7424);
   }

   public static void RenderBlock(String s, AxisAlignedBB bb, int c, Double width) {
      if (!s.equalsIgnoreCase("Tracer") && !ModSettings.Rendernonsee.getValBoolean()) {
         camera.func_78547_a(((Entity)Objects.requireNonNull(mc.func_175606_aa())).field_70165_t, mc.func_175606_aa().field_70163_u, mc.func_175606_aa().field_70161_v);
         if (!camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + mc.func_175598_ae().field_78730_l, -10.0D, bb.field_72339_c + mc.func_175598_ae().field_78728_n, bb.field_72336_d + mc.func_175598_ae().field_78730_l, 2500.0D, bb.field_72334_f + mc.func_175598_ae().field_78728_n))) {
            return;
         }
      }

      OpenGl();
      GlStateManager.func_187441_d((float)(1.5D * (width + 1.0E-4D)));
      float a = ColorUtils.colorcalc(c, 24);
      float r = ColorUtils.colorcalc(c, 16);
      float g = ColorUtils.colorcalc(c, 8);
      float b = ColorUtils.colorcalc(c, 0);
      byte var9 = -1;
      switch(s.hashCode()) {
      case -1781849107:
         if (s.equals("Tracer")) {
            var9 = 6;
         }
         break;
      case 2192281:
         if (s.equals("Flat")) {
            var9 = 1;
         }
         break;
      case 2201263:
         if (s.equals("Full")) {
            var9 = 3;
         }
         break;
      case 79847297:
         if (s.equals("Shape")) {
            var9 = 0;
         }
         break;
      case 84807002:
         if (s.equals("Xspot")) {
            var9 = 7;
         }
         break;
      case 558407714:
         if (s.equals("Outline")) {
            var9 = 4;
         }
         break;
      case 650354281:
         if (s.equals("FlatOutline")) {
            var9 = 2;
         }
         break;
      case 1985788004:
         if (s.equals("Beacon")) {
            var9 = 5;
         }
      }

      switch(var9) {
      case 0:
         Sphere sph = new Sphere();
         sph.setDrawStyle(100013);
         GlStateManager.func_179131_c(r, g, b, a);
         GlStateManager.func_179137_b((bb.field_72336_d + bb.field_72340_a) / 2.0D, (bb.field_72337_e + bb.field_72338_b) / 2.0D, (bb.field_72334_f + bb.field_72339_c) / 2.0D);
         sph.draw(1.0F, (int)ModSettings.Spherelines.getValDouble(), (int)ModSettings.SphereDist.getValDouble());
         break;
      case 1:
         RenderGlobal.func_189695_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, r, g, b, a);
         break;
      case 2:
         RenderGlobal.func_189694_a(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, bb.field_72338_b, bb.field_72334_f, r, g, b, a);
         break;
      case 3:
         RenderGlobal.func_189694_a(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, r, g, b, a);
         RenderGlobal.func_189695_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, r, g, b, a);
         break;
      case 4:
         RenderGlobal.func_189694_a(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, r, g, b, a);
         break;
      case 5:
         RenderGlobal.func_189694_a(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, r, g, b, a);
         RenderGlobal.func_189694_a(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, 0.0D - bb.field_72337_e + 255.0D, bb.field_72334_f, r, g, b, a / 2.0F);
         RenderGlobal.func_189695_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, 0.0D - bb.field_72337_e + 255.0D, bb.field_72334_f, r, g, b, a / 4.0F);
         break;
      case 6:
         GlStateManager.func_187447_r(1);
         GlStateManager.func_179131_c(r, g, b, a);
         Vec3d eyes = ActiveRenderInfo.getCameraPosition();
         GL11.glVertex3d(eyes.field_72450_a, eyes.field_72448_b, eyes.field_72449_c);
         GL11.glVertex3d(bb.func_189972_c().field_72450_a, bb.func_189972_c().field_72448_b, bb.func_189972_c().field_72449_c);
         GlStateManager.func_187437_J();
         break;
      case 7:
         BufferBuilder BBuild2 = Tessellator.func_178181_a().func_178180_c();
         BBuild2.func_181668_a(3, DefaultVertexFormats.field_181706_f);
         BBuild2.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
         BBuild2.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
         BBuild2.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(r, g, b, 0.0F).func_181675_d();
         BBuild2.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
         Tessellator.func_178181_a().func_78381_a();
      }

      ReleaseGl();
   }

   public static AxisAlignedBB Standardbb(BlockPos pos) {
      double renderPosX = (double)pos.func_177958_n() - mc.func_175598_ae().field_78730_l;
      double renderPosY = (double)pos.func_177956_o() - mc.func_175598_ae().field_78731_m;
      double renderPosZ = (double)pos.func_177952_p() - mc.func_175598_ae().field_78728_n;
      return new AxisAlignedBB(renderPosX, renderPosY, renderPosZ, renderPosX + 1.0D, renderPosY + 1.0D, renderPosZ + 1.0D);
   }

   public static AxisAlignedBB Boundingbb(Entity entity, double x, double y, double z, double x1, double y1, double z1) {
      double renderPosX = entity.func_174813_aQ().field_72340_a - mc.func_175598_ae().field_78730_l;
      double renderPosY = entity.func_174813_aQ().field_72338_b - mc.func_175598_ae().field_78731_m;
      double renderPosZ = entity.func_174813_aQ().field_72339_c - mc.func_175598_ae().field_78728_n;
      double renderPosX2 = entity.func_174813_aQ().field_72336_d - mc.func_175598_ae().field_78730_l;
      double renderPosY2 = entity.func_174813_aQ().field_72337_e - mc.func_175598_ae().field_78731_m;
      double renderPosZ2 = entity.func_174813_aQ().field_72334_f - mc.func_175598_ae().field_78728_n;
      return new AxisAlignedBB(renderPosX + x, renderPosY + y, renderPosZ + z, renderPosX2 + x1, renderPosY2 + y1, renderPosZ2 + z1);
   }

   public static AxisAlignedBB Modifiedbb(BlockPos pos, int x, int y, int z) {
      double renderPosX = (double)pos.func_177958_n() - mc.func_175598_ae().field_78730_l;
      double renderPosY = (double)pos.func_177956_o() - mc.func_175598_ae().field_78731_m;
      double renderPosZ = (double)pos.func_177952_p() - mc.func_175598_ae().field_78728_n;
      return new AxisAlignedBB(renderPosX + (double)x, renderPosY + (double)y, renderPosZ + (double)z, renderPosX + 1.0D + (double)x, renderPosY + 1.0D + (double)y, renderPosZ + 1.0D + (double)z);
   }

   public static void RenderLine(List<Vec3d> List, int Color, double width, boolean valBoolean) {
      OpenGl();
      GL11.glEnable(32925);
      GL11.glLineWidth((float)width);
      ColorUtils.glColor(Color);
      GL11.glBegin(3);
      RenderManager renderManager = mc.func_175598_ae();
      Iterator var6 = List.iterator();

      while(var6.hasNext()) {
         Vec3d blockPos = (Vec3d)var6.next();
         if (valBoolean) {
            BlockPos snap = new BlockPos(blockPos);
            GL11.glVertex3d((double)snap.field_177962_a - renderManager.field_78730_l, (double)snap.field_177960_b - renderManager.field_78731_m, (double)snap.field_177961_c - renderManager.field_78728_n);
         } else {
            GL11.glVertex3d(blockPos.field_72450_a - renderManager.field_78730_l, blockPos.field_72448_b - renderManager.field_78731_m, blockPos.field_72449_c - renderManager.field_78728_n);
         }
      }

      GL11.glEnd();
      ReleaseGl();
      GL11.glDisable(32925);
   }

   public static Vec3d getInterpolatedRenderPos(Vec3d pos) {
      return (new Vec3d(pos.field_72450_a, pos.field_72448_b, pos.field_72449_c)).func_178786_a(mc.func_175598_ae().field_78725_b, mc.func_175598_ae().field_78726_c, mc.func_175598_ae().field_78723_d);
   }

   public static void SimpleNametag(Vec3d pos, String str) {
      OpenGl();
      boolean isThirdPersonFrontal = mc.func_175598_ae().field_78733_k.field_74320_O == 2;
      Vec3d interp = getInterpolatedRenderPos(pos);
      GlStateManager.func_179137_b(interp.field_72450_a + 0.5D, interp.field_72448_b + 0.75D, interp.field_72449_c + 0.5D);
      GlStateManager.func_179114_b(-mc.func_175598_ae().field_78735_i, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b((float)(isThirdPersonFrontal ? -1 : 1) * mc.func_175598_ae().field_78732_j, 1.0F, 0.0F, 0.0F);
      float m = (float)Math.pow(1.04D, mc.field_71439_g.func_70011_f(pos.field_72450_a, pos.field_72448_b + 0.5D, pos.field_72449_c));
      GlStateManager.func_179152_a(m, m, m);
      GlStateManager.func_179152_a(-0.025F, -0.025F, 0.025F);
      int i = mc.field_71466_p.func_78256_a(str) / 2;
      GlStateManager.func_187432_a(0.0F, 1.0F, 0.0F);
      GlStateManager.func_179098_w();
      mc.field_71466_p.func_175063_a(str, (float)(-i), 9.0F, Color.WHITE.getRGB());
      GlStateManager.func_179090_x();
      GlStateManager.func_187432_a(0.0F, 0.0F, 0.0F);
      ReleaseGl();
   }

   public static void drawRectOutline(double left, double top, double right, double bottom, double width, int color) {
      double l = left - width;
      double t = top - width;
      double r = right + width;
      double b = bottom + width;
      drawRectDouble(l, t, r, top, color);
      drawRectDouble(right, top, r, b, color);
      drawRectDouble(l, bottom, right, b, color);
      drawRectDouble(l, top, left, bottom, color);
   }

   public static void drawRectDouble(double left, double top, double right, double bottom, int color) {
      double j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      GlStateManager.func_179147_l();
      GlStateManager.func_179090_x();
      GlStateManager.func_187428_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
      ColorUtils.glColor(color);
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
      bufferbuilder.func_181662_b(left, bottom, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(right, bottom, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(right, top, 0.0D).func_181675_d();
      bufferbuilder.func_181662_b(left, top, 0.0D).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179098_w();
      GlStateManager.func_179084_k();
   }
}
