package xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import dev.shadowsoffire.apotheosis.socket.gem.bonus.DurabilityBonus;
import dev.shadowsoffire.apotheosis.socket.gem.bonus.EnchantmentBonus;
import dev.shadowsoffire.apotheosis.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.apotheosis.socket.gem.bonus.special.AllStatsBonus;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.api.util.StringUtils;
import xyz.iwolfking.createfiltersanywhere.api.util.apotheosis.ApotheosisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record GemBonusTypeAttribute(String bonus) implements ItemAttribute {

    public static final MapCodec<GemBonusTypeAttribute> CODEC = Codec.STRING
            .xmap(GemBonusTypeAttribute::new, GemBonusTypeAttribute::bonus)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, GemBonusTypeAttribute> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
            .map(GemBonusTypeAttribute::new, GemBonusTypeAttribute::bonus);


    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        Set<String> gemBonuses = ApotheosisUtil.getGemBonusesTypeNames(itemStack);
        return !gemBonuses.isEmpty() && gemBonuses.contains(bonus);
    }

    @Override
    public ItemAttributeType getType() {
        return ApotheosisAttributes.APOTH_GEM_BONUS_TYPE;
    }

    @Override
    public String getTranslationKey() {
        return "apoth_gem_bonus_type";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Object[]{StringUtils.toTitleCase(this.bonus)};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new GemBonusTypeAttribute("Attribute");
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            Set<String> gemBonuses = ApotheosisUtil.getGemBonusesTypeNames(stack);
            if (!gemBonuses.isEmpty()) {
                for(String gemBonus : gemBonuses) {
                    list.add(new GemBonusTypeAttribute(gemBonus));
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