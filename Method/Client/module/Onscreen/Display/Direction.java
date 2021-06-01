package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Direction extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting Background;
   static Setting Shadow;
   static Setting FontSize;

   public Direction() {
      super("Direction", 0, Category.ONSCREEN, "Direction");
   }

   public void setup() {
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(Background = new Setting("Background", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 40.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
   }

   public void onEnable() {
      PinableFrame.Toggle("DirectionSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("DirectionSET", false);
   }

   public static class DirectionRUN extends PinableFrame {
      public DirectionRUN() {
         super("DirectionSET", new String[0], (int)Direction.ypos.getValDouble(), (int)Direction.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Direction.xpos, Direction.ypos, Direction.Frame, Direction.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Direction.xpos, Direction.ypos, Direction.Frame, Direction.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         String direction = String.format("%s " + ChatFormatting.GRAY + "%s", this.getFacing(), this.getTowards());
         if (Direction.Background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Direction.Frame, direction), this.y + 20, Direction.BgColor.getcolor());
         }

         this.fontSelect(Direction.Frame, direction, (float)this.getX(), (float)(this.getY() + 10), Direction.TextColor.getcolor(), Direction.Shadow.getValBoolean());
         super.onRenderGameOverlay(event);
      }

      public String getFacing() {
         switch(MathHelper.func_76128_c((double)(this.mc.field_71439_g.field_70177_z * 8.0F / 360.0F) + 0.5D) & 7) {
         case 0:
            return "South";
         case 1:
            return "South West";
         case 2:
            return "West";
         case 3:
            return "North West";
         case 4:
            return "North";
         case 5:
            return "North East";
         case 6:
            return "East";
         case 7:
            return "South East";
         default:
            return "Invalid";
         }
      }

      private String getTowards() {
         switch(MathHelper.func_76128_c((double)(this.mc.field_71439_g.field_70177_z * 8.0F / 360.0F) + 0.5D) & 7) {
         case 0:
            return "+Z";
         case 1:
            return "-X +Z";
         case 2:
            return "-X";
         case 3:
            return "-X -Z";
         case 4:
            return "-Z";
         case 5:
            return "+X -Z";
         case 6:
            return "+X";
         case 7:
            return "+X +Z";
         default:
            return "Invalid";
         }
      }
   }
}
