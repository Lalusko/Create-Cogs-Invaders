package net.lalusko.createcogsinvaders.item;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.item.custom.*;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
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
            ITEMS.register("brass_music_disc", () -> new RecordItem(7,
                    ModSounds.JINGLE_PUNKS_THE_STORY_UNFOLDS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 1800));

    public static final RegistryObject<Item> COPPER_WIRE =
            ITEMS.register("copper_wire", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DIAL =
            ITEMS.register("dial", () -> new DialItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DIAL_HEALING =
            ITEMS.register("dial_healing", () -> new DialHealingItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DIAL_REPAIR =
            ITEMS.register("dial_repair", () -> new DialRepairItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DYNAMIC_CHIP =
            ITEMS.register("dynamic_chip", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DYNAMIC_MECHANISM =
            ITEMS.register("dynamic_mechanism", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ELECTROCONDUCTIVE_CHIP =
            ITEMS.register("electroconductive_chip", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ELECTROCONDUCTIVE_MECHANISM =
            ITEMS.register("electroconductive_mechanism", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ELECTROSHOCK_CHARGE =
            ITEMS.register("electroshock_charge", () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> INCOMPLETE_ADVANCED_MECHANISM =
            ITEMS.register("incomplete_advanced_mechanism", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> INCOMPLETE_DYNAMIC_MECHANISM =
            ITEMS.register("incomplete_dynamic_mechanism", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> INCOMPLETE_ELECTROCONDUCTIVE_MECHANISM =
            ITEMS.register("incomplete_electroconductive_mechanism", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> LARGE_BOTTLE =
            ITEMS.register("large_bottle", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MEDKIT =
            ITEMS.register("medkit", () -> new MedkitItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> POWDERED_SULFUR =
            ITEMS.register("powdered_sulfur", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_SULFUR =
            ITEMS.register("raw_sulfur", () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> REPAIR_KIT =
            ITEMS.register("repair_kit", () -> new RepairKitItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> TESLA_BATTERY_AMMO =
            ITEMS.register("tesla_battery_ammo", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> TESLA_CANNON = ITEMS.register("tesla_cannon",
            () -> new Item(new Item.Properties().stacksTo(1).durability(100)));

    public static final RegistryObject<Item> XP_CONTAINER =
            ITEMS.register("xp_container", () -> new Item(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
