package Method.Client.module.Onscreen;

import Method.Client.clickgui.component.Component;
import Method.Client.managers.Setting;
import Method.Client.utils.font.CFontRenderer;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public class PinableFrame {
   private final int width;
   public int defaultWidth;
   public int y;
   public int x;
   public int barHeight;
   private boolean isDragging;
   public int dragX;
   public int dragY;
   private boolean isPinned = false;
   public CFontRenderer FontRender = new CFontRenderer(new Font("Impact", 0, 18), true, false);
   public String title;
   public String[] text;
   protected Minecraft mc = Minecraft.func_71410_x();

   public static void Toggle(String s, boolean b) {
      Iterator var2 = OnscreenGUI.pinableFrames.iterator();

      while(var2.hasNext()) {
         PinableFrame i = (PinableFrame)var2.next();
         if (i.title.equals(s)) {
            i.setPinned(b);
            break;
         }
      }

   }

   public PinableFrame(String title, String[] text, int y, int x) {
      this.title = title;
      this.text = text;
      this.x = x;
      this.y = y;
      this.width = 88;
      this.defaultWidth = 88;
      this.barHeight = 13;
      this.dragX = 0;
      this.isDragging = false;
   }

   protected DecimalFormat getDecimalFormat(int i) {
      switch(i) {
      case 0:
         return new DecimalFormat("0");
      case 1:
         return new DecimalFormat("0.0");
      case 2:
         return new DecimalFormat("0.00");
      case 3:
         return new DecimalFormat("0.000");
      case 4:
         return new DecimalFormat("0.0000");
      case 5:
         return new DecimalFormat("0.00000");
      default:
         return null;
      }
   }

   protected void GetSetup(PinableFrame pinableFrame, Setting xpos, Setting ypos, Setting Frame, Setting FontSize) {
      pinableFrame.x = (int)xpos.getValDouble();
      pinableFrame.y = (int)ypos.getValDouble();
      if (Frame.getValString().equalsIgnoreCase("false") || Frame.getValString() == null) {
         Frame.setValString("Times");
      }

      pinableFrame.FontRender.setFontS(Frame.getValString(), FontSize.getValDouble(), this.FontRender);
   }

   protected void GetInit(PinableFrame pinableFrame, Setting xpos, Setting ypos, Setting Frame, Setting FontSize) {
      if (pinableFrame.FontRender.getSize() != (int)FontSize.getValDouble() || !pinableFrame.FontRender.getFont().getName().equalsIgnoreCase(Frame.getValString())) {
         pinableFrame.FontRender.setFontS(Frame.getValString(), FontSize.getValDouble(), pinableFrame.FontRender);
      }

      if (!pinableFrame.getDrag()) {
         pinableFrame.x = (int)xpos.getValDouble();
         pinableFrame.y = (int)ypos.getValDouble();
      } else {
         xpos.setValDouble((double)pinableFrame.x);
         ypos.setValDouble((double)pinableFrame.y);
      }

   }

   protected void fontSelect(Setting s, String name, float v, float v1, int color, boolean shadow) {
      if (s.getValString().equalsIgnoreCase("MC")) {
         if (shadow) {
            this.mc.field_71466_p.func_175063_a(name, (float)((int)v), (float)((int)v1), color);
         }

         if (!shadow) {
            this.mc.field_71466_p.func_78276_b(name, (int)v, (int)v1, color);
         }
      } else {
         if (shadow) {
            this.FontRender.drawStringWithShadow(name, (double)v, (double)v1, color);
         }

         if (!shadow) {
            this.FontRender.String(name, (float)((int)v), (float)((int)v1), color);
         }
      }

   }

   protected int widthcal(Setting s, String name) {
      return s.getValString().equalsIgnoreCase("MC") ? this.mc.field_71466_p.func_78256_a(name) : this.FontRender.getStringWidth(name);
   }

   protected int heightcal(Setting s, String name) {
      return s.getValString().equalsIgnoreCase("MC") ? 10 : this.FontRender.getStringHeight(name);
   }

   public void renderFrame() {
      if (this.isPinned) {
         Component.FontRend.func_175063_a(this.title, (float)(this.x + 3), (float)(this.y + 3), -1);
      }

   }

   public void Ongui() {
   }

   public void renderFrameText() {
      int yCount = this.y + this.barHeight + 3;
      String[] var2 = this.text;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String line = var2[var4];
         Component.FontRend.func_78276_b(line, this.x + 3, yCount, -1);
         yCount += 10;
      }

   }

   public void updatePosition(int mouseX, int mouseY) {
      if (this.isDragging && this.isPinned) {
         this.setX(mouseX - this.dragX);
         this.setY(mouseY - this.dragY);
      }

   }

   public boolean isWithinHeader(int x, int y) {
      return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
   }

   public boolean isWithinExtendRange(int x, int y) {
      return x <= this.x + this.width - 2 && x >= this.x + this.width - 2 - 8 && y >= this.y + 2 && y <= this.y + 10;
   }

   public void setDrag(boolean drag) {
      this.isDragging = drag;
   }

   public Boolean getDrag() {
      return this.isDragging;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int newX) {
      this.x = newX;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int newY) {
      this.y = newY;
   }

   public int getWidth() {
      return this.width;
   }

   public boolean isPinned() {
      return this.isPinned;
   }

   public String getTitle() {
      return this.title;
   }

   public void setPinned(boolean pinned) {
      this.isPinned = pinned;
   }

   public void onRenderGameOverlay(Text event) {
   }

   public void setup() {
   }
}
