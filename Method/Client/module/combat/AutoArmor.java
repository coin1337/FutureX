package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoArmor extends Module {
   private int timer;
   Setting useEnchantments;
   Setting swapWhileMoving;
   Setting delay;
   Setting nocurse;
   Setting Elytra;
   Setting Edam;
   boolean ElytraSwitch;

   public AutoArmor() {
      super("Auto Armor", 0, Category.COMBAT, "Puts on any Armor");
      this.useEnchantments = Main.setmgr.add(new Setting("Enchantments", this, true));
      this.swapWhileMoving = Main.setmgr.add(new Setting("SwapWhileMoving", this, true));
      this.delay = Main.setmgr.add(new Setting("Delay", this, 1.0D, 0.0D, 5.0D, true));
      this.nocurse = Main.setmgr.add(new Setting("No Binding", this, true));
      this.Elytra = Main.setmgr.add(new Setting("Elytra Over Chest", this, true));
      this.Edam = Main.setmgr.add(new Setting("Elytra Damage", this, 2.0D, 0.0D, 320.0D, true));
      this.ElytraSwitch = false;
   }

   public void onEnable() {
      this.ElytraSwitch = false;
      this.timer = 0;
      super.onEnable();
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.timer > 0) {
         --this.timer;
      } else if (!(Wrapper.INSTANCE.mc().field_71462_r instanceof GuiContainer) || Wrapper.INSTANCE.mc().field_71462_r instanceof InventoryEffectRenderer) {
         InventoryPlayer inventory = mc.field_71439_g.field_71071_by;
         if (this.swapWhileMoving.getValBoolean() || mc.field_71439_g.field_71158_b.field_192832_b == 0.0F && mc.field_71439_g.field_71158_b.field_78902_a == 0.0F) {
            int[] bestArmorSlots = new int[4];
            int[] bestArmorValues = new int[4];

            int slot;
            ItemStack stack;
            ItemArmor item;
            for(slot = 0; slot < 4; ++slot) {
               bestArmorSlots[slot] = -1;
               stack = inventory.func_70440_f(slot);
               if (this.Elytra.getValBoolean() && slot == 2 && stack.func_77973_b() instanceof ItemElytra) {
                  if (stack.func_190926_b()) {
                     this.ElytraSwitch = false;
                  }

                  if ((double)stack.func_77973_b().getDamage(stack) > (double)stack.func_77973_b().getMaxDamage(stack) - this.Edam.getValDouble()) {
                     this.ElytraSwitch = false;
                  }
               } else if (!isNullOrEmpty(stack) && stack.func_77973_b() instanceof ItemArmor) {
                  item = (ItemArmor)stack.func_77973_b();
                  bestArmorValues[slot] = this.getArmorValue(item, stack);
               }
            }

            int slot;
            for(slot = 0; slot < 36; ++slot) {
               stack = inventory.func_70301_a(slot);
               if (stack.func_77973_b() instanceof ItemElytra && this.Elytra.getValBoolean() && !this.ElytraSwitch) {
                  if (!((double)stack.func_77973_b().getDamage(stack) > (double)stack.func_77973_b().getMaxDamage(stack) - this.Edam.getValDouble())) {
                     bestArmorSlots[2] = slot;
                     this.ElytraSwitch = true;
                  }
               } else if (!isNullOrEmpty(stack) && stack.func_77973_b() instanceof ItemArmor && (!this.nocurse.getValBoolean() || !EnchantmentHelper.func_190938_b(stack))) {
                  item = (ItemArmor)stack.func_77973_b();
                  slot = item.field_77881_a.func_188454_b();
                  int armorValue = this.getArmorValue(item, stack);
                  if (armorValue > bestArmorValues[slot]) {
                     bestArmorSlots[slot] = slot;
                     bestArmorValues[slot] = armorValue;
                  }
               }
            }

            ArrayList<Integer> types = new ArrayList(Arrays.asList(0, 1, 2, 3));
            Collections.shuffle(types);
            Iterator var11 = types.iterator();

            while(var11.hasNext()) {
               int type = (Integer)var11.next();
               slot = bestArmorSlots[type];
               if (slot != -1) {
                  ItemStack oldArmor;
                  if (inventory.func_70440_f(type).func_77973_b() instanceof ItemElytra && this.Elytra.getValBoolean()) {
                     oldArmor = inventory.func_70440_f(type);
                     if (!((double)oldArmor.func_77973_b().getDamage(oldArmor) > (double)oldArmor.func_77973_b().getMaxDamage(oldArmor) - this.Edam.getValDouble())) {
                        continue;
                     }

                     Wrapper.INSTANCE.mc().field_71442_b.func_187098_a(0, 8 - type, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
                     this.ElytraSwitch = false;
                  }

                  oldArmor = inventory.func_70440_f(type);
                  if (isNullOrEmpty(oldArmor) || inventory.func_70447_i() != -1) {
                     if (slot < 9) {
                        slot += 36;
                     }

                     if (!isNullOrEmpty(oldArmor)) {
                        Wrapper.INSTANCE.mc().field_71442_b.func_187098_a(0, 8 - type, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
                     }

                     Wrapper.INSTANCE.mc().field_71442_b.func_187098_a(0, slot, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
                     break;
                  }
               }
            }

            super.onClientTick(event);
         }
      }
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && packet instanceof CPacketClickWindow) {
         this.timer = (int)this.delay.getValDouble();
      }

      return true;
   }

   public static boolean isNullOrEmpty(ItemStack stack) {
      return stack == null || stack.func_190926_b();
   }

   int getArmorValue(ItemArmor item, ItemStack stack) {
      int armorPoints = item.field_77879_b;
      int prtPoints = 0;
      int armorToughness = (int)item.field_189415_e;
      int armorType = item.func_82812_d().func_78044_b(EntityEquipmentSlot.LEGS);
      if (this.useEnchantments.getValBoolean()) {
         Enchantment protection = Enchantments.field_180310_c;
         int prtLvl = EnchantmentHelper.func_77506_a(protection, stack);
         DamageSource dmgSource = DamageSource.func_76365_a(mc.field_71439_g);
         prtPoints = protection.func_77318_a(prtLvl, dmgSource);
      }

      return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
   }
}
