package xyz.iwolfking.createfiltersanywhere.api.attributes.impl.sophisticatedbackpacks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.api.CreateAttributeRegistry;
import xyz.iwolfking.createfiltersanywhere.api.attributes.impl.sophisticatedbackpacks.util.SophisticatedBackpackUtil;

import java.util.*;
public record BackpackHasUUIDAttribute(String UUID) implements ItemAttribute {

    public static final MapCodec<BackpackHasUUIDAttribute> CODEC = Codec.STRING
            .xmap(BackpackHasUUIDAttribute::new, BackpackHasUUIDAttribute::UUID)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, BackpackHasUUIDAttribute> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
            .map(BackpackHasUUIDAttribute::new, BackpackHasUUIDAttribute::UUID);

    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        String uuid = SophisticatedBackpackUtil.getBackpackUUID(itemStack);
        return uuid != null && uuid.equals(this.UUID);
    }

    @Override
    public ItemAttributeType getType() {
        return SophisticatedBackpackAttributes.HAS_BACKPACK_UUID;
    }

    @Override
    public String getTranslationKey() {
        return "has_backpack_uuid";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[]{this.UUID};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new BackpackHasUUIDAttribute("dummy");
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            String name = SophisticatedBackpackUtil.getBackpackUUID(stack);
            if (name != null && !name.isEmpty()) {
                list.add(new BackpackHasUUIDAttribute(name));
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
