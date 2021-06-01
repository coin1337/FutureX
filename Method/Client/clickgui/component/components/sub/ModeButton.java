package Method.Client.clickgui.component.components.sub;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.managers.Setting;
import Method.Client.module.misc.GuiModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ModeButton extends Component {
   private boolean hovered;
   private final Button parent;
   private final Setting set;
   private int offset;
   private int x;
   private int y;
   private int modeIndex;

   public ModeButton(Setting set, Button button, int offset) {
      this.set = set;
      this.parent = button;
      this.x = button.parent.getX() + button.parent.getWidth();
      this.y = button.parent.getY() + button.offset;
      this.offset = offset;
      this.modeIndex = 0;
   }

   public void setOff(int newOff) {
      this.offset = newOff;
   }

   public void renderComponent() {
      GL11.glEnable(3042);
      Gui.func_73734_a(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, this.hovered ? GuiModule.Hover.getcolor() : GuiModule.innercolor.getcolor());
      Gui.func_73734_a(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -1508830959);
      GL11.glPushMatrix();
      Button.fontSelectButton(this.set.getName() + ": " + this.set.getValString(), (float)(this.parent.parent.getX() + 7), (float)(this.parent.parent.getY() + this.offset + 3), -1);
      GlStateManager.func_179121_F();
   }

   public void updateComponent(int mouseX, int mouseY) {
      this.y = this.parent.parent.getY() + this.offset;
      this.x = this.parent.parent.getX();
      this.hovered = this.isMouseOnButton(mouseX, mouseY);
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.hovered && button == 0 && this.parent.open) {
         this.modeIndex = this.modeIndex + 1 >= this.set.getOptions().size() ? 0 : this.modeIndex + 1;
         this.set.setValString((String)this.set.getOptions().get(this.modeIndex));
      }

      if (this.hovered && button == 1 && this.parent.open) {
         this.modeIndex = this.modeIndex - 1 < 0 ? this.set.getOptions().size() - 1 : this.modeIndex - 1;
         this.set.setValString((String)this.set.getOptions().get(this.modeIndex));
      }

   }

   public boolean isMouseOnButton(int x, int y) {
      return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + this.parent.parent.getBarHeight();
   }
}
