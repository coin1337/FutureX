package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Patcher.Events.GetAmbientOcclusionLightValueEvent;
import Method.Client.utils.Patcher.Events.RenderBlockModelEvent;
import Method.Client.utils.Patcher.Events.RenderTileEntityEvent;
import Method.Client.utils.Patcher.Events.SetOpaqueCubeEvent;
import Method.Client.utils.Patcher.Events.ShouldSideBeRenderedEvent;
import Method.Client.utils.Screens.Custom.Xray.XrayGuiSettings;
import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Xray extends Module {
   public static ArrayList<String> blockNames;
   Setting Gui;

   public Xray() {
      super("Xray", 0, Category.RENDER, "Xray");
      this.Gui = Main.setmgr.add(new Setting("Gui", this, Main.Xray));
      new XrayGuiSettings(new Block[]{Blocks.field_150365_q, Blocks.field_150402_ci, Blocks.field_150366_p, Blocks.field_150339_S, Blocks.field_150352_o, Blocks.field_150340_R, Blocks.field_150369_x, Blocks.field_150368_y, Blocks.field_150450_ax, Blocks.field_150439_ay, Blocks.field_150451_bX, Blocks.field_150482_ag, Blocks.field_150484_ah, Blocks.field_150412_bA, Blocks.field_150475_bE, Blocks.field_150449_bY, Blocks.field_150353_l, Blocks.field_150474_ac, Blocks.field_150427_aO, Blocks.field_150384_bq, Blocks.field_150378_br});
   }

   public void onEnable() {
      blockNames = new ArrayList(XrayGuiSettings.getBlockNames());
      MinecraftForge.EVENT_BUS.register(this);
      mc.field_71438_f.func_72712_a();
   }

   public void onClientTick(ClientTickEvent event) {
      mc.field_71474_y.field_74333_Y = 16.0F;
   }

   public void SetOpaqueCubeEvent(SetOpaqueCubeEvent event) {
      event.setCanceled(true);
   }

   public void onGetAmbientOcclusionLightValue(GetAmbientOcclusionLightValueEvent event) {
      event.setLightValue(1.0F);
   }

   public void onShouldSideBeRendered(ShouldSideBeRenderedEvent event) {
      event.setRendered(this.isVisible(event.getState().func_177230_c()));
   }

   public void onRenderBlockModel(RenderBlockModelEvent event) {
      if (!this.isVisible(event.getState().func_177230_c())) {
         event.setCanceled(true);
      }

   }

   public void onRenderTileEntity(RenderTileEntityEvent event) {
      if (!this.isVisible(event.getTileEntity().func_145838_q())) {
         event.setCanceled(true);
      }

   }

   public void onDisable() {
      MinecraftForge.EVENT_BUS.unregister(this);
      mc.field_71438_f.func_72712_a();
   }

   private boolean isVisible(Block block) {
      String name = getName(block);
      int index = Collections.binarySearch(blockNames, name);
      return index >= 0;
   }

   public static String getName(Block block) {
      return "" + Block.field_149771_c.func_177774_c(block);
   }
}
