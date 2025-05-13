package xyz.iwolfking.createfiltersanywhere.mixin.compat.vanilla;

import com.simibubi.create.content.logistics.filter.FilterItem;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;

@Mixin(value = Allay.class)
public class MixinAllayEntity {
    @Shadow @Final private SimpleContainer inventory;

    @Inject(method = "allayConsidersItemEqual", at = @At("HEAD"), cancellable = true)
    private void allayConsidersCreateFiltersDifferently(ItemStack first, ItemStack second, CallbackInfoReturnable<Boolean> cir) {
        if(first.getItem() instanceof FilterItem) {
            cir.setReturnValue(CFATests.checkFilter(second, first, true, null));
        }
    }
}
