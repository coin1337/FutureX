package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.Patcher.Events.PlayerMoveEvent;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import java.util.Objects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class LongJump extends Module {
   private int airTicks;
   private int groundTicks;
   Setting mode;
   Setting boostval;
   Setting Lagback;
   private double moveSpeed;
   private double lastDist;
   private int level;
   private final TimerUtils timer;
   private final TimerUtils aac;
   int delay2;
   double y;
   double speed;
   boolean teleported;
   private float air;
   private int stage;
   private boolean jump;

   public LongJump() {
      super("LongJump", 0, Category.MOVEMENT, "Jump Far");
      this.mode = Main.setmgr.add(new Setting("Jump mode", this, "Vanilla", new String[]{"Vanilla", "AAC", "Damage", "Long", "Legit", "Quack", "AAC4", "Mineplex", "Hypixel", "NeruxVace", "NeruxVace2"}));
      this.boostval = Main.setmgr.add(new Setting("boostval", this, 1.0D, 0.0D, 3.0D, false, this.mode, "Long", 3));
      this.Lagback = Main.setmgr.add(new Setting("Lagback", this, true));
      this.timer = new TimerUtils();
      this.aac = new TimerUtils();
      this.delay2 = 0;
      this.y = 0.0D;
      this.speed = 0.0D;
      this.teleported = false;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketPlayerPosLook) {
         SPacketPlayerPosLook pac = (SPacketPlayerPosLook)packet;
         if (this.Lagback.getValBoolean()) {
            ChatUtils.warning("Lagback checks!");
            mc.field_71439_g.field_70122_E = false;
            EntityPlayerSP var10000 = mc.field_71439_g;
            var10000.field_70159_w *= 0.0D;
            var10000 = mc.field_71439_g;
            var10000.field_70179_y *= 0.0D;
            mc.field_71439_g.field_70747_aH = 0.0F;
            this.toggle();
         } else if (this.timer.hasReached(300.0F)) {
            pac.field_148936_d = mc.field_71439_g.field_70177_z;
            pac.field_148937_e = mc.field_71439_g.field_70125_A;
         }

         this.timer.reset();
      }

      return true;
   }

   public void onDisable() {
      if (mc.field_71439_g != null) {
         this.moveSpeed = this.getBaseMoveSpeed();
      }

      this.lastDist = 0.0D;

      assert mc.field_71439_g != null;

      mc.field_71439_g.field_71102_ce = 0.02F;
      if (this.mode.getValString().equalsIgnoreCase("NeruxVace2")) {
         this.setMotion(0.2D);
      }

      if (this.mode.getValString().equalsIgnoreCase("NeruxVace")) {
         this.teleported = false;
         this.setMotion(0.22D);
      }

      this.speed = 0.0D;
      this.delay2 = 0;
      mc.field_71428_T.field_194149_e = 50.0F;
   }

   public void onEnable() {
      this.teleported = false;
      this.groundTicks = -5;
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         this.level = 0;
         this.lastDist = 0.0D;
         if (this.mode.getValString().equalsIgnoreCase("Hypixel")) {
            this.setMotion(0.15D);
            this.speed = 0.4D;
            this.y = 0.02D;
         }

      }
   }

   public static void toFwd(double speed) {
      float f = mc.field_71439_g.field_70177_z * 0.017453292F;
      EntityPlayerSP var10000 = mc.field_71439_g;
      var10000.field_70159_w -= (double)MathHelper.func_76126_a(f) * speed;
      var10000 = mc.field_71439_g;
      var10000.field_70179_y += (double)MathHelper.func_76134_b(f) * speed;
   }

   public void onPlayerTick(PlayerTickEvent event) {
      double xDist = mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70169_q;
      double zDist = mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70166_s;
      this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
      if (this.mode.getValString().equalsIgnoreCase("AAC")) {
         mc.field_71474_y.field_74351_w.field_74513_e = false;
         if (mc.field_71439_g.field_70122_E) {
            this.jump = true;
         }

         if (mc.field_71439_g.field_70122_E && this.aac.isDelay(500L)) {
            mc.field_71439_g.field_70181_x = 0.42D;
            toFwd(2.3D);
            this.timer.setLastMS();
         } else if (!mc.field_71439_g.field_70122_E && this.jump) {
            mc.field_71439_g.field_70159_w = mc.field_71439_g.field_70179_y = 0.0D;
            this.jump = false;
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Quack")) {
         boolean moving = mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d();
         if (!moving) {
            return;
         }

         double forward = (double)mc.field_71439_g.field_71158_b.field_192832_b;
         float yaw = mc.field_71439_g.field_70177_z;
         if (forward != 0.0D) {
            if (forward > 0.0D) {
               forward = 1.0D;
            } else if (forward < 0.0D) {
               forward = -1.0D;
            }
         } else {
            forward = 0.0D;
         }

         float[] motion = new float[]{0.4206065F, 0.4179245F, 0.41525924F, 0.41261F, 0.409978F, 0.407361F, 0.404761F, 0.402178F, 0.399611F, 0.39706F, 0.394525F, 0.392F, 0.3894F, 0.38644F, 0.383655F, 0.381105F, 0.37867F, 0.37625F, 0.37384F, 0.37145F, 0.369F, 0.3666F, 0.3642F, 0.3618F, 0.35945F, 0.357F, 0.354F, 0.351F, 0.348F, 0.345F, 0.342F, 0.339F, 0.336F, 0.333F, 0.33F, 0.327F, 0.324F, 0.321F, 0.318F, 0.315F, 0.312F, 0.309F, 0.307F, 0.305F, 0.303F, 0.3F, 0.297F, 0.295F, 0.293F, 0.291F, 0.289F, 0.287F, 0.285F, 0.283F, 0.281F, 0.279F, 0.277F, 0.275F, 0.273F, 0.271F, 0.269F, 0.267F, 0.265F, 0.263F, 0.261F, 0.259F, 0.257F, 0.255F, 0.253F, 0.251F, 0.249F, 0.247F, 0.245F, 0.243F, 0.241F, 0.239F, 0.237F};
         float[] glide = new float[]{0.3425F, 0.5445F, 0.65425F, 0.685F, 0.675F, 0.2F, 0.895F, 0.719F, 0.76F};
         double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
         double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
         if (!mc.field_71439_g.field_70124_G && !mc.field_71439_g.field_70122_E) {
            ++this.airTicks;
            this.groundTicks = -5;
            EntityPlayerSP var10000;
            if (this.airTicks - 6 >= 0 && this.airTicks - 6 < glide.length) {
               var10000 = mc.field_71439_g;
               var10000.field_70181_x *= (double)glide[this.airTicks - 6];
            }

            if (mc.field_71439_g.field_70181_x < -0.2D && mc.field_71439_g.field_70181_x > -0.24D) {
               var10000 = mc.field_71439_g;
               var10000.field_70181_x *= 0.7D;
            } else if (mc.field_71439_g.field_70181_x < -0.25D && mc.field_71439_g.field_70181_x > -0.32D) {
               var10000 = mc.field_71439_g;
               var10000.field_70181_x *= 0.8D;
            } else if (mc.field_71439_g.field_70181_x < -0.35D && mc.field_71439_g.field_70181_x > -0.8D) {
               var10000 = mc.field_71439_g;
               var10000.field_70181_x *= 0.98D;
            }

            if (this.airTicks - 1 >= 0 && this.airTicks - 1 < motion.length) {
               mc.field_71439_g.field_70159_w = forward * (double)motion[this.airTicks - 1] * 3.0D * cos;
               mc.field_71439_g.field_70179_y = forward * (double)motion[this.airTicks - 1] * 3.0D * sin;
            } else {
               mc.field_71439_g.field_70159_w = 0.0D;
               mc.field_71439_g.field_70179_y = 0.0D;
            }
         } else {
            this.airTicks = 0;
            ++this.groundTicks;
            if (this.groundTicks <= 2) {
               mc.field_71439_g.field_70159_w = forward * 0.009999999776482582D * cos;
               mc.field_71439_g.field_70179_y = forward * 0.009999999776482582D * sin;
            } else {
               mc.field_71439_g.field_70159_w = forward * 0.30000001192092896D * cos;
               mc.field_71439_g.field_70179_y = forward * 0.30000001192092896D * sin;
               mc.field_71439_g.field_70181_x = 0.42399999499320984D;
            }
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Damage")) {
         if (mc.field_71439_g.field_70122_E) {
            Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.300001D, mc.field_71439_g.field_70161_v, false));
            Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
            Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, true));
         }

         if (mc.field_71439_g.field_70737_aN > 0) {
            Movemulti(5.0D);
         } else {
            Movemulti(0.0D);
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Vanilla") && (mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d()) && mc.field_71474_y.field_74314_A.func_151470_d()) {
         float dir = mc.field_71439_g.field_70177_z + (float)(mc.field_71439_g.field_191988_bg < 0.0F ? 180 : 0) + (mc.field_71439_g.field_70702_br > 0.0F ? -90.0F * (mc.field_71439_g.field_191988_bg < 0.0F ? -0.5F : (mc.field_71439_g.field_191988_bg > 0.0F ? 0.4F : 1.0F)) : 0.0F);
         float xDir = (float)Math.cos((double)(dir + 90.0F) * 3.141592653589793D / 180.0D);
         float zDir = (float)Math.sin((double)(dir + 90.0F) * 3.141592653589793D / 180.0D);
         if (mc.field_71439_g.field_70124_G && (mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d()) && mc.field_71474_y.field_74314_A.func_151470_d()) {
            mc.field_71439_g.field_70159_w = (double)(xDir * 0.29F);
            mc.field_71439_g.field_70179_y = (double)(zDir * 0.29F);
         }

         if (mc.field_71439_g.field_70181_x == 0.33319999363422365D && (mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d())) {
            mc.field_71439_g.field_70159_w = (double)xDir * 1.261D;
            mc.field_71439_g.field_70179_y = (double)zDir * 1.261D;
         }
      }

   }

   public void onPlayerMove(PlayerMoveEvent event) {
      if (mc.field_71439_g != null) {
         if (!mc.field_71439_g.func_70090_H()) {
            if (this.mode.getValString().equalsIgnoreCase("NeruxVace2")) {
               mc.field_71428_T.field_194149_e = 30.0F;
               if (mc.field_71439_g.field_70122_E) {
                  mc.field_71439_g.func_70664_aZ();
                  mc.field_71439_g.field_70181_x = 1.0D;
                  this.setMotion(9.5D);
               }
            }

            EntityPlayerSP var10000;
            if (this.mode.getValString().equalsIgnoreCase("NeruxVace")) {
               mc.field_71439_g.field_71102_ce = 0.025F;
               var10000 = mc.field_71439_g;
               var10000.field_70159_w *= 1.08D;
               var10000 = mc.field_71439_g;
               var10000.field_70179_y *= 1.08D;
               if (mc.field_71439_g.field_70122_E) {
                  mc.field_71439_g.func_70664_aZ();
               }

               var10000 = mc.field_71439_g;
               var10000.field_70181_x += 0.072D;
            }

            if (this.mode.getValString().equalsIgnoreCase("Hypixel")) {
               boolean var1 = false;
               if (this.y > 0.0D) {
                  this.y *= 0.9D;
               }

               float var7 = mc.field_71439_g.field_70143_R;
               mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t * 1.0D, mc.field_71439_g.field_70163_u + 0.035423123132D, mc.field_71439_g.field_70161_v * 1.0D);
               float var2 = 0.7F + (float)this.getSpeedEffect() * 0.45F;
               if ((mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) && mc.field_71439_g.field_70122_E) {
                  this.setMotion(0.15D);
                  mc.field_71439_g.func_70664_aZ();
                  this.stage = 1;
               }

               if (mc.field_71439_g.field_70122_E) {
                  this.air = 0.0F;
               } else {
                  if (mc.field_71439_g.field_70124_G) {
                     this.stage = 0;
                  }

                  double var3 = 0.95D + (double)this.getSpeedEffect() * 0.2D - (double)(this.air / 25.0F);
                  if (var3 < this.defaultSpeed() - 0.05D) {
                     var3 = this.defaultSpeed() - 0.05D;
                  }

                  this.setMotion(var3 * 0.75D);
                  if (this.stage > 0) {
                     this.stage |= 1;
                  }

                  this.air += var2;
               }
            }

            if (this.mode.getValString().equalsIgnoreCase("AAC4")) {
               if (mc.field_71439_g.field_70122_E) {
                  mc.field_71439_g.func_70664_aZ();
                  var10000 = mc.field_71439_g;
                  var10000.field_70181_x += 0.2D;
                  this.speed = 0.5972999999999999D;
               } else {
                  var10000 = mc.field_71439_g;
                  var10000.field_70181_x += 0.03D;
                  this.speed *= 0.99D;
               }

               this.setMotion(this.speed);
               if (!mc.field_71439_g.field_70122_E) {
                  this.delay2 |= 1;
               }
            }

            if (this.mode.getValString().equalsIgnoreCase("Mineplex")) {
               if (mc.field_71439_g.field_70122_E) {
                  mc.field_71439_g.func_70664_aZ();
                  var10000 = mc.field_71439_g;
                  var10000.field_70181_x += 0.1D;
                  this.speed = 0.65D;
               } else {
                  var10000 = mc.field_71439_g;
                  var10000.field_70181_x += 0.03D;
                  this.speed *= 0.992D;
               }

               if (!mc.field_71474_y.field_74370_x.field_74513_e && !mc.field_71474_y.field_74366_z.field_74513_e) {
                  this.setMotion(this.speed);
               } else {
                  this.setMotion(this.speed * 0.7D);
               }

               if (mc.field_71439_g.field_70122_E) {
                  this.setMotion(0.0D);
               }
            }

            if (this.mode.getValString().equalsIgnoreCase("legit")) {
               if (Utils.isMoving(mc.field_71439_g)) {
                  if (mc.field_71439_g.field_70122_E) {
                     mc.field_71439_g.field_70181_x = mc.field_71439_g.field_70181_x = 0.41D;
                  }
               } else {
                  mc.field_71439_g.field_70159_w = 0.0D;
                  mc.field_71439_g.field_70179_y = 0.0D;
               }
            }

            if (this.mode.getValString().equalsIgnoreCase("long")) {
               double forward = (double)mc.field_71439_g.field_71158_b.field_192832_b;
               double strafe = (double)mc.field_71439_g.field_71158_b.field_78902_a;
               float yaw = mc.field_71439_g.field_70177_z;
               if (forward == 0.0D && strafe == 0.0D) {
                  mc.field_71439_g.field_70159_w = 0.0D;
                  mc.field_71439_g.field_70179_y = 0.0D;
               }

               if (forward != 0.0D && strafe != 0.0D) {
                  forward *= Math.sin(0.7853981633974483D);
                  strafe *= Math.cos(0.7853981633974483D);
               }

               double motionY;
               if (this.level != 1 || mc.field_71439_g.field_191988_bg == 0.0F && mc.field_71439_g.field_70702_br == 0.0F) {
                  if (this.level == 2) {
                     ++this.level;
                     motionY = 0.40123128D;
                     if ((mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) && mc.field_71439_g.field_70122_E) {
                        if (mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                           motionY += (double)((float)(((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76430_j))).func_76458_c() + 1) * 0.1F);
                        }

                        mc.field_71439_g.field_70181_x = mc.field_71439_g.field_70181_x = motionY;
                        this.moveSpeed *= 2.149D;
                     }
                  } else if (this.level == 3) {
                     ++this.level;
                     motionY = 0.763D * (this.lastDist - this.getBaseMoveSpeed());
                     this.moveSpeed = this.lastDist - motionY;
                  } else {
                     if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, mc.field_71439_g.field_70181_x, 0.0D)).size() > 0 || mc.field_71439_g.field_70124_G) {
                        this.level = 1;
                     }

                     this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                  }
               } else {
                  this.level = 2;
                  motionY = mc.field_71439_g.func_70644_a(MobEffects.field_76424_c) ? this.boostval.getValDouble() : this.boostval.getValDouble() + 1.1D;
                  this.moveSpeed = motionY * this.getBaseMoveSpeed() - 0.01D;
               }

               this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
               motionY = -Math.sin(Math.toRadians((double)yaw));
               double mz = Math.cos(Math.toRadians((double)yaw));
               mc.field_71439_g.field_70159_w = forward * this.moveSpeed * motionY + strafe * this.moveSpeed * mz;
               mc.field_71439_g.field_70179_y = forward * this.moveSpeed * mz - strafe * this.moveSpeed * motionY;
            }

         }
      }
   }

   public static void Movemulti(double moveSpeed) {
      float forward = mc.field_71439_g.field_191988_bg;
      float strafe = mc.field_71439_g.field_70702_br;
      float yaw = mc.field_71439_g.field_70177_z;
      if ((double)forward == 0.0D && (double)strafe == 0.0D) {
         mc.field_71439_g.field_70159_w = 0.0D;
         mc.field_71439_g.field_70179_y = 0.0D;
      }

      int d = 45;
      if ((double)forward != 0.0D) {
         if ((double)strafe > 0.0D) {
            yaw += (float)((double)forward > 0.0D ? -d : d);
         } else if ((double)strafe < 0.0D) {
            yaw += (float)((double)forward > 0.0D ? d : -d);
         }

         strafe = 0.0F;
         if ((double)forward > 0.0D) {
            forward = 1.0F;
         } else if ((double)forward < 0.0D) {
            forward = -1.0F;
         }
      }

      double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double xDist = (double)forward * moveSpeed * cos + (double)strafe * moveSpeed * sin;
      double zDist = (double)forward * moveSpeed * sin - (double)strafe * moveSpeed * cos;
      mc.field_71439_g.field_70159_w = xDist;
      mc.field_71439_g.field_70179_y = zDist;
   }

   public void setMotion(double var1) {
      double var3 = (double)mc.field_71439_g.field_71158_b.field_192832_b;
      double var5 = (double)mc.field_71439_g.field_71158_b.field_78902_a;
      float var7 = mc.field_71439_g.field_70177_z;
      if (this.mode.getValString().equalsIgnoreCase("aac4")) {
         var5 = 0.0D;
         var7 = 0.0F;
      }

      if (var3 == 0.0D && var5 == 0.0D) {
         mc.field_71439_g.field_70159_w = 0.0D;
         mc.field_71439_g.field_70179_y = 0.0D;
      } else {
         if (var3 != 0.0D) {
            if (var5 > 0.0D) {
               var7 += (float)(var3 > 0.0D ? -45 : 45);
            } else if (var5 < 0.0D) {
               var7 += (float)(var3 > 0.0D ? 45 : -45);
            }

            var5 = 0.0D;
            if (var3 > 0.0D) {
               var3 = 1.0D;
            } else if (var3 < 0.0D) {
               var3 = -1.0D;
            }
         }

         mc.field_71439_g.field_70159_w = var3 * var1 * Math.cos(Math.toRadians((double)(var7 + 90.0F))) + var5 * var1 * Math.sin(Math.toRadians((double)(var7 + 90.0F)));
         mc.field_71439_g.field_70179_y = var3 * var1 * Math.sin(Math.toRadians((double)(var7 + 90.0F))) - var5 * var1 * Math.cos(Math.toRadians((double)(var7 + 90.0F)));
      }

   }

   public int getSpeedEffect() {
      return mc.field_71439_g.func_70644_a(MobEffects.field_76424_c) ? ((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c() | 1 : 0;
   }

   public double defaultSpeed() {
      double var1 = 0.2873D;
      if (mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
         int var3 = ((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c();
         var1 *= 1.0D + 0.2D * (double)(var3 | 1);
      }

      return var1;
   }

   private double getBaseMoveSpeed() {
      double n = 0.2873D;
      if (mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
         n *= 1.0D + 0.2D * (double)(((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76424_c))).func_76458_c() + 1);
      }

      return n;
   }
}
