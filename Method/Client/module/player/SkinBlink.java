package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import java.util.Random;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class SkinBlink extends Module {
   Setting mode;
   Setting slowness;
   private static final EnumPlayerModelParts[] PARTS_HORIZONTAL;
   private static final EnumPlayerModelParts[] PARTS_VERTICAL;
   private Random r;
   private int len;

   public SkinBlink() {
      super("SkinBlink", 0, Category.PLAYER, "SkinBlink");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Flat", new String[]{"HORIZONTAL", "VERTICAL", "RANDOM"}));
      this.slowness = Main.setmgr.add(new Setting("slowness", this, 2.0D, 1.0D, 2.0D, true));
   }

   public void setup() {
      this.r = new Random();
      this.len = EnumPlayerModelParts.values().length;
   }

   public void onClientTick(ClientTickEvent event) {
      String var2 = this.mode.getValString();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1884956477:
         if (var2.equals("RANDOM")) {
            var3 = 0;
         }
         break;
      case -1201514634:
         if (var2.equals("VERTICAL")) {
            var3 = 1;
         }
         break;
      case 1872721956:
         if (var2.equals("HORIZONTAL")) {
            var3 = 2;
         }
      }

      switch(var3) {
      case 0:
         if ((double)mc.field_71439_g.field_70173_aa % this.slowness.getValDouble() != 0.0D) {
            return;
         }

         mc.field_71474_y.func_178877_a(EnumPlayerModelParts.values()[this.r.nextInt(this.len)]);
         break;
      case 1:
      case 2:
         int i = (int)((double)mc.field_71439_g.field_70173_aa / this.slowness.getValDouble() % (double)(PARTS_HORIZONTAL.length * 2));
         if (i >= PARTS_HORIZONTAL.length) {
            i -= PARTS_HORIZONTAL.length;
            mc.field_71474_y.func_178878_a(this.mode.getValString().equalsIgnoreCase("Vertical") ? PARTS_VERTICAL[i] : PARTS_HORIZONTAL[i], true);
         }
      }

   }

   static {
      PARTS_HORIZONTAL = new EnumPlayerModelParts[]{EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.HAT, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.RIGHT_SLEEVE};
      PARTS_VERTICAL = new EnumPlayerModelParts[]{EnumPlayerModelParts.HAT, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.RIGHT_SLEEVE, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG};
   }
}
