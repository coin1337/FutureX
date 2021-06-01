package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Offhand extends Module {
   Setting switch_mode;
   Setting totem_switch;
   Setting gapple_in_hole;
   Setting gapple_hole_hp;
   Setting delay;
   boolean switching;
   int last_slot;

   public Offhand() {
      super("Offhand", 0, Category.COMBAT, "Offhand Item");
      this.switch_mode = Main.setmgr.add(new Setting("switch_mode", this, "Totem", new String[]{"Totem", "Crystal", "Gapple"}));
      this.totem_switch = Main.setmgr.add(new Setting("totem_switch", this, 16.0D, 0.0D, 36.0D, true));
      this.gapple_in_hole = Main.setmgr.add(new Setting("gapple_in_hole", this, true));
      this.gapple_hole_hp = Main.setmgr.add(new Setting("gapple_hole_hp", this, 8.0D, 0.0D, 36.0D, true));
      this.delay = Main.setmgr.add(new Setting("delay", this, true));
      this.switching = false;
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71462_r == null || mc.field_71462_r instanceof GuiInventory) {
         if (this.switching) {
            this.swap_items(this.last_slot, 2);
            return;
         }

         float hp = mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj();
         if (!((double)hp > this.totem_switch.getValDouble())) {
            this.swap_items(this.get_item_slot(Items.field_190929_cY), this.delay.getValBoolean() ? 1 : 0);
            return;
         }

         if (this.switch_mode.getValString().equalsIgnoreCase("Crystal") && ModuleManager.getModuleByName("CrystalAura").isToggled()) {
            this.swap_items(this.get_item_slot(Items.field_185158_cP), 0);
            return;
         }

         if (this.gapple_in_hole.getValBoolean() && (double)hp > this.gapple_hole_hp.getValDouble() && this.is_in_hole()) {
            this.swap_items(this.get_item_slot(Items.field_151153_ao), this.delay.getValBoolean() ? 1 : 0);
            return;
         }

         if (this.switch_mode.getValString().equalsIgnoreCase("Totem")) {
            this.swap_items(this.get_item_slot(Items.field_190929_cY), this.delay.getValBoolean() ? 1 : 0);
            return;
         }

         if (this.switch_mode.getValString().equalsIgnoreCase("Gapple")) {
            this.swap_items(this.get_item_slot(Items.field_151153_ao), this.delay.getValBoolean() ? 1 : 0);
            return;
         }

         if (this.switch_mode.getValString().equalsIgnoreCase("Crystal") && !ModuleManager.getModuleByName("CrystalAura").isToggled()) {
            this.swap_items(this.get_item_slot(Items.field_190929_cY), 0);
            return;
         }

         if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190931_a) {
            this.swap_items(this.get_item_slot(Items.field_190929_cY), this.delay.getValBoolean() ? 1 : 0);
         }
      }

   }

   public void swap_items(int slot, int step) {
      if (slot != -1) {
         if (step == 0) {
            mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, mc.field_71439_g);
            mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, mc.field_71439_g);
            mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, mc.field_71439_g);
         }

         if (step == 1) {
            mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, mc.field_71439_g);
            this.switching = true;
            this.last_slot = slot;
         }

         if (step == 2) {
            mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, mc.field_71439_g);
            mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, mc.field_71439_g);
            this.switching = false;
         }

         mc.field_71442_b.func_78765_e();
      }
   }

   private boolean is_in_hole() {
      BlockPos player_block = new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.floor(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v));
      return mc.field_71441_e.func_180495_p(player_block.func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block.func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block.func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block.func_177968_d()).func_177230_c() != Blocks.field_150350_a;
   }

   private int get_item_slot(Item input) {
      if (input == mc.field_71439_g.func_184592_cb().func_77973_b()) {
         return -1;
      } else {
         for(int i = 36; i >= 0; --i) {
            Item item = mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (item == input) {
               if (i < 9) {
                  if (input == Items.field_151153_ao) {
                     return -1;
                  }

                  i += 36;
               }

               return i;
            }
         }

         return -1;
      }
   }
}
