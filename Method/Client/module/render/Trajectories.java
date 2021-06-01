package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.ChatUtils;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.opengl.GL11;

public class Trajectories extends Module {
   public final List<Trajectories.Bpos> Pos = new ArrayList();
   Setting FindEpearl;
   Setting ChatPrint;
   Setting RenderTime;
   Setting Mode;
   Setting LineWidth;
   Setting Color;
   Setting skeleton;

   public Trajectories() {
      super("Trajectories", 0, Category.RENDER, "Trajectories");
      this.FindEpearl = Main.setmgr.add(new Setting("Follow Pearl", this, true));
      this.ChatPrint = Main.setmgr.add(new Setting("ChatPrint", this, false, this.FindEpearl, 3));
      this.RenderTime = Main.setmgr.add(new Setting("RenderTime", this, 5.0D, 0.0D, 35.0D, false, this.FindEpearl, 4));
      this.Mode = Main.setmgr.add(new Setting("Mode", this, "Xspot", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.Color = Main.setmgr.add(new Setting("Color", this, 0.22D, 1.0D, 0.6D, 0.65D));
      this.skeleton = Main.setmgr.add(new Setting("Skeleton", this, false));
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.FindEpearl.getValBoolean()) {
         Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

         while(true) {
            while(true) {
               Entity entity;
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  entity = (Entity)var2.next();
               } while(!(entity instanceof EntityEnderPearl));

               EntityEnderPearl e = (EntityEnderPearl)entity;
               boolean notfound = true;
               Iterator var6 = this.Pos.iterator();

               Trajectories.Bpos po;
               while(var6.hasNext()) {
                  po = (Trajectories.Bpos)var6.next();
                  if (po.getUuid().equals(e.func_110124_au())) {
                     notfound = false;
                     break;
                  }
               }

               if (notfound) {
                  this.Pos.add(new Trajectories.Bpos(new ArrayList(Collections.singletonList(e.func_174791_d())), e.func_110124_au(), System.currentTimeMillis()));
                  if (this.ChatPrint.getValBoolean()) {
                     ChatUtils.message(e.field_181555_c.toString() + " Threw a pearl!");
                  }
               } else {
                  var6 = this.Pos.iterator();

                  while(var6.hasNext()) {
                     po = (Trajectories.Bpos)var6.next();
                     if (po.uuid.equals(e.func_110124_au())) {
                        po.vec3ds.add(e.func_174791_d());
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public boolean itemcheck(Item item) {
      return item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl || item instanceof ItemSplashPotion || item instanceof ItemLingeringPotion || item instanceof ItemFishingRod;
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

      while(true) {
         EntityLivingBase livingBase;
         do {
            Entity entity;
            do {
               if (!var2.hasNext()) {
                  if (this.FindEpearl.getValBoolean()) {
                     RenderUtils.OpenGl();
                     GlStateManager.func_187441_d((float)this.LineWidth.getValDouble() * 3.0F);
                     List<Trajectories.Bpos> toremove = new ArrayList();
                     Iterator var34 = this.Pos.iterator();

                     Trajectories.Bpos po;
                     while(var34.hasNext()) {
                        po = (Trajectories.Bpos)var34.next();
                        if ((double)po.getaLong() + this.RenderTime.getValDouble() * 1000.0D < (double)System.currentTimeMillis()) {
                           toremove.add(po);
                        }
                     }

                     this.Pos.removeAll(toremove);
                     if (!this.Pos.isEmpty()) {
                        var34 = this.Pos.iterator();

                        while(var34.hasNext()) {
                           po = (Trajectories.Bpos)var34.next();
                           GlStateManager.func_187447_r(1);
                           ColorUtils.glColor(this.Color.getcolor());
                           double[] rPos = this.rPos();
                           Vec3d priorpoint = (Vec3d)po.getVec3ds().get(0);

                           Vec3d vec3d;
                           for(Iterator var7 = po.getVec3ds().iterator(); var7.hasNext(); priorpoint = vec3d) {
                              vec3d = (Vec3d)var7.next();
                              GL11.glVertex3d(vec3d.field_72450_a - rPos[0], vec3d.field_72448_b - rPos[1], vec3d.field_72449_c - rPos[2]);
                              GL11.glVertex3d(priorpoint.field_72450_a - rPos[0], priorpoint.field_72448_b - rPos[1], priorpoint.field_72449_c - rPos[2]);
                           }

                           GlStateManager.func_187437_J();
                        }
                     }

                     RenderUtils.ReleaseGl();
                  }

                  super.onRenderWorldLast(event);
                  return;
               }

               entity = (Entity)var2.next();
            } while(!(entity instanceof EntityLivingBase));

            livingBase = (EntityLivingBase)entity;
            if (livingBase instanceof EntitySkeleton && !this.skeleton.getValBoolean()) {
               return;
            }
         } while(!this.itemcheck(livingBase.func_184614_ca().func_77973_b()) && !this.itemcheck(livingBase.func_184592_cb().func_77973_b()));

         livingBase.func_184607_cu();
         boolean usingBow = livingBase.func_184607_cu().func_77973_b() instanceof ItemBow;
         double arrowPosX = livingBase.field_70142_S + (livingBase.field_70165_t - livingBase.field_70142_S) * (double)event.getPartialTicks() - (double)(MathHelper.func_76134_b((float)Math.toRadians((double)livingBase.field_70177_z)) * 0.16F);
         double arrowPosY = livingBase.field_70137_T + (livingBase.field_70163_u - livingBase.field_70137_T) * (double)event.getPartialTicks() + (double)livingBase.func_70047_e() - 0.1D;
         double arrowPosZ = livingBase.field_70136_U + (livingBase.field_70161_v - livingBase.field_70136_U) * (double)event.getPartialTicks() - (double)(MathHelper.func_76126_a((float)Math.toRadians((double)livingBase.field_70177_z)) * 0.16F);
         float arrowMotionFactor = usingBow ? 1.0F : 0.4F;
         float yaw = (float)Math.toRadians((double)livingBase.field_70177_z);
         float pitch = (float)Math.toRadians((double)livingBase.field_70125_A);
         float arrowMotionX = -MathHelper.func_76126_a(yaw) * MathHelper.func_76134_b(pitch) * arrowMotionFactor;
         float arrowMotionY = -MathHelper.func_76126_a(pitch) * arrowMotionFactor;
         float arrowMotionZ = MathHelper.func_76134_b(yaw) * MathHelper.func_76134_b(pitch) * arrowMotionFactor;
         double arrowMotion = Math.sqrt((double)(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ));
         double bowPower = 1.5D;
         if (usingBow) {
            bowPower = (double)((float)(72000 - livingBase.func_184605_cv()) / 20.0F);
            bowPower = (bowPower * bowPower + bowPower * 2.0D) / 3.0D;
            bowPower = !(bowPower > 1.0D) && !(bowPower <= 0.10000000149011612D) ? bowPower * 3.0D : 3.0D;
         }

         arrowMotionX = (float)((double)arrowMotionX / arrowMotion * bowPower);
         arrowMotionY = (float)((double)arrowMotionY / arrowMotion * bowPower);
         arrowMotionZ = (float)((double)arrowMotionZ / arrowMotion * bowPower);
         double gravity = usingBow ? 0.05D : (!(livingBase.func_184614_ca().func_77973_b() instanceof ItemPotion) && !(livingBase.func_184592_cb().func_77973_b() instanceof ItemPotion) ? (!(livingBase.func_184614_ca().func_77973_b() instanceof ItemFishingRod) && !(livingBase.func_184592_cb().func_77973_b() instanceof ItemFishingRod) ? 0.03D : 0.15D) : 0.4D);
         Vec3d playerVector = new Vec3d(livingBase.field_70165_t, livingBase.field_70163_u + (double)livingBase.func_70047_e(), livingBase.field_70161_v);
         RenderUtils.OpenGl();
         GL11.glEnable(32925);
         GlStateManager.func_187441_d((float)this.LineWidth.getValDouble());
         ColorUtils.glColor(this.Color.getcolor());
         GlStateManager.func_187447_r(3);
         RenderManager renderManager = mc.func_175598_ae();

         for(int i = 0; i < 1000; ++i) {
            GL11.glVertex3d(arrowPosX - renderManager.field_78730_l, arrowPosY - renderManager.field_78731_m, arrowPosZ - renderManager.field_78728_n);
            arrowPosX += (double)arrowMotionX * 0.1D;
            arrowPosY += (double)arrowMotionY * 0.1D;
            arrowPosZ += (double)arrowMotionZ * 0.1D;
            arrowMotionX = (float)((double)arrowMotionX * 0.999D);
            arrowMotionY = (float)((double)arrowMotionY * 0.999D - gravity * 0.1D);
            arrowMotionZ = (float)((double)arrowMotionZ * 0.999D);
            if (mc.field_71441_e.func_72933_a(playerVector, new Vec3d(arrowPosX, arrowPosY, arrowPosZ)) != null) {
               break;
            }
         }

         GlStateManager.func_187437_J();
         double renderX = arrowPosX - renderManager.field_78730_l;
         double renderY = arrowPosY - renderManager.field_78731_m;
         double renderZ = arrowPosZ - renderManager.field_78728_n;
         AxisAlignedBB bb = new AxisAlignedBB(renderX - 0.5D, renderY, renderZ - 0.5D, renderX + 0.5D, renderY + 0.5D, renderZ + 0.5D);
         RenderUtils.RenderBlock(this.Mode.getValString(), bb, this.Color.getcolor(), this.LineWidth.getValDouble());
         RenderUtils.ReleaseGl();
         GL11.glDisable(32925);
      }
   }

   private double[] rPos() {
      try {
         double renderPosX = mc.func_175598_ae().field_78730_l;
         double renderPosY = mc.func_175598_ae().field_78731_m;
         double renderPosZ = mc.func_175598_ae().field_78728_n;
         return new double[]{renderPosX, renderPosY, renderPosZ};
      } catch (Exception var7) {
         return new double[]{0.0D, 0.0D, 0.0D};
      }
   }

   static class Bpos {
      private final List<Vec3d> vec3ds;
      private final UUID uuid;
      private final long aLong;

      public List<Vec3d> getVec3ds() {
         return this.vec3ds;
      }

      public Bpos(List<Vec3d> vec3ds, UUID uuid, long l) {
         this.vec3ds = vec3ds;
         this.uuid = uuid;
         this.aLong = l;
      }

      public UUID getUuid() {
         return this.uuid;
      }

      public long getaLong() {
         return this.aLong;
      }
   }
}
