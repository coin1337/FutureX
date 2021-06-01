package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemShears;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Livestock extends Module {
   public Setting Dye;
   public Setting Shear;
   public Setting Breed;

   public Livestock() {
      super("Livestock Mod", 0, Category.MISC, "Auto Sheepmod");
      this.Dye = Main.setmgr.add(new Setting("Auto Dye", this, true));
      this.Shear = Main.setmgr.add(new Setting("Auto Shear", this, false));
      this.Breed = Main.setmgr.add(new Setting("Auto Breed", this, false));
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemDye && this.Dye.getValBoolean() || this.Shear.getValBoolean() && mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemShears || this.Breed.getValBoolean()) {
         Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

         while(true) {
            EntitySheep sheep;
            while(true) {
               do {
                  Entity e;
                  do {
                     if (!var2.hasNext()) {
                        return;
                     }

                     e = (Entity)var2.next();
                     if (this.Breed.getValBoolean() && e instanceof EntityAnimal) {
                        EntityAnimal animal = (EntityAnimal)e;
                        if (animal.func_110143_aJ() > 0.0F && !animal.func_70631_g_() && !animal.func_70880_s() && mc.field_71439_g.func_70032_d(animal) <= 4.5F && animal.func_70877_b(mc.field_71439_g.field_71071_by.func_70448_g())) {
                           mc.field_71442_b.func_187097_a(mc.field_71439_g, animal, EnumHand.MAIN_HAND);
                        }
                     }
                  } while(!(e instanceof EntitySheep));

                  sheep = (EntitySheep)e;
               } while(!(sheep.func_110143_aJ() > 0.0F));

               if (this.Dye.getValBoolean()) {
                  if (sheep.func_175509_cj() != EnumDyeColor.func_176766_a(mc.field_71439_g.field_71071_by.func_70448_g().func_77960_j())) {
                     break;
                  }
               } else if (!sheep.func_70892_o() && mc.field_71439_g.func_70032_d(sheep) <= 4.5F) {
                  break;
               }
            }

            mc.field_71442_b.func_187097_a(mc.field_71439_g, sheep, EnumHand.MAIN_HAND);
         }
      }
   }
}
