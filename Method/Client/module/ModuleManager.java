package Method.Client.module;

import Method.Client.managers.CommandManager;
import Method.Client.managers.FileManager;
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
import Method.Client.module.Profiles.Profiletem;
import Method.Client.module.combat.AimBot;
import Method.Client.module.combat.Anchor;
import Method.Client.module.combat.AntiBot;
import Method.Client.module.combat.AntiCrystal;
import Method.Client.module.combat.AutoArmor;
import Method.Client.module.combat.AutoRespawn;
import Method.Client.module.combat.AutoTotem;
import Method.Client.module.combat.AutoTrap;
import Method.Client.module.combat.BowMod;
import Method.Client.module.combat.Burrow;
import Method.Client.module.combat.Criticals;
import Method.Client.module.combat.CrystalAura;
import Method.Client.module.combat.FireballReturn;
import Method.Client.module.combat.HoleEsp;
import Method.Client.module.combat.HoleFill;
import Method.Client.module.combat.InteractClick;
import Method.Client.module.combat.KillAura;
import Method.Client.module.combat.MoreKnockback;
import Method.Client.module.combat.Offhand;
import Method.Client.module.combat.Refill;
import Method.Client.module.combat.Regen;
import Method.Client.module.combat.SelfTrap;
import Method.Client.module.combat.Strafe;
import Method.Client.module.combat.Surrond;
import Method.Client.module.combat.TotemPop;
import Method.Client.module.combat.Trigger;
import Method.Client.module.combat.Velocity;
import Method.Client.module.combat.Webfill;
import Method.Client.module.misc.AntiCheat;
import Method.Client.module.misc.AntiCrash;
import Method.Client.module.misc.AntiHandShake;
import Method.Client.module.misc.AntiHurt;
import Method.Client.module.misc.Antipacket;
import Method.Client.module.misc.Antispam;
import Method.Client.module.misc.AutoClicker;
import Method.Client.module.misc.AutoNametag;
import Method.Client.module.misc.ChatMutator;
import Method.Client.module.misc.CoordsFinder;
import Method.Client.module.misc.DiscordRPCModule;
import Method.Client.module.misc.EchestBP;
import Method.Client.module.misc.FastSleep;
import Method.Client.module.misc.Ghost;
import Method.Client.module.misc.GuiModule;
import Method.Client.module.misc.GuiPeek;
import Method.Client.module.misc.HitEffects;
import Method.Client.module.misc.Livestock;
import Method.Client.module.misc.ModSettings;
import Method.Client.module.misc.NbtView;
import Method.Client.module.misc.NoSlowdown;
import Method.Client.module.misc.Pickupmod;
import Method.Client.module.misc.PluginsGetter;
import Method.Client.module.misc.QuickCraft;
import Method.Client.module.misc.ServerCrash;
import Method.Client.module.misc.Shulkerspy;
import Method.Client.module.misc.TimeStamp;
import Method.Client.module.misc.ToolTipPlus;
import Method.Client.module.misc.VanishDetector;
import Method.Client.module.misc.VersionSpoofer;
import Method.Client.module.movement.AntiFall;
import Method.Client.module.movement.AutoHold;
import Method.Client.module.movement.AutoSwim;
import Method.Client.module.movement.Blink;
import Method.Client.module.movement.BoatFly;
import Method.Client.module.movement.Bunnyhop;
import Method.Client.module.movement.Derp;
import Method.Client.module.movement.ElytraFly;
import Method.Client.module.movement.EntityVanish;
import Method.Client.module.movement.Entityspeed;
import Method.Client.module.movement.FastFall;
import Method.Client.module.movement.Fly;
import Method.Client.module.movement.Glide;
import Method.Client.module.movement.InvMove;
import Method.Client.module.movement.Jesus;
import Method.Client.module.movement.Jump;
import Method.Client.module.movement.Levitate;
import Method.Client.module.movement.LiquidSpeed;
import Method.Client.module.movement.LongJump;
import Method.Client.module.movement.Parkour;
import Method.Client.module.movement.Phase;
import Method.Client.module.movement.SafeWalk;
import Method.Client.module.movement.Scaffold;
import Method.Client.module.movement.Sneak;
import Method.Client.module.movement.Speed;
import Method.Client.module.movement.Spider;
import Method.Client.module.movement.Sprint;
import Method.Client.module.movement.Step;
import Method.Client.module.movement.Teleport;
import Method.Client.module.player.AntiAFK;
import Method.Client.module.player.AutoFish;
import Method.Client.module.player.AutoRemount;
import Method.Client.module.player.Autotool;
import Method.Client.module.player.BuildHeight;
import Method.Client.module.player.ChestStealer;
import Method.Client.module.player.Disconnect;
import Method.Client.module.player.FastBreak;
import Method.Client.module.player.FastLadder;
import Method.Client.module.player.FastPlace;
import Method.Client.module.player.FoodMod;
import Method.Client.module.player.FreeCam;
import Method.Client.module.player.God;
import Method.Client.module.player.LiquidInteract;
import Method.Client.module.player.NoEffect;
import Method.Client.module.player.NoServerChange;
import Method.Client.module.player.Noswing;
import Method.Client.module.player.Nowall;
import Method.Client.module.player.Nuker;
import Method.Client.module.player.PitchLock;
import Method.Client.module.player.PortalMod;
import Method.Client.module.player.Reach;
import Method.Client.module.player.SchematicaNCP;
import Method.Client.module.player.SkinBlink;
import Method.Client.module.player.SmallShield;
import Method.Client.module.player.Timer;
import Method.Client.module.player.Xcarry;
import Method.Client.module.player.YawLock;
import Method.Client.module.render.ArmorRender;
import Method.Client.module.render.BlockOverlay;
import Method.Client.module.render.BossStack;
import Method.Client.module.render.Breadcrumb;
import Method.Client.module.render.BreakEsp;
import Method.Client.module.render.ChestESP;
import Method.Client.module.render.ChunkBorder;
import Method.Client.module.render.ESP;
import Method.Client.module.render.ExtraTab;
import Method.Client.module.render.F3Spoof;
import Method.Client.module.render.Fovmod;
import Method.Client.module.render.Fullbright;
import Method.Client.module.render.ItemESP;
import Method.Client.module.render.MobOwner;
import Method.Client.module.render.MotionBlur;
import Method.Client.module.render.NameTags;
import Method.Client.module.render.NetherSky;
import Method.Client.module.render.NewChunks;
import Method.Client.module.render.NoBlockLag;
import Method.Client.module.render.NoRender;
import Method.Client.module.render.Search;
import Method.Client.module.render.SeedViewer;
import Method.Client.module.render.SkyColor;
import Method.Client.module.render.Tracers;
import Method.Client.module.render.Trail;
import Method.Client.module.render.Trajectories;
import Method.Client.module.render.Visualrange;
import Method.Client.module.render.WallHack;
import Method.Client.module.render.Xray;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import net.minecraftforge.event.world.ChunkEvent.Unload;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import org.lwjgl.input.Keyboard;

public class ModuleManager {
   public static ArrayList<Module> modules = new ArrayList();
   public static ArrayList<Module> toggledModules = new ArrayList();
   public static ArrayList<Module> FileManagerLoader = new ArrayList();

   public ModuleManager() {
      addModule(new Antispam());
      addModule(new ChatMutator());
      addModule(new TimeStamp());
      addModule(new AimBot());
      addModule(new AntiBot());
      addModule(new HoleFill());
      addModule(new AutoArmor());
      addModule(new AntiCrystal());
      addModule(new Anchor());
      addModule(new AutoTotem());
      addModule(new AutoTrap());
      addModule(new Burrow());
      addModule(new Criticals());
      addModule(new CrystalAura());
      addModule(new BowMod());
      addModule(new InteractClick());
      addModule(new KillAura());
      addModule(new MoreKnockback());
      addModule(new Regen());
      addModule(new Refill());
      addModule(new Strafe());
      addModule(new Offhand());
      addModule(new SelfTrap());
      addModule(new Surrond());
      addModule(new Trigger());
      addModule(new Webfill());
      addModule(new TotemPop());
      addModule(new Velocity());
      addModule(new DiscordRPCModule());
      addModule(new AntiCrash());
      addModule(new AntiCheat());
      addModule(new AntiHurt());
      addModule(new Antipacket());
      addModule(new AntiHandShake());
      addModule(new AutoClicker());
      addModule(new AutoNametag());
      addModule(new Pickupmod());
      addModule(new Livestock());
      addModule(new CoordsFinder());
      addModule(new EchestBP());
      addModule(new FastSleep());
      addModule(new HitEffects());
      addModule(new Derp());
      addModule(new GuiModule());
      addModule(new InvMove());
      addModule(new ToolTipPlus());
      addModule(new NbtView());
      addModule(new NoSlowdown());
      addModule(new Ghost());
      addModule(new ModSettings());
      addModule(new VersionSpoofer());
      addModule(new PluginsGetter());
      addModule(new GuiPeek());
      addModule(new Shulkerspy());
      addModule(new ServerCrash());
      addModule(new VanishDetector());
      addModule(new QuickCraft());
      addModule(new AntiFall());
      addModule(new AutoSwim());
      addModule(new AutoHold());
      addModule(new Bunnyhop());
      addModule(new Blink());
      addModule(new BoatFly());
      addModule(new ElytraFly());
      addModule(new Entityspeed());
      addModule(new EntityVanish());
      addModule(new FastFall());
      addModule(new Fly());
      addModule(new Glide());
      addModule(new Jump());
      addModule(new Jesus());
      addModule(new Levitate());
      addModule(new LiquidSpeed());
      addModule(new LongJump());
      addModule(new Parkour());
      addModule(new Phase());
      addModule(new SafeWalk());
      addModule(new Sneak());
      addModule(new Speed());
      addModule(new Spider());
      addModule(new Sprint());
      addModule(new Step());
      addModule(new Teleport());
      addModule(new Armor());
      addModule(new Biome());
      addModule(new Coords());
      addModule(new ChunkSize());
      addModule(new Direction());
      addModule(new Durability());
      addModule(new EnabledMods());
      addModule(new Enemypos());
      addModule(new KeyStroke());
      addModule(new Fps());
      addModule(new CombatItems());
      addModule(new Angles());
      addModule(new Blockview());
      addModule(new Hole());
      addModule(new Hunger());
      addModule(new Inventory());
      addModule(new NetherCords());
      addModule(new Ping());
      addModule(new Player());
      addModule(new PlayerCount());
      addModule(new PlayerSpeed());
      addModule(new Potions());
      addModule(new Server());
      addModule(new ServerResponce());
      addModule(new Time());
      addModule(new Tps());
      addModule(new AntiAFK());
      addModule(new AutoFish());
      addModule(new AutoRemount());
      addModule(new AutoRespawn());
      addModule(new Autotool());
      addModule(new BuildHeight());
      addModule(new ChestStealer());
      addModule(new Disconnect());
      addModule(new FastBreak());
      addModule(new FastLadder());
      addModule(new FastPlace());
      addModule(new FireballReturn());
      addModule(new FoodMod());
      addModule(new FreeCam());
      addModule(new Reach());
      addModule(new God());
      addModule(new LiquidInteract());
      addModule(new NoServerChange());
      addModule(new Noswing());
      addModule(new Nowall());
      addModule(new Nuker());
      addModule(new PitchLock());
      addModule(new PortalMod());
      addModule(new Scaffold());
      addModule(new SchematicaNCP());
      addModule(new SkinBlink());
      addModule(new SmallShield());
      addModule(new Timer());
      addModule(new Xcarry());
      addModule(new YawLock());
      addModule(new BlockOverlay());
      addModule(new Breadcrumb());
      addModule(new BossStack());
      addModule(new BreakEsp());
      addModule(new ChestESP());
      addModule(new ChunkBorder());
      addModule(new ESP());
      addModule(new ExtraTab());
      addModule(new ArmorRender());
      addModule(new HoleEsp());
      addModule(new Fullbright());
      addModule(new F3Spoof());
      addModule(new ItemESP());
      addModule(new Visualrange());
      addModule(new MobOwner());
      addModule(new SeedViewer());
      addModule(new MotionBlur());
      addModule(new NewChunks());
      addModule(new NoEffect());
      addModule(new NoRender());
      addModule(new NoBlockLag());
      addModule(new NetherSky());
      addModule(new NameTags());
      addModule(new Search());
      addModule(new SkyColor());
      addModule(new Tracers());
      addModule(new Trail());
      addModule(new Trajectories());
      addModule(new WallHack());
      addModule(new Xray());
      addModule(new Fovmod());
      if (!FileManager.SaveDir.exists()) {
         addModule(new Profiletem("Example"));
         addModule(new Profiletem("Example2"));
      } else {
         FileManager.loadPROFILES();
      }

   }

   public static void addModule(Module m) {
      modules.add(m);
   }

   public static ArrayList<Module> getModules() {
      return modules;
   }

   public static ArrayList<Module> getEnabledmodules() {
      return toggledModules;
   }

   public static void onKeyPressed(int key) {
      Iterator var1 = modules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         boolean NeedControl = false;
         boolean NeedShift = false;
         boolean NeedAlt = false;
         int Keydiff = 0;
         Iterator var7 = m.getKeys().iterator();

         while(var7.hasNext()) {
            Integer mKey = (Integer)var7.next();
            if (mKey == 29) {
               NeedControl = true;
            } else if (mKey == 42) {
               NeedShift = true;
            } else if (mKey == 56) {
               NeedAlt = true;
            } else {
               Keydiff = mKey;
            }
         }

         if (key == Keydiff) {
            if (NeedControl && !Keyboard.isKeyDown(29)) {
               return;
            }

            if (NeedShift && !Keyboard.isKeyDown(42)) {
               return;
            }

            if (NeedAlt && !Keyboard.isKeyDown(56)) {
               return;
            }

            m.toggle();
         }
      }

   }

   public static Module getModuleByName(String name) {
      return (Module)modules.stream().filter((module) -> {
         return module.getName().equalsIgnoreCase(name);
      }).findFirst().orElse((Object)null);
   }

   public static ArrayList<Module> getModulesInCategory(Category categoryIn) {
      ArrayList<Module> mods = new ArrayList();
      Iterator var2 = getSortedHacksabc(false).iterator();

      while(var2.hasNext()) {
         Module m = (Module)var2.next();
         if (m.getCategory() == categoryIn) {
            mods.add(m);
         }
      }

      return mods;
   }

   public static void onWorldLoad(Load event) {
      Iterator var1 = FileManagerLoader.iterator();

      Module m;
      while(var1.hasNext()) {
         m = (Module)var1.next();
         m.setToggled(true);
      }

      FileManagerLoader.clear();
      var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         m = (Module)var1.next();
         m.onWorldLoad(event);
      }

   }

   public static void onCameraSetup(CameraSetup event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onCameraSetup(event);
      }

   }

   public static void onItemPickup(EntityItemPickupEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onItemPickup(event);
      }

   }

   public static void onProjectileImpact(ProjectileImpactEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onProjectileImpact(event);
      }

   }

   public static void onAttackEntity(AttackEntityEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onAttackEntity(event);
      }

   }

   public static void onPlayerTick(PlayerTickEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onPlayerTick(event);
      }

   }

   public static void onLivingUpdate(LivingUpdateEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onLivingUpdate(event);
      }

   }

   public static void onRenderWorldLast(RenderWorldLastEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onRenderWorldLast(event);
      }

   }

   public static void onRenderGameOverlay(Text event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onRenderGameOverlay(event);
      }

   }

   public static void onLeftClickBlock(LeftClickBlock event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onLeftClickBlock(event);
      }

   }

   public static void onRightClickBlock(RightClickBlock event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onRightClickBlock(event);
      }

   }

   public static void onClientTick(ClientTickEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onClientTick(event);
      }

   }

   public static void SetOpaqueCubeEvent(SetOpaqueCubeEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.SetOpaqueCubeEvent(event);
      }

   }

   public static void onGetAmbientOcclusionLightValue(GetAmbientOcclusionLightValueEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onGetAmbientOcclusionLightValue(event);
      }

   }

   public static void onShouldSideBeRendered(ShouldSideBeRenderedEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onShouldSideBeRendered(event);
      }

   }

   public static void onRenderBlockModel(RenderBlockModelEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onRenderBlockModel(event);
      }

   }

   public static void onRenderTileEntity(RenderTileEntityEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onRenderTileEntity(event);
      }

   }

   public static void EventCanCollide(EventCanCollide event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.EventCanCollide(event);
      }

   }

   public static void ItemTooltipEvent(ItemTooltipEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.ItemTooltipEvent(event);
      }

   }

   public static void postBackgroundTooltipRender(PostBackground event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.postBackgroundTooltipRender(event);
      }

   }

   public static void postDrawScreen(Post event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.postDrawScreen(event);
      }

   }

   public static void RenderGameOverLayPost(net.minecraftforge.client.event.RenderGameOverlayEvent.Post event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.RenderGameOverLayPost(event);
      }

   }

   public static void onPlayerMove(PlayerMoveEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onPlayerMove(event);
      }

   }

   public static void onPlayerJump(EntityPlayerJumpEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onPlayerJump(event);
      }

   }

   public static void RendergameOverlay(RenderGameOverlayEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.RendergameOverlay(event);
      }

   }

   public static void ChunkeventUNLOAD(Unload event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.ChunkeventUNLOAD(event);
      }

   }

   public static void ChunkeventLOAD(net.minecraftforge.event.world.ChunkEvent.Load event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.ChunkeventLOAD(event);
      }

   }

   public static void fogColor(FogColors event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.fogColor(event);
      }

   }

   public static void fogDensity(FogDensity event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.fogDensity(event);
      }

   }

   public static void DamageBlock(PlayerDamageBlockEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.DamageBlock(event);
      }

   }

   public static void PlayerSleepInBedEvent(PlayerSleepInBedEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.PlayerSleepInBedEvent(event);
      }

   }

   public static void onGetLiquidCollisionBox(GetLiquidCollisionBoxEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.GetLiquidCollisionBoxEvent(event);
      }

   }

   public static void EventBookPage(EventBookPage event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.EventBookPage(event);
      }

   }

   public static void ClientChatReceivedEvent(ClientChatReceivedEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.ClientChatReceivedEvent(event);
      }

   }

   public static void LivingDeathEvent(LivingDeathEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.LivingDeathEvent(event);
      }

   }

   public static void WorldEvent(WorldEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.WorldEvent(event);
      }

   }

   public static void GuiScreenEvent(GuiScreenEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.GuiScreenEvent(event);
      }

   }

   public static void RendertooltipPre(Pre event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.RendertooltipPre(event);
      }

   }

   public static void RenderPlayerEvent(RenderPlayerEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.RenderPlayerEvent(event);
      }

   }

   public static void RenderBlockOverlayEvent(RenderBlockOverlayEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.RenderBlockOverlayEvent(event);
      }

   }

   public static void GetCollisionBoxesEvent(GetCollisionBoxesEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.GetCollisionBoxesEvent(event);
      }

   }

   public static void FOVModifier(FOVModifier event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.FOVModifier(event);
      }

   }

   public static void DrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.DrawBlockHighlightEvent(event);
      }

   }

   public static void PreMotionEvent(PreMotionEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.PreMotionEvent(event);
      }

   }

   public static void PostMotionEvent(PostMotionEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.PostMotionEvent(event);
      }

   }

   public static void BackgroundDrawnEvent(BackgroundDrawnEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.BackgroundDrawnEvent(event);
      }

   }

   public static void RenderTickEvent(RenderTickEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.RenderTickEvent(event);
      }

   }

   public static void onRenderPre(net.minecraftforge.client.event.RenderGameOverlayEvent.Pre event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onRenderPre(event);
      }

   }

   public static void onWorldUnload(net.minecraftforge.event.world.WorldEvent.Unload event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.onWorldUnload(event);
      }

   }

   public static void GuiOpen(GuiOpenEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.GuiOpen(event);
      }

   }

   public static void PlayerRespawnEvent(PlayerRespawnEvent event) {
      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.PlayerRespawnEvent(event);
      }

   }

   public static void ClientChatEvent(ClientChatEvent event) {
      if (event.getMessage().startsWith(String.valueOf(CommandManager.cmdPrefix))) {
         CommandManager.getInstance().runCommands(CommandManager.cmdPrefix + event.getMessage().substring(1));
         event.setCanceled(true);
         event.setMessage((String)null);
      }

      Iterator var1 = toggledModules.iterator();

      while(var1.hasNext()) {
         Module m = (Module)var1.next();
         m.ClientChatEvent(event);
      }

   }

   public static ArrayList<Module> getSortedMods(boolean reverse, boolean enabled, boolean visible) {
      ArrayList<Module> list = new ArrayList();
      ArrayList<String> listofmods = new ArrayList();
      Iterator var5;
      Module mod;
      if (!enabled) {
         var5 = getModules().iterator();

         while(var5.hasNext()) {
            mod = (Module)var5.next();
            if (visible && mod.visible) {
               listofmods.add(mod.getName());
            }

            if (!visible) {
               listofmods.add(mod.getName());
            }
         }
      }

      if (enabled) {
         var5 = getEnabledmodules().iterator();

         while(var5.hasNext()) {
            mod = (Module)var5.next();
            if (visible && mod.visible) {
               listofmods.add(mod.getName());
            }

            if (!visible) {
               listofmods.add(mod.getName());
            }
         }
      }

      listofmods.sort(Comparator.comparing(String::length));
      if (reverse) {
         Collections.reverse(listofmods);
      }

      var5 = listofmods.iterator();

      while(var5.hasNext()) {
         String s = (String)var5.next();
         list.add(getModuleByName(s));
      }

      return list;
   }

   public static ArrayList<Module> getSortedHacksabc(boolean reverse) {
      ArrayList<Module> list = new ArrayList();
      ArrayList<String> listofcountries = new ArrayList();
      Iterator var3 = getModules().iterator();

      while(var3.hasNext()) {
         Module module = (Module)var3.next();
         listofcountries.add(module.getName());
      }

      Collections.sort(listofcountries);
      if (reverse) {
         Collections.reverse(listofcountries);
      }

      var3 = listofcountries.iterator();

      while(var3.hasNext()) {
         String s = (String)var3.next();
         list.add(getModuleByName(s));
      }

      return list;
   }
}
