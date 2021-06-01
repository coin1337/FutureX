package Method.Client.module.misc;

import Method.Client.FutureXDiscordRPC;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.ChatUtils;

public class DiscordRPCModule extends Module {
   public DiscordRPCModule() {
      super("DiscordRPC", 0, Category.MISC, "Discord Rich Presence");
   }

   public void onEnable() {
      FutureXDiscordRPC.init(false);
      ChatUtils.warning("If Discord RPC doesn't appear you will need to relog");
   }

   public void onDisable() {
      FutureXDiscordRPC.init(true);
   }
}
