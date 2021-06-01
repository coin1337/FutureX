package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.FriendManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Enemypos extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting Friends;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting ColorDistance;
   static Setting Background;
   static Setting LefttoRight;
   static Setting Shadow;
   static Setting FontSize;

   public Enemypos() {
      super("Enemypos", 0, Category.ONSCREEN, "Enemypos");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(LefttoRight = new Setting("LefttoRight", this, true));
      Main.setmgr.add(Friends = new Setting("Friends", this, true));
      Main.setmgr.add(ColorDistance = new Setting("ColorDistance", this, true));
      Main.setmgr.add(Background = new Setting("Background", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c / 2 + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 60.0D, -20.0D, (double)(mc.field_71440_d / 2 + 40), true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
   }

   public void onEnable() {
      PinableFrame.Toggle("EnemyposSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("EnemyposSET", false);
   }

   public static class EnemyposRUN extends PinableFrame {
      public EnemyposRUN() {
         super("EnemyposSET", new String[0], (int)Enemypos.ypos.getValDouble(), (int)Enemypos.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Enemypos.xpos, Enemypos.ypos, Enemypos.Frame, Enemypos.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Enemypos.xpos, Enemypos.ypos, Enemypos.Frame, Enemypos.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         int yCount = this.y + this.barHeight + 3;
         Iterator var3 = this.mc.field_71441_e.field_73010_i.iterator();

         while(true) {
            EntityPlayer player;
            do {
               if (!var3.hasNext()) {
                  super.onRenderGameOverlay(event);
                  return;
               }

               player = (EntityPlayer)var3.next();
            } while(FriendManager.isFriend(player.func_70005_c_()) && Enemypos.Friends.getValBoolean());

            if (!player.func_70005_c_().equals(this.mc.field_71439_g.func_70005_c_())) {
               int Lr = Enemypos.LefttoRight.getValBoolean() ? this.widthcal(Enemypos.Frame, player.func_70005_c_() + this.Pos(player)) - 70 : -3;
               if (Enemypos.Background.getValBoolean()) {
                  Gui.func_73734_a(this.x + 4, yCount, this.widthcal(Enemypos.Frame, player.func_70005_c_() + this.Pos(player)) + this.x + 3, yCount + this.heightcal(Enemypos.Frame, player.func_70005_c_() + player.func_180425_c()) - 1, Enemypos.Background.getcolor());
               }

               this.fontSelect(Enemypos.Frame, player.func_70005_c_() + this.Pos(player), (float)(this.x - Lr), (float)yCount, Enemypos.ColorDistance.getValBoolean() ? this.distancecolor(player) : Enemypos.TextColor.getcolor(), Enemypos.Shadow.getValBoolean());
               yCount += 8;
            }
         }
      }

      private int distancecolor(EntityPlayer player) {
         int g = 0;
         int r = 0;
         if (this.mc.field_71439_g.func_70032_d(player) > 50.0F && this.mc.field_71439_g.func_70032_d(player) < 100.0F) {
            g = (int)((double)(this.mc.field_71439_g.func_70032_d(player) - 50.0F) * 5.1D);
         }

         if (this.mc.field_71439_g.func_70032_d(player) < 50.0F) {
            r = (int)((double)this.mc.field_71439_g.func_70032_d(player) * 5.1D);
         }

         this.mc.field_71439_g.func_70032_d(player);
         return (new Color(r, g, 0)).getRGB();
      }

      public String Pos(EntityPlayer player) {
         return " X:" + player.func_180425_c().func_177958_n() + ", Y:" + player.func_180425_c().func_177956_o() + ", Z:" + player.func_180425_c().func_177952_p();
      }
   }
}
