package Method.Client.utils;

import Method.Client.module.ModuleManager;
import Method.Client.utils.Patcher.Events.EntityPlayerJumpEvent;
import Method.Client.utils.Patcher.Events.EventBookPage;
import Method.Client.utils.Patcher.Events.EventCanCollide;
import Method.Client.utils.Patcher.Events.GetAmbientOcclusionLightValueEvent;
import Method.Client.utils.Patcher.Events.GetLiquidCollisionBoxEvent;
import Method.Client.utils.Patcher.Events.PlayerDamageBlockEvent;
import Method.Client.utils.Patcher.Events.PlayerMoveEvent;
import Method.Client.utils.Patcher.Events.PostMotionEvent;
import Method.Client.utils.Patcher.Events.PreMotionEvent;
import Method.Client.utils.Patcher.Events.RenderBlockModelEvent;
import Method.Client.utils.Patcher.Events.RenderTileEntityEvent;
import Method.Client.utils.Patcher.Events.SetOpaqueCubeEvent;
import Method.Client.utils.Patcher.Events.ShouldSideBeRenderedEvent;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomEventsHandler {
   private void cow(String Evnt, RuntimeException ex) {
      ex.printStackTrace();
      ChatUtils.error("RuntimeException: " + Evnt);
      ChatUtils.error(ex.toString());
      Wrapper.INSTANCE.copy(ex.toString());
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onSetOpaqueCube(SetOpaqueCubeEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.SetOpaqueCubeEvent(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void EventBookPage(EventBookPage event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.EventBookPage(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void PreMotionEvent(PreMotionEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.PreMotionEvent(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void PostMotionEvent(PostMotionEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.PostMotionEvent(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerMove(PlayerMoveEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.onPlayerMove(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerJump(EntityPlayerJumpEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.onPlayerJump(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void DamageBlock(PlayerDamageBlockEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.DamageBlock(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onGetLiquidCollisionBox(GetLiquidCollisionBoxEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.onGetLiquidCollisionBox(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onGetAmbientOcclusionLightValue(GetAmbientOcclusionLightValueEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.onGetAmbientOcclusionLightValue(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onShouldSideBeRendered(ShouldSideBeRenderedEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.onShouldSideBeRendered(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void Liquidvisitor(EventCanCollide event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.EventCanCollide(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onRenderBlockModel(RenderBlockModelEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.onRenderBlockModel(event);
         } catch (RuntimeException var3) {
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onRenderTileEntity(RenderTileEntityEvent event) {
      if (EventsHandler.isInit) {
         try {
            ModuleManager.onRenderTileEntity(event);
         } catch (RuntimeException var3) {
            this.cow("onRenderTileEntity", var3);
         }

      }
   }
}
