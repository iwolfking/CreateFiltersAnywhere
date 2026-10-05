package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;

import de.maxhenkel.pipez.Filter;
import de.maxhenkel.pipez.blocks.tileentity.UpgradeTileEntity;
import de.maxhenkel.pipez.corelib.tag.SingleElementTag;
import de.maxhenkel.pipez.datacomponents.ItemData;
import de.maxhenkel.pipez.items.ModItems;
import de.maxhenkel.pipez.items.UpgradeItem;
import de.maxhenkel.pipez.utils.ComponentUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;

public class PipezFilterHandler {

    public static Boolean isSupportedFilterItem(Item item) {
        return item instanceof UpgradeItem;
    }

    public static Boolean checkFilter(ItemStack candidateStack, ItemStack filterStack) {
        if (!(filterStack.getItem() instanceof UpgradeItem)) {
            return false;
        }

        ItemData itemData = filterStack.get(ModItems.ITEM_DATA_COMPONENT);
        if (itemData == null) {
            return false;
        }

        boolean isBlacklist = itemData.getFilterMode() == UpgradeTileEntity.FilterMode.BLACKLIST;
        boolean matched = false;

        for (Filter<?, Item> filter : itemData.copyFilterList2()) {
            if (matchesPipezFilter(candidateStack, filter)) {
                matched = true;
                break;
            }
        }

        return isBlacklist ? !matched : matched;
    }

    private static boolean matchesPipezFilter(ItemStack candidateStack, Filter<?, Item> filter) {
        boolean result = false;

        if (filter.getTag() != null) {
            if (filter.getTag() instanceof SingleElementTag<Item> singleTag) {
                Item element = singleTag.getElement();
                ItemStack subFilterStack = new ItemStack(element);

                if (filter.getMetadata() != null && !filter.getMetadata().isEmpty()) {
                    HolderLookup.Provider registries = HolderLookup.Provider.create(java.util.stream.Stream.empty());

                    DataComponentPatch patch = ComponentUtils.getPatch(registries, filter.getMetadata());
                    subFilterStack.applyComponents(patch);
                }

                if (CFAFilterSelector.isSupportedFilterStack(subFilterStack)) {
                    result = CFAFilterSelector.doFilterTest(candidateStack, subFilterStack);
                } else {
                    result = filter.getTag().contains(candidateStack.getItem());
                }
            } else {
                result = filter.getTag().contains(candidateStack.getItem());
            }
        }

        return filter.isInvert() ? !result : result;
    }
}