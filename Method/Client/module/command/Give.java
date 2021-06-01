package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class Give extends Command {
   public Give() {
      super("Give");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (!mc.field_71439_g.func_184812_l_()) {
            ChatUtils.error("You must be in creative mode.");
         }

         Item item = null;
         int amount = 1;
         int metadata = 0;
         StringBuilder nbt = null;
         item = Item.func_111206_d(args[0]);
         if (item == null) {
            ChatUtils.error("There's no such item with name §7" + args[0] + "§c.");
            return;
         }

         if (args.length > 1) {
            try {
               amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException | NullPointerException var11) {
               ChatUtils.error("§7" + args[1] + "§c is not a valid number.");
               return;
            }

            if (args.length > 2) {
               try {
                  metadata = Integer.parseInt(args[2]);
               } catch (NumberFormatException | NullPointerException var10) {
                  ChatUtils.error("§7" + args[2] + "§c is not a valid number.");
                  return;
               }

               if (args.length > 3) {
                  nbt = new StringBuilder(args[3]);

                  for(int i = 4; i < args.length; ++i) {
                     nbt.append(" ").append(args[i]);
                  }

                  nbt = new StringBuilder(nbt.toString().replace('&', '§').replace("§§", "&"));
               }
            }
         }

         ItemStack stack = new ItemStack(item, amount, metadata);
         if (nbt != null) {
            try {
               stack.func_77982_d(JsonToNBT.func_180713_a(nbt.toString()));
            } catch (NBTException var9) {
               ChatUtils.error("Data tag parsing failed: " + var9.getMessage());
               return;
            }
         }

         updateFirstEmptySlot(stack);
      } catch (Exception var12) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Gives items";
   }

   public static void updateFirstEmptySlot(ItemStack stack) {
      int slot = 0;
      boolean slotFound = false;

      int convertedSlot;
      for(convertedSlot = 0; convertedSlot < 36; ++convertedSlot) {
         if (mc.field_71439_g.field_71071_by.func_70301_a(convertedSlot).func_190926_b()) {
            slot = convertedSlot;
            slotFound = true;
            break;
         }
      }

      if (!slotFound) {
         ChatUtils.warning("Could not find empty slot. Operation has been aborted.");
      } else {
         convertedSlot = slot;
         if (slot < 9) {
            convertedSlot = slot + 36;
         }

         if (stack.func_190916_E() > 64) {
            ItemStack passStack = stack.func_77946_l();
            stack.func_190920_e(64);
            passStack.func_190920_e(passStack.func_190916_E() - 64);
            mc.field_71439_g.field_71071_by.func_70299_a(slot, stack);
            ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketCreativeInventoryAction(convertedSlot, stack));
            updateFirstEmptySlot(passStack);
         } else {
            ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketCreativeInventoryAction(convertedSlot, stack));
         }
      }
   }

   public String getSyntax() {
      return "Give <Id> <MetaData>";
   }
}
