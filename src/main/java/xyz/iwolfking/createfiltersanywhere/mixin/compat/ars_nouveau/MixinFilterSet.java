package xyz.iwolfking.createfiltersanywhere.mixin.compat.ars_nouveau;

import com.hollingsworth.arsnouveau.api.item.inv.FilterSet;
import com.hollingsworth.arsnouveau.common.items.ItemScroll;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;
import xyz.iwolfking.createfiltersanywhere.api.lib.FilterType;

import java.util.List;
import java.util.function.Function;

@Mixin(value = FilterSet.class, remap = false)
public abstract class MixinFilterSet {

    @WrapOperation(
        method = "forPosition",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
            ordinal = 1
        )
    )
    private static boolean wrapDefaultFilterAdd(
            List<Function<ItemStack, ItemScroll.SortPref>> filters,
            Object element,
            Operation<Boolean> original,
            @Local(argsOnly = true) Level level,
            @Local(name = "stackInFrame") ItemStack stackInFrame
    ) {
        if(CFAFilterSelector.isSupportedFilterStack(stackInFrame)) {
            return filters.add(stackToStore -> cfa$evaluateCreateFilter(stackInFrame, stackToStore, level));
        }

        return original.call(filters, element);
    }

    @Unique
    private static ItemScroll.SortPref cfa$evaluateCreateFilter(ItemStack filterStack, ItemStack stackToStore, Level level) {
        if (filterStack.isEmpty() || stackToStore.isEmpty()) {
            return ItemScroll.SortPref.INVALID;
        }

        FilterType filterType = CFAFilterSelector.getFilterType(filterStack);
        if(filterType.equals(FilterType.CREATE)) {
            FilterItemStack wrapper = FilterItemStack.of(filterStack);

            if (wrapper instanceof FilterItemStack.ListFilterItemStack listFilter && listFilter.isBlacklist) {
                boolean matches = wrapper.test(level, stackToStore);
                return matches ? ItemScroll.SortPref.INVALID : ItemScroll.SortPref.LOW;
            }

            boolean matches = wrapper.test(level, stackToStore);
            return matches ? ItemScroll.SortPref.HIGHEST : ItemScroll.SortPref.INVALID;
        }
        else {
            boolean result = filterType.checkFilter(filterStack, stackToStore);

            return result ? ItemScroll.SortPref.HIGHEST : ItemScroll.SortPref.INVALID;
        }

    }
}