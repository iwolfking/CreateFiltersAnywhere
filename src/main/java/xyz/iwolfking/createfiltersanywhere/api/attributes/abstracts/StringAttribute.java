package xyz.iwolfking.createfiltersanywhere.api.attributes.abstracts;

import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class StringAttribute extends CFAAttribute<String>{
    private static final Map<Class<?>, Function<String, StringAttribute>> factories = new HashMap<>();


    protected StringAttribute(String value) {
        super(value);
    }

    public void register(Function<String, StringAttribute> factory) {
        factories.put(getClass(), factory);
        super.register();
    }

    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        return this.getValue(itemStack) != null;
    }

    @Override
    public StringAttribute withValue(String value) {
        return factories.getOrDefault(getClass(), ignored -> null).apply(value);
    }
}
