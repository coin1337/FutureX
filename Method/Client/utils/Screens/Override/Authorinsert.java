package Method.Client.utils.Screens.Override;

import Method.Client.utils.visual.ChatUtils;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;

public class Authorinsert extends GuiScreen {
   GuiTextField textbox;

   public void func_73866_w_() {
      this.textbox = new GuiTextField(0, this.field_146297_k.field_71466_p, (int)((double)this.field_146297_k.field_71443_c / 5.4D), 180, 240, this.field_146297_k.field_71443_c / 100);
      this.textbox.func_146195_b(true);
      this.func_189646_b(new GuiButton(200, this.field_146294_l / 2 - 50, this.field_146295_m / 4 + 120, 120, 20, "Done"));
   }

   public void doitnow() {
      if (!this.field_146297_k.field_71439_g.field_71075_bZ.field_75098_d) {
         ChatUtils.error("Creative mode only.");
         this.field_146297_k.func_147108_a((GuiScreen)null);
      } else {
         ItemStack heldItem = this.field_146297_k.field_71439_g.field_71071_by.func_70448_g();
         int heldItemID = Item.func_150891_b(heldItem.func_77973_b());
         int writtenBookID = Item.func_150891_b(Items.field_151164_bB);
         if (heldItemID != writtenBookID) {
            ChatUtils.error("You must hold a written book in your main hand.");
         } else {
            heldItem.func_77983_a("author", new NBTTagString(this.textbox.func_146179_b()));
            ChatUtils.message("Author Changed! Open Inventory.");
         }

         this.field_146297_k.func_147108_a((GuiScreen)null);
      }
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146289_q, "Change Author", this.field_146294_l / 2, 40, 16777215);
      this.textbox.func_146194_f();
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      this.textbox.func_146192_a(mouseX, mouseY, mouseButton);
   }

   protected void func_73869_a(char typedChar, int keyCode) {
      if (keyCode == 1) {
         this.field_146297_k.func_147108_a((GuiScreen)null);
      } else {
         this.textbox.func_146201_a(typedChar, keyCode);
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146127_k == 200) {
         this.doitnow();
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}
