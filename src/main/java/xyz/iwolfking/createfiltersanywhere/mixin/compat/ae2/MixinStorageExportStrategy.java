package xyz.iwolfking.createfiltersanywhere.mixin.compat.ae2;

import appeng.api.behaviors.StackExportStrategy;
import appeng.api.behaviors.StackTransferContext;
import appeng.api.config.Actionable;
import appeng.api.networking.storage.IStorageService;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.StorageHelper;
import appeng.parts.automation.HandlerStrategy;
import appeng.parts.automation.StorageExportStrategy;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.logistics.filter.FilterItem;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xyz.iwolfking.createfiltersanywhere.Config;
import xyz.iwolfking.createfiltersanywhere.api.compat.AE2KeyHandler;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;


@Mixin(value = StorageExportStrategy.class, remap = false)
public abstract class MixinStorageExportStrategy<T, S> implements StackExportStrategy {

    @Shadow @Final private HandlerStrategy<T, S> handlerStrategy;
    @Shadow @Final private BlockCapabilityCache<T, Direction> cache;
    @Shadow @Final private static Logger LOG;

    /**
     * @author iwolfking
     * @reason Add supporter for ME Export Bus
     */
    @Overwrite
    public long transfer(StackTransferContext context, AEKey what, long amount) {
        if (!this.handlerStrategy.isSupported(what)) {
            return 0L;
        } else {
            T adjacentStorage = (T) this.cache.getCapability();
            if (adjacentStorage == null) {
                return 0L;
            } else {
                IStorageService inv = context.getInternalStorage();
                if (what instanceof AEItemKey itemKey && Config.AE2_COMPAT.get() && itemKey.getItem() instanceof FilterItem) {
                    for (Object2LongMap.Entry<AEKey> key : inv.getInventory().getAvailableStacks()) {
                        AEKey aek = key.getKey();
                        if (!(aek instanceof AEItemKey itemKey2)) {
                            continue;
                        }

                        if (AE2KeyHandler.checkFilter(itemKey2, itemKey, true, null)) {
                            what = aek;
                            break;
                        }
                    }
                }
                long extracted = StorageHelper.poweredExtraction(context.getEnergySource(), inv.getInventory(), what, amount, context.getActionSource(), Actionable.SIMULATE);
                long wasInserted = this.handlerStrategy.insert(adjacentStorage, what, extracted, Actionable.SIMULATE);
                if (wasInserted > 0L) {
                    extracted = StorageHelper.poweredExtraction(context.getEnergySource(), inv.getInventory(), what, wasInserted, context.getActionSource(), Actionable.MODULATE);
                    wasInserted = this.handlerStrategy.insert(adjacentStorage, what, extracted, Actionable.MODULATE);
                    if (wasInserted < extracted) {
                        long leftover = extracted - wasInserted;
                        leftover -= inv.getInventory().insert(what, leftover, Actionable.MODULATE, context.getActionSource());
                        if (leftover > 0L) {
                            LOG.error("Storage export: adjacent block unexpectedly refused insert, voided {}x{}", leftover, what);
                        }
                    }
                }

                return wasInserted;
            }
        }
    }
}
