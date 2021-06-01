package Method.Client.managers;

import Method.Client.utils.visual.ChatUtils;
import java.util.ArrayList;

public class FriendManager {
   public static ArrayList<String> friendsList = new ArrayList();

   public static void addFriend(String friendname) {
      if (!friendsList.contains(friendname)) {
         friendsList.add(friendname);
         FileManager.saveFriends();
         ChatUtils.message(friendname + " Added to §bfriends §7list.");
      }

   }

   public static void removeFriend(String friendname) {
      if (friendsList.contains(friendname)) {
         friendsList.remove(friendname);
         FileManager.saveFriends();
         ChatUtils.message(friendname + " Removed from §bfriends §7list.");
      }

   }

   public static void clear() {
      if (!friendsList.isEmpty()) {
         friendsList.clear();
         FileManager.saveFriends();
         ChatUtils.message("§bFriends §7list clear.");
      }

   }

   public static boolean isFriend(String name) {
      return friendsList.contains(name);
   }
}
