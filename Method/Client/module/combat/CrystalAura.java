package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.FriendManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.RenderUtils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class CrystalAura extends Module {
   public Setting range;
   public Setting Explode;
   public Setting Placer;
   public Setting Damage;
   public Setting OtherDamage;
   public Setting HitDelayBetween;
   public Setting PlaceDelayBetween;
   public Setting FastSwitch;
   public Setting Packetreach;
   public Setting Hand;
   public Setting SpoofAngle;
   public Setting OverlayColor;
   public Setting Mode;
   public Setting LineWidth;
   public Setting HoleJiggle;
   public Setting MultiPlace;
   public Setting SwordHit;
   private final TimerUtils attackTimer;
   private final TimerUtils placeTimer;
   private final List<CrystalAura.PlaceLocation> placeLocations;
   private static ExecutorService executor;
   public float[] Rots;

   public CrystalAura() {
      super("CrystalAura", 0, Category.COMBAT, "CrystalAura");
      this.range = Main.setmgr.add(new Setting("Hit Range", this, 6.0D, 0.0D, 8.0D, false));
      this.Explode = Main.setmgr.add(new Setting("Explode", this, true));
      this.Placer = Main.setmgr.add(new Setting("Place Crystals", this, true));
      this.Damage = Main.setmgr.add(new Setting("Max Self Dmg", this, 14.0D, 0.0D, 20.0D, false));
      this.OtherDamage = Main.setmgr.add(new Setting("Min Enemy Dmg", this, 4.0D, 0.0D, 20.0D, false));
      this.HitDelayBetween = Main.setmgr.add(new Setting("Hit Delay", this, 0.2D, 0.0D, 1.0D, false));
      this.PlaceDelayBetween = Main.setmgr.add(new Setting("Place Delay", this, 0.2D, 0.0D, 1.0D, false));
      this.FastSwitch = Main.setmgr.add(new Setting("Fast Switch", this, true));
      this.Packetreach = Main.setmgr.add(new Setting("Packet reach", this, false));
      this.Hand = Main.setmgr.add(new Setting("Hand", this, "Mainhand", new String[]{"Mainhand", "Offhand", "Both", "None"}));
      this.SpoofAngle = Main.setmgr.add(new Setting("Spoof Angle", this, true));
      this.OverlayColor = Main.setmgr.add(new Setting("OverlayColor", this, 0.0D, 1.0D, 1.0D, 0.62D));
      this.Mode = Main.setmgr.add(new Setting("Render", this, "Outline", this.BlockEspOptions()));
      this.LineWidth = Main.setmgr.add(new Setting("LineWidth", this, 1.0D, 0.0D, 3.0D, false));
      this.HoleJiggle = Main.setmgr.add(new Setting("Hole Jiggle", this, true));
      this.MultiPlace = Main.setmgr.add(new Setting("MultiPlace", this, true));
      this.SwordHit = Main.setmgr.add(new Setting("SwordHit", this, false));
      this.attackTimer = new TimerUtils();
      this.placeTimer = new TimerUtils();
      this.placeLocations = new ArrayList();
   }

   public void setup() {
      executor = Executors.newSingleThreadExecutor();
   }

   public void onClientTick(ClientTickEvent event) {
      ArrayList<EntityEnderCrystal> Crystals = new ArrayList();
      Iterator var3 = mc.field_71441_e.field_72996_f.iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         if (entity instanceof EntityEnderCrystal) {
            Crystals.add((EntityEnderCrystal)entity);
         }
      }

      try {
         executor.execute(() -> {
            Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

            while(true) {
               EntityOtherPlayerMP playerMP;
               do {
                  do {
                     do {
                        Entity entity;
                        do {
                           if (!var2.hasNext()) {
                              return;
                           }

                           entity = (Entity)var2.next();
                        } while(!(entity instanceof EntityOtherPlayerMP));

                        playerMP = (EntityOtherPlayerMP)entity;
                     } while(FriendManager.isFriend(playerMP.func_70005_c_()));

                     if (this.Explode.getValBoolean()) {
                        if (this.MultiPlace.getValBoolean()) {
                           Collections.shuffle(Crystals);
                        }

                        Iterator var5 = Crystals.iterator();

                        while(var5.hasNext()) {
                           EntityEnderCrystal crystal = (EntityEnderCrystal)var5.next();
                           if (mc.field_71439_g.func_70685_l(crystal) && playerMP.func_70032_d(crystal) < 12.0F && (double)crystal.func_70032_d(mc.field_71439_g) <= this.range.getValDouble() && (double)Calculate_Damage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, mc.field_71439_g) < this.Damage.getValDouble() && (double)Calculate_Damage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, playerMP) > this.OtherDamage.getValDouble()) {
                              this.processAttack(crystal);
                           }
                        }
                     }
                  } while(!this.placeTimer.isDelay((long)(this.PlaceDelayBetween.getValDouble() * 1000.0D)));
               } while(!this.Placer.getValBoolean());

               ArrayList<BlockPos> posable = new ArrayList();
               if (mc.field_71439_g.func_70032_d(playerMP) < 13.0F) {
                  for(int x = (int)playerMP.field_70165_t - 8; x <= (int)playerMP.field_70165_t + 8; ++x) {
                     for(int z = (int)playerMP.field_70161_v - 8; z <= (int)playerMP.field_70161_v + 8; ++z) {
                        for(int y = (int)playerMP.field_70163_u - 4; y <= (int)playerMP.field_70163_u + 3; ++y) {
                           BlockPos blockPos = new BlockPos(x, y, z);
                           if (this.canPlaceCrystal(blockPos) && (double)Calculate_Damage((double)blockPos.field_177962_a + 0.5D, (double)(blockPos.field_177960_b + 1), (double)blockPos.field_177961_c + 0.5D, mc.field_71439_g) < this.Damage.getValDouble() && (double)Calculate_Damage((double)blockPos.field_177962_a + 0.5D, (double)(blockPos.field_177960_b + 1), (double)blockPos.field_177961_c + 0.5D, playerMP) > this.OtherDamage.getValDouble()) {
                              posable.add(blockPos);
                           }
                        }
                     }
                  }
               }

               this.placeCrystalOnBlock(posable, playerMP);
            }
         });
      } catch (ConcurrentModificationException var5) {
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (side == Connection.Side.IN && packet instanceof SPacketSpawnObject) {
         SPacketSpawnObject packetSpawnObject = (SPacketSpawnObject)packet;
         if (packetSpawnObject.func_148993_l() == 51) {
            Iterator var4 = this.placeLocations.iterator();

            while(var4.hasNext()) {
               CrystalAura.PlaceLocation placeLocation = (CrystalAura.PlaceLocation)var4.next();
               if (placeLocation.func_177951_i(new BlockPos(packetSpawnObject.func_186880_c(), packetSpawnObject.func_186882_d(), packetSpawnObject.func_186881_e())) < 2.0D) {
                  placeLocation.Timeset = (double)System.currentTimeMillis();
                  placeLocation.PacketConfirmed = true;
               }
            }
         }
      }

      if (side == Connection.Side.OUT && this.SpoofAngle.getValBoolean() && this.Rots != null && (packet instanceof Rotation || packet instanceof PositionRotation)) {
         CPacketPlayer packet2 = (CPacketPlayer)packet;
         packet2.field_149476_e = this.Rots[0];
         packet2.field_149473_f = this.Rots[1];
      }

      return true;
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      Iterator var2 = this.placeLocations.iterator();

      while(var2.hasNext()) {
         CrystalAura.PlaceLocation placeLocation = (CrystalAura.PlaceLocation)var2.next();
         if (placeLocation.PacketConfirmed) {
            RenderUtils.RenderBlock(this.Mode.getValString(), RenderUtils.Standardbb(new BlockPos(placeLocation.field_177962_a, placeLocation.field_177960_b, placeLocation.field_177961_c)), this.OverlayColor.getcolor(), this.LineWidth.getValDouble());
            RenderUtils.SimpleNametag(new Vec3d((double)placeLocation.field_177962_a, (double)placeLocation.field_177960_b, (double)placeLocation.field_177961_c), (new DecimalFormat("0.00")).format(placeLocation.damage));
         }
      }

      this.placeLocations.removeIf((placeLocationx) -> {
         return placeLocationx.Timeset + 1000.0D < (double)System.currentTimeMillis();
      });
   }

   public boolean canPlaceCrystal(BlockPos blockPos) {
      try {
         if (mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z && mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h) {
            return false;
         } else if (mc.field_71441_e.func_180495_p(blockPos.func_177984_a()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2)).func_177230_c() == Blocks.field_150350_a) {
            Iterator var2 = mc.field_71441_e.func_72872_a(Entity.class, (new AxisAlignedBB(blockPos)).func_72314_b(0.0D, 3.0D, 0.0D)).iterator();

            Object entity;
            do {
               do {
                  if (!var2.hasNext()) {
                     return true;
                  }

                  entity = var2.next();
               } while(entity instanceof EntityEnderCrystal && !this.MultiPlace.getValBoolean());
            } while(entity instanceof EntityXPOrb || entity instanceof EntityItem);

            return false;
         } else {
            return false;
         }
      } catch (Exception var4) {
         return false;
      }
   }

   public void placeCrystalOnBlock(ArrayList<BlockPos> finalcheck, EntityOtherPlayerMP playerMP) {
      Iterator var3 = finalcheck.iterator();

      while(var3.hasNext()) {
         BlockPos pos = (BlockPos)var3.next();
         RayTraceResult result = mc.field_71441_e.func_72933_a(mc.field_71439_g.func_174824_e(1.0F), new Vec3d((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() - 0.5D, (double)pos.func_177952_p() + 0.5D));
         EnumFacing facing;
         if (result != null && result.field_178784_b != null) {
            facing = result.field_178784_b;
         } else {
            facing = EnumFacing.UP;
         }

         if (!(mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.eyeHeight < (double)pos.field_177961_c) || facing != EnumFacing.UP) {
            assert result != null;

            if (!(result.func_178782_a().func_177951_i(pos) > 2.0D) && (!((double)mc.field_71439_g.eyeHeight + mc.field_71439_g.field_70163_u < (double)pos.field_177960_b) || !facing.equals(EnumFacing.UP))) {
               this.placeTimer.setLastMS();
               if (!mc.field_71439_g.func_184614_ca().func_77973_b().equals(Items.field_185158_cP) && this.Hand.getValString().equalsIgnoreCase("Mainhand") && this.FastSwitch.getValBoolean() && this.find_in_hotbar(Items.field_185158_cP) != -1) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(this.find_in_hotbar(Items.field_185158_cP)));
                  mc.field_71439_g.field_71071_by.field_70461_c = this.find_in_hotbar(Items.field_185158_cP);
               }

               this.TryJiggle(pos);
               EnumHand hand = null;
               if (mc.field_71439_g.func_184592_cb().func_77973_b().equals(Items.field_185158_cP) && (this.Hand.getValString().equalsIgnoreCase("Offhand") || this.Hand.getValString().equalsIgnoreCase("Either"))) {
                  hand = EnumHand.OFF_HAND;
               }

               if (mc.field_71439_g.func_184614_ca().func_77973_b().equals(Items.field_185158_cP) && (this.Hand.getValString().equalsIgnoreCase("Mainhand") || this.Hand.getValString().equalsIgnoreCase("Either"))) {
                  hand = EnumHand.MAIN_HAND;
               }

               if (hand != null) {
                  if (this.SpoofAngle.getValBoolean()) {
                     this.Rots = Utils.getNeededRotations(new Vec3d((double)pos.field_177962_a + 0.5D, (double)pos.field_177960_b + (facing == EnumFacing.UP ? 1.0D : 0.5D), (double)pos.field_177961_c + 0.5D), 0.0F, 0.0F);
                     mc.field_71439_g.field_70177_z = this.Rots[0];
                     mc.field_71439_g.field_70125_A = this.Rots[1];
                  }

                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.5F, 0.5F, 0.5F));
                  this.placeLocations.add(new CrystalAura.PlaceLocation((double)pos.field_177962_a, (double)pos.field_177960_b, (double)pos.field_177961_c, (double)Calculate_Damage((double)pos.field_177962_a + 0.5D, (double)(pos.field_177960_b + 1), (double)pos.field_177961_c + 0.5D, playerMP)));
                  break;
               }
            }
         }
      }

   }

   private void TryJiggle(BlockPos pos) {
      if (this.HoleJiggle.getValBoolean() && mc.field_71439_g.func_70011_f((double)pos.field_177962_a, (double)pos.field_177960_b, (double)pos.field_177961_c) > 5.0D) {
         Wrapper.INSTANCE.sendPacket(new Position(Math.floor(mc.field_71439_g.field_70165_t) + 0.5D + (mc.field_71439_g.field_70165_t < (double)pos.field_177962_a ? 0.2D : -0.2D), mc.field_71439_g.field_70163_u, Math.floor(mc.field_71439_g.field_70161_v) + 0.5D + (mc.field_71439_g.field_70161_v < (double)pos.field_177961_c ? 0.2D : -0.2D), mc.field_71439_g.field_70122_E));
      }

   }

   private int find_in_hotbar(Item item) {
      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack != ItemStack.field_190927_a && stack.func_77973_b().equals(item)) {
            return i;
         }
      }

      return -1;
   }

   public void processAttack(EntityEnderCrystal entity) {
      if (this.Packetreach.getValBoolean()) {
         double posX = entity.field_70165_t - 3.5D * Math.cos(Math.toRadians((double)(Utils.getYaw(entity) + 90.0F)));
         double posZ = entity.field_70161_v - 3.5D * Math.sin(Math.toRadians((double)(Utils.getYaw(entity) + 90.0F)));
         Wrapper.INSTANCE.sendPacket(new PositionRotation(posX, entity.field_70163_u, posZ, Utils.getYaw(entity), Utils.getPitch(entity), mc.field_71439_g.field_70122_E));
         Wrapper.INSTANCE.sendPacket(new CPacketUseEntity(entity));
         Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
      }

      if (this.attackTimer.isDelay((long)(this.HitDelayBetween.getValDouble() * 1000.0D))) {
         this.attackTimer.setLastMS();
         this.TryJiggle(entity.func_180425_c());
         if (this.SpoofAngle.getValBoolean()) {
            this.Rots = Utils.getNeededRotations(entity.func_174791_d().func_72441_c(0.0D, 0.8D, 0.0D), 0.0F, 0.0F);
            mc.field_71439_g.field_70177_z = this.Rots[0];
            mc.field_71439_g.field_70125_A = this.Rots[1];
         }

         if (this.SwordHit.getValBoolean() && this.find_in_hotbar(Items.field_151048_u) != -1 && mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(this.find_in_hotbar(Items.field_151048_u)));
            mc.field_71439_g.field_71071_by.field_70461_c = this.find_in_hotbar(Items.field_151048_u);
         }

         if (!this.SwordHit.getValBoolean() || mc.field_71439_g.func_184614_ca().func_77973_b().equals(Items.field_151048_u)) {
            Wrapper.INSTANCE.attack(entity);
            mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
         }
      }

   }

   private static float get_damage_multiplied(float damage) {
      int diff = mc.field_71441_e.func_175659_aa().func_151525_a();
      return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
   }

   public static float Calculate_Damage(double posX, double posY, double posZ, Entity entity) {
      double distancedsize = entity.func_70011_f(posX, posY, posZ) / 12.0D;
      double blockDensity = (double)entity.field_70170_p.func_72842_a(new Vec3d(posX, posY, posZ), entity.func_174813_aQ());
      double v = (1.0D - distancedsize) * blockDensity;
      float damage = (float)((int)((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D));
      return entity instanceof EntityLivingBase ? get_blast_reduction((EntityLivingBase)entity, get_damage_multiplied(damage), new Explosion(mc.field_71441_e, (Entity)null, posX, posY, posZ, 6.0F, false, true)) : 1.0F;
   }

   public static float get_blast_reduction(EntityLivingBase entity, float damage, Explosion explosion) {
      if (entity instanceof EntityPlayer) {
         EntityPlayer ep = (EntityPlayer)entity;
         damage = CombatRules.func_189427_a(damage, (float)ep.func_70658_aO(), (float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
         int k = EnchantmentHelper.func_77508_a(ep.func_184193_aE(), DamageSource.func_94539_a(explosion));
         damage *= 1.0F - MathHelper.func_76131_a((float)k, 0.0F, 20.0F) / 25.0F;
         if (entity.func_70644_a((Potion)Objects.requireNonNull(MobEffects.field_76429_m))) {
            damage -= damage / 4.0F;
         }

         return Math.max(damage, 0.0F);
      } else {
         return CombatRules.func_189427_a(damage, (float)entity.func_70658_aO(), (float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
      }
   }

   static class PlaceLocation extends Vec3i {
      double damage;
      boolean PacketConfirmed;
      double Timeset;

      private PlaceLocation(double xIn, double yIn, double zIn, double v) {
         super(xIn, yIn, zIn);
         this.damage = v;
      }

      // $FF: synthetic method
      PlaceLocation(double x0, double x1, double x2, double x3, Object x4) {
         this(x0, x1, x2, x3);
      }
   }
}
