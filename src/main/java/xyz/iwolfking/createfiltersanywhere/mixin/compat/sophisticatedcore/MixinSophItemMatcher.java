package xyz.iwolfking.createfiltersanywhere.mixin.compat.sophisticatedcore;

import com.simibubi.create.content.logistics.filter.FilterItem;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.Config;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;


@Mixin(FilterLogic.class)
public class MixinSophItemMatcher {
    @Inject(method = "stackMatchesFilter", at = @At("HEAD"), cancellable = true, remap = false)
    public void sophFilterMatcher(ItemStack stack, ItemStack filter, CallbackInfoReturnable<Boolean> cir) {
        if (Config.BACKPACKS_COMPAT.get() && CFAFilterSelector.isSupportedFilterStack(filter)) {
            cir.setReturnValue(CFAFilterSelector.doFilterTest(stack, filter));
        }
    }
}
