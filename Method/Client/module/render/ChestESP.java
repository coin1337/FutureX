package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.ChatUtils;
import Method.Client.utils.visual.RenderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.block.BlockChest.Type;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ChestESP extends Module {
   public boolean shouldInform = true;
   Setting Chest;
   Setting Shulker;
   Setting Trappedchest;
   Setting EnderChest;
   Setting MinecartChest;
   Setting ChestColor;
   Setting TrappedchestColor;
   Setting EnderChestColor;
   Setting MinecartChestColor;
   Setting ShulkerColor;
   Setting Mode;
   Setting LineWidth;
   Setting ChangeSeen;
   Setting OpenedColor;
   Setting Notify;
   Setting maxChests;
   ArrayList<BlockPos> Openedpos;

   public ChestESP() {
      super("ChestESP", 0, Category.RENDER, "ChestESP");
      this.Chest = Main.setmgr.add(new Setting("Chest", this, true));
      this.Shulker = Main.setmgr.add(new Setting("Shulker", this, true));
      this.Trappedchest = Main.setmgr.add(new Setting("Trapped", this, true));
      this.EnderChest = Main.setmgr.add(new Setting("Ender", this, true));
      this.MinecartChest = Main.setmgr.add(new Setting("Minecart", this, true));
      this.ChestColor = Main.setmgr.add(new Setting("ChestC", this, 0.22D, 1.0D, 1.0D, 0.5D, this.Chest, 7));
      this.TrappedchestColor = Main.setmgr.add(new Setting("TrappedC", this, 0.0D, 1.0D, 1.0D, 0.5D, this.Trappedchest, 7));
      this.EnderChestColor = Main.setmgr.add(new Setting("EnderC", this, 0.44D, 1.0D, 1.0D, 0.5D, this.EnderChest, 7));
      this.MinecartChestColor = Main.setmgr.add(new Setting("MinecartC", this, 0.88D, 1.0D, 1.0D, 0.5D, this.MinecartChest, 7));
      this.ShulkerColor = Main.setmgr.add(new Setting("ShulkerColor", this, 0.96D, 1.0D, 1.0D, 0.5D, this.MinecartChest, 7));
      this.Mode = Main.setmgr.add(new Setting("Draw Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.ChangeSeen = Main.setmgr.add(new Setting("Has been opened", this, true));
      this.OpenedColor = Main.setmgr.add(new Setting("OpenedColor", this, 0.0D, 1.0D, 1.0D, 0.5D, this.ChangeSeen, 7));
      this.Notify = Main.setmgr.add(new Setting("Notify", this, 50.0D, 100.0D, 2000.0D, true));
      this.maxChests = Main.setmgr.add(new Setting("maxChests", this, 1000.0D, 100.0D, 2000.0D, true));
      this.Openedpos = new ArrayList();
   }

   public void onEnable() {
      this.shouldInform = true;
      this.Openedpos.clear();
      super.onEnable();
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      int chests = 0;
      Iterator var3 = mc.field_71441_e.field_147482_g.iterator();

      while(var3.hasNext()) {
         TileEntity entity = (TileEntity)var3.next();
         if (entity instanceof TileEntityChest) {
            if ((double)chests >= this.maxChests.getValDouble() && this.shouldInform) {
               break;
            }

            ++chests;
            TileEntityChest chest = (TileEntityChest)entity;
            if (this.ChangeSeen.getValBoolean()) {
               if (chest.field_145987_o > 0) {
                  this.Openedpos.add(chest.func_174877_v());
               }

               if (this.Openedpos.contains(chest.func_174877_v())) {
                  RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Standardbb(chest.func_174877_v()), this.OpenedColor.getcolor(), this.LineWidth.getValDouble());
                  continue;
               }
            }

            if (chest.func_145980_j() == Type.TRAP && this.Trappedchest.getValBoolean()) {
               RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Standardbb(chest.func_174877_v()), this.TrappedchestColor.getcolor(), this.LineWidth.getValDouble());
               continue;
            }

            if (this.Chest.getValBoolean()) {
               RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Standardbb(chest.func_174877_v()), this.ChestColor.getcolor(), this.LineWidth.getValDouble());
               continue;
            }
         }

         if (entity instanceof TileEntityEnderChest && this.EnderChest.getValBoolean()) {
            ++chests;
            RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Standardbb(entity.func_174877_v()), this.EnderChestColor.getcolor(), this.LineWidth.getValDouble());
         }

         if (entity instanceof TileEntityShulkerBox && this.Shulker.getValBoolean()) {
            ++chests;
            RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Standardbb(entity.func_174877_v()), this.ShulkerColor.getcolor(), this.LineWidth.getValDouble());
         }
      }

      var3 = mc.field_71441_e.field_72996_f.iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         if ((double)chests >= this.maxChests.getValDouble()) {
            break;
         }

         if (entity instanceof EntityMinecartChest && this.MinecartChest.getValBoolean()) {
            ++chests;
            RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Standardbb(entity.func_180425_c()), this.MinecartChestColor.getcolor(), this.LineWidth.getValDouble());
         }
      }

      if (this.shouldInform) {
         if ((double)chests >= this.Notify.getValDouble()) {
            ChatUtils.warning("Found " + chests + " chests.");
            mc.func_147118_V().func_147682_a(PositionedSoundRecord.func_194007_a(SoundEvents.field_187604_bf, 1.0F, 1.0F));
         }

         if ((double)chests >= this.maxChests.getValDouble()) {
            ChatUtils.warning("To prevent lag, it will only show the first " + this.maxChests.getValDouble() + " chests.");
            this.shouldInform = false;
         }
      } else if ((double)chests < this.maxChests.getValDouble()) {
         this.shouldInform = true;
      }

      super.onRenderWorldLast(event);
   }

   @Nullable
   public ILockableContainer getLockableContainer(World worldIn, BlockPos pos) {
      return this.getContainer(worldIn, pos, false);
   }

   @Nullable
   public ILockableContainer getContainer(World worldIn, BlockPos pos, boolean allowBlocking) {
      TileEntity tileentity = worldIn.func_175625_s(pos);
      if (!(tileentity instanceof TileEntityChest)) {
         return null;
      } else {
         ILockableContainer ilockablecontainer = (TileEntityChest)tileentity;
         Iterator var6 = Plane.HORIZONTAL.iterator();

         while(true) {
            while(true) {
               EnumFacing enumfacing;
               TileEntity tileentity1;
               do {
                  if (!var6.hasNext()) {
                     return (ILockableContainer)ilockablecontainer;
                  }

                  enumfacing = (EnumFacing)var6.next();
                  BlockPos blockpos = pos.func_177972_a(enumfacing);
                  tileentity1 = worldIn.func_175625_s(blockpos);
               } while(!(tileentity1 instanceof TileEntityChest));

               if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH) {
                  ilockablecontainer = new InventoryLargeChest("container.chestDouble", (ILockableContainer)ilockablecontainer, (TileEntityChest)tileentity1);
               } else {
                  ilockablecontainer = new InventoryLargeChest("container.chestDouble", (TileEntityChest)tileentity1, (ILockableContainer)ilockablecontainer);
               }
            }
         }
      }
   }
}
