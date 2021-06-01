package Method.Client.module.Profiles;

import Method.Client.Main;
import Method.Client.managers.FileManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import java.util.ArrayList;
import java.util.Iterator;

public class Profiletem extends Module {
   public String name;

   public Profiletem(String name) {
      super(name, 0, Category.PROFILES, name);
      this.name = name;
   }

   public void setsave() {
      this.StoredModules.clear();
      this.StoredModules.addAll(ModuleManager.toggledModules);
      this.StoredModules.removeIf((module) -> {
         return module.getCategory().equals(Category.PROFILES) || module.getCategory().equals(Category.ONSCREEN);
      });
      this.StoredSettings.clear();
      Iterator var1 = this.StoredModules.iterator();

      while(var1.hasNext()) {
         Module storedModule = (Module)var1.next();
         Iterator var3 = Main.setmgr.getSettingsByMod(storedModule).iterator();

         while(var3.hasNext()) {
            Setting setting = (Setting)var3.next();
            this.StoredSettings.add(new Setting(setting));
         }
      }

      FileManager.savePROFILES();
   }

   public void setdelete() {
      this.StoredModules.clear();
      this.StoredSettings.clear();
   }

   public void onEnable() {
      ArrayList<Module> remove = new ArrayList();
      ModuleManager.toggledModules.forEach((module) -> {
         if (!module.getCategory().equals(Category.PROFILES) && !module.getCategory().equals(Category.ONSCREEN)) {
            remove.add(module);
         }

      });
      remove.forEach((module) -> {
         if (!module.getCategory().equals(Category.PROFILES) && !module.getCategory().equals(Category.ONSCREEN)) {
            module.setToggled(false);
         }

      });
      ModuleManager.toggledModules.removeAll(remove);
      Iterator var2 = this.StoredModules.iterator();

      while(true) {
         Module N;
         do {
            do {
               if (!var2.hasNext()) {
                  this.toggle();
                  return;
               }

               N = (Module)var2.next();
            } while(N.getCategory().equals(Category.PROFILES));
         } while(N.getCategory().equals(Category.ONSCREEN));

         N.toggle();
         ArrayList<Setting> change = new ArrayList();
         Iterator var5 = this.StoredSettings.iterator();

         while(var5.hasNext()) {
            Setting setting = (Setting)var5.next();
            if (setting.getParentMod().equals(N)) {
               change.add(setting);
            }
         }

         Main.setmgr.setSettingsByMod(N, change);
      }
   }
}
