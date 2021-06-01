package Method.Client.module.command;

import Method.Client.Main;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;

public class OpenGui extends Command {
   Thread t;

   public OpenGui() {
      super("Gui");
   }

   public void runCommand(String s, String[] args) {
      try {
         mc.func_147108_a((GuiScreen)null);
         Thread t = new Thread(new OpenGui.ThreadDemo());
         t.start();
         mc.field_71417_B.func_74373_b();
         ChatUtils.message(TextFormatting.GOLD + "Tried to open Gui");
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "Opens gui";
   }

   public String getSyntax() {
      return "gui";
   }

   private static class ThreadDemo implements Runnable {
      private ThreadDemo() {
      }

      public void run() {
         try {
            Thread.sleep(25L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }

         Command.mc.func_147108_a(Main.ClickGui);
      }

      // $FF: synthetic method
      ThreadDemo(Object x0) {
         this();
      }
   }
}
