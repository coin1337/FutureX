package Method.Client.utils.proxy.Overrides;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;

public class PitchYawHelper extends MouseHelper {
   public static boolean Pitch = false;
   public static boolean Yaw = false;

   public void func_74374_c() {
      this.field_74377_a = Mouse.getDX();
      this.field_74375_b = Mouse.getDY();
      if (Pitch) {
         Minecraft.func_71410_x().field_71417_B.field_74375_b = 0;
      }

      if (Yaw) {
         Minecraft.func_71410_x().field_71417_B.field_74377_a = 0;
      }

   }
}
