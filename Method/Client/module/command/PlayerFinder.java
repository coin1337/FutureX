package Method.Client.module.command;

import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;

public class PlayerFinder extends Command {
   public PlayerFinder() {
      super("pfind");
   }

   public void runCommand(String s, String[] args) {
      try {
         ArrayList<String> list = new ArrayList();
         Iterator var4;
         NetworkPlayerInfo npi;
         if (args[0].equalsIgnoreCase("all")) {
            var4 = ((NetHandlerPlayClient)Objects.requireNonNull(Wrapper.INSTANCE.mc().func_147114_u())).func_175106_d().iterator();

            while(var4.hasNext()) {
               npi = (NetworkPlayerInfo)var4.next();
               list.add("\n" + npi.func_178845_a().getName());
            }
         } else if (args[0].equalsIgnoreCase("creatives")) {
            var4 = ((NetHandlerPlayClient)Objects.requireNonNull(Wrapper.INSTANCE.mc().func_147114_u())).func_175106_d().iterator();

            while(var4.hasNext()) {
               npi = (NetworkPlayerInfo)var4.next();
               if (npi.func_178848_b().func_77145_d()) {
                  list.add("\n" + npi.func_178845_a().getName());
               }
            }
         }

         if (list.isEmpty()) {
            ChatUtils.error("List is empty.");
         } else {
            Wrapper.INSTANCE.copy(list.toString());
            ChatUtils.message("List copied to clipboard.");
         }
      } catch (Exception var6) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Get list of players.";
   }

   public String getSyntax() {
      return "pfind <all/creatives>";
   }
}
