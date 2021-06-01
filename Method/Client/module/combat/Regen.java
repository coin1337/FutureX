package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Regen extends Module {
   Setting mode;
   Setting packets;

   public Regen() {
      super("Regen", 0, Category.COMBAT, "Regen");
      this.mode = Main.setmgr.add(new Setting("Regen Mode", this, "Vanilla", new String[]{"Vanilla", "Packet"}));
      this.packets = Main.setmgr.add(new Setting("packets", this, 20.0D, 20.0D, 200.0D, false, this.mode, "Packet", 2));
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Packet") && mc.field_71439_g.func_110143_aJ() < mc.field_71439_g.func_110138_aP() && mc.field_71439_g.func_71024_bL().func_75116_a() > 1) {
         for(int i = 0; (double)i < this.packets.getValDouble(); ++i) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayer());
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Vanilla") && mc.field_71439_g.func_110143_aJ() < 20.0F) {
         mc.field_71428_T.field_194149_e = 0.8F;
         mc.field_71439_g.func_70606_j(20.0F);
      }

      super.onClientTick(event);
   }

   public void onDisable() {
      if (this.mode.getValString().equalsIgnoreCase("Vanilla")) {
         mc.field_71428_T.field_194149_e = 1.0F;
      }

   }
}
