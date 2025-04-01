package xyz.iwolfking.createfiltersanywhere.mixin.compat.refinedstorage;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.resource.filter.Filter;
import com.refinedmods.refinedstorage.api.resource.filter.FilterMode;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.simibubi.create.content.logistics.filter.FilterItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;

import java.util.Set;
import java.util.function.UnaryOperator;

@Mixin(value = Filter.class, remap = false)
public class MixinFilter {
    @Shadow private UnaryOperator<ResourceKey> normalizer;

    @Shadow @Final private Set<ResourceKey> filters;

    @Shadow private FilterMode mode;

    @Inject(method = "isAllowed", at = @At("HEAD"), cancellable = true)
    private void checkCreateFilter(ResourceKey resource, CallbackInfoReturnable<Boolean> cir) {
        final ResourceKey normalized = normalizer.apply(resource);
        if(normalized instanceof ItemResource normalizedItem) {
            for(ResourceKey filter : filters) {
                if(filter instanceof ItemResource itemResource) {
                    if(itemResource.item() instanceof FilterItem) {
                        if(mode.equals(FilterMode.ALLOW)) {
                            cir.setReturnValue(CFATests.checkFilter(normalizedItem.toItemStack(), itemResource.toItemStack(), true, null));
                            return;
                        }
                        else {
                            cir.setReturnValue(!CFATests.checkFilter(normalizedItem.toItemStack(), itemResource.toItemStack(), true, null));
                            return;
                        }
                    }
                    else {
                        cir.setReturnValue(switch (mode) {
                            case ALLOW -> filters.contains(normalized);
                            case BLOCK -> !filters.contains(normalized);
                        });
                        return;
                    }
                }
            }
        }
    }
}
