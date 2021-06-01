package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Screens.Override.Mixintab;
import Method.Client.utils.system.Connection;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;

public class ExtraTab extends Module {
   public static Setting Players;
   public static Setting ReplaceBar;
   public static Setting FriendColor;
   public static Setting FriendObfus;
   public static Setting NoHeaderFooter;
   private static GuiPlayerTabOverlay defaultscreen;

   public ExtraTab() {
      super("ExtraTab", 0, Category.RENDER, "ExtraTab");
   }

   public void setup() {
      Main.setmgr.add(Players = new Setting("Players", this, 100.0D, 1.0D, 500.0D, true));
      Main.setmgr.add(ReplaceBar = new Setting("ReplaceBar", this, true));
      Main.setmgr.add(NoHeaderFooter = new Setting("NoHeaderFooter", this, true));
      Main.setmgr.add(FriendColor = new Setting("Friend Color", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(FriendObfus = new Setting("FriendObfus", this, true));
   }

   public void onEnable() {
      defaultscreen = mc.field_71456_v.field_175196_v;
      Mixintab.Run();
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.IN && NoHeaderFooter.getValBoolean()) {
         return !(packet instanceof SPacketPlayerListHeaderFooter);
      } else {
         return true;
      }
   }

   public void onDisable() {
      if (defaultscreen != null) {
         mc.field_71456_v.field_175196_v = defaultscreen;
      }

   }
}
