package xyz.iwolfking.createfiltersanywhere.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.UnaryOperator;

public class CFAComponents {

    private static final DeferredRegister.DataComponents DATA_COMPONENTS;
    public static final DataComponentType<Boolean> FILTER_ITEMS_MATCH_ALL;

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = ((DataComponentType.Builder)builder.apply(DataComponentType.builder())).build();
        DATA_COMPONENTS.register(name, () -> type);
        return type;
    }

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }

    static {
        DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "createfiltersanywhere");
        FILTER_ITEMS_MATCH_ALL = register("filter_items_match_all", (builder) -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    }
}
