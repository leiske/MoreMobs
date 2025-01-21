package moremobs.examples.chests;

import java.awt.Color;
import java.util.function.Supplier;
import necesse.engine.registries.*;
import necesse.inventory.recipe.Recipes;
import necesse.inventory.recipe.Recipe;
import necesse.engine.registries.RecipeTechRegistry;
import moremobs.examples.chests.*;
import necesse.engine.registries.ObjectRegistry;

public class MoreChestsChest {

  public static final MoreChestsChest[] Chests = {
    new MoreChestsChest(
      "goldchest",
      50,
      new Color(255,233,73),
      100,
      () -> new Recipe("goldchest", 1, RecipeTechRegistry.IRON_ANVIL, Recipes.ingredientsFromScript("{{goldbar, 10}}"))
    ),
    new MoreChestsChest(
      "ivychest",
      60,
      new Color(76,95,51),
      200,
      () -> new Recipe("ivychest", 1, RecipeTechRegistry.DEMONIC_ANVIL, Recipes.ingredientsFromScript("{{ivybar, 10}}" ))
    ),
  };

  public String chestTexture;
  public Color chestColor;
  public int slots;
  public int brokerValue;

  private final Supplier<Recipe> recipeSupplier;

  public MoreChestsChest(String chestTexture, int slots, Color chestColor, int brokerValue, Supplier<Recipe> recipe) {
    this.chestTexture = chestTexture;
    this.slots = slots;
    this.chestColor = chestColor;
    this.brokerValue = brokerValue;
    this.recipeSupplier = recipe;
  }

  public void registerRecipe() {
    Recipe recipe = recipeSupplier.get();
    System.out.println(recipe);
    Recipes.registerModRecipe(recipe);
  }

  public void registerObject() {
    System.out.println("Registering chest object: " + this.chestTexture);
    ObjectRegistry.registerObject(
      this.chestTexture,
      new MoreChestStorageBoxInventoryObject(this.chestTexture, this.slots, this.chestColor),
      this.brokerValue,
      true
    );
  }
}
