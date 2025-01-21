package moremobs.examples.chests;

import java.awt.Color;
import java.awt.Rectangle;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.maps.Level;
import necesse.level.gameObject.furniture.StorageBoxInventoryObject;

public class MoreChestStorageBoxInventoryObject extends StorageBoxInventoryObject {
  // maybe upgrades in the future?

  public MoreChestStorageBoxInventoryObject(String textureName, int slots, ToolType toolType, Color mapColor) {
    super(textureName, slots, toolType, mapColor);
  }

  public MoreChestStorageBoxInventoryObject(String textureName, int slots, Color mapColor) {
    super(textureName, slots, mapColor);
  }
}
