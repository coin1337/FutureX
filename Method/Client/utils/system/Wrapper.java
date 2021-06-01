package Method.Client.utils.system;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumHand;

public class Wrapper {
   public static FontRenderer fr;
   public static volatile Wrapper INSTANCE;
   public static Minecraft mc;

   public Minecraft mc() {
      return Minecraft.func_71410_x();
   }

   public EntityPlayerSP player() {
      return INSTANCE.mc().field_71439_g;
   }

   public WorldClient world() {
      return INSTANCE.mc().field_71441_e;
   }

   public GameSettings mcSettings() {
      return INSTANCE.mc().field_71474_y;
   }

   public FontRenderer fontRenderer() {
      return INSTANCE.mc().field_71466_p;
   }

   public void sendPacket(Packet packet) {
      this.player().field_71174_a.func_147297_a(packet);
   }

   public PlayerControllerMP controller() {
      return INSTANCE.mc().field_71442_b;
   }

   public void swingArm() {
      this.player().func_184609_a(EnumHand.MAIN_HAND);
   }

   public void attack(Entity entity) {
      this.controller().func_78764_a(this.player(), entity);
   }

   public void copy(String content) {
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), (ClipboardOwner)null);
   }

   static {
      fr = Minecraft.func_71410_x().field_71466_p;
      INSTANCE = new Wrapper();
      mc = Minecraft.func_71410_x();
   }
}
