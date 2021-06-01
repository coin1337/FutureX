package Method.Client.module;

import Method.Client.managers.Setting;
import Method.Client.utils.Patcher.Events.EntityPlayerJumpEvent;
import Method.Client.utils.Patcher.Events.EventBookPage;
import Method.Client.utils.Patcher.Events.EventCanCollide;
import Method.Client.utils.Patcher.Events.GetAmbientOcclusionLightValueEvent;
import Method.Client.utils.Patcher.Events.GetLiquidCollisionBoxEvent;
import Method.Client.utils.Patcher.Events.PlayerDamageBlockEvent;
import Method.Client.utils.Patcher.Events.PlayerMoveEvent;
import Method.Client.utils.Patcher.Events.PostMotionEvent;
import Method.Client.utils.Patcher.Events.PreMotionEvent;
import Method.Client.utils.Patcher.Events.RenderBlockModelEvent;
import Method.Client.utils.Patcher.Events.RenderTileEntityEvent;
import Method.Client.utils.Patcher.Events.SetOpaqueCubeEvent;
import Method.Client.utils.Patcher.Events.ShouldSideBeRenderedEvent;
import Method.Client.utils.system.Connection;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.client.event.RenderTooltipEvent.PostBackground;
import net.minecraftforge.client.event.RenderTooltipEvent.Pre;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.ChunkEvent.Unload;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class Module {
   protected static Minecraft mc = Minecraft.func_71410_x();
   protected static Minecraft MC = Minecraft.func_71410_x();
   public ArrayList<Module> StoredModules = new ArrayList();
   public ArrayList<Setting> StoredSettings = new ArrayList();
   public ArrayList<Integer> Keys;
   private boolean toggled;
   public boolean visible = true;
   private String name;
   private String displayName;
   private final String tooltip;
   private Category category;

   public Module(String name, int key, Category category, String tooltip) {
      this.name = name;
      this.tooltip = tooltip;
      this.Keys = new ArrayList();
      this.Keys.add(key);
      this.category = category;
      this.toggled = false;
      this.setup();
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      return true;
   }

   public boolean onDisablePacket(Object packet, Connection.Side side) {
      return true;
   }

   public void onToggle() {
   }

   public void toggle() {
      this.toggled = !this.toggled;
      this.onToggle();
      if (this.toggled) {
         if (!ModuleManager.toggledModules.contains(this)) {
            ModuleManager.toggledModules.add(this);
         }

         this.onEnable();
      } else {
         this.onDisable();
         ModuleManager.toggledModules.remove(this);
      }

   }

   public ArrayList<String> fontoptions() {
      ArrayList<String> Fontoptions = new ArrayList();
      Fontoptions.add("Arial");
      Fontoptions.add("Impact");
      Fontoptions.add("Times");
      Fontoptions.add("MC");
      return Fontoptions;
   }

   public ArrayList<String> BlockEspOptions() {
      ArrayList<String> BlockOptions = new ArrayList();
      BlockOptions.add("Outline");
      BlockOptions.add("Full");
      BlockOptions.add("Flat");
      BlockOptions.add("FlatOutline");
      BlockOptions.add("Beacon");
      BlockOptions.add("Xspot");
      BlockOptions.add("Tracer");
      BlockOptions.add("Shape");
      BlockOptions.add("None");
      return BlockOptions;
   }

   public boolean isToggled() {
      return this.toggled;
   }

   public String getName() {
      return this.name;
   }

   public String getTooltip() {
      return this.tooltip;
   }

   public void setName(String name) {
      this.name = name;
   }

   public ArrayList<Integer> getKeys() {
      return this.Keys;
   }

   public void setKey(int Key, boolean Control, boolean Shift, boolean Alt) {
      this.Keys = new ArrayList();
      if (Control) {
         this.Keys.add(29);
      }

      if (Shift) {
         this.Keys.add(42);
      }

      if (Alt) {
         this.Keys.add(56);
      }

      this.Keys.add(Key);
   }

   public void setKeys(String keys) {
      if (keys != null) {
         keys = keys.replaceAll("\\[", "");
         keys = keys.replaceAll("]", "");
         keys = keys.replaceAll(" ", "");
         String[] tryit = keys.split(",");
         ArrayList<Integer> key = new ArrayList();
         String[] var4 = tryit;
         int var5 = tryit.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String s = var4[var6];
            key.add(Integer.valueOf(s));
         }

         this.Keys = key;
      }

   }

   public Category getCategory() {
      return this.category;
   }

   public void setCategory(Category category) {
      this.category = category;
   }

   public void setToggled(boolean toggled) {
      this.toggled = toggled;
      this.onToggle();
      if (toggled) {
         if (!ModuleManager.toggledModules.contains(this)) {
            ModuleManager.toggledModules.add(this);
         }

         this.onEnable();
      } else {
         this.onDisable();
         ModuleManager.toggledModules.remove(this);
      }

   }

   public String getDisplayName() {
      return this.displayName == null ? this.name : this.displayName;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   public void setup() {
   }

   public ArrayList<Module> getStoredModules() {
      return this.StoredModules;
   }

   public void setStoredModules(ArrayList<Module> storedModules) {
      this.StoredModules = storedModules;
   }

   public ArrayList<Setting> getStoredSettings() {
      return this.StoredSettings;
   }

   public void setStoredSettings(ArrayList<Setting> storedSettings) {
      this.StoredSettings = storedSettings;
   }

   public void onClientTick(ClientTickEvent event) {
   }

   public void onCameraSetup(CameraSetup event) {
   }

   public void onItemPickup(EntityItemPickupEvent event) {
   }

   public void onProjectileImpact(ProjectileImpactEvent event) {
   }

   public void onAttackEntity(AttackEntityEvent event) {
   }

   public void onPlayerTick(PlayerTickEvent event) {
   }

   public void onLivingUpdate(LivingUpdateEvent event) {
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
   }

   public void onRenderGameOverlay(Text event) {
   }

   public void onLeftClickBlock(LeftClickBlock event) {
   }

   public void onRightClickBlock(RightClickBlock event) {
   }

   public void SetOpaqueCubeEvent(SetOpaqueCubeEvent event) {
   }

   public void onGetAmbientOcclusionLightValue(GetAmbientOcclusionLightValueEvent event) {
   }

   public void onShouldSideBeRendered(ShouldSideBeRenderedEvent event) {
   }

   public void onRenderBlockModel(RenderBlockModelEvent event) {
   }

   public void onRenderTileEntity(RenderTileEntityEvent event) {
   }

   public void EventCanCollide(EventCanCollide event) {
   }

   public void ItemTooltipEvent(ItemTooltipEvent event) {
   }

   public void postBackgroundTooltipRender(PostBackground event) {
   }

   public void postDrawScreen(Post event) {
   }

   public void RenderGameOverLayPost(net.minecraftforge.client.event.RenderGameOverlayEvent.Post event) {
   }

   public void onPlayerMove(PlayerMoveEvent event) {
   }

   public void onPlayerJump(EntityPlayerJumpEvent event) {
   }

   public void RendergameOverlay(RenderGameOverlayEvent event) {
   }

   public void ChunkeventUNLOAD(Unload event) {
   }

   public void ChunkeventLOAD(Load event) {
   }

   public void fogColor(FogColors event) {
   }

   public void fogDensity(FogDensity event) {
   }

   public void DamageBlock(PlayerDamageBlockEvent event) {
   }

   public void PlayerSleepInBedEvent(PlayerSleepInBedEvent event) {
   }

   public void GetLiquidCollisionBoxEvent(GetLiquidCollisionBoxEvent event) {
   }

   public void EventBookPage(EventBookPage event) {
   }

   public void ClientChatReceivedEvent(ClientChatReceivedEvent event) {
   }

   public void ClientChatEvent(ClientChatEvent event) {
   }

   public void LivingDeathEvent(LivingDeathEvent event) {
   }

   public void WorldEvent(WorldEvent event) {
   }

   public void GuiScreenEvent(GuiScreenEvent event) {
   }

   public void GetCollisionBoxesEvent(GetCollisionBoxesEvent event) {
   }

   public void RendertooltipPre(Pre event) {
   }

   public void RenderPlayerEvent(RenderPlayerEvent event) {
   }

   public void RenderBlockOverlayEvent(RenderBlockOverlayEvent event) {
   }

   public void FOVModifier(FOVModifier event) {
   }

   public void DrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
   }

   public void PostMotionEvent(PostMotionEvent event) {
   }

   public void PreMotionEvent(PreMotionEvent event) {
   }

   public void BackgroundDrawnEvent(BackgroundDrawnEvent event) {
   }

   public void RenderTickEvent(RenderTickEvent event) {
   }

   public void onRenderPre(net.minecraftforge.client.event.RenderGameOverlayEvent.Pre event) {
   }

   public void setsave() {
   }

   public void setdelete() {
   }

   public void onWorldLoad(net.minecraftforge.event.world.WorldEvent.Load event) {
   }

   public void onWorldUnload(net.minecraftforge.event.world.WorldEvent.Unload event) {
   }

   public void GuiOpen(GuiOpenEvent event) {
   }

   public void PlayerRespawnEvent(PlayerRespawnEvent event) {
   }
}
