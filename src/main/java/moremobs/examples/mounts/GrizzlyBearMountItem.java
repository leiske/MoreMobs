package moremobs.examples.mounts;

import necesse.entity.mobs.*;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.mountItem.MountItem;
import necesse.inventory.item.Item;

public class GrizzlyBearMountItem extends MountItem {
  public GrizzlyBearMountItem() {
    super("grizzlybearmountmob");
    this.rarity = Item.Rarity.RARE;
  }
  
  public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
    ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
    tooltips.add("Grizzly Bear Mount");
    tooltips.add("Grizzly Bear Mount again");
    return tooltips;
  }
}

