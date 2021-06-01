package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.ValidUtils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import java.util.Iterator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class KillAura extends Module {
   public Setting priority;
   public Setting walls;
   public Setting autoDelay;
   public Setting packetReach;
   public Setting minCPS;
   public Setting maxCPS;
   public Setting packetRange;
   public Setting range;
   public Setting FOV;
   public Setting Mobs;
   public Setting Animals;
   public Setting SpoofAngle;
   public TimerUtils timer;
   public EntityLivingBase target;

   public KillAura() {
      super("KillAura", 0, Category.COMBAT, "KillAura");
      this.priority = Main.setmgr.add(new Setting("priority Mode", this, "Closest", new String[]{"Closest", "Health"}));
      this.walls = Main.setmgr.add(new Setting("walls", this, true));
      this.autoDelay = Main.setmgr.add(new Setting("autoDelay", this, false));
      this.packetReach = Main.setmgr.add(new Setting("packetReach", this, false));
      this.minCPS = Main.setmgr.add(new Setting("minCPS", this, 5.0D, 0.0D, 30.0D, false));
      this.maxCPS = Main.setmgr.add(new Setting("maxCPS", this, 8.0D, 0.0D, 30.0D, false));
      this.packetRange = Main.setmgr.add(new Setting("packetRange", this, 5.0D, 0.0D, 100.0D, false));
      this.range = Main.setmgr.add(new Setting("range", this, 5.0D, 0.0D, 10.0D, false));
      this.FOV = Main.setmgr.add(new Setting("FOV", this, 180.0D, 0.0D, 180.0D, false));
      this.Mobs = Main.setmgr.add(new Setting("Mobs", this, true));
      this.Animals = Main.setmgr.add(new Setting("Animals", this, false));
      this.SpoofAngle = Main.setmgr.add(new Setting("SpoofAngle", this, true));
      this.timer = new TimerUtils();
   }

   public void onClientTick(ClientTickEvent event) {
      this.killAuraUpdate();
      this.killAuraAttack(this.target);
      super.onClientTick(event);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      this.killAuraUpdate();
      if (side == Connection.Side.OUT && this.SpoofAngle.getValBoolean()) {
         if (packet instanceof Rotation || packet instanceof PositionRotation) {
            CPacketPlayer packet2 = (CPacketPlayer)packet;
            float[] rot = Utils.getNeededRotations(this.target.func_174791_d().func_72441_c(0.0D, (double)(this.target.func_70047_e() / 2.0F), 0.0D), 0.0F, 0.0F);
            packet2.field_149476_e = rot[0];
            packet2.field_149473_f = rot[1];
         }

         this.target = null;
      }

      return true;
   }

   void killAuraUpdate() {
      Iterator var1 = mc.field_71441_e.func_72910_y().iterator();

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

   public void killAuraAttack(EntityLivingBase entity) {
      if (entity != null) {
         if (this.autoDelay.getValBoolean()) {
            if (mc.field_71439_g.func_184825_o(0.0F) == 1.0F) {
               this.processAttack(entity);
               this.target = null;
            }
         } else {
            int CPS = Utils.random((int)this.minCPS.getValDouble(), (int)this.maxCPS.getValDouble());
            int r1 = Utils.random(1, 50);
            int r2 = Utils.random(1, 60);
            int r3 = Utils.random(1, 70);
            if (this.timer.isDelay((long)((1000 + r1 - r2 + r3) / CPS))) {
               this.processAttack(entity);
               this.timer.setLastMS();
               this.target = null;
            }
         }

      }
   }

   public void processAttack(EntityLivingBase entity) {
      if (!this.isInAttackRange(entity) && ValidUtils.isInAttackFOV(entity, (int)this.FOV.getValDouble())) {
         float sharpLevel = EnchantmentHelper.func_152377_a(mc.field_71439_g.func_184614_ca(), entity.func_70668_bt());
         if (this.packetReach.getValBoolean()) {
            double posX = entity.field_70165_t - 3.5D * Math.cos(Math.toRadians((double)(Utils.getYaw(entity) + 90.0F)));
            double posZ = entity.field_70161_v - 3.5D * Math.sin(Math.toRadians((double)(Utils.getYaw(entity) + 90.0F)));
            Wrapper.INSTANCE.sendPacket(new PositionRotation(posX, entity.field_70163_u, posZ, Utils.getYaw(entity), Utils.getPitch(entity), mc.field_71439_g.field_70122_E));
            Wrapper.INSTANCE.sendPacket(new CPacketUseEntity(entity));
            Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
         } else if (this.autoDelay.getValBoolean()) {
            Wrapper.INSTANCE.attack(entity);
         } else {
            Wrapper.INSTANCE.sendPacket(new CPacketUseEntity(entity));
         }

         Wrapper.INSTANCE.swingArm();
         if (sharpLevel > 0.0F) {
            mc.field_71439_g.func_71047_c(entity);
         }

      }
   }

   boolean isPriority(EntityLivingBase entity) {
      return this.priority.getValString().equalsIgnoreCase("Closest") && ValidUtils.isClosest(entity, this.target) || this.priority.getValString().equalsIgnoreCase("Health") && ValidUtils.isLowHealth(entity, this.target);
   }

   boolean isInAttackRange(EntityLivingBase entity) {
      return this.packetReach.getValBoolean() ? !((double)entity.func_70032_d(mc.field_71439_g) <= this.packetRange.getValDouble()) : !((double)entity.func_70032_d(mc.field_71439_g) <= this.range.getValDouble());
   }

   public boolean check(EntityLivingBase entity) {
      if (entity instanceof EntityArmorStand) {
         return false;
      } else if (!ValidUtils.isNoScreen()) {
         return false;
      } else if (entity == mc.field_71439_g) {
         return false;
      } else if (entity.field_70128_L) {
         return false;
      } else if (entity.field_70725_aQ > 0) {
         return false;
      } else if (ValidUtils.isBot(entity)) {
         return false;
      } else if (ValidUtils.isFriendEnemy(entity)) {
         return false;
      } else if (!ValidUtils.isInAttackFOV(entity, (int)this.FOV.getValDouble())) {
         return false;
      } else if (this.isInAttackRange(entity)) {
         return false;
      } else if (!ValidUtils.pingCheck(entity)) {
         return false;
      } else if (!this.walls.getValBoolean() && !mc.field_71439_g.func_70685_l(entity)) {
         return false;
      } else if (this.Animals.getValBoolean() && entity instanceof IAnimals) {
         return this.isPriority(entity);
      } else if (this.Mobs.getValBoolean() && entity instanceof IMob) {
         return this.isPriority(entity);
      } else {
         return entity instanceof EntityPlayer ? this.isPriority(entity) : false;
      }
   }

   public static boolean isInvisible(EntityLivingBase entity) {
      return !entity.func_82150_aj();
   }
}
