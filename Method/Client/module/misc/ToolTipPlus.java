package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.font.CFont;
import Method.Client.utils.visual.RenderUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.client.event.RenderTooltipEvent.Pre;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.config.GuiUtils;

public class ToolTipPlus extends Module {
   Setting TooltipModify;
   Setting Customfont;
   Setting CustomBackground;
   Setting Color;
   Setting Outline;
   Setting OutlineColor;
   Setting ItemSize;

   public ToolTipPlus() {
      super("ToolTipPlus", 0, Category.MISC, "ToolTipPlus Item Size");
      this.TooltipModify = Main.setmgr.add(new Setting("TooltipModify", this, true));
      this.Customfont = Main.setmgr.add(new Setting("Customfont", this, true, this.TooltipModify, 2));
      this.CustomBackground = Main.setmgr.add(new Setting("CustomBackground", this, false, this.TooltipModify, 3));
      this.Color = Main.setmgr.add(new Setting("Color", this, 0.1D, 1.0D, 0.95D, 0.8D, this.TooltipModify, 4));
      this.Outline = Main.setmgr.add(new Setting("Outline", this, false, this.TooltipModify, 5));
      this.OutlineColor = Main.setmgr.add(new Setting("OutlineColor", this, 0.55D, 1.0D, 1.0D, 1.0D, this.TooltipModify, 6));
      this.ItemSize = Main.setmgr.add(this.ItemSize = new Setting("ItemSize", this, true));
   }

   public String bytesToString(int count) {
      return count >= 1024 ? String.format("%.2f kb", (float)count / 1024.0F) : String.format("%d bytes", count);
   }

   public void ItemTooltipEvent(ItemTooltipEvent event) {
      if (this.ItemSize.getValBoolean()) {
         String interesting = String.valueOf(event.getItemStack().func_77978_p());
         byte[] utf8Bytes = interesting.getBytes(StandardCharsets.UTF_8);
         event.getToolTip().add(" " + this.bytesToString(utf8Bytes.length) + " TextSize");
         String dd = String.valueOf(event.getItemStack().func_151000_E());
         byte[] ee = dd.getBytes(StandardCharsets.UTF_8);
         event.getToolTip().add(" " + this.bytesToString(ee.length) + " TagSize");
      }
   }

   public void RendertooltipPre(Pre event) {
      if (this.TooltipModify.getValBoolean()) {
         int mouseX = event.getX();
         int screenWidth = event.getScreenWidth();
         int screenHeight = event.getScreenHeight();
         int maxTextWidth = event.getMaxWidth();
         List<String> textLines = event.getLines();
         FontRenderer font = event.getFontRenderer();
         GlStateManager.func_179101_C();
         RenderHelper.func_74518_a();
         GlStateManager.func_179140_f();
         GlStateManager.func_179097_i();
         int tooltipTextWidth = 0;
         Iterator var9 = ((List)textLines).iterator();

         int tooltipX;
         while(var9.hasNext()) {
            String textLine = (String)var9.next();
            tooltipX = font.func_78256_a(textLine);
            if (tooltipX > tooltipTextWidth) {
               tooltipTextWidth = tooltipX;
            }
         }

         boolean needsWrap = false;
         int titleLinesCount = 1;
         tooltipX = mouseX + 12;
         if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
            tooltipX = mouseX - 16 - tooltipTextWidth;
            if (tooltipX < 4) {
               if (mouseX > screenWidth / 2) {
                  tooltipTextWidth = mouseX - 12 - 8;
               } else {
                  tooltipTextWidth = screenWidth - 16 - mouseX;
               }

               needsWrap = true;
            }
         }

         if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
            tooltipTextWidth = maxTextWidth;
            needsWrap = true;
         }

         int tooltipY;
         int lineNumber;
         String line;
         if (needsWrap) {
            tooltipY = 0;
            List<String> wrappedTextLines = new ArrayList();

            for(lineNumber = 0; lineNumber < ((List)textLines).size(); ++lineNumber) {
               line = (String)((List)textLines).get(lineNumber);
               List<String> wrappedLine = font.func_78271_c(line, tooltipTextWidth);
               if (lineNumber == 0) {
                  titleLinesCount = wrappedLine.size();
               }

               String line;
               for(Iterator var17 = wrappedLine.iterator(); var17.hasNext(); wrappedTextLines.add(line)) {
                  line = (String)var17.next();
                  int lineWidth = font.func_78256_a(line);
                  if (lineWidth > tooltipY) {
                     tooltipY = lineWidth;
                  }
               }
            }

            tooltipTextWidth = tooltipY;
            textLines = wrappedTextLines;
            if (mouseX > screenWidth / 2) {
               tooltipX = mouseX - 16 - tooltipY;
            } else {
               tooltipX = mouseX + 12;
            }
         }

         tooltipY = event.getY() - 12;
         int tooltipHeight = 8;
         if (((List)textLines).size() > 1) {
            tooltipHeight += (((List)textLines).size() - 1) * 10;
            if (((List)textLines).size() > titleLinesCount) {
               tooltipHeight += 2;
            }
         }

         if (tooltipY < 4) {
            tooltipY = 4;
         } else if (tooltipY + tooltipHeight + 4 > screenHeight) {
            tooltipY = screenHeight - tooltipHeight - 4;
         }

         if (!this.CustomBackground.getValBoolean()) {
            int zLevel = true;
            int backgroundColor = -267386864;
            int borderColorStart = 1347420415;
            int borderColorEnd = (borderColorStart & 16711422) >> 1 | borderColorStart & -16777216;
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(300, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            GuiUtils.drawGradientRect(300, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);
         } else {
            Gui.func_73734_a(tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, this.Color.getcolor());
            if (this.Outline.getValBoolean()) {
               RenderUtils.drawRectOutline((double)(tooltipX - 3), (double)(tooltipY - 4), (double)(tooltipX + tooltipTextWidth + 3), (double)(tooltipY + tooltipHeight + 4), 1.0D, this.OutlineColor.getcolor());
            }
         }

         for(lineNumber = 0; lineNumber < ((List)textLines).size(); ++lineNumber) {
            line = (String)((List)textLines).get(lineNumber);
            if (this.Customfont.getValBoolean()) {
               CFont.tfontRenderer22.drawStringWithShadow(line, (double)((float)tooltipX), (double)((float)tooltipY), -1);
            } else {
               font.func_175063_a(line, (float)tooltipX, (float)tooltipY, -1);
            }

            if (lineNumber + 1 == titleLinesCount) {
               tooltipY += 2;
            }

            tooltipY += 10;
         }

         GlStateManager.func_179145_e();
         GlStateManager.func_179126_j();
         RenderHelper.func_74519_b();
         GlStateManager.func_179091_B();
         event.setCanceled(true);
      }
   }
}
