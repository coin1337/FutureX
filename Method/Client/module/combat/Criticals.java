package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import java.util.Objects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;

public class Criticals extends Module {
   TimerUtils timer = new TimerUtils();
   boolean cancelSomePackets;
   Setting mode;
   Setting ShowCrit;

   public Criticals() {
      super("Auto Criticals", 0, Category.COMBAT, "Criticals on hit");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Packet", new String[]{"Packet", "Simple", "Groundspoof", "Jump", "Fpacket", "Bpacket", "Falldist", "MiniJump", "NBypass"}));
      this.ShowCrit = Main.setmgr.add(new Setting("ShowCrit", this, true));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (mc.field_71439_g.field_70122_E) {
         if (side == Connection.Side.OUT) {
            if (packet instanceof CPacketUseEntity) {
               CPacketUseEntity attack = (CPacketUseEntity)packet;
               if (attack.func_149565_c() == Action.ATTACK) {
                  if (this.mode.getValString().equalsIgnoreCase("Bpacket")) {
                     try {
                        Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.11D, mc.field_71439_g.field_70161_v, true));
                        Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.1100013579D, mc.field_71439_g.field_70161_v, false));
                        Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.3579E-6D, mc.field_71439_g.field_70161_v, false));
                     } catch (Exception var5) {
                     }
                  }

                  EntityPlayerSP var10000;
                  if (this.mode.getValString().equalsIgnoreCase("NBypass")) {
                     mc.field_71439_g.field_70181_x = 0.41999998688697815D;
                     if (mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                        var10000 = mc.field_71439_g;
                        var10000.field_70181_x += (double)((float)(((PotionEffect)Objects.requireNonNull(mc.field_71439_g.func_70660_b(MobEffects.field_76430_j))).func_76458_c() + 1) * 0.1F);
                     }

                     if (mc.field_71439_g.func_70051_ag()) {
                        float var1 = mc.field_71439_g.field_70177_z * 0.017453292F;
                        var10000 = mc.field_71439_g;
                        var10000.field_70159_w -= (double)(MathHelper.func_76126_a(var1) * 0.2F);
                        var10000 = mc.field_71439_g;
                        var10000.field_70179_y += (double)(MathHelper.func_76134_b(var1) * 0.2F);
                     }

                     mc.field_71439_g.field_70160_al = true;
                  }

                  if (this.mode.getValString().equalsIgnoreCase("Simple")) {
                     if (this.canJump()) {
                        mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 0.5D, mc.field_71439_g.field_70161_v);
                        mc.field_71439_g.func_70031_b(true);
                     } else {
                        mc.field_71439_g.field_70181_x = -0.1D;
                     }
                  }

                  if (this.mode.getValString().equalsIgnoreCase("MiniJump")) {
                     mc.field_71439_g.func_70664_aZ();
                     var10000 = mc.field_71439_g;
                     var10000.field_70181_x -= 0.30000001192092896D;
                  }

                  if (this.mode.getValString().equalsIgnoreCase("Fpacket")) {
                     this.doCritical();
                  }

                  if (this.mode.getValString().equalsIgnoreCase("Falldist")) {
                     var10000 = mc.field_71439_g;
                     var10000.field_70181_x -= -0.001D;
                     mc.field_71439_g.field_70143_R = 9999.0F;
                     mc.field_71439_g.func_180430_e(20.0F, 0.0F);
                  }

                  if (this.mode.getValString().equalsIgnoreCase("Groundspoof")) {
                     mc.field_71439_g.field_70122_E = false;
                     mc.field_71439_g.field_70160_al = true;
                  }

                  if (this.mode.getValString().equalsIgnoreCase("Packet")) {
                     if (mc.field_71439_g.field_70124_G && this.timer.isDelay(500L)) {
                        Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 0.0627D, mc.field_71439_g.field_70161_v, false));
                        Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, false));
                        this.timer.setLastMS();
                        this.cancelSomePackets = true;
                     }
                  } else if (this.mode.getValString().equalsIgnoreCase("Jump") && this.canJump()) {
                     mc.field_71439_g.func_70664_aZ();
                  }

                  if (this.ShowCrit.getValBoolean()) {
                     Entity entity = attack.func_149564_a(mc.field_71441_e);
                     if (entity != null) {
                        mc.field_71439_g.func_71009_b(entity);
                     }
                  }
               }
            }
         } else if (this.mode.getValString().equalsIgnoreCase("Packet") && packet instanceof CPacketPlayer && this.cancelSomePackets) {
            this.cancelSomePackets = false;
            return false;
         }
      }

      return true;
   }

   private void doCritical() {
      if (!mc.field_71439_g.func_180799_ab() && !mc.field_71439_g.func_70090_H()) {
         double posX = mc.field_71439_g.field_70165_t;
         double posY = mc.field_71439_g.field_70163_u;
         double posZ = mc.field_71439_g.field_70161_v;
         Wrapper.INSTANCE.sendPacket(new Position(posX, posY + 0.0625D, posZ, false));
         Wrapper.INSTANCE.sendPacket(new Position(posX, posY, posZ, false));
         Wrapper.INSTANCE.sendPacket(new Position(posX, posY + 1.1E-5D, posZ, false));
         Wrapper.INSTANCE.sendPacket(new Position(posX, posY, posZ, false));
      }
   }

   boolean canJump() {
      if (mc.field_71439_g.func_70617_f_()) {
         return false;
      } else if (mc.field_71439_g.func_70090_H()) {
         return false;
      } else if (mc.field_71439_g.func_180799_ab()) {
         return false;
      } else if (mc.field_71439_g.func_70093_af()) {
         return false;
      } else if (mc.field_71439_g.func_184218_aH()) {
         return false;
      } else {
         return !mc.field_71439_g.func_70644_a(MobEffects.field_76440_q);
      }
   }
}
