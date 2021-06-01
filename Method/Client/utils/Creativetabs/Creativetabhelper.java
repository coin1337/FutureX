package Method.Client.utils.Creativetabs;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class Creativetabhelper {
   public static ItemStack ItemWithEnchants(Item foe, ArrayList<Enchantment> enchants, ArrayList<Integer> levels) {
      ItemStack item = new ItemStack(foe);

      try {
         int forlevels = 0;

         for(Iterator var5 = enchants.iterator(); var5.hasNext(); ++forlevels) {
            Enchantment ench = (Enchantment)var5.next();
            if (item.field_77990_d == null) {
               item.func_77982_d(new NBTTagCompound());
            }

            if (!item.field_77990_d.func_150297_b("ench", 9)) {
               item.field_77990_d.func_74782_a("ench", new NBTTagList());
            }

            NBTTagList nbttaglist = item.field_77990_d.func_150295_c("ench", 10);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.func_74777_a("id", (short)Enchantment.func_185258_b(ench));
            nbttagcompound.func_74768_a("lvl", (Integer)levels.get(forlevels));
            nbttaglist.func_74742_a(nbttagcompound);
         }
      } catch (Exception var9) {
      }

      return item;
   }

   static void Unbreakpack(Item item, ArrayList<Enchantment> enchants, ArrayList<Integer> levels, NonNullList<ItemStack> itemList) {
      if (enchants != null || levels != null) {
         ItemStack D32k = ItemWithEnchants(item, enchants, levels);
         D32k.func_77983_a("Unbreakable", new NBTTagInt(1));
         itemList.add(D32k);
      }
   }

   static void Attributeitems(Item item, ArrayList<Enchantment> enchants, ArrayList<Integer> levels, NonNullList<ItemStack> itemList, EntityEquipmentSlot ro, boolean Enchanted) {
      if (enchants == null && levels == null) {
         Enchanted = false;
      }

      ItemStack D32k;
      if (Enchanted) {
         D32k = ItemWithEnchants(item, enchants, levels);
      } else {
         D32k = new ItemStack(item);
         if (D32k.field_77990_d == null) {
            D32k.func_77982_d(new NBTTagCompound());
         }
      }

      if (ro == EntityEquipmentSlot.MAINHAND) {
         D32k.func_185129_a("generic.attackDamage", new AttributeModifier("Weapon modifier", 20.0D, 0), ro);
         D32k.func_185129_a("generic.attackDamage", new AttributeModifier("Weapon modifier", 20.0D, 0), EntityEquipmentSlot.OFFHAND);
         D32k.func_185129_a("generic.attackSpeed", new AttributeModifier("Tool modifier", 20.0D, 0), ro);
         D32k.func_185129_a("generic.attackSpeed", new AttributeModifier("Tool modifier", 20.0D, 0), EntityEquipmentSlot.OFFHAND);
         D32k.func_185129_a("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2.0D, 0), ro);
         D32k.func_185129_a("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2.0D, 0), EntityEquipmentSlot.OFFHAND);
         D32k.func_185129_a("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200.0D, 0), ro);
         D32k.func_185129_a("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200.0D, 0), EntityEquipmentSlot.OFFHAND);
      }

      if (ro == EntityEquipmentSlot.OFFHAND) {
         D32k.func_185129_a("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2.0D, 0), EntityEquipmentSlot.MAINHAND);
         D32k.func_185129_a("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2.0D, 0), EntityEquipmentSlot.OFFHAND);
      }

      if (ro == EntityEquipmentSlot.HEAD) {
         D32k.func_185129_a("generic.maxHealth", new AttributeModifier("effect.healthBoost", -20.0D, 0), EntityEquipmentSlot.MAINHAND);
         D32k.func_185129_a("generic.maxHealth", new AttributeModifier("effect.healthBoost", -20.0D, 0), EntityEquipmentSlot.OFFHAND);
      }

      if (ro == EntityEquipmentSlot.CHEST) {
         D32k.func_185129_a("generic.attackDamage", new AttributeModifier("Weapon modifier", 20.0D, 0), EntityEquipmentSlot.HEAD);
         D32k.func_185129_a("generic.attackDamage", new AttributeModifier("Weapon modifier", 20.0D, 0), EntityEquipmentSlot.CHEST);
         D32k.func_185129_a("generic.attackDamage", new AttributeModifier("Weapon modifier", 20.0D, 0), EntityEquipmentSlot.LEGS);
         D32k.func_185129_a("generic.attackDamage", new AttributeModifier("Weapon modifier", 20.0D, 0), EntityEquipmentSlot.FEET);
         D32k.func_185129_a("generic.attackSpeed", new AttributeModifier("Tool modifier", 20.0D, 0), EntityEquipmentSlot.HEAD);
         D32k.func_185129_a("generic.attackSpeed", new AttributeModifier("Tool modifier", 20.0D, 0), EntityEquipmentSlot.CHEST);
         D32k.func_185129_a("generic.attackSpeed", new AttributeModifier("Tool modifier", 20.0D, 0), EntityEquipmentSlot.LEGS);
         D32k.func_185129_a("generic.attackSpeed", new AttributeModifier("Tool modifier", 20.0D, 0), EntityEquipmentSlot.FEET);
         D32k.func_185129_a("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2.0D, 0), EntityEquipmentSlot.HEAD);
         D32k.func_185129_a("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2.0D, 0), EntityEquipmentSlot.CHEST);
         D32k.func_185129_a("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2.0D, 0), EntityEquipmentSlot.LEGS);
         D32k.func_185129_a("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2.0D, 0), EntityEquipmentSlot.FEET);
         D32k.func_185129_a("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200.0D, 0), EntityEquipmentSlot.HEAD);
         D32k.func_185129_a("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200.0D, 0), EntityEquipmentSlot.CHEST);
         D32k.func_185129_a("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200.0D, 0), EntityEquipmentSlot.LEGS);
         D32k.func_185129_a("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200.0D, 0), EntityEquipmentSlot.FEET);
         D32k.func_185129_a("generic.armorToughness", new AttributeModifier("Armor toughness", 20.0D, 0), EntityEquipmentSlot.HEAD);
         D32k.func_185129_a("generic.armorToughness", new AttributeModifier("Armor toughness", 20.0D, 0), EntityEquipmentSlot.CHEST);
         D32k.func_185129_a("generic.armorToughness", new AttributeModifier("Armor toughness", 20.0D, 0), EntityEquipmentSlot.LEGS);
         D32k.func_185129_a("generic.armorToughness", new AttributeModifier("Armor toughness", 20.0D, 0), EntityEquipmentSlot.FEET);
      }

      itemList.add(D32k);
   }

   static void Packsize(Item item, ArrayList<Enchantment> enchants, ArrayList<Integer> levels, NonNullList<ItemStack> itemList) {
      if (enchants != null || levels != null) {
         ItemStack D32k = ItemWithEnchants(item, enchants, levels);
         itemList.add(D32k);
         ItemStack DSword64 = ItemWithEnchants(item, enchants, levels);
         DSword64.func_190920_e(64);
         itemList.add(DSword64);
         ItemStack Damaged = ItemWithEnchants(item, enchants, levels);
         Damaged.func_77964_b(Damaged.func_77958_k() + 100);
         itemList.add(Damaged);
         ItemStack Damage64 = ItemWithEnchants(item, enchants, levels);
         Damage64.func_190920_e(64);
         Damage64.func_77964_b(Damage64.func_77958_k() + 100);
         itemList.add(Damage64);
      }
   }
}
