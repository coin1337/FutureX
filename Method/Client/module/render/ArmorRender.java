package Method.Client.module.render;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;

public class ArmorRender extends Module {
   public static Setting enableColoredGlint;
   public static Setting useRuneTexture;
   public static Setting CustomColor;
   public static Setting Color;
   public static Setting RenderArmor;
   public static int BANE_OF_ARTHROPODS = 13369599;
   public static int FIRE_ASPECT = 16728064;
   public static int KNOCKBACK = 6684927;
   public static int LOOTING = 16769126;
   public static int SHARPNESS = 16750899;
   public static int SMITE = 52479;
   public static int SWEEPING = 13434675;
   public static int UNBREAKING = 52326;
   public static int FLAME = 16728064;
   public static int INFINITY = 13369599;
   public static int POWER = 16750899;
   public static int PUNCH = 6684927;
   public static int EFFICIENCY = 3394815;
   public static int FORTUNE = 16769126;
   public static int SILK_TOUCH = 13434777;
   public static int LUCK_OF_THE_SEA = 16769126;
   public static int LURE = 3394815;
   public static int AQUA_AFFINITY = 3368703;
   public static int BLAST_PROTECTION = 13395609;
   public static int DEPTH_STRIDER = 6711039;
   public static int FEATHER_FALLING = 13434777;
   public static int FIRE_PROTECTION = 16728064;
   public static int FROST_WALKER = 13434879;
   public static int MENDING = 16769126;
   public static int PROJECTILE_PROTECTION = 13408767;
   public static int PROTECTION = 52377;
   public static int RESPIRATION = 3368703;
   public static int THORNS = 16750899;
   public static int VANISHING_CURSE = 6684876;
   public static int BINDING_CURSE = 16777215;
   public static int DEFAULT = -8372020;

   public ArmorRender() {
      super("ArmorRender", 0, Category.RENDER, "ArmorRender");
   }

   public void setup() {
      Main.setmgr.add(enableColoredGlint = new Setting("enableColoredGlint", this, false));
      Main.setmgr.add(CustomColor = new Setting("CustomColor", this, false));
      Main.setmgr.add(Color = new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 0.56D, CustomColor, 2));
      Main.setmgr.add(useRuneTexture = new Setting("useRuneTexture", this, false));
      Main.setmgr.add(RenderArmor = new Setting("RenderArmor", this, true));
   }
}
