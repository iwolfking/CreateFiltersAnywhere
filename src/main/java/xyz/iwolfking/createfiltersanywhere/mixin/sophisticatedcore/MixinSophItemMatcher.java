package xyz.iwolfking.createfiltersanywhere.mixin.sophisticatedcore;

import com.simibubi.create.content.logistics.filter.FilterItem;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.upgrades.FilterLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.Config;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;

@Restriction(
        require = {
                @Condition("sophisticatedcore")
        }
)
@Mixin(FilterLogic.class)
public class MixinSophItemMatcher {
    @Inject(method = "stackMatchesFilter", at = @At("HEAD"), cancellable = true)
    public void sophFilterMatcher(ItemStack stack, ItemStack filter, CallbackInfoReturnable<Boolean> cir) {
        if (Config.BACKPACKS_COMPAT.get() && filter.getItem() instanceof FilterItem) {
            cir.setReturnValue(CFATests.checkFilter(stack, filter,true,null));
        }
    }
}
