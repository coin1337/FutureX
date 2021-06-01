package Method.Client.managers;

import Method.Client.module.Module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class SettingsManager {
   private final ArrayList<Setting> settings = new ArrayList();

   public Setting add(Setting in) {
      this.settings.add(in);
      return in;
   }

   public ArrayList<Setting> getSettings() {
      return this.settings;
   }

   public ArrayList<Setting> getSettingsByMod(Module mod) {
      return (ArrayList)this.settings.stream().filter((s) -> {
         return s.getParentMod().equals(mod);
      }).collect(Collectors.toList());
   }

   public void setSettingsByMod(Module mod, ArrayList<Setting> change) {
      Iterator var3 = this.settings.iterator();

      while(true) {
         Setting s;
         do {
            if (!var3.hasNext()) {
               return;
            }

            s = (Setting)var3.next();
         } while(!s.getParentMod().equals(mod));

         Iterator var5 = change.iterator();

         while(var5.hasNext()) {
            Setting Inputsetting = (Setting)var5.next();
            if (s.getName().equalsIgnoreCase(Inputsetting.getName())) {
               s.setall(Inputsetting);
            }
         }
      }
   }

   public Setting getSettingByName(String name) {
      Iterator var2 = this.getSettings().iterator();

      Setting set;
      do {
         if (!var2.hasNext()) {
            System.err.println("[FutureX] Error Setting NOT found: '" + name + "'!");
            return null;
         }

         set = (Setting)var2.next();
      } while(!set.getName().equalsIgnoreCase(name));

      return set;
   }
}
