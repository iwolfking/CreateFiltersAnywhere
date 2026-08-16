package xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import dev.shadowsoffire.apotheosis.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.loot.LootRarity;
import dev.shadowsoffire.apotheosis.loot.RarityRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record HasRarityAttribute(Boolean hasRarity) implements ItemAttribute {

    public static final MapCodec<HasRarityAttribute> CODEC = Codec.BOOL
            .xmap(HasRarityAttribute::new, HasRarityAttribute::hasRarity)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, HasRarityAttribute> STREAM_CODEC = ByteBufCodecs.BOOL
            .map(HasRarityAttribute::new, HasRarityAttribute::hasRarity);


    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        DynamicHolder<LootRarity> rarity = AffixHelper.getRarity(itemStack);
        if (!rarity.isBound()) return false;
        ResourceLocation itemRarity = RarityRegistry.INSTANCE.getKey(rarity.get());
        return itemRarity != null;
    }

    @Override
    public ItemAttributeType getType() {
        return ApotheosisAttributes.APOTH_LOOT_RARITY;
    }

    @Override
    public String getTranslationKey() {
        return "apoth_has_rarity";
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new HasRarityAttribute(true);
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            DynamicHolder<LootRarity> itemRarity = AffixHelper.getRarity(stack);
            if (itemRarity.isBound()) {
                list.add(new HasRarityAttribute(true));
            }
            else {
                list.add(new HasRarityAttribute(false));
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