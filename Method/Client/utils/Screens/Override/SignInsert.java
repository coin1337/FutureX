package Method.Client.utils.Screens.Override;

import Method.Client.utils.Screens.Screen;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiOpenEvent;
import org.lwjgl.input.Keyboard;

public class SignInsert extends Screen {
   public void GuiOpen(GuiOpenEvent event) {
      if (event.getGui() instanceof GuiEditSign) {
         event.setGui(new SignInsert.BetterSignGui(((GuiEditSign)event.getGui()).field_146848_f));
      }

   }

   class BetterSignGui extends GuiScreen {
      private int focusedField = 0;
      public final TileEntitySign sign;
      private List<GuiTextField> textFields;
      private String[] defaultStrings;

      public BetterSignGui(TileEntitySign sign) {
         this.sign = sign;
      }

      public void func_73866_w_() {
         this.field_146292_n.clear();
         Keyboard.enableRepeatEvents(true);
         this.textFields = new LinkedList();
         this.defaultStrings = new String[4];

         int i;
         for(i = 0; i < 4; ++i) {
            GuiTextField field = new GuiTextField(i, this.field_146289_q, this.field_146294_l / 2 + 4, 75 + i * 24, 120, 20);
            field.func_175205_a(this::validateText);
            field.func_146203_f(100);
            String text = this.sign.field_145915_a[i].func_150260_c();
            this.defaultStrings[i] = text;
            field.func_146180_a(text);
            this.textFields.add(field);
         }

         ((GuiTextField)this.textFields.get(this.focusedField)).func_146195_b(true);
         this.func_189646_b(new GuiButton(4, this.field_146294_l / 2 + 5, this.field_146295_m / 4 + 120, 120, 20, "Done"));
         this.func_189646_b(new GuiButton(5, this.field_146294_l / 2 - 125, this.field_146295_m / 4 + 120, 120, 20, "Cancel"));
         this.func_189646_b(new GuiButton(6, this.field_146294_l / 2 - 41, 147, 40, 20, "Shift"));
         this.func_189646_b(new GuiButton(7, this.field_146294_l / 2 - 41, 123, 40, 20, "Clear"));
         this.func_189646_b(new GuiButton(8, this.field_146294_l / 2 + 130, 124, 40, 20, "Lag"));
         this.func_189646_b(new GuiButton(9, this.field_146294_l / 2 + 130, 99, 40, 20, "FillMax"));

         for(i = 0; i < 22; ++i) {
            this.func_189646_b(new GuiButton(i + 11, this.field_146294_l / 2 + 130 + i % 5 * 15, 215 - i / 5 * 15, 15, 15, SignInsert.this.ColorfromInt(i) + "&A"));
         }

         this.sign.func_145913_a(false);
      }

      protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
         super.func_73864_a(mouseX, mouseY, mouseButton);
         this.textFields.forEach((field) -> {
            field.func_146192_a(mouseX, mouseY, mouseButton);
            if (field.func_146206_l()) {
               this.focusedField = field.func_175206_d();
            }

         });
         if (!((GuiTextField)this.textFields.get(this.focusedField)).func_146206_l()) {
            ((GuiTextField)this.textFields.get(this.focusedField)).func_146195_b(true);
         }

      }

      protected void func_73869_a(char typedChar, int keyCode) {
         switch(keyCode) {
         case 1:
            this.exit();
            break;
         case 15:
            int change = func_146272_n() ? -1 : 1;
            this.tabFocus(change);
            break;
         case 28:
         case 156:
         case 208:
            this.tabFocus(1);
         default:
            this.textFields.forEach((field) -> {
               field.func_146201_a(typedChar, keyCode);
            });
            this.sign.field_145915_a[this.focusedField] = new TextComponentString(((GuiTextField)this.textFields.get(this.focusedField)).func_146179_b());
            break;
         case 200:
            this.tabFocus(-1);
         }

      }

      protected void func_146284_a(GuiButton button) throws IOException {
         super.func_146284_a(button);
         int change;
         int i;
         switch(button.field_146127_k) {
         case 5:
            for(int ixx = 0; ixx < 4; ++ixx) {
               this.sign.field_145915_a[ixx] = new TextComponentString(this.defaultStrings[ixx]);
            }
         case 4:
            this.exit();
            break;
         case 6:
            String[] replacements = new String[4];

            for(int ixxx = 0; ixxx < 4; ++ixxx) {
               change = func_146272_n() ? 1 : -1;
               i = this.wrapLine(ixxx + change);
               replacements[ixxx] = this.sign.field_145915_a[i].func_150260_c();
            }

            this.applytext(replacements);
            break;
         case 7:
            this.textFields.forEach((field) -> {
               int id = field.func_175206_d();
               field.func_146180_a("");
               this.sign.field_145915_a[id] = new TextComponentString("");
            });
            break;
         case 8:
            StringBuilder lagStringBuilder = new StringBuilder();

            for(change = 0; change < 500; ++change) {
               lagStringBuilder.append("/(!Â§()%/Â§)=/(!Â§()%/Â§)=/(!Â§()%/Â§)=");
            }

            String[] Builder = new String[4];

            for(i = 0; i < 4; ++i) {
               Builder[i] = lagStringBuilder.toString();
            }

            this.applytext(Builder);
            break;
         case 9:
            String line = this.Random();
            String[] rando = new String[4];

            for(int ix = 0; ix < 4; ++ix) {
               rando[ix] = line.substring(ix * 384, (ix + 1) * 384);
            }

            this.applytext(rando);
            break;
         default:
            StringBuilder var10000;
            GuiTextField var10002;
            if (button.field_146127_k < 27) {
               var10000 = new StringBuilder();
               var10002 = (GuiTextField)this.textFields.get(this.focusedField);
               var10002.field_146216_j = var10000.append(var10002.field_146216_j).append("&").append(Integer.toHexString(button.field_146127_k - 11)).toString();
            }

            if (button.field_146127_k == 27) {
               var10000 = new StringBuilder();
               var10002 = (GuiTextField)this.textFields.get(this.focusedField);
               var10002.field_146216_j = var10000.append(var10002.field_146216_j).append("&k").toString();
            }

            if (button.field_146127_k == 28) {
               var10000 = new StringBuilder();
               var10002 = (GuiTextField)this.textFields.get(this.focusedField);
               var10002.field_146216_j = var10000.append(var10002.field_146216_j).append("&l").toString();
            }

            if (button.field_146127_k == 29) {
               var10000 = new StringBuilder();
               var10002 = (GuiTextField)this.textFields.get(this.focusedField);
               var10002.field_146216_j = var10000.append(var10002.field_146216_j).append("&m").toString();
            }

            if (button.field_146127_k == 30) {
               var10000 = new StringBuilder();
               var10002 = (GuiTextField)this.textFields.get(this.focusedField);
               var10002.field_146216_j = var10000.append(var10002.field_146216_j).append("&n").toString();
            }

            if (button.field_146127_k == 31) {
               var10000 = new StringBuilder();
               var10002 = (GuiTextField)this.textFields.get(this.focusedField);
               var10002.field_146216_j = var10000.append(var10002.field_146216_j).append("&o").toString();
            }

            if (button.field_146127_k == 32) {
               var10000 = new StringBuilder();
               var10002 = (GuiTextField)this.textFields.get(this.focusedField);
               var10002.field_146216_j = var10000.append(var10002.field_146216_j).append("&r").toString();
            }
         }

      }

      void applytext(String[] index) {
         this.textFields.forEach((field) -> {
            int id = field.func_175206_d();
            field.func_146180_a(index[id]);
            this.sign.field_145915_a[id] = new TextComponentString(index[id]);
         });
      }

      public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
         this.func_146276_q_();
         this.func_73732_a(this.field_146289_q, I18n.func_135052_a("sign.edit", new Object[0]), this.field_146294_l / 2, 40, 16777215);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b((float)(this.field_146294_l / 2) - 63.0F, 0.0F, 50.0F);
         GlStateManager.func_179152_a(-93.75F, -93.75F, -93.75F);
         GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
         Block block = this.sign.func_145838_q();
         if (block == Blocks.field_150472_an) {
            float f1 = (float)(this.sign.func_145832_p() * 360) / 16.0F;
            GlStateManager.func_179114_b(f1, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, -1.0625F, 0.0F);
         } else {
            int i = this.sign.func_145832_p();
            float f2 = i == 2 ? 180.0F : (i == 4 ? 90.0F : (i == 5 ? -90.0F : 0.0F));
            GlStateManager.func_179114_b(f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, -0.7625F, 0.0F);
         }

         this.sign.field_145918_i = -1;
         TileEntityRendererDispatcher.field_147556_a.func_147549_a(this.sign, -0.5D, -0.75D, -0.5D, 0.0F);
         GlStateManager.func_179121_F();
         this.textFields.forEach(GuiTextField::func_146194_f);
         super.func_73863_a(mouseX, mouseY, partialTicks);
      }

      void exit() {
         this.sign.func_70296_d();
         this.field_146297_k.func_147108_a((GuiScreen)null);
      }

      private String Random() {
         IntStream gen = (new Random()).ints(128, 1112063).map((i) -> {
            return i < 55296 ? i : i + 2048;
         });
         return (String)gen.limit(1536L).mapToObj((i) -> {
            return String.valueOf((char)i);
         }).collect(Collectors.joining());
      }

      public void func_146281_b() {
         String[] color = new String[4];

         for(int i = 0; i < 4; ++i) {
            color[i] = this.sign.field_145915_a[i].func_150260_c().replace("&", "§§a");
         }

         this.applytext(color);
         Keyboard.enableRepeatEvents(false);
         NetHandlerPlayClient nethandlerplayclient = this.field_146297_k.func_147114_u();
         if (nethandlerplayclient != null) {
            nethandlerplayclient.func_147297_a(new CPacketUpdateSign(this.sign.func_174877_v(), this.sign.field_145915_a));
         }

         this.sign.func_145913_a(true);
      }

      void tabFocus(int change) {
         ((GuiTextField)this.textFields.get(this.focusedField)).func_146195_b(false);
         this.focusedField = this.wrapLine(this.focusedField + change);
         ((GuiTextField)this.textFields.get(this.focusedField)).func_146195_b(true);
      }

      int wrapLine(int line) {
         return line > 3 ? 0 : (line < 0 ? 3 : line);
      }

      boolean validateText(String s) {
         if (this.field_146289_q.func_78256_a(s) > 90) {
            return false;
         } else {
            char[] var2 = s.toCharArray();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               char c = var2[var4];
               if (!ChatAllowedCharacters.func_71566_a(c)) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
