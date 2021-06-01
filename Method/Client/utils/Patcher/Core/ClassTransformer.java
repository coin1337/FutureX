package Method.Client.utils.Patcher.Core;

import Method.Client.utils.Patcher.ModClassVisitor;
import Method.Client.utils.Patcher.Patches.BlockLiquidVisitor;
import Method.Client.utils.Patcher.Patches.EntityPlayerSPVisitor;
import Method.Client.utils.Patcher.Patches.EntityPlayerVisitor;
import Method.Client.utils.Patcher.Patches.ForgeBlockModelRendererVisitor;
import Method.Client.utils.Patcher.Patches.PlayerControllerMPVisitor;
import Method.Client.utils.Patcher.Patches.StateImplementationVisitor;
import Method.Client.utils.Patcher.Patches.TileEntityRendererDispatcherVisitor;
import Method.Client.utils.Patcher.Patches.VisGraphVisitor;
import java.util.HashMap;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public final class ClassTransformer implements IClassTransformer {
   private final HashMap<String, Class<? extends ModClassVisitor>> visitors = new HashMap();
   public static boolean obfuscated;

   public ClassTransformer() {
      this.visitors.put("net.minecraft.block.BlockLiquid", BlockLiquidVisitor.class);
      this.visitors.put("net.minecraft.client.entity.EntityPlayerSP", EntityPlayerSPVisitor.class);
      this.visitors.put("net.minecraft.entity.player.EntityPlayer", EntityPlayerVisitor.class);
      this.visitors.put("net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer", ForgeBlockModelRendererVisitor.class);
      this.visitors.put("net.minecraft.client.multiplayer.PlayerControllerMP", PlayerControllerMPVisitor.class);
      this.visitors.put("net.minecraft.block.state.BlockStateContainer$StateImplementation", StateImplementationVisitor.class);
      this.visitors.put("net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher", TileEntityRendererDispatcherVisitor.class);
      this.visitors.put("net.minecraft.client.renderer.chunk.VisGraph", VisGraphVisitor.class);
   }

   public byte[] transform(String name, String transformedName, byte[] basicClass) {
      if (!this.visitors.containsKey(transformedName)) {
         return basicClass;
      } else {
         System.out.println("Transforming " + transformedName + ", obfuscated=" + obfuscated);

         try {
            ClassReader reader = new ClassReader(basicClass);
            ClassWriter writer = new ClassWriter(3);
            reader.accept((ClassVisitor)((Class)this.visitors.get(transformedName)).getConstructor(ClassVisitor.class, Boolean.TYPE).newInstance(writer, obfuscated), 0);
            return writer.toByteArray();
         } catch (Exception var6) {
            var6.printStackTrace();
            return basicClass;
         }
      }
   }

   static {
      obfuscated = !FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/client/Minecraft").equals("net/minecraft/client/Minecraft");
   }
}
