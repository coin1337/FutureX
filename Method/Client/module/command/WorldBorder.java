package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;

public class WorldBorder extends Command {
   public WorldBorder() {
      super("WorldBorder");
   }

   public void runCommand(String s, String[] args) {
      try {
         net.minecraft.world.border.WorldBorder worldBorder = mc.field_71441_e.func_175723_af();
         ChatUtils.message("World border is at:\nMinX: " + worldBorder.func_177726_b() + "\nMinZ: " + worldBorder.func_177736_c() + "\nMaxX: " + worldBorder.func_177728_d() + "\nMaxZ: " + worldBorder.func_177733_e() + "\n");
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "WorldBorder distance";
   }

   public String getSyntax() {
      return "WorldBorder ";
   }
}
