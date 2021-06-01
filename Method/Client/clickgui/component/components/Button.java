package Method.Client.clickgui.component.components;

import Method.Client.Main;
import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.Frame;
import Method.Client.clickgui.component.components.sub.Checkbox;
import Method.Client.clickgui.component.components.sub.ColorPicker;
import Method.Client.clickgui.component.components.sub.Guibutton;
import Method.Client.clickgui.component.components.sub.Keybind;
import Method.Client.clickgui.component.components.sub.ModeButton;
import Method.Client.clickgui.component.components.sub.Slider;
import Method.Client.clickgui.component.components.sub.VisibleButton;
import Method.Client.managers.Setting;
import Method.Client.module.Module;
import Method.Client.module.misc.GuiModule;
import Method.Client.utils.font.CFont;
import Method.Client.utils.font.CFontRenderer;
import Method.Client.utils.visual.RenderUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Button extends Component {
   public Module mod;
   public Frame parent;
   public int offset;
   private boolean isHovered;
   private boolean saveisHovered;
   private boolean deleteisHovered;
   private double Animate;
   private final ArrayList<Component> subcomponents;
   private final ArrayList<Component> Inviscomponents;
   public static CFontRenderer ButtonFont;
   public static CFontRenderer SubcomponentFont;
   public boolean open;
   public int opentiming;
   protected Minecraft mc = Minecraft.func_71410_x();

   public Button(Module mod, Frame parent, int offset) {
      this.mod = mod;
      this.parent = parent;
      this.offset = offset;
      this.saveisHovered = false;
      this.deleteisHovered = false;
      this.subcomponents = new ArrayList();
      this.Inviscomponents = new ArrayList();
      this.open = false;
      int opY = offset + 12;
      if (Main.setmgr.getSettingsByMod(mod) != null) {
         Iterator var5 = Main.setmgr.getSettingsByMod(mod).iterator();

         while(var5.hasNext()) {
            Setting s = (Setting)var5.next();
            if (s.isCombo()) {
               this.subcomponents.add(new ModeButton(s, this, opY));
               opY += 12;
            }

            if (s.isSlider()) {
               this.subcomponents.add(new Slider(s, this, opY));
               opY += 12;
            }

            if (s.isCheck()) {
               this.subcomponents.add(new Checkbox(s, this, opY));
               opY += 12;
            }

            if (s.isColor()) {
               this.subcomponents.add(new ColorPicker(s, this, opY));
               opY += 12;
            }

            if (s.isGui()) {
               this.subcomponents.add(new Guibutton(s, this, opY, s.getScreen()));
               opY += 12;
            }
         }
      }

      this.subcomponents.add(new Keybind(this, opY));
      this.subcomponents.add(new VisibleButton(this, mod, opY));
   }

   public static void updateFont() {
      if (GuiModule.Button.getValString().equalsIgnoreCase("Arial")) {
         ButtonFont = CFont.afontRenderer22;
      }

      if (GuiModule.Button.getValString().equalsIgnoreCase("Times")) {
         ButtonFont = CFont.tfontRenderer22;
      }

      if (GuiModule.Button.getValString().equalsIgnoreCase("Impact")) {
         ButtonFont = CFont.ifontRenderer22;
      }

      if (GuiModule.Subcomponents.getValString().equalsIgnoreCase("Arial")) {
         SubcomponentFont = CFont.afontRenderer18;
      }

      if (GuiModule.Subcomponents.getValString().equalsIgnoreCase("Times")) {
         SubcomponentFont = CFont.tfontRenderer18;
      }

      if (GuiModule.Subcomponents.getValString().equalsIgnoreCase("Impact")) {
         SubcomponentFont = CFont.ifontRenderer18;
      }

   }

   public void setOff(int newOff) {
      this.offset = newOff;
      int opY = this.offset + 12;

      for(Iterator var3 = this.subcomponents.iterator(); var3.hasNext(); opY += 12) {
         Component comp = (Component)var3.next();
         comp.setOff(opY);
      }

   }

   public void renderComponent() {
      GL11.glEnable(3042);
      if (this.getCategory().equalsIgnoreCase("PROFILES")) {
         Gui.func_73734_a(this.parent.getX() + 50, this.parent.getY() + this.offset, this.parent.getX() + 60, this.parent.getY() + 12 + this.offset, 1712497170);
         Gui.func_73734_a(this.parent.getX() + 65, this.parent.getY() + this.offset, this.parent.getX() + 75, this.parent.getY() + 12 + this.offset, 1727543858);
      }

      Gui.func_73734_a(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? GuiModule.Hover.getcolor() : GuiModule.Background.getcolor());
      Gui.func_73734_a(this.parent.getX(), this.parent.getY() + this.offset, (int)((double)this.parent.getX() + this.Animate), this.parent.getY() + 12 + this.offset, GuiModule.ColorAni.getcolor());
      GlStateManager.func_179094_E();
      if (this.getCategory().equalsIgnoreCase("PROFILES")) {
         fontSelect("S   D", (float)(this.parent.getX() + 2) + 49.0F, (float)((double)(this.parent.getY() + this.offset) + 2.5D), this.mod.isToggled() ? -1498924494 : -1);
      }

      fontSelect(this.mod.getName(), (float)(this.parent.getX() + 2), (float)((double)(this.parent.getY() + this.offset) + 2.5D) - 2.0F, this.mod.isToggled() ? -1498924494 : -1);
      if (this.subcomponents.size() > 2) {
         fontSelect(this.open ? "-" : "+", (float)(this.parent.getX() + this.parent.getWidth() - 10), (float)((double)(this.parent.getY() + this.offset) + 2.5D) - 2.0F, -1);
      }

      GlStateManager.func_179121_F();
      if (this.open && !this.subcomponents.isEmpty()) {
         this.subcomponents.forEach(Component::renderComponent);
         if (GuiModule.border.getValBoolean()) {
            RenderUtils.drawRectOutline((double)(this.parent.getX() + 4), (double)(this.parent.getY() + this.offset + 10), (double)(this.parent.getX() + this.parent.getWidth()), (double)(this.parent.getY() + this.offset + (this.subcomponents.size() + 1) * 12), 1.0D, GuiModule.Highlight.getcolor());
         }
      }

   }

   public void RenderTooltip() {
      if (!this.subcomponents.isEmpty()) {
         this.subcomponents.forEach(Component::RenderTooltip);
      }

      if (this.isHovered) {
         Gui.func_73734_a(0, (int)((double)this.mc.field_71440_d / 2.085D), (int)((double)this.mod.getTooltip().length() * 5.1D), (int)((double)this.mc.field_71440_d / 2.085D) + 10, 1294082594);
         fontSelect(this.mod.getTooltip(), 0.0F, (float)((double)this.mc.field_71440_d / 2.085D), this.mod.isToggled() ? -1499883111 : -1);
      }

   }

   public int getHeight() {
      return this.open ? 12 * (this.subcomponents.size() + 1) : 12;
   }

   public int gety() {
      return this.parent.getY() + this.offset;
   }

   public String getName() {
      return this.mod.getName();
   }

   public String getCategory() {
      return this.mod.getCategory().toString();
   }

   public void updateComponent(int mouseX, int mouseY) throws IOException {
      this.isHovered = this.isMouseOnButton(mouseX, mouseY);
      if (this.getCategory().equalsIgnoreCase("PROFILES")) {
         this.saveisHovered = this.ProfileisMouseOnButton(mouseX, mouseY, false);
         this.deleteisHovered = this.ProfileisMouseOnButton(mouseX, mouseY, true);
      }

      if (this.isHovered && this.Animate < (double)this.parent.getWidth()) {
         this.Animate += GuiModule.Anispeed.getValDouble();
      }

      if (!this.isHovered && this.Animate > 0.0D) {
         this.Animate -= GuiModule.Anispeed.getValDouble();
      }

      if (!this.subcomponents.isEmpty()) {
         Iterator var3 = this.subcomponents.iterator();

         while(var3.hasNext()) {
            Component comp = (Component)var3.next();
            comp.updateComponent(mouseX, mouseY);
         }
      }

   }

   public static void fontSelect(String name, float v, float v1, int i) {
      if (GuiModule.Button.getValString().equalsIgnoreCase("MC")) {
         Minecraft.func_71410_x().field_71466_p.func_175063_a(name, (float)((int)v), (float)((int)v1), i);
      } else {
         ButtonFont.drawStringWithShadow(name, (double)v, (double)v1, i);
      }

   }

   public static void fontSelectButton(String name, float v, float v1, int i) {
      if (GuiModule.Subcomponents.getValString().equalsIgnoreCase("MC")) {
         Minecraft.func_71410_x().field_71466_p.func_175063_a(name, (float)((int)v), (float)((int)v1), i);
      } else {
         SubcomponentFont.drawStringWithShadow(name, (double)v, (double)v1, i);
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.isHovered && button == 0) {
         if (this.getCategory().equalsIgnoreCase("PROFILES")) {
            if (this.saveisHovered) {
               this.mod.setsave();
            } else {
               if (this.deleteisHovered) {
                  this.mod.setdelete();
                  return;
               }

               this.mod.toggle();
            }
         } else {
            this.mod.toggle();
         }
      }

      if (this.isHovered && button == 1) {
         this.open = !this.open;
         this.parent.refresh();
      }

      Iterator var4 = this.subcomponents.iterator();

      while(var4.hasNext()) {
         Component comp = (Component)var4.next();
         comp.mouseClicked(mouseX, mouseY, button);
      }

      this.CheckInvis();
   }

   private void CheckInvis() {
      if (Main.setmgr.getSettingsByMod(this.mod) != null) {
         Iterator var1 = Main.setmgr.getSettingsByMod(this.mod).iterator();

         while(true) {
            Setting s;
            do {
               if (!var1.hasNext()) {
                  return;
               }

               s = (Setting)var1.next();
            } while(s.getDependant() == null);

            double index = 0.0D;
            Component Compnt = null;
            Iterator var6 = ((ArrayList)Objects.requireNonNull(s.getDependant().isCheck() ? (s.getDependant().getValBoolean() ? this.Inviscomponents : this.subcomponents) : (s.getDependant().isCombo() ? (s.getDependant().getValString().equalsIgnoreCase(s.getselected()) ? this.Inviscomponents : this.subcomponents) : null))).iterator();

            while(var6.hasNext()) {
               Component com = (Component)var6.next();
               if (com.getName().equalsIgnoreCase(s.getName())) {
                  Compnt = com;
                  index = s.GetIndex();
                  break;
               }
            }

            if (Compnt != null) {
               this.Updateinvis(Compnt, index);
            }
         }
      }
   }

   private void Updateinvis(Component compnt, double index) {
      if (this.subcomponents.contains(compnt)) {
         this.subcomponents.remove(compnt);
      } else {
         this.subcomponents.add((int)index, compnt);
      }

      if (this.Inviscomponents.contains(compnt)) {
         this.Inviscomponents.remove(compnt);
      } else {
         this.Inviscomponents.add(compnt);
      }

      this.parent.refresh();
   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
      Iterator var4 = this.subcomponents.iterator();

      while(var4.hasNext()) {
         Component comp = (Component)var4.next();
         comp.mouseReleased(mouseX, mouseY, mouseButton);
      }

   }

   public void keyTyped(char typedChar, int key) {
      Iterator var3 = this.subcomponents.iterator();

      while(var3.hasNext()) {
         Component comp = (Component)var3.next();
         comp.keyTyped(typedChar, key);
      }

   }

   public boolean isMouseOnButton(int x, int y) {
      return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
   }

   public boolean ProfileisMouseOnButton(int x, int y, boolean delete) {
      return x > this.parent.getX() + (delete ? 65 : 50) && x < this.parent.getX() + (delete ? 75 : 60) && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
   }
}
