package Method.Client.utils.Screens;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Screen {
   protected static Minecraft mc = Minecraft.func_71410_x();
   public boolean visible = true;

   public TextFormatting ColorfromInt(int i) {
      if (i < 16) {
         return TextFormatting.func_175744_a(i);
      } else {
         switch(i) {
         case 16:
            return TextFormatting.OBFUSCATED;
         case 17:
            return TextFormatting.BOLD;
         case 18:
            return TextFormatting.STRIKETHROUGH;
         case 19:
            return TextFormatting.UNDERLINE;
         case 20:
            return TextFormatting.ITALIC;
         case 21:
            return TextFormatting.RESET;
         default:
            return null;
         }
      }
   }

   public void setup() {
   }

   public void GuiOpen(GuiOpenEvent event) {
   }

   public void GuiScreenEventPost(Post event) {
   }

   public void GuiScreenEventInit(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post event) {
   }

   public void GuiScreenEventPre(Pre event) {
   }

   public void onClientTick(ClientTickEvent event) {
   }

   public void DrawScreenEvent(DrawScreenEvent event) {
   }

   public void onWorldUnload(Unload event) {
   }
}
