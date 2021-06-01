package Method.Client.module.Onscreen.Display;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.visual.RenderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public final class CombatItems extends Module {
   static Setting BgColor;
   static Setting background;
   static Setting xpos;
   static Setting ypos;

   public CombatItems() {
      super("CombatItems", 0, Category.ONSCREEN, "CombatItems");
   }

   public void setup() {
      this.visible = false;
      Main.setmgr.add(BgColor = new Setting("BgColor", this, 0.22D, 0.88D, 0.22D, 0.22D));
      Main.setmgr.add(background = new Setting("background", this, false));
      Main.setmgr.add(xpos = new Setting("xpos", this, 200.0D, -20.0D, (double)(mc.field_71443_c + 40), true));
      Main.setmgr.add(ypos = new Setting("ypos", this, 110.0D, -20.0D, (double)(mc.field_71440_d + 40), true));
   }

   public void onEnable() {
      PinableFrame.Toggle("CombatItemsSET", true);
   }

   public void onDisable() {
      PinableFrame.Toggle("CombatItemsSET", false);
   }

   public static class CombatItemsRUN extends PinableFrame {
      ArrayList<ItemStack> itemStacks = new ArrayList();
      ArrayList<Item> Itemslist = new ArrayList();

      public CombatItemsRUN() {
         super("CombatItemsSET", new String[0], (int)CombatItems.ypos.getValDouble(), (int)CombatItems.xpos.getValDouble());
      }

      public void setup() {
         this.x = (int)CombatItems.xpos.getValDouble();
         this.y = (int)CombatItems.ypos.getValDouble();
      }

      public void Ongui() {
         if (!this.getDrag()) {
            this.x = (int)CombatItems.xpos.getValDouble();
            this.y = (int)CombatItems.ypos.getValDouble();
         } else {
            CombatItems.xpos.setValDouble((double)this.x);
            CombatItems.ypos.setValDouble((double)this.y);
         }

      }

      public void setupitems() {
         this.itemStacks.clear();
         this.Itemslist.clear();
         this.itemStacks.add(new ItemStack(Items.field_151032_g, 1));
         this.itemStacks.add(new ItemStack(Items.field_185158_cP, 1));
         this.itemStacks.add(new ItemStack(Items.field_151153_ao, 1, 1));
         this.itemStacks.add(new ItemStack(Items.field_190929_cY, 1));
         this.itemStacks.add(new ItemStack(Items.field_151062_by, 1));
         this.itemStacks.add(new ItemStack(Items.field_151079_bi, 1));
         this.itemStacks.add(new ItemStack(Items.field_185161_cS, 1));
         this.itemStacks.add(new ItemStack(Item.func_150899_d(49), 1));
         this.Itemslist.add(Items.field_185158_cP);
         this.Itemslist.add(Items.field_151032_g);
         this.Itemslist.add(Items.field_151153_ao);
         this.Itemslist.add(Items.field_190929_cY);
         this.Itemslist.add(Items.field_151062_by);
         this.Itemslist.add(Items.field_185161_cS);
         this.Itemslist.add(Item.func_150899_d(49));
         this.Itemslist.add(Items.field_185167_i);
         this.Itemslist.add(Items.field_185166_h);
      }

      public void onRenderGameOverlay(Text event) {
         if (this.mc.field_71439_g != null) {
            this.setupitems();
            Iterator var2 = this.mc.field_71439_g.field_71071_by.field_70462_a.iterator();

            while(true) {
               ItemStack itemStack;
               do {
                  if (!var2.hasNext()) {
                     int offset = 0;
                     RenderHelper.func_74520_c();
                     Iterator var7 = this.itemStacks.iterator();

                     while(var7.hasNext()) {
                        ItemStack itemStack = (ItemStack)var7.next();
                        itemStack.func_190920_e(itemStack.func_190916_E() - 1);
                        if (itemStack.func_190916_E() >= 1) {
                           this.mc.func_175599_af().func_180450_b(itemStack, this.getX() + offset, this.getY() - 3);
                           this.mc.func_175599_af().func_180453_a(this.mc.field_71466_p, itemStack, this.getX() + offset, this.getY() - 3, (String)null);
                           offset += 19;
                        }
                     }

                     if (CombatItems.background.getValBoolean()) {
                        RenderUtils.drawRectDouble((double)this.getX(), (double)this.getY(), (double)(this.getX() + offset + 10), (double)(this.getY() + 20), CombatItems.BgColor.getcolor());
                     }

                     RenderHelper.func_74518_a();
                     this.mc.func_175599_af().field_77023_b = 0.0F;
                     super.onRenderGameOverlay(event);
                     return;
                  }

                  itemStack = (ItemStack)var2.next();
               } while(!this.Itemslist.contains(itemStack.func_77973_b()));

               Iterator var4 = this.itemStacks.iterator();

               while(var4.hasNext()) {
                  ItemStack stack = (ItemStack)var4.next();
                  if (itemStack.func_77973_b().equals(Items.field_185167_i) || itemStack.func_77973_b().equals(Items.field_185166_h) && stack.func_77973_b().equals(Items.field_151032_g)) {
                     stack.func_190920_e(stack.func_190916_E() + itemStack.func_190916_E());
                  }

                  if (Objects.equals(stack.func_77973_b().getRegistryName(), itemStack.func_77973_b().getRegistryName())) {
                     stack.func_190920_e(stack.func_190916_E() + itemStack.func_190916_E());
                  }
               }
            }
         }
      }
   }
}
