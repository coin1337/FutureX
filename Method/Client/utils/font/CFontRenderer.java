package Method.Client.utils.font;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer extends CFont {
   protected CFont.CharData[] boldChars = new CFont.CharData[256];
   protected CFont.CharData[] italicChars = new CFont.CharData[256];
   protected CFont.CharData[] boldItalicChars = new CFont.CharData[256];
   private final int[] colorCode = new int[32];
   protected DynamicTexture texBold;
   protected DynamicTexture texItalic;
   protected DynamicTexture texItalicBold;

   public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
      super(font, antiAlias, fractionalMetrics);
      this.setupMinecraftColorcodes();
      this.setupBoldItalicIDs();
   }

   public float drawStringWithShadow(String text, double x, double y, int color) {
      float shadowWidth = this.String(text, x + 1.0D, y + 1.0D, color, true);
      return Math.max(shadowWidth, this.String(text, x, y, color, false));
   }

   public float String(String text, float x, float y, int color) {
      return this.String(text, (double)x, (double)y, color, false);
   }

   public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
      return this.drawStringWithShadow(text, (double)(x - (float)(this.getStringWidth(text) / 2)), (double)y, color);
   }

   public float drawCenteredString(String text, float x, float y, int color) {
      return this.String(text, x - (float)(this.getStringWidth(text) / 2), y, color);
   }

   public float String(String text, double x, double y, int color, boolean shadow) {
      --x;
      y -= 2.0D;
      if (text == null) {
         return 0.0F;
      } else {
         if (color == 553648127) {
            color = 16777215;
         }

         if ((color & -67108864) == 0) {
            color |= -16777216;
         }

         if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
         }

         CFont.CharData[] currentData = this.charData;
         float alpha = (float)(color >> 24 & 255) / 255.0F;
         boolean bold = false;
         boolean italic = false;
         boolean strikethrough = false;
         boolean underline = false;
         x *= 2.0D;
         y *= 2.0D;
         GL11.glPushMatrix();
         GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179131_c((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, alpha);
         int size = text.length();
         GlStateManager.func_179098_w();
         GlStateManager.func_179144_i(this.tex.func_110552_b());
         GL11.glBindTexture(3553, this.tex.func_110552_b());

         for(int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == 167) {
               int colorIndex = 21;

               try {
                  colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
               } catch (Exception var19) {
               }

               if (colorIndex < 16) {
                  bold = false;
                  italic = false;
                  underline = false;
                  strikethrough = false;
                  GlStateManager.func_179144_i(this.tex.func_110552_b());
                  currentData = this.charData;
                  if (colorIndex < 0) {
                     colorIndex = 15;
                  }

                  if (shadow) {
                     colorIndex += 16;
                  }

                  int colorcode = this.colorCode[colorIndex];
                  GlStateManager.func_179131_c((float)(colorcode >> 16 & 255) / 255.0F, (float)(colorcode >> 8 & 255) / 255.0F, (float)(colorcode & 255) / 255.0F, alpha);
               } else if (colorIndex == 17) {
                  bold = true;
                  if (italic) {
                     GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
                     currentData = this.boldItalicChars;
                  } else {
                     GlStateManager.func_179144_i(this.texBold.func_110552_b());
                     currentData = this.boldChars;
                  }
               } else if (colorIndex == 18) {
                  strikethrough = true;
               } else if (colorIndex == 19) {
                  underline = true;
               } else if (colorIndex == 20) {
                  italic = true;
                  if (bold) {
                     GlStateManager.func_179144_i(this.texItalicBold.func_110552_b());
                     currentData = this.boldItalicChars;
                  } else {
                     GlStateManager.func_179144_i(this.texItalic.func_110552_b());
                     currentData = this.italicChars;
                  }
               } else {
                  bold = false;
                  italic = false;
                  underline = false;
                  strikethrough = false;
                  GlStateManager.func_179131_c((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, alpha);
                  GlStateManager.func_179144_i(this.tex.func_110552_b());
                  currentData = this.charData;
               }

               ++i;
            } else if (character < currentData.length) {
               GL11.glBegin(4);
               this.drawChar(currentData, character, (float)x, (float)y);
               GL11.glEnd();
               if (strikethrough) {
                  this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double)currentData[character].width - 8.0D, y + (double)(currentData[character].height / 2));
               }

               if (underline) {
                  this.drawLine(x, y + (double)currentData[character].height - 2.0D, x + (double)currentData[character].width - 8.0D, y + (double)currentData[character].height - 2.0D);
               }

               x += (double)(currentData[character].width - 8 + this.charOffset);
            }
         }

         GL11.glHint(3155, 4352);
         GL11.glPopMatrix();
         return (float)x / 2.0F;
      }
   }

   public int getStringWidth(String text) {
      if (text == null) {
         return 0;
      } else {
         int width = 0;
         CFont.CharData[] currentData = this.charData;
         int size = text.length();

         for(int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == 167) {
               ++i;
            } else if (character < currentData.length) {
               width += currentData[character].width - 8 + this.charOffset;
            }
         }

         return width / 2;
      }
   }

   public void setFont(Font font) {
      super.setFont(font);
      this.setupBoldItalicIDs();
   }

   public void setAntiAlias(boolean antiAlias) {
      super.setAntiAlias(antiAlias);
      this.setupBoldItalicIDs();
   }

   public void setFractionalMetrics(boolean fractionalMetrics) {
      super.setFractionalMetrics(fractionalMetrics);
      this.setupBoldItalicIDs();
   }

   private void setupBoldItalicIDs() {
      this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
      this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
      this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
   }

   private void drawLine(double x, double y, double x1, double y1) {
      GL11.glDisable(3553);
      GL11.glLineWidth(1.0F);
      GL11.glBegin(1);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x1, y1);
      GL11.glEnd();
      GL11.glEnable(3553);
   }

   public List<String> formatString(String string, double width) {
      List<String> finalWords = new ArrayList();
      StringBuilder currentWord = new StringBuilder();
      char lastColorCode = '\uffff';
      char[] chars = string.toCharArray();

      for(int i = 0; i < chars.length; ++i) {
         char c = chars[i];
         if (c == 167 && i < chars.length - 1) {
            lastColorCode = chars[i + 1];
         }

         if ((double)this.getStringWidth(currentWord.toString() + c) < width) {
            currentWord.append(c);
         } else {
            finalWords.add(currentWord.toString());
            currentWord = new StringBuilder("§" + lastColorCode + c);
         }
      }

      if (currentWord.length() > 0) {
         finalWords.add(currentWord.toString());
      }

      return finalWords;
   }

   private void setupMinecraftColorcodes() {
      for(int index = 0; index < 32; ++index) {
         int noClue = (index >> 3 & 1) * 85;
         int red = (index >> 2 & 1) * 170 + noClue;
         int green = (index >> 1 & 1) * 170 + noClue;
         int blue = (index & 1) * 170 + noClue;
         if (index == 6) {
            red += 85;
         }

         if (index >= 16) {
            red /= 4;
            green /= 4;
            blue /= 4;
         }

         this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
      }

   }
}
