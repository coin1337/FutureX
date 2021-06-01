package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public class F3Spoof extends Module {
   public Setting Coords;
   public Setting FPS;
   public Setting Direction;
   public Setting Biome;
   public Setting System;

   public F3Spoof() {
      super("F3Spoof", 0, Category.RENDER, "F3Spoof");
      this.Coords = Main.setmgr.add(new Setting("Coords", this, true));
      this.FPS = Main.setmgr.add(new Setting("FPS", this, true));
      this.Direction = Main.setmgr.add(new Setting("Direction", this, true));
      this.Biome = Main.setmgr.add(new Setting("Biome", this, true));
      this.System = Main.setmgr.add(new Setting("System", this, true));
   }

   public void onRenderGameOverlay(Text event) {
      if (mc.field_71474_y.field_74330_P) {
         for(int i = 0; i < event.getLeft().size(); ++i) {
            if (this.Coords.getValBoolean()) {
               if (((String)event.getLeft().get(i)).contains("Looking")) {
                  event.getLeft().set(i, "Looking at a block!");
               }

               if (((String)event.getLeft().get(i)).contains("XYZ")) {
                  event.getLeft().set(i, "XYZ: Hidden!");
               }

               if (((String)event.getLeft().get(i)).contains("Block:")) {
                  event.getLeft().set(i, "Block: Hidden!");
               }

               if (((String)event.getLeft().get(i)).contains("Chunk:")) {
                  event.getLeft().set(i, "Chunk: Hidden!");
               }
            }

            if (this.FPS.getValBoolean() && ((String)event.getLeft().get(i)).contains("fps")) {
               event.getLeft().set(i, "fps: 0!");
            }

            if (this.Direction.getValBoolean() && ((String)event.getLeft().get(i)).contains("Facing:")) {
               event.getLeft().set(i, "Facing: Hidden!");
            }

            if (this.Biome.getValBoolean() && ((String)event.getLeft().get(i)).contains("Biome:")) {
               event.getLeft().set(i, "Biome: Hidden!");
            }

            if (this.System.getValBoolean()) {
               if (((String)event.getRight().get(i)).contains("Display:")) {
                  event.getRight().set(i, "Display: 15360x 8640 (MethodClient)");
               } else if (((String)event.getRight().get(i)).contains("CPU:")) {
                  event.getRight().set(i, "CPU: 256x Apple PowerPC");
               } else if (((String)event.getRight().get(i)).contains("NVIDIA") || ((String)event.getRight().get(i)).contains("AMD") || ((String)event.getRight().get(i)).contains("PCIe")) {
                  event.getRight().set(i, "HIDDEN!");
               }
            }
         }
      }

   }
}
