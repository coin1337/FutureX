package Method.Client.utils.Creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.NonNullList;

public class CreativeTabBlocks extends CreativeTabs {
   public CreativeTabBlocks() {
      super("Blocks");
   }

   public String func_78013_b() {
      return "Blocks";
   }

   public void func_78018_a(NonNullList<ItemStack> itemList) {
      itemList.add(new ItemStack(Items.field_151095_cc));
      itemList.add(new ItemStack(Items.field_151164_bB));
      itemList.add(new ItemStack(Items.field_151098_aY));
      itemList.add(new ItemStack(Blocks.field_150483_bI));
      itemList.add(new ItemStack(Blocks.field_185777_dd));
      itemList.add(new ItemStack(Blocks.field_185776_dc));
      itemList.add(new ItemStack(Blocks.field_185779_df));
      itemList.add(new ItemStack(Blocks.field_189881_dj));
      itemList.add(new ItemStack(Blocks.field_150380_bt));
      itemList.add(new ItemStack(Blocks.field_180401_cv));
      itemList.add(new ItemStack(Blocks.field_185774_da));
      itemList.add(new ItemStack(Blocks.field_150420_aW));
      itemList.add(new ItemStack(Blocks.field_150419_aX));
      itemList.add(new ItemStack(Blocks.field_150458_ak));
      itemList.add(new ItemStack(Blocks.field_150474_ac));
      itemList.add(new ItemStack(Blocks.field_150329_H));
      ItemStack Furnace = new ItemStack(Blocks.field_150460_al);

      try {
         Furnace.func_77982_d(JsonToNBT.func_180713_a("{BlockStateTag:{lit:\"true\"}}"));
      } catch (NBTException var20) {
         var20.printStackTrace();
      }

      itemList.add(Furnace);
      ItemStack Water = new ItemStack(Items.field_151131_as);
      Water.func_190920_e(64);
      itemList.add(Water);
      ItemStack Lava = new ItemStack(Items.field_151129_at);
      Lava.func_190920_e(64);
      itemList.add(Lava);
      ItemStack Bucket = new ItemStack(Items.field_151133_ar);
      Bucket.func_190920_e(64);
      itemList.add(Bucket);
      ItemStack Epearl = new ItemStack(Items.field_151079_bi);
      Epearl.func_190920_e(64);
      itemList.add(Epearl);
      ItemStack egg = new ItemStack(Items.field_151110_aK);
      egg.func_190920_e(64);
      itemList.add(egg);
      ItemStack Sign = new ItemStack(Items.field_151155_ap);
      Sign.func_190920_e(64);
      itemList.add(Sign);
      ItemStack Banner = new ItemStack(Items.field_179564_cE);
      Banner.func_190920_e(64);
      itemList.add(Banner);
      ItemStack Snowball = new ItemStack(Items.field_151126_ay);
      Snowball.func_190920_e(64);
      itemList.add(Snowball);
      ItemStack Bed = new ItemStack(Items.field_151104_aV);
      Bed.func_190920_e(64);
      itemList.add(Bed);
      ItemStack Boat = new ItemStack(Items.field_151124_az);
      Boat.func_190920_e(64);
      itemList.add(Boat);
      ItemStack Cake = new ItemStack(Items.field_151105_aU);
      Cake.func_190920_e(64);
      itemList.add(Cake);
      ItemStack Totm = new ItemStack(Items.field_190929_cY);
      Totm.func_190920_e(64);
      itemList.add(Totm);
      ItemStack Shul = new ItemStack(Item.func_150899_d(229));
      Shul.func_190920_e(64);
      itemList.add(Shul);
      ItemStack Mush = new ItemStack(Items.field_151009_A);
      Mush.func_190920_e(64);
      itemList.add(Mush);
      ItemStack Saddle = new ItemStack(Items.field_151141_av);
      Saddle.func_190920_e(64);
      itemList.add(Saddle);
      ItemStack Tntmc = new ItemStack(Items.field_151142_bV);
      Tntmc.func_190920_e(64);
      itemList.add(Tntmc);
      ItemStack Minecart = new ItemStack(Items.field_151143_au);
      Minecart.func_190920_e(64);
      itemList.add(Minecart);
   }

   public ItemStack func_78016_d() {
      return new ItemStack(Items.field_151069_bo);
   }
}
