package Method.Client.module.combat;

import Method.Client.managers.FriendManager;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Mouse;

public class InteractClick extends Module {
   public InteractClick() {
      super("InteractClick", 0, Category.COMBAT, "InteractClick");
   }

   public void onClientTick(ClientTickEvent event) {
      RayTraceResult object = Wrapper.INSTANCE.mc().field_71476_x;
      if (object != null) {
         if (object.field_72313_a == Type.ENTITY) {
            Entity entity = object.field_72308_g;
            if (entity instanceof EntityPlayer && !mc.field_71439_g.field_70128_L && mc.field_71439_g.func_70685_l(entity)) {
               EntityPlayer player = (EntityPlayer)entity;
               String ID = Utils.getPlayerName(player);
               if (Mouse.isButtonDown(2) && Wrapper.INSTANCE.mc().field_71462_r == null) {
                  FriendManager.addFriend(ID);
               } else if (Mouse.isButtonDown(1) && Wrapper.INSTANCE.mc().field_71462_r == null) {
                  FriendManager.removeFriend(ID);
               }
            }
         }

         super.onClientTick(event);
      }
   }
}
