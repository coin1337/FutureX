package Method.Client.utils.system;

import Method.Client.utils.visual.ChatUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.GameType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.world.ChunkEvent.Load;

public class WorldDownloader {
   public static AnvilChunkLoader newdownload;
   public static boolean Saving = false;
   public static File SaveDir;
   public static String Savename;
   public static ArrayList<Chunk> chunks = new ArrayList();

   public static void ChunkeventLOAD(Load event) {
      if (Saving) {
         chunks.add(event.getChunk());
      }

   }

   public static ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata, File file) {
      return new AnvilSaveHandler(file, saveName, storePlayerdata, new DataFixer(Wrapper.mc.func_184126_aj().field_188262_d));
   }

   public static void Clienttick() {
      if (Saving) {
         ArrayList<Chunk> remove = new ArrayList();
         Iterator var1 = chunks.iterator();

         while(var1.hasNext()) {
            Chunk option = (Chunk)var1.next();
            if (Wrapper.mc.field_71441_e.func_190526_b(option.field_76635_g, option.field_76647_h)) {
               try {
                  newdownload.func_75816_a(Wrapper.INSTANCE.world(), Wrapper.mc.field_71441_e.func_72964_e(option.field_76635_g, option.field_76647_h));
               } catch (IOException | MinecraftException var4) {
                  var4.printStackTrace();
               }
            }

            if (newdownload.func_191063_a(option.field_76635_g, option.field_76647_h)) {
               remove.add(option);
            }
         }

         chunks.removeAll(remove);
      }

   }

   public static void start() {
      ChatUtils.message("Starting world download");
      Savename = "Download Time" + (int)System.currentTimeMillis() / 1000;
      SaveDir = new File((new File(Wrapper.INSTANCE.mc().field_71412_D, "saves")).getAbsolutePath(), Savename);
      if (!SaveDir.exists()) {
         SaveDir.mkdir();
         newdownload = new AnvilChunkLoader(new File(SaveDir.getAbsolutePath()), Wrapper.mc.func_184126_aj());
         Saving = true;
         chunks.addAll(Wrapper.mc.field_71441_e.func_72863_F().field_73236_b.values());
         ChatUtils.message(".minecraft/saves/" + Savename);
      } else {
         ChatUtils.error("This file already Exists?");
         ChatUtils.message(SaveDir.getPath());
      }
   }

   protected static NBTTagList newDoubleNBTList(double... numbers) {
      NBTTagList nbttaglist = new NBTTagList();
      double[] var2 = numbers;
      int var3 = numbers.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         double d0 = var2[var4];
         nbttaglist.func_74742_a(new NBTTagDouble(d0));
      }

      return nbttaglist;
   }

   public static void stop() {
      ChatUtils.message("Stopped world download");
      newdownload.func_75818_b();
      Saving = false;
      ISaveHandler isavehandler = getSaveLoader(Savename, true, new File(Wrapper.INSTANCE.mc().field_71412_D, "saves"));
      WorldInfo info = new WorldInfo(Wrapper.mc.field_71441_e.func_72912_H());
      NBTTagCompound player = new NBTTagCompound();
      Wrapper.mc.field_71439_g.func_189511_e(player);
      info.func_176143_a(Wrapper.mc.field_71439_g.func_180425_c());
      info.field_76108_i = player;

      try {
         player.func_74782_a("Pos", newDoubleNBTList(Wrapper.mc.field_71439_g.field_70165_t, Wrapper.mc.field_71439_g.field_70163_u, Wrapper.mc.field_71439_g.field_70161_v));
      } catch (Exception var4) {
      }

      info.func_176121_c(true);
      info.func_76060_a(GameType.CREATIVE);
      info.field_82576_c = "3;minecraft:air;127;decoration";
      info.func_76085_a(WorldType.field_77138_c);
      isavehandler.func_75755_a(info, player);
      if (newdownload.getPendingSaveCount() > 1) {
         ChatUtils.warning("There are still: " + newdownload.getPendingSaveCount() + " Chunks Pending to be saved");
      }

      ChatUtils.message("Finished Download!");
   }
}
