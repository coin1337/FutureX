package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.FriendManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Webfill extends Module {
   Setting always_on;
   Setting rotate;
   Setting range;
   int new_slot;
   boolean sneak;

   public Webfill() {
      super("Webfill", 0, Category.COMBAT, "Webfill");
      this.always_on = Main.setmgr.add(new Setting("hole toggle", this, true));
      this.rotate = Main.setmgr.add(new Setting("hole rotate", this, true));
      this.range = Main.setmgr.add(new Setting("range", this, 4.0D, 1.0D, 6.0D, true));
      this.new_slot = -1;
      this.sneak = false;
   }

   public void onEnable() {
      if (mc.field_71439_g != null) {
         this.new_slot = this.find_in_hotbar();
         if (this.new_slot == -1) {
            this.toggle();
         }
      }

   }

   public void onDisable() {
      if (mc.field_71439_g != null && this.sneak) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
         this.sneak = false;
      }

   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71439_g != null) {
         if (this.always_on.getValBoolean()) {
            EntityPlayer target = this.find_closest_target();
            if (target == null) {
               return;
            }

            if ((double)mc.field_71439_g.func_70032_d(target) < this.range.getValDouble() && this.is_surround()) {
               int last_slot = mc.field_71439_g.field_71071_by.field_70461_c;
               mc.field_71439_g.field_71071_by.field_70461_c = this.new_slot;
               mc.field_71442_b.func_78765_e();
               this.place_blocks(HoleFill.GetLocalPlayerPosFloored());
               mc.field_71439_g.field_71071_by.field_70461_c = last_slot;
            }
         } else {
            int last_slot = mc.field_71439_g.field_71071_by.field_70461_c;
            mc.field_71439_g.field_71071_by.field_70461_c = this.new_slot;
            mc.field_71442_b.func_78765_e();
            this.place_blocks(HoleFill.GetLocalPlayerPosFloored());
            mc.field_71439_g.field_71071_by.field_70461_c = last_slot;
            this.toggle();
         }

      }
   }

   public EntityPlayer find_closest_target() {
      if (mc.field_71441_e.field_73010_i.isEmpty()) {
         return null;
      } else {
         EntityPlayer closestTarget = null;
         Iterator var2 = mc.field_71441_e.field_73010_i.iterator();

         while(true) {
            EntityPlayer target;
            do {
               do {
                  do {
                     do {
                        do {
                           if (!var2.hasNext()) {
                              return closestTarget;
                           }

                           target = (EntityPlayer)var2.next();
                        } while(target == mc.field_71439_g);
                     } while(FriendManager.isFriend(target.func_70005_c_()));
                  } while(!Utils.isLiving(target));
               } while(target.func_110143_aJ() <= 0.0F);
            } while(closestTarget != null && mc.field_71439_g.func_70032_d(target) > mc.field_71439_g.func_70032_d(closestTarget));

            closestTarget = target;
         }
      }
   }

   private int find_in_hotbar() {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack.func_77973_b() == Item.func_150899_d(30)) {
            return i;
         }
      }

      return -1;
   }

   private boolean is_surround() {
      BlockPos player_block = HoleFill.GetLocalPlayerPosFloored();
      return mc.field_71441_e.func_180495_p(player_block.func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block.func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block.func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block.func_177968_d()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(player_block).func_177230_c() == Blocks.field_150350_a;
   }

   private void place_blocks(BlockPos pos) {
      if (mc.field_71441_e.func_180495_p(pos).func_185904_a().func_76222_j()) {
         if (Utils.checkForNeighbours(pos)) {
            EnumFacing[] var2 = EnumFacing.values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               EnumFacing side = var2[var4];
               BlockPos neighbor = pos.func_177972_a(side);
               EnumFacing side2 = side.func_176734_d();
               if (Utils.canBeClicked(neighbor)) {
                  if (Surrond.blackList.contains(mc.field_71441_e.func_180495_p(neighbor).func_177230_c())) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
                     this.sneak = true;
                  }

                  Vec3d hitVec = (new Vec3d(neighbor)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(side2.func_176730_m())).func_186678_a(0.5D));
                  if (this.rotate.getValBoolean()) {
                     Utils.faceVectorPacketInstant(hitVec);
                  }

                  mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                  mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                  return;
               }
            }

         }
      }
   }
}
