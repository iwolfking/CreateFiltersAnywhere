package xyz.iwolfking.createfiltersanywhere.api.core;

import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;
import xyz.iwolfking.createfiltersanywhere.api.integration.FilterHandler;
import xyz.iwolfking.createfiltersanywhere.api.integration.IntegrationHandler;
import xyz.iwolfking.createfiltersanywhere.api.integration.handlers.CreateFilterHandler;
import xyz.iwolfking.createfiltersanywhere.api.integration.handlers.FFSFilterHandler;
import xyz.iwolfking.createfiltersanywhere.api.lib.FilterType;

import java.util.Objects;
import java.util.function.BiFunction;

public class CFAFilterSelector {
    public static boolean isSupportedFilterStack(ItemStack filterStack) {
        return getFilterType(filterStack) != FilterType.INVALID;
    }

    public static boolean doFilterTest(ItemStack stack, ItemStack filterStack) {
        FilterType filter = getFilterType(filterStack);
        return filter.filterFunction.apply(stack, filterStack);
    }

    public static FilterType getFilterType(ItemStack filterStack) {
        for(FilterType type : FilterType.values()) {
            if(type.equals(FilterType.INVALID)) {
                continue;
            }

            if(IntegrationHandler.isModLoaded(type.modId)) {
                if(type.filterValidationFunction.apply(filterStack)) {
                    return type;
                }
            }
        }

        return FilterType.INVALID;
    }
}
