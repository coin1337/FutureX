package Method.Client.clickgui.component.components.sub;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.components.Button;
import Method.Client.managers.Setting;
import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ColorPicker extends Component {
   private final Setting set;
   private final Button parent;
   private int offset;
   private int x;
   private int y;
   private boolean dragging = false;
   private String localname;
   private int indexer = 0;
   private boolean hovered;
   public double renderWidth = 0.0D;

   public ColorPicker(Setting value, Button button, int offset) {
      this.set = value;
      this.parent = button;
      this.x = button.parent.getX() + button.parent.getWidth();
      this.y = button.parent.getY() + button.offset;
      this.offset = offset;
      this.localname = this.set.getName();
   }

   public void renderComponent() {
      GL11.glEnable(3042);
      Gui.func_73734_a(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, -1507712478);
      Gui.func_73734_a(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, this.set.getcolor());
      Gui.func_73734_a((int)((double)this.parent.parent.getX() + this.renderWidth), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + (int)this.renderWidth + 6, this.parent.parent.getY() + this.offset + 12, -1086045116);
      GlStateManager.func_179094_E();
      GlStateManager.func_179152_a(1.0F, 1.0F, 0.5F);
      Button.fontSelectButton(this.localname, (float)(this.parent.parent.getX() + 8), (float)(this.parent.parent.getY() + this.offset + 2), -1);
      GlStateManager.func_179121_F();
   }

   public void setOff(int newOff) {
      this.offset = newOff;
   }

   public String getName() {
      return this.set.getName();
   }

   public void updateComponent(int mouseX, int mouseY) {
      this.y = this.parent.parent.getY() + this.offset;
      this.x = this.parent.parent.getX();
      this.hovered = this.isMouseOnButton(mouseX, mouseY);
      double diff = (double)Math.min(this.parent.parent.getWidth(), Math.max(0, mouseX - this.x));
      double min = this.set.getMin();
      double max = this.set.getMax();
      switch(this.indexer) {
      case 0:
         this.renderWidth = 88.0D * (this.set.getValDouble() - min) / (max - min);
         this.localname = this.set.getName() + " Color";
         break;
      case 1:
         this.renderWidth = 88.0D * (this.set.getSat() - min) / (max - min);
         this.localname = this.set.getName() + " Saturation";
         break;
      case 2:
         this.localname = this.set.getName() + " Brightness";
         this.renderWidth = 88.0D * (this.set.getBri() - min) / (max - min);
         break;
      case 3:
         this.localname = this.set.getName() + " Alpha";
         this.renderWidth = 88.0D * (this.set.getAlpha() - min) / (max - min);
      }

      if (this.dragging) {
         double value = diff / 88.0D * (max - min) + min;
         switch(this.indexer) {
         case 0:
            this.set.setValDouble(diff == 0.0D ? this.set.getMin() : roundToPlace(value));
            break;
         case 1:
            this.set.setsaturation((float)(diff == 0.0D ? this.set.getMin() : roundToPlace(value)));
            break;
         case 2:
            this.set.setbrightness((float)(diff == 0.0D ? this.set.getMin() : roundToPlace(value)));
            break;
         case 3:
            this.set.setAlpha((float)(diff == 0.0D ? this.set.getMin() : roundToPlace(value)));
         }
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.hovered && button == 0 && this.parent.open) {
         this.dragging = true;
      }

      if (this.hovered && button == 1 && this.parent.open) {
         this.indexer = this.indexer == 3 ? 0 : this.indexer + 1;
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
      this.dragging = false;
   }

   public boolean isMouseOnButton(int x, int y) {
      return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + this.parent.parent.getBarHeight();
   }

   private static double roundToPlace(double value) {
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }
}
