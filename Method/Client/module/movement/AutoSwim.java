package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoSwim extends Module {
   Setting mode;

   public AutoSwim() {
      super("Auto Swim", 0, Category.MOVEMENT, "Swims for you");
      this.mode = Main.setmgr.add(this.mode = new Setting("Mode", this, "Dolphin", new String[]{"Dolphin", "Jump", "Fish"}));
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71439_g.func_70090_H() || mc.field_71439_g.func_180799_ab()) {
         if (!mc.field_71439_g.func_70093_af() && !Wrapper.INSTANCE.mcSettings().field_74314_A.func_151470_d()) {
            if (this.mode.getValString().equalsIgnoreCase("Jump")) {
               mc.field_71439_g.func_70664_aZ();
            } else {
               EntityPlayerSP var10000;
               if (this.mode.getValString().equalsIgnoreCase("Dolphin")) {
                  var10000 = mc.field_71439_g;
                  var10000.field_70181_x += 0.03999999910593033D;
               } else if (this.mode.getValString().equalsIgnoreCase("Fish")) {
                  var10000 = mc.field_71439_g;
                  var10000.field_70181_x += 0.019999999552965164D;
               }
            }

            super.onClientTick(event);
         }
      }
   }
}
