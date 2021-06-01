package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Potions extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting Shadow;
   static Setting name;
   static Setting amplifer;
   static Setting duration;
   static Setting Frame;
   static Setting background;
   static Setting xpos;
   static Setting ypos;
   static Setting FontSize;

   public Potions() {
      super("Potions", 0, Category.ONSCREEN, "Potions");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(name = new Setting("name", this, true));
      Main.setmgr.add(amplifer = new Setting("amplifer", this, false));
      Main.setmgr.add(duration = new Setting("duration", this, false));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 160.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("PotionsSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("PotionsSET", false);
   }

   public static class PotionsRUN extends PinableFrame {
      public PotionsRUN() {
         super("PotionsSET", new String[0], (int)Potions.ypos.getValDouble(), (int)Potions.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Potions.xpos, Potions.ypos, Potions.Frame, Potions.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Potions.xpos, Potions.ypos, Potions.Frame, Potions.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         int offset = 0;

         for(Iterator var3 = this.mc.field_71439_g.func_70651_bq().iterator(); var3.hasNext(); offset += 10) {
            PotionEffect activePotionEffect = (PotionEffect)var3.next();
            String effect = Potions.name.getValBoolean() ? activePotionEffect.func_76453_d().substring(7) + " " : "";
            String amp = Potions.amplifer.getValBoolean() ? "x" + activePotionEffect.func_76458_c() + " " : "";
            String dur = Potions.duration.getValBoolean() ? activePotionEffect.func_76459_b() / 20 + " " : "";
            String all = effect + "" + amp + "" + dur;
            this.fontSelect(Potions.Frame, all, (float)this.getX(), (float)(this.getY() + 10 - offset), Potions.TextColor.getcolor(), Potions.Shadow.getValBoolean());
            if (Potions.background.getValBoolean()) {
               Gui.func_73734_a(this.x, this.y + 10 - offset, this.x + this.widthcal(Potions.Frame, effect + amp + dur), this.y + 20 - offset, Potions.BgColor.getcolor());
            }
         }

         super.onRenderGameOverlay(event);
      }
   }
}
