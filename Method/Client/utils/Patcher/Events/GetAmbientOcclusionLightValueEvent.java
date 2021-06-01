package Method.Client.utils.Patcher.Events;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Event;

public final class GetAmbientOcclusionLightValueEvent extends Event {
   private final IBlockState state;
   private float lightValue;
   private final float defaultLightValue;

   public GetAmbientOcclusionLightValueEvent(IBlockState state, float lightValue) {
      this.state = state;
      this.lightValue = lightValue;
      this.defaultLightValue = lightValue;
   }

   public IBlockState getState() {
      return this.state;
   }

   public float getLightValue() {
      return this.lightValue;
   }

   public void setLightValue(float lightValue) {
      this.lightValue = lightValue;
   }

   public float getDefaultLightValue() {
      return this.defaultLightValue;
   }
}
