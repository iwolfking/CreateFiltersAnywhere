package xyz.iwolfking.createfiltersanywhere.attributes.impl.sophisticatedbackpacks;

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
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.createfiltersanywhere.api.util.sophisticatedbackpacks.SophisticatedBackpackUtil;

import java.util.ArrayList;
import java.util.List;

public record BackpackHasUpgradeAttribute(String upgrade) implements ItemAttribute {

    public static final MapCodec<BackpackHasUpgradeAttribute> CODEC = Codec.STRING
            .xmap(BackpackHasUpgradeAttribute::new, BackpackHasUpgradeAttribute::upgrade)
            .fieldOf("value");

    public static final StreamCodec<ByteBuf, BackpackHasUpgradeAttribute> STREAM_CODEC = ByteBufCodecs.STRING_UTF8
            .map(BackpackHasUpgradeAttribute::new, BackpackHasUpgradeAttribute::upgrade);

    @Override
    public boolean appliesTo(ItemStack itemStack, Level level) {
        if(itemStack.getItem() instanceof BackpackItem) {
            return SophisticatedBackpackUtil.getUpgrades(itemStack).contains(upgrade);
        }

        return false;
    }



    @Override
    public ItemAttributeType getType() {
        return SophisticatedBackpackAttributes.HAS_BACKPACK_UPGRADE;
    }

    @Override
    public String getTranslationKey() {
        return "has_backpack_upgrade";
    }

    @Override
    public Object[] getTranslationParameters() {
        String modifiedItemName = this.upgrade.replace("[", "").replace("]", "").trim();
        return new Object[]{modifiedItemName};
    }

    public static class Type implements ItemAttributeType {
        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new BackpackHasUpgradeAttribute("dummy");
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            List<ItemAttribute> list = new ArrayList<>();
            List<String> upgrades = SophisticatedBackpackUtil.getUpgrades(stack);
            if (!upgrades.isEmpty()) {
                for(String upgrade : upgrades) {
                    list.add(new BackpackHasUpgradeAttribute(upgrade));
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
