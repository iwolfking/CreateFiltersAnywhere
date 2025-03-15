package xyz.iwolfking.createfiltersanywhere.api.attributes.abstracts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import com.simibubi.create.content.logistics.item.filter.attribute.SingletonItemAttribute;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.api.CreateAttributeRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class BooleanAttribute extends CFAAttribute<Boolean>{

    private static final Map<Class<?>, Function<Boolean, BooleanAttribute>> factories = new HashMap<>();

    protected BooleanAttribute(Boolean value) {
        super(value);
    }

    public void register(Function<Boolean, BooleanAttribute> factory) {
        factories.put(getClass(), factory);
        super.register();
    }

    @Override
    public BooleanAttribute withValue(Boolean value) {
        return factories.getOrDefault(getClass(), ignored -> null).apply(value);
    }
}
