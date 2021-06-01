package Method.Client.module.Onscreen;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.Frame;
import Method.Client.module.Category;
import Method.Client.module.Onscreen.Display.Angles;
import Method.Client.module.Onscreen.Display.Armor;
import Method.Client.module.Onscreen.Display.Biome;
import Method.Client.module.Onscreen.Display.Blockview;
import Method.Client.module.Onscreen.Display.ChunkSize;
import Method.Client.module.Onscreen.Display.CombatItems;
import Method.Client.module.Onscreen.Display.Coords;
import Method.Client.module.Onscreen.Display.Direction;
import Method.Client.module.Onscreen.Display.Durability;
import Method.Client.module.Onscreen.Display.EnabledMods;
import Method.Client.module.Onscreen.Display.Enemypos;
import Method.Client.module.Onscreen.Display.Fps;
import Method.Client.module.Onscreen.Display.Hole;
import Method.Client.module.Onscreen.Display.Hunger;
import Method.Client.module.Onscreen.Display.Inventory;
import Method.Client.module.Onscreen.Display.KeyStroke;
import Method.Client.module.Onscreen.Display.NetherCords;
import Method.Client.module.Onscreen.Display.Ping;
import Method.Client.module.Onscreen.Display.Player;
import Method.Client.module.Onscreen.Display.PlayerCount;
import Method.Client.module.Onscreen.Display.PlayerSpeed;
import Method.Client.module.Onscreen.Display.Potions;
import Method.Client.module.Onscreen.Display.Server;
import Method.Client.module.Onscreen.Display.ServerResponce;
import Method.Client.module.Onscreen.Display.Time;
import Method.Client.module.Onscreen.Display.Tps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public class OnscreenGUI extends GuiScreen {
   public static final ArrayList<PinableFrame> pinableFrames = new ArrayList();
   private final Frame Onscreen;
   protected Minecraft field_146297_k = Minecraft.func_71410_x();

   public OnscreenGUI() {
      this.Onscreen = new Frame(Category.ONSCREEN);
      this.Onscreen.setOpen(true);
      pinableFrames.add(new Angles.AnglesRUN());
      pinableFrames.add(new Player.PlayerRUN());
      pinableFrames.add(new EnabledMods.EnabledModsRUN());
      pinableFrames.add(new Armor.ArmorRUN());
      pinableFrames.add(new Biome.BiomeRUN());
      pinableFrames.add(new Blockview.BlockviewRUN());
      pinableFrames.add(new Durability.DurabilityRUN());
      pinableFrames.add(new Coords.CoordsRUN());
      pinableFrames.add(new Direction.DirectionRUN());
      pinableFrames.add(new Fps.FpsRUN());
      pinableFrames.add(new CombatItems.CombatItemsRUN());
      pinableFrames.add(new ChunkSize.ChunkSizeRUN());
      pinableFrames.add(new Inventory.InventoryRUN());
      pinableFrames.add(new NetherCords.NetherCordsRUN());
      pinableFrames.add(new Ping.PingRUN());
      pinableFrames.add(new Hole.HoleRUN());
      pinableFrames.add(new PlayerCount.PlayerCountRUN());
      pinableFrames.add(new Server.ServerRUN());
      pinableFrames.add(new PlayerSpeed.SpeedRUN());
      pinableFrames.add(new KeyStroke.KeyStrokeRUN());
      pinableFrames.add(new Time.TimeRUN());
      pinableFrames.add(new Tps.TpsRUN());
      pinableFrames.add(new Hunger.HungerRUN());
      pinableFrames.add(new Potions.PotionsRUN());
      pinableFrames.add(new Enemypos.EnemyposRUN());
      pinableFrames.add(new ServerResponce.ServerResponceRUN());
   }

   public void func_73876_c() {
      super.func_73876_c();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      if (this.Onscreen.isWithinBounds(mouseX, mouseY)) {
         this.Onscreen.handleScrollinput();
      }

      this.Onscreen.updatePosition(mouseX, mouseY);
      this.Onscreen.renderFrame();
      Iterator var4;
      if (this.Onscreen.isOpen()) {
         var4 = this.Onscreen.getComponents().iterator();

         while(var4.hasNext()) {
            Component comp = (Component)var4.next();
            comp.RenderTooltip();

            try {
               comp.updateComponent(mouseX, mouseY);
            } catch (IOException var7) {
               var7.printStackTrace();
            }
         }
      }

      var4 = pinableFrames.iterator();

      while(var4.hasNext()) {
         PinableFrame pinableFrame = (PinableFrame)var4.next();
         if (this.field_146297_k.field_71462_r instanceof OnscreenGUI) {
            pinableFrame.renderFrame();
            pinableFrame.Ongui();
         }

         pinableFrame.renderFrameText();
         pinableFrame.updatePosition(mouseX, mouseY);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      if (this.Onscreen.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
         this.Onscreen.setDrag(true);
         this.Onscreen.dragX = mouseX - this.Onscreen.getX();
         this.Onscreen.dragY = mouseY - this.Onscreen.getY();
      }

      if (this.Onscreen.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
         this.Onscreen.setOpen(!this.Onscreen.isOpen());
      }

      if (this.Onscreen.isOpen() && !this.Onscreen.getComponents().isEmpty() && this.Onscreen.isWithinBounds(mouseX, mouseY)) {
         Iterator var4 = this.Onscreen.getComponents().iterator();

         while(var4.hasNext()) {
            Component component = (Component)var4.next();
            component.mouseClicked(mouseX, mouseY, mouseButton);
         }
      }

      if (this.Onscreen.isWithinFooter(mouseX, mouseY) && mouseButton == 0) {
         this.Onscreen.dragScrollstop = mouseY - this.Onscreen.getScrollpos();
         this.Onscreen.setDragBot(true);
      }

      boolean multidrag = false;
      Iterator var8 = pinableFrames.iterator();

      while(var8.hasNext()) {
         PinableFrame pinableFrame = (PinableFrame)var8.next();
         if (pinableFrame.isWithinHeader(mouseX, mouseY) && mouseButton == 0 && !multidrag && pinableFrame.isPinned()) {
            pinableFrame.setDrag(true);
            pinableFrame.dragX = mouseX - pinableFrame.getX();
            pinableFrame.dragY = mouseY - pinableFrame.getY();
            multidrag = true;
         }
      }

   }

   protected void func_146286_b(int mouseX, int mouseY, int state) {
      this.Onscreen.setDrag(false);
      this.Onscreen.setDragBot(false);
      Iterator var4;
      if (this.Onscreen.isOpen() && !this.Onscreen.getComponents().isEmpty()) {
         var4 = this.Onscreen.getComponents().iterator();

         while(var4.hasNext()) {
            Component component = (Component)var4.next();
            component.mouseReleased(mouseX, mouseY, state);
         }
      }

      var4 = pinableFrames.iterator();

      while(var4.hasNext()) {
         PinableFrame pinableFrame = (PinableFrame)var4.next();
         pinableFrame.setDrag(false);
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) {
      if (this.Onscreen.isOpen() && keyCode != 1 && !this.Onscreen.getComponents().isEmpty()) {
         Iterator var3 = this.Onscreen.getComponents().iterator();

         while(var3.hasNext()) {
            Component component = (Component)var3.next();
            component.keyTyped(typedChar, keyCode);
         }
      }

      if (keyCode == 1) {
         this.field_146297_k.func_147108_a((GuiScreen)null);
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public static void onRenderGameOverlay(Text event) {
      Iterator var1 = pinableFrames.iterator();

      while(var1.hasNext()) {
         PinableFrame frame = (PinableFrame)var1.next();
         if (frame.isPinned()) {
            frame.onRenderGameOverlay(event);
         }
      }

   }
}
