package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.util.ArrayList;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Mouse;

public final class Player extends Module {
   static Setting xpos;
   static Setting ypos;
   static Setting Scale;
   static Setting Nolook;

   public Player() {
      super("Player", 0, Category.ONSCREEN, "Player");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 20.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
      Main.setmgr.add(Scale = new Setting("Scale", this, 1.0D, 0.0D, 5.0D, false));
      ArrayList<String> options = new ArrayList();
      options.add("Free");
      options.add("Mouse");
      options.add("None");
      Main.setmgr.add(Nolook = new Setting("Mode", this, "Free", options));
   }

   public void onEnable() {
      PinableFrame.Toggle("PlayerSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("PlayerSET", false);
   }

   public static class PlayerRUN extends PinableFrame {
      public PlayerRUN() {
         super("PlayerSET", new String[0], (int)Player.ypos.getValDouble(), (int)Player.xpos.getValDouble());
      }

      public void setup() {
         this.x = (int)Player.xpos.getValDouble();
         this.y = (int)Player.ypos.getValDouble();
      }

      public void Ongui() {
         if (!this.getDrag()) {
            this.x = (int)Player.xpos.getValDouble();
            this.y = (int)Player.ypos.getValDouble();
         } else {
            Player.xpos.setValDouble((double)this.x);
            Player.ypos.setValDouble((double)this.y);
         }

      }

      public void onRenderGameOverlay(Text event) {
         if (this.mc.field_71439_g != null) {
            if (this.mc.field_71474_y.field_74320_O == 0) {
               GlStateManager.func_179094_E();
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               if (Player.Nolook.getValString().equalsIgnoreCase("Free")) {
                  this.drawPlayer(this.x, this.y, this.mc.field_71439_g);
               } else {
                  GuiInventory.func_147046_a(this.x + 17, this.y + 60, (int)(Player.Scale.getValDouble() * 30.0D), Player.Nolook.getValString().equalsIgnoreCase("None") ? 0.0F : (float)this.x - (float)Mouse.getX(), Player.Nolook.getValString().equalsIgnoreCase("None") ? 0.0F : (float)(-this.mc.field_71440_d) + (float)Mouse.getY(), this.mc.field_71439_g);
               }

               GlStateManager.func_179121_F();
               super.onRenderGameOverlay(event);
            }
         }
      }

      private void drawPlayer(int x, int y, EntityLivingBase ent) {
         GlStateManager.func_179109_b((float)x + 30.0F, (float)y + 50.0F, 50.0F);
         GlStateManager.func_179152_a((float)(-Player.Scale.getValDouble()) * 24.0F, (float)Player.Scale.getValDouble() * 24.0F, (float)Player.Scale.getValDouble() * 24.0F);
         GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.func_179114_b(135.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179084_k();
         GlStateManager.func_179126_j();
         GlStateManager.func_179132_a(true);
         RenderHelper.func_74519_b();
         GlStateManager.func_179114_b(-135.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179114_b(-((float)Math.atan(0.0D)) * 20.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.func_179109_b(0.0F, 0.0F, 0.0F);
         RenderManager renderManager = this.mc.func_175598_ae();
         renderManager.func_178631_a(180.0F);
         renderManager.func_178633_a(false);
         renderManager.func_188391_a(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
         renderManager.func_178633_a(true);
         RenderHelper.func_74518_a();
         GlStateManager.func_179147_l();
         GlStateManager.func_179097_i();
         GlStateManager.func_179132_a(false);
      }
   }
}
