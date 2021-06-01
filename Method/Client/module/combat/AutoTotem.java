package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import java.util.OptionalInt;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoTotem extends Module {
   private final int OFFHAND_SLOT = 45;
   Setting allowGui;
   Setting health;
   Setting needheal;
   Setting predict;
   private boolean predicted;

   public AutoTotem() {
      super("Auto Totem", 0, Category.COMBAT, "Auto Totem");
      this.allowGui = Main.setmgr.add(new Setting("Use in Gui", this, true));
      this.health = Main.setmgr.add(new Setting("health for switch", this, 10.0D, 0.0D, 35.0D, true));
      this.needheal = Main.setmgr.add(new Setting("Use Health", this, false));
      this.predict = Main.setmgr.add(new Setting("Predict", this, false));
      this.predicted = false;
   }

   @SubscribeEvent
   public void onLivingUpdate(LivingUpdateEvent event) {
      if (getOffhand().func_77973_b() != Items.field_190929_cY) {
         if (mc.field_71462_r == null || this.allowGui.getValBoolean()) {
            if (this.needheal.getValBoolean()) {
               if (!((double)mc.field_71439_g.func_110143_aJ() >= this.health.getValDouble())) {
                  findItem().ifPresent((slot) -> {
                     this.invPickup(slot);
                     this.invPickup(45);
                  });
               }
            } else if (this.predict.getValBoolean()) {
               if (this.predicted) {
                  findItem().ifPresent((slot) -> {
                     this.invPickup(slot);
                     this.invPickup(45);
                  });
               }
            } else {
               findItem().ifPresent((slot) -> {
                  this.invPickup(slot);
                  this.invPickup(45);
               });
            }

            if (this.predicted) {
               ChatUtils.warning("Predicted Totem!");
               this.predicted = false;
            }

         }
      }
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.predict.getValBoolean() && side == Connection.Side.IN && packet instanceof SPacketUpdateHealth) {
         SPacketUpdateHealth packet2 = (SPacketUpdateHealth)packet;
         if (packet2.func_149332_c() <= 0.0F) {
            this.predicted = true;
         }
      }

      return true;
   }

   private void invPickup(int slot) {
      MC.field_71442_b.func_187098_a(0, slot, 0, ClickType.PICKUP, MC.field_71439_g);
   }

   private static OptionalInt findItem() {
      for(int i = 9; i <= 44; ++i) {
         if (MC.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c().func_77973_b() == Items.field_190929_cY) {
            return OptionalInt.of(i);
         }
      }

      return OptionalInt.empty();
   }

   private static ItemStack getOffhand() {
      return MC.field_71439_g.func_184582_a(EntityEquipmentSlot.OFFHAND);
   }
}
