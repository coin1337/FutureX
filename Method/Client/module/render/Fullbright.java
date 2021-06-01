package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class Fullbright extends Module {
   private float oldBrightness;
   Setting mode;

   public Fullbright() {
      super("Fullbright", 0, Category.RENDER, "Makes the screen bright");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Potion", new String[]{"Gamma", "Potion"}));
   }

   public void PlayerRespawnEvent(PlayerRespawnEvent event) {
      PotionEffect nv = new PotionEffect((Potion)Objects.requireNonNull(Potion.func_188412_a(16)), 9999999, 3);
      mc.field_71439_g.func_70690_d(nv);
   }

   public void onEnable() {
      if (this.mode.getValString().equalsIgnoreCase("Gamma")) {
         this.oldBrightness = mc.field_71474_y.field_74333_Y;
         mc.field_71474_y.field_74333_Y = 10.0F;
      }

      if (this.mode.getValString().equalsIgnoreCase("Potion")) {
         PotionEffect nv = new PotionEffect((Potion)Objects.requireNonNull(Potion.func_188412_a(16)), 9999999, 3);
         mc.field_71439_g.func_70690_d(nv);
      }

      super.onEnable();
   }

   public void onDisable() {
      mc.field_71474_y.field_74333_Y = this.oldBrightness;
      mc.field_71439_g.func_184596_c(Potion.func_188412_a(16));
      super.onDisable();
   }
}
