package xyz.iwolfking.createfiltersanywhere.mixin.create;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.logistics.filter.FilterMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.iwolfking.createfiltersanywhere.api.accessors.FilterMenuAdvancedAccessor;
import xyz.iwolfking.createfiltersanywhere.data.CFAComponents;


@Mixin(value = FilterMenu.class, remap = false)
public class MixinFilterMenu implements FilterMenuAdvancedAccessor {
    @Shadow
    boolean respectNBT;
    @Shadow
    boolean blacklist;

    @Unique
    boolean vf$matchAll;

    @Inject(method = "initAndReadInventory(Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    public void initMatchALl(ItemStack filterItem, CallbackInfo ci) {
        vf$matchAll = filterItem.getOrDefault(CFAComponents.FILTER_ITEMS_MATCH_ALL, false);
    }

    @Inject(method = "saveData(Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1, shift = At.Shift.AFTER), cancellable = true, remap = true)
    private void injectSaveData(ItemStack filterItem, CallbackInfo ci) {
        filterItem.set(CFAComponents.FILTER_ITEMS_MATCH_ALL, vf$matchAll);
        if (blacklist || respectNBT || vf$matchAll) {
            ci.cancel();
        }
    }

    @Override
    public boolean vault_filters$getMatchAll() {
        return vf$matchAll;
    }

    @Override
    public void vault_filters$setMatchAll(boolean matchAll) {
        this.vf$matchAll = matchAll;
    }
}
