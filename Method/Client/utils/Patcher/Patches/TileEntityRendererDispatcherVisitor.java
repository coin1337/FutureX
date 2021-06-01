package Method.Client.utils.Patcher.Patches;

import Method.Client.utils.Patcher.ModClassVisitor;
import Method.Client.utils.Patcher.Events.RenderTileEntityEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public final class TileEntityRendererDispatcherVisitor extends ModClassVisitor {
   public TileEntityRendererDispatcherVisitor(ClassVisitor cv, boolean obf) {
      super(cv);
      String tileEntity = this.unmap("net/minecraft/tileentity/TileEntity");
      String render_name = obf ? "a" : "render";
      String render_desc = "(L" + tileEntity + ";FI)V";
      this.registerMethodVisitor(render_name, render_desc, TileEntityRendererDispatcherVisitor.RenderVisitor::new);
   }

   public static class RenderVisitor extends MethodVisitor {
      public RenderVisitor(MethodVisitor mv) {
         super(262144, mv);
      }

      public void visitCode() {
         System.out.println("TileEntityRendererDispatcherVisitor.RenderVisitor.visitCode()");
         super.visitCode();
         this.mv.visitVarInsn(25, 1);
         this.mv.visitMethodInsn(184, Type.getInternalName(this.getClass()), "renderTileEntity", "(Lnet/minecraft/tileentity/TileEntity;)Z", false);
         Label l1 = new Label();
         this.mv.visitJumpInsn(154, l1);
         this.mv.visitInsn(177);
         this.mv.visitLabel(l1);
         this.mv.visitFrame(3, 0, (Object[])null, 0, (Object[])null);
      }

      public static boolean renderTileEntity(TileEntity tileEntity) {
         return !MinecraftForge.EVENT_BUS.post(new RenderTileEntityEvent(tileEntity));
      }
   }
}
