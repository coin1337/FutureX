package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;

public class CoordsFinder extends Module {
   Setting BossDetector;
   Setting logLightning;
   Setting minLightningDist;
   Setting logWolf;
   Setting minWolfDist;
   Setting logPlayer;
   Setting minPlayerDist;

   public CoordsFinder() {
      super("CoordsFinder", 0, Category.MISC, "Coords Finder exploit");
      this.BossDetector = Main.setmgr.add(new Setting("Boss detector", this, true));
      this.logLightning = Main.setmgr.add(new Setting("logLightning", this, true));
      this.minLightningDist = Main.setmgr.add(new Setting("minLightningDist", this, 32.0D, 0.0D, 100.0D, true, this.logLightning, 3));
      this.logWolf = Main.setmgr.add(new Setting("logWolf", this, true));
      this.minWolfDist = Main.setmgr.add(new Setting("minWolfDist", this, 256.0D, 0.0D, 1024.0D, true, this.logWolf, 3));
      this.logPlayer = Main.setmgr.add(new Setting("logPlayer", this, true));
      this.minPlayerDist = Main.setmgr.add(new Setting("minPlayerDist", this, 256.0D, 0.0D, 1024.0D, true, this.logPlayer, 3));
   }

   private boolean pastDistance(EntityPlayer player, BlockPos pos, double dist) {
      return player.func_174831_c(pos) >= Math.pow(dist, 2.0D);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      BlockPos pos;
      if (this.BossDetector.getValBoolean() && packet instanceof SPacketEffect) {
         SPacketEffect sPacketEffect = (SPacketEffect)packet;
         pos = new BlockPos(sPacketEffect.func_179746_d().field_177962_a, sPacketEffect.func_179746_d().field_177960_b, sPacketEffect.func_179746_d().field_177961_c);
         switch(sPacketEffect.func_149242_d()) {
         case 1023:
            ChatUtils.message("Wither Spawned " + pos.field_177962_a + " Y:" + pos.field_177960_b + " Z:" + pos.field_177961_c);
            break;
         case 1028:
            ChatUtils.message("Ender Dragon Defeated " + pos.field_177962_a + " Y:" + pos.field_177960_b + " Z:" + pos.field_177961_c);
            break;
         case 1038:
            ChatUtils.message("End Portal Activated " + pos.field_177962_a + " Y:" + pos.field_177960_b + " Z:" + pos.field_177961_c);
         }
      }

      if (this.logLightning.getValBoolean() && packet instanceof SPacketSoundEffect) {
         SPacketSoundEffect packet2 = (SPacketSoundEffect)packet;
         if (packet2.func_186978_a() != SoundEvents.field_187754_de) {
            return true;
         }

         pos = new BlockPos(packet2.func_149207_d(), packet2.func_149211_e(), packet2.func_149210_f());
         if (this.pastDistance(mc.field_71439_g, pos, this.minLightningDist.getValDouble())) {
            ChatUtils.warning("Lightning strike At X:" + pos.field_177962_a + " Y:" + pos.field_177960_b + " Z:" + pos.field_177961_c);
         }
      } else if (packet instanceof SPacketEntityTeleport) {
         SPacketEntityTeleport sPacketEntityTeleport = (SPacketEntityTeleport)packet;
         Entity teleporting = mc.field_71441_e.func_73045_a(sPacketEntityTeleport.func_149451_c());
         BlockPos pos = new BlockPos(sPacketEntityTeleport.func_186982_b(), sPacketEntityTeleport.func_186983_c(), sPacketEntityTeleport.func_186981_d());
         if (this.logWolf.getValBoolean() && teleporting instanceof EntityWolf) {
            if (this.pastDistance(mc.field_71439_g, pos, this.minWolfDist.getValDouble())) {
               ChatUtils.warning("Wolf Teleport At X:" + pos.field_177962_a + " Y:" + pos.field_177960_b + " Z:" + pos.field_177961_c);
            }
         } else if (this.logPlayer.getValBoolean() && teleporting instanceof EntityPlayer && this.pastDistance(mc.field_71439_g, pos, this.minPlayerDist.getValDouble())) {
            ChatUtils.warning(teleporting.func_70005_c_() + " Teleported to X:" + pos.field_177962_a + " Y:" + pos.field_177960_b + " Z:" + pos.field_177961_c);
         }
      }

      return true;
   }
}
