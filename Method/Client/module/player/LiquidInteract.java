package Method.Client.module.player;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Patcher.Events.EventCanCollide;
import Method.Client.utils.Patcher.Events.GetLiquidCollisionBoxEvent;

public class LiquidInteract extends Module {
   public LiquidInteract() {
      super("LiquidInteract", 0, Category.PLAYER, "LiquidInteract");
   }

   public void EventCanCollide(EventCanCollide event) {
      event.setCanceled(true);
   }

   public void GetLiquidCollisionBoxEvent(GetLiquidCollisionBoxEvent event) {
      event.setSolidCollisionBox();
   }
}
