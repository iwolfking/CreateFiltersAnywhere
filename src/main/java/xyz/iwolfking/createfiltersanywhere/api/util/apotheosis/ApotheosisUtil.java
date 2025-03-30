package xyz.iwolfking.createfiltersanywhere.api.util.apotheosis;

import dev.shadowsoffire.apotheosis.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.socket.gem.GemItem;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import dev.shadowsoffire.apotheosis.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.api.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ApotheosisUtil {
    public static boolean isGemItem(ItemStack stack) {
        return stack.getItem() instanceof GemItem;
    }

    public static Optional<Purity> getPurityOf(ItemStack stack) {
        if(isGemItem(stack)) {
            return Optional.ofNullable(GemItem.getPurity(stack));
        }

        return Optional.empty();
    }

    public static boolean isUniqueGem(ItemStack stack) {
        if(isGemItem(stack)) {
           DynamicHolder<Gem> gem = GemItem.getGem(stack);
           if(gem.getOptional().isPresent()) {
                return gem.get().isUnique();
           }
        }

        return false;
    }

    public static Set<GemBonus> getGemBonuses(ItemStack stack) {
        if(isGemItem(stack)) {
            DynamicHolder<Gem> gem = GemItem.getGem(stack);
            if(gem.getOptional().isPresent()) {
                return Set.copyOf(gem.get().getBonuses());
            }
        }

        return Set.of();
    }

    public static Set<String> getGemBonusesTypeNames(ItemStack stack) {
        Set<String> names = new HashSet<>();
        if(isGemItem(stack)) {
            DynamicHolder<Gem> gem = GemItem.getGem(stack);
            if(gem.getOptional().isPresent()) {
               gem.get().getBonuses().forEach(bonus -> {
                   names.add(StringUtils.toTitleCase(bonus.getTypeKey().getPath()));
               });
            }
        }

        return names;
    }
}
