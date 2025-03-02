package xyz.iwolfking.createfiltersanywhere.mixin.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.simibubi.create.content.logistics.filter.FilterItem;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.data.CFAComponents;

import java.util.List;

@Restriction(
        require = {
                @Condition("create")
        }
)
@Mixin(FilterItem.class)
public abstract class MixinClientFilterItem {

    @WrapOperation(method = "makeSummary", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private boolean recursiveSummary(ItemStack instance, Operation<Boolean> original, @Local List<Component> list, @Local(ordinal = 0) LocalIntRef count) {
        if (original.call(instance)) {
            return true;
        }

        if (!Screen.hasControlDown()) {
            return false;
        }

        count.set(1); // expand infinitely instead of limiting to 3 lines

        if (instance.getItem() instanceof FilterItem fi) {
            MutableComponent firstComp = Component.literal("- ").append(instance.getHoverName()).append(" ").withStyle(ChatFormatting.GRAY);
            List<Component> innerSummary = ((FilterItemInvoker) fi).invokeMakeSummary(instance);
            boolean isFst = true;
            for (Component component : innerSummary) {
                list.add((isFst ? firstComp : Component.literal("   ")).append(component).withStyle(ChatFormatting.GRAY));
                isFst = false;
            }
            return true;

        }
        return false;
    }

    @Inject(method = "makeSummary", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/logistics/item/filter/attribute/ItemAttribute$ItemAttributeEntry;attribute()Lcom/simibubi/create/content/logistics/item/filter/attribute/ItemAttribute;"), remap = false)
    private void unlimitedAttributeFilterTooltip(ItemStack filter, CallbackInfoReturnable<List<Component>> cir, @Local LocalIntRef count){
        if (Screen.hasControlDown()) {
            count.set(1); // expand infinitely instead of limiting to 3 lines
        }
    }

    @Inject(method = "appendHoverText", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/logistics/filter/FilterItem;makeSummary(Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", remap = false))
    private void addCtrlHint(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        ChatFormatting color = Screen.hasControlDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY;
        tooltip.add(Component.literal("Hold [").append(Component.translatable("create.tooltip.keyCtrl").withStyle(color)).append("] to show nested filters.").withStyle(ChatFormatting.DARK_GRAY));
    }

    @ModifyArg(method = "makeSummary", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), remap = false)
    @SuppressWarnings("unchecked") // erased generics
    private <E> E showAndAnyInListFilterTooltip(E e, @Local LocalBooleanRef blacklist,
                                                @Local(argsOnly = true) ItemStack filter) {

        boolean matchAll = filter.getOrDefault(CFAComponents.FILTER_ITEMS_MATCH_ALL, false);
          MutableComponent filterTypeComp = (MutableComponent) e;
        if (matchAll) {
            filterTypeComp.append(Component.literal(" (All)").withStyle(ChatFormatting.GOLD));
        } else {
            filterTypeComp.append(Component.literal(" (Any)").withStyle(ChatFormatting.GOLD));
        }
        return (E) filterTypeComp;
    }

}
