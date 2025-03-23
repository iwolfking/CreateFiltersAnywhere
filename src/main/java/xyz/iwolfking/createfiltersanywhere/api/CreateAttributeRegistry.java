package xyz.iwolfking.createfiltersanywhere.api;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import net.minecraft.core.Registry;
import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;
import xyz.iwolfking.createfiltersanywhere.api.attributes.impl.sophisticatedbackpacks.BackpackHasUUIDAttribute;
import xyz.iwolfking.createfiltersanywhere.api.attributes.impl.sophisticatedbackpacks.BackpackHasUpgradeAttribute;

public class CreateAttributeRegistry {

    public static final ItemAttributeType HAS_BACKPACK_UUID = new BackpackHasUUIDAttribute.Type();
    public static final ItemAttributeType HAS_BACKPACK_UPGRADE = new BackpackHasUpgradeAttribute.Type();

    public static void register(String id, ItemAttributeType type) {
        Registry.register(CreateBuiltInRegistries.ITEM_ATTRIBUTE_TYPE, CreateFiltersAnywhere.asResource(id), type);
    }
}
