package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Patcher.Events.PlayerDamageBlockEvent;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import java.util.Iterator;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Noswing extends Module {
   Setting mode;
   Setting Nobreakani;
   EnumFacing lastFacing;
   BlockPos lastPos;
   boolean isMining;

   public Noswing() {
      super("Noswing", 0, Category.PLAYER, "Noswing");
      this.mode = Main.setmgr.add(new Setting("No swing", this, "Vanilla", new String[]{"Vanilla", "Packet", "BlockClick", "PacketSwing", "Clientonly"}));
      this.Nobreakani = Main.setmgr.add(new Setting("Nobreakani", this, false));
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("Clientonly")) {
         ItemRenderer itemRenderer = mc.field_71460_t.field_78516_c;
         itemRenderer.field_187469_f = 1.0F;
         itemRenderer.field_187467_d = mc.field_71439_g.func_184614_ca();
      }

      if (this.mode.getValString().equalsIgnoreCase("Vanilla") && mc.field_71439_g.field_70733_aJ <= 0.0F) {
         mc.field_71439_g.field_110158_av = 5;
      }

      if (this.Nobreakani.getValBoolean()) {
         if (mc.field_71474_y.field_74312_F.func_151470_d()) {
            this.resetMining();
         } else if (this.isMining && this.lastPos != null && this.lastFacing != null) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
         }
      }

   }

   public void resetMining() {
      this.isMining = false;
      this.lastPos = null;
      this.lastFacing = null;
   }

   public void DamageBlock(PlayerDamageBlockEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("PacketSwing")) {
         Wrapper.INSTANCE.sendPacket(new CPacketPlayerDigging(Action.ABORT_DESTROY_BLOCK, event.getPos(), event.getFacing()));
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof CPacketPlayerDigging && side == Connection.Side.OUT && this.Nobreakani.getValBoolean()) {
         CPacketPlayerDigging packet2 = (CPacketPlayerDigging)packet;
         Iterator var4 = mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(packet2.func_179715_a())).iterator();

         while(var4.hasNext()) {
            Entity entity = (Entity)var4.next();
            if (entity instanceof EntityEnderCrystal) {
               this.resetMining();
            } else if (entity instanceof EntityLivingBase) {
               this.resetMining();
            }
         }

         if (packet2.func_180762_c().equals(Action.START_DESTROY_BLOCK)) {
            this.isMining = true;
            this.setMiningInfo(packet2.func_179715_a(), packet2.func_179714_b());
         }

         if (packet2.func_180762_c().equals(Action.STOP_DESTROY_BLOCK)) {
            this.resetMining();
         }
      }

      return !(packet instanceof CPacketAnimation) || !this.mode.getValString().equalsIgnoreCase("packet");
   }

   public void onLeftClickBlock(LeftClickBlock event) {
      if (this.mode.getValString().equalsIgnoreCase("BlockClick")) {
         this.Blockclick(event);
      }

   }

   public void onRightClickBlock(RightClickBlock event) {
      if (this.mode.getValString().equalsIgnoreCase("BlockClick")) {
         this.Blockclick(event);
      }

   }

   void Blockclick(Event event) {
      if (mc.field_71476_x.field_72308_g == null) {
         BlockPos blockPos = mc.field_71476_x.func_178782_a();
         Wrapper.INSTANCE.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));
         Wrapper.INSTANCE.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
         event.setCanceled(true);
      }

      if (mc.field_71476_x.field_72308_g != null) {
         mc.field_71442_b.func_78764_a(mc.field_71439_g, mc.field_71476_x.field_72308_g);
         event.setCanceled(true);
      }

   }

   private void setMiningInfo(BlockPos blockPos, EnumFacing enumFacing) {
      this.lastPos = blockPos;
      this.lastFacing = enumFacing;
   }
}
