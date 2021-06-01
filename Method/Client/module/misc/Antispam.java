package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class Antispam extends Module {
   Setting unicode;
   Setting broadcasts;
   Setting spam;
   Setting deletenew;
   Setting Similarity;
   public static List<Antispam.Priorchatlist> priorchatlists = Lists.newArrayList();
   public static int line;
   public static List<ChatLine> lastChatLine;

   public Antispam() {
      super("Antispam", 0, Category.MISC, "Antispam");
      this.unicode = Main.setmgr.add(new Setting("Unicode", this, true));
      this.broadcasts = Main.setmgr.add(new Setting("Server Broadcasts", this, false));
      this.spam = Main.setmgr.add(new Setting("Repeated messages", this, true));
      this.deletenew = Main.setmgr.add(new Setting("Delete Repeated", this, true, this.spam, 3));
      this.Similarity = Main.setmgr.add(new Setting("Similarity %", this, 0.85D, 0.0D, 1.0D, false, this.spam, 4));
   }

   public void ClientChatReceivedEvent(ClientChatReceivedEvent event) {
      if (this.spam.getValBoolean()) {
         GuiNewChat chatinst = mc.field_71456_v.func_146158_b();
         String Incomingtext = event.getMessage().func_150260_c();
         Iterator var4 = priorchatlists.iterator();

         while(var4.hasNext()) {
            Antispam.Priorchatlist Oldchat = (Antispam.Priorchatlist)var4.next();
            if (levenshteinDistance(Oldchat.fulltext, Incomingtext) >= this.Similarity.getValDouble()) {
               ++Oldchat.spammedtimes;
               String change = TextFormatting.GRAY + " (" + TextFormatting.GOLD + "x" + Oldchat.spammedtimes + TextFormatting.GRAY + ")";
               event.getMessage().func_150258_a(change);
               chatinst.func_146242_c(Oldchat.Removable + 1);
               Oldchat.Removable = line;
               break;
            }
         }

         priorchatlists.add(new Antispam.Priorchatlist(line, Incomingtext, 1));
         ++line;
         if (!event.isCanceled()) {
            chatinst.func_146234_a(event.getMessage(), line);
         }

         if (line > 256) {
            line = 0;
            priorchatlists.clear();
         }

         event.setCanceled(true);
      }

      super.ClientChatReceivedEvent(event);
   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (packet instanceof SPacketChat) {
         SPacketChat packet2 = (SPacketChat)packet;
         if (this.broadcasts.getValBoolean() && packet2.func_148915_c().func_150254_d().startsWith("§5[SERVER]")) {
            return false;
         }

         if (this.unicode.getValBoolean() && packet2.func_148915_c() instanceof TextComponentString) {
            TextComponentString component = (TextComponentString)packet2.func_148915_c();
            StringBuilder sb = new StringBuilder();
            boolean containsUnicode = false;
            String[] var7 = component.func_150254_d().split(" ");
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String s = var7[var9];
               StringBuilder line = new StringBuilder();
               char[] var12 = s.toCharArray();
               int var13 = var12.length;

               for(int var14 = 0; var14 < var13; ++var14) {
                  char c = var12[var14];
                  if (c >= 'ﻠ') {
                     c -= 'ﻠ';
                     containsUnicode = true;
                  }

                  line.append(c);
               }

               sb.append(line).append(" ");
            }

            if (containsUnicode) {
               packet2.field_148919_a = new TextComponentString(sb.toString());
            }
         }
      }

      return true;
   }

   public static double levenshteinDistance(String s1, String s2) {
      String longer = s1;
      String shorter = s2;
      if (s1.length() < s2.length()) {
         longer = s2;
         shorter = s1;
      }

      int longerLength = longer.length();
      return longerLength == 0 ? 1.0D : (double)(longerLength - editDistance(longer, shorter)) / (double)longerLength;
   }

   public static int editDistance(String s1, String s2) {
      s1 = s1.toLowerCase();
      s2 = s2.toLowerCase();
      int[] costs = new int[s2.length() + 1];

      for(int i = 0; i <= s1.length(); ++i) {
         int lastValue = i;

         for(int j = 0; j <= s2.length(); ++j) {
            if (i == 0) {
               costs[j] = j;
            } else if (j > 0) {
               int newValue = costs[j - 1];
               if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                  newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
               }

               costs[j - 1] = lastValue;
               lastValue = newValue;
            }
         }

         if (i > 0) {
            costs[s2.length()] = lastValue;
         }
      }

      return costs[s2.length()];
   }

   public static class Priorchatlist {
      public int Removable;
      public String fulltext;
      public int spammedtimes;

      public Priorchatlist(int Removable, String fulltext, int spammedtimes) {
         this.Removable = Removable;
         this.fulltext = fulltext;
         this.spammedtimes = spammedtimes;
      }
   }
}
