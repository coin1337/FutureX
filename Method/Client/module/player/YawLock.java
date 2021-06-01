package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.proxy.Overrides.PitchYawHelper;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class YawLock extends Module {
   float YawOld = 0.0F;
   boolean overshot;
   Setting auto;
   Setting slice;
   Setting yaw;
   Setting Silent;
   Setting Gradual;
   Setting Gradualamnt;
   Setting Wiggle;
   Setting Wiggleamnt;
   float NewYaw;

   public YawLock() {
      super("YawLock", 0, Category.PLAYER, "YawLock");
      this.auto = Main.setmgr.add(new Setting("auto", this, true));
      this.slice = Main.setmgr.add(new Setting("slice", this, 45.0D, 0.0D, 360.0D, true));
      this.yaw = Main.setmgr.add(new Setting("yaw", this, 0.0D, -360.0D, 360.0D, true));
      this.Silent = Main.setmgr.add(new Setting("Silent", this, true));
      this.Gradual = Main.setmgr.add(new Setting("Gradual", this, true));
      this.Gradualamnt = Main.setmgr.add(new Setting("Gradualamnt", this, 0.1D, 0.0D, 1.0D, false));
      this.Wiggle = Main.setmgr.add(new Setting("Wiggle", this, true));
      this.Wiggleamnt = Main.setmgr.add(new Setting("Wiggleamnt", this, 0.1D, 0.0D, 1.0D, false));
   }

   public void onEnable() {
      mc.field_71417_B = new PitchYawHelper();
      PitchYawHelper.Yaw = true;
      this.YawOld = mc.field_71439_g.field_70177_z;
      this.overshot = false;
      if (this.yaw.getValDouble() > 90.0D || this.yaw.getValDouble() < -90.0D) {
         ChatUtils.warning("Out of normal Range! Use Silent?");
      }

   }

   public void onDisable() {
      PitchYawHelper.Yaw = false;
   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      PitchYawHelper.Yaw = !this.Silent.getValBoolean();
      this.NewYaw = this.YawOld;
      if (this.Gradual.getValBoolean() && !this.overshot) {
         this.YawOld = (float)((double)this.YawOld + ((double)this.YawOld < this.yaw.getValDouble() ? this.Gradualamnt.getValDouble() : -this.Gradualamnt.getValDouble()));
         if ((double)this.NewYaw > this.yaw.getValDouble() && (double)this.YawOld < this.yaw.getValDouble() || (double)this.NewYaw < this.yaw.getValDouble() && (double)this.YawOld > this.yaw.getValDouble()) {
            this.YawOld = (float)this.yaw.getValDouble();
            this.overshot = true;
         }
      }

      if (this.overshot && this.yaw.getValDouble() != (double)this.YawOld) {
         this.overshot = false;
      }

      if (this.Wiggle.getValBoolean()) {
         this.NewYaw = (float)((double)this.NewYaw + this.Wiggleamnt.getValDouble() * (Math.random() > 0.5D ? Math.random() : -Math.random()));
      }

      if (!this.Silent.getValBoolean()) {
         if (this.slice.getValDouble() == 0.0D) {
            return;
         }

         if (this.auto.getValBoolean()) {
            int angle = (int)(360.0D / this.slice.getValDouble());
            float yaw = mc.field_71439_g.field_70177_z;
            yaw = (float)(Math.round(yaw / (float)angle) * angle);
            mc.field_71439_g.field_70177_z = yaw;
            if (mc.field_71439_g.func_184218_aH()) {
               ((Entity)Objects.requireNonNull(mc.field_71439_g.func_184187_bx())).field_70177_z = yaw;
            }
         } else {
            mc.field_71439_g.field_70177_z = this.NewYaw;
         }
      }

      mc.field_71439_g.field_70759_as = this.NewYaw;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && this.Silent.getValBoolean()) {
         if (packet instanceof Rotation) {
            Rotation p = (Rotation)packet;
            p.field_149476_e = this.NewYaw;
         }

         if (packet instanceof PositionRotation) {
            PositionRotation p = (PositionRotation)packet;
            p.field_149476_e = this.NewYaw;
         }
      }

      return true;
   }
}
