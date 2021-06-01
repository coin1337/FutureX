package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import net.minecraft.entity.player.EntityPlayer;

public class Reach extends Module {
   Setting Reach;

   public Reach() {
      super("Reach", 0, Category.PLAYER, "Reach");
      this.Reach = Main.setmgr.add(this.Reach = new Setting("Reach", this, 10.0D, 0.0D, 20.0D, true));
   }

   public void onEnable() {
      mc.field_71439_g.func_110148_a(EntityPlayer.REACH_DISTANCE).func_111128_a(this.Reach.getValDouble());
   }

   public void onDisable() {
      mc.field_71439_g.func_110148_a(EntityPlayer.REACH_DISTANCE).func_111128_a(5.0D);
   }
}
