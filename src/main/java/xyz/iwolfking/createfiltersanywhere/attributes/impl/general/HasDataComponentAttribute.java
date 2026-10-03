package xyz.iwolfking.createfiltersanywhere.attributes.impl.general;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.api.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public record HasDataComponentAttribute(ResourceLocation componentId) implements ItemAttribute {

    public static final MapCodec<HasDataComponentAttribute> CODEC = ResourceLocation.CODEC
            .xmap(HasDataComponentAttribute::new, HasDataComponentAttribute::componentId)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, HasDataComponentAttribute> STREAM_CODEC = ResourceLocation.STREAM_CODEC
            .map(HasDataComponentAttribute::new, HasDataComponentAttribute::componentId);

    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        if (itemStack.isEmpty()) {
            return false;
        }
        DataComponentType<?> type = BuiltInRegistries.DATA_COMPONENT_TYPE.get(this.componentId);
        return type != null && itemStack.has(type);
    }

    @Override
    public ItemAttributeType getType() {
        return ModAttributes.HAS_DATA_COMPONENT;
    }

    @Override
    public String getTranslationKey() {
        return "has_data_component";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[]{StringUtils.toTitleCase(this.componentId.getPath())};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new HasDataComponentAttribute(ResourceLocation.withDefaultNamespace("custom_data"));
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            if (stack.isEmpty()) {
                return list;
            }

            for (TypedDataComponent<?> component : stack.getComponents()) {
                ResourceLocation id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(component.type());
                if (id != null) {
                    list.add(new HasDataComponentAttribute(id));
                }
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