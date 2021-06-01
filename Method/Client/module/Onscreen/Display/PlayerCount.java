package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.FriendManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class PlayerCount extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting Friends;
   static Setting background;
   static Setting Shadow;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting FontSize;

   public PlayerCount() {
      super("PlayerCount", 0, Category.ONSCREEN, "PlayerCount");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(Friends = new Setting("Friends", this, false));
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 140.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("PlayerCountSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("PlayerCountSET", false);
   }

   public static class PlayerCountRUN extends PinableFrame {
      int lasting = 0;

      public PlayerCountRUN() {
         super("PlayerCountSET", new String[0], (int)PlayerCount.ypos.getValDouble(), (int)PlayerCount.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, PlayerCount.xpos, PlayerCount.ypos, PlayerCount.Frame, PlayerCount.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, PlayerCount.xpos, PlayerCount.ypos, PlayerCount.Frame, PlayerCount.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         String playerCount = "ONLINE: " + this.mc.field_71439_g.field_71174_a.func_175106_d().size();
         if (PlayerCount.Friends.getValBoolean()) {
            if (this.mc.field_71439_g.field_70173_aa % 20 == 0) {
               int onlinefriend = 0;
               Iterator var4 = this.mc.field_71441_e.field_73010_i.iterator();

               while(var4.hasNext()) {
                  EntityPlayer s = (EntityPlayer)var4.next();
                  if (FriendManager.friendsList.contains(s.func_70005_c_())) {
                     ++onlinefriend;
                  }
               }

               this.lasting = onlinefriend;
            }

            playerCount = playerCount + " Friends: " + this.lasting;
         }

         this.fontSelect(PlayerCount.Frame, playerCount, (float)this.getX(), (float)(this.getY() + 10), PlayerCount.TextColor.getcolor(), PlayerCount.Shadow.getValBoolean());
         if (PlayerCount.background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(PlayerCount.Frame, playerCount), this.y + 20, PlayerCount.BgColor.getcolor());
         }

         super.onRenderGameOverlay(event);
      }
   }
}
