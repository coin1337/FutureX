package Method.Client.clickgui;

import Method.Client.Main;
import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.Frame;
import Method.Client.clickgui.component.components.Button;
import Method.Client.clickgui.component.components.sub.Keybind;
import Method.Client.managers.CommandManager;
import Method.Client.managers.FileManager;
import Method.Client.module.Category;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.module.command.Command;
import Method.Client.utils.visual.ColorUtils;
import Method.Client.utils.visual.RenderUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;

public class ClickGui extends GuiScreen {
   public static ArrayList<Frame> frames = new ArrayList();
   private GuiTextField textbox;
   boolean nomultidrag = false;
   boolean loaded;
   boolean Trycommand = false;

   public ClickGui() {
      int frameX = 5;
      Category[] var2 = Category.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Category category = var2[var4];
         Frame frame = new Frame(category);
         frame.setX(frameX);
         frames.add(frame);
         frameX += frame.getWidth() + 1;
      }

      FileManager.LoadMods();
      this.loaded = true;
      Iterator var7 = OnscreenGUI.pinableFrames.iterator();

      while(var7.hasNext()) {
         PinableFrame me = (PinableFrame)var7.next();
         me.setup();
      }

   }

   public void func_73866_w_() {
      this.textbox = new GuiTextField(0, this.field_146289_q, (int)((double)this.field_146297_k.field_71443_c / 5.4D), 1, 240, this.field_146297_k.field_71443_c / 100);
      this.textbox.func_146195_b(true);
      this.textbox.func_146203_f(999);
      this.textbox.func_146185_a(false);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      GlStateManager.func_179152_a(1.0F, 1.0F, 0.5F);
      String parse = this.textbox.func_146179_b();
      this.textbox.func_146194_f();
      Frame.updateFont();
      Button.updateFont();
      Iterator var5 = frames.iterator();

      while(var5.hasNext()) {
         Frame frame = (Frame)var5.next();
         if (frame.isWithinBounds(mouseX, mouseY)) {
            frame.handleScrollinput();
         }

         frame.updatePosition(mouseX, mouseY);
         frame.renderFrame();
         if (frame.isOpen()) {
            Iterator var7 = frame.getComponents().iterator();

            while(var7.hasNext()) {
               Component comp = (Component)var7.next();
               comp.RenderTooltip();
               if (comp.getName().toLowerCase().contains(parse.toLowerCase()) && !parse.isEmpty()) {
                  RenderUtils.drawRectOutline((double)frame.getX(), (double)comp.gety(), (double)(frame.getX() + 88), (double)(comp.gety() + 12), 1.0D, ColorUtils.rainbow().getRGB());
               }

               try {
                  comp.updateComponent(mouseX, mouseY);
               } catch (IOException var10) {
                  var10.printStackTrace();
               }
            }
         }
      }

      this.func_73733_a((int)((double)this.field_146297_k.field_71443_c / 5.4D), 0, (int)((double)this.field_146297_k.field_71443_c / 5.4D + 240.0D), 14, 865704345, 865704345);
      if (!parse.isEmpty()) {
         int add = 0;
         Iterator var12 = CommandManager.commands.iterator();

         while(var12.hasNext()) {
            Command c = (Command)var12.next();
            if (c.getCommand().toLowerCase().startsWith(parse.toLowerCase())) {
               Component.FontRend.func_175063_a(c.getSyntax(), (float)((double)this.field_146297_k.field_71443_c / 5.4D), (float)(add + 10), -1);
               add += 10;
            }
         }
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      if (this.nomultidrag) {
         Collections.reverse(frames);
         this.nomultidrag = false;
      }

      Iterator var4 = frames.iterator();

      while(var4.hasNext()) {
         Frame frame = (Frame)var4.next();
         if (frame.isOpen() && !frame.getComponents().isEmpty() && frame.isWithinBounds(mouseX, mouseY)) {
            Iterator var6 = frame.getComponents().iterator();

            while(var6.hasNext()) {
               Component component = (Component)var6.next();
               component.mouseClicked(mouseX, mouseY, mouseButton);
            }
         }

         if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0 && !this.nomultidrag) {
            frame.setDrag(true);
            frame.dragX = mouseX - frame.getX();
            frame.dragY = mouseY - frame.getY();
            this.nomultidrag = true;
         }

         if (frame.isWithinFooter(mouseX, mouseY) && mouseButton == 0) {
            frame.dragScrollstop = mouseY - frame.getScrollpos();
            frame.setDragBot(true);
         }

         if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
            if (frame.getName().equalsIgnoreCase("Onscreen")) {
               this.field_146297_k.func_147108_a(Main.OnscreenGUI);
            } else {
               frame.setOpen(!frame.isOpen());
            }
         }
      }

      if (mouseButton == 0 && (mouseY >= 14 || !((double)mouseX < (double)this.field_146297_k.field_71443_c / 5.4D + 240.0D) || !((double)mouseX > (double)this.field_146297_k.field_71443_c / 5.4D - 5.0D))) {
         this.textbox.func_146180_a("");
         this.Trycommand = false;
      }

      if (mouseY < 14 && mouseButton == 0 && (double)mouseX < (double)this.field_146297_k.field_71443_c / 5.4D + 240.0D && (double)mouseX > (double)this.field_146297_k.field_71443_c / 5.4D - 5.0D) {
         this.Trycommand = true;
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) {
      if (this.loaded) {
         FileManager.SaveMods();
         FileManager.saveframes();
         FileManager.savePROFILES();
      }

      if (keyCode == 1) {
         this.field_146297_k.func_147108_a((GuiScreen)null);
      }

      if (!Keybind.PublicBinding) {
         this.textbox.func_146201_a(typedChar, keyCode);
      }

      Iterator var3 = frames.iterator();

      while(true) {
         Frame frame;
         do {
            do {
               do {
                  if (!var3.hasNext()) {
                     if (typedChar == 15) {
                        label82: {
                           var3 = CommandManager.commands.iterator();

                           Command c;
                           String parse;
                           do {
                              do {
                                 if (!var3.hasNext()) {
                                    break label82;
                                 }

                                 c = (Command)var3.next();
                                 parse = this.textbox.func_146179_b();
                              } while(parse.length() <= 0);
                           } while(!c.getCommand().toLowerCase().startsWith(parse.toLowerCase().substring(0, parse.indexOf(32))) && !parse.substring(0, parse.indexOf(32)).toLowerCase().startsWith(c.getCommand()));

                           this.textbox.func_146180_a(c.getCommand());
                        }
                     }

                     if (this.textbox.func_146206_l() && keyCode == 28 && this.Trycommand) {
                        CommandManager.getInstance().runCommands(CommandManager.cmdPrefix + this.textbox.func_146179_b());
                        this.field_146297_k.func_147108_a((GuiScreen)null);
                     }

                     return;
                  }

                  frame = (Frame)var3.next();
               } while(!frame.isOpen());
            } while(keyCode == 1);
         } while(frame.getComponents().isEmpty());

         Iterator var5 = frame.getComponents().iterator();

         while(var5.hasNext()) {
            Component component = (Component)var5.next();
            component.keyTyped(typedChar, keyCode);
         }
      }
   }

   public void func_73876_c() {
      this.textbox.func_146178_a();
      super.func_73876_c();
   }

   protected void func_146286_b(int mouseX, int mouseY, int state) {
      Iterator var4 = frames.iterator();

      while(true) {
         Frame frame;
         do {
            do {
               if (!var4.hasNext()) {
                  return;
               }

               frame = (Frame)var4.next();
               frame.setDrag(false);
               frame.setDragBot(false);
            } while(!frame.isOpen());
         } while(frame.getComponents().isEmpty());

         Iterator var6 = frame.getComponents().iterator();

         while(var6.hasNext()) {
            Component component = (Component)var6.next();
            component.mouseReleased(mouseX, mouseY, state);
         }
      }
   }

   public boolean func_73868_f() {
      return false;
   }
}
