package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import java.util.Arrays;
import java.util.OptionalInt;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class EntityVanish extends Module {
   Setting noDismountPlugin;
   Setting dismountEntity;
   Setting removeEntity;
   Setting respawnEntity;
   Setting sendMovePackets;
   Setting forceOnGround;
   Setting setMountPosition;
   private Entity originalRidingEntity;

   public EntityVanish() {
      super("EntityVanish", 0, Category.MOVEMENT, "Entity Vanish");
      this.noDismountPlugin = Main.setmgr.add(new Setting("no Dismount Plugin", this, true));
      this.dismountEntity = Main.setmgr.add(new Setting("dismoun tEntity", this, true));
      this.removeEntity = Main.setmgr.add(new Setting("remove Entity", this, true));
      this.respawnEntity = Main.setmgr.add(new Setting("respawn Entity", this, true));
      this.sendMovePackets = Main.setmgr.add(new Setting("send Move Packets", this, true));
      this.forceOnGround = Main.setmgr.add(new Setting("force On Ground", this, true));
      this.setMountPosition = Main.setmgr.add(new Setting("set MountPosition", this, true));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.IN) {
         if (packet instanceof SPacketSetPassengers && this.hasOriginalRidingEntity() && Minecraft.func_71410_x().field_71441_e != null) {
            SPacketSetPassengers packetSetPassengers = (SPacketSetPassengers)packet;
            if (this.originalRidingEntity.equals(Minecraft.func_71410_x().field_71441_e.func_73045_a(packetSetPassengers.func_186972_b()))) {
               OptionalInt isPlayerAPassenger = Arrays.stream(packetSetPassengers.func_186971_a()).filter((value) -> {
                  return Minecraft.func_71410_x().field_71441_e.func_73045_a(value) == Minecraft.func_71410_x().field_71439_g;
               }).findAny();
               if (!isPlayerAPassenger.isPresent()) {
                  ChatUtils.message("You Have Been Dismounted.");
                  this.toggle();
               }
            }
         }

         if (packet instanceof SPacketDestroyEntities) {
            SPacketDestroyEntities packetDestroyEntities = (SPacketDestroyEntities)packet;
            boolean isEntityNull = Arrays.stream(packetDestroyEntities.func_149098_c()).filter((value) -> {
               return value == this.originalRidingEntity.func_145782_y();
            }).findAny().isPresent();
            if (isEntityNull) {
               ChatUtils.message("Your riding entity has been destroyed.");
            }
         }
      }

      if (side == Connection.Side.OUT && this.noDismountPlugin.getValBoolean()) {
         if (packet instanceof Position) {
            Position cPacketPlayer = (Position)packet;
            Minecraft.func_71410_x().field_71439_g.field_71174_a.func_147297_a(new PositionRotation(cPacketPlayer.field_149479_a, cPacketPlayer.field_149477_b, cPacketPlayer.field_149478_c, cPacketPlayer.field_149476_e, cPacketPlayer.field_149473_f, cPacketPlayer.field_149474_g));
            return false;
         } else {
            return !(packet instanceof CPacketPlayer) || packet instanceof PositionRotation;
         }
      } else {
         return true;
      }
   }

   public void onEnable() {
      super.onEnable();
      this.originalRidingEntity = null;
      Minecraft mc = Minecraft.func_71410_x();
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         if (mc.field_71439_g.func_184218_aH()) {
            this.originalRidingEntity = mc.field_71439_g.func_184187_bx();
            if (this.dismountEntity.getValBoolean()) {
               mc.field_71439_g.func_184210_p();
               ChatUtils.message("Dismounted entity.");
            }

            if (this.removeEntity.getValBoolean()) {
               mc.field_71441_e.func_72900_e(this.originalRidingEntity);
               ChatUtils.message("Removed entity from world.");
            }
         } else {
            ChatUtils.message("Please mount an entity before enabling this module.");
            this.toggle();
         }
      }

   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71441_e != null && mc.field_71439_g != null && !mc.field_71439_g.func_184218_aH() && this.hasOriginalRidingEntity()) {
         if (this.forceOnGround.getValBoolean()) {
            mc.field_71439_g.field_70122_E = true;
         }

         if (this.setMountPosition.getValBoolean()) {
            this.originalRidingEntity.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
         }

         if (this.sendMovePackets.getValBoolean()) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketVehicleMove(this.originalRidingEntity));
         }
      }

   }

   public void onDisable() {
      super.onDisable();
      if (this.hasOriginalRidingEntity()) {
         Minecraft mc = Minecraft.func_71410_x();
         if (this.respawnEntity.getValBoolean()) {
            this.originalRidingEntity.field_70128_L = false;
         }

         if (!mc.field_71439_g.func_184218_aH()) {
            mc.field_71441_e.func_72838_d(this.originalRidingEntity);
            mc.field_71439_g.func_184205_a(this.originalRidingEntity, true);
            ChatUtils.message("Spawned & mounted original entity.");
         }

         this.originalRidingEntity = null;
      }

   }

   private boolean hasOriginalRidingEntity() {
      return this.originalRidingEntity != null;
   }
}
