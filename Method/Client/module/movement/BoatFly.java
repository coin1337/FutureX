package Method.Client.module.movement;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import java.util.Objects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemBoat;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketPlayerPosLook.EnumFlags;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class BoatFly extends Module {
   Setting Gravity;
   Setting AllEntities;
   Setting mode;
   Setting FakePackets;
   Setting BoatClip;
   Setting BoatClipSpeed;
   Setting bypass;
   Setting Tickdelay;
   Setting UpYmotion;
   Setting DownYmotion;
   Setting PlaceBypass;
   Setting ComplexMotion;
   Setting ignoreVehicleMove;
   Setting NoKick;
   Setting ignorePlayerPosRot;
   Setting Fakerotdist;
   Setting Ncptoggle;
   Setting PacketJump;
   public static Setting BoatRender;
   public static Setting Boatblend;
   double FakerotX;
   double FakerotZ;
   boolean aBoolean;
   String updatetexture;
   int tpId;
   private int PacketLazyTimer;
   int ClipLazyTimer;
   public static ResourceLocation[] BOAT_TEXTURES = new ResourceLocation[]{new ResourceLocation("textures/entity/boat/boat_oak.png"), new ResourceLocation("textures/entity/boat/boat_spruce.png"), new ResourceLocation("textures/entity/boat/boat_birch.png"), new ResourceLocation("textures/entity/boat/boat_jungle.png"), new ResourceLocation("textures/entity/boat/boat_acacia.png"), new ResourceLocation("textures/entity/boat/boat_darkoak.png")};

   public BoatFly() {
      super("BoatFly", 0, Category.MOVEMENT, "Boat Fly");
      this.Gravity = Main.setmgr.add(new Setting("Gravity", this, true));
      this.AllEntities = Main.setmgr.add(new Setting("All Entities", this, true));
      this.mode = Main.setmgr.add(new Setting("Boat Mode", this, "Vanilla", new String[]{"Vanilla", "Fast", "Packet"}));
      this.FakePackets = Main.setmgr.add(new Setting("Fake Packet Spam", this, false));
      this.BoatClip = Main.setmgr.add(new Setting("BoatClip", this, "None", new String[]{"None", "Vanilla", "Fast"}));
      this.BoatClipSpeed = Main.setmgr.add(new Setting("BoatClipSpeed", this, 1.0D, 0.5D, 5.0D, false, this.BoatClip, 13));
      this.bypass = Main.setmgr.add(new Setting("bypass Mode", this, "None", new String[]{"Packet", "Vanilla", "None"}));
      this.Tickdelay = Main.setmgr.add(new Setting("Tickdelay", this, 1.0D, 0.0D, 20.0D, true, this.bypass, "Packet", 13));
      this.UpYmotion = Main.setmgr.add(new Setting("UpYmotion", this, 0.2D, 0.10000000149011612D, 2.0D, false));
      this.DownYmotion = Main.setmgr.add(new Setting("Fallmotion", this, 0.1D, 0.0D, 2.0D, false));
      this.PlaceBypass = Main.setmgr.add(new Setting("PlaceBypass", this, true));
      this.ComplexMotion = Main.setmgr.add(new Setting("Complex Y Motion", this, true));
      this.ignoreVehicleMove = Main.setmgr.add(new Setting("No Boat Motion", this, false));
      this.NoKick = Main.setmgr.add(new Setting("NoKick", this, false));
      this.ignorePlayerPosRot = Main.setmgr.add(new Setting("No Player Rotation", this, false));
      this.Fakerotdist = Main.setmgr.add(new Setting("Fakerotdist", this, 1.0D, 0.5D, 10.0D, false, this.ignorePlayerPosRot, 14));
      this.Ncptoggle = Main.setmgr.add(new Setting("Ncptoggle", this, true, this.bypass, "Packet", 13));
      this.PacketJump = Main.setmgr.add(new Setting("PacketJump", this, false));
      this.aBoolean = false;
      this.updatetexture = "NULL";
      this.tpId = 0;
      this.ClipLazyTimer = 0;
   }

   public void setup() {
      Main.setmgr.add(BoatRender = new Setting("Render", this, "Defualt", new String[]{"Defualt", "Vanish", "Rainbow", "Carpet"}));
      Main.setmgr.add(Boatblend = new Setting("Boatblend", this, false));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.OUT && this.PlaceBypass.getValBoolean() && (packet instanceof CPacketPlayerTryUseItemOnBlock && mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBoat || mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBoat)) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
         return false;
      } else {
         if (mc.field_71439_g.func_184187_bx() instanceof EntityBoat || this.AllEntities.getValBoolean() && mc.field_71439_g.func_184187_bx() != null) {
            Entity e = mc.field_71439_g.func_184187_bx();
            if (e == null) {
               return true;
            }

            if (side == Connection.Side.OUT) {
               if (this.mode.getValString().equalsIgnoreCase("Packet") && !(packet instanceof CPacketVehicleMove) && !(packet instanceof CPacketSteerBoat) && !(packet instanceof CPacketPlayer) && packet instanceof CPacketEntityAction) {
                  Action Getaction = ((CPacketEntityAction)packet).func_180764_b();
                  if (Getaction != Action.OPEN_INVENTORY) {
                     return false;
                  }
               }

               if (this.bypass.getValString().equalsIgnoreCase("Packet") && mc.field_71439_g != null) {
                  if (this.Ncptoggle.getValBoolean()) {
                     if (packet instanceof CPacketInput && !mc.field_71474_y.field_151444_V.func_151470_d() && !mc.field_71439_g.func_184187_bx().field_70122_E) {
                        ++this.PacketLazyTimer;
                        if ((double)this.PacketLazyTimer > this.Tickdelay.getValDouble()) {
                           this.PacketLazyTimer = 0;
                           mc.field_71439_g.field_71174_a.func_147297_a(new CPacketUseEntity(e, EnumHand.MAIN_HAND));
                        }
                     }
                  } else if (packet instanceof CPacketVehicleMove && (double)(this.PacketLazyTimer++) >= this.Tickdelay.getValDouble()) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketUseEntity(e, EnumHand.MAIN_HAND));
                     this.PacketLazyTimer = 0;
                  } else if (packet instanceof Rotation || packet instanceof CPacketInput) {
                     return false;
                  }
               }

               if (this.ignorePlayerPosRot.getValBoolean() && mc.field_71439_g.field_70173_aa % 5 == 0) {
                  MC.field_71439_g.field_71174_a.func_147297_a(new CPacketUseEntity(mc.field_71439_g.func_184187_bx(), EnumHand.OFF_HAND));
                  MC.field_71439_g.field_71174_a.func_147297_a(new CPacketVehicleMove(mc.field_71439_g.func_184187_bx()));
                  MC.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, MC.field_71439_g.field_70122_E));
                  MC.field_71439_g.field_71174_a.func_147297_a(new CPacketConfirmTeleport(this.tpId));
               }

               if (this.bypass.getValString().equalsIgnoreCase("Vanilla")) {
                  if (packet instanceof CPacketVehicleMove) {
                     CPacketVehicleMove cPacketVehicleMove = (CPacketVehicleMove)packet;
                     cPacketVehicleMove.field_187011_e = mc.field_71439_g.func_184187_bx().field_70127_C;
                     cPacketVehicleMove.field_187010_d = mc.field_71439_g.func_184187_bx().field_70126_B;
                     cPacketVehicleMove.field_187007_a = Double.parseDouble((String)null);
                     cPacketVehicleMove.field_187008_b = Double.parseDouble((String)null);
                     cPacketVehicleMove.field_187009_c = Double.parseDouble((String)null);
                  }

                  if (packet instanceof CPacketSteerBoat) {
                     mc.field_71439_g.field_70181_x = 0.0D;
                     e.field_70160_al = false;
                     return false;
                  }
               }
            }

            if (side == Connection.Side.IN) {
               if (this.ignoreVehicleMove.getValBoolean()) {
                  Entity entity;
                  if (packet instanceof SPacketEntityVelocity) {
                     entity = mc.field_71441_e.func_73045_a(((SPacketEntityVelocity)packet).func_149412_c());
                     if (entity == mc.field_71439_g || entity == mc.field_71439_g.func_184187_bx()) {
                        return false;
                     }
                  }

                  if (packet instanceof SPacketEntity) {
                     entity = ((SPacketEntity)packet).func_149065_a(mc.field_71441_e);
                     if (entity == mc.field_71439_g || entity == mc.field_71439_g.func_184187_bx()) {
                        return false;
                     }
                  }

                  if (packet instanceof SPacketEntityHeadLook) {
                     entity = ((SPacketEntityHeadLook)packet).func_149381_a(mc.field_71441_e);
                     if (entity == mc.field_71439_g || entity == mc.field_71439_g.func_184187_bx()) {
                        return false;
                     }
                  }

                  if (packet instanceof SPacketEntityTeleport) {
                     entity = mc.field_71441_e.func_73045_a(((SPacketEntityTeleport)packet).func_149451_c());
                     if (entity == mc.field_71439_g || entity == mc.field_71439_g.func_184187_bx()) {
                        return false;
                     }
                  }

                  if (packet instanceof SPacketMoveVehicle && mc.field_71439_g.func_184187_bx() instanceof EntityBoat) {
                     return false;
                  }
               }

               if (packet instanceof SPacketPlayerPosLook && this.ignorePlayerPosRot.getValBoolean()) {
                  SPacketPlayerPosLook pp = (SPacketPlayerPosLook)packet;
                  this.tpId = pp.func_186965_f();
                  double d = Math.sqrt(Math.pow(this.FakerotX - pp.func_148932_c(), 2.0D) + Math.pow(this.FakerotZ - pp.func_148933_e(), 2.0D));
                  if (d >= this.Fakerotdist.getValDouble()) {
                     this.respondToPosLook(packet);
                     this.FakerotX = pp.func_148932_c();
                     this.FakerotZ = pp.func_148933_e();
                     return false;
                  }

                  if (mc.field_71439_g.func_184218_aH() && this.isBorderingChunk(mc.field_71439_g.func_184187_bx(), 0.0D, 0.0D)) {
                     this.respondToPosLook(packet);
                     this.FakerotX = pp.func_148932_c();
                     this.FakerotZ = pp.func_148933_e();
                     return false;
                  }

                  return false;
               }

               if (this.bypass.getValString().equalsIgnoreCase("Packet") && (packet instanceof SPacketMoveVehicle || packet instanceof SPacketPlayerPosLook)) {
                  return false;
               }

               if (this.mode.getValString().equalsIgnoreCase("Packet")) {
                  if (packet instanceof SPacketMoveVehicle) {
                     SPacketMoveVehicle VehicleMove = (SPacketMoveVehicle)packet;
                     return !(mc.field_71439_g.func_70011_f(VehicleMove.func_186957_a(), VehicleMove.func_186955_b(), VehicleMove.func_186956_c()) <= 15.0D);
                  }

                  if (packet instanceof SPacketSetPassengers) {
                     SPacketSetPassengers Setpassengers = (SPacketSetPassengers)packet;
                     if (Setpassengers.func_186972_b() == e.func_145782_y()) {
                        int[] passengerIds = Setpassengers.func_186971_a();
                        int[] var7 = passengerIds;
                        int var8 = passengerIds.length;

                        for(int var9 = 0; var9 < var8; ++var9) {
                           int i = var7[var9];
                           if (i != mc.field_71439_g.func_145782_y() && mc.field_71439_g.func_70089_S() && mc.field_71441_e.func_175668_a(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v), false) && !(mc.field_71462_r instanceof GuiDownloadTerrain) && !mc.field_71439_g.func_184218_aH()) {
                              return false;
                           }
                        }
                     }
                  }
               }
            }
         }

         return true;
      }
   }

   public void onDisable() {
      if (mc.field_71439_g.func_184187_bx() instanceof EntityBoat || this.AllEntities.getValBoolean() && mc.field_71439_g.func_184187_bx() != null) {
         if (this.Gravity.getValBoolean() && mc.field_71439_g.func_184187_bx().func_189652_ae()) {
            mc.field_71439_g.func_184187_bx().func_189654_d(false);
         }

         mc.field_71439_g.func_184187_bx().field_70145_X = false;
      }

   }

   public void onClientTick(ClientTickEvent event) {
      if (!BoatRender.getValString().equalsIgnoreCase(this.updatetexture)) {
         this.updatetexture = BoatRender.getValString();
         if (BoatRender.getValString().equalsIgnoreCase("Defualt")) {
            RenderBoat.field_110782_f = BOAT_TEXTURES;
         }

         if (BoatRender.getValString().equalsIgnoreCase("Carpet")) {
            RenderBoat.field_110782_f = new ResourceLocation[]{new ResourceLocation("futurex", "carpet.png"), new ResourceLocation("futurex", "carpet.png"), new ResourceLocation("futurex", "carpet.png"), new ResourceLocation("futurex", "carpet.png"), new ResourceLocation("futurex", "carpet.png"), new ResourceLocation("futurex", "carpet.png")};
         }
      }

      if (mc.field_71439_g.func_184187_bx() instanceof EntityBoat || this.AllEntities.getValBoolean() && mc.field_71439_g.func_184187_bx() != null) {
         Entity e = mc.field_71439_g.func_184187_bx();
         if (e == null) {
            return;
         }

         e.func_189654_d(this.Gravity.getValBoolean());
         e.field_70171_ac = true;
         e.field_70160_al = false;
         if (this.mode.getValString().equalsIgnoreCase("Fast")) {
            double[] directionSpeedVanilla = Utils.directionSpeed(0.20000000298023224D);
            e.field_70159_w = directionSpeedVanilla[0];
            e.field_70179_y = directionSpeedVanilla[1];
            mc.field_71439_g.field_70181_x = 0.0D;
            e.field_70181_x = 0.0D;
            mc.field_71439_g.field_71174_a.func_147297_a(new PositionRotation(e.field_70165_t + e.field_70159_w, e.field_70163_u, e.field_70161_v + e.field_70179_y, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false));
            e.field_70181_x = 0.0D;
            mc.field_71439_g.field_71174_a.func_147297_a(new PositionRotation(e.field_70165_t + e.field_70159_w, e.field_70163_u - 2.0D, e.field_70161_v + e.field_70179_y, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
            e.field_70163_u -= 0.2D;
         }

         if (this.ComplexMotion.getValBoolean()) {
            e.field_70181_x = mc.field_71474_y.field_151444_V.field_74513_e ? -this.UpYmotion.getValDouble() : (mc.field_71439_g.field_70173_aa % 2 != 0 ? -this.DownYmotion.getValDouble() / 10.0D : (mc.field_71474_y.field_74314_A.field_74513_e ? this.getUpyMotion() : this.DownYmotion.getValDouble() / 10.0D));
         } else if (mc.field_71474_y.field_74314_A.func_151470_d()) {
            e.field_70181_x += this.getUpyMotion() / 20.0D;
         } else if (mc.field_71474_y.field_151444_V.func_151470_d()) {
            e.field_70181_x -= this.DownYmotion.getValDouble() / 20.0D;
         } else {
            e.field_70181_x = 0.0D;
         }

         if (this.NoKick.getValBoolean()) {
            if (mc.field_71474_y.field_74314_A.func_151470_d()) {
               if (mc.field_71439_g.field_70173_aa % 8 < 2) {
                  mc.field_71439_g.func_184187_bx().field_70181_x = -0.03999999910593033D;
               }
            } else if (mc.field_71439_g.field_70173_aa % 8 < 4) {
               mc.field_71439_g.func_184187_bx().field_70181_x = -0.07999999821186066D;
            }
         }

         if (this.FakePackets.getValBoolean()) {
            this.FakePackets();
         }

         if (this.BoatClip.getValString().equalsIgnoreCase("Vanilla")) {
            e.field_70145_X = true;
            e.field_70122_E = false;
            e.field_70144_Y = 1.0F;
         }

         if (this.BoatClip.getValString().equalsIgnoreCase("Fast")) {
            this.Boatclip(e);
         }
      }

      super.onClientTick(event);
   }

   private double getUpyMotion() {
      if (this.PacketJump.getValBoolean()) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_RIDING_JUMP));
      }

      return this.UpYmotion.getValDouble();
   }

   private void FakePackets() {
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketVehicleMove());
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketSteerBoat(true, true));
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketVehicleMove());
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketSteerBoat(true, true));
   }

   public void respondToPosLook(Object packet) {
      if (mc.field_71441_e != null && mc.field_71439_g != null && packet instanceof SPacketPlayerPosLook && ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).field_147309_h) {
         SPacketPlayerPosLook packetIn = (SPacketPlayerPosLook)packet;
         double d0 = packetIn.func_148932_c();
         double d2 = packetIn.func_148933_e();
         if (packetIn.func_179834_f().contains(EnumFlags.X)) {
            d0 += mc.field_71439_g.field_70165_t;
         }

         if (packetIn.func_179834_f().contains(EnumFlags.Z)) {
            d2 += mc.field_71439_g.field_70161_v;
         }

         mc.func_147114_u().func_147297_a(new CPacketConfirmTeleport(packetIn.func_186965_f()));
         mc.func_147114_u().func_147297_a(new PositionRotation(d0, mc.field_71439_g.func_174813_aQ().field_72338_b, d2, packetIn.field_148936_d, packetIn.field_148937_e, false));
      }

   }

   private void Boatclip(Entity e) {
      CPacketVehicleMove packetVehicleMove = new CPacketVehicleMove(e);
      e.field_70122_E = false;
      if (mc.field_71474_y.field_74368_y.func_151470_d()) {
         packetVehicleMove.field_187008_b = -2.0D;
         if (mc.field_71439_g.field_70163_u > 0.0D) {
            EntityPlayerSP var10000 = mc.field_71439_g;
            var10000.field_70159_w -= this.getMotionX(mc.field_71439_g.field_70177_z);
            var10000 = mc.field_71439_g;
            var10000.field_70179_y -= this.getMotionZ(mc.field_71439_g.field_70177_z);
         }
      }

      this.Packet();
      this.aBoolean = !this.aBoolean;
      ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(packetVehicleMove);
      if (mc.field_71439_g.field_70163_u < 0.0D) {
         ++this.ClipLazyTimer;
      } else {
         this.ClipLazyTimer = 0;
      }

      if (this.ClipLazyTimer > 20 && mc.field_71439_g.field_70163_u < 0.0D) {
         this.ClipLazyTimer = 21;
         if (mc.field_71439_g.func_184218_aH()) {
            mc.field_71439_g.func_184210_p();
         }

         double oldMotionX = mc.field_71439_g.field_70159_w;
         double oldMotionY = mc.field_71439_g.field_70181_x;
         double oldMotionZ = mc.field_71439_g.field_70179_y;
         if ((mc.field_71474_y.field_74351_w.func_151470_d() || mc.field_71474_y.field_74370_x.func_151470_d() || mc.field_71474_y.field_74366_z.func_151470_d() || mc.field_71474_y.field_74368_y.func_151470_d()) && !mc.field_71474_y.field_74314_A.func_151470_d()) {
            mc.field_71439_g.field_70159_w = this.getMotionX(mc.field_71439_g.field_71109_bG) * 0.26D;
            mc.field_71439_g.field_70179_y = this.getMotionZ(mc.field_71439_g.field_71109_bG) * 0.26D;
         }

         this.Packet();
         mc.func_147114_u().func_147297_a(new PositionRotation(mc.field_71439_g.field_70165_t + mc.field_71439_g.field_70159_w, 3.0D + mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v + mc.field_71439_g.field_70179_y, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, true));
         mc.func_147114_u().func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_FALL_FLYING));
         mc.field_71439_g.field_70159_w = oldMotionX;
         mc.field_71439_g.field_70181_x = oldMotionY;
         mc.field_71439_g.field_70179_y = oldMotionZ;
      }

   }

   public boolean isBorderingChunk(Entity entity, double motX, double motZ) {
      return mc.field_71441_e.func_72964_e((int)(entity.field_70165_t + motX) >> 4, (int)(entity.field_70161_v + motZ) >> 4) instanceof EmptyChunk;
   }

   private void Packet() {
      ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147297_a(new PositionRotation(mc.field_71439_g.field_70165_t + mc.field_71439_g.field_70159_w, mc.field_71439_g.field_70163_u + (this.aBoolean ? 0.0625D : (mc.field_71474_y.field_74314_A.func_151470_d() ? 0.0624D : 1.0E-8D)) - (this.aBoolean ? 0.0625D : (mc.field_71474_y.field_74311_E.func_151470_d() ? 0.0624D : 2.0E-8D)), mc.field_71439_g.field_70161_v + mc.field_71439_g.field_70179_y, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, false));
   }

   private double getMotionX(float yaw) {
      return (double)MathHelper.func_76126_a(-yaw * 0.017453292F * 1.0F) * this.BoatClipSpeed.getValDouble();
   }

   private double getMotionZ(float yaw) {
      return (double)(MathHelper.func_76134_b(yaw * 0.017453292F) * 1.0F) * this.BoatClipSpeed.getValDouble();
   }
}
