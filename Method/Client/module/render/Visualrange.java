package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.FriendManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.ChatUtils;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Visualrange extends Module {
   Setting playSound;
   Setting leaving;
   Setting Box;
   Setting color;
   Setting Mode;
   Setting LineWidth;
   Setting ShowCoords;
   private List<EntityPlayer> knownPlayers;
   public static List<EntityPlayer> logoutPositions = Lists.newArrayList();

   public Visualrange() {
      super("Visualrange", 0, Category.RENDER, "Visualrange");
      this.playSound = Main.setmgr.add(new Setting("playSound", this, true));
      this.leaving = Main.setmgr.add(new Setting("leaving", this, true));
      this.Box = Main.setmgr.add(new Setting("Box", this, true));
      this.color = Main.setmgr.add(new Setting("Logoff", this, 1.0D, 1.0D, 1.0D, 1.0D));
      this.Mode = Main.setmgr.add(new Setting("Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.ShowCoords = Main.setmgr.add(new Setting("ShowCoords", this, true));
   }

   public void onClientTick(ClientTickEvent event) {
      List<EntityPlayer> tempplayer = new ArrayList();
      Iterator var3 = mc.field_71441_e.func_72910_y().iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         if (entity instanceof EntityPlayer) {
            EntityPlayer entity1 = (EntityPlayer)entity;
            if (!entity1.func_70005_c_().equals(mc.field_71439_g.func_70005_c_())) {
               if (!this.knownPlayers.contains(entity1)) {
                  if (this.Box.getValBoolean()) {
                     logoutPositions.remove(entity1);
                  }

                  this.knownPlayers.add(entity1);
                  if (this.playSound.getValBoolean()) {
                     mc.func_147118_V().func_147682_a(PositionedSoundRecord.func_194007_a(SoundEvents.field_187604_bf, 1.0F, 1.0F));
                  }

                  ChatUtils.message(FriendManager.isFriend(entity1.func_70005_c_()) ? ChatFormatting.GREEN.toString() : ChatFormatting.RED.toString() + entity1.func_70005_c_() + ChatFormatting.RESET.toString() + (this.ShowCoords.getValBoolean() ? " Joined at, x: " + entity1.func_180425_c().func_177958_n() + " y: " + entity1.func_180425_c().func_177956_o() + " z: " + entity1.func_180425_c().func_177952_p() : " Joined!"));
               }

               tempplayer.add(entity1);
            }
         }
      }

      var3 = this.knownPlayers.iterator();

      while(var3.hasNext()) {
         EntityPlayer knownPlayer = (EntityPlayer)var3.next();
         if (!tempplayer.contains(knownPlayer)) {
            this.knownPlayers.remove(knownPlayer);
            if (this.leaving.getValBoolean()) {
               if (this.Box.getValBoolean()) {
                  logoutPositions.add(knownPlayer);
               }

               if (this.playSound.getValBoolean()) {
                  mc.func_147118_V().func_147682_a(PositionedSoundRecord.func_194007_a(SoundEvents.field_187604_bf, 1.0F, 1.0F));
               }

               ChatUtils.message(FriendManager.isFriend(knownPlayer.func_70005_c_()) ? ChatFormatting.GREEN.toString() : ChatFormatting.RED.toString() + knownPlayer.func_70005_c_() + ChatFormatting.RESET.toString() + (this.ShowCoords.getValBoolean() ? " Left at, x: " + knownPlayer.func_180425_c().func_177958_n() + " y: " + knownPlayer.func_180425_c().func_177956_o() + " z: " + knownPlayer.func_180425_c().func_177952_p() : " Left!"));
            }
         }
      }

   }

   public void onEnable() {
      this.knownPlayers = new ArrayList();
      if (mc.field_71439_g != null && mc.func_71356_B()) {
         this.toggle();
      }

   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (this.Box.getValBoolean()) {
         Iterator var2 = logoutPositions.iterator();

         while(var2.hasNext()) {
            EntityPlayer logoutPosition = (EntityPlayer)var2.next();
            double renderPosX = logoutPosition.field_70165_t - mc.func_175598_ae().field_78730_l;
            double renderPosY = logoutPosition.field_70163_u - mc.func_175598_ae().field_78731_m;
            double renderPosZ = logoutPosition.field_70161_v - mc.func_175598_ae().field_78728_n;
            AxisAlignedBB bb = new AxisAlignedBB(renderPosX, renderPosY, renderPosZ, renderPosX + 1.0D, renderPosY + 2.0D, renderPosZ + 1.0D);
            RenderUtils.SimpleNametag(logoutPosition.func_174791_d(), logoutPosition.func_70005_c_() + (this.ShowCoords.getValBoolean() ? "X: " + (int)logoutPosition.field_70165_t + " Y: " + (int)logoutPosition.field_70163_u + " Z: " + (int)logoutPosition.field_70161_v : ""));
            RenderUtils.RenderBlock(this.Mode.getValString(), bb, FriendManager.isFriend(logoutPosition.func_70005_c_()) ? ColorUtils.rainbow().getRGB() : this.color.getcolor(), this.LineWidth.getValDouble());
         }
      }

   }
}
