package xyz.iwolfking.createfiltersanywhere.mixin.compat.ae2;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.util.prioritylist.PrecisePriorityList;
import com.simibubi.create.content.logistics.filter.FilterItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.Config;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;

@Mixin(value = PrecisePriorityList.class, remap = false)
public abstract class MixinPrecisePriorityList {
    @Shadow
    @Final
    private KeyCounter list;

    @Inject(method = "isListed", at = @At("HEAD"), cancellable = true)
    public void isListed(AEKey input, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.AE2_COMPAT.get()) {
            return;
        }

        for (AEKey key : this.getItems()) {
            if (!(key instanceof AEItemKey itemKey)) {
                continue;
            }

            if (itemKey.getItem() instanceof FilterItem && input instanceof AEItemKey inputItemKey) {
                boolean result = CFATests.checkFilter(inputItemKey, itemKey, true, null);
                if (result || (!(inputItemKey.getItem() instanceof FilterItem) && this.list.get(input) > 0L)) {
                    cir.setReturnValue(true);
                    break;
                }
            }
        }
    }

    @Shadow
    public abstract Iterable<AEKey> getItems();
}
