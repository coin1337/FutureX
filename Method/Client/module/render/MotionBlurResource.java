package Method.Client.module.render;

import java.io.InputStream;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

class MotionBlurResource implements IResource {
   public ResourceLocation func_177241_a() {
      return null;
   }

   public InputStream func_110527_b() {
      double amount = 0.7D + MotionBlur.blurAmount.getValDouble() / 100.0D * 3.0D - 0.01D;
      return IOUtils.toInputStream(String.format(Locale.ENGLISH, "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}", amount, amount, amount));
   }

   public boolean func_110528_c() {
      return false;
   }

   @Nullable
   public <T extends IMetadataSection> T func_110526_a(String s) {
      return null;
   }

   public String func_177240_d() {
      return null;
   }

   public void close() {
   }
}
