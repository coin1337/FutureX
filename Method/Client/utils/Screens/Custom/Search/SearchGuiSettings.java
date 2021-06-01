package Method.Client.utils.Screens.Custom.Search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.block.Block;

public class SearchGuiSettings {
   public static ArrayList<String> blockNames = new ArrayList();
   public static String[] defaultNames;

   public static String getName(Block block) {
      return "" + Block.field_149771_c.func_177774_c(block);
   }

   public SearchGuiSettings(Block... blocks) {
      Stream var10000 = ((Stream)Arrays.stream(blocks).parallel()).map(SearchGuiSettings::getName).distinct().sorted();
      ArrayList var10001 = blockNames;
      var10000.forEachOrdered(var10001::add);
      defaultNames = (String[])blockNames.toArray(new String[0]);
   }

   public static List<String> getBlockNames() {
      return Collections.unmodifiableList(blockNames);
   }

   public static List<String> getAllBlockNames(List<String> blockNames) {
      ArrayList<String> Allblocks = new ArrayList();
      Iterator var2 = Block.field_149771_c.iterator();

      while(var2.hasNext()) {
         Block block = (Block)var2.next();
         if (!blockNames.contains(block.func_149732_F())) {
            Allblocks.add(getName(block));
         }
      }

      return Allblocks;
   }

   public static void add(Block block) {
      String name = getName(block);
      if (Collections.binarySearch(blockNames, name) < 0) {
         blockNames.add(name);
         Collections.sort(blockNames);
      }
   }

   public static void remove(int index) {
      if (index >= 0 && index < blockNames.size()) {
         blockNames.remove(index);
      }
   }

   public static void resetToDefaults() {
      blockNames.clear();
      blockNames.addAll(Arrays.asList(defaultNames));
   }

   public static List<String> SearchBlocksAll(List<String> blockNames, String text) {
      ArrayList<String> Allblocks = new ArrayList();
      Iterator var3 = Block.field_149771_c.iterator();

      while(var3.hasNext()) {
         Block block = (Block)var3.next();
         if (!blockNames.contains(block.func_149732_F()) && block.func_149732_F().toLowerCase().contains(text.toLowerCase())) {
            Allblocks.add(getName(block));
         }
      }

      return Allblocks;
   }

   public static void fromJson(JsonElement json) {
      if (json.isJsonArray()) {
         blockNames.clear();
         StreamSupport.stream(json.getAsJsonArray().spliterator(), true).filter(JsonElement::isJsonPrimitive).filter((e) -> {
            return e.getAsJsonPrimitive().isString();
         }).map((e) -> {
            return Block.func_149684_b(e.getAsString());
         }).filter(Objects::nonNull).map(SearchGuiSettings::getName).distinct().sorted().forEachOrdered((s) -> {
            blockNames.add(s);
         });
      }
   }

   public static JsonElement toJson() {
      JsonArray json = new JsonArray();
      blockNames.forEach((s) -> {
         json.add(new JsonPrimitive(s));
      });
      return json;
   }
}
