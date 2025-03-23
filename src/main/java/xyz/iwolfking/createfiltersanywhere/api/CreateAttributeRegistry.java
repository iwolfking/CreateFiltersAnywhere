package xyz.iwolfking.createfiltersanywhere.api;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import com.simibubi.create.content.logistics.item.filter.attribute.SingletonItemAttribute;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class CreateAttributeRegistry {

    public static ItemAttributeType singleton(String id, Predicate<ItemStack> predicate) {
        return register(id, new SingletonItemAttribute.Type(type -> new SingletonItemAttribute(type, (stack, level) -> predicate.test(stack), id)));
    }

    public static ItemAttributeType singleton(String id, BiPredicate<ItemStack, Level> predicate) {
        return register(id, new SingletonItemAttribute.Type(type -> new SingletonItemAttribute(type, predicate, id)));
    }

    public static ItemAttributeType register(String id, ItemAttributeType type) {
        return Registry.register(CreateBuiltInRegistries.ITEM_ATTRIBUTE_TYPE, CreateFiltersAnywhere.asResource(id), type);
    }
}
