package xyz.iwolfking.createfiltersanywhere.attributes.impl.general;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record IsItemAttribute(ResourceLocation itemId) implements ItemAttribute {

    public static final MapCodec<IsItemAttribute> CODEC = ResourceLocation.CODEC
            .xmap(IsItemAttribute::new, IsItemAttribute::itemId)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, IsItemAttribute> STREAM_CODEC = ResourceLocation.STREAM_CODEC
            .map(IsItemAttribute::new, IsItemAttribute::itemId);

    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        if (itemStack.isEmpty()) {
            return false;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        return id != null && id.equals(this.itemId);
    }

    @Override
    public ItemAttributeType getType() {
        return ModAttributes.IS_ITEM; // Register your ItemAttributeType here
    }

    @Override
    public String getTranslationKey() {
        return "is_item";
    }

    @Override
    public Object[] getTranslationParameters() {
        Item item = BuiltInRegistries.ITEM.get(this.itemId);
        if (item != null) {
            return new Object[]{item.getDescription().getString()};
        }
        return new Object[]{this.itemId.toString()};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new IsItemAttribute(ResourceLocation.withDefaultNamespace("air"));
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            if (stack.isEmpty()) {
                return list;
            }

            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id != null) {
                list.add(new IsItemAttribute(id));
            }

            return list;
        }

        @Override
        public MapCodec<? extends ItemAttribute> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ? extends ItemAttribute> streamCodec() {
            return STREAM_CODEC;
        }
    }
}