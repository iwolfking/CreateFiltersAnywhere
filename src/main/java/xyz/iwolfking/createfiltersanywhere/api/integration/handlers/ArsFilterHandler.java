package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;



import com.hollingsworth.arsnouveau.common.items.ItemScroll;
import com.hollingsworth.arsnouveau.common.items.itemscrolls.AllowItemScroll;
import com.hollingsworth.arsnouveau.common.items.itemscrolls.DenyItemScroll;
import net.minecraft.world.item.ItemStack;

public class ArsFilterHandler {

    public static boolean isSupportedFilterItem(ItemStack filterStack) {
        return filterStack.getItem() instanceof AllowItemScroll || filterStack.getItem() instanceof DenyItemScroll;
    }

    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        if(filterStack.getItem() instanceof ItemScroll filterItem) {
            return filterItem.getSortPref(stack, filterStack, null) != ItemScroll.SortPref.INVALID;
        }

        return false;
    }
}
