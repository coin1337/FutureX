package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ElytraFly extends Module {
   Setting mode;
   Setting speed;
   Setting autoStart;
   Setting disableInLiquid;
   Setting infiniteDurability;
   Setting Flatfly;
   Setting BuildupTicks;
   Setting noKick;
   Setting RandomFlap;
   Setting Yboost;
   Setting Flatboost;
   Setting Fallspeedboost;
   Setting Fallspeed;
   Setting StopPitch;
   Setting Speedme;
   Setting upspeed;
   Setting SpeedMulti;
   Setting AutoRocket;
   Setting RocketTicks;
   Setting Lockmove;
   Setting AfterBurner;
   Setting TicksAfter;
   double yposperson;
   double Lazyticks;
   EntityFireworkRocket wasBoosted;
   private final TimerUtils timer;
   private final TimerUtils Fireworktimer1;
   private final TimerUtils Fireworktimer2;

   public ElytraFly() {
      super("ElytraFly", 0, Category.MOVEMENT, "Elytra Fly");
      this.mode = Main.setmgr.add(new Setting("mode", this, "Control", new String[]{"BYPASS", "Control", "Boost", "Try1", "Legit+", "Mouse", "Rocket"}));
      this.speed = Main.setmgr.add(new Setting("speed", this, 1.0D, 0.0D, 5.0D, false));
      this.autoStart = Main.setmgr.add(new Setting("autoStart", this, false));
      this.disableInLiquid = Main.setmgr.add(new Setting("disableInLiquid", this, false));
      this.infiniteDurability = Main.setmgr.add(new Setting("Packet Durability", this, false));
      this.Flatfly = Main.setmgr.add(new Setting("Flatfly", this, false, this.mode, "Control", 6));
      this.BuildupTicks = Main.setmgr.add(new Setting("BuildupTicks", this, 0.0D, 0.0D, 200.0D, false, this.mode, "Control", 7));
      this.noKick = Main.setmgr.add(new Setting("noKick", this, false, this.mode, "Control", 8));
      this.RandomFlap = Main.setmgr.add(new Setting("RandomFlap", this, false));
      this.Yboost = Main.setmgr.add(new Setting("Yboost", this, false, this.mode, "Boost", 8));
      this.Flatboost = Main.setmgr.add(new Setting("Flatboost", this, false, this.mode, "Boost", 8));
      this.Fallspeedboost = Main.setmgr.add(new Setting("FlatFall% ", this, 99.95D, 10.0D, 99.95D, false, this.mode, "Boost", 7));
      this.Fallspeed = Main.setmgr.add(new Setting("Fall multiplier", this, 99.95D, 10.0D, 99.95D, false, this.mode, "Control", 7));
      this.StopPitch = Main.setmgr.add(new Setting("StopPitch", this, 0.0D, 0.0D, 90.0D, false, this.mode, "Control", 7));
      this.Speedme = Main.setmgr.add(new Setting("Speed Weird", this, 1.0D, 0.01D, 3.0D, false, this.mode, "Try1", 6));
      this.upspeed = Main.setmgr.add(new Setting("upspeed", this, 1.0D, 0.0D, 3.0D, false, this.mode, "Mouse", 6));
      this.SpeedMulti = Main.setmgr.add(new Setting("Burner Speed", this, 2.0D, 0.0D, 10.0D, false, this.mode, "Rocket", 7));
      this.AutoRocket = Main.setmgr.add(new Setting("AutoRocket", this, false, this.mode, "Rocket", 6));
      this.RocketTicks = Main.setmgr.add(new Setting("RocketTicks ", this, 22.0D, 0.0D, 100.0D, false, this.mode, "Rocket", 7));
      this.Lockmove = Main.setmgr.add(new Setting("Lockmove", this, false, this.mode, "Rocket", 6));
      this.AfterBurner = Main.setmgr.add(new Setting("After Burner", this, false, this.mode, "Rocket", 6));
      this.TicksAfter = Main.setmgr.add(new Setting("Burner Ticks", this, 22.0D, 0.0D, 60.0D, false, this.mode, "Rocket", 7));
      this.Lazyticks = 0.0D;
      this.wasBoosted = null;
      this.timer = new TimerUtils();
      this.Fireworktimer1 = new TimerUtils();
      this.Fireworktimer2 = new TimerUtils();
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71439_g.func_184582_a(EntityEquipmentSlot.CHEST).func_77973_b() == Items.field_185160_cR) {
         if (this.disableInLiquid.getValBoolean() && (mc.field_71439_g.func_70090_H() || mc.field_71439_g.func_180799_ab())) {
            if (mc.field_71439_g.func_184613_cA()) {
               ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_FALL_FLYING));
            }

         } else {
            if (this.autoStart.getValBoolean() && !mc.field_71439_g.func_184613_cA()) {
               if (mc.field_71439_g.field_70163_u + 0.02D < mc.field_71439_g.field_70137_T && !mc.field_71439_g.field_70122_E) {
                  mc.field_71439_g.field_70163_u = this.yposperson;
                  ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_FALL_FLYING));
                  Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
               } else {
                  this.yposperson = mc.field_71439_g.field_70163_u;
               }
            }

            double[] directionSpeedPacket;
            if (mc.field_71439_g.func_184613_cA()) {
               if (this.RandomFlap.getValBoolean()) {
                  mc.field_71439_g.field_184835_a = (float)Math.random();
                  mc.field_71439_g.field_184836_b = (float)Math.random();
                  mc.field_71439_g.field_184837_c = (float)Math.random();
               }

               EntityPlayerSP var10000;
               if (this.mode.getValString().equalsIgnoreCase("Legit+")) {
                  var10000 = mc.field_71439_g;
                  var10000.field_70747_aH *= 1.01222F;
                  mc.field_71439_g.field_70143_R = 0.0F;
                  var10000 = mc.field_71439_g;
                  var10000.field_70702_br += 0.1F;
                  mc.field_71439_g.field_70133_I = true;
                  if (mc.field_71439_g.field_70726_aT > 88.0F) {
                     mc.field_71439_g.func_70024_g(0.1D, 0.0D, 0.1D);
                  }

                  directionSpeedPacket = Utils.directionSpeed(0.02D);
                  if (mc.field_71439_g.field_70726_aT < 0.0F) {
                     mc.field_71439_g.func_70024_g(directionSpeedPacket[0], -0.001D, directionSpeedPacket[1]);
                  }
               }

               float speedScaled;
               if (this.mode.getValString().equalsIgnoreCase("Rocket")) {
                  mc.field_71439_g.func_70097_a(DamageSource.field_191552_t, 10.0F);
                  if (this.wasBoosted != null) {
                     speedScaled = (float)(this.speed.getValDouble() * 0.05000000074505806D) * 10.0F;
                     if (this.Fireworktimer1.isDelay((long)(this.TicksAfter.getValDouble() * 100.0D)) && this.AfterBurner.getValBoolean()) {
                        this.wasBoosted = null;
                        speedScaled = (float)((double)speedScaled * this.SpeedMulti.getValDouble());
                        this.Fireworktimer1.setLastMS();
                     } else if (this.Lockmove.getValBoolean()) {
                        this.freezePlayer(mc.field_71439_g);
                     }

                     double[] directionSpeedVanilla = Utils.directionSpeed((double)speedScaled);
                     if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
                        var10000 = mc.field_71439_g;
                        var10000.field_70159_w += directionSpeedVanilla[0];
                        var10000 = mc.field_71439_g;
                        var10000.field_70179_y += directionSpeedVanilla[1];
                     }
                  } else {
                     if (this.AutoRocket.getValBoolean() && this.Fireworktimer2.isDelay((long)this.RocketTicks.getValDouble() * 100L)) {
                        if (mc.field_71439_g.func_184614_ca().field_151002_e == Items.field_151152_bP || mc.field_71439_g.func_184592_cb().field_151002_e == Items.field_151152_bP) {
                           mc.func_147121_ag();
                        }

                        this.Fireworktimer2.setLastMS();
                     }

                     if (this.AfterBurner.getValBoolean()) {
                        Iterator var6 = mc.field_71441_e.field_72996_f.iterator();

                        while(var6.hasNext()) {
                           Entity entity = (Entity)var6.next();
                           if (entity instanceof EntityFireworkRocket) {
                              EntityFireworkRocket e = (EntityFireworkRocket)entity;
                              if (e.field_191513_e == mc.field_71439_g && !e.func_184202_aL()) {
                                 e.func_184195_f(true);
                                 this.wasBoosted = e;
                                 this.Fireworktimer1.setLastMS();
                                 break;
                              }
                           }
                        }
                     }
                  }
               }

               float y;
               if (this.mode.getValString().equalsIgnoreCase("Boost")) {
                  speedScaled = mc.field_71439_g.field_70125_A;
                  y = (float)(this.speed.getValDouble() * 0.05000000074505806D);
                  double[] directionSpeedVanilla = Utils.directionSpeed((double)y);
                  if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
                     var10000 = mc.field_71439_g;
                     var10000.field_70159_w += directionSpeedVanilla[0];
                     var10000 = mc.field_71439_g;
                     var10000.field_70179_y += directionSpeedVanilla[1];
                  }

                  var10000 = mc.field_71439_g;
                  var10000.field_70181_x += this.Yboost.getValBoolean() ? Math.sin(Math.toRadians((double)speedScaled)) * 0.05D : 0.0D;
                  if (this.Flatboost.getValBoolean()) {
                     mc.field_71439_g.field_70181_x = 0.03D * Math.cos(3.141592653589793D * (double)Math.abs(mc.field_71439_g.field_70125_A) / 90.0D + 3.141592653589793D) + 0.05D * (this.Fallspeedboost.getValDouble() / 100.0D);
                  }

                  if (mc.field_71474_y.field_74314_A.func_151470_d()) {
                     var10000 = mc.field_71439_g;
                     var10000.field_70181_x += 0.05D;
                  }

                  if (mc.field_71474_y.field_74311_E.func_151470_d()) {
                     var10000 = mc.field_71439_g;
                     var10000.field_70181_x -= 0.05D;
                  }
               }

               if (this.mode.getValString().equalsIgnoreCase("Try1") && (mc.field_71439_g.field_70181_x < 0.0D && mc.field_71439_g.field_70160_al || mc.field_71439_g.field_70122_E)) {
                  mc.field_71439_g.field_70181_x = -0.125D;
                  var10000 = mc.field_71439_g;
                  var10000.field_70747_aH = (float)((double)var10000.field_70747_aH * (1.0122699737548828D + this.Speedme.getValDouble()));
                  mc.field_71439_g.field_70145_X = true;
                  mc.field_71439_g.field_70143_R = 0.0F;
                  mc.field_71439_g.field_70122_E = true;
                  var10000 = mc.field_71439_g;
                  var10000.field_70702_br = (float)((double)var10000.field_70702_br + 0.800000011920929D + this.Speedme.getValDouble());
                  var10000 = mc.field_71439_g;
                  var10000.field_70747_aH = (float)((double)var10000.field_70747_aH + 0.20000000298023224D + this.Speedme.getValDouble());
                  mc.field_71439_g.field_70133_I = true;
                  if (mc.field_71439_g.field_70173_aa % 8 == 0 && mc.field_71439_g.field_70163_u <= 240.0D) {
                     mc.field_71439_g.field_70181_x = 0.019999999552965164D;
                  }
               }

               if (this.mode.getValString().equalsIgnoreCase("Control")) {
                  this.runNoKick();
                  directionSpeedPacket = Utils.directionSpeed(this.speed.getValDouble() / Math.max(this.Lazyticks, 1.0D));
                  if (mc.field_71439_g.field_71158_b.field_78902_a == 0.0F && mc.field_71439_g.field_71158_b.field_192832_b == 0.0F) {
                     this.Lazyticks = 0.0D;
                     this.freezePlayer(mc.field_71439_g);
                     mc.field_71439_g.field_70181_x = 0.03D * Math.cos(3.141592653589793D * (double)Math.abs(mc.field_71439_g.field_70125_A) / 90.0D + 3.141592653589793D) + 0.05D * (this.Fallspeed.getValDouble() / 100.0D);
                     if (mc.field_71439_g.field_71158_b.field_78899_d) {
                        mc.field_71439_g.field_70181_x = -this.speed.getValDouble();
                     }
                  } else if ((double)mc.field_71439_g.field_70125_A > this.StopPitch.getValDouble() - 0.01D) {
                     if (this.BuildupTicks.getValDouble() > 0.0D) {
                        if (this.Lazyticks == 0.0D) {
                           this.Lazyticks = this.BuildupTicks.getValDouble();
                        }

                        if (this.Lazyticks > 1.0D) {
                           this.Lazyticks /= 1.03D;
                        }
                     }

                     this.freezePlayer(mc.field_71439_g);
                     mc.field_71439_g.field_70181_x = 0.03D * Math.cos(3.141592653589793D * (double)Math.abs(mc.field_71439_g.field_70125_A) / 90.0D + 3.141592653589793D) + 0.05D * (this.Fallspeed.getValDouble() / 100.0D);
                     if (mc.field_71439_g.field_71158_b.field_78899_d) {
                        mc.field_71439_g.field_70181_x = -this.speed.getValDouble();
                     }

                     if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F || mc.field_71439_g.field_71158_b.field_192832_b != 0.0F) {
                        mc.field_71439_g.field_70159_w = directionSpeedPacket[0];
                        mc.field_71439_g.field_70179_y = directionSpeedPacket[1];
                     }
                  } else if (mc.field_71439_g.field_70163_u < mc.field_71439_g.field_70137_T && this.timer.isDelay(100L)) {
                     mc.field_71439_g.field_70125_A = 0.0F;
                     this.timer.setLastMS();
                  }
               }

               if (this.mode.getValString().equalsIgnoreCase("Mouse")) {
                  directionSpeedPacket = Utils.directionSpeed(this.speed.getValDouble());
                  if (Module.mc.field_71439_g.field_71158_b.field_78902_a == 0.0F && Module.mc.field_71439_g.field_71158_b.field_192832_b == 0.0F) {
                     Module.mc.field_71439_g.field_70159_w = 0.0D;
                     Module.mc.field_71439_g.field_70179_y = 0.0D;
                  } else {
                     Module.mc.field_71439_g.field_70159_w = directionSpeedPacket[0];
                     Module.mc.field_71439_g.field_70179_y = directionSpeedPacket[1];
                     var10000 = mc.field_71439_g;
                     var10000.field_70159_w -= Module.mc.field_71439_g.field_70159_w * (double)(Math.abs(Module.mc.field_71439_g.field_70125_A) + 90.0F) / 90.0D - Module.mc.field_71439_g.field_70159_w;
                     var10000 = mc.field_71439_g;
                     var10000.field_70179_y -= Module.mc.field_71439_g.field_70179_y * (double)(Math.abs(Module.mc.field_71439_g.field_70125_A) + 90.0F) / 90.0D - Module.mc.field_71439_g.field_70179_y;
                  }

                  y = 0.0F;
                  if (mc.field_71474_y.field_74314_A.func_151470_d()) {
                     y = (float)this.upspeed.getValDouble();
                  }

                  double Ymove = (double)y + -this.degToRad((double)Module.mc.field_71439_g.field_70125_A) * (double)Module.mc.field_71439_g.field_71158_b.field_192832_b;
                  if (this.upspeed.getValDouble() == 0.0D && Ymove > 0.0D) {
                     Ymove = 0.0D;
                  }

                  Module.mc.field_71439_g.field_70181_x = Ymove;
               }
            }

            if (this.mode.getValString().equalsIgnoreCase("BYPASS")) {
               if (mc.field_71474_y.field_74314_A.func_151470_d()) {
                  mc.field_71439_g.field_70181_x = 0.019999999552965164D;
               }

               if (mc.field_71474_y.field_74311_E.func_151470_d()) {
                  mc.field_71439_g.field_70181_x = -0.20000000298023224D;
               }

               if (mc.field_71439_g.field_70173_aa % 8 == 0 && mc.field_71439_g.field_70163_u <= 240.0D) {
                  mc.field_71439_g.field_70181_x = 0.019999999552965164D;
               }

               mc.field_71439_g.field_71075_bZ.field_75100_b = true;
               mc.field_71439_g.field_71075_bZ.func_75092_a(0.025F);
               directionSpeedPacket = Utils.directionSpeed(0.5199999809265137D);
               if (mc.field_71439_g.field_71158_b.field_78902_a == 0.0F && mc.field_71439_g.field_71158_b.field_192832_b == 0.0F) {
                  mc.field_71439_g.field_70159_w = 0.0D;
                  mc.field_71439_g.field_70179_y = 0.0D;
               } else {
                  mc.field_71439_g.field_70159_w = directionSpeedPacket[0];
                  mc.field_71439_g.field_70179_y = directionSpeedPacket[1];
               }
            }

            if (this.infiniteDurability.getValBoolean()) {
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_FALL_FLYING));
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_FALL_FLYING));
            }

         }
      }
   }

   public double degToRad(double deg) {
      return deg * 0.01745329238474369D;
   }

   private void runNoKick() {
      if (this.noKick.getValBoolean() && !mc.field_71439_g.func_184613_cA() && mc.field_71439_g.field_70173_aa % 4 == 0) {
         mc.field_71439_g.field_70181_x = -0.03999999910593033D;
      }

   }

   private void freezePlayer(EntityPlayer player) {
      player.field_70159_w = 0.0D;
      player.field_70181_x = 0.0D;
      player.field_70179_y = 0.0D;
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof PositionRotation && (double)mc.field_71439_g.field_70125_A > -this.StopPitch.getValDouble() && this.mode.getValString().equalsIgnoreCase("Control") && mc.field_71439_g.func_184613_cA() && this.Flatfly.getValBoolean()) {
         PositionRotation rotation = (PositionRotation)packet;
         ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new Position(rotation.field_149479_a, rotation.field_149477_b, rotation.field_149478_c, rotation.field_149474_g));
         return false;
      } else {
         return true;
      }
   }

   public void onDisable() {
      if (mc.field_71439_g != null) {
         mc.field_71439_g.field_71075_bZ.field_75100_b = false;
      }

   }
}
