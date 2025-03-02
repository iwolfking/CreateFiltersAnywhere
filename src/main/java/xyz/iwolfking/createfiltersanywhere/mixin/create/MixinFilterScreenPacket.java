package xyz.iwolfking.createfiltersanywhere.mixin.create;


import com.simibubi.create.content.logistics.filter.FilterMenu;
import com.simibubi.create.content.logistics.filter.FilterScreenPacket;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.iwolfking.createfiltersanywhere.api.accessors.FilterMenuAdvancedAccessor;

@Restriction(
        require = {
                @Condition("create")
        }
)
@Mixin(value = FilterScreenPacket.class, remap = false)
public class MixinFilterScreenPacket {
    @Final
    @Shadow
    private FilterScreenPacket.Option option;

    @SuppressWarnings("target")
    @ModifyVariable(method = "handle", at = @At(value = "STORE", ordinal = 0), name = "c", remap = false)
    private FilterMenu modifyFilterMenu(FilterMenu c) {
        // Modify or use the FilterMenu instance `c` here
        if (this.option == FilterScreenPacket.Option.ADD_TAG) {
            ((FilterMenuAdvancedAccessor) c).vault_filters$setMatchAll(true);
        } else if (this.option == FilterScreenPacket.Option.ADD_INVERTED_TAG) {
            ((FilterMenuAdvancedAccessor) c).vault_filters$setMatchAll(false);
        }
        return c;
    }
}
