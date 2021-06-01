package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Wrapper;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Disconnect extends Module {
   Setting leaveHealth;
   Setting Totem;
   Setting Playersight;

   public Disconnect() {
      super("Auto Disconnect", 0, Category.PLAYER, "Disconnect on low health");
      this.leaveHealth = Main.setmgr.add(new Setting("LeaveHealth", this, 4.0D, 0.0D, 20.0D, true));
      this.Totem = Main.setmgr.add(new Setting("Totem", this, false));
      this.Playersight = Main.setmgr.add(new Setting("Player", this, false));
   }

   public void onClientTick(ClientTickEvent event) {
      if ((double)mc.field_71439_g.func_110143_aJ() <= this.leaveHealth.getValDouble()) {
         if (!this.Totem.getValBoolean()) {
            this.Quit();
         } else if (Totemcount() < 1) {
            this.Quit();
         }

         this.toggle();
      }

   }

   public static int Totemcount() {
      int totem = 0;
      if (mc.field_71439_g.func_184592_cb().func_77973_b().equals(Items.field_190929_cY)) {
         ++totem;
      }

      for(int i = 9; i <= 44; ++i) {
         if (MC.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c().func_77973_b() == Items.field_190929_cY) {
            ++totem;
         }
      }

      return totem;
   }

   private void Quit() {
      boolean flag = Wrapper.INSTANCE.mc().func_71387_A();
      mc.field_71441_e.func_72882_A();
      Wrapper.INSTANCE.mc().func_71403_a((WorldClient)null);
      if (flag) {
         Wrapper.INSTANCE.mc().func_147108_a(new GuiMainMenu());
      } else {
         Wrapper.INSTANCE.mc().func_147108_a(new GuiMultiplayer(new GuiMainMenu()));
      }

   }
}
