package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;



import com.hollingsworth.arsnouveau.common.items.ItemScroll;
import com.hollingsworth.arsnouveau.common.items.itemscrolls.AllowItemScroll;
import com.hollingsworth.arsnouveau.common.items.itemscrolls.DenyItemScroll;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ArsFilterHandler {

    public static boolean isSupportedFilterItem(Item filterItem) {
        return filterItem instanceof AllowItemScroll || filterItem instanceof DenyItemScroll;
    }

    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        if(filterStack.getItem() instanceof ItemScroll filterItem) {
            return filterItem.getSortPref(stack, filterStack, null) != ItemScroll.SortPref.INVALID;
        }

        return false;
    }
}
