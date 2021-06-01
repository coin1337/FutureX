package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class UsernameHistory extends Command {
   public UsernameHistory() {
      super("NameHistory ");
   }

   public void runCommand(String s, String[] args) {
      String name = args[0];

      try {
         String uuid = this.grabUUID(name);
         String names = readURL(new URL("https://api.mojang.com/user/profiles/" + uuid + "/names"));
         if (names.isEmpty()) {
            ChatUtils.error(name + " has had no username changes.");
         } else {
            Collection<GameProfile> profiles = (Collection)(new Gson()).fromJson(names, (new TypeToken<Collection<GameProfile>>() {
            }).getType());
            String output = "";

            GameProfile profile;
            for(Iterator var8 = profiles.iterator(); var8.hasNext(); output = output + "\"" + profile.getName() + "\", ") {
               profile = (GameProfile)var8.next();
            }

            ChatUtils.warning(name + " has had the usernames: " + output.substring(0, output.length() - 2) + ".");
         }
      } catch (Exception var10) {
         ChatUtils.error("Failed to look up user.");
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   private String grabUUID(String name) {
      try {
         String userInfo = readURL(new URL("https://api.mojang.com/users/profiles/minecraft/" + name));
         Map<String, Object> output = (Map)(new Gson()).fromJson(userInfo, (new TypeToken<Map<String, Object>>() {
         }).getType());
         return output.get("id").toString();
      } catch (MalformedURLException var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static String readURL(URL url) {
      StringBuilder temp = new StringBuilder();
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

         String s;
         while((s = reader.readLine()) != null) {
            temp.append(s);
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException var11) {
               var11.printStackTrace();
            }
         }

      }

      return temp.toString();
   }

   public String getDescription() {
      return "Finds username's past";
   }

   public String getSyntax() {
      return "NameHistory <username>";
   }
}
