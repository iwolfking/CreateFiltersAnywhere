package xyz.iwolfking.createfiltersanywhere.mixin.compat.modularrouters;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.logistics.filter.FilterItem;
import me.desht.modularrouters.container.ModuleMenu;
import me.desht.modularrouters.item.smartfilter.SmartFilterItem;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
        require = {
                @Condition("modularrouters")
        }
)
@Mixin(value = ModuleMenu.class, remap = false)
public class MixinContainerModule {
    @WrapOperation(method = "isItemOKForFilter", at = @At(value = "CONSTANT", args = "classValue=me/desht/modularrouters/item/smartfilter/SmartFilterItem"))
    private boolean injected(Object object, Operation<Boolean> original, ItemStack stack) {
        return stack.getItem() instanceof SmartFilterItem || stack.getItem() instanceof FilterItem;
    }
}
