package Method.Client.module.command;

import Method.Client.clickgui.ClickGui;
import Method.Client.clickgui.component.Frame;
import Method.Client.utils.visual.ChatUtils;
import java.util.Iterator;

public class ResetGui extends Command {
   public ResetGui() {
      super("ResetGui");
   }

   public void runCommand(String s, String[] args) {
      try {
         int xOffset = 5;

         Frame frame;
         for(Iterator var4 = ClickGui.frames.iterator(); var4.hasNext(); xOffset += frame.getWidth()) {
            frame = (Frame)var4.next();
            frame.setY(20);
            frame.setX(xOffset + 10);
         }

         ChatUtils.message("Guireset!");
      } catch (Exception var6) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "ResetGui";
   }

   public String getSyntax() {
      return "ResetGui";
   }
}
