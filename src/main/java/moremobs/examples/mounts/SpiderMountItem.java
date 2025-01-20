package moremobs.examples.mounts;

import necesse.entity.mobs.*;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.mountItem.MountItem;
import necesse.inventory.item.Item;

public class SpiderMountItem extends MountItem {
  public SpiderMountItem() {
    super("spidermountmob");
    this.rarity = Item.Rarity.RARE;
  }
  
  public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
    ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
    tooltips.add("Spider Mount");
    tooltips.add("Spider Mount again");
    return tooltips;
  }
}

