package xyz.iwolfking.createfiltersanywhere.api.integration.ftbquests;

import dev.ftb.mods.ftbquests.api.ItemFilterAdapter;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;
import xyz.iwolfking.createfiltersanywhere.api.lib.FilterType;

public class CreateItemFilterAdapter implements ItemFilterAdapter {

    @Override
    public String getName() {
        return "Create Filters Anywhere";
    }

    @Override
    public boolean isFilterStack(ItemStack stack) {
        return !stack.isEmpty() && CFAFilterSelector.isSupportedFilterStack(stack);
    }

    @Override
    public boolean doesItemMatch(ItemStack filterStack, ItemStack toCheck, HolderLookup.Provider provider) {
        if (!isFilterStack(filterStack) || toCheck.isEmpty()) {
            return false;
        }

        return CFAFilterSelector.doFilterTest(toCheck, filterStack);
    }

    @Override
    public Matcher getMatcher(ItemStack itemStack, HolderLookup.Provider provider) {
        if (!isFilterStack(itemStack)) {
            return NO_MATCH;
        }

        return stackToTest -> {
            if (stackToTest.isEmpty()) {
                return false;
            }

            return CFAFilterSelector.doFilterTest(stackToTest, itemStack);
        };
    }

    @Override
    public boolean hasItemTagFilter() {
        return false;
    }

    @Override
    public ItemStack makeTagFilterStack(TagKey<Item> tag) {
        return ItemStack.EMPTY;
    }
}