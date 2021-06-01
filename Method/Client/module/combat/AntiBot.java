package Method.Client.module.combat;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiBot extends Module {
   public static ArrayList<AntiBot.EntityBot> bots = new ArrayList();
   public Setting level;
   public Setting tick;
   public Setting ifInAir;
   public Setting ifGround;
   public Setting ifZeroHealth;
   public Setting ifInvisible;
   public Setting ifEntityId;
   public Setting ifTabName;
   public Setting ifPing;
   public Setting remove;
   public Setting gwen;

   public AntiBot() {
      super("AntiBot", 0, Category.COMBAT, "Does not hit bots");
      this.level = Main.setmgr.add(new Setting("level", this, 0.0D, 0.0D, 6.0D, false));
      this.tick = Main.setmgr.add(new Setting("tick", this, 0.0D, 0.0D, 999.0D, true));
      this.ifInAir = Main.setmgr.add(new Setting("InAir", this, false));
      this.ifGround = Main.setmgr.add(new Setting("OnGround", this, false));
      this.ifZeroHealth = Main.setmgr.add(new Setting("ZeroHealth", this, false));
      this.ifInvisible = Main.setmgr.add(new Setting("Invisible", this, false));
      this.ifEntityId = Main.setmgr.add(new Setting("EntityId", this, false));
      this.ifTabName = Main.setmgr.add(new Setting("OutTabName", this, false));
      this.ifPing = Main.setmgr.add(new Setting("PingCheck", this, false));
      this.remove = Main.setmgr.add(new Setting("RemoveBots", this, false));
      this.gwen = Main.setmgr.add(new Setting("Gwen", this, false));
   }

   public void onEnable() {
      bots.clear();
      super.onEnable();
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.gwen.getValBoolean()) {
         Iterator var3 = mc.field_71441_e.field_72996_f.iterator();

         while(var3.hasNext()) {
            Object entity = var3.next();
            if (packet instanceof SPacketSpawnPlayer) {
               SPacketSpawnPlayer spawn = (SPacketSpawnPlayer)packet;
               double posX = spawn.func_186898_d() / 32.0D;
               double posY = spawn.func_186897_e() / 32.0D;
               double posZ = spawn.func_186899_f() / 32.0D;
               double difX = mc.field_71439_g.field_70165_t - posX;
               double difY = mc.field_71439_g.field_70163_u - posY;
               double difZ = mc.field_71439_g.field_70161_v - posZ;
               double dist = Math.sqrt(difX * difX + difY * difY + difZ * difZ);
               if (dist <= 17.0D && posX != mc.field_71439_g.field_70165_t && posY != mc.field_71439_g.field_70163_u && posZ != mc.field_71439_g.field_70161_v) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public void onClientTick(ClientTickEvent event) {
      if (this.tick.getValDouble() > 0.0D) {
         bots.clear();
      }

      Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

      while(true) {
         while(true) {
            while(true) {
               EntityLivingBase entity;
               do {
                  do {
                     Object object;
                     do {
                        if (!var2.hasNext()) {
                           super.onClientTick(event);
                           return;
                        }

                        object = var2.next();
                     } while(!(object instanceof EntityLivingBase));

                     entity = (EntityLivingBase)object;
                  } while(entity instanceof EntityPlayerSP);
               } while(!(entity instanceof EntityPlayer));

               EntityPlayer bot = (EntityPlayer)entity;
               if (!this.isBotBase(bot)) {
                  int ailevel = (int)this.level.getValDouble();
                  boolean isAi = (double)ailevel > 0.0D;
                  if (isAi && this.botPercentage(bot) > ailevel) {
                     this.addBot(bot);
                  } else if (!isAi && this.botCondition(bot)) {
                     this.addBot(bot);
                  }
               } else {
                  this.addBot(bot);
                  if (this.remove.getValBoolean()) {
                     mc.field_71441_e.func_72900_e(bot);
                  }
               }
            }
         }
      }
   }

   void addBot(EntityPlayer player) {
      if (!isBot(player)) {
         bots.add(new AntiBot.EntityBot(player));
      }

   }

   public static boolean isBot(EntityPlayer player) {
      Iterator var1 = bots.iterator();

      AntiBot.EntityBot bot;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         bot = (AntiBot.EntityBot)var1.next();
         if (bot.getName().equals(player.func_70005_c_())) {
            if (player.func_82150_aj() != bot.isInvisible()) {
               return player.func_82150_aj();
            }

            return true;
         }
      } while(bot.getId() != player.func_145782_y() && !bot.getUuid().equals(player.func_146103_bH().getId()));

      return true;
   }

   boolean botCondition(EntityPlayer bot) {
      if (this.tick.getValDouble() > 0.0D && (double)bot.field_70173_aa < this.tick.getValDouble()) {
         return true;
      } else if (this.ifInAir.getValBoolean() && bot.func_82150_aj() && bot.field_70181_x == 0.0D && bot.field_70163_u > mc.field_71439_g.field_70163_u + 1.0D && Utils.isBlockMaterial((new BlockPos(bot)).func_177977_b(), Blocks.field_150350_a)) {
         return true;
      } else if (this.ifGround.getValBoolean() && bot.field_70181_x == 0.0D && !bot.field_70124_G && bot.field_70122_E && bot.field_70163_u % 1.0D != 0.0D && bot.field_70163_u % 0.5D != 0.0D) {
         return true;
      } else if (this.ifZeroHealth.getValBoolean() && bot.func_110143_aJ() <= 0.0F) {
         return true;
      } else if (this.ifInvisible.getValBoolean() && bot.func_82150_aj()) {
         return true;
      } else if (this.ifEntityId.getValBoolean() && bot.func_145782_y() >= 1000000000) {
         return true;
      } else if (this.ifTabName.getValBoolean()) {
         boolean isTabName = false;
         Iterator var3 = ((NetHandlerPlayClient)Objects.requireNonNull(Wrapper.INSTANCE.mc().func_147114_u())).func_175106_d().iterator();

         while(var3.hasNext()) {
            NetworkPlayerInfo npi = (NetworkPlayerInfo)var3.next();
            npi.func_178845_a();
            if (npi.func_178845_a().getName().contains(bot.func_70005_c_())) {
               isTabName = true;
            }
         }

         return !isTabName;
      } else {
         return false;
      }
   }

   int botPercentage(EntityPlayer bot) {
      int percentage = 0;
      if (this.tick.getValDouble() > 0.0D && (double)bot.field_70173_aa < this.tick.getValDouble()) {
         ++percentage;
      }

      if (this.ifInAir.getValBoolean() && bot.func_82150_aj() && bot.field_70163_u > mc.field_71439_g.field_70163_u + 1.0D && Utils.isBlockMaterial((new BlockPos(bot)).func_177977_b(), Blocks.field_150350_a)) {
         ++percentage;
      }

      if (this.ifGround.getValBoolean() && bot.field_70181_x == 0.0D && !bot.field_70124_G && bot.field_70122_E && bot.field_70163_u % 1.0D != 0.0D && bot.field_70163_u % 0.5D != 0.0D) {
         ++percentage;
      }

      if (this.ifZeroHealth.getValBoolean() && bot.func_110143_aJ() <= 0.0F) {
         ++percentage;
      }

      if (this.ifInvisible.getValBoolean() && bot.func_82150_aj()) {
         ++percentage;
      }

      if (this.ifEntityId.getValBoolean() && bot.func_145782_y() >= 1000000000) {
         ++percentage;
      }

      if (this.ifTabName.getValBoolean()) {
         boolean isTabName = false;
         Iterator var4 = ((NetHandlerPlayClient)Objects.requireNonNull(Wrapper.INSTANCE.mc().func_147114_u())).func_175106_d().iterator();

         while(var4.hasNext()) {
            NetworkPlayerInfo npi = (NetworkPlayerInfo)var4.next();
            npi.func_178845_a();
            if (npi.func_178845_a().getName().contains(bot.func_70005_c_())) {
               isTabName = true;
            }
         }

         if (!isTabName) {
            ++percentage;
         }
      }

      return percentage;
   }

   boolean isBotBase(EntityPlayer bot) {
      if (isBot(bot)) {
         return true;
      } else {
         bot.func_146103_bH();
         GameProfile botProfile = bot.func_146103_bH();
         bot.func_110124_au();
         if (botProfile.getName() == null) {
            return true;
         } else {
            String botName = botProfile.getName();
            return botName.contains("Body #") || botName.contains("NPC") || botName.equalsIgnoreCase(Utils.getEntityNameColor(bot));
         }
      }
   }

   public static class EntityBot {
      private final String name;
      private final int id;
      private final UUID uuid;
      private final boolean invisible;

      public EntityBot(EntityPlayer player) {
         this.name = String.valueOf(player.func_146103_bH().getName());
         this.id = player.func_145782_y();
         this.uuid = player.func_146103_bH().getId();
         this.invisible = player.func_82150_aj();
      }

      public int getId() {
         return this.id;
      }

      public String getName() {
         return this.name;
      }

      public UUID getUuid() {
         return this.uuid;
      }

      public boolean isInvisible() {
         return this.invisible;
      }
   }
}
