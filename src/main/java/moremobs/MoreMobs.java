package moremobs;

import moremobs.examples.*;
import moremobs.examples.mounts.*;
import necesse.engine.commands.CommandsManager;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.*;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;
import necesse.level.maps.biomes.Biome;
import necesse.entity.mobs.friendly.GrizzlyBearMob;
import necesse.inventory.item.matItem.MatItem;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;

@ModEntry
public class MoreMobs {

    public void init() {
        // Register our tiles
        TileRegistry.registerTile("exampletile", new ExampleTile(), 1, true);

        // Register out objects
        ObjectRegistry.registerObject("exampleobject", new ExampleObject(), 2, true);
        ObjectRegistry.registerObject("spidermountcobweb", new SpiderMountCobweb(), 2, true);

        // Register our items
        ItemRegistry.registerItem("exampleitem", new ExampleMaterialItem(), 10, true);
        ItemRegistry.registerItem("examplesword", new ExampleSwordItem(), 20, true);
        ItemRegistry.registerItem("icesword", new IceSwordItem(), 2000, true);
        ItemRegistry.registerItem("examplestaff", new ExampleProjectileWeapon(), 30, true);

        // Register mounts
        ItemRegistry.registerItem("spidermountitem", new SpiderMountItem(), 100, true);
        MobRegistry.registerMob("spidermountmob", SpiderMountMob.class, true);
        ItemRegistry.registerItem("grizzlybearmountitem", new GrizzlyBearMountItem(), 100, true);
        MobRegistry.registerMob("grizzlybearmountmob", GrizzlyBearMountMob.class, true);

        ItemRegistry.registerItem(
          "coalore",
          (new MatItem(500, new String[] { "anylog", "anyflammable" })).setItemCategory(new String[] { "materials", "ore" }),
          2.0F,
          true
        );

        // Register our mob
        MobRegistry.registerMob("examplemob", ExampleMob.class, true);

        // Register our projectile
        ProjectileRegistry.registerProjectile("exampleprojectile", ExampleProjectile.class, "exampleprojectile", "exampleprojectile_shadow");

        // Register our buff
        BuffRegistry.registerBuff("examplebuff", new ExampleBuff());

        PacketRegistry.registerPacket(ExamplePacket.class);
    }

    public void initResources() {
        // Sometimes your textures will have a black or other outline unintended under rotation or scaling
        // This is caused by alpha blending between transparent pixels and the edge
        // To fix this, run the preAntialiasTextures gradle task
        // It will process your textures and save them again with a fixed alpha edge color

        ExampleMob.texture = GameTexture.fromFile("mobs/examplemob");
        SpiderMountMob.texture = GameTexture.fromFile("mobs/spidermountmob");
        GrizzlyBearMountMob.texture = GameTexture.fromFile("mobs/grizzlybearmountmob");
    }

    public void postInit() {
        // Add recipes
        // Example item recipe, crafted in inventory for 2 iron bars
        Recipes.registerModRecipe(new Recipe(
                "exampleitem",
                1,
                RecipeTechRegistry.NONE,
                new Ingredient[]{
                        new Ingredient("ironbar", 2)
                }
        ).showAfter("woodboat")); // Show recipe after wood boat recipe
        // Example sword recipe, crafted in iron anvil using 4 example items and 5 copper bars
        Recipes.registerModRecipe(new Recipe(
                "examplesword",
                1,
                RecipeTechRegistry.IRON_ANVIL,
                new Ingredient[]{
                        new Ingredient("exampleitem", 4),
                        new Ingredient("copperbar", 5)
                }
        ));
        // Example staff recipe, crafted in workstation using 4 example items and 10 gold bars
        Recipes.registerModRecipe(new Recipe(
                "examplestaff",
                1,
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("exampleitem", 4),
                        new Ingredient("goldbar", 10)
                }
        ).showAfter("exampleitem")); // Show the recipe after example item recipe

        // Add out example mob to default cave mobs.
        // Spawn tables use a ticket/weight system. In general, common mobs have about 100 tickets.
        // Biome.defaultCaveMobs
        //         .add(100, "examplemob");

        // Register our server chat command
        CommandsManager.registerServerCommand(new ExampleChatCommand());

        GrizzlyBearMob.lootTable.items.add(
                new ChanceLootItem(1.0F, "grizzlybearmountitem")
        );
    }

}
