package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.proxy.Overrides.EntityRenderMixin;
import Method.Client.utils.system.Connection;
import java.util.Objects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class NoEffect extends Module {
   Setting hurtcam;
   Setting Levitate;
   Setting weather;
   Setting Time;
   Setting Settime;
   Setting fire;
   Setting push;
   Setting NoVoid;
   public static Setting NoScreenEvents;

   public NoEffect() {
      super("NoEffect", 0, Category.PLAYER, "Prevent effects such as weather");
      this.hurtcam = Main.setmgr.add(new Setting("hurtcam", this, false));
      this.Levitate = Main.setmgr.add(new Setting("Levitate", this, false));
      this.weather = Main.setmgr.add(new Setting("weather", this, false));
      this.Time = Main.setmgr.add(new Setting("Time", this, 0.0D, 0.0D, 18000.0D, true));
      this.Settime = Main.setmgr.add(new Setting("Settime", this, false));
      this.fire = Main.setmgr.add(new Setting("fire", this, false));
      this.push = Main.setmgr.add(new Setting("push", this, false));
      this.NoVoid = Main.setmgr.add(new Setting("NoVoid", this, false));
   }

   public void setup() {
      Main.setmgr.add(NoScreenEvents = new Setting("NoScreenEvents", this, false));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      return !(packet instanceof SPacketTimeUpdate) || !this.Settime.getValBoolean();
   }

   public void onClientTick(ClientTickEvent event) {
      EntityRenderMixin.Camswitch = !this.hurtcam.getValBoolean();
      if (this.weather.getValBoolean()) {
         mc.field_71441_e.func_72912_H().func_76084_b(false);
         mc.field_71441_e.func_72894_k(0.0F);
         mc.field_71441_e.func_72912_H().func_76090_f(0);
         mc.field_71441_e.func_72912_H().func_76069_a(false);
      }

      if (this.push.getValBoolean()) {
         mc.field_71439_g.field_70144_Y = 1.0F;
      }

      if (this.Levitate.getValBoolean() && mc.field_71439_g.func_70644_a((Potion)Objects.requireNonNull(Potion.func_188412_a(25)))) {
         mc.field_71439_g.func_184596_c(Potion.func_188412_a(25));
      }

      if (this.NoVoid.getValBoolean()) {
         if (mc.field_71439_g.field_70163_u <= 0.5D) {
            mc.field_71439_g.field_70701_bs = 10.0F;
            mc.field_71439_g.func_70664_aZ();
         }

         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_70181_x += 0.1D;
      }

   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      if (mc.field_71439_g.func_70027_ad() && this.fire.getValBoolean()) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayer(mc.field_71439_g.field_70122_E));
         event.getEntityLiving().func_70066_B();
         mc.field_71439_g.func_70015_d(0);
      }

      if (this.Settime.getValBoolean()) {
         mc.field_71441_e.func_72877_b((long)this.Time.getValDouble());
      }

   }
}
