package Method.Client.module.player;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;

public class Autotool extends Module {
   public BlockPos position;
   public EnumFacing facing;
   Setting silent;
   Setting Select;
   Setting EchestSilk;

   public Autotool() {
      super("Autotool", 0, Category.PLAYER, "Autotool");
      this.silent = Main.setmgr.add(new Setting("spoof tool", this, false));
      this.Select = Main.setmgr.add(new Setting("Full Inventory", this, true));
      this.EchestSilk = Main.setmgr.add(new Setting("Echest Silk", this, false));
   }

   public void onLeftClickBlock(LeftClickBlock event) {
      int slot = this.getTool(event.getPos(), this.Select.getValBoolean() ? 36 : 9);
      if (slot != -1) {
         if (this.silent.getValBoolean()) {
            PlayerControllerMP var10000 = mc.field_71442_b;
            var10000.field_78770_f += this.blockStrength(event.getPos(), mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c());
         }

         mc.field_71439_g.field_71071_by.field_70461_c = slot;
         mc.field_71442_b.func_78765_e();
         this.Pswap(slot);
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof CPacketPlayerDigging) {
         CPacketPlayerDigging packet2 = (CPacketPlayerDigging)packet;
         if (packet2.func_180762_c() == Action.STOP_DESTROY_BLOCK) {
            this.position = packet2.func_179715_a();
            this.facing = packet2.func_179714_b();
         }
      }

      return true;
   }

   private void Pswap(int slot) {
      mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, slot, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, this.position, this.facing));
      mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, slot, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
   }

   private int getTool(BlockPos pos, int slots) {
      int index = -1;
      float CurrentFastest = 1.0F;

      for(int i = 0; i <= slots; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack != ItemStack.field_190927_a) {
            float digSpeed = (float)EnchantmentHelper.func_77506_a(Enchantments.field_185305_q, stack);
            float destroySpeed = stack.func_150997_a(mc.field_71441_e.func_180495_p(pos));
            if (mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockEnderChest && this.EchestSilk.getValBoolean()) {
               if (EnchantmentHelper.func_77506_a(Enchantments.field_185306_r, stack) > 0 && digSpeed + destroySpeed > CurrentFastest) {
                  CurrentFastest = digSpeed + destroySpeed;
                  index = i;
               }
            } else if (digSpeed + destroySpeed > CurrentFastest) {
               CurrentFastest = digSpeed + destroySpeed;
               index = i;
            }
         }
      }

      return index;
   }

   private float blockStrength(BlockPos pos, ItemStack stack) {
      float hardness = mc.field_71441_e.func_180495_p(pos).func_185887_b(mc.field_71441_e, pos);
      return hardness < 0.0F ? 0.0F : this.getDigSpeed(mc.field_71441_e.func_180495_p(pos), pos, stack) / hardness / (!this.canHarvestBlock(mc.field_71441_e.func_180495_p(pos).func_177230_c(), pos, stack) ? 100.0F : 30.0F);
   }

   private boolean canHarvestBlock(Block block, BlockPos pos, ItemStack stack) {
      IBlockState state = mc.field_71441_e.func_180495_p(pos);
      state = state.func_177230_c().func_176221_a(state, mc.field_71441_e, pos);
      if (state.func_185904_a().func_76229_l()) {
         return true;
      } else {
         String tool = block.getHarvestTool(state);
         if (!stack.func_190926_b() && tool != null) {
            int toolLevel = stack.func_77973_b().getHarvestLevel(stack, tool, mc.field_71439_g, state);
            if (toolLevel < 0) {
               return mc.field_71439_g.func_184823_b(state);
            } else {
               return toolLevel >= block.getHarvestLevel(state);
            }
         } else {
            return mc.field_71439_g.func_184823_b(state);
         }
      }
   }

   private float getDigSpeed(IBlockState state, BlockPos pos, ItemStack stack) {
      float f = stack.func_150997_a(state);
      if (f > 1.0F) {
         int i = EnchantmentHelper.func_185293_e(mc.field_71439_g);
         if (i > 0 && !stack.func_190926_b()) {
            f += (float)(i * i + 1);
         }
      }

      if (mc.field_71439_g.func_70644_a(MobEffects.field_76422_e)) {
         f *= 1.0F + (float)(((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76422_e))).func_76458_c() + 1) * 0.2F;
      }

      if (mc.field_71439_g.func_70644_a(MobEffects.field_76419_f)) {
         float f1;
         switch(((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76419_f))).func_76458_c()) {
         case 0:
            f1 = 0.3F;
            break;
         case 1:
            f1 = 0.09F;
            break;
         case 2:
            f1 = 0.0027F;
            break;
         case 3:
         default:
            f1 = 8.1E-4F;
         }

         f *= f1;
      }

      if (mc.field_71439_g.func_70055_a(Material.field_151586_h) && !EnchantmentHelper.func_185287_i(mc.field_71439_g)) {
         f /= 5.0F;
      }

      if (!mc.field_71439_g.field_70122_E) {
         f /= 5.0F;
      }

      f = ForgeEventFactory.getBreakSpeed(mc.field_71439_g, state, f, pos);
      return f < 0.0F ? 0.0F : f;
   }
}
