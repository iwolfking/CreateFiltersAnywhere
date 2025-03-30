package xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.api.util.StringUtils;
import xyz.iwolfking.createfiltersanywhere.api.util.apotheosis.ApotheosisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record GemUniqueAttribute(Purity purity) implements ItemAttribute {

    public static final MapCodec<GemUniqueAttribute> CODEC = Purity.CODEC
            .xmap(GemUniqueAttribute::new, GemUniqueAttribute::purity)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, GemUniqueAttribute> STREAM_CODEC = Purity.STREAM_CODEC
            .map(GemUniqueAttribute::new, GemUniqueAttribute::purity);

    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        Optional<Purity> gemPurity = ApotheosisUtil.getPurityOf(itemStack);
        return gemPurity.isPresent() && gemPurity.get().equals(purity);
    }

    @Override
    public ItemAttributeType getType() {
        return ApotheosisAttributes.APOTH_GEM_PURITY;
    }

    @Override
    public String getTranslationKey() {
        return "apoth_gem_purity";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[]{StringUtils.toTitleCase(this.purity.getName())};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new GemUniqueAttribute(Purity.NORMAL);
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            Optional<Purity> purity = ApotheosisUtil.getPurityOf(stack);
            if (purity != null && purity.isPresent()) {
                list.add(new GemUniqueAttribute(purity.get()));
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