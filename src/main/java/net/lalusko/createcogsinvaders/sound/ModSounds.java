package net.lalusko.createcogsinvaders.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final String MOD_ID = "create_cogs_invaders";

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

    public static final RegistryObject<SoundEvent> STATION_USE =
            SOUNDS.register("station_use",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "station_use")));
    public static final RegistryObject<SoundEvent> STATION_RELOAD =
            SOUNDS.register("station_reload",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "station_reload")));
    public static final RegistryObject<SoundEvent> STATION_ERROR =
            SOUNDS.register("station_error",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "station_error")));

    public static final RegistryObject<SoundEvent> THE_STORY_UNFOLDS =
            SOUNDS.register("jingle_punks_the_story_unfolds",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "jingle_punks_the_story_unfolds")));
}
