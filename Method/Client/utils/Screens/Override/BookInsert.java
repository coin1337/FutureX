package Method.Client.utils.Screens.Override;

import Method.Client.utils.Screens.Screen;
import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;

public class BookInsert extends Screen {
   public void GuiScreenEventInit(Post event) {
      if (event.getGui() instanceof GuiScreenBook) {
         event.getButtonList().add(new GuiButton(3334, event.getGui().field_146294_l / 2 + 80, 50, 50, 20, "Fillmax"));
         event.getButtonList().add(new GuiButton(3335, event.getGui().field_146294_l / 2 + 80, 70, 50, 20, "AutoSign"));
         event.getButtonList().add(new GuiButton(3336, event.getGui().field_146294_l / 2 + 80, 90, 50, 20, "Copy"));
         event.getButtonList().add(new GuiButton(3337, event.getGui().field_146294_l / 2 + 80, 110, 50, 20, "Fillbook"));
         event.getButtonList().add(new GuiButton(3338, event.getGui().field_146294_l / 2 + 80, 130, 50, 20, "Clear"));
         event.getButtonList().add(new GuiButton(3339, event.getGui().field_146294_l / 2 + 80, 150, 50, 20, "Author"));

         for(int i = 0; i < 22; ++i) {
            event.getButtonList().add(new GuiButton(i + 11, event.getGui().field_146294_l / 2 + 130 + i % 5 * 15, 105 - i / 5 * 15, 15, 15, this.ColorfromInt(i) + "&A"));
         }
      }

   }

   public void GuiScreenEventPost(net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post event) {
      if (event.getGui() instanceof GuiScreenBook) {
         GuiScreenBook Gui = (GuiScreenBook)event.getGui();
         switch(event.getButton().field_146127_k) {
         case 27:
            this.checkandinstert(Gui, event.getButton().field_146127_k, TextFormatting.OBFUSCATED);
            break;
         case 28:
            this.checkandinstert(Gui, event.getButton().field_146127_k, TextFormatting.BOLD);
            break;
         case 29:
            this.checkandinstert(Gui, event.getButton().field_146127_k, TextFormatting.STRIKETHROUGH);
            break;
         case 30:
            this.checkandinstert(Gui, event.getButton().field_146127_k, TextFormatting.UNDERLINE);
            break;
         case 31:
            this.checkandinstert(Gui, event.getButton().field_146127_k, TextFormatting.ITALIC);
            break;
         case 32:
            this.checkandinstert(Gui, event.getButton().field_146127_k, TextFormatting.RESET);
            break;
         case 3334:
            this.RandomFill(Gui, Gui.field_146484_x);
            break;
         case 3335:
            Gui.field_146480_s = true;
            Gui.field_146482_z = mc.field_71439_g.func_70005_c_();

            try {
               Gui.func_146462_a(true);
            } catch (IOException var7) {
               var7.printStackTrace();
            }

            mc.func_147108_a((GuiScreen)null);
            break;
         case 3336:
            String s = this.pageGetCurrent(Gui);
            this.NewPage(Gui);
            this.pageSetCurrent(Gui, s);
            break;
         case 3337:
            int G;
            for(G = 0; G < Gui.field_146476_w; ++G) {
               this.NewPage(Gui);
            }

            for(G = 0; G < Gui.field_146476_w; ++G) {
               this.RandomFill(Gui, G);
            }

            return;
         case 3338:
            this.pageSetCurrent(Gui, "");
            break;
         case 3339:
            mc.func_147108_a((GuiScreen)null);
            mc.func_147108_a(new Authorinsert());
            break;
         default:
            if (event.getButton().field_146127_k < 27 && event.getButton().field_146127_k > 10) {
               String getCurrent = this.pageGetCurrent(Gui);
               String s1 = getCurrent + TextFormatting.func_175744_a(event.getButton().field_146127_k - 11);
               int wordWrappedHeight = Gui.field_146289_q.func_78267_b(s1 + "" + TextFormatting.BLACK + "_", 118);
               if (wordWrappedHeight <= 128 && s1.length() < 256) {
                  this.pageSetCurrent(Gui, s1);
               }
            }
         }
      }

   }

   private void RandomFill(GuiScreenBook gui, int loc) {
      (new Thread(() -> {
         String collect = this.randomget().substring(0, 254);
         gui.field_146483_y.func_150304_a(loc, new NBTTagString(collect));
         gui.field_146481_r = true;
      })).start();
   }

   public String randomget() {
      IntStream gen = (new Random()).ints(128, 1112063).map((i) -> {
         return i < 55296 ? i : i + 2048;
      });
      return (String)gen.limit(1536L).mapToObj((i) -> {
         return String.valueOf((char)i);
      }).collect(Collectors.joining());
   }

   private void NewPage(GuiScreenBook gui) {
      if (gui.field_146475_i) {
         if (gui.field_146483_y != null && gui.field_146483_y.func_74745_c() < 50) {
            gui.field_146483_y.func_74742_a(new NBTTagString(""));
            ++gui.field_146476_w;
            gui.field_146481_r = true;
         }

         if (gui.field_146484_x < gui.field_146476_w - 1) {
            ++gui.field_146484_x;
         }
      }

   }

   void checkandinstert(GuiScreenBook gui, int id, TextFormatting d) {
      String s = this.pageGetCurrent(gui);
      String s1 = s + d;
      int i = gui.field_146289_q.func_78267_b(s1 + "" + TextFormatting.BLACK + "_", 118);
      if (i <= 128 && s1.length() < 256) {
         this.pageSetCurrent(gui, s1);
      }

   }

   public String pageGetCurrent(GuiScreenBook gui) {
      return gui.field_146483_y != null && gui.field_146484_x >= 0 && gui.field_146484_x < gui.field_146483_y.func_74745_c() ? gui.field_146483_y.func_150307_f(gui.field_146484_x) : "";
   }

   private void pageSetCurrent(GuiScreenBook gui, String s) {
      if (gui.field_146483_y != null && gui.field_146484_x >= 0 && gui.field_146484_x < gui.field_146483_y.func_74745_c()) {
         gui.field_146483_y.func_150304_a(gui.field_146484_x, new NBTTagString(s));
         gui.field_146481_r = true;
      }

   }
}
