package xyz.iwolfking.createfiltersanywhere.mixin.create;

import com.simibubi.create.content.logistics.filter.FilterItem;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
@Restriction(
        require = {
                @Condition("create")
        }
)
@Mixin(value = FilterItem.class, remap = false)
public interface FilterItemInvoker {
    @Invoker
    List<Component> invokeMakeSummary(ItemStack stack);
}