package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoRemount extends Module {
   Setting Bypass;
   Setting boat;
   Setting Minecart;
   Setting horse;
   Setting skeletonHorse;
   Setting donkey;
   Setting mule;
   Setting pig;
   Setting llama;
   Setting range;

   public AutoRemount() {
      super("AutoRemount", 0, Category.PLAYER, "AutoRemount");
      this.Bypass = Main.setmgr.add(new Setting("Bypass", this, true));
      this.boat = Main.setmgr.add(new Setting("boat", this, true));
      this.Minecart = Main.setmgr.add(new Setting("Minecart", this, true));
      this.horse = Main.setmgr.add(new Setting("horse", this, true));
      this.skeletonHorse = Main.setmgr.add(new Setting("skeletonHorse", this, true));
      this.donkey = Main.setmgr.add(new Setting("donkey", this, true));
      this.mule = Main.setmgr.add(new Setting("mule", this, true));
      this.pig = Main.setmgr.add(new Setting("pig ", this, true));
      this.llama = Main.setmgr.add(new Setting("llama", this, true));
      this.range = Main.setmgr.add(new Setting("range", this, 2.0D, 0.0D, 10.0D, true));
   }

   public void onClientTick(ClientTickEvent event) {
      if (!mc.field_71439_g.func_184218_aH()) {
         mc.field_71441_e.field_72996_f.stream().filter(this::isValidEntity).min(Comparator.comparing((en) -> {
            return mc.field_71439_g.func_70032_d(en);
         })).ifPresent((entity) -> {
            mc.field_71442_b.func_187097_a(mc.field_71439_g, entity, EnumHand.MAIN_HAND);
         });
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof CPacketUseEntity && this.Bypass.getValBoolean()) {
         CPacketUseEntity packet2 = (CPacketUseEntity)packet;
         if (this.isValidEntity(packet2.func_149564_a(mc.field_71441_e))) {
            return !packet2.field_149566_b.equals(Action.INTERACT_AT);
         }
      }

      return true;
   }

   private boolean isValidEntity(Entity entity) {
      if (mc.field_71439_g.func_184218_aH()) {
         return false;
      } else if ((double)entity.func_70032_d(mc.field_71439_g) > this.range.getValDouble()) {
         return false;
      } else {
         if (entity instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse)entity;
            if (horse.func_70631_g_()) {
               return false;
            }
         }

         if (entity instanceof EntityDonkey) {
            EntityDonkey entityDonkey = (EntityDonkey)entity;
            if (entityDonkey.func_70631_g_()) {
               return false;
            }
         }

         if (entity instanceof EntityMule) {
            EntityMule entityDonkey = (EntityMule)entity;
            if (entityDonkey.func_70631_g_()) {
               return false;
            }
         }

         if (entity instanceof EntityBoat && this.boat.getValBoolean()) {
            return true;
         } else if (entity instanceof EntityMinecart && this.Minecart.getValBoolean()) {
            return true;
         } else if (entity instanceof EntityHorse && this.horse.getValBoolean()) {
            return true;
         } else if (entity instanceof EntitySkeletonHorse && this.skeletonHorse.getValBoolean()) {
            return true;
         } else if (entity instanceof EntityDonkey && this.donkey.getValBoolean()) {
            return true;
         } else if (entity instanceof EntityMule && this.mule.getValBoolean()) {
            return true;
         } else if (entity instanceof EntityPig && this.pig.getValBoolean()) {
            EntityPig pig = (EntityPig)entity;
            return pig.func_70631_g_() ? false : pig.func_70901_n();
         } else {
            return entity instanceof EntityLlama && this.llama.getValBoolean();
         }
      }
   }
}
