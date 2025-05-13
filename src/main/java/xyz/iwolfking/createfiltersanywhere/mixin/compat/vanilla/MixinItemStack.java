package xyz.iwolfking.createfiltersanywhere.mixin.compat.vanilla;

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.iwolfking.createfiltersanywhere.api.core.CFACache;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements DataComponentHolder, IItemStackExtension, MutableDataComponentHolder {
    @Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"))
    private void removeFromCacheOnDurabilityChange(int p_220158_, ServerLevel p_346256_, LivingEntity p_220160_, Consumer<Item> p_348596_, CallbackInfo ci) {
        if(CFACache.ITEM_CACHES.contains(this.hashCode())) {
            CFACache.ITEM_CACHES.remove(this.hashCode());
        }

    }
}
