package xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis;

import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis.affix.HasAffixAttribute;

public class ApotheosisAttributes {
    public static final ItemAttributeType APOTH_GEM_PURITY = new GemPurityAttribute.Type();
    public static final ItemAttributeType APOTH_GEM_BONUS_TYPE = new GemBonusTypeAttribute.Type();
    public static final ItemAttributeType APOTH_GEM_ID = new GemIdAttribute.Type();
    public static final ItemAttributeType APOTH_LOOT_RARITY = new LootRarityAttribute.Type();
    public static final ItemAttributeType APOTH_HAS_RARITY = new HasRarityAttribute.Type();
    public static final ItemAttributeType APOTH_HAS_AFFIX = new HasAffixAttribute.Type();
    public static final ItemAttributeType APOTH_SOCKET_COUNT = new SocketCountAttribute.Type();
    public static final ItemAttributeType APOTH_SOCKET_COUNT_EMPTY = new SocketCountEmptyAttribute.Type();

}
