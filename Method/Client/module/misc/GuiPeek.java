package Method.Client.module.misc;

import Method.Client.module.Category;
import Method.Client.module.Module;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiPeek extends Module {
   private String name = "";
   private boolean box = false;
   private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");

   public GuiPeek() {
      super("Gui Peek", 0, Category.MISC, "Peek into maps/shulker ");
   }

   public void ItemTooltipEvent(ItemTooltipEvent event) {
      if (this.box) {
         event.getToolTip().clear();
         event.getToolTip().add(this.name);
      }

      if (event.getItemStack().func_77973_b() instanceof ItemMap) {
         event.getToolTip().clear();
         event.getToolTip().add(event.getItemStack().func_82833_r());
      }

   }

   public void postDrawScreen(Post event) {
      if (event.getGui() instanceof GuiContainer && ((GuiContainer)event.getGui()).getSlotUnderMouse() != null) {
         ItemStack item = ((Slot)Objects.requireNonNull(((GuiContainer)event.getGui()).getSlotUnderMouse())).func_75211_c();
         if (item.func_77973_b() instanceof ItemShulkerBox) {
            this.name = item.func_82833_r();
            this.box = true;
            int X = event.getMouseX() + 8;
            int Y = event.getMouseY() - 82;
            NBTTagList nbttaglist = ((NBTTagCompound)Objects.requireNonNull(item.func_77942_o() ? item.func_77978_p() : new NBTTagCompound())).func_74775_l("BlockEntityTag").func_150295_c("Items", 10);
            int xMod = 6;
            int yMod = 6;
            GlStateManager.func_179141_d();
            GlStateManager.func_179147_l();
            GL11.glDisable(2929);
            GlStateManager.func_179131_c(2.0F, 2.0F, 2.0F, 1.0F);
            mc.func_110434_K().func_110577_a(new ResourceLocation("futurex", "shulker.png"));
            mc.field_71456_v.func_73729_b(X, Y, 0, 0, 172, 64);
            GL11.glEnable(2929);
            Iterator var8 = nbttaglist.iterator();

            while(true) {
               if (!var8.hasNext()) {
                  GL11.glDepthFunc(515);
                  break;
               }

               NBTBase list = (NBTBase)var8.next();
               ItemStack stack = new ItemStack((NBTTagCompound)list);
               String stringofitems = list.toString();
               int slotnum = Integer.parseInt(stringofitems.substring(stringofitems.lastIndexOf("{Slot:") + 1, stringofitems.indexOf("b,")).replaceAll("[^0-9]", ""));
               if (slotnum > 8 && slotnum < 17) {
                  xMod = -156;
                  yMod = 24;
               } else if (slotnum > 17) {
                  xMod = -318;
                  yMod = 42;
               }

               GlStateManager.func_179094_E();
               GL11.glDepthFunc(517);
               RenderHelper.func_74518_a();
               RenderHelper.func_74520_c();
               mc.func_175599_af().func_180450_b(stack, X + xMod + 18 * slotnum, Y + yMod);
               String string = Integer.toString(stack.func_190916_E());
               if (stack.func_190916_E() == 1) {
                  string = "";
               }

               mc.func_175599_af().func_180453_a(mc.field_71466_p, stack, X + xMod + 18 * slotnum, Y + yMod, string);
               GlStateManager.func_179121_F();
            }
         }

         if (mc.field_71439_g.field_71071_by.func_70445_o().func_77973_b() instanceof ItemAir) {
            Slot slotUnderMouse = ((GuiContainer)event.getGui()).getSlotUnderMouse();
            if (slotUnderMouse != null && slotUnderMouse.func_75216_d()) {
               ItemStack itemUnderMouse = slotUnderMouse.func_75211_c();
               if (itemUnderMouse.func_77973_b() instanceof ItemMap) {
                  MapData mapdata = ((ItemMap)itemUnderMouse.func_77973_b()).func_77873_a(itemUnderMouse, mc.field_71441_e);
                  if (mapdata != null) {
                     GlStateManager.func_179097_i();
                     GlStateManager.func_179140_f();
                     mc.func_110434_K().func_110577_a(RES_MAP_BACKGROUND);
                     Tessellator tessellator = Tessellator.func_178181_a();
                     BufferBuilder bufferbuilder = tessellator.func_178180_c();
                     GlStateManager.func_179137_b((double)event.getMouseX(), (double)event.getMouseY() + 15.5D, 0.0D);
                     GlStateManager.func_179139_a(0.5D, 0.5D, 1.0D);
                     bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
                     bufferbuilder.func_181662_b(-7.0D, 135.0D, 0.0D).func_187315_a(0.0D, 1.0D).func_181675_d();
                     bufferbuilder.func_181662_b(135.0D, 135.0D, 0.0D).func_187315_a(1.0D, 1.0D).func_181675_d();
                     bufferbuilder.func_181662_b(135.0D, -7.0D, 0.0D).func_187315_a(1.0D, 0.0D).func_181675_d();
                     bufferbuilder.func_181662_b(-7.0D, -7.0D, 0.0D).func_187315_a(0.0D, 0.0D).func_181675_d();
                     tessellator.func_78381_a();
                     mc.field_71460_t.func_147701_i().func_148250_a(mapdata, true);
                     GlStateManager.func_179145_e();
                     GlStateManager.func_179126_j();
                  }
               }
            }
         }
      } else {
         this.box = false;
      }

   }

   public void onClientTick(ClientTickEvent event) {
      ItemStack itemStack = mc.field_71439_g.func_184614_ca();
      if (itemStack.func_77973_b() instanceof ItemShulkerBox && Mouse.getEventButton() == 1) {
         Peekcode(itemStack, mc);
      }

   }

   public static void Peekcode(ItemStack itemStack, Minecraft mc) {
      TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
      entityBox.field_145854_h = ((ItemShulkerBox)itemStack.func_77973_b()).func_179223_d();
      entityBox.func_145834_a(mc.field_71441_e);

      assert itemStack.func_77978_p() != null;

      entityBox.func_145839_a(itemStack.func_77978_p().func_74775_l("BlockEntityTag"));
      entityBox.func_190575_a("Shulker Peek");
      mc.field_71439_g.func_71007_a(entityBox);
   }
}
