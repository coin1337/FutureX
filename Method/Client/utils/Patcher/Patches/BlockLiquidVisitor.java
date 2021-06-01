package Method.Client.utils.Patcher.Patches;

import Method.Client.utils.Patcher.ModClassVisitor;
import Method.Client.utils.Patcher.Events.EventCanCollide;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public final class BlockLiquidVisitor extends ModClassVisitor {
   public BlockLiquidVisitor(ClassVisitor cv, boolean obf) {
      super(cv);
      String iBlockState = this.unmap("net/minecraft/block/state/IBlockState");
      String Getcollidecheck_name = obf ? "a" : "canCollideCheck";
      String Getcollidecheck_desc = "(L" + iBlockState + ";Z)Z";
      this.registerMethodVisitor(Getcollidecheck_name, Getcollidecheck_desc, BlockLiquidVisitor.canCollideCheckVisitor::new);
   }

   public static class canCollideCheckVisitor extends MethodVisitor {
      public canCollideCheckVisitor(MethodVisitor mv) {
         super(262144, mv);
      }

      public void visitCode() {
         System.out.println("BlockLiquidVisitor.canCollideCheck.visitFieldInsn()");
         this.mv.visitMethodInsn(184, Type.getInternalName(this.getClass()), "canCollideCheckHook", "()Z", false);
         Label l1 = new Label();
         this.mv.visitJumpInsn(153, l1);
         this.mv.visitInsn(4);
         this.mv.visitInsn(172);
         this.mv.visitLabel(l1);
         super.visitCode();
      }

      public static boolean canCollideCheckHook() {
         return MinecraftForge.EVENT_BUS.post(new EventCanCollide());
      }
   }
}
