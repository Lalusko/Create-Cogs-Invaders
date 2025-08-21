package net.lalusko.createcogsinvaders.sound;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CreateCogsInvadersMod.MOD_ID);

    public static final RegistryObject<SoundEvent> STATION_RELOAD = registerSoundEvents("station_reload");
    public static final RegistryObject<SoundEvent> STATION_USE = registerSoundEvents("station_use");
    public static final RegistryObject<SoundEvent> STATION_ERROR = registerSoundEvents("station_error");
    public static final RegistryObject<SoundEvent> JINGLE_PUNKS_THE_STORY_UNFOLDS = registerSoundEvents("jingle_punks_the_story_unfolds");
    public static final RegistryObject<SoundEvent> ELECTROSHOCK_START = registerSoundEvents("electroshock_start");
    public static final RegistryObject<SoundEvent> TESLA_CANNON_SHOOT = registerSoundEvents("shocking_shoot");
    public static final RegistryObject<SoundEvent> AMMO_RELOAD = registerSoundEvents("ammo_recharge");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CreateCogsInvadersMod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
