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

public class PitchLock extends Module {
   float PitchOLD = 0.0F;
   boolean overshot;
   Setting auto;
   Setting slice;
   Setting pitch;
   Setting Gradual;
   Setting Gradualamnt;
   Setting Wiggle;
   Setting Wiggleamnt;
   Setting Silent;
   float NewPitch;

   public PitchLock() {
      super("PitchLock", 0, Category.PLAYER, "PitchLock");
      this.auto = Main.setmgr.add(new Setting("Auto Snap", this, true));
      this.slice = Main.setmgr.add(new Setting("Auto Slice", this, 45.0D, 0.0D, 360.0D, true, this.auto, 1));
      this.pitch = Main.setmgr.add(new Setting("pitch", this, 0.0D, -180.0D, 180.0D, true));
      this.Gradual = Main.setmgr.add(new Setting("Gradual", this, true));
      this.Gradualamnt = Main.setmgr.add(new Setting("Gradualamnt", this, 0.1D, 0.0D, 1.0D, false));
      this.Wiggle = Main.setmgr.add(new Setting("Wiggle", this, true));
      this.Wiggleamnt = Main.setmgr.add(new Setting("Wiggleamnt", this, 0.1D, 0.0D, 1.0D, false, this.Wiggle, 5));
      this.Silent = Main.setmgr.add(new Setting("Silent", this, true));
   }

   public void onEnable() {
      mc.field_71417_B = new PitchYawHelper();
      PitchYawHelper.Pitch = true;
      this.PitchOLD = mc.field_71439_g.field_70125_A;
      this.overshot = false;
      if (this.pitch.getValDouble() > 90.0D || this.pitch.getValDouble() < -90.0D) {
         ChatUtils.warning("Out of normal Range! Use Silent?");
      }

   }

   public void onDisable() {
      PitchYawHelper.Pitch = false;
   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      PitchYawHelper.Pitch = !this.Silent.getValBoolean();
      this.NewPitch = this.PitchOLD;
      if (this.Gradual.getValBoolean() && !this.overshot) {
         this.PitchOLD = (float)((double)this.PitchOLD + ((double)this.PitchOLD < this.pitch.getValDouble() ? this.Gradualamnt.getValDouble() : -this.Gradualamnt.getValDouble()));
         if ((double)this.NewPitch > this.pitch.getValDouble() && (double)this.PitchOLD < this.pitch.getValDouble() || (double)this.NewPitch < this.pitch.getValDouble() && (double)this.PitchOLD > this.pitch.getValDouble()) {
            this.PitchOLD = (float)this.pitch.getValDouble();
            this.overshot = true;
         }
      }

      if (this.overshot && this.pitch.getValDouble() != (double)this.PitchOLD) {
         this.overshot = false;
      }

      if (!this.Gradual.getValBoolean()) {
         this.NewPitch = (float)this.pitch.getValDouble();
      }

      if (this.Wiggle.getValBoolean()) {
         this.NewPitch = (float)((double)this.NewPitch + this.Wiggleamnt.getValDouble() * (Math.random() > 0.5D ? Math.random() : -Math.random()));
      }

      if (!this.Silent.getValBoolean()) {
         if (this.slice.getValDouble() == 0.0D) {
            return;
         }

         if (this.auto.getValBoolean()) {
            int angle = (int)(360.0D / this.slice.getValDouble());
            float yaw = mc.field_71439_g.field_70125_A;
            yaw = (float)(Math.round(yaw / (float)angle) * angle);
            mc.field_71439_g.field_70125_A = yaw;
            if (mc.field_71439_g.func_184218_aH()) {
               ((Entity)Objects.requireNonNull(mc.field_71439_g.func_184187_bx())).field_70125_A = yaw;
            }
         } else {
            mc.field_71439_g.field_70125_A = this.NewPitch;
         }
      }

      mc.field_71439_g.field_70761_aq = this.NewPitch;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && this.Silent.getValBoolean()) {
         if (packet instanceof Rotation) {
            Rotation p = (Rotation)packet;
            p.field_149473_f = this.NewPitch;
         }

         if (packet instanceof PositionRotation) {
            PositionRotation p = (PositionRotation)packet;
            p.field_149473_f = this.NewPitch;
         }
      }

      return true;
   }
}
