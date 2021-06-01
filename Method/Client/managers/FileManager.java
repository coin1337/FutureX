package Method.Client.managers;

import Method.Client.Main;
import Method.Client.clickgui.ClickGui;
import Method.Client.clickgui.component.Frame;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.Profiles.Profiletem;
import Method.Client.utils.Screens.Custom.Search.SearchGuiSettings;
import Method.Client.utils.Screens.Custom.Xray.XrayGuiSettings;
import Method.Client.utils.system.Wrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class FileManager {
   private static final Gson gsonPretty = (new GsonBuilder()).setPrettyPrinting().create();
   private static final JsonParser jsonParser = new JsonParser();
   public static final File SaveDir;
   private static final File Mods;
   private static final File XRAYDATA;
   private static final File FRIENDS;
   private static final File ONSCREEN;
   private static final File SEARCH;
   private static final File PROFILES;

   public FileManager() {
      if (!SaveDir.exists()) {
         SaveDir.mkdir();
      }

      if (!Mods.exists()) {
         SaveMods();
      }

      if (!ONSCREEN.exists()) {
         saveframes();
      } else {
         Loadpos();
      }

      if (!XRAYDATA.exists()) {
         saveXRayData();
      } else {
         loadXRayData();
      }

      if (!PROFILES.exists()) {
         savePROFILES();
      }

      if (!SEARCH.exists()) {
         saveSearchData();
      } else {
         loadSearchData();
      }

      if (!FRIENDS.exists()) {
         saveFriends();
      } else {
         loadFriends();
      }

   }

   public static void loadPROFILES() {
      try {
         BufferedReader loadJson = new BufferedReader(new FileReader(PROFILES));
         JsonObject moduleJason = (JsonObject)jsonParser.parse(loadJson);
         loadJson.close();
         Iterator var2 = moduleJason.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<String, JsonElement> entry = (Entry)var2.next();
            Module m = new Profiletem((String)entry.getKey());
            ModuleManager.addModule(m);
            JsonObject jsonMod = (JsonObject)entry.getValue();
            m.setKeys(jsonMod.get("key").getAsString());
            m.visible = jsonMod.get("Visible").getAsBoolean();
            ArrayList<Module> Modstore = new ArrayList();
            Iterator var7 = ModuleManager.modules.iterator();

            while(var7.hasNext()) {
               Module module = (Module)var7.next();
               if (!module.getCategory().equals(Category.ONSCREEN) && !module.getCategory().equals(Category.PROFILES) && jsonMod.has(module.getName())) {
                  Modstore.add(module);
               }
            }

            m.setStoredModules(Modstore);
            ArrayList<Setting> Setstore = new ArrayList();
            Iterator var14 = Modstore.iterator();

            while(var14.hasNext()) {
               Module module = (Module)var14.next();

               Setting s;
               for(Iterator var10 = Main.setmgr.getSettingsByMod(module).iterator(); var10.hasNext(); Setstore.add(s)) {
                  s = (Setting)var10.next();
                  if (s.getMode().equals("Slider")) {
                     s.setValDouble(jsonMod.get(s.getName()).getAsDouble());
                  }

                  if (s.getMode().equals("Check")) {
                     s.setValBoolean(jsonMod.get(s.getName()).getAsBoolean());
                  }

                  if (s.getMode().equals("Combo")) {
                     s.setValString(jsonMod.get(s.getName()).getAsString());
                  }

                  if (s.getMode().equals("Color")) {
                     s.setValDouble(jsonMod.get(s.getName() + "c").getAsDouble());
                     s.setsaturation((float)jsonMod.get(s.getName() + "s").getAsDouble());
                     s.setbrightness((float)jsonMod.get(s.getName() + "b").getAsDouble());
                     s.setAlpha((float)jsonMod.get(s.getName() + "a").getAsDouble());
                  }
               }
            }

            m.setStoredSettings(Setstore);
         }
      } catch (IOException | NullPointerException var12) {
         var12.printStackTrace();
      }

   }

   public static void savePROFILES() {
      try {
         JsonObject json = new JsonObject();
         Iterator var1 = ModuleManager.getModulesInCategory(Category.PROFILES).iterator();

         while(var1.hasNext()) {
            Module m = (Module)var1.next();
            JsonObject JsonMod = new JsonObject();
            JsonMod.addProperty("key", m.getKeys().toString());
            JsonMod.addProperty("Visible", m.visible);
            json.add(m.getName(), JsonMod);
            Iterator var4 = m.getStoredSettings().iterator();

            while(var4.hasNext()) {
               Setting s = (Setting)var4.next();
               if (s.getMode().equals("Slider")) {
                  JsonMod.addProperty(s.getName(), s.getValDouble());
               }

               if (s.getMode().equals("Check")) {
                  JsonMod.addProperty(s.getName(), s.getValBoolean());
               }

               if (s.getMode().equals("Combo")) {
                  JsonMod.addProperty(s.getName(), s.getValString());
               }

               if (s.getMode().equals("Color")) {
                  JsonMod.addProperty(s.getName() + "c", s.getValDouble());
                  JsonMod.addProperty(s.getName() + "s", s.getSat());
                  JsonMod.addProperty(s.getName() + "b", s.getBri());
                  JsonMod.addProperty(s.getName() + "a", s.getAlpha());
               }
            }

            var4 = m.getStoredModules().iterator();

            while(var4.hasNext()) {
               Module storedModule = (Module)var4.next();
               JsonMod.addProperty(storedModule.getName(), "Toggled");
            }
         }

         PrintWriter saveJson = new PrintWriter(new FileWriter(PROFILES));
         saveJson.println(gsonPretty.toJson(json));
         saveJson.close();
      } catch (IOException | NullPointerException var6) {
         var6.printStackTrace();
      }

   }

   public static void SaveMods() {
      try {
         JsonObject json = new JsonObject();
         ModuleManager.getModules().forEach((module) -> {
            Save(json, module);
         });
         JsonObject JsonMod = new JsonObject();
         JsonMod.addProperty("PREFIX", CommandManager.cmdPrefix);
         json.add("PREFIX", JsonMod);
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("VERSION", "0.0.1");
         json.add("VERSION", jsonObject);
         PrintWriter saveJson = new PrintWriter(new FileWriter(Mods));
         saveJson.println(gsonPretty.toJson(json));
         saveJson.close();
      } catch (IOException | NullPointerException var4) {
         var4.printStackTrace();
      }

   }

   private static void Save(JsonObject json, Module m) {
      JsonObject JsonMod = new JsonObject();
      JsonMod.addProperty("toggled", m.isToggled());
      JsonMod.addProperty("key", m.getKeys().toString());
      JsonMod.addProperty("Visible", m.visible);
      json.add(m.getName(), JsonMod);
      Iterator var3 = Main.setmgr.getSettingsByMod(m).iterator();

      while(var3.hasNext()) {
         Setting s = (Setting)var3.next();
         if (s.getMode().equals("Slider")) {
            JsonMod.addProperty(s.getName(), s.getValDouble());
         }

         if (s.getMode().equals("Check")) {
            JsonMod.addProperty(s.getName(), s.getValBoolean());
         }

         if (s.getMode().equals("Combo")) {
            JsonMod.addProperty(s.getName(), s.getValString());
         }

         if (s.getMode().equals("Color")) {
            JsonMod.addProperty(s.getName() + "c", s.getValDouble());
            JsonMod.addProperty(s.getName() + "s", s.getSat());
            JsonMod.addProperty(s.getName() + "b", s.getBri());
            JsonMod.addProperty(s.getName() + "a", s.getAlpha());
         }
      }

   }

   public static void LoadMods() {
      try {
         BufferedReader loadJson = new BufferedReader(new FileReader(Mods));
         JsonObject moduleJason = (JsonObject)jsonParser.parse(loadJson);
         loadJson.close();

         Entry entry;
         Module mods;
         for(Iterator var2 = moduleJason.entrySet().iterator(); var2.hasNext(); Load(entry, mods)) {
            entry = (Entry)var2.next();
            mods = ModuleManager.getModuleByName((String)entry.getKey());
            if (((String)entry.getKey()).equals("PREFIX")) {
               JsonObject jsonMod = (JsonObject)entry.getValue();
               CommandManager.cmdPrefix = jsonMod.get("PREFIX").getAsCharacter();
            }
         }
      } catch (IOException | NullPointerException var6) {
         var6.printStackTrace();
      }

   }

   private static void Load(Entry<String, JsonElement> entry, Module m) {
      if (m != null) {
         JsonObject jsonMod = (JsonObject)entry.getValue();
         m.setKeys(jsonMod.get("key").getAsString());
         m.visible = jsonMod.get("Visible").getAsBoolean();
         Iterator var3 = Main.setmgr.getSettingsByMod(m).iterator();

         while(var3.hasNext()) {
            Setting s = (Setting)var3.next();
            if (s.getMode().equals("Slider")) {
               s.setValDouble(jsonMod.get(s.getName()).getAsDouble());
            }

            if (s.getMode().equals("Check")) {
               s.setValBoolean(jsonMod.get(s.getName()).getAsBoolean());
            }

            if (s.getMode().equals("Combo")) {
               s.setValString(jsonMod.get(s.getName()).getAsString());
            }

            if (s.getMode().equals("Color")) {
               s.setValDouble(jsonMod.get(s.getName() + "c").getAsDouble());
               s.setsaturation((float)jsonMod.get(s.getName() + "s").getAsDouble());
               s.setbrightness((float)jsonMod.get(s.getName() + "b").getAsDouble());
               s.setAlpha((float)jsonMod.get(s.getName() + "a").getAsDouble());
            }
         }

         if (jsonMod.get("toggled").getAsBoolean()) {
            ModuleManager.FileManagerLoader.add(m);
            m.setToggled(true);
         }
      }

   }

   public static void loadSearchData() {
      try {
         BufferedReader loadJson = new BufferedReader(new FileReader(SEARCH));
         JsonArray json = (JsonArray)jsonParser.parse(loadJson);
         loadJson.close();
         SearchGuiSettings.fromJson(json);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static void saveSearchData() {
      try {
         JsonElement json = SearchGuiSettings.toJson();
         PrintWriter saveJson = new PrintWriter(new FileWriter(SEARCH));
         saveJson.println(gsonPretty.toJson(json));
         saveJson.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static void Loadpos() {
      try {
         BufferedReader loadJson = new BufferedReader(new FileReader(ONSCREEN));
         JsonObject moduleJason = (JsonObject)jsonParser.parse(loadJson);
         loadJson.close();
         Iterator var2 = moduleJason.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<String, JsonElement> entry = (Entry)var2.next();
            JsonObject jsonMod = (JsonObject)entry.getValue();
            Iterator var5 = ClickGui.frames.iterator();

            while(var5.hasNext()) {
               Frame frame = (Frame)var5.next();
               if (((String)entry.getKey()).equals(frame.getName())) {
                  frame.setX(jsonMod.get("x").getAsInt());
                  frame.setY(jsonMod.get("y").getAsInt());
                  frame.setOpen(jsonMod.get("open").getAsBoolean());
               }
            }
         }
      } catch (IOException | NullPointerException var7) {
         var7.printStackTrace();
      }

   }

   public static void loadFriends() {
      List<String> friends = read(FRIENDS);
      Iterator var1 = friends.iterator();

      while(var1.hasNext()) {
         String name = (String)var1.next();
         FriendManager.addFriend(name);
      }

   }

   public static void saveXRayData() {
      try {
         JsonElement json = XrayGuiSettings.toJson();
         PrintWriter saveJson = new PrintWriter(new FileWriter(XRAYDATA));
         saveJson.println(gsonPretty.toJson(json));
         saveJson.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static void loadXRayData() {
      try {
         BufferedReader loadJson = new BufferedReader(new FileReader(XRAYDATA));
         JsonArray json = (JsonArray)jsonParser.parse(loadJson);
         loadJson.close();
         XrayGuiSettings.fromJson(json);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static void saveframes() {
      try {
         JsonObject json = new JsonObject();
         Iterator var1 = ClickGui.frames.iterator();

         while(var1.hasNext()) {
            Frame frame = (Frame)var1.next();
            JsonObject jsonHack = new JsonObject();
            jsonHack.addProperty("x", frame.getX());
            jsonHack.addProperty("y", frame.getY());
            jsonHack.addProperty("open", frame.isOpen());
            json.add(frame.getName(), jsonHack);
         }

         PrintWriter saveJson = new PrintWriter(new FileWriter(ONSCREEN));
         saveJson.println(gsonPretty.toJson(json));
         saveJson.close();
      } catch (IOException | NullPointerException var4) {
         var4.printStackTrace();
      }

   }

   public static void saveFriends() {
      write(FRIENDS, FriendManager.friendsList, true, true);
   }

   public static void write(File outputFile, List<String> writeContent, boolean newline, boolean overrideContent) {
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new FileWriter(outputFile, !overrideContent));
         Iterator var5 = writeContent.iterator();

         while(var5.hasNext()) {
            String outputLine = (String)var5.next();
            writer.write(outputLine);
            writer.flush();
            if (newline) {
               writer.newLine();
            }
         }
      } catch (Exception var8) {
         try {
            if (writer != null) {
               writer.close();
            }
         } catch (Exception var7) {
         }
      }

   }

   public static List<String> read(File inputFile) {
      ArrayList<String> readContent = new ArrayList();
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new FileReader(inputFile));

         String line;
         while((line = reader.readLine()) != null) {
            readContent.add(line);
         }
      } catch (Exception var6) {
         try {
            if (reader != null) {
               reader.close();
            }
         } catch (Exception var5) {
         }
      }

      return readContent;
   }

   static {
      SaveDir = new File(String.format("%s%s%s-%s-%s%s", Wrapper.INSTANCE.mc().field_71412_D, File.separator, "FutureX", "1.12.2", "0.0.1", File.separator));
      Mods = new File(SaveDir, "mods.json");
      XRAYDATA = new File(SaveDir, "xraydata.json");
      FRIENDS = new File(SaveDir, "friends.json");
      ONSCREEN = new File(SaveDir, "onscreen.json");
      SEARCH = new File(SaveDir, "search.json");
      PROFILES = new File(SaveDir, "profiles.json");
   }
}
