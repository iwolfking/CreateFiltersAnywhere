package xyz.iwolfking.createfiltersanywhere.mixin.create;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.logistics.filter.AbstractFilterScreen;
import com.simibubi.create.content.logistics.filter.FilterMenu;
import com.simibubi.create.content.logistics.filter.FilterScreen;
import com.simibubi.create.content.logistics.filter.FilterScreenPacket;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import com.simibubi.create.foundation.utility.CreateLang;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.api.accessors.FilterMenuAdvancedAccessor;

import java.util.Arrays;
import java.util.List;

@Restriction(
        require = {
                @Condition("create")
        }
)
@Mixin(value = FilterScreen.class, remap = false)
public abstract class MixinFilterScreen extends AbstractFilterScreen<FilterMenu> {
    @Shadow
    private IconButton blacklist;
    @Shadow private IconButton whitelist;
    @Shadow private IconButton respectNBT;
    @Shadow private IconButton ignoreNBT;
    @Shadow private Component denyDESC;
    @Shadow private Component allowDESC;
    @Shadow private Component respectDataDESC;
    @Shadow private Component ignoreDataDESC;

    @Shadow private Component allowN;
    @Shadow private Component denyN;
    @Shadow private Component respectDataN;
    @Shadow private Component ignoreDataN;

    @Unique
    private Component allowAnyN = CreateLang.translateDirect("gui.attribute_filter.allow_list_disjunctive");
    @Unique
    private Component allowAnyDESC = CreateLang.translateDirect("gui.attribute_filter.allow_list_disjunctive.description");
    @Unique
    private Component allowAllN = CreateLang.translateDirect("gui.attribute_filter.allow_list_conjunctive");
    @Unique
    private Component allowAllDESC = CreateLang.translateDirect("gui.attribute_filter.allow_list_conjunctive.description");
    @Unique
    private IconButton matchAny, matchAll;
    @Unique
    private Indicator matchAnyIndicator, matchAllIndicator;
    @Unique
    private FilterMenuAdvancedAccessor menuAccessor = (FilterMenuAdvancedAccessor) menu;


    // This constructor is required by Mixin
    protected MixinFilterScreen(FilterMenu menu, Inventory inv, Component title, AllGuiTextures background) {
        super(menu, inv, title, background);
    }

    @Inject(method = "init",at = @At(value = "INVOKE",
            target = "Lcom/simibubi/create/content/logistics/filter/FilterScreen;handleIndicators()V",shift = At.Shift.BEFORE, ordinal = 0, remap = false), remap = true)
    private void injectInitializer(CallbackInfo ci, @Local(ordinal = 0) int x, @Local(ordinal = 1) int y) {
//        if (!ModPresence.serverHasVaultFilters()) {
//            return;
//        }

        matchAll = new IconButton(x + 102, y + 75, AllIcons.I_BLACKLIST);

        matchAll.withCallback(() -> {
            menuAccessor.vault_filters$setMatchAll(true);
            sendOptionUpdate(FilterScreenPacket.Option.ADD_TAG);
        });
        matchAll.setToolTip(allowAllN);
        matchAllIndicator= new Indicator(x + 102, y + 69, Component.empty());

        matchAny= new IconButton(x + 120, y + 75, AllIcons.I_WHITELIST_OR);
        matchAny.withCallback(() -> {
            menuAccessor.vault_filters$setMatchAll(false);
            sendOptionUpdate(FilterScreenPacket.Option.ADD_INVERTED_TAG);
        });
        matchAny.setToolTip(allowAnyN);
        matchAnyIndicator = new Indicator(x + 120, y + 69, Component.empty());

        addRenderableWidgets(matchAll, matchAny, matchAllIndicator, matchAnyIndicator);
    }

    @Inject(method = "getTooltipButtons", at = @At("HEAD"), cancellable = true)
    private void addToolTipButtons(CallbackInfoReturnable<List<IconButton>> cir) {
        cir.setReturnValue(Arrays.asList(blacklist, whitelist, respectNBT, ignoreNBT, matchAll, matchAny));
//        if (ModPresence.serverHasVaultFilters()) {
//            cir.setReturnValue(Arrays.asList(blacklist, whitelist, respectNBT, ignoreNBT, matchAll, matchAny));
//        }
    }

    @Inject(method = "getTooltipDescriptions", at = @At("HEAD"), cancellable = true)
    private void addToolTipDescriptions(CallbackInfoReturnable<List<MutableComponent>> cir) {
        cir.setReturnValue(Arrays.asList(denyDESC.plainCopy(), allowDESC.plainCopy(), respectDataDESC.plainCopy(),
                ignoreDataDESC.plainCopy(), allowAllDESC.plainCopy(), allowAnyDESC.plainCopy()));
//        if (ModPresence.serverHasVaultFilters()) {
//            cir.setReturnValue(Arrays.asList(denyDESC.plainCopy(), allowDESC.plainCopy(), respectDataDESC.plainCopy(),
//                    ignoreDataDESC.plainCopy(), allowAllDESC.plainCopy(), allowAnyDESC.plainCopy()));
//
//        }
    }

    @Inject(method = "isButtonEnabled", at = @At("TAIL"), cancellable = true)
    private void addButtonsEnabled(IconButton button, CallbackInfoReturnable<Boolean> cir) {
        if (button == matchAll) {
            cir.setReturnValue(!menuAccessor.vault_filters$getMatchAll());
        } else if (button == matchAny) {
            cir.setReturnValue(menuAccessor.vault_filters$getMatchAll());
        }
    }
}
