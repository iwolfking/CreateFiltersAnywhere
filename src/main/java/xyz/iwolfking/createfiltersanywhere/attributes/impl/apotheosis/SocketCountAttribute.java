package xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import dev.shadowsoffire.apotheosis.socket.SocketHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record SocketCountAttribute(int count) implements ItemAttribute {

    public static final MapCodec<SocketCountAttribute> CODEC = Codec.INT
            .xmap(SocketCountAttribute::new, SocketCountAttribute::count)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, SocketCountAttribute> STREAM_CODEC = ByteBufCodecs.INT
            .map(SocketCountAttribute::new, SocketCountAttribute::count);


    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        return SocketHelper.getSockets(itemStack) >= count;
    }

    @Override
    public ItemAttributeType getType() {
        return ApotheosisAttributes.APOTH_SOCKET_COUNT;
    }

    @Override
    public String getTranslationKey() {
        return "apoth_socket_count";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[]{this.count};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new SocketCountAttribute(2);
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            var sockets = SocketHelper.getSockets(stack);
            if (sockets > 0)
                list.add(new SocketCountAttribute(sockets));

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