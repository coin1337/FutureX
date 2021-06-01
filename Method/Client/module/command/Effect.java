package Method.Client.module.command;

import Method.Client.utils.visual.ChatUtils;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Effect extends Command {
   public Effect() {
      super("effect");
   }

   public void runCommand(String s, String[] args) {
      try {
         int id;
         if (args[0].equalsIgnoreCase("add")) {
            id = Integer.parseInt(args[1]);
            int duration = Integer.parseInt(args[2]);
            int amplifier = Integer.parseInt(args[3]);
            if (Potion.func_188412_a(id) == null) {
               ChatUtils.error("Potion is null");
               return;
            }

            this.addEffect(id, duration, amplifier);
         } else if (args[0].equalsIgnoreCase("remove")) {
            id = Integer.parseInt(args[1]);
            if (Potion.func_188412_a(id) == null) {
               ChatUtils.error("Potion is null");
               return;
            }

            this.removeEffect(id);
         } else if (args[0].equalsIgnoreCase("clear")) {
            this.clearEffects();
         }
      } catch (Exception var6) {
         ChatUtils.error("Usage: " + this.getSyntax());
      }

   }

   void addEffect(int id, int duration, int amplifier) {
      mc.field_71439_g.func_70690_d(new PotionEffect((Potion)Objects.requireNonNull(Potion.func_188412_a(id)), duration, amplifier));
   }

   void removeEffect(int id) {
      mc.field_71439_g.func_184589_d((Potion)Objects.requireNonNull(Potion.func_188412_a(id)));
   }

   void clearEffects() {
      Iterator var1 = mc.field_71439_g.func_70651_bq().iterator();

      while(var1.hasNext()) {
         PotionEffect effect = (PotionEffect)var1.next();
         mc.field_71439_g.func_184589_d(effect.func_188419_a());
      }

   }

   public String getDescription() {
      return "Effect manager.";
   }

   public String getSyntax() {
      return "effect <add/remove/clear> <id> <duration> <amplifier>";
   }
}
