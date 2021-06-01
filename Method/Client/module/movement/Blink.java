package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Entity301;
import Method.Client.utils.system.Connection;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;

public class Blink extends Module {
   public Entity301 entity301 = null;
   Setting Limit;
   Setting Renable;
   Setting ShowPos;
   Setting Entity;
   int limitcount;

   public Blink() {
      super("Blink", 0, Category.MOVEMENT, "Cancel packets and move");
      this.Limit = Main.setmgr.add(new Setting("Packet limit", this, 0.0D, 0.0D, 500.0D, true));
      this.Renable = Main.setmgr.add(new Setting("Renable", this, false));
      this.ShowPos = Main.setmgr.add(new Setting("ShowPos", this, true));
      this.Entity = Main.setmgr.add(new Setting("Entity", this, true));
      this.limitcount = 0;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && packet instanceof CPacketVehicleMove && this.Entity.getValBoolean()) {
         ++this.limitcount;
         if (this.Limit.getValDouble() == 0.0D) {
            return false;
         }

         if (this.Limit.getValDouble() < (double)this.limitcount) {
            this.limitcount = 0;
            this.onoff();
            return true;
         }
      }

      if (side == Connection.Side.OUT && packet instanceof CPacketPlayer) {
         ++this.limitcount;
         if (this.Limit.getValDouble() == 0.0D) {
            return false;
         }

         if (this.Limit.getValDouble() < (double)this.limitcount) {
            this.limitcount = 0;
            this.onoff();
            return true;
         }
      }

      return true;
   }

   public void onEnable() {
      this.Enable();
      super.onEnable();
   }

   public void onDisable() {
      this.limitcount = 0;
      if (this.entity301 != null && mc.field_71441_e != null) {
         mc.field_71441_e.func_72900_e(this.entity301);
         this.entity301 = null;
      }

      super.onDisable();
   }

   private void onoff() {
      this.limitcount = 0;
      if (this.entity301 != null && mc.field_71441_e != null) {
         mc.field_71441_e.func_72900_e(this.entity301);
         this.entity301 = null;
      }

      if (this.Renable.getValBoolean()) {
         this.Enable();
      }

   }

   private void Enable() {
      this.limitcount = 0;
      if (mc.field_71439_g != null && mc.field_71441_e != null && this.ShowPos.getValBoolean()) {
         this.entity301 = new Entity301(mc.field_71441_e, mc.field_71439_g.func_146103_bH());
         this.entity301.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
         this.entity301.field_71071_by = mc.field_71439_g.field_71071_by;
         this.entity301.field_70125_A = mc.field_71439_g.field_70125_A;
         this.entity301.field_70177_z = mc.field_71439_g.field_70177_z;
         this.entity301.field_70759_as = mc.field_71439_g.field_70759_as;
         mc.field_71441_e.func_72838_d(this.entity301);
      }

   }
}
