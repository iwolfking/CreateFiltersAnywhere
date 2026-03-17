package xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import dev.shadowsoffire.apotheosis.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.api.util.apotheosis.ApotheosisUtil;

import java.util.ArrayList;
import java.util.List;

public record GemIdAttribute(String gemId) implements ItemAttribute {

    public static final MapCodec<GemIdAttribute> CODEC = Codec.STRING
        .xmap(GemIdAttribute::new, GemIdAttribute::gemId)
        .fieldOf("value");

    public static final StreamCodec<ByteBuf, GemIdAttribute> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
        .map(GemIdAttribute::new, GemIdAttribute::gemId);


    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        String gemId = ApotheosisUtil.getGemId(itemStack);
        return gemId != null && gemId.equals(this.gemId);
    }

    @Override
    public ItemAttributeType getType() {
        return ApotheosisAttributes.APOTH_GEM_ID;
    }

    @Override
    public String getTranslationKey() {
        return "apoth_gem_id";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[]{Component.translatable("item.apotheosis.gem." + gemId)};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new GemIdAttribute("apotheosis:core/guardian");
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            String gemId = ApotheosisUtil.getGemId(stack);
            if (gemId != null) {
                list.add(new GemIdAttribute(gemId));
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