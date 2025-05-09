package xyz.iwolfking.createfiltersanywhere.mixin.compat.modularrouters;


import com.simibubi.create.content.logistics.filter.FilterItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(targets = {"me.desht.modularrouters.util.SetofItemStack$ItemStackHashingStrategy"}, remap = false)
public class MixinItemStackHashingStrategy {
    @Inject(method = "hashCode(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    public void filterHash(ItemStack object, CallbackInfoReturnable<Integer> cir) {
        if (object.getItem() instanceof FilterItem) {
            cir.setReturnValue(object.hashCode());
        }
    }

    @Inject(method = "equals(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    public void filterEquals(ItemStack o1, ItemStack o2, CallbackInfoReturnable<Boolean> cir) {
        if (o1 != null && o2 != null && o1.getItem() instanceof FilterItem && o2.getItem() instanceof FilterItem) {
            cir.setReturnValue(o1.hashCode() == o2.hashCode());
        }
    }
}
