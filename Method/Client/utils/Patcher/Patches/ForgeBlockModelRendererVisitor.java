package Method.Client.utils.Patcher.Patches;

import Method.Client.utils.Patcher.ModClassVisitor;
import Method.Client.utils.Patcher.Events.RenderBlockModelEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public final class ForgeBlockModelRendererVisitor extends ModClassVisitor {
   public ForgeBlockModelRendererVisitor(ClassVisitor cv, boolean obf) {
      super(cv);
      String iBlockAccess = this.unmap("net/minecraft/world/IBlockAccess");
      String iBakedModel = this.unmap("net/minecraft/client/renderer/block/model/IBakedModel");
      String iBlockState = this.unmap("net/minecraft/block/state/IBlockState");
      String blockPos = this.unmap("net/minecraft/util/math/BlockPos");
      String bufferBuilder = this.unmap("net/minecraft/client/renderer/BufferBuilder");
      String renderModelFlat_name = obf ? "c" : "renderModelFlat";
      String renderModelSmooth_name = obf ? "b" : "renderModelSmooth";
      String renderModel_desc = "(L" + iBlockAccess + ";L" + iBakedModel + ";L" + iBlockState + ";L" + blockPos + ";L" + bufferBuilder + ";ZJ)Z";
      this.registerMethodVisitor(renderModelFlat_name, renderModel_desc, ForgeBlockModelRendererVisitor.RenderModelFlatVisitor::new);
      this.registerMethodVisitor(renderModelSmooth_name, renderModel_desc, ForgeBlockModelRendererVisitor.RenderModelSmoothVisitor::new);
   }

   public static class RenderModelSmoothVisitor extends MethodVisitor {
      public RenderModelSmoothVisitor(MethodVisitor mv) {
         super(262144, mv);
      }

      public void visitCode() {
         System.out.println("ForgeBlockModelRendererVisitor.RenderModelSmoothVisitor.visitCode()");
         super.visitCode();
         this.mv.visitVarInsn(25, 3);
         this.mv.visitMethodInsn(184, Type.getInternalName(this.getClass()), "renderBlockModel", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
         Label l1 = new Label();
         this.mv.visitJumpInsn(154, l1);
         this.mv.visitInsn(3);
         this.mv.visitInsn(172);
         this.mv.visitLabel(l1);
         this.mv.visitFrame(3, 0, (Object[])null, 0, (Object[])null);
      }

      public static boolean renderBlockModel(IBlockState state) {
         return !MinecraftForge.EVENT_BUS.post(new RenderBlockModelEvent(state));
      }
   }

   public static class RenderModelFlatVisitor extends MethodVisitor {
      public RenderModelFlatVisitor(MethodVisitor mv) {
         super(262144, mv);
      }

      public void visitCode() {
         System.out.println("ForgeBlockModelRendererVisitor.RenderModelFlatVisitor.visitCode()");
         super.visitCode();
         this.mv.visitVarInsn(25, 3);
         this.mv.visitMethodInsn(184, Type.getInternalName(this.getClass()), "renderBlockModel", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
         Label l1 = new Label();
         this.mv.visitJumpInsn(154, l1);
         this.mv.visitInsn(3);
         this.mv.visitInsn(172);
         this.mv.visitLabel(l1);
         this.mv.visitFrame(3, 0, (Object[])null, 0, (Object[])null);
      }

      public static boolean renderBlockModel(IBlockState state) {
         return !MinecraftForge.EVENT_BUS.post(new RenderBlockModelEvent(state));
      }
   }
}
