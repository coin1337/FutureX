package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Refill extends Module {
   Setting delay;
   Setting percentage;
   Setting offHand;
   private final TimerUtils timer;

   public Refill() {
      super("Refill", 0, Category.COMBAT, "Refill");
      this.delay = Main.setmgr.add(new Setting("delay", this, 5.0D, 0.0D, 10.0D, true));
      this.percentage = Main.setmgr.add(new Setting("percentage", this, 50.0D, 0.0D, 100.0D, false));
      this.offHand = Main.setmgr.add(new Setting("offHand", this, true));
      this.timer = new TimerUtils();
   }

   public void onClientTick(ClientTickEvent event) {
      if (!this.timer.isDelay((long)(this.delay.getValDouble() * 1000.0D)) || !(mc.field_71462_r instanceof GuiInventory)) {
         int toRefill = this.getRefillable(mc.field_71439_g);
         if (toRefill != -1) {
            this.refillHotbarSlot(toRefill);
         }

      }
   }

   private int getRefillable(EntityPlayerSP player) {
      if (this.offHand.getValBoolean() && player.func_184592_cb().func_77973_b() != Items.field_190931_a && player.func_184592_cb().func_190916_E() < player.func_184592_cb().func_77976_d() && (double)player.func_184592_cb().func_190916_E() / (double)player.func_184592_cb().func_77976_d() <= this.percentage.getValDouble() / 100.0D) {
         return 45;
      } else {
         for(int i = 0; i < 9; ++i) {
            ItemStack stack = (ItemStack)player.field_71071_by.field_70462_a.get(i);
            if (stack.func_77973_b() != Items.field_190931_a && stack.func_190916_E() < stack.func_77976_d() && (double)stack.func_190916_E() / (double)stack.func_77976_d() <= this.percentage.getValDouble() / 100.0D) {
               return i;
            }
         }

         return -1;
      }
   }

   private int getSmallestStack(EntityPlayerSP player, ItemStack itemStack) {
      if (itemStack == null) {
         return -1;
      } else {
         int minCount = itemStack.func_77976_d() + 1;
         int minIndex = -1;

         for(int i = 9; i < player.field_71071_by.field_70462_a.size(); ++i) {
            ItemStack stack = (ItemStack)player.field_71071_by.field_70462_a.get(i);
            if (stack.func_77973_b() != Items.field_190931_a && stack.func_77973_b() == itemStack.func_77973_b() && stack.func_190916_E() < minCount) {
               minCount = stack.func_190916_E();
               minIndex = i;
            }
         }

         return minIndex;
      }
   }

   public void refillHotbarSlot(int slot) {
      ItemStack stack;
      if (slot == 45) {
         stack = mc.field_71439_g.func_184592_cb();
      } else {
         stack = (ItemStack)mc.field_71439_g.field_71071_by.field_70462_a.get(slot);
      }

      if (stack.func_77973_b() != Items.field_190931_a) {
         int biggestStack = this.getSmallestStack(mc.field_71439_g, stack);
         if (biggestStack != -1) {
            if (slot == 45) {
               mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, biggestStack, 0, ClickType.PICKUP, mc.field_71439_g);
               mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, 45, 0, ClickType.PICKUP, mc.field_71439_g);
               mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, biggestStack, 0, ClickType.PICKUP, mc.field_71439_g);
            } else {
               int overflow = -1;

               for(int i = 0; i < 9 && overflow == -1; ++i) {
                  if (((ItemStack)mc.field_71439_g.field_71071_by.field_70462_a.get(i)).func_77973_b() == Items.field_190931_a) {
                     overflow = i;
                  }
               }

               mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, biggestStack, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
               if (overflow != -1 && ((ItemStack)mc.field_71439_g.field_71071_by.field_70462_a.get(overflow)).func_77973_b() != Items.field_190931_a) {
                  mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, biggestStack, overflow, ClickType.SWAP, mc.field_71439_g);
               }

            }
         }
      }
   }
}
