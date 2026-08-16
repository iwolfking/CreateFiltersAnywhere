package xyz.iwolfking.createfiltersanywhere.api.lib;

import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.api.integration.handlers.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public enum FilterType {
    CREATE("create", () -> CreateFilterHandler::checkFilter, () -> CreateFilterHandler::isSupportedFilterItem),
    FTB("ftbfiltersystem", () -> FFSFilterHandler::checkFilter, () -> FFSFilterHandler::isSupportedFilterItem),
    MODULAR_ROUTERS("modularrouters", () -> MRFilterHandler::checkFilter, () -> MRFilterHandler::isSupportedFilterItem),
    TOMS_STORAGE("toms_storage", () -> TomsFilterHandler::checkFilter, () -> TomsFilterHandler::isSupportedFilterItem),
    INVALID("", () -> FilterHandler::checkFilter, () -> FilterHandler::isSupportedFilterItem);

    public final Supplier<BiFunction<ItemStack, ItemStack, Boolean>> filterFunction;
    public final String modId;
    public final Supplier<Function<ItemStack, Boolean>> filterValidationFunction;

    FilterType(String modId, Supplier<BiFunction<ItemStack, ItemStack, Boolean>> filterFunction, Supplier<Function<ItemStack, Boolean>> filterValidationMethod) {
        this.modId = modId;
        this.filterFunction = filterFunction;
        this.filterValidationFunction = filterValidationMethod;
    }
}
