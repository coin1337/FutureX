package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FakePlayer extends Command {
   public FakePlayer() {
      super("FakePlayer");
   }

   public void runCommand(String s, String[] args) {
      try {
         EntityOtherPlayerMP fake = new EntityOtherPlayerMP(mc.field_71441_e, new GameProfile(UUID.randomUUID(), args[0]));
         fake.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
         mc.field_71441_e.field_72996_f.add(fake);
         ChatUtils.message("Added Fake Player ");
      } catch (Exception var4) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   public String getDescription() {
      return "FakePlayer Spawner";
   }

   public String getSyntax() {
      return "FakePlayer <Name>";
   }
}
