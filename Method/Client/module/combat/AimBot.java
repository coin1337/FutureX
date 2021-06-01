package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.ValidUtils;
import Method.Client.utils.system.Connection;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AimBot extends Module {
   Setting priority;
   Setting walls;
   Setting yaw;
   Setting pitch;
   Setting range;
   Setting FOV;
   Setting Silent;
   Setting Mobs;
   Setting Animals;
   public EntityLivingBase target;

   public AimBot() {
      super("AimBot", 0, Category.COMBAT, "Aims to enemys");
      this.priority = Main.setmgr.add(new Setting("priority", this, "Closest", new String[]{"Closest", "Health"}));
      this.walls = Main.setmgr.add(new Setting("walls", this, false));
      this.yaw = Main.setmgr.add(new Setting("yaw", this, 15.0D, 0.0D, 50.0D, false));
      this.pitch = Main.setmgr.add(new Setting("pitch", this, 15.0D, 0.0D, 50.0D, false));
      this.range = Main.setmgr.add(new Setting("range", this, 4.7D, 0.1D, 10.0D, false));
      this.FOV = Main.setmgr.add(new Setting("FOV", this, 90.0D, 1.0D, 180.0D, false));
      this.Silent = Main.setmgr.add(new Setting("Silent", this, false));
      this.Mobs = Main.setmgr.add(new Setting("Mobs", this, true));
      this.Animals = Main.setmgr.add(new Setting("Animals", this, false));
   }

   public void onDisable() {
      this.target = null;
      super.onDisable();
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && this.Silent.getValBoolean()) {
         if (packet instanceof Rotation || packet instanceof PositionRotation) {
            this.updateTarget();
            CPacketPlayer packet2 = (CPacketPlayer)packet;
            float[] rot = Utils.getNeededRotations(this.target.func_174791_d().func_72441_c(0.0D, (double)(this.target.func_70047_e() / 2.0F), 0.0D), (float)this.yaw.getValDouble(), (float)this.pitch.getValDouble());
            packet2.field_149476_e = rot[0];
            packet2.field_149473_f = rot[1];
         }

         this.target = null;
      }

      return true;
   }

   public void onClientTick(ClientTickEvent event) {
      this.updateTarget();
      if (!this.Silent.getValBoolean()) {
         float[] rot = Utils.getNeededRotations(this.target.func_174791_d().func_72441_c(0.0D, (double)(this.target.func_70047_e() / 2.0F), 0.0D), (float)this.yaw.getValDouble(), (float)this.pitch.getValDouble());
         mc.field_71439_g.field_70177_z = rot[0];
         mc.field_71439_g.field_70125_A = rot[1];
      }

      this.target = null;
      super.onClientTick(event);
   }

   void updateTarget() {
      Iterator var1 = mc.field_71441_e.field_72996_f.iterator();

      while(var1.hasNext()) {
         Object object = var1.next();
         if (object instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase)object;
            if (this.check(entity)) {
               this.target = entity;
            }
         }
      }

   }

   public boolean check(EntityLivingBase entity) {
      if (this.Checks(entity, this.FOV)) {
         return false;
      } else if (!ValidUtils.isInAttackRange(entity, (float)this.range.getValDouble())) {
         return false;
      } else if (!ValidUtils.pingCheck(entity)) {
         return false;
      } else if (!this.isPriority(entity)) {
         return false;
      } else {
         return !this.walls.getValBoolean() ? mc.field_71439_g.func_70685_l(entity) : true;
      }
   }

   boolean Checks(EntityLivingBase entity, Setting fov) {
      if (entity instanceof EntityArmorStand) {
         return true;
      } else if (!ValidUtils.isNoScreen()) {
         return true;
      } else if (entity == mc.field_71439_g) {
         return true;
      } else if (entity.field_70128_L) {
         return true;
      } else if (ValidUtils.isBot(entity)) {
         return true;
      } else if (ValidUtils.isFriendEnemy(entity)) {
         return true;
      } else if (!ValidUtils.isInAttackFOV(entity, (int)fov.getValDouble())) {
         return true;
      } else if (this.Animals.getValBoolean() && entity instanceof IAnimals) {
         return false;
      } else if (this.Mobs.getValBoolean() && entity instanceof IMob) {
         return false;
      } else {
         return !(entity instanceof EntityPlayer);
      }
   }

   boolean isPriority(EntityLivingBase entity) {
      return this.priority.getValString().equalsIgnoreCase("Closest") && ValidUtils.isClosest(entity, this.target) || this.priority.getValString().equalsIgnoreCase("Health") && ValidUtils.isLowHealth(entity, this.target);
   }
}
