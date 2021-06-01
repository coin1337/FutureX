package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class MotionBlur extends Module {
   boolean Setup = true;
   double old = 0.0D;
   private static Map domainResourceManagers;
   public static Setting blurAmount;

   public void setup() {
      Main.setmgr.add(blurAmount = new Setting("blurAmount", this, 1.0D, 0.0D, 10.0D, false));
   }

   public MotionBlur() {
      super("MotionBlur", 0, Category.RENDER, "MotionBlur");
   }

   public void onEnable() {
      this.Setup = true;
      domainResourceManagers = null;
   }

   public void onDisable() {
      mc.field_71460_t.func_181022_b();
      domainResourceManagers = null;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.old != blurAmount.getValDouble()) {
         this.old = blurAmount.getValDouble();
         this.Setup = true;
         domainResourceManagers = null;
      } else {
         if (domainResourceManagers == null) {
            try {
               Field[] var2 = SimpleReloadableResourceManager.class.getDeclaredFields();
               Field[] var3 = var2;
               int var4 = var2.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  Field field = var3[var5];
                  if (field.getType() == Map.class) {
                     field.setAccessible(true);
                     domainResourceManagers = (Map)field.get(mc.func_110442_L());
                     break;
                  }
               }
            } catch (Exception var7) {
               throw new RuntimeException(var7);
            }
         }

         assert domainResourceManagers != null;

         if (!domainResourceManagers.containsKey("motionblur")) {
            domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
         }

         if (this.Setup) {
            mc.field_71460_t.func_175069_a(new ResourceLocation("motionblur", "motionblur"));
            mc.field_71460_t.func_147706_e().func_148026_a(MC.field_71443_c, MC.field_71440_d);
            this.Setup = false;
         }

      }
   }
}
