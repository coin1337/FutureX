package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import java.util.Iterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Pickupmod extends Module {
   Setting Fast;
   Setting Antipickup;
   Setting RemoveDrops;
   Setting LongRange;
   Setting Distance;

   public Pickupmod() {
      super("Pickupmod", 0, Category.MISC, "Pickup tools");
      this.Fast = Main.setmgr.add(this.Fast = new Setting("Fast", this, true));
      this.Antipickup = Main.setmgr.add(new Setting("Antipickup", this, false));
      this.RemoveDrops = Main.setmgr.add(new Setting("RemoveDrops", this, false));
      this.LongRange = Main.setmgr.add(new Setting("LongRange", this, true));
      this.Distance = Main.setmgr.add(new Setting("Distance", this, 4.0D, 0.0D, 10.0D, false));
   }

   public void onClientTick(ClientTickEvent event) {
      Iterator var2 = mc.field_71441_e.func_72872_a(EntityItem.class, mc.field_71439_g.func_174813_aQ().func_72314_b(this.Distance.getValDouble(), this.Distance.getValDouble(), this.Distance.getValDouble())).iterator();

      while(var2.hasNext()) {
         EntityItem entityItem = (EntityItem)var2.next();
         if (this.Antipickup.getValBoolean()) {
            if (entityItem.field_70173_aa > 30) {
               entityItem.field_70173_aa = 0;
               Wrapper.INSTANCE.sendPacket(new Position(entityItem.field_70165_t, entityItem.field_70163_u + 2.0D, entityItem.field_70161_v, false));
            }

            entityItem.field_145802_g = "NULL";
            entityItem.field_70132_H = false;
            entityItem.field_70176_ah = 0;
            entityItem.field_70162_ai = 0;
            entityItem.field_70164_aj = 0;
            entityItem.field_71093_bK = 57;
            entityItem.lifespan = -1;
            entityItem.field_70123_F = false;
            entityItem.field_70124_G = false;
         }

         if (this.LongRange.getValBoolean() && entityItem.field_70173_aa > 30) {
            entityItem.field_70173_aa = 10;
            Wrapper.INSTANCE.sendPacket(new Position(entityItem.field_70165_t, entityItem.field_70163_u, entityItem.field_70161_v, mc.field_71439_g.field_70122_E));
         }

         if (this.RemoveDrops.getValBoolean()) {
            entityItem.func_70106_y();
            entityItem.onRemovedFromWorld();
         }

         if (this.Fast.getValBoolean()) {
            entityItem.field_70173_aa = 45;
            entityItem.func_174868_q();
            entityItem.field_70123_F = true;
            entityItem.field_70124_G = true;
            entityItem.field_70132_H = true;
         }
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (!this.Antipickup.getValBoolean()) {
         return true;
      } else {
         return !(packet instanceof ItemPickupEvent) && !(packet instanceof EntityItemPickupEvent) && !(packet instanceof SPacketCollectItem);
      }
   }

   public void onItemPickup(EntityItemPickupEvent event) {
      if (this.Antipickup.getValBoolean()) {
         event.setCanceled(true);
      }

   }
}
