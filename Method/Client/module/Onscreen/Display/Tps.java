package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.system.Connection;
import java.util.Arrays;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Tps extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting background;
   static Setting xpos;
   static Setting ypos;
   static Setting Shadow;
   static Setting Frame;
   static Setting FontSize;
   private static final float[] tickRates = new float[20];
   private int nextIndex = 0;
   private long timeLastTimeUpdate;

   public Tps() {
      super("Tps", 0, Category.ONSCREEN, "Tps");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
      this.nextIndex = 0;
      this.timeLastTimeUpdate = -1L;
      Arrays.fill(tickRates, 0.0F);
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 210.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
   }

   public void onEnable() {
      PinableFrame.Toggle("TpsSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("TpsSET", false);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketTimeUpdate) {
         this.onTimeUpdate();
      }

      return true;
   }

   private void onTimeUpdate() {
      if (this.timeLastTimeUpdate != -1L) {
         float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0F;
         tickRates[this.nextIndex % tickRates.length] = MathHelper.func_76131_a(20.0F / timeElapsed, 0.0F, 20.0F);
         ++this.nextIndex;
      }

      this.timeLastTimeUpdate = System.currentTimeMillis();
   }

   public static float getTickRate() {
      float numTicks = 0.0F;
      float sumTickRates = 0.0F;
      float[] var2 = tickRates;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         float tickRate = var2[var4];
         if (tickRate > 0.0F) {
            sumTickRates += tickRate;
            ++numTicks;
         }
      }

      return MathHelper.func_76131_a(sumTickRates / numTicks, 0.0F, 20.0F);
   }

   public static class TpsRUN extends PinableFrame {
      public TpsRUN() {
         super("TpsSET", new String[0], (int)Tps.ypos.getValDouble(), (int)Tps.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Tps.xpos, Tps.ypos, Tps.Frame, Tps.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Tps.xpos, Tps.ypos, Tps.Frame, Tps.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         String tickrate = String.format("TPS: %.2f", Tps.getTickRate());
         this.fontSelect(Tps.Frame, tickrate, (float)this.getX(), (float)(this.getY() + 10), Tps.TextColor.getcolor(), Tps.Shadow.getValBoolean());
         if (Tps.background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Tps.Frame, tickrate), this.y + 20, Tps.BgColor.getcolor());
         }

         super.onRenderGameOverlay(event);
      }
   }
}
