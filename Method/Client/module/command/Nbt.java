package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class Nbt extends Command {
   public Nbt() {
      super("Nbt");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (args.length < 1) {
            ChatUtils.error("Invalid syntax.");
            return;
         }

         ItemStack stack = mc.field_71439_g.field_71071_by.func_70448_g();
         if (stack.func_190926_b()) {
            ChatUtils.error("You must hold an item in your hand.");
            return;
         }

         String var4 = args[0];
         byte var5 = -1;
         switch(var4.hashCode()) {
         case -934610812:
            if (var4.equals("remove")) {
               var5 = 2;
            }
            break;
         case 96417:
            if (var4.equals("add")) {
               var5 = 0;
            }
            break;
         case 113762:
            if (var4.equals("set")) {
               var5 = 1;
            }
            break;
         case 3059573:
            if (var4.equals("copy")) {
               var5 = 4;
            }
            break;
         case 94746189:
            if (var4.equals("clear")) {
               var5 = 3;
            }
         }

         StringBuilder nbt;
         int i;
         switch(var5) {
         case 0:
            if (!mc.field_71439_g.func_184812_l_()) {
               ChatUtils.warning("You must be in creative mode.");
            }

            if (args.length < 2) {
               ChatUtils.error("No NBT data provided.");
               return;
            }

            nbt = new StringBuilder(args[1]);

            for(i = 2; i < args.length; ++i) {
               nbt.append(" ").append(args[i]);
            }

            nbt = new StringBuilder(nbt.toString().replace('&', '§').replace("§§", "&"));

            try {
               if (!stack.func_77942_o()) {
                  stack.func_77982_d(JsonToNBT.func_180713_a(nbt.toString()));
               } else {
                  assert stack.func_77978_p() != null;

                  stack.func_77978_p().func_179237_a(JsonToNBT.func_180713_a(nbt.toString()));
               }

               updateSlot(36 + mc.field_71439_g.field_71071_by.field_70461_c, stack);
               ChatUtils.message("Item modified.");
            } catch (NBTException var10) {
               ChatUtils.error("Data tag parsing failed: " + var10.getMessage());
            }
            break;
         case 1:
            if (!mc.field_71439_g.func_184812_l_()) {
               ChatUtils.warning("You must be in creative mode.");
            }

            if (args.length < 2) {
               ChatUtils.error("No NBT data provided.");
               return;
            }

            nbt = new StringBuilder(args[1]);

            for(i = 2; i < args.length; ++i) {
               nbt.append(" ").append(args[i]);
            }

            nbt = new StringBuilder(nbt.toString().replace('&', '§').replace("§§", "&"));

            try {
               stack.func_77982_d(JsonToNBT.func_180713_a(nbt.toString()));
            } catch (NBTException var9) {
               ChatUtils.error("Data tag parsing failed: " + var9.getMessage());
               return;
            }

            updateSlot(36 + mc.field_71439_g.field_71071_by.field_70461_c, stack);
            ChatUtils.message("Item modified.");
            break;
         case 2:
            if (!mc.field_71439_g.func_184812_l_()) {
               ChatUtils.warning("You must be in creative mode.");
            }

            if (args.length < 2) {
               ChatUtils.error("No NBT tag specified.");
               return;
            }

            if (args.length > 2) {
               ChatUtils.warning("Too many arguments.");
            }

            String tag = args[1];
            if (stack.func_77942_o() && ((NBTTagCompound)Objects.requireNonNull(stack.func_77978_p())).func_74764_b(tag)) {
               stack.func_77978_p().func_82580_o(tag);
               if (stack.func_77978_p().func_82582_d()) {
                  stack.func_77982_d((NBTTagCompound)null);
               }

               updateSlot(36 + mc.field_71439_g.field_71071_by.field_70461_c, stack);
               ChatUtils.message("Item modified.");
               break;
            }

            ChatUtils.error("Item has no NBT tag with name §7" + args[1] + "§c.");
            return;
         case 3:
            if (!mc.field_71439_g.func_184812_l_()) {
               ChatUtils.warning("You must be in creative mode.");
            }

            if (args.length > 1) {
               ChatUtils.warning("Too many arguments.");
            }

            if (!stack.func_77942_o()) {
               ChatUtils.error("Item has no NBT data.");
               return;
            }

            stack.func_77982_d((NBTTagCompound)null);
            updateSlot(36 + mc.field_71439_g.field_71071_by.field_70461_c, stack);
            ChatUtils.message("Cleared item's NBT data.");
            break;
         case 4:
            if (args.length > 1) {
               ChatUtils.warning("Too many arguments.");
            }

            if (!stack.func_77942_o()) {
               ChatUtils.error("Item has no NBT data.");
               return;
            }

            assert stack.func_77978_p() != null;

            StringSelection selection = new StringSelection(stack.func_77978_p().toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            ChatUtils.message("Copied item's NBT data to clipboard.");
         }
      } catch (Exception var11) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public static void updateSlot(int slot, ItemStack stack) {
      ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketCreativeInventoryAction(slot, stack));
   }

   public String getDescription() {
      return "Modifies held item's NBT data.";
   }

   public String getSyntax() {
      return "nbt <add <dataTag>|set <dataTag>|remove <tagName>|clear|copy>";
   }
}
