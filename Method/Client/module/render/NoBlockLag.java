package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Patcher.Events.RenderBlockModelEvent;
import Method.Client.utils.Patcher.Events.RenderTileEntityEvent;
import Method.Client.utils.system.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class NoBlockLag extends Module {
   Setting StopCollision;
   Setting antiSound;
   Setting antiPiston;
   Setting antiSign;
   Setting Storage;
   Setting Spawner;
   Setting Beacon;
   Setting MobHead;
   Setting Falling;
   Setting Banner;
   Setting Gentity;
   Setting Object;
   Setting Grass;
   Setting Painting;
   Setting Fire;
   Setting NoChunk;
   ArrayList<BlockPos> modifiedsigns;
   private Set<SoundEvent> sounds;

   public NoBlockLag() {
      super("NoBlockLag", 0, Category.RENDER, "NoBlockLag");
      this.StopCollision = Main.setmgr.add(new Setting("StopCollision", this, false));
      this.antiSound = Main.setmgr.add(new Setting("antiSound", this, true));
      this.antiPiston = Main.setmgr.add(new Setting("antiPiston", this, false));
      this.antiSign = Main.setmgr.add(new Setting("antiSign", this, false));
      this.Storage = Main.setmgr.add(new Setting("Storage", this, false));
      this.Spawner = Main.setmgr.add(new Setting("Spawner", this, false));
      this.Beacon = Main.setmgr.add(new Setting("Beacon", this, false));
      this.MobHead = Main.setmgr.add(new Setting("MobHead", this, false));
      this.Falling = Main.setmgr.add(new Setting("Falling", this, false));
      this.Banner = Main.setmgr.add(new Setting("Banner", this, false));
      this.Gentity = Main.setmgr.add(new Setting("Global Entity", this, false));
      this.Object = Main.setmgr.add(new Setting("objects", this, false));
      this.Grass = Main.setmgr.add(new Setting("Grass", this, false));
      this.Painting = Main.setmgr.add(new Setting("Paintings", this, false));
      this.Fire = Main.setmgr.add(new Setting("Fire", this, false));
      this.NoChunk = Main.setmgr.add(new Setting("NoChunk", this, false));
      this.modifiedsigns = new ArrayList();
   }

   public void setup() {
      this.sounds = new HashSet();
   }

   public void onRenderTileEntity(RenderTileEntityEvent event) {
      Block block = event.getTileEntity().func_145838_q().func_176194_O().func_177622_c();
      if (this.BlockCheck(block)) {
         event.setCanceled(true);
      }

   }

   public void onRenderBlockModel(RenderBlockModelEvent event) {
      Block block = event.getState().func_177230_c();
      if (this.BlockCheck(block)) {
         event.setCanceled(true);
      }

   }

   public void DrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
      if (this.StopCollision.getValBoolean() && this.BlockCheck(mc.field_71441_e.func_180495_p(event.getTarget().func_178782_a()).func_177230_c())) {
         event.setCanceled(true);
      }

   }

   public boolean BlockCheck(Block block) {
      if (this.antiPiston.getValBoolean() && (block instanceof BlockPistonMoving || block instanceof BlockPistonExtension)) {
         return true;
      } else if (this.Storage.getValBoolean() && (block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockDispenser || block instanceof BlockFurnace || block instanceof BlockHopper || block instanceof BlockShulkerBox || block instanceof BlockBrewingStand)) {
         return true;
      } else if (this.Spawner.getValBoolean() && block instanceof BlockMobSpawner) {
         return true;
      } else if (this.Beacon.getValBoolean() && block instanceof BlockBeacon) {
         return true;
      } else if (this.Banner.getValBoolean() && block instanceof BlockBanner) {
         return true;
      } else if (this.Fire.getValBoolean() && block instanceof BlockFire) {
         return true;
      } else if (this.Grass.getValBoolean() && (block instanceof BlockDoublePlant || block instanceof BlockTallGrass || block instanceof BlockDeadBush)) {
         return true;
      } else if (this.MobHead.getValBoolean() && block instanceof BlockSkull) {
         return true;
      } else {
         return this.Falling.getValBoolean() && block instanceof BlockFalling;
      }
   }

   public void onEnable() {
      this.modifiedsigns.clear();
      mc.field_71438_f.func_72712_a();
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (this.antiSign.getValBoolean()) {
         Iterator var2 = mc.field_71441_e.field_147482_g.iterator();

         label46:
         while(true) {
            while(true) {
               TileEntity tileEntity;
               do {
                  if (!var2.hasNext()) {
                     break label46;
                  }

                  tileEntity = (TileEntity)var2.next();
               } while(!(tileEntity instanceof TileEntitySign));

               TileEntitySign tileEntitySign = (TileEntitySign)tileEntity;
               if (!this.modifiedsigns.contains(tileEntity.func_174877_v())) {
                  this.modifiedsigns.add(tileEntity.func_174877_v());
                  int lenght = 0;
                  ITextComponent[] var11 = tileEntitySign.field_145915_a;
                  int i = var11.length;

                  for(int var14 = 0; var14 < i; ++var14) {
                     ITextComponent line = var11[var14];
                     lenght = line.func_150260_c().length();
                  }

                  String[] array = new String[]{"METHOD", "Sign length", "" + lenght + "", ""};

                  for(i = 0; i < tileEntitySign.field_145915_a.length; ++i) {
                     tileEntitySign.field_145915_a[i] = new TextComponentString(array[i]);
                  }
               } else {
                  ITextComponent[] var5 = tileEntitySign.field_145915_a;
                  int var6 = var5.length;
                  byte var7 = 0;
                  if (var7 < var6) {
                     ITextComponent line = var5[var7];
                     if (!line.func_150260_c().startsWith("METHOD")) {
                        this.modifiedsigns.remove(tileEntity.func_174877_v());
                     }
                  }
               }
            }
         }
      }

      super.onRenderWorldLast(event);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketSpawnGlobalEntity && this.Gentity.getValBoolean() || packet instanceof SPacketSpawnObject && this.Object.getValBoolean() || packet instanceof SPacketSpawnPainting && this.Painting.getValBoolean()) {
         return false;
      } else {
         if (this.antiSound.getValBoolean() && packet instanceof SPacketSoundEffect) {
            SPacketSoundEffect sPacketSoundEffect = (SPacketSoundEffect)packet;
            if (this.sounds.contains(sPacketSoundEffect.func_186978_a())) {
               return false;
            }

            this.sounds.add(sPacketSoundEffect.func_186978_a());
         }

         if (side != Connection.Side.IN) {
            return true;
         } else {
            return !(packet instanceof SPacketChunkData) || !this.NoChunk.getValBoolean();
         }
      }
   }
}
