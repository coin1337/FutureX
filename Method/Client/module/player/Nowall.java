package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Nowall extends Module {
   Setting Storage;
   Setting Mine;
   private boolean clicked;
   private boolean focus;

   public Nowall() {
      super("Nowall", 0, Category.PLAYER, "Click through walls");
      this.Storage = Main.setmgr.add(new Setting("Storage", this, true));
      this.Mine = Main.setmgr.add(new Setting("Mine", this, false));
      this.focus = false;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.Mine.getValBoolean()) {
         mc.field_71441_e.field_72996_f.stream().filter((entity) -> {
            return entity instanceof EntityLivingBase;
         }).filter((entity) -> {
            return mc.field_71439_g == entity;
         }).map((entity) -> {
            return (EntityLivingBase)entity;
         }).filter((entity) -> {
            return !entity.field_70128_L;
         }).forEach(this::process);
         RayTraceResult normal_result = mc.field_71476_x;
         if (normal_result != null) {
            this.focus = normal_result.field_72313_a == Type.ENTITY;
         }
      }

   }

   private void process(EntityLivingBase event) {
      RayTraceResult bypass_entity_result = event.func_174822_a(6.0D, mc.func_184121_ak());
      if (bypass_entity_result != null && this.focus && bypass_entity_result.field_72313_a == Type.BLOCK) {
         BlockPos block_pos = bypass_entity_result.func_178782_a();
         if (mc.field_71474_y.field_74312_F.func_151470_d()) {
            mc.field_71442_b.func_180512_c(block_pos, EnumFacing.UP);
         }
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && this.Storage.getValBoolean() && packet instanceof CPacketPlayerTryUseItemOnBlock) {
         if (this.clicked) {
            this.clicked = false;
            return true;
         }

         CPacketPlayerTryUseItemOnBlock packet2 = (CPacketPlayerTryUseItemOnBlock)packet;
         if (mc.field_71462_r == null) {
            Block block = mc.field_71441_e.func_180495_p(packet2.func_187023_a()).func_177230_c();
            BlockPos usable = this.findUsableBlock(packet2.func_187022_c(), packet2.func_187024_b(), packet2.func_187026_d(), packet2.func_187025_e(), packet2.func_187020_f());
            if (block.func_180639_a(mc.field_71441_e, packet2.func_187023_a(), mc.field_71441_e.func_180495_p(packet2.func_187023_a()), mc.field_71439_g, packet2.func_187022_c(), packet2.func_187024_b(), packet2.func_187026_d(), packet2.func_187025_e(), packet2.func_187020_f())) {
               return true;
            }

            if (usable != null) {
               mc.field_71439_g.func_184609_a(packet2.func_187022_c());
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(usable, packet2.func_187024_b(), packet2.func_187022_c(), packet2.func_187026_d(), packet2.func_187025_e(), packet2.func_187020_f()));
               this.clicked = true;
            } else {
               Entity usableEntity = this.findUsableEntity();
               if (usableEntity != null) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketUseEntity(usableEntity, packet2.func_187022_c()));
                  this.clicked = true;
               }
            }
         }
      }

      return true;
   }

   private Entity findUsableEntity() {
      Entity entity = null;

      for(int i = 0; (float)i <= mc.field_71442_b.func_78757_d(); ++i) {
         AxisAlignedBB bb = this.traceToBlock((double)i, mc.func_184121_ak());
         float maxDist = mc.field_71442_b.func_78757_d();
         Iterator var5 = mc.field_71441_e.func_72839_b(mc.field_71439_g, bb).iterator();

         while(var5.hasNext()) {
            Entity e = (Entity)var5.next();
            float currentDist = mc.field_71439_g.func_70032_d(e);
            if (currentDist <= maxDist) {
               entity = e;
               maxDist = currentDist;
            }
         }
      }

      return entity;
   }

   private BlockPos findUsableBlock(EnumHand hand, EnumFacing dir, float x, float y, float z) {
      for(int i = 0; (float)i <= mc.field_71442_b.func_78757_d(); ++i) {
         AxisAlignedBB bb = this.traceToBlock((double)i, mc.func_184121_ak());
         BlockPos pos = new BlockPos(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c);
         Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
         if (block.func_180639_a(mc.field_71441_e, pos, mc.field_71441_e.func_180495_p(pos), mc.field_71439_g, hand, dir, x, y, z)) {
            return new BlockPos(pos);
         }
      }

      return null;
   }

   private AxisAlignedBB traceToBlock(double dist, float partialTicks) {
      Vec3d pos = mc.field_71439_g.func_174824_e(partialTicks);
      Vec3d angles = mc.field_71439_g.func_70676_i(partialTicks);
      Vec3d end = pos.func_72441_c(angles.field_72450_a * dist, angles.field_72448_b * dist, angles.field_72449_c * dist);
      return new AxisAlignedBB(end.field_72450_a, end.field_72448_b, end.field_72449_c, end.field_72450_a + 1.0D, end.field_72448_b + 1.0D, end.field_72449_c + 1.0D);
   }
}
