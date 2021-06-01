package Method.Client.module.command;

import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.network.play.client.CPacketChatMessage;

public class Say extends Command {
   public Say() {
      super("say");
   }

   public void runCommand(String s, String[] args) {
      try {
         String content = "";

         for(int i = 0; i < args.length; ++i) {
            content = content + " " + args[i];
         }

         Wrapper.INSTANCE.sendPacket(new CPacketChatMessage(content));
      } catch (Exception var5) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Send message to chat.";
   }

   public String getSyntax() {
      return "say <message>";
   }
}
