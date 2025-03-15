package xyz.iwolfking.createfiltersanywhere.api;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import net.minecraft.core.Registry;
import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;

public class CreateAttributeRegistry {
    public static ItemAttributeType register(String id, ItemAttributeType type) {
        return Registry.register(CreateBuiltInRegistries.ITEM_ATTRIBUTE_TYPE, CreateFiltersAnywhere.asResource(id), type);
    }
}
