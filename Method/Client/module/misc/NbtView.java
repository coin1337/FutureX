package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class NbtView extends Module {
   private static int line_scrolled = 0;
   private static int time = 0;
   static Setting maxLinesShown;
   static Setting ticksBeforeScroll;
   static Setting showDelimiters;
   static Setting showSeparator;
   static Setting compress;
   private boolean readytocopy = false;
   private static final String FORMAT;

   public NbtView() {
      super("Nbt View", 0, Category.MISC, "Show Nbt Data,Press alt to copy");
   }

   public void onEnable() {
      MinecraftForge.EVENT_BUS.register(this);
   }

   public void onDisable() {
      MinecraftForge.EVENT_BUS.unregister(this);
   }

   public void setup() {
      Main.setmgr.add(maxLinesShown = new Setting("maxLinesShown", this, 10.0D, 1.0D, 100.0D, true));
      Main.setmgr.add(ticksBeforeScroll = new Setting("ticksBeforeScroll", this, 20.0D, 1.0D, 400.0D, true));
      Main.setmgr.add(showDelimiters = new Setting("showDelimiters", this, true));
      Main.setmgr.add(showSeparator = new Setting("showSeparator", this, true));
      Main.setmgr.add(compress = new Setting("compress", this, true));
   }

   public void onRightClickBlock(RightClickBlock event) {
      if (event.getWorld().field_72995_K || !event.getWorld().field_72995_K) {
         ItemStack stack = event.getEntityPlayer().func_184586_b(event.getHand());
         if (event.getWorld().func_175625_s(event.getPos()) != null && stack.func_77973_b() == Items.field_151032_g) {
            ArrayList<String> tag = new ArrayList();
            unwrapTag(tag, ((TileEntity)Objects.requireNonNull(event.getWorld().func_175625_s(event.getPos()))).func_189515_b(new NBTTagCompound()), "", "", "\t");
            StringBuilder sb = new StringBuilder();
            tag.forEach((s) -> {
               sb.append(s);
               sb.append('\n');
            });
            new NbtView.InfoWindow(sb.toString(), event.getWorld().field_72995_K);
         }
      }

   }

   public void onClientTick(ClientTickEvent event) {
      if (GuiScreen.func_175283_s()) {
         this.readytocopy = true;
      }

      if (!GuiScreen.func_146272_n()) {
         ++time;
         if ((double)time >= ticksBeforeScroll.getValDouble() / (double)(GuiScreen.func_175283_s() ? 4 : 1)) {
            time = 0;
            ++line_scrolled;
         }
      }

   }

   public void ItemTooltipEvent(ItemTooltipEvent event) {
      NBTTagCompound tag = event.getItemStack().func_77978_p();
      ArrayList<String> ttip = new ArrayList((int)maxLinesShown.getValDouble());
      if (tag != null) {
         if (this.readytocopy) {
            StringSelection selection = new StringSelection(tag.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            this.readytocopy = false;
         }

         event.getToolTip().add("");
         if (showDelimiters.getValBoolean()) {
            ttip.add(TextFormatting.DARK_PURPLE + " - nbt start -");
         }

         if (compress.getValBoolean()) {
            ttip.add(FORMAT + tag.toString());
         } else {
            unwrapTag(ttip, tag, FORMAT, "", compress.getValBoolean() ? "" : "  ");
         }

         if (showDelimiters.getValBoolean()) {
            ttip.add(TextFormatting.DARK_PURPLE + " - nbt end -");
         }

         ttip = transformTtip(ttip);
         event.getToolTip().addAll(ttip);
      } else {
         event.getToolTip().add(FORMAT + "No NBT tag");
      }

   }

   private static ArrayList<String> transformTtip(ArrayList<String> ttip) {
      ArrayList<String> newttip = new ArrayList((int)maxLinesShown.getValDouble());
      if (showSeparator.getValBoolean()) {
         newttip.add("- NBTView -");
      }

      if ((double)ttip.size() > maxLinesShown.getValDouble()) {
         if (maxLinesShown.getValDouble() + (double)line_scrolled > (double)ttip.size()) {
            line_scrolled = 0;
         }

         for(int i = 0; (double)i < maxLinesShown.getValDouble(); ++i) {
            newttip.add(ttip.get(i + line_scrolled));
         }
      } else {
         line_scrolled = 0;
         newttip.addAll(ttip);
      }

      return newttip;
   }

   static void unwrapTag(List<String> tooltip, NBTBase base, String pad, @Nonnull String tagName, String padIncrement) {
      if (base.func_74732_a() == 10) {
         NBTTagCompound tag = (NBTTagCompound)base;
         tag.func_150296_c().forEach((s) -> {
            boolean nested = tag.func_74781_a(s).func_74732_a() == 10 || tag.func_74781_a(s).func_74732_a() == 9;
            if (nested) {
               tooltip.add(pad + s + ": {");
               unwrapTag(tooltip, tag.func_74781_a(s), pad + padIncrement, s, padIncrement);
               tooltip.add(pad + "}");
            } else {
               addValueToTooltip(tooltip, tag.func_74781_a(s), s, pad);
            }

         });
      } else if (base.func_74732_a() == 9) {
         NBTTagList tag = (NBTTagList)base;
         int index = 0;

         for(Iterator var7 = tag.iterator(); var7.hasNext(); ++index) {
            NBTBase nbtnext = (NBTBase)var7.next();
            if (nbtnext.func_74732_a() != 10 && nbtnext.func_74732_a() != 9) {
               tooltip.add(pad + "[" + index + "] -> " + nbtnext.toString());
            } else {
               tooltip.add(pad + "[" + index + "]: {");
               unwrapTag(tooltip, nbtnext, pad + padIncrement, "", padIncrement);
               tooltip.add(pad + "}");
            }
         }
      } else {
         addValueToTooltip(tooltip, base, tagName, pad);
      }

   }

   private static void addValueToTooltip(List<String> tooltip, NBTBase nbt, String name, String pad) {
      tooltip.add(pad + name + ": " + nbt.toString());
   }

   static {
      FORMAT = TextFormatting.ITALIC.toString() + TextFormatting.DARK_GRAY;
   }

   public static class InfoWindow extends Frame {
      private static final long serialVersionUID = 8935325049409596603L;
      private static NbtView.InfoWindow client = null;
      private static NbtView.InfoWindow server = null;
      protected boolean isRemote;

      public InfoWindow(String tag, boolean isRemote) {
         this.setSize(400, 300);
         this.setTitle(I18n.func_135052_a("reader.window", new Object[]{I18n.func_135052_a("reader.side_" + (isRemote ? "client" : "server"), new Object[0])}));
         TextArea ta = new TextArea(tag);
         this.add(ta);
         this.isRemote = isRemote;
         this.setAutoRequestFocus(false);
         this.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
               InfoWindow.this.setVisible(false);
               InfoWindow.this.dispose();
               if (InfoWindow.this.isRemote) {
                  NbtView.InfoWindow.client = null;
               } else {
                  NbtView.InfoWindow.server = null;
               }

            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }
         });
         if (isRemote) {
            if (client != null && client.isValid()) {
               this.setBounds(client.getX(), client.getY(), client.getWidth(), client.getHeight());
               client.setVisible(false);
               client.dispose();
            }

            client = this;
         } else {
            if (server != null && server.isValid()) {
               this.setBounds(server.getX(), server.getY(), server.getWidth(), server.getHeight());
               server.setVisible(false);
               server.dispose();
            }

            server = this;
         }

         this.setVisible(true);
      }
   }
}
