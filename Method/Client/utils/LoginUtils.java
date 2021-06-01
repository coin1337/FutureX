package Method.Client.utils;

import Method.Client.utils.system.Wrapper;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LoginUtils {
   public static String loginAlt(String email, String password) {
      YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)authenticationService.createUserAuthentication(Agent.MINECRAFT);
      authentication.setUsername(email);
      authentication.setPassword(password);
      String displayText = "Logged [License]: " + Wrapper.INSTANCE.mc().func_110432_I().func_111285_a();

      try {
         authentication.logIn();
         Minecraft.func_71410_x().field_71449_j = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang");
      } catch (AuthenticationUnavailableException var6) {
         displayText = "Cannot contact authentication server!";
      } catch (AuthenticationException var7) {
         if (!var7.getMessage().contains("Invalid username or password.") && !var7.getMessage().toLowerCase().contains("account migrated")) {
            displayText = "Cannot contact authentication server!";
         } else {
            displayText = "Wrong password!";
         }
      } catch (NullPointerException var8) {
         displayText = "Wrong password!";
      }

      return displayText;
   }

   public static String getName(String email, String password) {
      YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
      YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)authenticationService.createUserAuthentication(Agent.MINECRAFT);
      authentication.setUsername(email);
      authentication.setPassword(password);

      try {
         authentication.logIn();
         return authentication.getSelectedProfile().getName();
      } catch (Exception var5) {
         return null;
      }
   }

   public static void changeCrackedName(String name) {
      Minecraft.func_71410_x().field_71449_j = new Session(name, "", "", "mojang");
   }
}
