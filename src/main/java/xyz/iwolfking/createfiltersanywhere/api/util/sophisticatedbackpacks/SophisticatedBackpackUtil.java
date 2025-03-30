package xyz.iwolfking.createfiltersanywhere.api.util.sophisticatedbackpacks;

import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SophisticatedBackpackUtil {
    public static List<String> getUpgrades(ItemStack stack) {
        List<String> upgradeNames = new ArrayList<>();
        if(stack.getItem() instanceof BackpackItem) {
            IBackpackWrapper wrapper = BackpackWrapper.fromStack(stack);
            for(IUpgradeWrapper upgradeWrapper : wrapper.getUpgradeHandler().getSlotWrappers().values()) {
                upgradeNames.add(upgradeWrapper.getUpgradeStack().getDisplayName().getString());
            }
        }
        return upgradeNames;
    }

    public static String getBackpackUUID(ItemStack stack) {
        if(stack.getItem() instanceof BackpackItem) {
            Optional<UUID> uuid = BackpackWrapper.fromStack(stack).getContentsUuid();

            if(uuid.isPresent()) {
                return uuid.get().toString();
            }
        }

        return null;
    }
}
