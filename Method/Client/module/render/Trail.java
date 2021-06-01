package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Trail extends Module {
   Setting Self;
   Setting Player;
   Setting Mob;
   Setting Hostile;
   Setting Tickrate;
   Setting Yoffset;
   Setting Trail;

   public Trail() {
      super("Trail", 0, Category.RENDER, "Trail");
      this.Self = Main.setmgr.add(new Setting("Self", this, true));
      this.Player = Main.setmgr.add(new Setting("Player", this, true));
      this.Mob = Main.setmgr.add(new Setting("Mob", this, false));
      this.Hostile = Main.setmgr.add(new Setting("Hostile", this, false));
      this.Tickrate = Main.setmgr.add(new Setting("Per Sec", this, 10.0D, 2.0D, 20.0D, true));
      this.Yoffset = Main.setmgr.add(new Setting("Y Offset", this, 0.0D, 0.0D, 2.0D, false));
      this.Trail = Main.setmgr.add(new Setting("Mode", this, "SMOKE", new String[]{"HEART", "FIREWORK", "FLAME", "CLOUD", "WATER", "LAVA", "SLIME", "EXPLOSION", "MAGIC", "REDSTONE", "SWORD"}));
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if ((double)mc.field_71439_g.field_70173_aa % this.Tickrate.getValDouble() == 0.0D) {
         Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

         while(var2.hasNext()) {
            Entity object = (Entity)var2.next();
            if (object instanceof EntityLivingBase) {
               EntityLivingBase entity = (EntityLivingBase)object;
               if (entity instanceof IAnimals && this.Mob.getValBoolean()) {
                  Renderparticle(entity, this.Trail.getValString(), this.Yoffset.getValDouble());
               }

               if (entity instanceof IMob && this.Hostile.getValBoolean()) {
                  Renderparticle(entity, this.Trail.getValString(), this.Yoffset.getValDouble());
               }

               if (entity instanceof EntityPlayer && this.Player.getValBoolean() && entity != mc.field_71439_g) {
                  Renderparticle(entity, this.Trail.getValString(), this.Yoffset.getValDouble());
               }

               if (entity == mc.field_71439_g && this.Self.getValBoolean()) {
                  Renderparticle(entity, this.Trail.getValString(), this.Yoffset.getValDouble());
               }
            }
         }
      }

   }

   public static void Renderparticle(EntityLivingBase entity, String s, double yoffset) {
      try {
         byte var5 = -1;
         switch(s.hashCode()) {
         case -1174650123:
            if (s.equals("EXPLOSION")) {
               var5 = 9;
            }
            break;
         case 2329312:
            if (s.equals("LAVA")) {
               var5 = 4;
            }
            break;
         case 15786612:
            if (s.equals("REDSTONE")) {
               var5 = 2;
            }
            break;
         case 64218645:
            if (s.equals("CLOUD")) {
               var5 = 6;
            }
            break;
         case 66975507:
            if (s.equals("FLAME")) {
               var5 = 7;
            }
            break;
         case 68614182:
            if (s.equals("HEART")) {
               var5 = 0;
            }
            break;
         case 73118093:
            if (s.equals("MAGIC")) {
               var5 = 3;
            }
            break;
         case 78988968:
            if (s.equals("SLIME")) {
               var5 = 8;
            }
            break;
         case 79024463:
            if (s.equals("SMOKE")) {
               var5 = 5;
            }
            break;
         case 79322589:
            if (s.equals("SWORD")) {
               var5 = 1;
            }
            break;
         case 82365687:
            if (s.equals("WATER")) {
               var5 = 10;
            }
            break;
         case 219914823:
            if (s.equals("FIREWORK")) {
               var5 = 11;
            }
         }

         switch(var5) {
         case 0:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.HEART, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 1:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.FIREWORKS_SPARK, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            mc.field_71441_e.func_175688_a(EnumParticleTypes.CRIT, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            mc.field_71441_e.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            mc.field_71441_e.func_175688_a(EnumParticleTypes.CRIT_MAGIC, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            mc.field_71441_e.func_175688_a(EnumParticleTypes.ENCHANTMENT_TABLE, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 2:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.REDSTONE, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 3:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.ENCHANTMENT_TABLE, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 4:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.LAVA, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            mc.field_71441_e.func_175688_a(EnumParticleTypes.DRIP_LAVA, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 5:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 6:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.CLOUD, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 7:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.FLAME, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 8:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.SLIME, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 9:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.EXPLOSION_NORMAL, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 10:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.WATER_BUBBLE, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            mc.field_71441_e.func_175688_a(EnumParticleTypes.WATER_SPLASH, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
            return;
         case 11:
            mc.field_71441_e.func_175688_a(EnumParticleTypes.FIREWORKS_SPARK, entity.field_70165_t, entity.field_70163_u + 0.01D + yoffset, entity.field_70161_v, entity.field_70159_w * 0.4D, entity.field_70181_x * 0.4D, entity.field_70179_y * 0.4D, new int[0]);
         }
      } catch (Exception var6) {
      }

   }
}
