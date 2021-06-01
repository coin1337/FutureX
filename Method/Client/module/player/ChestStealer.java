package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.utils.TimerUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ChestStealer extends Module {
   private final TimerUtils Timer = new TimerUtils();
   Setting delay;
   Setting Entity;
   Setting Shulker;
   public static Setting Mode;

   public ChestStealer() {
      super("ChestStealer", 0, Category.PLAYER, "ChestStealer");
   }

   public void setup() {
      Main.setmgr.add(Mode = new Setting("Mode", this, "Steal", new String[]{"Steal", "Store", "Drop"}));
      Main.setmgr.add(this.delay = new Setting("Delay", this, 3.0D, 0.0D, 20.0D, true));
      Main.setmgr.add(this.Shulker = new Setting("Take Shulker", this, false));
      Main.setmgr.add(this.Entity = new Setting("Entitys Chest", this, true));
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.Timer.isDelay((long)(this.delay.getValDouble() * 100.0D))) {
         this.Timer.setLastMS();
         if (mc.field_71462_r instanceof GuiChest) {
            GuiChest guiChest = (GuiChest)mc.field_71462_r;
            this.Quickhandle(guiChest, guiChest.field_147015_w.func_70302_i_());
         } else if (mc.field_71462_r instanceof GuiScreenHorseInventory && this.Entity.getValBoolean()) {
            GuiScreenHorseInventory horseInventory = (GuiScreenHorseInventory)mc.field_71462_r;
            this.Quickhandle(horseInventory, horseInventory.field_147029_w.func_70302_i_());
         } else if (mc.field_71462_r instanceof GuiShulkerBox && this.Shulker.getValBoolean()) {
            GuiShulkerBox shulkerBox = (GuiShulkerBox)mc.field_71462_r;
            this.Quickhandle((GuiShulkerBox)mc.field_71462_r, shulkerBox.field_190779_v.func_70302_i_());
         } else {
            ModuleManager.getModuleByName("ChestStealer").toggle();
         }

      }
   }

   private void Quickhandle(GuiContainer guiContainer, int size) {
      for(int i = 0; i < size; ++i) {
         ItemStack stack = (ItemStack)guiContainer.field_147002_h.func_75138_a().get(i);
         if ((stack.func_190926_b() || stack.func_77973_b() == Items.field_190931_a) && Mode.getValString().equalsIgnoreCase("Store")) {
            this.HandleStoring(guiContainer.field_147002_h.field_75152_c, size - 9);
            return;
         }

         if (this.StealorDrop(guiContainer.field_147002_h.field_75152_c, i, stack)) {
            break;
         }
      }

   }

   private void HandleStoring(int pWindowId, int stack) {
      for(int i = 9; i < mc.field_71439_g.field_71069_bz.field_75151_b.size() - 1; ++i) {
         ItemStack itemStack = mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
         if (!itemStack.func_190926_b() && itemStack.func_77973_b() != Items.field_190931_a && (!this.Shulker.getValBoolean() || itemStack.func_77973_b() instanceof ItemShulkerBox)) {
            mc.field_71442_b.func_187098_a(pWindowId, i + stack, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
            return;
         }
      }

   }

   private boolean StealorDrop(int windowId, int i, ItemStack stack) {
      if (stack.func_190926_b() || this.Shulker.getValBoolean() && !(stack.func_77973_b() instanceof ItemShulkerBox)) {
         return false;
      } else if (Mode.getValString().equalsIgnoreCase("Steal")) {
         mc.field_71442_b.func_187098_a(windowId, i, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
         return true;
      } else if (Mode.getValString().equalsIgnoreCase("Drop")) {
         mc.field_71442_b.func_187098_a(windowId, i, 1, ClickType.THROW, mc.field_71439_g);
         return true;
      } else {
         return false;
      }
   }
}
