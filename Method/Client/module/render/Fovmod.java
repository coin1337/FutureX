package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Fovmod extends Module {
   public float defaultFov;
   Setting Change;
   Setting Smooth;
   Setting FovMode;

   public Fovmod() {
      super("Fovmod", 0, Category.RENDER, "Fovmod");
      this.Change = Main.setmgr.add(new Setting("Change", this, 100.0D, 0.0D, 500.0D, true));
      this.Smooth = Main.setmgr.add(new Setting("Smooth", this, true));
      this.FovMode = Main.setmgr.add(new Setting("FovMode", this, "ViewModelChanger", new String[]{"ViewModelChanger", "FovChanger", "Zoom"}));
   }

   public void FOVModifier(FOVModifier event) {
      if (this.FovMode.getValString().equalsIgnoreCase("ViewModelChanger")) {
         event.setFOV((float)this.Change.getValDouble());
      }

   }

   public void onEnable() {
      this.defaultFov = mc.field_71474_y.field_74334_X;
   }

   public void onDisable() {
      mc.field_71474_y.field_74334_X = this.defaultFov;
      mc.field_71474_y.field_74326_T = false;
   }

   public void onClientTick(ClientTickEvent event) {
      mc.field_71474_y.field_74326_T = this.Smooth.getValBoolean();
      if (this.FovMode.getValString().equalsIgnoreCase("FovChanger")) {
         mc.field_71474_y.field_74334_X = (float)this.Change.getValDouble();
      }

      if (this.FovMode.getValString().equalsIgnoreCase("Zoom")) {
         GameSettings var10000;
         int i;
         if (mc.field_71474_y.field_74334_X > 12.0F) {
            for(i = 0; (double)i < this.Change.getValDouble(); ++i) {
               if (mc.field_71474_y.field_74334_X > 12.0F) {
                  var10000 = mc.field_71474_y;
                  var10000.field_74334_X -= 0.1F;
               }
            }
         } else if (mc.field_71474_y.field_74334_X < this.defaultFov) {
            for(i = 0; (double)i < this.Change.getValDouble(); ++i) {
               var10000 = mc.field_71474_y;
               var10000.field_74334_X += 0.1F;
            }
         }
      }

   }
}
