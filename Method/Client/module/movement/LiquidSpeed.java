package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class LiquidSpeed extends Module {
   Setting waterSpeed;
   Setting lavaSpeed;
   Setting mode;
   private final TimerUtils timer;

   public LiquidSpeed() {
      super("LiquidSpeed", 0, Category.MOVEMENT, "Liquid Speed");
      this.waterSpeed = Main.setmgr.add(new Setting("waterSpeed", this, 1.0D, 0.9D, 1.1D, false));
      this.lavaSpeed = Main.setmgr.add(new Setting("lavaSpeed", this, 1.0D, 0.9D, 1.1D, false));
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Vanilla", new String[]{"Vanilla", "Bypass"}));
      this.timer = new TimerUtils();
   }

   public void onClientTick(ClientTickEvent event) {
      EntityPlayerSP var10000;
      if (this.mode.getValString().equalsIgnoreCase("Bypass") && mc.field_71439_g.func_70090_H() && this.timer.isDelay(940L)) {
         var10000 = mc.field_71439_g;
         var10000.field_70159_w *= 1.005D;
         var10000 = mc.field_71439_g;
         var10000.field_70179_y *= 1.005D;
         mc.field_71439_g.field_70181_x = 0.4D;
         this.timer.setLastMS();
      }

      if (this.mode.getValString().equalsIgnoreCase("Vanilla")) {
         BlockPos blockPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.4D, mc.field_71439_g.field_70161_v);
         if (mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150353_l) {
            this.Speed(this.lavaSpeed);
            if (mc.field_71474_y.field_74314_A.func_151470_d()) {
               mc.field_71439_g.field_70181_x = 0.06D;
            }

            if (mc.field_71474_y.field_74311_E.func_151470_d()) {
               mc.field_71439_g.field_70181_x = -0.14D;
            }
         }

         if (mc.field_71439_g.func_70090_H()) {
            this.Speed(this.waterSpeed);
            if (mc.field_71474_y.field_74314_A.func_151470_d()) {
               var10000 = mc.field_71439_g;
               var10000.field_70181_x *= this.waterSpeed.getValDouble() / 1.2D;
            }
         }
      }

   }

   private void Speed(Setting waterSpeed) {
      if (mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d()) {
         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_70159_w *= waterSpeed.getValDouble();
         var10000 = mc.field_71439_g;
         var10000.field_70179_y *= waterSpeed.getValDouble();
      }

   }
}
