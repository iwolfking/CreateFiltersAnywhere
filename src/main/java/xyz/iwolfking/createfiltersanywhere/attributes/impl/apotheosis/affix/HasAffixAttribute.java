package xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import dev.shadowsoffire.apotheosis.affix.AffixHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis.ApotheosisAttributes;

import java.util.ArrayList;
import java.util.List;

public record HasAffixAttribute(String affixType) implements ItemAttribute {

    public static final MapCodec<HasAffixAttribute> CODEC = Codec.STRING
            .xmap(HasAffixAttribute::new, HasAffixAttribute::affixType)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, HasAffixAttribute> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
            .map(HasAffixAttribute::new, HasAffixAttribute::affixType);


    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        var affixes = AffixHelper.getAffixes(itemStack);
        for (var entry : affixes.entrySet()) {
            if (entry.getKey().get().id().toString().equals(this.affixType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemAttributeType getType() {
        return ApotheosisAttributes.APOTH_HAS_AFFIX;
    }

    @Override
    public String getTranslationKey() {
        return "apoth_has_affix";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[]{I18n.get("affix." + this.affixType)};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new HasAffixAttribute("apotheosis:armor/attribute/fortunate");
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            var affixes = AffixHelper.getAffixes(stack);
            for (var entry : affixes.entrySet()) {
                list.add(new HasAffixAttribute(entry.getKey().get().id().toString()));
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