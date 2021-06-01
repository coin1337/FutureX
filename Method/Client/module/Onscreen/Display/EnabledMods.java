package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.ColorUtils;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class EnabledMods extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting background;
   static Setting xpos;
   static Setting ypos;
   static Setting ToptoBottom;
   static Setting LefttoRight;
   static Setting Frame;
   static Setting Shadow;
   static Setting FontSize;
   static Setting Wave;

   public EnabledMods() {
      super("EnabledMods", 0, Category.ONSCREEN, "EnabledMods");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 0.75D, 0.85D, 0.85D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Wave = new Setting("Wave", this, true));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(ToptoBottom = new Setting("ToptoBottom", this, true));
      Main.setmgr.add(LefttoRight = new Setting("LefttoRight", this, false));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(xpos = new Setting("xpos", this, 0.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 0.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
      this.setToggled(true);
   }

   public void onEnable() {
      PinableFrame.Toggle("EnabledModsSet", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("EnabledModsSet", false);
   }

   public static class EnabledModsRUN extends PinableFrame {
      public EnabledModsRUN() {
         super("EnabledModsSet", new String[0], (int)EnabledMods.ypos.getValDouble(), (int)EnabledMods.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, EnabledMods.xpos, EnabledMods.ypos, EnabledMods.Frame, EnabledMods.FontSize);
         this.setPinned(true);
      }

      public void Ongui() {
         this.GetInit(this, EnabledMods.xpos, EnabledMods.ypos, EnabledMods.Frame, EnabledMods.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         int yCount = this.y + 3;

         for(Iterator var3 = ModuleManager.getSortedMods(EnabledMods.ToptoBottom.getValBoolean(), true, true).iterator(); var3.hasNext(); yCount += 9) {
            Module module = (Module)var3.next();
            int Lr = EnabledMods.LefttoRight.getValBoolean() ? this.widthcal(EnabledMods.Frame, module.getDisplayName()) - 70 : -3;
            if (EnabledMods.background.getValBoolean()) {
               Gui.func_73734_a(this.x - Lr, yCount + 3, this.widthcal(EnabledMods.Frame, module.getDisplayName()) + this.x + 2 - Lr, yCount + this.heightcal(EnabledMods.Frame, module.getDisplayName()) + 1, EnabledMods.BgColor.getcolor());
            }

            this.fontSelect(EnabledMods.Frame, module.getDisplayName(), (float)(this.x - Lr), (float)yCount, EnabledMods.Wave.getValBoolean() ? ColorUtils.wave((double)((yCount - this.y - this.barHeight - 3) / 8), EnabledMods.TextColor.getSat(), EnabledMods.TextColor.getBri()).getRGB() : EnabledMods.TextColor.getcolor(), EnabledMods.Shadow.getValBoolean());
         }

         super.onRenderGameOverlay(event);
      }
   }
}
