package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.block.BlockTripWire;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiCrystal extends Module {
   public Setting Hand;
   public Setting rotate;

   public AntiCrystal() {
      super("AntiCrystal", 0, Category.COMBAT, "String");
      this.Hand = Main.setmgr.add(new Setting("Hand", this, "Mainhand", new String[]{"Mainhand", "Offhand", "Both", "None"}));
      this.rotate = Main.setmgr.add(new Setting("rotate", this, true));
   }

   private int find_in_hotbar(Item item) {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack != ItemStack.field_190927_a && stack.func_77973_b().equals(item)) {
            return i;
         }
      }

      return -1;
   }

   public void onEnable() {
      if (this.find_in_hotbar(Items.field_151007_F) == -1) {
         ChatUtils.warning("Must have string in hotbar!");
         this.toggle();
      }

   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71439_g.field_70173_aa % 2 == 0 && this.find_in_hotbar(Items.field_151007_F) != -1 && !(mc.field_71441_e.func_180495_p(mc.field_71439_g.func_180425_c()).func_177230_c() instanceof BlockTripWire)) {
         int old_slot = true;
         int old_slot = mc.field_71439_g.field_71071_by.field_70461_c;
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(this.find_in_hotbar(Items.field_151007_F)));
         mc.field_71439_g.field_71071_by.field_70461_c = this.find_in_hotbar(Items.field_151007_F);
         this.Blockplace(EnumHand.MAIN_HAND, new BlockPos(mc.field_71439_g.field_70165_t, Math.ceil(mc.field_71439_g.field_70163_u), mc.field_71439_g.field_70161_v));
         mc.field_71439_g.field_71071_by.field_70461_c = old_slot;
      }

      super.onClientTick(event);
   }

   public void Blockplace(EnumHand enumHand, BlockPos blockPos) {
      Vec3d vec3d = new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
      BlockPos offset = blockPos.func_177972_a(EnumFacing.UP);
      EnumFacing opposite = EnumFacing.UP.func_176734_d();
      Vec3d Vec3d = (new Vec3d(offset)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
      if (vec3d.func_72436_e(Vec3d) <= 18.0625D) {
         float[] array = Utils.getNeededRotations(Vec3d, 0.0F, 0.0F);
         mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(array[0], array[1], mc.field_71439_g.field_70122_E));
         mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, offset, opposite, Vec3d, enumHand);
         mc.field_71439_g.func_184609_a(enumHand);
      }

   }
}
