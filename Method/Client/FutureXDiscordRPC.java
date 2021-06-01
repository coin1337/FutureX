package Method.Client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;

public class FutureXDiscordRPC {
   private static final String ClientId = "842487217473323088";
   private static final Minecraft mc = Minecraft.func_71410_x();
   private static final DiscordRPC rpc;
   public static DiscordRichPresence presence;
   private static String details;
   private static String state;
   private static String version;

   public static void init(boolean stop) {
      if (stop) {
         rpc.Discord_Shutdown();
      } else {
         DiscordEventHandlers handlers = new DiscordEventHandlers();
         handlers.disconnected = (var1, var2) -> {
            System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2);
         };
         rpc.Discord_Initialize("842487217473323088", handlers, true, "");
         presence.startTimestamp = System.currentTimeMillis() / 1000L;
         presence.details = "FutureX on top";
         presence.state = "Main Menu";
         presence.largeImageKey = "futurex";
         presence.largeImageText = "v" + version;
         rpc.Discord_UpdatePresence(presence);
         (new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
               try {
                  rpc.Discord_RunCallbacks();
                  details = "FutureX on top";
                  state = "";
                  if (mc.func_71387_A()) {
                     state = "Playing Singleplayer";
                  } else if (mc.func_147104_D() != null) {
                     if (!mc.func_147104_D().field_78845_b.equals("")) {
                        state = "Playing " + mc.func_147104_D().field_78845_b;
                     }
                  } else {
                     state = "Main Menu";
                  }

                  if (!details.equals(presence.details) || !state.equals(presence.state)) {
                     presence.startTimestamp = System.currentTimeMillis() / 1000L;
                  }

                  presence.details = details;
                  presence.state = state;
                  rpc.Discord_UpdatePresence(presence);
               } catch (Exception var2) {
                  var2.printStackTrace();
               }

               try {
                  Thread.sleep(5000L);
               } catch (InterruptedException var1) {
                  var1.printStackTrace();
               }
            }

            rpc.Discord_Shutdown();
         }, "Discord-RPC-Callback-Handler")).start();
      }
   }

   static {
      rpc = DiscordRPC.INSTANCE;
      presence = new DiscordRichPresence();
      version = "0.0.1";
   }
}
