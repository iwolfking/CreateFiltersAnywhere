package xyz.iwolfking.createfiltersanywhere.compat.enderio;

import com.enderio.enderio.api.EnderIOCapabilities;
import com.enderio.enderio.api.filter.FilterMenuProvider;
import com.enderio.enderio.api.filter.ItemFilter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;

public class EnderIOCapabilitiesRegistry {
    public static final ICapabilityProvider<ItemStack, Void, ItemFilter> ITEM_FILTER_PROVIDER = (stack, v) -> (ItemFilter) (target, itemStack) -> {
        if (CFAFilterSelector.isSupportedFilterStack(stack)) {
            boolean matches = CFAFilterSelector.doFilterTest(itemStack, stack);
            return matches ? itemStack : ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    };

    public static final ICapabilityProvider<ItemStack, Void, FilterMenuProvider> FILTER_MENU_PROVIDER = (stack, v) -> (FilterMenuProvider) (serverPlayer, itemHandler, slot, runnable) -> {
    };

    public static void init(IEventBus eventBus) {
        eventBus.addListener(EnderIOCapabilitiesRegistry::registerCapEvent);
    }

    private static void registerCapEvent(RegisterCapabilitiesEvent event) {
        BuiltInRegistries.ITEM.forEach(item -> {
            if(!BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("enderio") && CFAFilterSelector.isSupportedFilterStack(item)) {
                event.registerItem(EnderIOCapabilities.ITEM_FILTER, ITEM_FILTER_PROVIDER, item);
                event.registerItem(EnderIOCapabilities.FILTER_MENU_PROVIDER, FILTER_MENU_PROVIDER, item);
            }
        });
    }
}