package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.FriendManager;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import Method.Client.utils.visual.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class TotemPop extends Module {
   public static HashMap<String, Integer> popList;
   Setting Friend;

   public TotemPop() {
      super("TotemPop", 0, Category.COMBAT, "TotemPop");
      this.Friend = Main.setmgr.add(this.Friend = new Setting("Friend", this, true));
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketEntityStatus) {
         SPacketEntityStatus packet2 = (SPacketEntityStatus)packet;
         if (packet2.func_149160_c() == 35) {
            Entity entity = packet2.func_149161_a(mc.field_71441_e);
            this.pop(entity);
         }
      }

      return true;
   }

   public void onClientTick(ClientTickEvent event) {
      Iterator var2 = mc.field_71441_e.field_73010_i.iterator();

      while(var2.hasNext()) {
         EntityPlayer player = (EntityPlayer)var2.next();
         if (player.func_110143_aJ() <= 0.0F && popList.containsKey(player.func_70005_c_())) {
            ChatUtils.message(ChatFormatting.RED + player.func_70005_c_() + " died after popping " + ChatFormatting.GREEN + popList.get(player.func_70005_c_()) + ChatFormatting.RED + " totems!");
            popList.remove(player.func_70005_c_(), popList.get(player.func_70005_c_()));
         }
      }

      super.onClientTick(event);
   }

   public void pop(Entity entity) {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         if (popList == null) {
            popList = new HashMap();
         }

         if (this.Friend.getValBoolean() || !FriendManager.isFriend(entity.func_70005_c_())) {
            if (popList.get(entity.func_70005_c_()) == null) {
               popList.put(entity.func_70005_c_(), 1);
               ChatUtils.message(ChatFormatting.RED + entity.func_70005_c_() + " popped " + ChatFormatting.YELLOW + 1 + ChatFormatting.RED + " totem!");
            } else {
               this.Check(entity);
            }
         }

      }
   }

   private void Check(Entity entity) {
      if (popList.get(entity.func_70005_c_()) != null) {
         int popCounter = (Integer)popList.get(entity.func_70005_c_());
         HashMap var10000 = popList;
         String var10001 = entity.func_70005_c_();
         ++popCounter;
         var10000.put(var10001, popCounter);
         StringBuilder var3 = (new StringBuilder()).append(ChatFormatting.RED).append(entity.func_70005_c_()).append(ChatFormatting.RED).append(" popped ").append(ChatFormatting.YELLOW);
         ++popCounter;
         ChatUtils.message(var3.append(popCounter).append(ChatFormatting.RED).append(" totems!").toString());
      }

   }

   public static int getpops(Entity entity) {
      return popList.get(entity.func_70005_c_()) != null ? (Integer)popList.get(entity.func_70005_c_()) : 0;
   }
}
