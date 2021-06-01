package Method.Client.module.misc;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Mouse;

public class EchestBP extends Module {
   private GuiScreen echestScreen = null;
   boolean EchestSet = false;
   boolean Tryrightclick = false;

   public EchestBP() {
      super("EchestBP", 0, Category.MISC, "EchestBP");
   }

   public void onEnable() {
      this.EchestSet = false;
      this.Tryrightclick = false;
      ChatUtils.message(ChatFormatting.AQUA + " Open an Echest to start!");
   }

   public void onDisable() {
      if (this.echestScreen != null) {
         mc.func_147108_a(this.echestScreen);
      }

      this.echestScreen = null;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof CPacketEntityAction) {
         CPacketEntityAction pac = (CPacketEntityAction)packet;
         if (pac.func_180764_b().equals(Action.OPEN_INVENTORY)) {
            return false;
         }
      }

      return !(packet instanceof CPacketCloseWindow);
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71462_r instanceof GuiInventory) {
         mc.field_71442_b.func_78765_e();
      }

      if (mc.field_71462_r instanceof GuiContainer) {
         if (((GuiContainer)mc.field_71462_r).field_147002_h instanceof ContainerChest) {
            Container inventorySlots = ((GuiContainer)mc.field_71462_r).field_147002_h;
            if (((ContainerChest)inventorySlots).func_85151_d() instanceof InventoryBasic && ((ContainerChest)inventorySlots).func_85151_d().func_70005_c_().equalsIgnoreCase("Ender Chest")) {
               if (!this.EchestSet) {
                  this.EchestSet = true;
                  mc.field_71439_g.func_71053_j();
               } else {
                  this.echestScreen = mc.field_71462_r;
                  mc.field_71462_r = null;
                  ChatUtils.message(ChatFormatting.AQUA + "Done! To open please disable EchestBP");
                  Mouse.setGrabbed(true);
               }
            }
         }
      } else if (this.EchestSet && !this.Tryrightclick) {
         this.Tryrightclick = true;
         mc.func_147121_ag();
      }

   }
}
