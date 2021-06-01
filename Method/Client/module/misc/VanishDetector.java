package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketEntity.S17PacketEntityLookMove;
import net.minecraft.network.play.server.SPacketPlayerListItem.Action;
import net.minecraft.network.play.server.SPacketPlayerListItem.AddPlayerData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class VanishDetector extends Module {
   private final HashMap<UUID, String> Hashmap;
   DecimalFormat decimalFormat = new DecimalFormat("0.00");
   Setting EntityMove;
   Setting EntityBedloc;
   Setting StopRemove;
   Setting Soundpos;
   Setting BlockChanges;

   public VanishDetector() {
      super("VanishDetector", 0, Category.MISC, "Staff Vanish Detector");
      this.EntityMove = Main.setmgr.add(new Setting("EntityMove", this, false));
      this.EntityBedloc = Main.setmgr.add(new Setting("Entity Bed ", this, true, this.EntityMove, 1));
      this.StopRemove = Main.setmgr.add(new Setting("Stop Entity Remove", this, true));
      this.Soundpos = Main.setmgr.add(new Setting("Sound pos", this, false));
      this.BlockChanges = Main.setmgr.add(new Setting("BlockChanges", this, false));
      this.Hashmap = new HashMap();
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      Iterator var5;
      if (side == Connection.Side.IN) {
         if (this.EntityMove.getValBoolean() && packet instanceof S17PacketEntityLookMove) {
            S17PacketEntityLookMove packet1 = (S17PacketEntityLookMove)packet;
            if (packet1.func_149065_a(mc.field_71441_e) instanceof EntityOtherPlayerMP) {
               ChatUtils.message("Player: " + packet1.func_149065_a(mc.field_71441_e).func_70005_c_() + " pos X:" + this.decimalFormat.format(packet1.func_149065_a(mc.field_71441_e).field_70165_t) + " Y: " + this.decimalFormat.format(packet1.func_149065_a(mc.field_71441_e).field_70163_u) + " Z: " + this.decimalFormat.format(packet1.func_149065_a(mc.field_71441_e).field_70161_v));
               if (this.EntityBedloc.getValBoolean()) {
                  ChatUtils.message("Player: " + packet1.func_149065_a(mc.field_71441_e).func_70005_c_() + " Bed " + ((EntityPlayer)packet1.func_149065_a(mc.field_71441_e)).field_71081_bT);
               }
            }
         }

         boolean found;
         Entity entity;
         TileEntity entity;
         if (this.Soundpos.getValBoolean() && packet instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet1 = (SPacketSoundEffect)packet;
            if (packet1.func_186977_b() == SoundCategory.PLAYERS && ((SPacketSoundEffect)packet).field_149218_c < 250) {
               found = false;
               var5 = mc.field_71441_e.field_72996_f.iterator();

               while(var5.hasNext()) {
                  entity = (Entity)var5.next();
                  if (Math.sqrt(Math.pow(entity.field_70165_t - (double)packet1.field_149217_b, 2.0D) + Math.pow(entity.field_70163_u - (double)packet1.field_149218_c, 2.0D) + Math.pow(entity.field_70161_v - (double)packet1.field_149215_d, 2.0D)) < 8.0D) {
                     found = true;
                     break;
                  }
               }

               var5 = mc.field_71441_e.field_147482_g.iterator();

               while(var5.hasNext()) {
                  entity = (TileEntity)var5.next();
                  if (Math.sqrt(Math.pow((double)(entity.func_174877_v().field_177962_a - packet1.field_149217_b), 2.0D) + Math.pow((double)(entity.func_174877_v().field_177960_b - packet1.field_149218_c), 2.0D) + Math.pow((double)(entity.func_174877_v().field_177961_c - packet1.field_149215_d), 2.0D)) < 8.0D) {
                     found = true;
                     break;
                  }
               }

               if (!found) {
                  ChatUtils.message("Sound near: X: " + packet1.field_149217_b + " Y: " + packet1.field_149218_c + " Z: " + packet1.field_149215_d);
               }
            }
         }

         if (packet instanceof SPacketDestroyEntities && this.StopRemove.getValBoolean()) {
            return false;
         }

         if (this.BlockChanges.getValBoolean() && packet instanceof SPacketBlockChange) {
            SPacketBlockChange packet1 = (SPacketBlockChange)packet;
            found = false;
            var5 = mc.field_71441_e.field_72996_f.iterator();

            while(var5.hasNext()) {
               entity = (Entity)var5.next();
               if (Math.sqrt(Math.pow(entity.field_70165_t - (double)packet1.func_179827_b().field_177962_a, 2.0D) + Math.pow(entity.field_70163_u - (double)packet1.func_179827_b().field_177960_b, 2.0D) + Math.pow(entity.field_70161_v - (double)packet1.func_179827_b().field_177961_c, 2.0D)) < 8.0D) {
                  found = true;
                  break;
               }
            }

            var5 = mc.field_71441_e.field_147482_g.iterator();

            while(var5.hasNext()) {
               entity = (TileEntity)var5.next();
               if (Math.sqrt(Math.pow((double)(entity.func_174877_v().field_177962_a - packet1.func_179827_b().field_177962_a), 2.0D) + Math.pow((double)(entity.func_174877_v().field_177960_b - packet1.func_179827_b().field_177960_b), 2.0D) + Math.pow((double)(entity.func_174877_v().field_177961_c - packet1.func_179827_b().field_177961_c), 2.0D)) < 8.0D) {
                  found = true;
                  break;
               }
            }

            if (found) {
               ChatUtils.message("BlockChange: X: " + packet1.func_179827_b().field_177962_a + " Y: " + packet1.func_179827_b().field_177960_b + " Z: " + packet1.func_179827_b().field_177961_c);
            }
         }
      }

      if (packet instanceof SPacketPlayerListItem) {
         SPacketPlayerListItem sPacketPlayerListItem = (SPacketPlayerListItem)packet;
         if (sPacketPlayerListItem.func_179768_b() == Action.UPDATE_LATENCY) {
            HashSet<UUID> set = new HashSet();
            var5 = sPacketPlayerListItem.func_179767_a().iterator();

            while(var5.hasNext()) {
               AddPlayerData addPlayerData = (AddPlayerData)var5.next();
               set.add(addPlayerData.func_179962_a().getId());
            }

            (new Thread(() -> {
               Iterator var2 = set.iterator();

               while(var2.hasNext()) {
                  UUID uuid = (UUID)var2.next();
                  this.Hashmap.put(uuid, uuid.toString());
               }

            })).start();
         }
      }

      return true;
   }

   public void onClientTick(ClientTickEvent event) {
      synchronized(this.Hashmap) {
         Iterator var3 = this.Hashmap.entrySet().iterator();

         while(var3.hasNext()) {
            Entry<UUID, String> entry = (Entry)var3.next();
            ChatUtils.message("PlayerPreviewElement " + (String)entry.getValue() + " has gone into vanish (" + entry.getKey() + ")");
         }

         this.Hashmap.clear();
      }
   }
}
