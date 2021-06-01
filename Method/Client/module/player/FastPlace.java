package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class FastPlace extends Module {
   Setting Delay;
   Setting XP;
   Setting Crystal;

   public FastPlace() {
      super("FastPlace", 0, Category.PLAYER, "Place Blocks Faster");
      this.Delay = Main.setmgr.add(new Setting("Delay", this, 0.0D, 0.0D, 20.0D, false));
      this.XP = Main.setmgr.add(new Setting("XP Only", this, false));
      this.Crystal = Main.setmgr.add(new Setting("Crystal Only", this, false));
   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      if (this.XP.getValBoolean() && (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemExpBottle || mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemExpBottle)) {
         mc.field_71467_ac = (int)this.Delay.getValDouble();
      }

      if (this.Crystal.getValBoolean() && (mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemEndCrystal || mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemEndCrystal)) {
         mc.field_71467_ac = (int)this.Delay.getValDouble();
      }

      if (!this.XP.getValBoolean() || !this.Crystal.getValBoolean()) {
         mc.field_71467_ac = (int)this.Delay.getValDouble();
      }

   }
}
