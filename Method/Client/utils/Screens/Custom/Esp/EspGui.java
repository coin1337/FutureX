package Method.Client.utils.Screens.Custom.Esp;

import Method.Client.Main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.lwjgl.input.Mouse;

public final class EspGui extends GuiScreen {
   private static EspGui.ListGui listGui;
   private EspGui.ListGui AllMobs;
   private GuiTextField MobFieldName;
   private GuiButton addButton;
   private GuiButton removeButton;
   private GuiButton doneButton;
   private String MobToAdd;
   private String MobToRemove;
   public static ArrayList<String> mobs = new ArrayList();
   public static ArrayList<String> allmobs = new ArrayList();

   public boolean func_73868_f() {
      return false;
   }

   public static List<String> RemoveMobs(List<String> input) {
      allmobs.clear();
      Iterator var1 = EntityList.func_180124_b().iterator();

      while(var1.hasNext()) {
         ResourceLocation resourceLocation = (ResourceLocation)var1.next();
         if (!input.contains(resourceLocation.toString())) {
            allmobs.add(resourceLocation.toString());
         }
      }

      return allmobs;
   }

   public static List<String> MobSearch(String text) {
      ArrayList<String> Temp = new ArrayList();
      Iterator var2 = EntityList.func_180124_b().iterator();

      while(var2.hasNext()) {
         ResourceLocation object = (ResourceLocation)var2.next();
         String s = object.toString();
         if (object.toString().contains(text)) {
            Temp.add(s);
         }
      }

      return Temp;
   }

   public void func_73866_w_() {
      listGui = new EspGui.ListGui(this.field_146297_k, this, mobs, this.field_146294_l - this.field_146294_l / 3, 0);
      this.AllMobs = new EspGui.ListGui(this.field_146297_k, this, RemoveMobs(mobs), 0, 0);
      this.MobFieldName = new GuiTextField(1, this.field_146297_k.field_71466_p, 64, this.field_146295_m - 55, 150, 18);
      this.field_146292_n.add(this.addButton = new GuiButton(0, 214, this.field_146295_m - 56, 30, 20, "Add"));
      this.field_146292_n.add(this.removeButton = new GuiButton(1, this.field_146294_l - 300, this.field_146295_m - 56, 100, 20, "Remove Selected"));
      this.field_146292_n.add(new GuiButton(2, this.field_146294_l - 108, 8, 100, 20, "Remove All"));
      this.field_146292_n.add(new GuiButton(20, this.field_146294_l - 308, 8, 100, 20, "Add Passive"));
      this.field_146292_n.add(new GuiButton(40, this.field_146294_l - 208, 8, 100, 20, "Add Hostile"));
      this.field_146292_n.add(this.doneButton = new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m - 28, "Done"));
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146124_l) {
         Iterator var2;
         ResourceLocation resourceLocation;
         switch(button.field_146127_k) {
         case 0:
            if (this.AllMobs.selected >= 0 && this.AllMobs.selected <= this.AllMobs.list.size() && !this.MobToAdd.isEmpty()) {
               mobs.add(this.MobToAdd);
               allmobs.remove(this.MobToAdd);
               this.MobToAdd = "";
            }
            break;
         case 1:
            mobs.remove(listGui.selected);
            allmobs.add(this.MobToRemove);
            break;
         case 2:
            this.field_146297_k.func_147108_a(new GuiYesNo(this, "Reset to Defaults", "Are you sure?", 0));
            break;
         case 3:
            this.field_146297_k.func_147108_a(Main.ClickGui);
            break;
         case 20:
            if (this.field_146297_k.field_71441_e != null) {
               var2 = EntityList.func_180124_b().iterator();

               while(var2.hasNext()) {
                  resourceLocation = (ResourceLocation)var2.next();
                  if (EntityList.func_188429_b(resourceLocation, this.field_146297_k.field_71441_e) instanceof IAnimals && !(EntityList.func_188429_b(resourceLocation, this.field_146297_k.field_71441_e) instanceof IMob) && !listGui.list.contains(resourceLocation.toString())) {
                     listGui.list.add(resourceLocation.toString());
                  }
               }
            }
            break;
         case 40:
            if (this.field_146297_k.field_71441_e != null) {
               var2 = EntityList.func_180124_b().iterator();

               while(var2.hasNext()) {
                  resourceLocation = (ResourceLocation)var2.next();
                  if (EntityList.func_188429_b(resourceLocation, this.field_146297_k.field_71441_e) instanceof IMob && !listGui.list.contains(resourceLocation.toString())) {
                     listGui.list.add(resourceLocation.toString());
                  }
               }
            }
         }

      }
   }

   public void func_73878_a(boolean result, int id) {
      if (id == 0 && result) {
         mobs.clear();
      }

      super.func_73878_a(result, id);
      this.field_146297_k.func_147108_a(this);
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      int mouseX = Mouse.getEventX() * this.field_146294_l / this.field_146297_k.field_71443_c;
      int mouseY = this.field_146295_m - Mouse.getEventY() * this.field_146295_m / this.field_146297_k.field_71440_d - 1;
      listGui.handleMouseInput(mouseX, mouseY);
      this.AllMobs.handleMouseInput(mouseX, mouseY);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      this.MobFieldName.func_146192_a(mouseX, mouseY, mouseButton);
      if (mouseX < 550 || mouseX > this.field_146294_l - 50 || mouseY < 32 || mouseY > this.field_146295_m - 64) {
         listGui.selected = -1;
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      this.MobFieldName.func_146201_a(typedChar, keyCode);
      if (keyCode == 28) {
         this.func_146284_a(this.addButton);
      } else if (keyCode == 211) {
         this.func_146284_a(this.removeButton);
      } else if (keyCode == 1) {
         this.func_146284_a(this.doneButton);
      }

      if (!this.MobFieldName.func_146179_b().isEmpty()) {
         this.AllMobs.list = MobSearch(this.MobFieldName.func_146179_b());
      } else {
         this.AllMobs.list = RemoveMobs(mobs);
      }

   }

   public static ArrayList<String> Getmobs() {
      return new ArrayList(listGui.list);
   }

   public void func_73876_c() {
      this.MobFieldName.func_146178_a();
      if (listGui.selected >= 0 && listGui.selected <= listGui.list.size()) {
         this.MobToRemove = (String)listGui.list.get(listGui.selected);
      }

      if (this.AllMobs.selected >= 0 && this.AllMobs.selected < this.AllMobs.list.size()) {
         this.MobToAdd = (String)this.AllMobs.list.get(this.AllMobs.selected);
      }

      this.addButton.field_146124_l = this.MobToAdd != null;
      this.removeButton.field_146124_l = listGui.selected >= 0 && listGui.selected < listGui.list.size();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146297_k.field_71466_p, "Mob (" + listGui.getSize() + ")", this.field_146294_l / 2, 12, 16777215);
      listGui.drawScreen(mouseX, mouseY, partialTicks);
      this.AllMobs.drawScreen(mouseX, mouseY, partialTicks);
      this.MobFieldName.func_146194_f();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (this.MobFieldName.func_146179_b().isEmpty() && !this.MobFieldName.func_146206_l()) {
         this.func_73731_b(this.field_146297_k.field_71466_p, "Mob name", 68, this.field_146295_m - 50, 8421504);
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
   }

   private static class ListGui extends GuiScrollingList {
      private final Minecraft mc;
      private List<String> list;
      private int selected = -1;
      private int offsetx;

      public ListGui(Minecraft mc, EspGui screen, List<String> list, int offsetx, int offsety) {
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
         FontRenderer fr = this.mc.field_71466_p;
         GlStateManager.func_179094_E();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);

         try {
            int scale = 13;
            if (name.contains("giant") || name.contains("ghast") || name.contains("ender_dragon")) {
               scale = 5;
            }

            GuiInventory.func_147046_a(58 + this.offsetx, slotTop + 13, scale, 0.0F, 0.0F, (EntityLivingBase)Objects.requireNonNull(EntityList.func_188429_b(new ResourceLocation(name), this.mc.field_71441_e)));
         } catch (Exception var9) {
         }

         GlStateManager.func_179121_F();
         fr.func_78276_b(" (" + name + ")", 68 + this.offsetx, slotTop + 2, 15790320);
      }
   }
}
