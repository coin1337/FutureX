package Method.Client.utils.Screens.Override;

import Method.Client.managers.FriendManager;
import Method.Client.module.render.ExtraTab;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.IScoreCriteria.EnumRenderType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;

public class Mixintab extends GuiPlayerTabOverlay {
   protected static Minecraft mc = Minecraft.func_71410_x();
   public static final Ordering<NetworkPlayerInfo> ENTRY_ORDERING;
   public ITextComponent field_175255_h;
   public ITextComponent field_175256_i;
   public long field_175253_j;
   public boolean field_175254_k;

   public static void Run() {
      mc.field_71456_v.field_175196_v = new Mixintab(mc, mc.field_71456_v);
   }

   public Mixintab(Minecraft mcIn, GuiIngame guiIngameIn) {
      super(mcIn, guiIngameIn);
   }

   public String func_175243_a(NetworkPlayerInfo networkPlayerInfoIn) {
      return networkPlayerInfoIn.func_178854_k() != null ? networkPlayerInfoIn.func_178854_k().func_150254_d() : ScorePlayerTeam.func_96667_a(networkPlayerInfoIn.func_178850_i(), networkPlayerInfoIn.func_178845_a().getName());
   }

   public void func_175246_a(boolean willBeRendered) {
      if (willBeRendered && !this.field_175254_k) {
         this.field_175253_j = Minecraft.func_71386_F();
      }

      this.field_175254_k = willBeRendered;
   }

   public void func_175249_a(int width, Scoreboard scoreboardIn, @Nullable ScoreObjective scoreObjectiveIn) {
      NetHandlerPlayClient nethandlerplayclient = mc.field_71439_g.field_71174_a;
      List<NetworkPlayerInfo> list = ENTRY_ORDERING.sortedCopy(nethandlerplayclient.func_175106_d());
      int i = 0;
      int j = 0;
      Iterator var8 = list.iterator();

      int j4;
      while(var8.hasNext()) {
         NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo)var8.next();
         j4 = mc.field_71466_p.func_78256_a(this.func_175243_a(networkplayerinfo));
         i = Math.max(i, j4);
         if (scoreObjectiveIn != null && scoreObjectiveIn.func_178766_e() != EnumRenderType.HEARTS) {
            j4 = mc.field_71466_p.func_78256_a(" " + scoreboardIn.func_96529_a(networkplayerinfo.func_178845_a().getName(), scoreObjectiveIn).func_96652_c());
            j = Math.max(j, j4);
         }
      }

      list = list.subList(0, (int)Math.min((double)list.size(), ExtraTab.Players.getValDouble()));
      int l3 = list.size();
      int i4 = l3;

      for(j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4) {
         ++j4;
      }

      boolean flag = mc.func_71387_A() || ((NetHandlerPlayClient)Objects.requireNonNull(mc.func_147114_u())).func_147298_b().func_179292_f();
      int l;
      if (scoreObjectiveIn != null) {
         if (scoreObjectiveIn.func_178766_e() == EnumRenderType.HEARTS) {
            l = 90;
         } else {
            l = j;
         }
      } else {
         l = 0;
      }

      int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
      int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
      int k1 = 10;
      int l1 = i1 * j4 + (j4 - 1) * 5;
      List<String> list1 = null;
      if (this.field_175256_i != null) {
         list1 = mc.field_71466_p.func_78271_c(this.field_175256_i.func_150254_d(), width - 50);

         String s;
         for(Iterator var18 = list1.iterator(); var18.hasNext(); l1 = Math.max(l1, mc.field_71466_p.func_78256_a(s))) {
            s = (String)var18.next();
         }
      }

      List<String> list2 = null;
      if (this.field_175255_h != null) {
         list2 = mc.field_71466_p.func_78271_c(this.field_175255_h.func_150254_d(), width - 50);

         String s1;
         for(Iterator var35 = list2.iterator(); var35.hasNext(); l1 = Math.max(l1, mc.field_71466_p.func_78256_a(s1))) {
            s1 = (String)var35.next();
         }
      }

      if (list1 != null) {
         k1 = this.getK1(width, k1, l1, list1);
         ++k1;
      }

      func_73734_a(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);

      for(int k4 = 0; k4 < l3; ++k4) {
         int l4 = k4 / i4;
         int i5 = k4 % i4;
         int j2 = j1 + l4 * i1 + l4 * 5;
         int k2 = k1 + i5 * 9;
         func_73734_a(j2, k2, j2 + i1, k2 + 8, 553648127);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179141_d();
         GlStateManager.func_179147_l();
         GlStateManager.func_187428_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
         if (k4 < list.size()) {
            NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo)list.get(k4);
            GameProfile gameprofile = networkplayerinfo1.func_178845_a();
            int l5;
            if (flag) {
               EntityPlayer entityplayer = mc.field_71441_e.func_152378_a(gameprofile.getId());
               boolean flag1 = entityplayer != null && entityplayer.func_175148_a(EnumPlayerModelParts.CAPE) && ("Dinnerbone".equals(gameprofile.getName()) || "Grumm".equals(gameprofile.getName()));
               mc.func_110434_K().func_110577_a(networkplayerinfo1.func_178837_g());
               l5 = 8 + (flag1 ? 8 : 0);
               int i3 = 8 * (flag1 ? -1 : 1);
               Gui.func_152125_a(j2, k2, 8.0F, (float)l5, 8, i3, 8, 8, 64.0F, 64.0F);
               if (entityplayer != null && entityplayer.func_175148_a(EnumPlayerModelParts.HAT)) {
                  int j3 = 8 + (flag1 ? 8 : 0);
                  int k3 = 8 * (flag1 ? -1 : 1);
                  Gui.func_152125_a(j2, k2, 40.0F, (float)j3, 8, k3, 8, 8, 64.0F, 64.0F);
               }

               j2 += 9;
            }

            String s4 = this.func_175243_a(networkplayerinfo1);
            if (networkplayerinfo1.func_178848_b() == GameType.SPECTATOR) {
               mc.field_71466_p.func_175063_a(TextFormatting.ITALIC + s4, (float)j2, (float)k2, -1862270977);
            } else if (FriendManager.isFriend(s4)) {
               mc.field_71466_p.func_175063_a(ExtraTab.FriendObfus.getValBoolean() ? "Friend" : s4, (float)j2, (float)k2, ExtraTab.FriendColor.getcolor());
            } else {
               mc.field_71466_p.func_175063_a(s4, (float)j2, (float)k2, -1);
            }

            if (scoreObjectiveIn != null && networkplayerinfo1.func_178848_b() != GameType.SPECTATOR) {
               int k5 = j2 + i + 1;
               l5 = k5 + l;
               if (l5 - k5 > 5) {
                  this.drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
               }
            }

            this.func_175245_a(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
         }
      }

      if (list2 != null) {
         k1 = k1 + i4 * 9 + 1;
         this.getK1(width, k1, l1, list2);
      }

   }

   private int getK1(int width, int k1, int l1, List<String> list1) {
      func_73734_a(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * mc.field_71466_p.field_78288_b, Integer.MIN_VALUE);

      for(Iterator var5 = list1.iterator(); var5.hasNext(); k1 += mc.field_71466_p.field_78288_b) {
         String s2 = (String)var5.next();
         int i2 = mc.field_71466_p.func_78256_a(s2);
         mc.field_71466_p.func_175063_a(s2, (float)(width / 2 - i2 / 2), (float)k1, -1);
      }

      return k1;
   }

   protected void func_175245_a(int offset, int x, int yval, NetworkPlayerInfo networkPlayerInfoIn) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (ExtraTab.ReplaceBar.getValBoolean()) {
         this.field_73735_i += 100.0F;
         mc.field_71466_p.func_175063_a(String.valueOf(networkPlayerInfoIn.func_178853_c()), (float)(x + offset - 11), (float)yval, -1);
         this.field_73735_i -= 100.0F;
      } else {
         mc.func_110434_K().func_110577_a(field_110324_m);
         byte j;
         if (networkPlayerInfoIn.func_178853_c() < 0) {
            j = 5;
         } else if (networkPlayerInfoIn.func_178853_c() < 150) {
            j = 0;
         } else if (networkPlayerInfoIn.func_178853_c() < 300) {
            j = 1;
         } else if (networkPlayerInfoIn.func_178853_c() < 600) {
            j = 2;
         } else if (networkPlayerInfoIn.func_178853_c() < 1000) {
            j = 3;
         } else {
            j = 4;
         }

         this.field_73735_i += 100.0F;
         this.func_73729_b(x + offset - 11, yval, 0, 176 + j * 8, 10, 8);
         this.field_73735_i -= 100.0F;
      }
   }

   private void drawScoreboardValues(ScoreObjective objective, int p_175247_2_, String name, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo info) {
      int i = objective.func_96682_a().func_96529_a(name, objective).func_96652_c();
      if (objective.func_178766_e() == EnumRenderType.HEARTS) {
         mc.func_110434_K().func_110577_a(field_110324_m);
         if (this.field_175253_j == info.func_178855_p()) {
            if (i < info.func_178835_l()) {
               info.func_178846_a(Minecraft.func_71386_F());
               info.func_178844_b((long)(this.field_175251_g.func_73834_c() + 20));
            } else if (i > info.func_178835_l()) {
               info.func_178846_a(Minecraft.func_71386_F());
               info.func_178844_b((long)(this.field_175251_g.func_73834_c() + 10));
            }
         }

         if (Minecraft.func_71386_F() - info.func_178847_n() > 1000L || this.field_175253_j != info.func_178855_p()) {
            info.func_178836_b(i);
            info.func_178857_c(i);
            info.func_178846_a(Minecraft.func_71386_F());
         }

         info.func_178843_c(this.field_175253_j);
         info.func_178836_b(i);
         int j = MathHelper.func_76123_f((float)Math.max(i, info.func_178860_m()) / 2.0F);
         int k = Math.max(MathHelper.func_76123_f((float)(i / 2)), Math.max(MathHelper.func_76123_f((float)(info.func_178860_m() / 2)), 10));
         boolean flag = info.func_178858_o() > (long)this.field_175251_g.func_73834_c() && (info.func_178858_o() - (long)this.field_175251_g.func_73834_c()) / 3L % 2L == 1L;
         if (j > 0) {
            float f = Math.min((float)(p_175247_5_ - p_175247_4_ - 4) / (float)k, 9.0F);
            if (f > 3.0F) {
               int j1;
               for(j1 = j; j1 < k; ++j1) {
                  this.func_175174_a((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
               }

               for(j1 = 0; j1 < j; ++j1) {
                  this.func_175174_a((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                  if (flag) {
                     if (j1 * 2 + 1 < info.func_178860_m()) {
                        this.func_175174_a((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, 70, 0, 9, 9);
                     }

                     if (j1 * 2 + 1 == info.func_178860_m()) {
                        this.func_175174_a((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, 79, 0, 9, 9);
                     }
                  }

                  if (j1 * 2 + 1 < i) {
                     this.func_175174_a((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, j1 >= 10 ? 160 : 52, 0, 9, 9);
                  }

                  if (j1 * 2 + 1 == i) {
                     this.func_175174_a((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, j1 >= 10 ? 169 : 61, 0, 9, 9);
                  }
               }
            } else {
               float f1 = MathHelper.func_76131_a((float)i / 20.0F, 0.0F, 1.0F);
               int i1 = (int)((1.0F - f1) * 255.0F) << 16 | (int)(f1 * 255.0F) << 8;
               String s = "" + (float)i / 2.0F;
               if (p_175247_5_ - mc.field_71466_p.func_78256_a(s + "hp") >= p_175247_4_) {
                  s = s + "hp";
               }

               mc.field_71466_p.func_175063_a(s, (float)((p_175247_5_ + p_175247_4_) / 2 - mc.field_71466_p.func_78256_a(s) / 2), (float)p_175247_2_, i1);
            }
         }
      } else {
         String s1 = TextFormatting.YELLOW + "" + i;
         mc.field_71466_p.func_175063_a(s1, (float)(p_175247_5_ - mc.field_71466_p.func_78256_a(s1)), (float)p_175247_2_, 16777215);
      }

   }

   public void func_175248_a(@Nullable ITextComponent footerIn) {
      this.field_175255_h = footerIn;
   }

   public void func_175244_b(@Nullable ITextComponent headerIn) {
      this.field_175256_i = headerIn;
   }

   public void func_181030_a() {
      this.field_175256_i = null;
      this.field_175255_h = null;
   }

   static {
      ENTRY_ORDERING = GuiPlayerTabOverlay.field_175252_a;
   }
}
