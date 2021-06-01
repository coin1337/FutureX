package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

public class Lore extends Command {
   public Lore() {
      super("Lore");
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

         StringBuilder lore = new StringBuilder(args[0]);

         for(int i = 1; i < args.length; ++i) {
            lore.append(" ").append(args[i]);
         }

         lore = new StringBuilder(lore.toString().replace('&', '§').replace("§§", "&"));
         if (!mc.field_71439_g.func_184812_l_()) {
            ChatUtils.warning("You must be in creative mode.");
         }

         NBTTagCompound display;
         NBTTagList lores;
         if (stack.func_77942_o()) {
            assert stack.func_77978_p() != null;

            stack.func_77978_p().func_74775_l("display").func_74781_a("Lore");
            lores = (NBTTagList)stack.func_77978_p().func_74775_l("display").func_74781_a("Lore");
            lores.func_74742_a(new NBTTagString(lore.toString()));
            display = new NBTTagCompound();
            display.func_74782_a("Lore", lores);
            stack.func_77978_p().func_74775_l("display").func_179237_a(display);
         } else {
            lores = new NBTTagList();
            lores.func_74742_a(new NBTTagString(lore.toString()));
            display = new NBTTagCompound();
            display.func_74782_a("Lore", lores);
            stack.func_77983_a("display", display);
         }

         updateSlot(36 + mc.field_71439_g.field_71071_by.field_70461_c, stack);
         ChatUtils.message("Added lore §7" + lore + "§e to the item.");
      } catch (Exception var7) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public static void updateSlot(int slot, ItemStack stack) {
      ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketCreativeInventoryAction(slot, stack));
   }

   public String getDescription() {
      return "Adds Lore to and object";
   }

   public String getSyntax() {
      return "Lore <Lore>  ";
   }
}
