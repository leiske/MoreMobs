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

  public void hitMob(InventoryItem item, ToolItemMobAbilityEvent event, Level level, Mob target, Mob attacker) {
      super.hitMob(item, event, level, target, attacker);
      if (level.isServer() && target != null) {
        BuffManager attackerBM = attacker.buffManager;
        float thresholdMod = ((Float)attackerBM.getModifier(BuffModifiers.CRIT_CHANCE)).floatValue() + ((Float)attackerBM.getModifier(BuffModifiers.MELEE_CRIT_CHANCE)).floatValue();
        float crystallizeMod = ((Float)attackerBM.getModifier(BuffModifiers.CRIT_DAMAGE)).floatValue() + ((Float)attackerBM.getModifier(BuffModifiers.MELEE_CRIT_CHANCE)).floatValue();
        int stackThreshold = (int)GameMath.limit(10.0F - 7.0F * thresholdMod, 3.0F, 10.0F);
        float crystallizeDamageMultiplier = GameMath.limit(crystallizeMod, 2.0F, stackThreshold);
        Buff crystallizeBuff = BuffRegistry.Debuffs.CRYSTALLIZE_BUFF;
        ActiveBuff ab = new ActiveBuff(crystallizeBuff, target, 10000, (Attacker)attacker);
        target.buffManager.addBuff(ab, true);
        if (target.buffManager.getBuff(crystallizeBuff).getStacks() >= stackThreshold) {
          level.entityManager.addLevelEvent((LevelEvent)new CrystallizeShatterEvent(target, CrystallizeShatterEvent.ParticleType.AMETHYST));
          target.buffManager.removeBuff(crystallizeBuff, true);
          GameDamage finalDamage = getDamage(item).modDamage(crystallizeDamageMultiplier);
          target.isServerHit(finalDamage, 0.0F, 0.0F, 0.0F, (Attacker)attacker);
        } 
      } 
    }

}
