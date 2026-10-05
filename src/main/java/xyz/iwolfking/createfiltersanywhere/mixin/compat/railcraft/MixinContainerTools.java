package xyz.iwolfking.createfiltersanywhere.mixin.compat.railcraft;

import mods.railcraft.util.container.ContainerTools;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;

@Mixin(value = ContainerTools.class, remap = false)
public class MixinContainerTools {

    @Inject(
        method = "matchesFilter",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void cfa$matchCreateFilter(ItemStack filter, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (CFAFilterSelector.isSupportedFilterStack(filter)) {
            cir.setReturnValue(CFAFilterSelector.doFilterTest(stack, filter));
        }
    }
}