package Method.Client.clickgui.component.components.sub;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.module.Module;
import Method.Client.module.misc.GuiModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class VisibleButton extends Component {
   private boolean hovered;
   private final Button parent;
   private int offset;
   private int x;
   private int y;
   private final Module mod;

   public VisibleButton(Button button, Module mod, int offset) {
      this.parent = button;
      this.mod = mod;
      this.x = button.parent.getX() + button.parent.getWidth();
      this.y = button.parent.getY() + button.offset;
      this.offset = offset;
   }

   public void setOff(int newOff) {
      this.offset = newOff;
   }

   public void renderComponent() {
      GL11.glEnable(3042);
      Gui.func_73734_a(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, this.hovered ? GuiModule.Hover.getcolor() : GuiModule.innercolor.getcolor());
      Gui.func_73734_a(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -1508830959);
      GL11.glPushMatrix();
      Button.fontSelectButton("Visible: " + this.mod.visible, (float)(this.parent.parent.getX() + 7), (float)(this.parent.parent.getY() + this.offset + 2), -1);
      GlStateManager.func_179121_F();
   }

   public void updateComponent(int mouseX, int mouseY) {
      this.y = this.parent.parent.getY() + this.offset;
      this.x = this.parent.parent.getX();
      this.hovered = this.isMouseOnButton(mouseX, mouseY);
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
         this.mod.visible = !this.mod.visible;
      }

   }

   public boolean isMouseOnButton(int x, int y) {
      return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + this.parent.parent.getBarHeight();
   }
}
