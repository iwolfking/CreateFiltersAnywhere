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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.api.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public record LootRarityAttribute(String rarity) implements ItemAttribute {

    public static final MapCodec<LootRarityAttribute> CODEC = Codec.STRING
            .xmap(LootRarityAttribute::new, LootRarityAttribute::rarity)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, LootRarityAttribute> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
            .map(LootRarityAttribute::new, LootRarityAttribute::rarity);


    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        DynamicHolder<LootRarity> rarity = AffixHelper.getRarity(itemStack);
        if (!rarity.isBound()) return false;
        ResourceLocation itemRarity = RarityRegistry.INSTANCE.getKey(rarity.get());
        if (itemRarity == null) return false;
        return itemRarity.toString().equals(this.rarity);
    }

    @Override
    public ItemAttributeType getType() {
        return ApotheosisAttributes.APOTH_LOOT_RARITY;
    }

    @Override
    public String getTranslationKey() {
        return "apoth_loot_rarity";
    }

    @Override
    public Object[] getTranslationParameters() {
        try { // try to get pretty colored name from registry
            LootRarity lootRarity = RarityRegistry.INSTANCE.getValue(ResourceLocation.parse(this.rarity));
            return new Object[]{lootRarity.toComponent()};
        } catch (Exception ignored) {}

        // fallback to resource location
        return new Object[]{this.rarity};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new LootRarityAttribute("apotheosis:epic");
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            DynamicHolder<LootRarity> itemRarity = AffixHelper.getRarity(stack);
            if (itemRarity.isBound()) {
                ResourceLocation itemRarityRl = RarityRegistry.INSTANCE.getKey(AffixHelper.getRarity(stack).get());
                if (itemRarityRl != null)
                    list.add(new LootRarityAttribute(itemRarityRl.toString()));
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