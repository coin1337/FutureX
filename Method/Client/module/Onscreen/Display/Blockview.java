package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Blockview extends Module {
   static Setting xpos;
   static Setting ypos;
   static Setting Background;
   static Setting Shadow;
   static Setting Frame;
   static Setting Color;
   static Setting TextColor;
   static Setting FontSize;
   static Setting Text;
   static Setting Image;

   public Blockview() {
      super("Blockview", 0, Category.ONSCREEN, "BlockOverlay");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(Color = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.01D, 0.0D, 0.0D, 0.55D));
      Main.setmgr.add(Background = new Setting("Background", this, false));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(Text = new Setting("Text", this, true));
      Main.setmgr.add(Image = new Setting("Image", this, true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 10.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("BlockviewSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("BlockviewSET", false);
   }

   public static class BlockviewRUN extends PinableFrame {
      public BlockviewRUN() {
         super("BlockviewSET", new String[0], (int)Blockview.ypos.getValDouble(), (int)Blockview.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Blockview.xpos, Blockview.ypos, Blockview.Frame, Blockview.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Blockview.xpos, Blockview.ypos, Blockview.Frame, Blockview.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         if (this.mc.field_71476_x.field_72313_a == Type.BLOCK) {
            Block block = this.mc.field_71441_e.func_180495_p(this.mc.field_71476_x.func_178782_a()).func_177230_c();
            if (Block.func_149682_b(block) == 0) {
               return;
            }

            if (Blockview.Text.getValBoolean()) {
               if (Blockview.Background.getValBoolean()) {
                  Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Blockview.Frame, block.func_149732_F()), this.y + 22, Blockview.Color.getcolor());
               }

               this.fontSelect(Blockview.Frame, block.func_149732_F(), (float)this.getX(), (float)(this.getY() + 10), Blockview.TextColor.getcolor(), Blockview.Shadow.getValBoolean());
            }

            if (Blockview.Image.getValBoolean()) {
               GlStateManager.func_179094_E();
               GlStateManager.func_179109_b((float)(this.x + 8), (float)(this.y - 1), 0.0F);
               GlStateManager.func_179139_a(0.75D, 0.75D, 0.75D);
               RenderHelper.func_74520_c();
               this.mc.func_175599_af().func_180450_b(new ItemStack(block), 0, 0);
               RenderHelper.func_74518_a();
               GlStateManager.func_179121_F();
            }
         }

         super.onRenderGameOverlay(event);
      }
   }
}
