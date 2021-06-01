package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.proxy.Overrides.MoveOverride;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

public class AutoHold extends Module {
   private static boolean Toggled;
   private static boolean Unloaded = false;
   public Setting unloadedChunk;
   public static Setting w;
   public static Setting a;
   public static Setting s;
   public static Setting d;
   public static Setting lc;
   public static Setting rc;
   public static Setting Space;

   public static boolean runthething() {
      if (Toggled) {
         if (Unloaded) {
            return false;
         } else {
            mc.field_71439_g.field_71158_b.field_78902_a = 0.0F;
            mc.field_71439_g.field_71158_b.field_192832_b = 0.0F;
            KeyBinding.func_74510_a(mc.field_71474_y.field_74351_w.func_151463_i(), w.getValBoolean() || Keyboard.isKeyDown(mc.field_71474_y.field_74351_w.func_151463_i()));
            if (!w.getValBoolean() && !Keyboard.isKeyDown(mc.field_71474_y.field_74351_w.func_151463_i())) {
               mc.field_71439_g.field_71158_b.field_187255_c = false;
            } else {
               ++mc.field_71439_g.field_71158_b.field_192832_b;
               mc.field_71439_g.field_71158_b.field_187255_c = true;
            }

            KeyBinding.func_74510_a(mc.field_71474_y.field_74368_y.func_151463_i(), s.getValBoolean() || Keyboard.isKeyDown(mc.field_71474_y.field_74368_y.func_151463_i()));
            if (!s.getValBoolean() && !Keyboard.isKeyDown(mc.field_71474_y.field_74368_y.func_151463_i())) {
               mc.field_71439_g.field_71158_b.field_187256_d = false;
            } else {
               --mc.field_71439_g.field_71158_b.field_192832_b;
               mc.field_71439_g.field_71158_b.field_187256_d = true;
            }

            KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), a.getValBoolean() || Keyboard.isKeyDown(mc.field_71474_y.field_74370_x.func_151463_i()));
            if (!a.getValBoolean() && !Keyboard.isKeyDown(mc.field_71474_y.field_74370_x.func_151463_i())) {
               mc.field_71439_g.field_71158_b.field_187257_e = false;
            } else {
               ++mc.field_71439_g.field_71158_b.field_78902_a;
               mc.field_71439_g.field_71158_b.field_187257_e = true;
            }

            KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), d.getValBoolean() || Keyboard.isKeyDown(mc.field_71474_y.field_74366_z.func_151463_i()));
            if (!d.getValBoolean() && !Keyboard.isKeyDown(mc.field_71474_y.field_74366_z.func_151463_i())) {
               mc.field_71439_g.field_71158_b.field_187258_f = false;
            } else {
               --mc.field_71439_g.field_71158_b.field_78902_a;
               mc.field_71439_g.field_71158_b.field_187258_f = true;
            }

            KeyBinding.func_74510_a(mc.field_71474_y.field_74314_A.func_151463_i(), Keyboard.isKeyDown(mc.field_71474_y.field_74314_A.func_151463_i()));
            mc.field_71439_g.field_71158_b.field_78901_c = Keyboard.isKeyDown(mc.field_71474_y.field_74314_A.func_151463_i());
            mc.field_71439_g.field_71158_b.field_78901_c = Space.getValBoolean();
            return true;
         }
      } else {
         return false;
      }
   }

   public void setup() {
      Main.setmgr.add(this.unloadedChunk = new Setting("Stop for unloaded", this, false));
      Main.setmgr.add(w = new Setting("w", this, true));
      Main.setmgr.add(a = new Setting("a", this, false));
      Main.setmgr.add(s = new Setting("s", this, false));
      Main.setmgr.add(d = new Setting("d", this, false));
      Main.setmgr.add(lc = new Setting("lc", this, false));
      Main.setmgr.add(rc = new Setting("rc", this, false));
      Main.setmgr.add(Space = new Setting("Space", this, false));
   }

   public AutoHold() {
      super("AutoHold", 0, Category.MOVEMENT, "Auto Walk + More!");
   }

   public void onEnable() {
      MoveOverride.toggle();
      Toggled = true;
   }

   public void onDisable() {
      Toggled = false;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.unloadedChunk.getValBoolean()) {
         Unloaded = !mc.field_71441_e.func_175726_f(mc.field_71439_g.func_180425_c()).func_177410_o();
      }

   }
}
