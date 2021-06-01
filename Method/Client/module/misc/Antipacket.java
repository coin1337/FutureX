package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Screens.Custom.Packet.AntiPacketGui;
import Method.Client.utils.Screens.Custom.Packet.AntiPacketPacket;
import Method.Client.utils.system.Connection;
import java.util.Iterator;

public class Antipacket extends Module {
   Setting Gui;

   public Antipacket() {
      super("Antipacket", 0, Category.MISC, "Cancel Packets");
      this.Gui = Main.setmgr.add(new Setting("Gui", this, Main.AntiPacketgui));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      System.out.println(packet.toString());
      Iterator var3 = AntiPacketGui.GetPackets().iterator();

      AntiPacketPacket packet2;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         packet2 = (AntiPacketPacket)var3.next();
      } while(!packet.getClass().isInstance(packet2.packet));

      return false;
   }
}
