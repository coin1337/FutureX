package Method.Client.utils.Patcher.Core;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@MCVersion("1.12.2")
@TransformerExclusions({"Method.Client.utils.Patcher.Core"})
public final class CoreModPatcher implements IFMLLoadingPlugin {
   public static boolean IN_MCP = false;

   public String[] getASMTransformerClass() {
      return new String[]{ClassTransformer.class.getName()};
   }

   public String getModContainerClass() {
      return null;
   }

   @Nullable
   public String getSetupClass() {
      return null;
   }

   public void injectData(Map data) {
      IN_MCP = !(Boolean)data.get("runtimeDeobfuscationEnabled");
   }

   public String getAccessTransformerClass() {
      return ModAccessTransformer.class.getName();
   }
}
