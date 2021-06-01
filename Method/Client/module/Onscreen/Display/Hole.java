package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.module.movement.Phase;
import Method.Client.utils.Utils;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Hole extends Module {
   static Setting xpos;
   static Setting ypos;

   public Hole() {
      super("Hole", 0, Category.ONSCREEN, "Hole");
   }

   public void setup() {
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 90.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("HoleSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("HoleSET", false);
   }

   public static class HoleRUN extends PinableFrame {
      public HoleRUN() {
         super("HoleSET", new String[0], (int)Hole.ypos.getValDouble(), (int)Hole.xpos.getValDouble());
      }

      public void setup() {
         Hole.xpos.setValDouble((double)this.x);
         Hole.ypos.setValDouble((double)this.y);
      }

      public void onRenderGameOverlay(Text event) {
         float yaw = 0.0F;
         int dir = MathHelper.func_76128_c((double)(this.mc.field_71439_g.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
         switch(dir) {
         case 1:
            yaw = 90.0F;
            break;
         case 2:
            yaw = -180.0F;
            break;
         case 3:
            yaw = -90.0F;
         }

         BlockPos northPos = this.traceToBlock(this.mc.func_184121_ak(), yaw);
         Block north = this.getBlock(northPos);
         if (north != null && north != Blocks.field_150350_a) {
            int damage = this.getBlockDamage(northPos);
            if (damage != 0) {
               Gui.func_73734_a(this.getX() + 16, this.getY(), this.getX() + 32, this.getY() + 16, 1627324416);
            }

            this.drawBlock(north, (float)(this.getX() + 16), (float)this.getY());
         }

         BlockPos southPos = this.traceToBlock(this.mc.func_184121_ak(), yaw - 180.0F);
         Block south = this.getBlock(southPos);
         if (south != null && south != Blocks.field_150350_a) {
            int damage = this.getBlockDamage(southPos);
            if (damage != 0) {
               Gui.func_73734_a(this.getX() + 16, this.getY() + 32, this.getX() + 32, this.getY() + 48, 1627324416);
            }

            this.drawBlock(south, (float)(this.getX() + 16), (float)(this.getY() + 32));
         }

         BlockPos eastPos = this.traceToBlock(this.mc.func_184121_ak(), yaw + 90.0F);
         Block east = this.getBlock(eastPos);
         if (east != null && east != Blocks.field_150350_a) {
            int damage = this.getBlockDamage(eastPos);
            if (damage != 0) {
               Gui.func_73734_a(this.getX() + 32, this.getY() + 16, this.getX() + 48, this.getY() + 32, 1627324416);
            }

            this.drawBlock(east, (float)(this.getX() + 32), (float)(this.getY() + 16));
         }

         BlockPos westPos = this.traceToBlock(this.mc.func_184121_ak(), yaw - 90.0F);
         Block west = this.getBlock(westPos);
         if (west != null && west != Blocks.field_150350_a) {
            int damage = this.getBlockDamage(westPos);
            if (damage != 0) {
               Gui.func_73734_a(this.getX(), this.getY() + 16, this.getX() + 16, this.getY() + 32, 1627324416);
            }

            this.drawBlock(west, (float)this.getX(), (float)(this.getY() + 16));
         }

      }

      private BlockPos traceToBlock(float partialTicks, float yaw) {
         Vec3d pos = Utils.interpolateEntity(this.mc.field_71439_g, partialTicks);
         Vec3d dir = direction(yaw);
         return new BlockPos(pos.field_72450_a + dir.field_72450_a, pos.field_72448_b, pos.field_72449_c + dir.field_72449_c);
      }

      public static Vec3d direction(float yaw) {
         return new Vec3d(Math.cos(Phase.degToRad((double)(yaw + 90.0F))), 0.0D, Math.sin(Phase.degToRad((double)(yaw + 90.0F))));
      }

      private int getBlockDamage(BlockPos pos) {
         Iterator var2 = this.mc.field_71438_f.field_72738_E.values().iterator();

         DestroyBlockProgress destBlockProgress;
         do {
            if (!var2.hasNext()) {
               return 0;
            }

            destBlockProgress = (DestroyBlockProgress)var2.next();
         } while(destBlockProgress.func_180246_b().func_177958_n() != pos.func_177958_n() || destBlockProgress.func_180246_b().func_177956_o() != pos.func_177956_o() || destBlockProgress.func_180246_b().func_177952_p() != pos.func_177952_p());

         return destBlockProgress.func_73106_e();
      }

      private Block getBlock(BlockPos pos) {
         Block block = this.mc.field_71441_e.func_180495_p(pos).func_177230_c();
         return block != Blocks.field_150357_h && block != Blocks.field_150343_Z ? Blocks.field_150350_a : block;
      }

      private void drawBlock(Block block, float x, float y) {
         ItemStack stack = new ItemStack(block);
         GlStateManager.func_179094_E();
         GlStateManager.func_179147_l();
         GlStateManager.func_179120_a(770, 771, 1, 0);
         RenderHelper.func_74520_c();
         GlStateManager.func_179109_b(x, y, 0.0F);
         this.mc.func_175599_af().func_180450_b(stack, 0, 0);
         RenderHelper.func_74518_a();
         GlStateManager.func_179084_k();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179121_F();
      }
   }
}
