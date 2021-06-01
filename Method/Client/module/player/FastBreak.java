package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.BlockUtils;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import java.util.Objects;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FastBreak extends Module {
   Setting mode;
   PotionEffect Haste;
   Setting autoBreak;
   Setting picOnly;
   Setting Blockair;
   Setting delay;
   private BlockPos renderBlock;
   private BlockPos lastBlock;
   private boolean packetCancel;
   public static final TimerUtils timer = new TimerUtils();
   private EnumFacing direction;

   public FastBreak() {
      super("FastBreak", 0, Category.PLAYER, "FastBreak");
      this.mode = Main.setmgr.add(new Setting("break mode", this, "potion", new String[]{"Potion", "Packet", "INSTANT", "NoDelay"}));
      this.Haste = new PotionEffect((Potion)Objects.requireNonNull(Potion.func_188412_a(3)));
      this.autoBreak = Main.setmgr.add(new Setting("autoBreak", this, false, this.mode, "INSTANT", 1));
      this.picOnly = Main.setmgr.add(new Setting("picOnly", this, false, this.mode, "INSTANT", 2));
      this.Blockair = Main.setmgr.add(new Setting("Blockair", this, false, this.mode, "INSTANT", 3));
      this.delay = Main.setmgr.add(new Setting("delay", this, 1.0D, 0.0D, 5.0D, true, this.mode, "INSTANT", 4));
      this.packetCancel = false;
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (this.renderBlock != null) {
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.mode.getValString().equalsIgnoreCase("INSTANT") && side == Connection.Side.OUT && packet instanceof CPacketPlayerDigging) {
         CPacketPlayerDigging digPacket = (CPacketPlayerDigging)packet;
         return digPacket.func_180762_c() != Action.START_DESTROY_BLOCK || !this.packetCancel;
      } else {
         return true;
      }
   }

   private boolean canBreak(BlockPos pos) {
      return mc.field_71441_e.func_180495_p(pos).func_177230_c().func_176195_g(mc.field_71441_e.func_180495_p(pos), mc.field_71441_e, pos) != -1.0F;
   }

   public void setTarget(BlockPos pos) {
      this.renderBlock = pos;
      this.packetCancel = false;
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
      this.packetCancel = true;
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
      this.direction = EnumFacing.DOWN;
      this.lastBlock = pos;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("INSTANT")) {
         if (this.renderBlock != null && this.autoBreak.getValBoolean() && timer.isDelay((long)this.delay.getValDouble() * 1000L)) {
            if (this.picOnly.getValBoolean() && mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() != Items.field_151046_w) {
               return;
            }

            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.renderBlock, this.direction));
            timer.setLastMS();
         }

         mc.field_71442_b.field_78781_i = 0;
      }

      if (this.mode.getValString().equalsIgnoreCase("NoDelay")) {
         mc.field_71442_b.field_78781_i = 0;
      }

      if (this.mode.getValString().equalsIgnoreCase("potion") && mc.field_71439_g.field_70122_E) {
         mc.field_71439_g.func_70690_d(this.Haste);
      }

      if (this.mode.getValString().equalsIgnoreCase("Packet")) {
         mc.field_71439_g.func_184596_c(this.Haste.func_188419_a());
         if (mc.field_71442_b.field_78770_f > 0.7F) {
            mc.field_71442_b.field_78770_f = 1.0F;
         }

         mc.field_71442_b.field_78781_i = 0;
      }

      super.onClientTick(event);
   }

   public void onLeftClickBlock(LeftClickBlock event) {
      if (this.mode.getValString().equalsIgnoreCase("INSTANT") && this.canBreak(event.getPos())) {
         if (this.lastBlock == null || event.getPos().field_177962_a != this.lastBlock.field_177962_a || event.getPos().field_177960_b != this.lastBlock.field_177960_b || event.getPos().field_177961_c != this.lastBlock.field_177961_c) {
            this.packetCancel = false;
            mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, event.getPos(), (EnumFacing)Objects.requireNonNull(event.getFace())));
         }

         this.packetCancel = true;
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, event.getPos(), (EnumFacing)Objects.requireNonNull(event.getFace())));
         this.renderBlock = event.getPos();
         this.lastBlock = event.getPos();
         this.direction = event.getFace();
         if (this.Blockair.getValBoolean()) {
            mc.field_71442_b.func_187103_a(event.getPos());
            mc.field_71441_e.func_175698_g(event.getPos());
         }

         event.setResult(Result.DENY);
      }

      if (this.mode.getValString().equalsIgnoreCase("packet")) {
         float progress = mc.field_71442_b.field_78770_f + BlockUtils.getHardness(event.getPos());
         if (progress >= 1.0F) {
            return;
         }

         Wrapper.INSTANCE.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, event.getPos(), mc.field_71476_x.field_178784_b));
      }

      super.onLeftClickBlock(event);
   }

   public void onDisable() {
      mc.field_71439_g.func_184596_c(this.Haste.func_188419_a());
      super.onDisable();
   }
}
