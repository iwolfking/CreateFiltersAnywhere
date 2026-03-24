package xyz.iwolfking.createfiltersanywhere.mixin.compat.tomsstorage;

import com.tom.storagemod.inventory.filter.SimpleItemFilter;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.iwolfking.createfiltersanywhere.Config;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;


@Mixin(value = SimpleItemFilter.class, remap = false)
public class MixinSimpleItemFilter {
    @Redirect(method = "test0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean checkCreateFilterNormal(ItemStack stack, ItemStack other) {
        if (Config.TOMS_COMPAT.get() && CFAFilterSelector.isSupportedFilterStack(other)) {
            return CFAFilterSelector.doFilterTest(stack, other);
        }

        return ItemStack.isSameItem(stack, other);
    }
}
