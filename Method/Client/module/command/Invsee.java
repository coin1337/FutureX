package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;

public class Invsee extends Command {
   public Invsee() {
      super("Invsee");
   }

   public void runCommand(String s, String[] args) {
      try {
         if (mc.field_71439_g.field_71075_bZ.field_75098_d) {
            ChatUtils.error("Must Be Creative");
            return;
         }

         String id = args[0];
         Iterator var4 = mc.field_71441_e.field_73010_i.iterator();

         while(var4.hasNext()) {
            EntityPlayer entityPlayer = (EntityPlayer)var4.next();
            if (entityPlayer.getDisplayNameString().equalsIgnoreCase(id)) {
               mc.func_147108_a(new GuiInventory(entityPlayer));
               return;
            }
         }

         ChatUtils.error("Could not find player " + id);
      } catch (Exception var6) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "See inv of other players";
   }

   public String getSyntax() {
      return "Invsee <Player>";
   }
}
