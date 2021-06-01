package Method.Client.utils.proxy.Overrides;

import Method.Client.module.movement.AutoHold;
import Method.Client.module.movement.InvMove;
import Method.Client.utils.system.Wrapper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;

public class MoveOverride extends MovementInputFromOptions {
   public MoveOverride(GameSettings gameSettingsIn) {
      super(gameSettingsIn);
   }

   public static void toggle() {
      Wrapper.mc.field_71439_g.field_71158_b = new MoveOverride(Wrapper.mc.field_71474_y);
   }

   public void func_78898_a() {
      if (!InvMove.runthething() && !AutoHold.runthething()) {
         super.func_78898_a();
      } else {
         this.field_78901_c = Keyboard.isKeyDown(Wrapper.mc.field_71474_y.field_74314_A.func_151463_i());
         this.field_78899_d = Keyboard.isKeyDown(Wrapper.mc.field_71474_y.field_74311_E.func_151463_i());
         if (this.field_78899_d) {
            this.field_78902_a = (float)((double)this.field_78902_a * 0.3D);
            this.field_192832_b = (float)((double)this.field_192832_b * 0.3D);
         }
      }

   }
}
