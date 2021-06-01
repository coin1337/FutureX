package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.TimerUtils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.Executors;
import java.util.zip.DeflaterOutputStream;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public final class ChunkSize extends Module {
   static Setting Delay;
   static Setting TextColor;
   static Setting BgColor;
   static Setting xpos;
   static Setting ypos;
   static Setting Shadow;
   static Setting Frame;
   static Setting FontSize;
   static Setting Background;
   public static final TimerUtils timer = new TimerUtils();
   public static boolean running = false;
   public static long size = 0L;
   public static long previousSize = 0L;
   public static ChunkPos current = null;
   AnvilChunkLoader loader;
   NBTTagCompound root = null;
   NBTTagCompound level;
   DataOutputStream compressed;

   public ChunkSize() {
      super("ChunkSize", 0, Category.ONSCREEN, "ChunkSize");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(Delay = new Setting("Delay", this, 1.0D, 1.0D, 10.0D, true));
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(Background = new Setting("Background", this, false));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 250.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("ChunkSizeSET", true);
      timer.reset();
      this.loader = new AnvilChunkLoader((File)null, (DataFixer)null);
      running = false;
      previousSize = 0L;
      size = 0L;
      current = null;
      this.root = new NBTTagCompound();
      this.level = new NBTTagCompound();
      this.root.func_74782_a("Level", this.level);
      this.root.func_74768_a("DataVersion", 2021);
   }

   public void onDisable() {
      PinableFrame.Toggle("ChunkSizeSET", false);
   }

   public void onClientTick(ClientTickEvent event) {
      if (!running) {
         if (event.phase == Phase.END) {
            Chunk chunk = mc.field_71441_e.func_175726_f(mc.field_71439_g.func_180425_c());
            if (chunk.func_76621_g()) {
               return;
            }

            ChunkPos pos = chunk.func_76632_l();
            if (!pos.equals(current) || timer.isDelay(1000L)) {
               if (current != null && !pos.equals(current)) {
                  previousSize = 0L;
                  size = 0L;
               }

               current = pos;
               running = true;
               Executors.newSingleThreadExecutor().execute(() -> {
                  try {
                     try {
                        this.loader.func_75820_a(chunk, mc.field_71441_e, this.level);
                     } catch (Throwable var8) {
                        size = -1L;
                        previousSize = 0L;
                        return;
                     }

                     this.compressed = new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(new ByteArrayOutputStream(8096))));

                     try {
                        CompressedStreamTools.func_74800_a(this.root, this.compressed);
                        previousSize = size;
                        size = (long)this.compressed.size();
                     } catch (IOException var7) {
                        size = -1L;
                        previousSize = 0L;
                     }

                  } finally {
                     timer.setLastMS();
                     running = false;
                  }
               });
            }
         }

      }
   }

   public static class ChunkSizeRUN extends PinableFrame {
      public ChunkSizeRUN() {
         super("ChunkSizeSET", new String[0], (int)ChunkSize.ypos.getValDouble(), (int)ChunkSize.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, ChunkSize.xpos, ChunkSize.ypos, ChunkSize.Frame, ChunkSize.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, ChunkSize.xpos, ChunkSize.ypos, ChunkSize.Frame, ChunkSize.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         String Size = " " + String.format("[%s | %s]", ChunkSize.size == -1L ? "<error>" : toFormattedBytes(ChunkSize.size), difference(ChunkSize.size - ChunkSize.previousSize));
         if (ChunkSize.Background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(ChunkSize.Frame, Size), this.y + 20, ChunkSize.BgColor.getcolor());
         }

         this.fontSelect(ChunkSize.Frame, Size, (float)this.getX(), (float)(this.getY() + 10), ChunkSize.TextColor.getcolor(), ChunkSize.Shadow.getValBoolean());
         super.onRenderGameOverlay(event);
      }

      private static String toFormattedBytes(long size) {
         NumberFormat format = NumberFormat.getInstance();
         format.setGroupingUsed(true);
         if (size < 1000L) {
            return format.format(size) + " B";
         } else {
            return size < 1000000L ? format.format((double)size / 1000.0D) + " KB" : format.format((double)size / 1000000.0D) + " MB";
         }
      }

      private static String difference(long size) {
         if (size == 0L) {
            return "+0 B";
         } else {
            return size > 0L ? "+" + toFormattedBytes(size) : "-" + toFormattedBytes(Math.abs(size));
         }
      }
   }
}
