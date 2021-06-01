package Method.Client.utils.Creativetabs;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class CreativeTabEggpot extends CreativeTabs {
   ArrayList<Enchantment> Enchants = new ArrayList();
   ArrayList<Integer> Levels = new ArrayList();
   ItemStack Blankspot;

   public CreativeTabEggpot() {
      super("Items");
      this.Blankspot = new ItemStack(Items.field_151118_aC);
   }

   public void func_78018_a(NonNullList<ItemStack> itemList) {
      this.Blankspot.func_190920_e(-1);

      try {
         Iterator var2 = LootTableList.func_186374_a().iterator();

         while(var2.hasNext()) {
            ResourceLocation dan = (ResourceLocation)var2.next();
            ItemStack Entitydrop = new ItemStack(Items.field_151063_bx);
            Entitydrop.func_77982_d(JsonToNBT.func_180713_a("{EntityTag:{DeathLootTable:\"" + dan.func_110624_b() + "\",id:\"minecraft:bat\",ActiveEffects:[{Duration:2147483647,Id:7,Amplifier:0}]}}"));
            itemList.add(Entitydrop);
         }
      } catch (NBTException var5) {
         var5.printStackTrace();
      }

      super.func_78018_a(itemList);
   }

   private void clearvar() {
      this.Enchants.clear();
      this.Levels.clear();
   }

   public ItemStack func_78016_d() {
      return new ItemStack(Items.field_151110_aK);
   }
}
