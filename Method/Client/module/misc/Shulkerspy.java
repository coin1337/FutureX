package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import Method.Client.module.render.NameTags;
import Method.Client.utils.visual.RenderUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Shulkerspy extends Module {
   public static ConcurrentHashMap<String, TileEntityShulkerBox> shulkerMap = new ConcurrentHashMap();
   public Setting Mode;
   public Setting X;
   public Setting Y;
   public Setting Scale;
   public Setting Background;
   public Setting BgColor;

   public Shulkerspy() {
      super("Shulkerspy", 0, Category.MISC, "See others last held Shulker");
      this.Mode = Main.setmgr.add(new Setting(" Mode", this, "Dynamic", new String[]{"Dynamic", "Static"}));
      this.X = Main.setmgr.add(new Setting("X", this, 1.0D, 0.0D, 1000.0D, false, this.Mode, "Static", 2));
      this.Y = Main.setmgr.add(new Setting("Y", this, 1.0D, 0.0D, 1000.0D, false, this.Mode, "Static", 3));
      this.Scale = Main.setmgr.add(new Setting("Scale", this, 1.0D, 0.0D, 4.0D, false, this.Mode, "Dynamic", 2));
      this.Background = Main.setmgr.add(new Setting("Background", this, true));
      this.BgColor = Main.setmgr.add(new Setting("BgColor", this, 0.22D, 0.88D, 0.22D, 0.22D, this.Background, 2));
   }

   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (this.Mode.getValString().equalsIgnoreCase("Dynamic")) {
         Iterator var2 = mc.field_71441_e.field_72996_f.iterator();

         while(var2.hasNext()) {
            Entity object = (Entity)var2.next();
            if (object instanceof EntityOtherPlayerMP && shulkerMap.containsKey(object.func_70005_c_().toLowerCase())) {
               IInventory inv = (IInventory)shulkerMap.get(object.func_70005_c_().toLowerCase());
               this.DrawBox((EntityLivingBase)object, inv);
            }
         }
      }

   }

   public void onRenderGameOverlay(Text event) {
      if (this.Mode.getValString().equalsIgnoreCase("Static")) {
         int Players = 0;
         Iterator var3 = mc.field_71441_e.field_72996_f.iterator();

         while(var3.hasNext()) {
            Entity object = (Entity)var3.next();
            if (object instanceof EntityOtherPlayerMP && shulkerMap.containsKey(object.func_70005_c_().toLowerCase())) {
               IInventory inv = (IInventory)shulkerMap.get(object.func_70005_c_().toLowerCase());
               this.DrawSbox(inv, Players, (EntityLivingBase)object);
               ++Players;
            }
         }
      }

   }

   private void DrawSbox(IInventory inv, int players, EntityLivingBase object) {
      RenderHelper.func_74520_c();
      if (this.Background.getValBoolean()) {
         RenderUtils.drawRectDouble(this.X.getValDouble(), this.Y.getValDouble() - (double)(players * 100), this.X.getValDouble() + 88.0D + 60.0D, this.Y.getValDouble() + 50.0D - (double)(players * 100), this.BgColor.getcolor());
      }

      mc.field_71466_p.func_175063_a(object.func_70005_c_() + "'s Shulker", (float)this.X.getValDouble() + 45.0F, (float)this.Y.getValDouble() - 10.0F, -1);

      for(int i = 0; i < inv.func_70302_i_(); ++i) {
         ItemStack itemStack = inv.func_70301_a(i);
         int offsetX = (int)(this.X.getValDouble() + (double)(i % 9 * 16));
         int offsetY = (int)(this.Y.getValDouble() + (double)(i / 9 * 16)) - players * 100;
         mc.func_175599_af().func_180450_b(itemStack, offsetX, offsetY);
         mc.func_175599_af().func_180453_a(mc.field_71466_p, itemStack, offsetX, offsetY, (String)null);
      }

      RenderHelper.func_74518_a();
      mc.func_175599_af().field_77023_b = 0.0F;
   }

   public void DrawBox(EntityLivingBase e, IInventory inv) {
      int morey = ModuleManager.getModuleByName("NameTags").isToggled() ? -6 : 0;
      double scale = Math.max(this.Scale.getValDouble() * (double)(mc.field_71439_g.func_70032_d(e) / 20.0F), 2.0D);

      for(int i = 0; i < inv.func_70302_i_(); ++i) {
         NameTags.drawItem(e.field_70165_t, e.field_70163_u + (double)e.field_70131_O + 0.75D * scale, e.field_70161_v, -2.5D + (double)(i % 9), -((double)(i / 9) * 1.2D) - (double)morey, scale, inv.func_70301_a(i));
      }

   }

   public void onClientTick(ClientTickEvent event) {
      List<Entity> valids = (List)mc.field_71441_e.func_72910_y().stream().filter((en) -> {
         return en instanceof EntityOtherPlayerMP;
      }).filter((mpx) -> {
         return ((EntityOtherPlayerMP)mpx).func_184614_ca().func_77973_b() instanceof ItemShulkerBox;
      }).collect(Collectors.toList());
      Iterator var3 = valids.iterator();

      while(var3.hasNext()) {
         Entity valid = (Entity)var3.next();
         EntityOtherPlayerMP mp = (EntityOtherPlayerMP)valid;
         TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
         ItemShulkerBox shulker = (ItemShulkerBox)mp.func_184614_ca().func_77973_b();
         entityBox.field_145854_h = shulker.func_179223_d();
         entityBox.func_145834_a(mc.field_71441_e);
         ItemStackHelper.func_191283_b(((NBTTagCompound)Objects.requireNonNull(mp.func_184614_ca().func_77978_p())).func_74775_l("BlockEntityTag"), entityBox.field_190596_f);
         entityBox.func_145839_a(mp.func_184614_ca().func_77978_p().func_74775_l("BlockEntityTag"));
         entityBox.func_190575_a(mp.func_184614_ca().func_82837_s() ? mp.func_184614_ca().func_82833_r() : mp.func_70005_c_() + "'s current shulker box");
         shulkerMap.put(mp.func_70005_c_().toLowerCase(), entityBox);
      }

   }
}
