package Method.Client.module.command;

import Method.Client.managers.FriendManager;
import Method.Client.utils.Utils;
import Method.Client.utils.visual.ChatUtils;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;

public class Friend extends Command {
   public Friend() {
      super("friend");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (args[0].equalsIgnoreCase("add")) {
            if (args[1].equalsIgnoreCase("all")) {
               Iterator var3 = mc.field_71441_e.field_72996_f.iterator();

               while(var3.hasNext()) {
                  Object object = var3.next();
                  if (object instanceof EntityPlayer) {
                     EntityPlayer player = (EntityPlayer)object;
                     if (!player.func_82150_aj()) {
                        FriendManager.addFriend(Utils.getPlayerName(player));
                     }
                  }
               }
            } else {
               FriendManager.addFriend(args[1]);
            }
         }

         if (args[0].equalsIgnoreCase("list")) {
            ChatUtils.message(FriendManager.friendsList.toString());
         } else if (args[0].equalsIgnoreCase("remove")) {
            FriendManager.removeFriend(args[1]);
         } else if (args[0].equalsIgnoreCase("clear")) {
            FriendManager.clear();
         }
      } catch (Exception var6) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Friend manager.";
   }

   public String getSyntax() {
      return "friend <add/remove/list/clear> <nick>";
   }
}
