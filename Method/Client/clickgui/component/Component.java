package Method.Client.clickgui.component;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class Component {
   public static FontRenderer FontRend;

   public void renderComponent() {
   }

   public void updateComponent(int mouseX, int mouseY) throws IOException {
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
   }

   public void RenderTooltip() {
   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
   }

   public int getParentHeight() {
      return 0;
   }

   public void keyTyped(char typedChar, int key) {
   }

   public void setOff(int newOff) {
   }

   public int getHeight() {
      return 0;
   }

   public int gety() {
      return 0;
   }

   public String getName() {
      return "";
   }

   public String getCategory() {
      return null;
   }

   static {
      FontRend = new FontRenderer(Minecraft.func_71410_x().field_71474_y, new ResourceLocation("textures/font/ascii_sga.png"), Minecraft.func_71410_x().field_71446_o, true);
   }
}
