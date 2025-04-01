package xyz.iwolfking.createfiltersanywhere.mixin.create;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;


@Mixin(value = FilteringBehaviour.class, remap = false)
public abstract class MixinCreateFilteringBehaviour extends BlockEntityBehaviour {

    @Shadow public abstract ItemStack getFilter();

    protected MixinCreateFilteringBehaviour(SmartBlockEntity be) {
        super(be);
    }

    @Inject(method = "test(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    public void checkFilter(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!isActive() || this.getFilter().isEmpty() || CFATests.checkFilter(stack, this.getFilter(), true, this.blockEntity.getLevel()));
    }

    @Shadow
    public abstract boolean isActive();
}
