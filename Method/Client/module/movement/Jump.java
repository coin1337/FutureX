package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Patcher.Events.EntityPlayerJumpEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Jump extends Module {
   Setting mode;
   Setting height;
   Setting Ymotion;
   Setting AirJump;
   Setting RapidJump;

   public Jump() {
      super("Jump", 0, Category.MOVEMENT, "Jump Mod");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "PotionHJ", new String[]{"PotionHJ", "Ymotion", "JumpPos", "Random", "Packet", "None"}));
      this.height = Main.setmgr.add(new Setting("height", this, 1.0D, 0.5D, 20.0D, true, this.mode, "PotionHJ", 1));
      this.Ymotion = Main.setmgr.add(new Setting("Ymotion", this, 1.0D, 0.0D, 2.0D, false, this.mode, "Ymotion", 1));
      this.AirJump = Main.setmgr.add(new Setting("AirJump", this, false));
      this.RapidJump = Main.setmgr.add(new Setting("RapidJump", this, false));
   }

   public void onPlayerTick(PlayerTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("PotionHJ")) {
         PotionEffect nv = new PotionEffect(MobEffects.field_76430_j, 3, (int)this.height.getValDouble());
         mc.field_71439_g.func_70690_d(nv);
      }

      if (this.mode.getValString().equalsIgnoreCase("JumpPos") && mc.field_71474_y.field_74314_A.field_74513_e) {
         mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70142_S, (double)((float)mc.field_71439_g.field_70117_cu + 0.139F), (double)mc.field_71439_g.field_70116_cv);
      }

      if (this.mode.getValString().equalsIgnoreCase("Packet")) {
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.41999998688698D, mc.field_71439_g.field_70161_v, true));
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.7531999805211997D, mc.field_71439_g.field_70161_v, true));
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.00133597911214D, mc.field_71439_g.field_70161_v, true));
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.16610926093821D, mc.field_71439_g.field_70161_v, true));
      }

      if (this.AirJump.getValBoolean() && !mc.field_71439_g.field_70122_E && mc.field_71474_y.field_74314_A.func_151468_f()) {
         mc.field_71439_g.func_70664_aZ();
      }

      if (this.RapidJump.getValBoolean() && mc.field_71439_g.field_70122_E && mc.field_71474_y.field_74314_A.field_74513_e) {
         mc.field_71439_g.func_70664_aZ();
      }

   }

   public void onPlayerJump(EntityPlayerJumpEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Random")) {
         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_70181_x += Math.random() / 10.0D;
         var10000 = mc.field_71439_g;
         var10000.field_70163_u += Math.random() / 10.0D;
      }

      if (this.mode.getValString().equalsIgnoreCase("Ymotion")) {
         mc.field_71439_g.field_70181_x *= this.Ymotion.getValDouble();
      }

   }

   public void onDisable() {
      mc.field_71439_g.func_184596_c(MobEffects.field_76430_j);
      super.onDisable();
   }
}
