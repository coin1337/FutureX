package Method.Client.module.render;

import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

class MotionBlurResourceManager implements IResourceManager {
   public Set<String> func_135055_a() {
      return null;
   }

   public IResource func_110536_a(ResourceLocation resourceLocation) {
      return new MotionBlurResource();
   }

   public List<IResource> func_135056_b(ResourceLocation resourceLocation) {
      return null;
   }
}
