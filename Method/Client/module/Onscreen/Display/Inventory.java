package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Inventory extends Module {
   static Setting BgColor;
   static Setting background;
   static Setting Hotbar;
   static Setting Xcarry;
   static Setting xpos;
   static Setting ypos;

   public Inventory() {
      super("Inventory", 0, Category.ONSCREEN, "Inventory");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(BgColor = new Setting("BgColor", this, 0.22D, 0.88D, 0.22D, 0.22D));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(Hotbar = new Setting("Hotbar", this, false));
      Main.setmgr.add(Xcarry = new Setting("Xcarry", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 110.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("InventorySET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("InventorySET", false);
   }

   public static class InventoryRUN extends PinableFrame {
      public InventoryRUN() {
         super("InventorySET", new String[0], (int)Inventory.ypos.getValDouble(), (int)Inventory.xpos.getValDouble());
      }

      public void setup() {
         this.x = (int)Inventory.xpos.getValDouble();
         this.y = (int)Inventory.ypos.getValDouble();
      }

      public void Ongui() {
         if (!this.getDrag()) {
            this.x = (int)Inventory.xpos.getValDouble();
            this.y = (int)Inventory.ypos.getValDouble();
         } else {
            Inventory.xpos.setValDouble((double)this.x);
            Inventory.ypos.setValDouble((double)this.y);
         }

      }

      public void onRenderGameOverlay(Text event) {
         if (this.mc.field_71439_g != null) {
            RenderHelper.func_74520_c();
            if (Inventory.background.getValBoolean()) {
               RenderUtils.drawRectDouble((double)this.getX(), (double)this.getY(), (double)(this.getX() + this.getWidth() + 60), (double)(this.getY() + 50 + (Inventory.Hotbar.getValBoolean() ? 25 : 0)), Inventory.BgColor.getcolor());
            }

            int i;
            ItemStack itemStack;
            int offsetX;
            int offsetY;
            for(i = 0; i < 27; ++i) {
               itemStack = (ItemStack)this.mc.field_71439_g.field_71071_by.field_70462_a.get(i + 9);
               offsetX = this.getX() + i % 9 * 16;
               offsetY = this.getY() + i / 9 * 16;
               this.mc.func_175599_af().func_180450_b(itemStack, offsetX, offsetY);
               this.mc.func_175599_af().func_180453_a(this.mc.field_71466_p, itemStack, offsetX, offsetY, (String)null);
            }

            if (Inventory.Hotbar.getValBoolean()) {
               for(i = 0; i < 9; ++i) {
                  itemStack = (ItemStack)this.mc.field_71439_g.field_71071_by.field_70462_a.get(i);
                  offsetX = this.getX() + i % 9 * 16;
                  offsetY = this.getY() + 48;
                  this.mc.func_175599_af().func_180450_b(itemStack, offsetX, offsetY);
                  this.mc.func_175599_af().func_180453_a(this.mc.field_71466_p, itemStack, offsetX, offsetY, (String)null);
               }
            }

            if (Inventory.Xcarry.getValBoolean()) {
               for(i = 0; i < 5; ++i) {
                  itemStack = (ItemStack)this.mc.field_71439_g.field_71069_bz.func_75138_a().get(i);
                  offsetX = this.getX() + i * 16;
                  offsetY = this.getY() + 60;
                  this.mc.func_175599_af().func_180450_b(itemStack, offsetX, offsetY);
                  this.mc.func_175599_af().func_180453_a(this.mc.field_71466_p, itemStack, offsetX, offsetY, (String)null);
               }
            }

            RenderHelper.func_74518_a();
            this.mc.func_175599_af().field_77023_b = 0.0F;
            super.onRenderGameOverlay(event);
         }
      }
   }
}
