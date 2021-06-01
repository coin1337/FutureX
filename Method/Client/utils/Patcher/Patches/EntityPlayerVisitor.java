package Method.Client.utils.Patcher.Patches;

import Method.Client.utils.Patcher.ModClassVisitor;
import Method.Client.utils.Patcher.Events.EntityPlayerJumpEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public final class EntityPlayerVisitor extends ModClassVisitor {
   public EntityPlayerVisitor(ClassVisitor cv, boolean obf) {
      super(cv);
      String jump_name = obf ? "cu" : "jump";
      String jump_desc = "()V";
      this.registerMethodVisitor(jump_name, jump_desc, EntityPlayerVisitor.JumpVisitor::new);
   }

   public static class JumpVisitor extends MethodVisitor {
      public JumpVisitor(MethodVisitor mv) {
         super(262144, mv);
      }

      public void visitCode() {
         System.out.println("EntityPlayerVisitor.JumpVisitor.visitCode()");
         super.visitCode();
         this.mv.visitVarInsn(25, 0);
         this.mv.visitMethodInsn(184, Type.getInternalName(this.getClass()), "entityPlayerJump", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", false);
         Label l1 = new Label();
         this.mv.visitJumpInsn(154, l1);
         this.mv.visitInsn(177);
         this.mv.visitLabel(l1);
         this.mv.visitFrame(3, 0, (Object[])null, 0, (Object[])null);
      }

      public static boolean entityPlayerJump(EntityPlayer player) {
         return !MinecraftForge.EVENT_BUS.post(new EntityPlayerJumpEvent(player));
      }
   }
}
