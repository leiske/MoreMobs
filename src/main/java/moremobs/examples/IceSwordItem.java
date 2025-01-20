package moremobs.examples;

import necesse.engine.localization.Localization;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameMath;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.CrystallizeShatterEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.ToolItemMobAbilityEvent;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffManager;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.level.maps.Level;
import necesse.inventory.item.toolItem.swordToolItem.SwordToolItem;

// Extends SwordToolItem
public class IceSwordItem extends SwordToolItem {

    public IceSwordItem() {
        super(200);
        rarity = Item.Rarity.RARE;
        attackAnimTime.setBaseValue(100);
        attackDamage.setBaseValue(500);
        attackRange.setBaseValue(500);
        knockback.setBaseValue(500);
    }
}
