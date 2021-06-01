package Method.Client.module.movement;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.proxy.Overrides.MoveOverride;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InvMove extends Module {
   private static boolean Toggled;

   public InvMove() {
      super("Inv Move", 0, Category.MOVEMENT, "Inventory Move");
   }

   public static boolean runthething() {
      if (Toggled) {
         mc.field_71439_g.field_71158_b.field_78902_a = 0.0F;
         mc.field_71439_g.field_71158_b.field_192832_b = 0.0F;
         if (Keyboard.isKeyDown(203)) {
            mc.field_71439_g.field_70177_z -= 5.0F;
         }

         if (Keyboard.isKeyDown(205)) {
            mc.field_71439_g.field_70177_z += 5.0F;
         }

         if (Keyboard.isKeyDown(200)) {
            mc.field_71439_g.field_70125_A = Math.max(mc.field_71439_g.field_70125_A - 5.0F, -90.0F);
         }

         if (Keyboard.isKeyDown(208)) {
            mc.field_71439_g.field_70125_A = Math.min(mc.field_71439_g.field_70125_A + 5.0F, 90.0F);
         }

         KeyBinding.func_74510_a(mc.field_71474_y.field_74351_w.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74351_w.func_151463_i()));
         if (Keyboard.isKeyDown(mc.field_71474_y.field_74351_w.func_151463_i())) {
            ++mc.field_71439_g.field_71158_b.field_192832_b;
            mc.field_71439_g.field_71158_b.field_187255_c = true;
         } else {
            mc.field_71439_g.field_71158_b.field_187255_c = false;
         }

         KeyBinding.func_74510_a(mc.field_71474_y.field_74368_y.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74368_y.func_151463_i()));
         if (Keyboard.isKeyDown(mc.field_71474_y.field_74368_y.func_151463_i())) {
            --mc.field_71439_g.field_71158_b.field_192832_b;
            mc.field_71439_g.field_71158_b.field_187256_d = true;
         } else {
            mc.field_71439_g.field_71158_b.field_187256_d = false;
         }

         KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74370_x.func_151463_i()));
         if (Keyboard.isKeyDown(mc.field_71474_y.field_74370_x.func_151463_i())) {
            ++mc.field_71439_g.field_71158_b.field_78902_a;
            mc.field_71439_g.field_71158_b.field_187257_e = true;
         } else {
            mc.field_71439_g.field_71158_b.field_187257_e = false;
         }

         KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74366_z.func_151463_i()));
         if (Keyboard.isKeyDown(mc.field_71474_y.field_74366_z.func_151463_i())) {
            --mc.field_71439_g.field_71158_b.field_78902_a;
            mc.field_71439_g.field_71158_b.field_187258_f = true;
         } else {
            mc.field_71439_g.field_71158_b.field_187258_f = false;
         }

         KeyBinding.func_74510_a(mc.field_71474_y.field_74314_A.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74314_A.func_151463_i()));
         mc.field_71439_g.field_71158_b.field_78901_c = Keyboard.isKeyDown(mc.field_71474_y.field_74314_A.func_151463_i());
         if (Mouse.isButtonDown(2)) {
            Mouse.setGrabbed(true);
            mc.field_71415_G = true;
         } else if (mc.field_71462_r != null) {
            Mouse.setGrabbed(false);
            mc.field_71415_G = false;
         }

         return true;
      } else {
         return false;
      }
   }

   public void onEnable() {
      Toggled = true;
      MoveOverride.toggle();
   }

   public void onDisable() {
      Toggled = false;
   }
}
