package xyz.iwolfking.createfiltersanywhere.api.lib;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;
import xyz.iwolfking.createfiltersanywhere.api.integration.handlers.CreateFilterHandler;
import xyz.iwolfking.createfiltersanywhere.api.integration.handlers.FFSFilterHandler;
import xyz.iwolfking.createfiltersanywhere.api.integration.handlers.FilterHandler;
import xyz.iwolfking.createfiltersanywhere.api.integration.handlers.MRFilterHandler;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum FilterType {
    CREATE("create", CreateFilterHandler::checkFilter, CreateFilterHandler::isSupportedFilterItem),
    FTB("ftbfiltersystem", FFSFilterHandler::checkFilter, FFSFilterHandler::isSupportedFilterItem),
    MODULAR_ROUTERS("modularrouters", MRFilterHandler::checkFilter, MRFilterHandler::isSupportedFilterItem),
    INVALID("", FilterHandler::checkFilter, (i) -> false);

    public final BiFunction<ItemStack, ItemStack, Boolean> filterFunction;
    public final String modId;
    public final Function<ItemStack, Boolean> filterValidationFunction;

    FilterType(String modId, BiFunction<ItemStack, ItemStack, Boolean> filterFunction, Function<ItemStack, Boolean> filterValidationMethod) {
        this.modId = modId;
        this.filterFunction = filterFunction;
        this.filterValidationFunction = filterValidationMethod;
    }
}
