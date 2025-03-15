package xyz.iwolfking.createfiltersanywhere.api.attributes.abstracts;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import com.simibubi.create.content.logistics.item.filter.attribute.SingletonItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.attributes.AddedByAttribute;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.api.CreateAttributeRegistry;

import java.util.Collections;
import java.util.List;

public abstract class CFAAttribute<V> implements ItemAttribute {

    protected final V value;

    protected CFAAttribute(V value) {
        this.value = value;
    }

    public abstract ItemAttribute withValue(V value);

    public abstract V getValue(ItemStack stack);

    public boolean appliesTo(ItemStack itemStack, Level level) {
        return this.value == getValue(itemStack);
    }

    public void register() {
        CreateAttributeRegistry.register(this.getTranslationKey(), getType());
    }

    @Override
    public ItemAttributeType getType() {
        return new SingletonItemAttribute.Type(type -> new SingletonItemAttribute(type, this::appliesTo, this.getTranslationKey()));
    }

    public abstract String getTranslationKey();
}
