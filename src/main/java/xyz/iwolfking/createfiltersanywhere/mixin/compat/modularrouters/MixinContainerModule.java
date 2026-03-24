package xyz.iwolfking.createfiltersanywhere.mixin.compat.modularrouters;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.desht.modularrouters.container.ModuleMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;

@Mixin(value = ModuleMenu.class, remap = false)
public class MixinContainerModule {
    @WrapOperation(method = "isItemOKForFilter", at = @At(value = "CONSTANT", args = "classValue=me/desht/modularrouters/item/smartfilter/SmartFilterItem"))
    private boolean injected(Object object, Operation<Boolean> original, ItemStack stack) {
        return CFAFilterSelector.isSupportedFilterStack(stack);
    }
}
