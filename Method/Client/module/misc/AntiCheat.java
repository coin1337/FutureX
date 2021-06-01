package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.FriendManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.visual.ChatUtils;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiCheat extends Module {
   Setting Experimental;

   public AntiCheat() {
      super("CheatDetect", 0, Category.MISC, "AntiCheat For others!");
      this.Experimental = Main.setmgr.add(new Setting("Experimental", this, true));
   }

   public void onClientTick(ClientTickEvent event) {
      if (mc.field_71439_g.field_70173_aa % 5 == 0) {
         Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

         while(true) {
            EntityLivingBase entity;
            do {
               do {
                  Entity entityplayer;
                  do {
                     do {
                        do {
                           if (!var2.hasNext()) {
                              return;
                           }

                           entityplayer = (Entity)var2.next();
                        } while(!(entityplayer instanceof EntityPlayer));
                     } while(entityplayer.func_70005_c_().equalsIgnoreCase(mc.field_71439_g.func_70005_c_()));
                  } while(FriendManager.isFriend(entityplayer.func_70005_c_()));

                  entity = (EntityLivingBase)entityplayer;
               } while(entity.field_70173_aa <= 40);

               if (entity.field_70125_A > 90.0F || entity.field_70125_A < -90.0F) {
                  ChatUtils.message(entity.func_70005_c_() + " flagged Invalid Head Pitch ");
               }

               if (Math.abs(Math.abs(entity.field_70163_u) - Math.abs(entity.field_70167_r)) > 1.0D && !entity.field_70122_E && !entity.field_70124_G && entity.field_70163_u > entity.field_70167_r && !entity.func_70617_f_() && !entity.func_70090_H()) {
                  ChatUtils.message(entity.func_70005_c_() + " flagged Ascension ");
               }

               if (Utils.isMoving(entity) && (entity.field_70726_aT == entity.field_70727_aS && entity.field_70726_aT == (float)((int)entity.field_70726_aT) || entity.field_70177_z == entity.field_70126_B && entity.field_70177_z == (float)((int)entity.field_70126_B))) {
                  ChatUtils.message(entity.func_70005_c_() + " flagged  Yaw/Pitch [AA]");
               }
            } while(!this.Experimental.getValBoolean());

            double yawDelta = (double)Math.abs(Math.abs(entity.field_70177_z) - Math.abs(entity.field_70126_B));
            double pitchDelta = (double)Math.abs(Math.abs(entity.field_70125_A) - Math.abs(entity.field_70127_C));
            if (entity.field_70177_z == (float)Math.round(entity.field_70177_z) && entity.field_70177_z != 0.0F && yawDelta > 10.0D) {
               ChatUtils.message(entity.func_70005_c_() + " flagged KillAura [AA]");
            }

            if (entity.field_70125_A == (float)Math.round(entity.field_70125_A) && entity.field_70125_A != 90.0F && entity.field_70125_A != -90.0F && pitchDelta > 10.0D) {
               ChatUtils.message(entity.func_70005_c_() + " flagged KillAura [AB]");
            }

            if (entity.func_70051_ag() && Utils.isMoving(entity) && entity.field_191988_bg < 0.0F) {
               ChatUtils.message(entity.func_70005_c_() + " flagged Omni-Sprint ");
            }

            if (entity.func_184218_aH() && entity.func_184187_bx() instanceof EntityBoat && !entity.field_70171_ac) {
               ChatUtils.message(entity.func_70005_c_() + " flagged Boat-Fly ");
            }

            if (entity.func_191953_am() && entity.func_70051_ag()) {
               ChatUtils.message(entity.func_70005_c_() + " flagged Jesus ");
            }

            if (entity.field_70138_W > 1.0F) {
               ChatUtils.message(entity.func_70005_c_() + " flagged Step ");
            }

            if (Math.abs(entity.field_70165_t - entity.field_70142_S) > 0.42D || Math.abs(entity.field_70161_v - entity.field_70136_U) > 0.42D) {
               ChatUtils.message(entity.func_70005_c_() + " flagged Speed ");
            }

            if (entity.func_184585_cz() && (Math.abs(entity.field_70165_t - entity.field_70142_S) > 0.3D || Math.abs(entity.field_70161_v - entity.field_70136_U) > 0.3D)) {
               ChatUtils.message(entity.func_70005_c_() + " flagged NoSlow ");
            }

            if (!entity.func_184613_cA() && !mc.field_71441_e.func_72829_c(entity.func_174813_aQ().func_72317_d(0.0D, -1.1D, 0.0D)) && entity.field_70167_r < entity.field_70163_u) {
               ChatUtils.message(entity.func_70005_c_() + " flagged Flying ");
            }

            if (entity.field_70172_ad > 6 && entity.field_70172_ad < 12 && entity.field_70142_S == entity.field_70165_t && entity.field_70161_v == entity.field_70136_U && !mc.field_71441_e.func_72829_c(entity.func_174813_aQ().func_72321_a(0.05D, 0.0D, 0.05D))) {
               ChatUtils.message(entity.func_70005_c_() + " flagged KnockBack ");
            }

            if (entity.field_70172_ad > 6 && entity.field_70172_ad < 12 && entity.field_70137_T == entity.field_70163_u) {
               ChatUtils.message(entity.func_70005_c_() + " flagged KnockBack ");
            }

            if (entity.field_70122_E && entity.func_184613_cA()) {
               ChatUtils.message(entity.func_70005_c_() + " flagged Ground Elytra ");
            }
         }
      }
   }
}
