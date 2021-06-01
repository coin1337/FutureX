package Method.Client.utils.Patcher.Patches;

import Method.Client.utils.Patcher.ModClassVisitor;
import Method.Client.utils.Patcher.Events.SetOpaqueCubeEvent;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public final class VisGraphVisitor extends ModClassVisitor {
   public VisGraphVisitor(ClassVisitor cv, boolean obf) {
      super(cv);
      String blockPos = this.unmap("net/minecraft/util/math/BlockPos");
      String setOpaqueCube_name = obf ? "a" : "setOpaqueCube";
      String setOpaqueCube_desc = "(L" + blockPos + ";)V";
      this.registerMethodVisitor(setOpaqueCube_name, setOpaqueCube_desc, VisGraphVisitor.SetOpaqueCubeVisitor::new);
   }

   public static class SetOpaqueCubeVisitor extends MethodVisitor {
      public SetOpaqueCubeVisitor(MethodVisitor mv) {
         super(262144, mv);
      }

      public void visitCode() {
         System.out.println("VisGraphVisitor.SetOpaqueCubeVisitor.visitCode()");
         super.visitCode();
         this.mv.visitMethodInsn(184, Type.getInternalName(this.getClass()), "setOpaqueCube", "()Z", false);
         Label l1 = new Label();
         this.mv.visitJumpInsn(154, l1);
         this.mv.visitInsn(177);
         this.mv.visitLabel(l1);
         this.mv.visitFrame(3, 0, (Object[])null, 0, (Object[])null);
      }

      public static boolean setOpaqueCube() {
         return !MinecraftForge.EVENT_BUS.post(new SetOpaqueCubeEvent());
      }
   }
}
