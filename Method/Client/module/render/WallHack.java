package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.Executer;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class WallHack extends Module {
   Setting players;
   Setting mobs;
   Setting Barrier;
   TimerUtils timer;

   public WallHack() {
      super("WallHack", 0, Category.RENDER, "WallHack");
      this.players = Main.setmgr.add(new Setting("players", this, false));
      this.mobs = Main.setmgr.add(new Setting("mobs", this, false));
      this.Barrier = Main.setmgr.add(new Setting("Barrier", this, false));
      this.timer = new TimerUtils();
   }

   public void onEnable() {
      Executer.init();
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (this.timer.isDelay(4500L)) {
         if (this.Barrier.getValBoolean()) {
            Executer.execute(() -> {
               Vec3i playerPos = new Vec3i(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);

               for(int x = playerPos.func_177958_n() - 10; x < playerPos.func_177958_n() + 10; ++x) {
                  for(int z = playerPos.func_177952_p() - 10; z < playerPos.func_177952_p() + 10; ++z) {
                     for(int y = playerPos.func_177956_o() + 6; y > playerPos.func_177956_o() - 6; --y) {
                        if (mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c() == Blocks.field_180401_cv) {
                           mc.field_71441_e.func_175688_a(EnumParticleTypes.BARRIER, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0.0D, 0.0D, 0.0D, new int[0]);
                        }
                     }
                  }
               }

            });
         }

         this.timer.setLastMS();
      }

      GlStateManager.func_179086_m(256);
      RenderHelper.func_74519_b();
      Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

      while(var2.hasNext()) {
         Object object = var2.next();
         Entity entity = (Entity)object;
         this.render(entity, event.getPartialTicks());
      }

      super.onRenderWorldLast(event);
   }

   void render(Entity entity, float ticks) {
      Entity ent = this.getEntity(entity);
      if (ent != null && ent != mc.field_71439_g) {
         if (ent != mc.func_175606_aa() || Wrapper.INSTANCE.mcSettings().field_74320_O != 0) {
            mc.field_71460_t.func_175072_h();
            mc.func_175598_ae().func_188388_a(ent, ticks, false);
            mc.field_71460_t.func_180436_i();
         }
      }
   }

   Entity getEntity(Entity e) {
      Entity entity = null;
      if (this.players.getValBoolean() && e instanceof EntityPlayer) {
         entity = e;
      } else if (this.mobs.getValBoolean() && e instanceof EntityLiving) {
         entity = e;
      } else if (e instanceof EntityItem) {
         entity = e;
      } else if (e instanceof EntityArrow) {
         entity = e;
      }

      return entity;
   }
}
