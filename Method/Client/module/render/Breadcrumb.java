package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.RenderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Breadcrumb extends Module {
   Setting tickdelay;
   Setting Color;
   Setting OtherColor;
   Setting Width;
   Setting BlockSnap;
   Setting OtherPlayers;
   List<Vec3d> doubles;
   List<Breadcrumb.OtherPos> OtherPos;

   public Breadcrumb() {
      super("Breadcrumb", 0, Category.RENDER, "Breadcrumbs");
      this.tickdelay = Main.setmgr.add(new Setting("tickdelay", this, 2.0D, 1.0D, 20.0D, true));
      this.Color = Main.setmgr.add(new Setting("Color", this, 0.4D, 0.8D, 0.9D, 1.0D));
      this.OtherColor = Main.setmgr.add(new Setting("OtherColor", this, 0.8D, 0.8D, 0.9D, 1.0D));
      this.Width = Main.setmgr.add(new Setting("Width", this, 2.5D, 1.0D, 5.0D, false));
      this.BlockSnap = Main.setmgr.add(new Setting("BlockSnap", this, false));
      this.OtherPlayers = Main.setmgr.add(new Setting("OtherPlayers", this, false));
      this.doubles = new ArrayList();
      this.OtherPos = new ArrayList();
   }

   public void onEnable() {
      this.doubles.clear();
      if (mc.field_71439_g.func_70011_f(mc.field_71439_g.field_70142_S, mc.field_71439_g.field_70137_T, mc.field_71439_g.field_70136_U) < 200.0D) {
         this.doubles.add(new Vec3d(mc.field_71439_g.field_70142_S, mc.field_71439_g.field_70137_T, mc.field_71439_g.field_70136_U));
         this.doubles.add(new Vec3d(mc.field_71439_g.func_180425_c()));
      }

   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      Iterator var2;
      if (mc.field_71439_g.field_70173_aa % (int)this.tickdelay.getValDouble() == 0) {
         this.doubles.add(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v));
         if (this.OtherPlayers.getValBoolean()) {
            var2 = mc.field_71441_e.field_72996_f.iterator();

            label44:
            while(true) {
               Entity entity;
               do {
                  if (!var2.hasNext()) {
                     break label44;
                  }

                  entity = (Entity)var2.next();
               } while(!(entity instanceof EntityOtherPlayerMP));

               EntityOtherPlayerMP otherPlayerMP = (EntityOtherPlayerMP)entity;
               boolean newplayer = true;
               Iterator var6 = this.OtherPos.iterator();

               while(var6.hasNext()) {
                  Breadcrumb.OtherPos otherPo = (Breadcrumb.OtherPos)var6.next();
                  if (otherPo.getName().equals(otherPlayerMP.func_70005_c_())) {
                     otherPo.doubles.add(new Vec3d(otherPlayerMP.field_70165_t, otherPlayerMP.field_70163_u, otherPlayerMP.field_70161_v));
                     newplayer = false;
                  }
               }

               if (newplayer) {
                  Breadcrumb.OtherPos NewPla = new Breadcrumb.OtherPos(otherPlayerMP.func_70005_c_());
                  this.OtherPos.add(NewPla);
                  NewPla.doubles.add(new Vec3d(otherPlayerMP.field_70165_t, otherPlayerMP.field_70163_u, otherPlayerMP.field_70161_v));
               }
            }
         }
      }

      RenderUtils.RenderLine(this.doubles, this.Color.getcolor(), this.Width.getValDouble(), this.BlockSnap.getValBoolean());
      if (this.OtherPlayers.getValBoolean()) {
         var2 = this.OtherPos.iterator();

         while(var2.hasNext()) {
            Breadcrumb.OtherPos otherPo = (Breadcrumb.OtherPos)var2.next();
            RenderUtils.RenderLine(otherPo.doubles, this.OtherColor.getcolor(), this.Width.getValDouble(), this.BlockSnap.getValBoolean());
         }
      }

   }

   static class OtherPos {
      private final String name;
      List<Vec3d> doubles = new ArrayList();

      public String getName() {
         return this.name;
      }

      public OtherPos(String name) {
         this.name = name;
      }
   }
}
