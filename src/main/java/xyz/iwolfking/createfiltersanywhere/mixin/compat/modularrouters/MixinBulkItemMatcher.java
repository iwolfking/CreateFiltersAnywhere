package xyz.iwolfking.createfiltersanywhere.mixin.compat.modularrouters;

import me.desht.modularrouters.api.matching.IModuleFlags;
import me.desht.modularrouters.logic.filter.matchers.BulkItemMatcher;
import me.desht.modularrouters.util.SetofItemStack;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;

@Mixin(value = BulkItemMatcher.class, remap = false)
public class MixinBulkItemMatcher {
    @Shadow
    @Final
    private SetofItemStack stacks;

    @Inject(method = "matchItem", at = @At("HEAD"), cancellable = true)
    public void createItemMatcher(ItemStack stack, IModuleFlags flags, CallbackInfoReturnable<Boolean> cir) {
        for (ItemStack filter : this.stacks) {
            if (CFATests.checkFilter(stack, filter,true,null)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
