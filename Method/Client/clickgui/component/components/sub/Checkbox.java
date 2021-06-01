package Method.Client.clickgui.component.components.sub;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.managers.Setting;
import Method.Client.module.misc.GuiModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Checkbox extends Component {
   private boolean hovered;
   private final Setting op;
   private final Button parent;
   private int offset;
   private int x;
   private int y;

   public Checkbox(Setting option, Button button, int offset) {
      this.op = option;
      this.parent = button;
      this.x = button.parent.getX() + button.parent.getWidth();
      this.y = button.parent.getY() + button.offset;
      this.offset = offset;
   }

   public void renderComponent() {
      GL11.glEnable(3042);
      Gui.func_73734_a(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, this.hovered ? GuiModule.Hover.getcolor() : GuiModule.innercolor.getcolor());
      Gui.func_73734_a(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -1508830959);
      GL11.glPushMatrix();
      Button.fontSelectButton(this.op.getName(), (float)(this.parent.parent.getX() + 10 + 4), (float)(this.parent.parent.getY() + this.offset + 2), -1);
      GlStateManager.func_179121_F();
      Gui.func_73734_a(this.parent.parent.getX() + 3 + 4, this.parent.parent.getY() + this.offset + 3, this.parent.parent.getX() + 9 + 4, this.parent.parent.getY() + this.offset + 9, -1499883111);
      if (this.op.getValBoolean()) {
         Gui.func_73734_a(this.parent.parent.getX() + 4 + 4, this.parent.parent.getY() + this.offset + 4, this.parent.parent.getX() + 8 + 4, this.parent.parent.getY() + this.offset + 8, -674009);
      }

   }

   public void setOff(int newOff) {
      this.offset = newOff;
   }

   public String getName() {
      return this.op.getName();
   }

   public void updateComponent(int mouseX, int mouseY) {
      this.y = this.parent.parent.getY() + this.offset;
      this.x = this.parent.parent.getX();
      this.hovered = this.isMouseOnButton(mouseX, mouseY);
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.hovered && button == 0 && this.parent.open) {
         this.op.setValBoolean(!this.op.getValBoolean());
      }

   }

   public boolean isMouseOnButton(int x, int y) {
      return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + this.parent.parent.getBarHeight();
   }
}
