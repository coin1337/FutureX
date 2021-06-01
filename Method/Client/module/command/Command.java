package Method.Client.module.command;

import net.minecraft.client.Minecraft;

public abstract class Command {
   private final String command;
   protected static Minecraft mc = Minecraft.func_71410_x();

   public Command(String command) {
      this.command = command;
   }

   public abstract void runCommand(String var1, String[] var2);

   public abstract String getDescription();

   public abstract String getSyntax();

   public String getCommand() {
      return this.command;
   }
}
