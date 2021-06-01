package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.combat.AutoArmor;
import Method.Client.utils.BlockUtils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FoodMod extends Module {
   Setting Souphunger;
   Setting Soup;
   Setting AntiHunger;
   Setting AutoEat;
   Setting SetFoodLevelMax;
   int slotBefore;
   int bestSlot;
   int eating;
   private int oldSlot;

   public FoodMod() {
      super("FoodMod", 0, Category.PLAYER, "FoodMod");
      this.Souphunger = Main.setmgr.add(new Setting("Hunger", this, 10.0D, 0.0D, 20.0D, true));
      this.Soup = Main.setmgr.add(new Setting("Soup", this, false));
      this.AntiHunger = Main.setmgr.add(new Setting("AntiHunger", this, false));
      this.AutoEat = Main.setmgr.add(new Setting("AutoEat", this, false));
      this.SetFoodLevelMax = Main.setmgr.add(new Setting("SetFoodLevelMax", this, false));
      this.slotBefore = -1;
      this.bestSlot = -1;
      this.eating = 40;
      this.oldSlot = -1;
   }

   public void onEnable() {
      this.oldSlot = -1;
      this.bestSlot = -1;
      super.onEnable();
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && this.AntiHunger.getValBoolean() && packet instanceof CPacketPlayer) {
         CPacketPlayer packet2 = (CPacketPlayer)packet;
         packet2.field_149474_g = mc.field_71439_g.field_70143_R >= 0.0F || mc.field_71442_b.func_181040_m();
      }

      return true;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.SetFoodLevelMax.getValBoolean()) {
         mc.field_71439_g.func_71024_bL().func_75114_a(20);
      }

      float bestRestoration;
      int PrevSlot;
      ItemStack stack;
      float restoration;
      if (this.AutoEat.getValBoolean()) {
         if (this.oldSlot == -1) {
            if (!this.canEat()) {
               return;
            }

            bestRestoration = 0.0F;

            for(PrevSlot = 0; PrevSlot < 9; ++PrevSlot) {
               stack = mc.field_71439_g.field_71071_by.func_70301_a(PrevSlot);
               if (this.isFood(stack)) {
                  ItemFood food = (ItemFood)stack.func_77973_b();
                  restoration = food.func_150906_h(stack);
                  if (restoration > bestRestoration) {
                     bestRestoration = restoration;
                     this.bestSlot = PrevSlot;
                  }
               }
            }

            if (this.bestSlot != -1) {
               this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
            }
         } else {
            if (!this.canEat()) {
               this.stop();
               return;
            }

            if (!this.isFood(mc.field_71439_g.field_71071_by.func_70301_a(this.bestSlot))) {
               this.stop();
               return;
            }

            mc.field_71439_g.field_71071_by.field_70461_c = this.bestSlot;
            mc.field_71474_y.field_74313_G.field_74513_e = true;
         }
      }

      if (this.AntiHunger.getValBoolean()) {
         mc.field_71439_g.field_70122_E = false;
      }

      if (this.Soup.getValBoolean()) {
         int soupInHotbar;
         for(soupInHotbar = 0; soupInHotbar < 36; ++soupInHotbar) {
            ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(soupInHotbar);
            if (stack.func_77973_b() == Items.field_151054_z && soupInHotbar != 9) {
               stack = mc.field_71439_g.field_71071_by.func_70301_a(9);
               boolean swap = !AutoArmor.isNullOrEmpty(stack) && stack.func_77973_b() != Items.field_151054_z;
               windowClick_PICKUP(soupInHotbar < 9 ? 36 + soupInHotbar : soupInHotbar);
               windowClick_PICKUP(9);
               if (swap) {
                  windowClick_PICKUP(soupInHotbar < 9 ? 36 + soupInHotbar : soupInHotbar);
               }
            }
         }

         soupInHotbar = this.findSoup(0, 9);
         if (soupInHotbar != -1) {
            if (!this.shouldEatSoup()) {
               this.stopIfEating();
               return;
            }

            if (this.oldSlot == -1) {
               this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
            }

            mc.field_71439_g.field_71071_by.field_70461_c = soupInHotbar;
            mc.field_71474_y.field_74313_G.field_74513_e = true;
            processRightClick();
            return;
         }

         this.stopIfEating();
         PrevSlot = this.findSoup(9, 36);
         if (PrevSlot != -1) {
            windowClick_QUICK_MOVE(PrevSlot);
         }
      }

      if (!this.Soup.getValBoolean()) {
         if (this.eating < 41) {
            ++this.eating;
            if (this.eating <= 1) {
               mc.field_71439_g.field_71071_by.field_70461_c = this.bestSlot;
            }

            mc.field_71474_y.field_74313_G.field_74513_e = true;
            if (this.eating >= 38) {
               mc.field_71474_y.field_74313_G.field_74513_e = true;
               if (this.slotBefore != -1) {
                  mc.field_71439_g.field_71071_by.field_70461_c = this.slotBefore;
               }

               this.slotBefore = -1;
            }

            return;
         }

         bestRestoration = 0.0F;
         this.bestSlot = -1;
         PrevSlot = mc.field_71439_g.field_71071_by.field_70461_c;

         for(int i = 0; i < 9; ++i) {
            ItemStack item = mc.field_71439_g.field_71071_by.func_70301_a(i);
            restoration = 0.0F;
            if (item.func_77973_b() instanceof ItemFood) {
               restoration = ((ItemFood)item.func_77973_b()).func_150906_h(item);
            }

            if (restoration > bestRestoration) {
               bestRestoration = restoration;
               this.bestSlot = i;
            }
         }

         if (this.bestSlot == -1) {
            return;
         }

         if (!((double)mc.field_71439_g.func_71024_bL().func_75116_a() < this.Souphunger.getValDouble())) {
            return;
         }

         this.slotBefore = mc.field_71439_g.field_71071_by.field_70461_c;
         if (this.slotBefore == -1) {
            return;
         }

         mc.field_71439_g.field_71071_by.field_70461_c = PrevSlot;
         mc.field_71439_g.func_184597_cx();
         mc.field_71439_g.field_71071_by.field_70461_c = PrevSlot;
         this.eating = 0;
         super.onClientTick(event);
      }

   }

   private int findSoup(int startSlot, int endSlot) {
      for(int i = startSlot; i < endSlot; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack.func_77973_b() instanceof ItemSoup) {
            return i;
         }
      }

      return -1;
   }

   private boolean canEat() {
      if (!mc.field_71439_g.func_71043_e(false)) {
         return false;
      } else {
         if (mc.field_71476_x != null) {
            Entity entity = mc.field_71476_x.field_72308_g;
            if (entity instanceof EntityVillager || entity instanceof EntityTameable) {
               return false;
            }

            BlockPos pos = mc.field_71476_x.func_178782_a();
            if (pos != null) {
               Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
               if (block instanceof BlockContainer || block instanceof BlockWorkbench) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean isFood(ItemStack stack) {
      return stack.func_77973_b() instanceof ItemFood;
   }

   private void stop() {
      mc.field_71474_y.field_74313_G.field_74513_e = false;
      mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot;
      this.oldSlot = -1;
   }

   private boolean shouldEatSoup() {
      if ((double)mc.field_71439_g.func_110143_aJ() > 13.0D) {
         return false;
      } else if (Wrapper.mc.field_71462_r != null) {
         return false;
      } else if (Wrapper.mc.field_71476_x != null) {
         Entity entity = Wrapper.mc.field_71476_x.field_72308_g;
         if (!(entity instanceof EntityVillager) && !(entity instanceof EntityTameable)) {
            Wrapper.mc.field_71476_x.func_178782_a();
            return !(getBlock(Wrapper.mc.field_71476_x.func_178782_a()) instanceof BlockContainer);
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private void stopIfEating() {
      if (this.oldSlot != -1) {
         mc.field_71474_y.field_74313_G.field_74513_e = true;
         mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot;
         this.oldSlot = -1;
      }
   }

   public static Block getBlock(BlockPos pos) {
      return BlockUtils.getState(pos).func_177230_c();
   }

   public static ItemStack windowClick_PICKUP(int slot) {
      return getPlayerController().func_187098_a(0, slot, 0, ClickType.PICKUP, mc.field_71439_g);
   }

   private static PlayerControllerMP getPlayerController() {
      return mc.field_71442_b;
   }

   public static void processRightClick() {
      getPlayerController().func_187101_a(mc.field_71439_g, mc.field_71439_g.field_70170_p, EnumHand.MAIN_HAND);
   }

   public static ItemStack windowClick_QUICK_MOVE(int slot) {
      return getPlayerController().func_187098_a(0, slot, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
   }
}
