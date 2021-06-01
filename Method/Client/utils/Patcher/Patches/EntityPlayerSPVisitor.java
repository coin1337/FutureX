package Method.Client.utils.Patcher.Patches;

import Method.Client.utils.Patcher.ModClassVisitor;
import Method.Client.utils.Patcher.Events.PlayerMoveEvent;
import Method.Client.utils.Patcher.Events.PostMotionEvent;
import Method.Client.utils.Patcher.Events.PreMotionEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public final class EntityPlayerSPVisitor extends ModClassVisitor {
   public EntityPlayerSPVisitor(ClassVisitor cv, boolean obf) {
      super(cv);
      String onUpdatedesc = "()V";
      String moverType = this.unmap("net/minecraft/entity/MoverType");
      String onUpdate = obf ? "B_" : "onUpdate";
      String moveEntity_name = obf ? "a" : "move";
      String moveEntity_desc = "(L" + moverType + ";DDD)V";
      this.registerMethodVisitor(onUpdate, onUpdatedesc, EntityPlayerSPVisitor.onUpdateplayer::new);
      this.registerMethodVisitor(moveEntity_name, moveEntity_desc, EntityPlayerSPVisitor.MoveEntityVisitor::new);
   }

   public static class MoveEntityVisitor extends MethodVisitor {
      public MoveEntityVisitor(MethodVisitor mv) {
         super(262144, mv);
      }

      public void visitCode() {
         System.out.println("EntityPlayerSPVisitor.MoveEntityVisitor.visitCode()");
         super.visitCode();
         this.mv.visitVarInsn(25, 0);
         this.mv.visitMethodInsn(184, Type.getInternalName(this.getClass()), "onPlayerMove", "(Lnet/minecraft/client/entity/EntityPlayerSP;)V", false);
      }

      public static void onPlayerMove(EntityPlayerSP player) {
         MinecraftForge.EVENT_BUS.post(new PlayerMoveEvent(player));
      }
   }

   public static class onUpdateplayer extends MethodVisitor {
      public onUpdateplayer(MethodVisitor mv) {
         super(262144, mv);
      }

      public void visitCode() {
         System.out.println("EntityPlayerSPVisitor.onUpdateplayer.visitCode()");
         this.mv.visitVarInsn(25, 0);
         this.mv.visitMethodInsn(184, Type.getInternalName(this.getClass()), "onPreMotion", "(Lnet/minecraft/client/entity/EntityPlayerSP;)V", false);
         super.visitCode();
      }

      public void visitInsn(int opcode) {
         if (opcode == 177) {
            System.out.println("EntityPlayerSPVisitor.onUpdateplayer.visitInsn()");
            this.mv.visitVarInsn(25, 0);
            this.mv.visitMethodInsn(184, Type.getInternalName(this.getClass()), "onPostMotion", "(Lnet/minecraft/client/entity/EntityPlayerSP;)V", false);
         }

         super.visitInsn(opcode);
      }

      public static void onPreMotion(EntityPlayerSP player) {
         MinecraftForge.EVENT_BUS.post(new PreMotionEvent(player));
      }

      public static void onPostMotion(EntityPlayerSP player) {
         MinecraftForge.EVENT_BUS.post(new PostMotionEvent(player));
      }
   }
}
