package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.RenderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class NewChunks extends Module {
   Setting OverlayColor;
   Setting Mode;
   Setting LineWidth;
   Setting MaxDistance;
   private final List<Vec2f> chunkDataList;

   public NewChunks() {
      super("NewChunks", 0, Category.RENDER, "NewChunks");
      this.OverlayColor = Main.setmgr.add(new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 1.0D));
      this.Mode = Main.setmgr.add(new Setting("Hole Mode", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.MaxDistance = Main.setmgr.add(new Setting("MaxDistance", this, 1000.0D, 0.0D, 50000.0D, false));
      this.chunkDataList = new ArrayList();
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketChunkData) {
         SPacketChunkData packet2 = (SPacketChunkData)packet;
         if (!packet2.func_149274_i()) {
            Vec2f chunk = new Vec2f((float)(packet2.func_149273_e() * 16), (float)(packet2.func_149271_f() * 16));
            if (!this.chunkDataList.contains(chunk)) {
               this.chunkDataList.add(chunk);
            }
         }
      }

      return true;
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      List<Vec2f> found = new ArrayList();
      Iterator var3 = this.chunkDataList.iterator();

      while(var3.hasNext()) {
         Vec2f chunkData = (Vec2f)var3.next();
         if (chunkData != null) {
            if (mc.field_71439_g.func_70011_f((double)chunkData.field_189982_i, mc.field_71439_g.field_70163_u, (double)chunkData.field_189983_j) > this.MaxDistance.getValDouble()) {
               found.add(chunkData);
            }

            double renderPosX = (double)chunkData.field_189982_i - mc.func_175598_ae().field_78730_l;
            double renderPosY = -mc.func_175598_ae().field_78731_m;
            double renderPosZ = (double)chunkData.field_189983_j - mc.func_175598_ae().field_78728_n;
            AxisAlignedBB bb = new AxisAlignedBB(renderPosX, renderPosY, renderPosZ, renderPosX + 16.0D, renderPosY + 1.0D, renderPosZ + 16.0D);
            RenderUtils.RenderBlock(this.Mode.getValString(), bb, this.OverlayColor.getcolor(), this.LineWidth.getValDouble());
         }
      }

      this.chunkDataList.removeAll(found);
      super.onRenderWorldLast(event);
   }
}
