package xyz.iwolfking.createfiltersanywhere.api.core;

import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.api.integration.IntegrationHandler;
import xyz.iwolfking.createfiltersanywhere.api.lib.FilterType;

public class CFAFilterSelector {
    public static boolean isSupportedFilterStack(ItemStack filterStack) {
        if(filterStack.isEmpty()) {
            return false;
        }

        return getFilterType(filterStack) != FilterType.INVALID;
    }

    public static boolean doFilterTest(ItemStack stack, ItemStack filterStack) {
        FilterType filter = getFilterType(filterStack);
        return filter.filterFunction.get().apply(stack, filterStack);
    }

    public static FilterType getFilterType(ItemStack filterStack) {
        for(FilterType type : FilterType.values()) {
            if(type.equals(FilterType.INVALID)) {
                continue;
            }

            if(IntegrationHandler.isModLoaded(type.modId)) {
                if(type.filterValidationFunction.get().apply(filterStack)) {
                    return type;
                }
            }
        }

        return FilterType.INVALID;
    }
}
