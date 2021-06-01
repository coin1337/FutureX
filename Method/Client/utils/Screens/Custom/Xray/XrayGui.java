package Method.Client.utils.Screens.Custom.Xray;

import Method.Client.Main;
import Method.Client.managers.FileManager;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.render.Xray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.lwjgl.input.Mouse;

public final class XrayGui extends GuiScreen {
   private XrayGui.ListGui listGui;
   private XrayGui.ListGui Allblocks;
   private GuiTextField blockNameField;
   private GuiButton addButton;
   private GuiButton removeButton;
   private GuiButton doneButton;
   private Block blockToAdd;

   public boolean func_73868_f() {
      return false;
   }

   public void func_73866_w_() {
      this.listGui = new XrayGui.ListGui(this.field_146297_k, this, XrayGuiSettings.getBlockNames(), 500, 0);
      this.Allblocks = new XrayGui.ListGui(this.field_146297_k, this, XrayGuiSettings.getAllBlockNames(XrayGuiSettings.getBlockNames()), 0, 0);
      this.blockNameField = new GuiTextField(1, this.field_146297_k.field_71466_p, 64, this.field_146295_m - 55, 150, 18);
      this.field_146292_n.add(this.addButton = new GuiButton(0, 214, this.field_146295_m - 56, 30, 20, "Add"));
      this.field_146292_n.add(this.removeButton = new GuiButton(1, this.field_146294_l - 300, this.field_146295_m - 56, 100, 20, "Remove Selected"));
      this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 108, 8, 100, 20, "Reset to Defaults"));
      this.field_146292_n.add(this.doneButton = new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m - 28, "Done"));
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146124_l) {
         switch(button.field_146127_k) {
         case 0:
            XrayGuiSettings.add(this.blockToAdd);
            this.blockNameField.func_146180_a("");
            this.Allblocks.list = XrayGuiSettings.getAllBlockNames(XrayGuiSettings.getBlockNames());
            break;
         case 1:
            XrayGuiSettings.remove(this.listGui.selected);
            break;
         case 2:
            this.field_146297_k.func_147108_a(new GuiYesNo(this, "Reset to Defaults", "Are you sure?", 0));
            break;
         case 3:
            this.field_146297_k.func_147108_a(Main.ClickGui);
            Module xray = ModuleManager.getModuleByName("Xray");
            if (xray.isToggled()) {
               Xray.blockNames = new ArrayList(XrayGuiSettings.getBlockNames());
               this.field_146297_k.field_71438_f.func_72712_a();
            }

            FileManager.saveXRayData();
         }

      }
   }

   public void func_73878_a(boolean result, int id) {
      if (id == 0 && result) {
         XrayGuiSettings.resetToDefaults();
      }

      super.func_73878_a(result, id);
      this.field_146297_k.func_147108_a(this);
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      int mouseX = Mouse.getEventX() * this.field_146294_l / this.field_146297_k.field_71443_c;
      int mouseY = this.field_146295_m - Mouse.getEventY() * this.field_146295_m / this.field_146297_k.field_71440_d - 1;
      this.listGui.handleMouseInput(mouseX, mouseY);
      this.Allblocks.handleMouseInput(mouseX, mouseY);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      this.blockNameField.func_146192_a(mouseX, mouseY, mouseButton);
      if (mouseX < 550 || mouseX > this.field_146294_l - 50 || mouseY < 32 || mouseY > this.field_146295_m - 64) {
         this.listGui.selected = -1;
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      this.blockNameField.func_146201_a(typedChar, keyCode);
      if (keyCode == 28) {
         this.func_146284_a(this.addButton);
      } else if (keyCode == 211) {
         this.func_146284_a(this.removeButton);
      } else if (keyCode == 1) {
         this.func_146284_a(this.doneButton);
      }

      if (!this.blockNameField.func_146179_b().isEmpty()) {
         this.Allblocks.list = XrayGuiSettings.SearchBlocksAll(XrayGuiSettings.getBlockNames(), this.blockNameField.func_146179_b());
      } else {
         this.Allblocks.list = XrayGuiSettings.getAllBlockNames(XrayGuiSettings.getBlockNames());
      }

   }

   public void func_73876_c() {
      this.blockNameField.func_146178_a();
      this.blockToAdd = Block.func_149684_b(this.blockNameField.func_146179_b());
      if (this.blockNameField.func_146179_b().isEmpty() && this.Allblocks.selected >= 0 && this.Allblocks.selected < this.Allblocks.list.size()) {
         this.blockToAdd = Block.func_149684_b((String)this.Allblocks.list.get(this.Allblocks.selected));
      }

      this.addButton.field_146124_l = this.blockToAdd != null;
      this.removeButton.field_146124_l = this.listGui.selected >= 0 && this.listGui.selected < this.listGui.list.size();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146297_k.field_71466_p, "XRAY (" + this.listGui.getSize() + ")", this.field_146294_l / 2, 12, 16777215);
      this.listGui.drawScreen(mouseX, mouseY, partialTicks);
      this.Allblocks.drawScreen(mouseX, mouseY, partialTicks);
      this.blockNameField.func_146194_f();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (this.blockNameField.func_146179_b().isEmpty() && !this.blockNameField.func_146206_l()) {
         this.func_73731_b(this.field_146297_k.field_71466_p, "block name or ID", 68, this.field_146295_m - 50, 8421504);
      }

      func_73734_a(48, this.field_146295_m - 56, 64, this.field_146295_m - 36, -6250336);
      func_73734_a(49, this.field_146295_m - 55, 64, this.field_146295_m - 37, -16777216);
      func_73734_a(214, this.field_146295_m - 56, 244, this.field_146295_m - 55, -6250336);
      func_73734_a(214, this.field_146295_m - 37, 244, this.field_146295_m - 36, -6250336);
      func_73734_a(244, this.field_146295_m - 56, 246, this.field_146295_m - 36, -6250336);
      func_73734_a(214, this.field_146295_m - 55, 243, this.field_146295_m - 52, -16777216);
      func_73734_a(214, this.field_146295_m - 40, 243, this.field_146295_m - 37, -16777216);
      func_73734_a(215, this.field_146295_m - 55, 216, this.field_146295_m - 37, -16777216);
      func_73734_a(242, this.field_146295_m - 55, 245, this.field_146295_m - 37, -16777216);
      this.listGui.renderIconAndGetName(new ItemStack(this.blockToAdd), this.field_146295_m - 52);
      this.Allblocks.renderIconAndGetName(new ItemStack(this.blockToAdd), this.field_146295_m - 52);
   }

   private static class ListGui extends GuiScrollingList {
      private final Minecraft mc;
      private List<String> list;
      private int selected = -1;
      private int offsetx;

      public ListGui(Minecraft mc, XrayGui screen, List<String> list, int offsetx, int offsety) {
         super(mc, screen.field_146294_l / 4, screen.field_146295_m, 32 + offsety, screen.field_146295_m - 64, 50 + offsetx, 16, screen.field_146294_l, screen.field_146295_m);
         this.offsetx = offsetx;
         this.mc = mc;
         this.list = list;
      }

      protected int getSize() {
         return this.list.size();
      }

      protected void elementClicked(int index, boolean doubleClick) {
         if (index >= 0 && index < this.list.size()) {
            this.selected = index;
         }

      }

      protected boolean isSelected(int index) {
         return index == this.selected;
      }

      protected void drawBackground() {
         Gui.func_73734_a(50 + this.offsetx, this.top, 66 + this.offsetx, this.bottom, -1);
      }

      protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
         String name = (String)this.list.get(slotIdx);
         ItemStack stack = new ItemStack((Block)Objects.requireNonNull(Block.func_149684_b(name)));
         FontRenderer fr = this.mc.field_71466_p;
         String displayName = this.renderIconAndGetName(stack, slotTop);
         if (displayName.equalsIgnoreCase("Air") && name.contains(":") && !name.endsWith(":")) {
            displayName = name.substring(name.lastIndexOf(":"));
         }

         fr.func_78276_b(displayName + " (" + name + ")", 68 + this.offsetx, slotTop + 2, 15790320);
      }

      private String renderIconAndGetName(ItemStack stack, int y) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b((float)(52 + this.offsetx), (float)y, 0.0F);
         GlStateManager.func_179139_a(0.75D, 0.75D, 0.75D);
         RenderHelper.func_74520_c();
         this.mc.func_175599_af().func_180450_b(stack, 0, 0);
         RenderHelper.func_74518_a();
         GlStateManager.func_179121_F();
         return stack.func_82833_r();
      }
   }
}
