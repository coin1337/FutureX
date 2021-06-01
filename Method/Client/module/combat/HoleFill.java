package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class HoleFill extends Module {
   Setting hole_toggle;
   Setting hole_rotate;
   Setting hole_range;
   Setting swing;
   private final ArrayList<BlockPos> holes;

   public HoleFill() {
      super("HoleFill", 0, Category.COMBAT, "HoleFill");
      this.hole_toggle = Main.setmgr.add(new Setting("hole toggle", this, true));
      this.hole_rotate = Main.setmgr.add(new Setting("hole rotate", this, true));
      this.hole_range = Main.setmgr.add(new Setting("hole range", this, 4.0D, 1.0D, 6.0D, true));
      this.swing = Main.setmgr.add(new Setting("swing", this, "Mainhand", new String[]{"Mainhand", "Offhand", "Both", "None"}));
      this.holes = new ArrayList();
   }

   public void onEnable() {
      if (this.find_in_hotbar() == -1) {
         this.toggle();
      }

      this.find_new_holes();
   }

   public void onDisable() {
      this.holes.clear();
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.find_in_hotbar() == -1) {
         this.toggle();
      } else if (!this.hole_toggle.getValBoolean()) {
         this.toggle();
      } else {
         this.find_new_holes();
         BlockPos pos_to_fill = null;
         Iterator var3 = (new ArrayList(this.holes)).iterator();

         while(var3.hasNext()) {
            BlockPos pos = (BlockPos)var3.next();
            if (pos != null) {
               Utils.ValidResult result = Utils.valid(pos);
               if (result == Utils.ValidResult.Ok) {
                  pos_to_fill = pos;
                  break;
               }

               this.holes.remove(pos);
            }
         }

         int old_slot = -1;
         if (this.find_in_hotbar() != mc.field_71439_g.field_71071_by.field_70461_c) {
            old_slot = mc.field_71439_g.field_71071_by.field_70461_c;
            mc.field_71439_g.field_71071_by.field_70461_c = this.find_in_hotbar();
         }

         if (pos_to_fill != null && Utils.placeBlock(pos_to_fill, this.hole_rotate.getValBoolean(), this.swing)) {
            this.holes.remove(pos_to_fill);
         }

         if (old_slot != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = old_slot;
         }

      }
   }

   public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
      ArrayList<BlockPos> circleblocks = new ArrayList();
      int cx = loc.func_177958_n();
      int cy = loc.func_177956_o();
      int cz = loc.func_177952_p();

      for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
         for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
            int y = sphere ? cy - (int)r : cy;

            while(true) {
               float f = sphere ? (float)cy + r : (float)(cy + h);
               if (!((float)y < f)) {
                  break;
               }

               double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));
               if (dist < (double)(r * r) && (!hollow || !(dist < (double)((r - 1.0F) * (r - 1.0F))))) {
                  BlockPos l = new BlockPos(x, y + plus_y, z);
                  circleblocks.add(l);
               }

               ++y;
            }
         }
      }

      return circleblocks;
   }

   public static BlockPos GetLocalPlayerPosFloored() {
      return new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.floor(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v));
   }

   public void find_new_holes() {
      this.holes.clear();
      Iterator var1 = getSphere(GetLocalPlayerPosFloored(), (float)this.hole_range.getValDouble(), (int)this.hole_range.getValDouble(), false, true, 0).iterator();

      while(true) {
         BlockPos pos;
         do {
            do {
               do {
                  if (!var1.hasNext()) {
                     return;
                  }

                  pos = (BlockPos)var1.next();
               } while(!mc.field_71441_e.func_180495_p(pos).func_177230_c().equals(Blocks.field_150350_a));
            } while(!mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a));
         } while(!mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals(Blocks.field_150350_a));

         boolean possible = true;
         BlockPos[] var4 = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            BlockPos seems_blocks = var4[var6];
            Block block = mc.field_71441_e.func_180495_p(pos.func_177971_a(seems_blocks)).func_177230_c();
            if (block != Blocks.field_150357_h && block != Blocks.field_150343_Z && block != Blocks.field_150477_bB && block != Blocks.field_150467_bQ) {
               possible = false;
               break;
            }
         }

         if (possible) {
            this.holes.add(pos);
         }
      }
   }

   private int find_in_hotbar() {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock) {
            Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
            if (block instanceof BlockEnderChest) {
               return i;
            }

            if (block instanceof BlockObsidian) {
               return i;
            }
         }
      }

      return -1;
   }
}
