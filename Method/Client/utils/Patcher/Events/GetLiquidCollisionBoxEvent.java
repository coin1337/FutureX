package Method.Client.utils.Patcher.Events;

import net.minecraftforge.fml.common.eventhandler.Event;

public final class GetLiquidCollisionBoxEvent extends Event {
   private boolean solidCollisionBox;

   public boolean isSolidCollisionBox() {
      return this.solidCollisionBox;
   }

   public void setSolidCollisionBox() {
      this.solidCollisionBox = true;
   }
}
