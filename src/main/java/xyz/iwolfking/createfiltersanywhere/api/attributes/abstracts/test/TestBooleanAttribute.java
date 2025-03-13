package xyz.iwolfking.createfiltersanywhere.api.attributes.abstracts.test;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import xyz.iwolfking.createfiltersanywhere.api.attributes.abstracts.BooleanAttribute;

public class TestBooleanAttribute extends BooleanAttribute {
    public TestBooleanAttribute(Boolean value) {
        super(true);
    }

    @Override
    public Boolean getValue(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    public String getTranslationKey() {
        return "is_sword_item";
    }
}
