package xyz.iwolfking.createfiltersanywhere.api.util;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities; // Or ForgeCapabilities.ENERGY on older Forge
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.Optional;

public class EnergyAttributeHelper {

    private static Optional<IEnergyStorage> getEnergyStorage(ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        
        IEnergyStorage energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        

        return Optional.ofNullable(energy);
    }

    public static boolean isUncharged(ItemStack stack) {
        return getEnergyStorage(stack)
                .map(energy -> energy.getEnergyStored() == 0)
                .orElse(false);
    }

    public static boolean canBeCharged(ItemStack stack) {
        return getEnergyStorage(stack).isPresent();
    }

    public static boolean isFullyCharged(ItemStack stack) {
        return getEnergyStorage(stack)
                .map(energy -> energy.getMaxEnergyStored() > 0 && energy.getEnergyStored() >= energy.getMaxEnergyStored())
                .orElse(false);
    }
}