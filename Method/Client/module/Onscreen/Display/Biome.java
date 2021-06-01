package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class Biome extends Module {
   static Setting TextColor;
   static Setting BgColor;
   static Setting xpos;
   static Setting ypos;
   static Setting Frame;
   static Setting Background;
   static Setting Shadow;
   static Setting FontSize;

   public Biome() {
      super("Biome", 0, Category.ONSCREEN, "Biome");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(TextColor = new Setting("TextColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      Main.setmgr.add(BgColor = new Setting("BGColor", this, 0.01D, 0.0D, 0.3D, 0.22D));
      Main.setmgr.add(Shadow = new Setting("Shadow", this, true));
      Main.setmgr.add(Background = new Setting("Background", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 20.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
      Main.setmgr.add(Frame = new Setting("Font", this, "Times", this.fontoptions()));
      Main.setmgr.add(FontSize = new Setting("FontSize", this, 22.0D, 10.0D, 40.0D, true));
   }

   public void onEnable() {
      PinableFrame.Toggle("BiomeSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("BiomeSET", false);
   }

   public static class BiomeRUN extends PinableFrame {
      public BiomeRUN() {
         super("BiomeSET", new String[0], (int)Biome.ypos.getValDouble(), (int)Biome.xpos.getValDouble());
      }

      public void setup() {
         this.GetSetup(this, Biome.xpos, Biome.ypos, Biome.Frame, Biome.FontSize);
      }

      public void Ongui() {
         this.GetInit(this, Biome.xpos, Biome.ypos, Biome.Frame, Biome.FontSize);
      }

      public void onRenderGameOverlay(Text event) {
         BlockPos pos = this.mc.field_71439_g.func_180425_c();
         Chunk chunk = this.mc.field_71441_e.func_175726_f(pos);
         net.minecraft.world.biome.Biome biome = chunk.func_177411_a(pos, this.mc.field_71441_e.func_72959_q());
         if (Biome.Background.getValBoolean()) {
            Gui.func_73734_a(this.x, this.y + 10, this.x + this.widthcal(Biome.Frame, biome.func_185359_l()), this.y + 22, Biome.BgColor.getcolor());
         }

         this.fontSelect(Biome.Frame, biome.func_185359_l(), (float)this.getX(), (float)(this.getY() + 10), Biome.TextColor.getcolor(), Biome.Shadow.getValBoolean());
         super.onRenderGameOverlay(event);
      }
   }
}
