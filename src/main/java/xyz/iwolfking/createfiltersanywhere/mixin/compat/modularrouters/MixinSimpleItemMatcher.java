package xyz.iwolfking.createfiltersanywhere.mixin.compat.modularrouters;

import me.desht.modularrouters.api.matching.IModuleFlags;
import me.desht.modularrouters.logic.filter.matchers.SimpleItemMatcher;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.Config;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;


@Mixin(value = SimpleItemMatcher.class, remap = false)
public class MixinSimpleItemMatcher {
    @Shadow
    @Final
    private ItemStack filterStack;

    @Inject(method = "matchItem", at = @At("HEAD"), cancellable = true)
    public void createFilterMatcher(ItemStack stack, IModuleFlags flags, HolderLookup.Provider registryAccess, CallbackInfoReturnable<Boolean> cir) {
        if (Config.MR_COMPAT.get() && CFAFilterSelector.isSupportedFilterStack(filterStack)) {
            cir.setReturnValue(CFAFilterSelector.doFilterTest(stack, this.filterStack));
        }
    }
}
