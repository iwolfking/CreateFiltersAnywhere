package xyz.iwolfking.createfiltersanywhere.api.attributes.abstracts;

import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class IntAttribute extends CFAAttribute<Integer>{
    private static final Map<Class<?>, Function<Integer, IntAttribute>> factories = new HashMap<>();

    protected IntAttribute(Integer value) {
        super(value);
    }

    public void register(Function<Integer, IntAttribute> factory) {
        factories.put(getClass(), factory);
        super.register();
    }

    @Override
    public IntAttribute withValue(Integer value) {
        return factories.getOrDefault(getClass(), ignored -> null).apply(value);
    }
}
