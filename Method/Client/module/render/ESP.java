package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Screens.Custom.Esp.EspGui;
import Method.Client.utils.visual.RenderUtils;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ESP extends Module {
   Setting Box;
   Setting Nametag;
   Setting MobColor;
   Setting Mode;
   Setting LineWidth;
   Setting Glow;
   Setting GlowWidth;
   Setting MobSelect;

   public ESP() {
      super("ESP", 0, Category.RENDER, "ESP");
      this.Box = Main.setmgr.add(new Setting("Box", this, true));
      this.Nametag = Main.setmgr.add(new Setting("Nametag", this, false));
      this.MobColor = Main.setmgr.add(new Setting("MobColor", this, 0.88D, 1.0D, 1.0D, 1.0D));
      this.Mode = Main.setmgr.add(new Setting("Hole Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.Glow = Main.setmgr.add(new Setting("Glow", this, true));
      this.GlowWidth = Main.setmgr.add(new Setting("GlowWidth", this, 0.0D, 0.0D, 1.0D, false));
      this.MobSelect = Main.setmgr.add(new Setting("Gui", this, Main.GuiEsp));
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

      while(var2.hasNext()) {
         Entity object = (Entity)var2.next();
         Iterator var4 = EspGui.Getmobs().iterator();

         while(var4.hasNext()) {
            String mob = (String)var4.next();
            if (((Class)Objects.requireNonNull(EntityList.func_192839_a(mob))).getName().equalsIgnoreCase(object.getClass().getName())) {
               this.render(object);
            }
         }
      }

      super.onRenderWorldLast(event);
   }

   void render(Entity ent) {
      if (ent != mc.field_71439_g) {
         if (this.Nametag.getValBoolean()) {
            ent.func_174805_g(true);
         }

         if (this.Glow.getValBoolean()) {
            mc.field_71438_f.field_174991_A.field_148031_d.forEach((shader) -> {
               ShaderUniform outlineRadius = shader.func_148043_c().func_147991_a("Radius");
               if (outlineRadius != null) {
                  outlineRadius.func_148090_a((float)this.GlowWidth.getValDouble());
               }

            });
         }

         if (ent instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase)ent;
            entity.func_184195_f(this.Glow.getValBoolean());
            if (this.Nametag.getValBoolean()) {
               entity.func_96094_a(ent.func_70005_c_());
            }

            if (this.Box.getValBoolean()) {
               RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Boundingbb(entity, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D), this.MobColor.getcolor(), this.LineWidth.getValDouble());
            }
         }

      }
   }

   public void onDisable() {
      Iterator var1 = mc.field_71441_e.field_72996_f.iterator();

      while(var1.hasNext()) {
         Object object = var1.next();
         Entity entity = (Entity)object;
         if (entity.func_184202_aL()) {
            entity.func_184195_f(false);
         }
      }

      super.onDisable();
   }
}
