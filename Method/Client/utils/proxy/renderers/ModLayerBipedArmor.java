package Method.Client.utils.proxy.renderers;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModLayerBipedArmor extends ModLayerArmorBase<ModelBiped> {
   public ModLayerBipedArmor(RenderLivingBase<?> rendererIn) {
      super(rendererIn);
   }

   protected void initArmor() {
      this.modelLeggings = new ModelBiped(0.5F);
      this.modelArmor = new ModelBiped(1.0F);
   }

   protected void setModelSlotVisible(ModelBiped modelIn, EntityEquipmentSlot slotIn) {
      this.setModelVisible(modelIn);
      switch(slotIn) {
      case HEAD:
         modelIn.field_78116_c.field_78806_j = true;
         modelIn.field_178720_f.field_78806_j = true;
         break;
      case CHEST:
         modelIn.field_78115_e.field_78806_j = true;
         modelIn.field_178723_h.field_78806_j = true;
         modelIn.field_178724_i.field_78806_j = true;
         break;
      case LEGS:
         modelIn.field_78115_e.field_78806_j = true;
         modelIn.field_178721_j.field_78806_j = true;
         modelIn.field_178722_k.field_78806_j = true;
         break;
      case FEET:
         modelIn.field_178721_j.field_78806_j = true;
         modelIn.field_178722_k.field_78806_j = true;
      }

   }

   protected void setModelVisible(ModelBiped model) {
      model.func_178719_a(false);
   }

   protected ModelBiped getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model) {
      return ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
   }
}
