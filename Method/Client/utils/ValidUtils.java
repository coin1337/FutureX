package Method.Client.utils;

import Method.Client.managers.FriendManager;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.combat.AntiBot;
import Method.Client.module.player.NoEffect;
import Method.Client.utils.system.Wrapper;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ValidUtils {
   public static boolean pingCheck(EntityLivingBase entity) {
      if (ModuleManager.getModuleByName("AntiBot").isToggled() && entity instanceof EntityPlayer) {
         ((NetHandlerPlayClient)Objects.requireNonNull(Wrapper.INSTANCE.mc().func_147114_u())).func_175102_a(entity.func_110124_au());
         return ((NetHandlerPlayClient)Objects.requireNonNull(Wrapper.INSTANCE.mc().func_147114_u())).func_175102_a(entity.func_110124_au()).func_178853_c() <= 5;
      } else {
         return true;
      }
   }

   public static boolean isInAttackFOV(EntityLivingBase entity, int fov) {
      return Utils.getDistanceFromMouse(entity) <= fov;
   }

   public static boolean isClosest(EntityLivingBase entity, EntityLivingBase entityPriority) {
      return entityPriority == null || Wrapper.INSTANCE.player().func_70032_d(entity) < Wrapper.INSTANCE.player().func_70032_d(entityPriority);
   }

   public static boolean isLowHealth(EntityLivingBase entity, EntityLivingBase entityPriority) {
      return entityPriority == null || entity.func_110143_aJ() < entityPriority.func_110143_aJ();
   }

   public static boolean isInAttackRange(EntityLivingBase entity, float range) {
      return entity.func_70032_d(Wrapper.INSTANCE.player()) <= range;
   }

   public static boolean isBot(EntityLivingBase entity) {
      if (!(entity instanceof EntityPlayer)) {
         return false;
      } else {
         EntityPlayer player = (EntityPlayer)entity;
         Module module = ModuleManager.getModuleByName("AntiBot");
         return module.isToggled() && AntiBot.isBot(player);
      }
   }

   public static boolean isFriendEnemy(EntityLivingBase entity) {
      if (entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         String ID = Utils.getPlayerName(player);
         return FriendManager.friendsList.contains(ID);
      } else {
         return true;
      }
   }

   public static boolean isNoScreen() {
      if (ModuleManager.getModuleByName("NoEffect").isToggled()) {
         if (NoEffect.NoScreenEvents.getValBoolean()) {
            return !Utils.checkScreen();
         } else {
            return false;
         }
      } else {
         return true;
      }
   }
}
