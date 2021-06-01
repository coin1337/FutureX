package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Entity301;
import Method.Client.utils.Utils;
import Method.Client.utils.Patcher.Events.SetOpaqueCubeEvent;
import Method.Client.utils.system.Connection;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class FreeCam extends Module {
   public Entity301 entity301 = null;
   Setting ShowPlayer;
   Setting Speed;
   Setting Tp;

   public FreeCam() {
      super("FreeCam", 0, Category.PLAYER, "FreeCam");
      this.ShowPlayer = Main.setmgr.add(new Setting("ShowPlayer", this, true));
      this.Speed = Main.setmgr.add(new Setting("Speed", this, 1.0D, 0.0D, 3.0D, false));
      this.Tp = Main.setmgr.add(new Setting("Tp", this, false));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.IN && packet instanceof SPacketPlayerPosLook) {
         return false;
      } else {
         return side != Connection.Side.OUT || !(packet instanceof CPacketPlayer);
      }
   }

   public void onEnable() {
      if (mc.field_71439_g != null && mc.field_71441_e != null && !this.Tp.getValBoolean()) {
         this.entity301 = new Entity301(mc.field_71441_e, mc.field_71439_g.func_146103_bH());
         this.entity301.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
         this.entity301.field_71071_by = mc.field_71439_g.field_71071_by;
         this.entity301.field_70125_A = mc.field_71439_g.field_70125_A;
         this.entity301.field_70177_z = mc.field_71439_g.field_70177_z;
         this.entity301.field_70759_as = mc.field_71439_g.field_70759_as;
         if (this.ShowPlayer.getValBoolean()) {
            mc.field_71441_e.func_72838_d(this.entity301);
         }
      }

      super.onEnable();
   }

   public void onDisable() {
      if (this.entity301 != null && mc.field_71441_e != null) {
         mc.field_71439_g.func_70107_b(this.entity301.field_70165_t, this.entity301.field_70163_u, this.entity301.field_70161_v);
         mc.field_71439_g.field_70125_A = this.entity301.field_70125_A;
         mc.field_71439_g.field_70177_z = this.entity301.field_70177_z;
         mc.field_71439_g.field_70759_as = this.entity301.field_70759_as;
         mc.field_71441_e.func_72900_e(this.entity301);
         this.entity301 = null;
      }

      mc.field_71439_g.field_70145_X = false;
      super.onDisable();
   }

   public void SetOpaqueCubeEvent(SetOpaqueCubeEvent event) {
      event.setCanceled(true);
   }

   public void onPlayerTick(PlayerTickEvent event) {
      if (mc.field_71439_g.field_70725_aQ <= 0 && !(mc.field_71439_g.func_110143_aJ() <= 0.0F)) {
         EntityPlayerSP player = mc.field_71439_g;
         player.field_71075_bZ.field_75100_b = false;
         player.field_70181_x = 0.0D;
         player.field_70179_y = 0.0D;
         player.field_70159_w = 0.0D;
         double[] directionSpeedVanilla = Utils.directionSpeed(this.Speed.getValDouble());
         player.field_70747_aH = (float)this.Speed.getValDouble();
         if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
            EntityPlayerSP var10000 = mc.field_71439_g;
            var10000.field_70159_w += directionSpeedVanilla[0];
            var10000 = mc.field_71439_g;
            var10000.field_70179_y += directionSpeedVanilla[1];
         }

         if (mc.field_71474_y.field_74314_A.func_151470_d()) {
            player.field_70181_x += this.Speed.getValDouble();
         }

         if (mc.field_71474_y.field_74311_E.func_151470_d()) {
            player.field_70181_x -= this.Speed.getValDouble();
         }

      } else {
         this.toggle();
      }
   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      mc.field_71439_g.field_70181_x = 0.0D;
      EntityPlayerSP var10000;
      if (mc.field_71474_y.field_74314_A.func_151470_d()) {
         var10000 = mc.field_71439_g;
         var10000.field_70181_x += this.Speed.getValDouble();
      }

      if (mc.field_71474_y.field_74311_E.func_151470_d()) {
         var10000 = mc.field_71439_g;
         var10000.field_70181_x -= this.Speed.getValDouble();
      }

      mc.field_71439_g.field_70145_X = true;
      super.onLivingUpdate(event);
   }
}
