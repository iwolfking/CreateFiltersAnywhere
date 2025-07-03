package xyz.iwolfking.createfiltersanywhere.mixin.compat.oritech;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.logistics.filter.FilterItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rearth.oritech.block.entity.pipes.ItemFilterBlockEntity;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;

@Mixin(value = ItemFilterBlockEntity.FilterBlockInventory.class, remap = false)
public class MixinItemFilterBlockEntity {
    @Shadow @Final private ItemFilterBlockEntity this$0;

    @Inject(method = "canInsert", at = @At(value = "INVOKE", target = "Ljava/lang/Object;equals(Ljava/lang/Object;)Z", ordinal = 0), cancellable = true)
    private void checkCreateFilterInOritechFilter(ItemStack stack, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) ItemStack filterItem, @Local(ordinal = 2) boolean matchesFilterItems, @Local(ordinal = 1) boolean matchComponents, @Local(ordinal = 0) boolean matchNbt) {
        if(filterItem.getItem() instanceof FilterItem) {
            if(!matchComponents && !matchNbt) {
                if(this$0.getFilterSettings().useWhitelist()) {
                    cir.setReturnValue(CFATests.checkFilter(stack, filterItem, true, null));
                }
                else {
                    cir.setReturnValue(!CFATests.checkFilter(stack, filterItem, true, null));
                }

            }
        }
    }
}
