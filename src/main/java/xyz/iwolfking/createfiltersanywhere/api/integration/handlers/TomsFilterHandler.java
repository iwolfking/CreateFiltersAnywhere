package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;



import com.tom.storagemod.inventory.StoredItemStack;
import com.tom.storagemod.item.FilterItem;
import com.tom.storagemod.item.TagFilterItem;
import net.minecraft.world.item.ItemStack;

public class TomsFilterHandler {

    public static boolean isSupportedFilterItem(ItemStack filterStack) {
        return filterStack.getItem() instanceof FilterItem || filterStack.getItem() instanceof TagFilterItem;
    }

    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        if(filterStack.getItem() instanceof FilterItem filterItem) {
            return filterItem.createFilter(null, filterStack).test(new StoredItemStack(stack));
        }
        else if (filterStack.getItem() instanceof TagFilterItem tagFilterItem) {
            return tagFilterItem.createFilter(null, filterStack).test(new StoredItemStack(stack));
        }

        return false;
    }
}
