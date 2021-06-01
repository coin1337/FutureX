package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Patcher.Events.PlayerMoveEvent;
import Method.Client.utils.system.Connection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class God extends Module {
   Setting mode;
   Setting Footsteps;
   private Entity riding;

   public God() {
      super("God", 0, Category.PLAYER, "Take no damage in certain situations");
      this.mode = Main.setmgr.add(new Setting("God mode", this, "Vanilla", new String[]{"Vanilla", "TickMode", "Riding"}));
      this.Footsteps = Main.setmgr.add(new Setting("Footsteps", this, false, this.mode, "Riding", 1));
   }

   public void onEnable() {
      if (this.mode.getValString().equalsIgnoreCase("Riding") && mc.field_71439_g.func_184187_bx() != null) {
         this.riding = mc.field_71439_g.func_184187_bx();
         mc.field_71439_g.func_184210_p();
         mc.field_71441_e.func_72900_e(this.riding);
         mc.field_71439_g.func_70107_b((double)mc.field_71439_g.func_180425_c().func_177958_n(), (double)(mc.field_71439_g.func_180425_c().func_177956_o() - 1), (double)mc.field_71439_g.func_180425_c().func_177952_p());
      }

   }

   public void onDisable() {
      if (this.mode.getValString().equalsIgnoreCase("Riding") && this.riding != null) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketUseEntity(this.riding, EnumHand.MAIN_HAND));
      }

      if (this.mode.getValString().equalsIgnoreCase("TickMode")) {
         mc.field_71439_g.func_71004_bE();
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.mode.getValString().equalsIgnoreCase("Riding") && side == Connection.Side.OUT) {
         if (packet instanceof CPacketUseEntity) {
            CPacketUseEntity packet2 = (CPacketUseEntity)packet;
            if (this.riding != null) {
               Entity entity = packet2.func_149564_a(mc.field_71441_e);
               if (entity != null) {
                  this.riding.field_70165_t = entity.field_70165_t;
                  this.riding.field_70163_u = entity.field_70163_u;
                  this.riding.field_70161_v = entity.field_70161_v;
                  this.riding.field_70177_z = mc.field_71439_g.field_70177_z;
                  this.Movepackets(mc);
               }
            }
         }

         if (packet instanceof Position || packet instanceof PositionRotation) {
            return false;
         }
      }

      return !(packet instanceof CPacketConfirmTeleport) || !this.mode.getValString().equalsIgnoreCase("Vanilla");
   }

   private void Movepackets(Minecraft mc) {
      mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketInput(mc.field_71439_g.field_71158_b.field_192832_b, mc.field_71439_g.field_71158_b.field_78902_a, false, false));
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketVehicleMove(this.riding));
   }

   public void onPlayerMove(PlayerMoveEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Riding") && this.riding != null) {
         this.riding.field_70165_t = mc.field_71439_g.field_70165_t;
         this.riding.field_70163_u = mc.field_71439_g.field_70163_u + (double)(this.Footsteps.getValBoolean() ? 0.3F : 0.0F);
         this.riding.field_70161_v = mc.field_71439_g.field_70161_v;
         this.riding.field_70177_z = mc.field_71439_g.field_70177_z;
         this.Movepackets(mc);
      }

   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("TickMode")) {
         mc.field_71439_g.func_70606_j(20.0F);
         mc.field_71439_g.func_71024_bL().func_75114_a(20);
         mc.field_71439_g.field_70128_L = false;
         if (mc.field_71462_r instanceof GuiGameOver) {
            mc.func_147108_a((GuiScreen)null);
         }
      }

      if (mc.field_71462_r instanceof GuiGameOver && this.mode.getValString().equalsIgnoreCase("Tickmode")) {
         try {
            mc.field_71439_g.func_71004_bE();
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }
}
