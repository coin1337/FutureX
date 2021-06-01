package Method.Client.utils.Screens;

import Method.Client.utils.Screens.Override.BookInsert;
import Method.Client.utils.Screens.Override.ChestGuiInsert;
import Method.Client.utils.Screens.Override.ConnectingInsert;
import Method.Client.utils.Screens.Override.DeathOverride;
import Method.Client.utils.Screens.Override.DisconnectedInsert;
import Method.Client.utils.Screens.Override.EscInsert;
import Method.Client.utils.Screens.Override.SignInsert;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class NewScreen {
   public static ArrayList<Screen> Screens = new ArrayList();

   public NewScreen() {
      addScreen(new EscInsert());
      addScreen(new SignInsert());
      addScreen(new ChestGuiInsert());
      addScreen(new BookInsert());
      addScreen(new DisconnectedInsert());
      addScreen(new ConnectingInsert());
      addScreen(new DeathOverride());
   }

   public static void addScreen(Screen m) {
      Screens.add(m);
   }

   public static void GuiOpen(GuiOpenEvent event) {
      Iterator var1 = Screens.iterator();

      while(var1.hasNext()) {
         Screen m = (Screen)var1.next();
         m.GuiOpen(event);
      }

   }

   public static void GuiScreenEventPost(Post event) {
      Iterator var1 = Screens.iterator();

      while(var1.hasNext()) {
         Screen m = (Screen)var1.next();
         m.GuiScreenEventPost(event);
      }

   }

   public static void GuiScreenEventInit(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post event) {
      Iterator var1 = Screens.iterator();

      while(var1.hasNext()) {
         Screen m = (Screen)var1.next();
         m.GuiScreenEventInit(event);
      }

   }

   public static void GuiScreenEventPre(Pre event) {
      Iterator var1 = Screens.iterator();

      while(var1.hasNext()) {
         Screen m = (Screen)var1.next();
         m.GuiScreenEventPre(event);
      }

   }

   public static void onClientTick(ClientTickEvent event) {
      Iterator var1 = Screens.iterator();

      while(var1.hasNext()) {
         Screen m = (Screen)var1.next();
         m.onClientTick(event);
      }

   }

   public static void DrawScreenEvent(DrawScreenEvent event) {
      Iterator var1 = Screens.iterator();

      while(var1.hasNext()) {
         Screen m = (Screen)var1.next();
         m.DrawScreenEvent(event);
      }

   }

   public static void onWorldUnload(Unload event) {
      Iterator var1 = Screens.iterator();

      while(var1.hasNext()) {
         Screen m = (Screen)var1.next();
         m.onWorldUnload(event);
      }

   }
}
