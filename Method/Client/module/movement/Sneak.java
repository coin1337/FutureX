package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Sneak extends Module {
   Setting mode;
   Setting Antisneak;
   Setting fullSprint;

   public Sneak() {
      super("Sneak", 0, Category.MOVEMENT, "Sneak");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Legit", new String[]{"Legit", "Packet"}));
      this.Antisneak = Main.setmgr.add(new Setting("Antisneak", this, false));
      this.fullSprint = Main.setmgr.add(new Setting("FullSprint", this, false, this.Antisneak, 2));
   }

   public void onDisable() {
      if (this.mode.getValString().equalsIgnoreCase("legit")) {
         mc.field_71474_y.field_74311_E.field_74513_e = false;
      }

      if (this.mode.getValString().equalsIgnoreCase("Packet")) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
      }

   }

   public void onClientTick(ClientTickEvent event) {
      EntityPlayerSP player;
      if (this.Antisneak.getValBoolean()) {
         player = mc.field_71439_g;
         GameSettings settings = Wrapper.INSTANCE.mcSettings();
         if (player.field_70122_E && settings.field_74311_E.func_151470_d()) {
            if (!this.fullSprint.getValBoolean() && settings.field_74351_w.func_151470_d()) {
               player.func_70031_b(Utils.isMoving(player));
            } else if (this.fullSprint.getValBoolean()) {
               player.func_70031_b(Utils.isMoving(player));
            }

            if (!settings.field_74366_z.func_151470_d() && !settings.field_74370_x.func_151470_d() && !settings.field_74368_y.func_151470_d()) {
               player.field_70159_w *= 1.2848D;
               player.field_70179_y *= 1.2848D;
            } else if (settings.field_74368_y.func_151470_d()) {
               player.field_70159_w *= 1.268D;
               player.field_70179_y *= 1.268D;
            } else {
               player.field_70159_w *= 1.252D;
               player.field_70179_y *= 1.252D;
            }
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Legit")) {
         mc.field_71474_y.field_74311_E.field_74513_e = true;
      }

      if (this.mode.getValString().equalsIgnoreCase("Packet")) {
         player = mc.field_71439_g;
         if (!Utils.isMoving(mc.field_71439_g)) {
            player.field_71174_a.func_147297_a(new CPacketEntityAction(player, Action.START_SNEAKING));
            player.field_71174_a.func_147297_a(new CPacketEntityAction(player, Action.STOP_SNEAKING));
         }

         if (Utils.isMoving(mc.field_71439_g)) {
            player.field_71174_a.func_147297_a(new CPacketEntityAction(player, Action.STOP_SNEAKING));
            player.field_71174_a.func_147297_a(new CPacketEntityAction(player, Action.START_SNEAKING));
         }
      }

      super.onClientTick(event);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.Antisneak.getValBoolean() && side == Connection.Side.OUT && packet instanceof CPacketEntityAction) {
         CPacketEntityAction p = (CPacketEntityAction)packet;
         return p.func_180764_b() != Action.STOP_SNEAKING;
      } else {
         return true;
      }
   }
}
