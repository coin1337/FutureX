package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;

public class NoRender extends Module {
   Setting stopParticles;
   Setting stopExplosions;
   Setting helmet;
   Setting portal;
   Setting crosshair;
   Setting bosshealth;
   Setting bossinfo;
   Setting armor;
   Setting health;
   Setting food;
   Setting air;
   Setting hotbar;
   Setting experience;
   Setting horsehealth;
   Setting horsejump;
   Setting chat;
   Setting playerlist;
   Setting potionicon;
   Setting subtitles;
   Setting fpsgraph;
   Setting vignette;
   Setting Blockoverlay;
   Setting Liquidoverlay;

   public NoRender() {
      super("NoRender", 0, Category.RENDER, "NoRender");
      this.stopParticles = Main.setmgr.add(new Setting("stopParticles", this, false));
      this.stopExplosions = Main.setmgr.add(new Setting("stop Explosions", this, false));
      this.helmet = Main.setmgr.add(new Setting("helmet", this, false));
      this.portal = Main.setmgr.add(new Setting("portal", this, false));
      this.crosshair = Main.setmgr.add(new Setting("crosshair", this, false));
      this.bosshealth = Main.setmgr.add(new Setting("bosshealth", this, false));
      this.bossinfo = Main.setmgr.add(new Setting("bossinfo", this, false));
      this.armor = Main.setmgr.add(new Setting("armor", this, false));
      this.health = Main.setmgr.add(new Setting("health", this, false));
      this.food = Main.setmgr.add(new Setting("food", this, false));
      this.air = Main.setmgr.add(new Setting("air", this, false));
      this.hotbar = Main.setmgr.add(new Setting("hotbar", this, false));
      this.experience = Main.setmgr.add(new Setting("experience", this, false));
      this.horsehealth = Main.setmgr.add(new Setting("horsehealth", this, false));
      this.horsejump = Main.setmgr.add(new Setting("horsejump", this, false));
      this.chat = Main.setmgr.add(new Setting("chat", this, false));
      this.playerlist = Main.setmgr.add(new Setting("playerlist", this, false));
      this.potionicon = Main.setmgr.add(new Setting("potionicon", this, false));
      this.subtitles = Main.setmgr.add(new Setting("subtitles", this, false));
      this.fpsgraph = Main.setmgr.add(new Setting("fpsgraph", this, false));
      this.vignette = Main.setmgr.add(new Setting("vignette", this, false));
      this.Blockoverlay = Main.setmgr.add(new Setting("Liquidoverlay", this, false));
      this.Liquidoverlay = Main.setmgr.add(new Setting("Blockoverlay", this, false));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketExplosion && this.stopExplosions.getValBoolean()) {
         return false;
      } else {
         return !this.stopParticles.getValBoolean() || !(packet instanceof SPacketParticles);
      }
   }

   public void fogDensity(FogDensity event) {
      if (this.Liquidoverlay.getValBoolean() && (event.getState().func_185904_a().equals(Material.field_151586_h) || event.getState().func_185904_a().equals(Material.field_151587_i))) {
         event.setDensity(0.0F);
         event.setCanceled(true);
      }

   }

   public void RenderBlockOverlayEvent(RenderBlockOverlayEvent event) {
      if (this.Blockoverlay.getValBoolean()) {
         event.setCanceled(true);
      }

   }

   public void RendergameOverlay(RenderGameOverlayEvent event) {
      switch(event.getType()) {
      case HELMET:
         event.setCanceled(this.helmet.getValBoolean());
         break;
      case PORTAL:
         event.setCanceled(this.portal.getValBoolean());
         break;
      case CROSSHAIRS:
         event.setCanceled(this.crosshair.getValBoolean());
         break;
      case BOSSHEALTH:
         event.setCanceled(this.bosshealth.getValBoolean());
         break;
      case BOSSINFO:
         event.setCanceled(this.bossinfo.getValBoolean());
         break;
      case ARMOR:
         event.setCanceled(this.armor.getValBoolean());
         break;
      case HEALTH:
         event.setCanceled(this.health.getValBoolean());
         break;
      case FOOD:
         event.setCanceled(this.food.getValBoolean());
         break;
      case AIR:
         event.setCanceled(this.air.getValBoolean());
         break;
      case HOTBAR:
         event.setCanceled(this.hotbar.getValBoolean());
         break;
      case EXPERIENCE:
         event.setCanceled(this.experience.getValBoolean());
         break;
      case HEALTHMOUNT:
         if (mc.field_71439_g.func_184218_aH()) {
            event.setCanceled(this.horsehealth.getValBoolean());
         }
         break;
      case JUMPBAR:
         if (mc.field_71439_g.func_184218_aH()) {
            event.setCanceled(this.horsejump.getValBoolean());
         }
         break;
      case CHAT:
         event.setCanceled(this.chat.getValBoolean());
         break;
      case PLAYER_LIST:
         event.setCanceled(this.playerlist.getValBoolean());
         break;
      case POTION_ICONS:
         event.setCanceled(this.potionicon.getValBoolean());
         break;
      case SUBTITLES:
         event.setCanceled(this.subtitles.getValBoolean());
         break;
      case FPS_GRAPH:
         event.setCanceled(this.fpsgraph.getValBoolean());
         break;
      case VIGNETTE:
         event.setCanceled(this.vignette.getValBoolean());
      }

   }
}
