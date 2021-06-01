package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Wrapper;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class NoSlowdown extends Module {
   private boolean sneaking;
   Setting web;
   Setting Webfall;
   Setting Eat;
   Setting Slowdownbypass;
   Setting Breakdelay;
   Setting Slimeblock;
   Setting NoIceSlip;

   public NoSlowdown() {
      super("NoSlowdown", 0, Category.MISC, "No more slow");
      this.web = Main.setmgr.add(new Setting("webs", this, false));
      this.Webfall = Main.setmgr.add(new Setting("Webfall", this, false));
      this.Eat = Main.setmgr.add(new Setting("Eat", this, false));
      this.Slowdownbypass = Main.setmgr.add(new Setting("Slowdown Bypass", this, false));
      this.Breakdelay = Main.setmgr.add(new Setting("Breakdelay", this, false));
      this.Slimeblock = Main.setmgr.add(new Setting("Slimeblock", this, false));
      this.NoIceSlip = Main.setmgr.add(new Setting("NoIceSlip", this, false));
   }

   public void onDisable() {
      Blocks.field_150432_aD.field_149765_K = 0.98F;
      Blocks.field_150403_cj.field_149765_K = 0.98F;
      Blocks.field_185778_de.field_149765_K = 0.98F;
   }

   public void onLivingUpdate(LivingUpdateEvent event) {
      if (this.Slowdownbypass.getValBoolean()) {
         if (this.sneaking && !mc.field_71439_g.func_184587_cr()) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
            this.sneaking = false;
         }

         if (!this.sneaking && mc.field_71439_g.func_184587_cr()) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
            this.sneaking = true;
         }
      }

      if (this.NoIceSlip.getValBoolean()) {
         Blocks.field_150432_aD.field_149765_K = 0.0F;
         Blocks.field_150403_cj.field_149765_K = 0.0F;
         Blocks.field_185778_de.field_149765_K = 0.0F;
      }

      if (this.Slimeblock.getValBoolean()) {
         BlockPos pos = new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.ceil(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v));
         if (mc.field_71441_e.func_180495_p(pos.func_177982_a(0, -1, 0)).func_177230_c() instanceof BlockSlime && mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.field_70181_x = 1.5D;
         }
      }

      if (mc.field_71439_g.func_184587_cr() && this.Eat.getValBoolean()) {
         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_191988_bg *= 5.0F;
         var10000 = mc.field_71439_g;
         var10000.field_70702_br *= 5.0F;
         mc.field_71442_b.func_78750_j();
         Wrapper.INSTANCE.sendPacket(new CPacketPlayerDigging(net.minecraft.network.play.client.CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
      }

      if (this.web.getValBoolean()) {
         mc.field_71439_g.field_70134_J = false;
      }

      if (this.Webfall.getValBoolean() && !mc.field_71439_g.field_70122_E && mc.field_71439_g.field_70143_R > 3.0F) {
         mc.field_71439_g.field_70181_x = -0.22000000000000003D;
      }

      if (this.Breakdelay.getValBoolean()) {
         mc.field_71442_b.field_78781_i = 0;
      }

   }
}
