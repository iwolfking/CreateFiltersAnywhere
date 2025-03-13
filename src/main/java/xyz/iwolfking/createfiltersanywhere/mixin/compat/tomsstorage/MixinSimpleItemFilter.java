package xyz.iwolfking.createfiltersanywhere.mixin.compat.tomsstorage;

import com.simibubi.create.content.logistics.filter.FilterItem;
import com.tom.storagemod.inventory.StoredItemStack;
import com.tom.storagemod.inventory.filter.SimpleItemFilter;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;

@Restriction(
        require = {
                @Condition("toms_storage")
        }
)
@Mixin(value = SimpleItemFilter.class, remap = false)
public class MixinSimpleItemFilter {
    @Redirect(method = "test0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean checkCreateFilterNormal(ItemStack stack, ItemStack other) {
        if(other.getItem() instanceof FilterItem) {
            return CFATests.checkFilter(stack, other, true, null);
        }

        return ItemStack.isSameItem(stack, other);
    }
}
