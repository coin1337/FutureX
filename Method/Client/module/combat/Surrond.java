package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Surrond extends Module {
   Setting blocksPerTick;
   Setting rotate;
   Setting autoCenter;
   Setting offInAir;
   Setting BypassCenter;
   Setting Hand;
   Setting Onerun;
   private int playerHotbarSlot;
   private int lastHotbarSlot;
   private int offsetStep;
   public static List<Block> blackList;
   public static List<Block> shulkerList;
   private static Vec3d[] SURROUND;

   public Surrond() {
      super("Surrond", 0, Category.COMBAT, "Surrond you with obsidian");
      this.blocksPerTick = Main.setmgr.add(new Setting("blocksPerTick", this, 10.0D, 0.0D, 10.0D, true));
      this.rotate = Main.setmgr.add(new Setting("rotate", this, true));
      this.autoCenter = Main.setmgr.add(new Setting("autoCenter", this, true));
      this.offInAir = Main.setmgr.add(new Setting("offInAir", this, true));
      this.BypassCenter = Main.setmgr.add(new Setting("Bypass AutoCenter", this, true, this.autoCenter, 2));
      this.Hand = Main.setmgr.add(new Setting("Hand", this, "Mainhand", new String[]{"Mainhand", "Offhand", "Both", "None"}));
      this.Onerun = Main.setmgr.add(new Setting("Run Once", this, true));
   }

   public void setup() {
      blackList = Arrays.asList(Blocks.field_150477_bB, Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z, Blocks.field_150415_aT, Blocks.field_150381_bn);
      shulkerList = Arrays.asList(Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA);
      SURROUND = new Vec3d[]{new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D)};
      this.playerHotbarSlot = -1;
      this.lastHotbarSlot = -1;
      this.offsetStep = 0;
   }

   public void onEnable() {
      if (this.autoCenter.getValBoolean()) {
         if (this.BypassCenter.getValBoolean()) {
            double lMotionX = Math.floor(mc.field_71439_g.field_70165_t) + 0.5D - mc.field_71439_g.field_70165_t;
            double lMotionZ = Math.floor(mc.field_71439_g.field_70161_v) + 0.5D - mc.field_71439_g.field_70161_v;
            mc.field_71439_g.field_70159_w = lMotionX / 2.0D;
            mc.field_71439_g.field_70179_y = lMotionZ / 2.0D;
         } else {
            this.centerPlayer(Math.floor(mc.field_71439_g.field_70165_t) + 0.5D, mc.field_71439_g.field_70163_u, Math.floor(mc.field_71439_g.field_70161_v) + 0.5D);
         }
      }

      this.playerHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
      this.lastHotbarSlot = -1;
   }

   public void onDisable() {
      if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
         mc.field_71439_g.field_71071_by.field_70461_c = this.playerHotbarSlot;
      }

      this.playerHotbarSlot = -1;
      this.lastHotbarSlot = -1;
   }

   private void centerPlayer(double x, double y, double z) {
      mc.field_71439_g.field_71174_a.func_147297_a(new Position(x, y, z, true));
      mc.field_71439_g.func_70107_b(x, y, z);
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.offInAir.getValBoolean() && !mc.field_71439_g.field_70122_E) {
         this.toggle();
      }

      int blocksPlaced = 0;

      while((double)blocksPlaced < this.blocksPerTick.getValDouble()) {
         if (this.offsetStep >= SURROUND.length) {
            this.offsetStep = 0;
            break;
         }

         BlockPos offsetPos = new BlockPos(SURROUND[this.offsetStep]);
         BlockPos targetPos = (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(offsetPos.func_177958_n(), offsetPos.func_177956_o(), offsetPos.func_177952_p());
         int old_slot = -1;
         if (this.find_obi_in_hotbar() != mc.field_71439_g.field_71071_by.field_70461_c) {
            old_slot = mc.field_71439_g.field_71071_by.field_70461_c;
            mc.field_71439_g.field_71071_by.field_70461_c = this.find_obi_in_hotbar();
         }

         if (Utils.trytoplace(targetPos) && Utils.placeBlock(targetPos, this.rotate.getValBoolean(), this.Hand) && Utils.placeBlock(targetPos, this.rotate.getValBoolean(), this.Hand)) {
            ++blocksPlaced;
         }

         if (old_slot != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = old_slot;
         }

         ++this.offsetStep;
         if (blocksPlaced > 0 && this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = this.playerHotbarSlot;
            this.lastHotbarSlot = this.playerHotbarSlot;
         }

         if (blocksPlaced == 0) {
            this.toggle();
         }
      }

      if (this.Onerun.getValBoolean()) {
         this.toggle();
      }

   }

   private int find_obi_in_hotbar() {
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
