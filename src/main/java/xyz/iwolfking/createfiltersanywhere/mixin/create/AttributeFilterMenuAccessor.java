package xyz.iwolfking.createfiltersanywhere.mixin.create;


import com.simibubi.create.content.logistics.filter.AttributeFilterMenu;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import net.createmod.catnip.data.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = AttributeFilterMenu.class,remap = false)
public interface AttributeFilterMenuAccessor {
    @Accessor("selectedAttributes")
    List<ItemAttribute.ItemAttributeEntry> getSelectedAttributes();
}