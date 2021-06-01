package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Speed extends Module {
   public Setting mode;
   Setting Icespeed;
   Setting motiony;
   Setting groundmultiplier;
   Setting airmultiplier;
   Setting motionzp;
   int counter;
   public static double G;
   public boolean sink;

   public Speed() {
      super("Speed", 0, Category.PLAYER, "Speed");
      this.mode = Main.setmgr.add(new Setting("Speed Mode", this, "Jump", new String[]{"Jump", "Forward", "Ice", "GroundHop", "Y-Port", "MoveTry", "AAC", "Hypixel BHop", "AAC_ZOOM", "Packet"}));
      this.Icespeed = Main.setmgr.add(new Setting("Icespeed", this, 1.0D, 0.1D, 5.0D, false, this.mode, "Ice", 2));
      this.motiony = Main.setmgr.add(new Setting("motiony", this, 0.0D, 0.0D, 1.5D, false, this.mode, "Forward", 2));
      this.groundmultiplier = Main.setmgr.add(new Setting("groundmultiplier", this, 0.2D, 0.001D, 0.5D, false, this.mode, "Forward", 3));
      this.airmultiplier = Main.setmgr.add(new Setting("airmultiplier", this, 1.0064D, 1.0D, 1.1D, false, this.mode, "Forward", 4));
      this.motionzp = Main.setmgr.add(new Setting("motionzp", this, 1.0D, 0.5D, 4.0D, false, this.mode, "Packet", 2));
      this.sink = false;
   }

   public void onPlayerTick(PlayerTickEvent event) {
      if (this.mode.getValString().equalsIgnoreCase("packet") && Utils.isMoving(mc.field_71439_g) && mc.field_71439_g.field_70122_E) {
         boolean step = ModuleManager.getModuleByName("step").isToggled();
         double posX = mc.field_71439_g.field_70165_t;
         double posY = mc.field_71439_g.field_70163_u;
         double posZ = mc.field_71439_g.field_70161_v;
         double[] dir1 = Utils.directionSpeed(0.5D);
         BlockPos pos = new BlockPos(posX + dir1[0], posY, posZ + dir1[1]);
         Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
         if (step && !(block instanceof BlockAir)) {
            setSpeed(mc.field_71439_g, 0.0D);
            return;
         }

         if (mc.field_71441_e.func_180495_p(new BlockPos(pos.func_177958_n(), pos.func_177956_o() - 1, pos.func_177952_p())).func_177230_c() instanceof BlockAir) {
            return;
         }

         setSpeed(mc.field_71439_g, 4.0D);
         mc.field_71439_g.field_71174_a.func_147297_a(new Position(posX + mc.field_71439_g.field_70159_w, mc.field_71439_g.field_70163_u <= 10.0D ? 255.0D : 1.0D, posZ + mc.field_71439_g.field_70179_y, true));
      }

   }

   public static void setSpeed(EntityLivingBase entity, double speed) {
      double[] dir = Utils.directionSpeed(speed);
      entity.field_70159_w = dir[0];
      entity.field_70179_y = dir[1];
   }

   public void onClientTick(ClientTickEvent event) {
      double direction;
      double speed;
      double currentMotion;
      if (this.mode.getValString().equalsIgnoreCase("Fast") && mc.field_71439_g.field_70122_E && mc.field_71439_g.field_191988_bg > 0.0F) {
         direction = Math.toRadians((double)mc.field_71439_g.field_70177_z);
         speed = -Math.sin(direction);
         currentMotion = Math.cos(direction);
         mc.field_71439_g.field_70159_w = speed * 5.0D;
         mc.field_71439_g.field_70179_y = currentMotion * 5.0D;
         mc.field_71439_g.field_71109_bG = 0.15F;
         mc.field_71439_g.func_70031_b(true);
      }

      EntityPlayerSP var10000;
      if (this.mode.getValString().equalsIgnoreCase("MoveTry")) {
         if (mc.field_71439_g.func_70093_af() || mc.field_71439_g.field_191988_bg == 0.0F && mc.field_71439_g.field_70702_br == 0.0F) {
            return;
         }

         if (mc.field_71439_g.field_191988_bg > 0.0F && !mc.field_71439_g.field_70123_F) {
            mc.field_71439_g.func_70031_b(true);
         }

         if (mc.field_71439_g.field_70122_E) {
            var10000 = mc.field_71439_g;
            var10000.field_70181_x += 0.1D;
            var10000 = mc.field_71439_g;
            var10000.field_70159_w *= 1.8D;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= 1.8D;
            direction = Math.sqrt(Math.pow(mc.field_71439_g.field_70159_w, 2.0D) + Math.pow(mc.field_71439_g.field_70179_y, 2.0D));
            speed = 0.6600000262260437D;
            if (direction > speed) {
               mc.field_71439_g.field_70159_w = mc.field_71439_g.field_70159_w / direction * speed;
               mc.field_71439_g.field_70179_y = mc.field_71439_g.field_70179_y / direction * speed;
            }
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Forward")) {
         this.Forward(this.motiony.getValDouble(), (float)this.groundmultiplier.getValDouble(), this.airmultiplier.getValDouble());
      }

      boolean boost;
      if (this.mode.getValString().equalsIgnoreCase("Jump")) {
         boost = Math.abs(mc.field_71439_g.field_70759_as - mc.field_71439_g.field_70177_z) < 90.0F;
         if (mc.field_71439_g.field_191988_bg > 0.0F && mc.field_71439_g.field_70737_aN < 5) {
            if (mc.field_71439_g.field_70122_E) {
               mc.field_71439_g.field_70181_x = 0.405D;
               float f = Utils.getDirection();
               var10000 = mc.field_71439_g;
               var10000.field_70159_w -= (double)(MathHelper.func_76126_a(f) * 0.2F);
               var10000 = mc.field_71439_g;
               var10000.field_70179_y += (double)(MathHelper.func_76134_b(f) * 0.2F);
            } else {
               double currentSpeed = Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y);
               double speed = boost ? 1.0064D : 1.001D;
               double direction = (double)Utils.getDirection();
               mc.field_71439_g.field_70159_w = -Math.sin(direction) * speed * currentSpeed;
               mc.field_71439_g.field_70179_y = Math.cos(direction) * speed * currentSpeed;
            }
         }

         super.onClientTick(event);
      }

      if (this.mode.getValString().equalsIgnoreCase("Ice")) {
         Blocks.field_150432_aD.field_149765_K = 0.4F * (float)(0.4000000059604645D * this.Icespeed.getValDouble());
         Blocks.field_150403_cj.field_149765_K = 0.4F * (float)(0.4000000059604645D * this.Icespeed.getValDouble());
         Blocks.field_185778_de.field_149765_K = 0.4F * (float)(0.4000000059604645D * this.Icespeed.getValDouble());
      }

      if (this.mode.getValString().equalsIgnoreCase("GroundHop") && mc.field_71439_g.field_70122_E) {
         this.sink = !this.sink;
         ++this.counter;
         if ((double)this.counter > 3.189546D) {
            var10000 = mc.field_71439_g;
            var10000.field_70159_w *= 0.009999999776482582D;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= 0.009999999776482582D;
            mc.field_71439_g.func_184614_ca().func_77972_a(0, mc.field_71439_g);
            mc.field_71439_g.field_70737_aN = 62284;
            this.counter = 0;
         }

         var10000 = mc.field_71439_g;
         var10000.field_70159_w *= 1.8300000429153442D;
         var10000 = mc.field_71439_g;
         var10000.field_70179_y *= 1.8300000429153442D;
         mc.field_71439_g.field_70172_ad = 1;
         mc.field_71439_g.field_70181_x = mc.field_71439_g.func_70093_af() ? -0.02D : (mc.field_71474_y.field_74314_A.field_74513_e ? 0.43D : (this.sink ? 0.37D : 0.25D));
         if (!(mc.field_71439_g.field_191988_bg > 0.0F)) {
            mc.field_71439_g.field_70159_w = 0.0D;
            mc.field_71439_g.field_70179_y = 0.0D;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Y-Port")) {
         if (this.isOnLiquid()) {
            return;
         }

         if (mc.field_71439_g.field_70122_E && (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F)) {
            if (mc.field_71439_g.field_70173_aa % 2 != 0) {
               var10000 = mc.field_71439_g;
               var10000.field_70163_u += 0.4D;
            }

            this.setSpeed(mc.field_71439_g.field_70173_aa % 2 == 0 ? 0.45F : 0.2F);
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("ACC")) {
         boost = Math.abs(mc.field_71439_g.field_70759_as - mc.field_71439_g.field_70177_z) < 90.0F;
         if (mc.field_71439_g.field_191988_bg > 0.0F && mc.field_71439_g.field_70737_aN < 5) {
            this.MoveSpeed(boost);
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Hypixel BHop")) {
         mc.field_71439_g.func_70031_b(true);
         if (mc.field_71441_e != null && mc.field_71439_g != null) {
            mc.field_71474_y.field_74314_A.field_74513_e = false;
            if (mc.field_71474_y.field_74351_w.field_74513_e && !mc.field_71439_g.field_70123_F) {
               if (mc.field_71439_g.field_70122_E) {
                  mc.field_71439_g.func_70664_aZ();
                  mc.field_71428_T.field_74280_b = 1;
                  var10000 = mc.field_71439_g;
                  var10000.field_70159_w *= 1.0700000524520874D;
                  var10000 = mc.field_71439_g;
                  var10000.field_70179_y *= 1.0700000524520874D;
               } else {
                  mc.field_71439_g.field_70747_aH = 0.0265F;
                  mc.field_71428_T.field_74280_b = 1;
                  direction = getDirection();
                  speed = 1.0D;
                  currentMotion = Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y);
                  mc.field_71439_g.field_70159_w = -Math.sin(direction) * speed * currentMotion;
                  mc.field_71439_g.field_70179_y = Math.cos(direction) * speed * currentMotion;
               }
            }
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("AAC_ZOOM")) {
         boost = Math.abs(mc.field_71439_g.field_70759_as - mc.field_71439_g.field_70177_z) < 90.0F;
         if (mc.field_71439_g.field_191988_bg > 0.0F && mc.field_71439_g.field_70737_aN < 5) {
            mc.field_71428_T.field_74280_b = 1;
            this.MoveSpeed(boost);
         }
      }

   }

   private void MoveSpeed(boolean boost) {
      if (mc.field_71439_g.field_70122_E) {
         mc.field_71439_g.field_70181_x = 0.405D;
         float f = (float)getDirection();
         EntityPlayerSP var10000 = mc.field_71439_g;
         var10000.field_70159_w -= (double)(MathHelper.func_76126_a(f) * 0.2F);
         var10000 = mc.field_71439_g;
         var10000.field_70179_y += (double)(MathHelper.func_76134_b(f) * 0.2F);
      } else {
         double currentSpeed = Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y);
         double speed = boost ? 1.0064D : 1.001D;
         double direction = getDirection();
         mc.field_71439_g.field_70159_w = -Math.sin(direction) * speed * currentSpeed;
         mc.field_71439_g.field_70179_y = Math.cos(direction) * speed * currentSpeed;
      }

   }

   public void onDisable() {
      Blocks.field_150432_aD.field_149765_K = 0.98F;
      Blocks.field_150403_cj.field_149765_K = 0.98F;
      Blocks.field_185778_de.field_149765_K = 0.98F;
      mc.field_71428_T.field_74280_b = 1;
      super.onDisable();
   }

   private boolean isOnLiquid() {
      boolean onLiquid = false;
      int y = (int)(mc.field_71439_g.func_174813_aQ().field_72338_b - 0.01D);

      for(int x = floor_double(mc.field_71439_g.func_174813_aQ().field_72340_a); x < floor_double(mc.field_71439_g.func_174813_aQ().field_72336_d) + 1; ++x) {
         for(int z = floor_double(mc.field_71439_g.func_174813_aQ().field_72339_c); z < floor_double(mc.field_71439_g.func_174813_aQ().field_72334_f) + 1; ++z) {
            Block block = mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
            if (!(block instanceof BlockAir)) {
               if (!(block instanceof BlockLiquid)) {
                  return false;
               }

               onLiquid = true;
            }
         }
      }

      return onLiquid;
   }

   public void setSpeed(float speed) {
      mc.field_71439_g.field_70159_w = -(Math.sin(getDirection()) * (double)speed);
      mc.field_71439_g.field_70179_y = Math.cos(getDirection()) * (double)speed;
   }

   public static double getDirection() {
      float var1 = mc.field_71439_g.field_70177_z;
      if (mc.field_71439_g.field_191988_bg < 0.0F) {
         var1 += 180.0F;
      }

      float forward = 1.0F;
      if (mc.field_71439_g.field_191988_bg < 0.0F) {
         forward = -0.5F;
      } else if (mc.field_71439_g.field_191988_bg > 0.0F) {
         forward = 0.5F;
      }

      if (mc.field_71439_g.field_70702_br > 0.0F) {
         var1 -= 90.0F * forward;
      }

      if (mc.field_71439_g.field_70702_br < 0.0F) {
         var1 += 90.0F * forward;
      }

      var1 *= 0.017453292F;
      return (double)var1;
   }

   public static int floor_double(double value) {
      int i = (int)value;
      return value < (double)i ? i - 1 : i;
   }

   private void Forward(double motionY, float groundmultiplier, double airmultiplier) {
      if (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) {
         mc.field_71439_g.func_70031_b(true);
         if (mc.field_71439_g.field_70122_E) {
            if (motionY > 0.01D) {
               mc.field_71439_g.field_70181_x = motionY;
            }

            float a = Converter();
            EntityPlayerSP var10000 = mc.field_71439_g;
            var10000.field_70159_w -= (double)(MathHelper.func_76126_a(a) * groundmultiplier);
            var10000 = mc.field_71439_g;
            var10000.field_70179_y += (double)(MathHelper.func_76134_b(a) * groundmultiplier);
         } else {
            double sqrt = Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y);
            double n3 = (double)Converter();
            mc.field_71439_g.field_70159_w = -Math.sin(n3) * airmultiplier * sqrt;
            mc.field_71439_g.field_70179_y = Math.cos(n3) * airmultiplier * sqrt;
         }
      }

   }

   public static float Converter() {
      float rotationYaw = mc.field_71439_g.field_70177_z;
      if (mc.field_71439_g.field_191988_bg < 0.0F) {
         rotationYaw += 180.0F;
      }

      float n = 1.0F;
      if (mc.field_71439_g.field_191988_bg < 0.0F) {
         n = -0.5F;
      } else if (mc.field_71439_g.field_191988_bg > 0.0F) {
         n = 0.5F;
      }

      if (mc.field_71439_g.field_70702_br > 0.0F) {
         rotationYaw -= 90.0F * n;
      }

      if (mc.field_71439_g.field_70702_br < 0.0F) {
         rotationYaw += 90.0F * n;
      }

      return rotationYaw * 0.017453292F;
   }
}
