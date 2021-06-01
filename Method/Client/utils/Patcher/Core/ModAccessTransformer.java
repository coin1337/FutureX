package Method.Client.utils.Patcher.Core;

import java.io.IOException;
import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

public final class ModAccessTransformer extends AccessTransformer {
   public ModAccessTransformer() throws IOException {
      super("META-INF/accesstransformer.cfg");
   }
}
