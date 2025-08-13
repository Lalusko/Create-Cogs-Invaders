package net.lalusko.createcogsinvaders.item;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreateCogsInvadersMod.MOD_ID);

    public static final RegistryObject<Item> ADVANCED_CHIP =
            ITEMS.register("advanced_chip", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ADVANCED_MECHANISM =
            ITEMS.register("advanced_mechanism", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BRASS_MUSIC_DISC =
            ITEMS.register("brass_music_disc", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HAND_DRILL =
            ITEMS.register("hand_drill", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_WIRE =
            ITEMS.register("copper_wire", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DIAL =
            ITEMS.register("dial", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DIAL_HEALING =
            ITEMS.register("dial_healing", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DIAL_REPAIR =
            ITEMS.register("dial_repair", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_CHIP =
            ITEMS.register("dynamic_chip", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_MECHANISM =
            ITEMS.register("dynamic_mechanism", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ELECTROCONDUCTIVE_CHIP =
            ITEMS.register("electroconductive_chip", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ELECTROCONDUCTIVE_MECHANISM =
            ITEMS.register("electroconductive_mechanism", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INCOMPLETE_ADVANCED_MECHANISM =
            ITEMS.register("incomplete_advanced_mechanism", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INCOMPLETE_DYNAMIC_MECHANISM =
            ITEMS.register("incomplete_dynamic_mechanism", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INCOMPLETE_ELECTROCONDUCTIVE_MECHANISM =
            ITEMS.register("incomplete_electroconductive_mechanism", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MEDKIT =
            ITEMS.register("medkit", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> NETHERITE_HAND_DRILL =
            ITEMS.register("netherite_hand_drill", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> POWDERED_SULFUR =
            ITEMS.register("powdered_sulfur", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_SULFUR =
            ITEMS.register("raw_sulfur", () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> REPAIR_KIT =
            ITEMS.register("repair_kit", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TESLA_BATTERY_AMMO =
            ITEMS.register("tesla_battery_ammo", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TESLA_CANNON =
            ITEMS.register("tesla_cannon", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TESLA_SHIELD =
            ITEMS.register("tesla_shield", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> XP_CONTAINER_EMPTY =
            ITEMS.register("xp_container_empty", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> XP_CONTAINER_FULL =
            ITEMS.register("xp_container_full", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
