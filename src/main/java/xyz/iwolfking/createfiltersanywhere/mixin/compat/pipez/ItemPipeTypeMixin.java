package xyz.iwolfking.createfiltersanywhere.mixin.compat.pipez;

import com.simibubi.create.content.logistics.filter.FilterItem;
import de.maxhenkel.pipez.Filter;
import de.maxhenkel.pipez.blocks.tileentity.types.ItemPipeType;
import de.maxhenkel.pipez.corelib.tag.SingleElementTag;
import de.maxhenkel.pipez.utils.ComponentUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;
import xyz.iwolfking.createfiltersanywhere.api.lib.FilterType;

@Mixin(value = ItemPipeType.class, remap = false)
public class ItemPipeTypeMixin {

    @Inject(
        method = "matches",
        at = @At("HEAD"),
        cancellable = true
    )
    private void pipez$evalCreateFilter(
        HolderLookup.Provider provider,
        Filter<?, Item> filter,
        ItemStack stack,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (filter.getTag() instanceof SingleElementTag<Item> singleTag) {
            Item configuredItem = singleTag.getElement();

            if (CFAFilterSelector.isSupportedFilterStack(configuredItem)) {
                ItemStack filterStack = new ItemStack(configuredItem);

                if (filter.getMetadata() != null && !filter.getMetadata().isEmpty()) {
                    DataComponentPatch patch = ComponentUtils.getPatch(provider, filter.getMetadata());
                    filterStack.applyComponents(patch);
                }

                boolean matches = CFAFilterSelector.doFilterTest(stack, filterStack);

                cir.setReturnValue(matches);
            }
        }
    }
}