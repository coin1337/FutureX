package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import java.util.Iterator;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FireballReturn extends Module {
   Setting yaw;
   Setting pitch;
   Setting range;
   public EntityFireball target;
   public TimerUtils timer;

   public FireballReturn() {
      super("FireballReturn", 0, Category.COMBAT, "Returns Fireballs to sender");
      this.yaw = Main.setmgr.add(new Setting("yaw", this, 25.0D, 0.0D, 50.0D, false));
      this.pitch = Main.setmgr.add(new Setting("pitch", this, 25.0D, 0.0D, 50.0D, false));
      this.range = Main.setmgr.add(new Setting("range", this, 10.0D, 0.1D, 10.0D, false));
      this.timer = new TimerUtils();
   }

   public void onClientTick(ClientTickEvent event) {
      this.updateTarget();
      this.attackTarget();
      super.onClientTick(event);
   }

   void updateTarget() {
      Iterator var1 = mc.field_71441_e.field_72996_f.iterator();

      while(var1.hasNext()) {
         Object object = var1.next();
         if (object instanceof EntityFireball) {
            EntityFireball entity = (EntityFireball)object;
            if (this.isInAttackRange(entity) && !entity.field_70128_L && !entity.field_70122_E && entity.func_70075_an()) {
               this.target = entity;
            }
         }
      }

   }

   void attackTarget() {
      if (this.target != null) {
         Utils.getNeededRotations(this.target.func_174791_d(), (float)this.yaw.getValDouble(), (float)this.pitch.getValDouble());
         int currentCPS = Utils.random(4, 7);
         if (this.timer.isDelay((long)(1000 / currentCPS))) {
            mc.func_147116_af();
            this.timer.setLastMS();
            this.target = null;
         }

      }
   }

   public boolean isInAttackRange(EntityFireball entity) {
      return (double)entity.func_70032_d(mc.field_71439_g) <= this.range.getValDouble();
   }
}
