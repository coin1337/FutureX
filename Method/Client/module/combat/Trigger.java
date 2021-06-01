package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.ValidUtils;
import Method.Client.utils.system.Wrapper;
import java.util.Iterator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Trigger extends Module {
   public Setting autoDelay;
   public Setting advanced;
   public Setting minCPS;
   public Setting maxCPS;
   Setting Mode;
   public EntityLivingBase target;
   public TimerUtils timer;

   public Trigger() {
      super("Trigger", 0, Category.COMBAT, "Triggers");
      this.autoDelay = Main.setmgr.add(new Setting("AutoDelay", this, true));
      this.advanced = Main.setmgr.add(new Setting("Advanced", this, false));
      this.minCPS = Main.setmgr.add(new Setting("MinCPS", this, 4.0D, 1.0D, 20.0D, true));
      this.maxCPS = Main.setmgr.add(new Setting("MaxCPS", this, 8.0D, 1.0D, 20.0D, false));
      this.Mode = Main.setmgr.add(new Setting("Mode", this, "Click", new String[]{"Click", "Attack"}));
      this.timer = new TimerUtils();
   }

   public void onDisable() {
      this.target = null;
      super.onDisable();
   }

   public void onClientTick(ClientTickEvent event) {
      this.updateTarget();
      this.attackTarget(this.target);
      super.onClientTick(event);
   }

   void attackTarget(EntityLivingBase target) {
      if (this.check(target)) {
         if (this.autoDelay.getValBoolean()) {
            if (mc.field_71439_g.func_184825_o(0.0F) == 1.0F) {
               this.processAttack(target, false);
            }
         } else {
            int currentCPS = Utils.random((int)this.minCPS.getValDouble(), (int)this.maxCPS.getValDouble());
            if (this.timer.isDelay((long)(1000 / currentCPS))) {
               this.processAttack(target, true);
               this.timer.setLastMS();
            }
         }
      }

   }

   public void processAttack(EntityLivingBase entity, boolean packet) {
      float sharpLevel = EnchantmentHelper.func_152377_a(mc.field_71439_g.func_184614_ca(), this.target.func_70668_bt());
      if (this.Mode.getValString().equalsIgnoreCase("Click")) {
         mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
      } else {
         if (packet) {
            Wrapper.INSTANCE.sendPacket(new CPacketUseEntity(this.target));
         } else {
            Wrapper.INSTANCE.attack(this.target);
         }

         Wrapper.INSTANCE.swingArm();
         if (sharpLevel > 0.0F) {
            mc.field_71439_g.func_71047_c(this.target);
         }
      }

   }

   void updateTarget() {
      RayTraceResult object = Wrapper.INSTANCE.mc().field_71476_x;
      if (object != null) {
         EntityLivingBase entity = null;
         if (this.target != entity) {
            this.target = null;
         }

         if (object.field_72313_a == Type.ENTITY) {
            if (object.field_72308_g instanceof EntityLivingBase) {
               entity = (EntityLivingBase)object.field_72308_g;
               this.target = entity;
            }
         } else if (object.field_72313_a != Type.ENTITY && this.advanced.getValBoolean()) {
            entity = this.getClosestEntity();
         }

         if (entity != null) {
            this.target = entity;
         }

      }
   }

   EntityLivingBase getClosestEntity() {
      EntityLivingBase closestEntity = null;
      Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

      while(true) {
         EntityLivingBase entity;
         do {
            do {
               Object o;
               do {
                  do {
                     if (!var2.hasNext()) {
                        return closestEntity;
                     }

                     o = var2.next();
                  } while(!(o instanceof EntityLivingBase));
               } while(o instanceof EntityArmorStand);

               entity = (EntityLivingBase)o;
            } while(!this.check(entity));
         } while(closestEntity != null && !(mc.field_71439_g.func_70032_d(entity) < mc.field_71439_g.func_70032_d(closestEntity)));

         closestEntity = entity;
      }
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
      } else if (ValidUtils.isBot(entity)) {
         return false;
      } else if (ValidUtils.isFriendEnemy(entity)) {
         return false;
      } else {
         if (this.advanced.getValBoolean()) {
            if (!ValidUtils.isInAttackFOV(entity, 50)) {
               return false;
            }

            if (!ValidUtils.isInAttackRange(entity, 4.7F)) {
               return false;
            }
         }

         return !ValidUtils.pingCheck(entity) ? false : mc.field_71439_g.func_70685_l(entity);
      }
   }
}
