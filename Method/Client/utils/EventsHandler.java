package Method.Client.utils;

import Method.Client.Main;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.combat.AntiBot;
import Method.Client.module.misc.ModSettings;
import Method.Client.module.render.NameTags;
import Method.Client.utils.Screens.NewScreen;
import Method.Client.utils.SeedViewer.WorldLoader;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.WorldDownloader;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.client.event.RenderTooltipEvent.PostBackground;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import org.lwjgl.input.Keyboard;

public class EventsHandler {
   public static boolean isInit = false;

   public boolean onPacket(Object packet, Connection.Side side) {
      boolean suc = true;

      try {
         Iterator var4 = ModuleManager.getEnabledmodules().iterator();

         while(var4.hasNext()) {
            Module m = (Module)var4.next();
            if (!isInit) {
               suc &= m.onDisablePacket(packet, side);
            }

            if (Wrapper.INSTANCE.world() != null) {
               suc &= m.onPacket(packet, side);
            }
         }
      } catch (RuntimeException var6) {
         this.cow("PacketError", var6);
      }

      return suc;
   }

   private void cow(String Evnt, RuntimeException ex) {
      if (ModSettings.ShowErrors.getValBoolean()) {
         ex.printStackTrace();
         ChatUtils.error("RuntimeException: " + Evnt);
         ChatUtils.error(ex.toString());
         Wrapper.INSTANCE.copy(ex.toString());
      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onClientTick(ClientTickEvent event) {
      if (Wrapper.INSTANCE.player() != null && Wrapper.INSTANCE.world() != null) {
         try {
            if (!isInit) {
               new Connection(this);
               isInit = true;
            }

            WorldDownloader.Clienttick();
            ModuleManager.onClientTick(event);
         } catch (RuntimeException var3) {
            this.cow("onClientTick", var3);
         }

      } else {
         AntiBot.bots.clear();
         isInit = false;

         try {
            NewScreen.onClientTick(event);
         } catch (RuntimeException var4) {
            this.cow("onClientTick", var4);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onKeyInput(KeyInputEvent event) {
      if (isInit) {
         try {
            int key = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
               ModuleManager.onKeyPressed(key);
            }
         } catch (RuntimeException var3) {
            this.cow("onKeyInput", var3);
         }

      }
   }

   @SubscribeEvent
   public void onConfigChanged(OnConfigChangedEvent event) {
      if (event.getModID().equals("futurex")) {
         (new Thread(() -> {
            while(true) {
               try {
                  if (Wrapper.mc.field_71462_r != Main.ClickGui) {
                     Wrapper.mc.func_147108_a(Main.ClickGui);
                     Thread.sleep(25L);
                     if (Wrapper.mc.field_71462_r != Main.ClickGui) {
                        continue;
                     }
                  }

                  Wrapper.mc.func_147108_a(Main.ClickGui);
               } catch (Exception var1) {
               }

               return;
            }
         })).start();
         Main.config.syncConfig();
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void ClientChatReceivedEvent(ClientChatReceivedEvent event) {
      if (isInit) {
         try {
            ModuleManager.ClientChatReceivedEvent(event);
         } catch (RuntimeException var3) {
            this.cow("ClientChatReceivedEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void PlayerRespawnEvent(PlayerRespawnEvent event) {
      if (isInit) {
         try {
            ModuleManager.PlayerRespawnEvent(event);
         } catch (RuntimeException var3) {
            this.cow("PlayerRespawnEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void BackgroundDrawnEvent(BackgroundDrawnEvent event) {
      if (isInit) {
         try {
            ModuleManager.BackgroundDrawnEvent(event);
         } catch (RuntimeException var3) {
            this.cow("BackgroundDrawnEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onRenderPre(Pre event) {
      if (isInit) {
         try {
            ModuleManager.onRenderPre(event);
         } catch (RuntimeException var3) {
            this.cow("onRenderPre", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void GetCollisionBoxesEvent(GetCollisionBoxesEvent event) {
      if (isInit) {
         try {
            ModuleManager.GetCollisionBoxesEvent(event);
         } catch (RuntimeException var3) {
            this.cow("GetCollisionBoxesEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void DrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
      if (isInit) {
         try {
            ModuleManager.DrawBlockHighlightEvent(event);
         } catch (RuntimeException var3) {
            this.cow("DrawBlockHighlightEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void ClientChatEvent(ClientChatEvent event) {
      if (isInit) {
         try {
            ModuleManager.ClientChatEvent(event);
         } catch (RuntimeException var3) {
            this.cow("ClientChatEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onLivingDeath(LivingDeathEvent event) {
      if (isInit) {
         try {
            ModuleManager.LivingDeathEvent(event);
         } catch (RuntimeException var3) {
            this.cow("LivingDeathEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void GuiScreenEventInit(Post event) {
      try {
         NewScreen.GuiScreenEventInit(event);
      } catch (RuntimeException var3) {
         this.cow("GuiScreenEventPre", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void DrawScreenEvent(DrawScreenEvent event) {
      try {
         NewScreen.DrawScreenEvent(event);
      } catch (RuntimeException var3) {
         this.cow("DrawScreenEvent", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void PopulateChunkEvent(Populate event) {
      try {
         WorldLoader.event(event);
      } catch (RuntimeException var3) {
         this.cow("PopulateChunkEvent", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void DecorateBiomeEvent(Decorate event) {
      try {
         WorldLoader.DecorateBiomeEvent(event);
      } catch (RuntimeException var3) {
         this.cow("DecorateBiomeEvent", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onWorldUnload(Unload event) {
      try {
         NewScreen.onWorldUnload(event);
         if (!isInit) {
            return;
         }

         ModuleManager.onWorldUnload(event);
      } catch (RuntimeException var3) {
         this.cow("onWorldUnload", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onWorldLoad(Load event) {
      if (isInit) {
         try {
            ModuleManager.onWorldLoad(event);
         } catch (RuntimeException var3) {
            this.cow("onWorldLoad", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void GuiScreenEventPost(net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post event) {
      try {
         NewScreen.GuiScreenEventPost(event);
      } catch (RuntimeException var3) {
         this.cow("GuiScreenEventActionPerformedEvent", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void GuiScreenEventPre(net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event) {
      try {
         NewScreen.GuiScreenEventPre(event);
      } catch (RuntimeException var3) {
         this.cow("GuiScreenEventPre", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void GuiOpen(GuiOpenEvent event) {
      try {
         NewScreen.GuiOpen(event);
         if (!isInit) {
            return;
         }

         ModuleManager.GuiOpen(event);
      } catch (RuntimeException var3) {
         this.cow("GuiOpen", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onCameraSetup(CameraSetup event) {
      if (isInit) {
         try {
            ModuleManager.onCameraSetup(event);
         } catch (RuntimeException var3) {
            this.cow("onCameraSetup", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void RenderBlockOverlayEvent(RenderBlockOverlayEvent event) {
      if (isInit) {
         try {
            ModuleManager.RenderBlockOverlayEvent(event);
         } catch (RuntimeException var3) {
            this.cow("RenderBlockOverlayEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void RenderPlayerEvent(RenderPlayerEvent event) {
      if (isInit) {
         try {
            ModuleManager.RenderPlayerEvent(event);
         } catch (RuntimeException var3) {
            this.cow("RenderPlayerEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void fogColor(FogColors event) {
      if (isInit) {
         try {
            ModuleManager.fogColor(event);
         } catch (RuntimeException var3) {
            this.cow("fogColor", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void fogDensity(FogDensity event) {
      if (isInit) {
         try {
            ModuleManager.fogDensity(event);
         } catch (RuntimeException var3) {
            this.cow("fogColor", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void renderNamePlate(net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre e) {
      try {
         if (NameTags.toggled && e.getEntity() instanceof EntityPlayer) {
            e.setCanceled(true);
         }
      } catch (RuntimeException var3) {
         this.cow("renderNamePlate", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onTooltip(ItemTooltipEvent event) {
      if (isInit) {
         try {
            ModuleManager.ItemTooltipEvent(event);
         } catch (RuntimeException var3) {
            this.cow("onCameraSetup", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void RendergameOverlay(RenderGameOverlayEvent event) {
      if (isInit) {
         try {
            ModuleManager.RendergameOverlay(event);
         } catch (RuntimeException var3) {
            this.cow("onCameraSetup", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void BedSleep(PlayerSleepInBedEvent event) {
      if (isInit) {
         try {
            ModuleManager.PlayerSleepInBedEvent(event);
         } catch (RuntimeException var3) {
            this.cow("onCameraSetup", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onItemPickup(EntityItemPickupEvent event) {
      if (isInit) {
         try {
            ModuleManager.onItemPickup(event);
         } catch (RuntimeException var3) {
            this.cow("onItemPickup", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onProjectileImpact(ProjectileImpactEvent event) {
      if (isInit) {
         try {
            ModuleManager.onProjectileImpact(event);
         } catch (RuntimeException var3) {
            this.cow("ProjectileImpact", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onAttackEntity(AttackEntityEvent event) {
      if (isInit) {
         try {
            ModuleManager.onAttackEntity(event);
         } catch (RuntimeException var3) {
            this.cow("onAttackEntity", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerTick(PlayerTickEvent event) {
      if (isInit) {
         try {
            ModuleManager.onPlayerTick(event);
         } catch (RuntimeException var3) {
            this.cow("onPlayerTick", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void Chunkevent(net.minecraftforge.event.world.ChunkEvent.Unload event) {
      if (isInit) {
         try {
            ModuleManager.ChunkeventUNLOAD(event);
         } catch (RuntimeException var3) {
            this.cow("ChunkeventUnload", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void Chunkevent(net.minecraftforge.event.world.ChunkEvent.Load event) {
      if (isInit) {
         try {
            WorldDownloader.ChunkeventLOAD(event);
            ModuleManager.ChunkeventLOAD(event);
         } catch (RuntimeException var3) {
            this.cow("ChunkeventLOAD", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onLivingUpdate(LivingUpdateEvent event) {
      if (isInit) {
         try {
            ModuleManager.onLivingUpdate(event);
         } catch (RuntimeException var3) {
            this.cow("onLivingUpdate", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.NORMAL
   )
   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (Wrapper.INSTANCE.player() != null && Wrapper.INSTANCE.world() != null && !Wrapper.INSTANCE.mcSettings().field_74319_N) {
         try {
            ModuleManager.onRenderWorldLast(event);
         } catch (RuntimeException var3) {
            this.cow("RenderWorldLastEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onRenderGameOverlay(Text event) {
      if (isInit) {
         try {
            OnscreenGUI.onRenderGameOverlay(event);
            ModuleManager.onRenderGameOverlay(event);
         } catch (RuntimeException var3) {
            this.cow("RenderGameOverlayEvent.Text", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void RenderGameOverLayPost(net.minecraftforge.client.event.RenderGameOverlayEvent.Post event) {
      if (isInit) {
         try {
            ModuleManager.RenderGameOverLayPost(event);
         } catch (RuntimeException var3) {
            this.cow("RenderGameOverLayPost", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void FOVModifier(FOVModifier event) {
      if (isInit) {
         try {
            ModuleManager.FOVModifier(event);
         } catch (RuntimeException var3) {
            this.cow("FOVModifier", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void PlayerLoggedInEvent(PlayerLoggedInEvent event) {
      try {
         if (event.player != null) {
            ArrayList<IRecipe> recipes = Lists.newArrayList(CraftingManager.field_193380_a);
            recipes.removeIf((recipe) -> {
               return recipe.func_77571_b().func_190926_b();
            });
            recipes.removeIf((recipe) -> {
               return recipe.func_192400_c().isEmpty();
            });
            event.player.func_192021_a(recipes);
         }
      } catch (RuntimeException var3) {
         this.cow("PlayerLoggedInEvent", var3);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onLeftClickBlock(LeftClickBlock event) {
      if (isInit) {
         try {
            ModuleManager.onLeftClickBlock(event);
         } catch (RuntimeException var3) {
            this.cow("onLeftClickBlock", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void WorldEvent(WorldEvent event) {
      if (isInit) {
         try {
            ModuleManager.WorldEvent(event);
         } catch (RuntimeException var3) {
            this.cow("WorldEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onRightClickBlock(RightClickBlock event) {
      if (isInit) {
         try {
            ModuleManager.onRightClickBlock(event);
         } catch (RuntimeException var3) {
            this.cow("OnRightClickBlock", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void postBackgroundTooltipRender(PostBackground event) {
      if (isInit) {
         try {
            ModuleManager.postBackgroundTooltipRender(event);
         } catch (RuntimeException var3) {
            this.cow("postBackgroundTooltipRender", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void RendertooltipPre(net.minecraftforge.client.event.RenderTooltipEvent.Pre event) {
      if (isInit) {
         try {
            ModuleManager.RendertooltipPre(event);
         } catch (RuntimeException var3) {
            this.cow("RendertooltipPre", var3);
         }

      }
   }

   @SubscribeEvent
   public void RenderTickEvent(RenderTickEvent event) {
      if (isInit) {
         try {
            ModuleManager.RenderTickEvent(event);
         } catch (RuntimeException var3) {
            this.cow("RenderTickEvent", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void postDrawScreen(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post event) {
      if (isInit) {
         try {
            ModuleManager.postDrawScreen(event);
         } catch (RuntimeException var3) {
            this.cow("postDrawScreen", var3);
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void GuiScreenEvent(GuiScreenEvent event) {
      if (isInit) {
         try {
            ModuleManager.GuiScreenEvent(event);
         } catch (RuntimeException var3) {
            this.cow("GuiScreenEvent", var3);
         }

      }
   }
}
