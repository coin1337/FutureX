package Method.Client.module.misc;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.util.math.BlockPos;

public class PluginsGetter extends Module {
   public PluginsGetter() {
      super("PluginsGetter", 0, Category.MISC, "Trys Plugins Getter");
   }

   public void onEnable() {
      if (mc.field_71439_g != null) {
         Wrapper.INSTANCE.sendPacket(new CPacketTabComplete("/", (BlockPos)null, false));
         super.onEnable();
      }
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketTabComplete) {
         SPacketTabComplete s3APacketTabComplete = (SPacketTabComplete)packet;
         List<String> plugins = new ArrayList();
         String[] commands = s3APacketTabComplete.func_149630_c();
         String[] var6 = commands;
         int var7 = commands.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String s = var6[var8];
            String[] command = s.split(":");
            if (command.length > 1) {
               String pluginName = command[0].replace("/", "");
               if (!plugins.contains(pluginName)) {
                  plugins.add(pluginName);
               }
            }
         }

         Collections.sort(plugins);
         if (!plugins.isEmpty()) {
            ChatUtils.message("Plugins §7(§8" + plugins.size() + "§7): §9" + Strings.join((String[])plugins.toArray(new String[0]), "§7, §9"));
         } else {
            ChatUtils.error("No plugins found.");
         }

         this.toggle();
      }

      return true;
   }
}
