package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Bunnyhop extends Module {
   private int airMoves;
   Setting mode;

   public Bunnyhop() {
      super("Bunnyhop", 0, Category.MOVEMENT, "Bunny hop");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "AAC", new String[]{"AAC", "NCP", "Timer", "Spartan"}));
   }

   public void onClientTick(ClientTickEvent event) {
      EntityPlayerSP var10000;
      if (this.mode.getValString().equalsIgnoreCase("Timer")) {
         if (mc.field_71439_g.field_70122_E) {
            if (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) {
               mc.field_71439_g.func_70664_aZ();
            }

            var10000 = mc.field_71439_g;
            var10000.field_70179_y /= 2.0D;
            var10000 = mc.field_71439_g;
            var10000.field_70159_w /= 2.0D;
            var10000 = mc.field_71439_g;
            var10000.field_70181_x += 0.05000000074505806D;
         } else {
            var10000 = mc.field_71439_g;
            var10000.field_70181_x -= 0.029999999329447746D;
            var10000 = mc.field_71439_g;
            var10000.field_70702_br *= 0.07F;
            mc.field_71439_g.field_70747_aH = 0.05F;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Spartan") && mc.field_71474_y.field_74351_w.func_151468_f() && !mc.field_71474_y.field_74314_A.func_151468_f()) {
         if (mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.func_70664_aZ();
            this.airMoves = 0;
         } else {
            if (this.airMoves >= 3) {
               mc.field_71439_g.field_70747_aH = 0.0275F;
            }

            if (this.airMoves >= 4 && (double)(this.airMoves % 2) == 0.0D) {
               mc.field_71439_g.field_70181_x = -0.3199999928474426D - 0.009D * Math.random();
               mc.field_71439_g.field_70747_aH = 0.0238F;
            }

            ++this.airMoves;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("AAC")) {
         if (mc.field_71439_g.func_70090_H()) {
            return;
         }

         if (mc.field_71439_g.field_191988_bg == 0.0F && mc.field_71439_g.field_70702_br == 0.0F) {
            mc.field_71439_g.field_70159_w = 0.0D;
            mc.field_71439_g.field_70179_y = 0.0D;
         } else if (mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.func_70664_aZ();
            var10000 = mc.field_71439_g;
            var10000.field_70159_w *= 1.012D;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= 1.012D;
         } else if (mc.field_71439_g.field_70181_x > -0.2D) {
            mc.field_71439_g.field_70747_aH = 0.08F;
            var10000 = mc.field_71439_g;
            var10000.field_70181_x += 3.1E-4D;
            mc.field_71439_g.field_70747_aH = 0.07F;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("NCP") && mc.field_71439_g != null && mc.field_71441_e != null && mc.field_71474_y.field_74351_w.field_74513_e && !mc.field_71439_g.field_70123_F) {
         mc.field_71474_y.field_74314_A.field_74513_e = false;
         if (mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.func_70664_aZ();
            var10000 = mc.field_71439_g;
            var10000.field_70159_w *= 1.0707999467849731D;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= 1.0707999467849731D;
            var10000 = mc.field_71439_g;
            var10000.field_70702_br *= 2.0F;
         } else {
            mc.field_71439_g.field_70747_aH = 0.0265F;
         }
      }

   }

   public void onDisable() {
      mc.field_71439_g.field_70747_aH = 0.03F;
      super.onDisable();
   }
}
