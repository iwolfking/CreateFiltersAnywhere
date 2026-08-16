package xyz.iwolfking.createfiltersanywhere.api.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.api.integration.IntegrationHandler;
import xyz.iwolfking.createfiltersanywhere.api.lib.FilterType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CFAFilterSelector {
    public static final Set<FilterType> LOADED_FILTER_TYPES = new HashSet<>();
    private static final ConcurrentHashMap<Item, FilterType> CACHED_ITEM_FILTER_TYPES = new ConcurrentHashMap<>();

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
        if(CACHED_ITEM_FILTER_TYPES.containsKey(filterStack.getItem())) {
            return CACHED_ITEM_FILTER_TYPES.get(filterStack.getItem());
        }

        for(FilterType type : LOADED_FILTER_TYPES) {
            if(type.equals(FilterType.INVALID)) {
                continue;
            }

            if(IntegrationHandler.isModLoaded(type.modId)) {
                if(type.filterValidationFunction.get().apply(filterStack)) {
                    CACHED_ITEM_FILTER_TYPES.put(filterStack.getItem(), type);
                    return type;
                }
            }
        }

        return FilterType.INVALID;
    }
}
