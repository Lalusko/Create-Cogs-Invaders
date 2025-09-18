package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TeslaCannonModel extends GeoModel<TeslaCannonItem> {
    @Override
    public ResourceLocation getModelResource(TeslaCannonItem animatable) {
        return new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "geo/tesla_cannon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TeslaCannonItem animatable) {
        return new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "textures/item/tesla_cannon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TeslaCannonItem animatable) {
        return new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "animations/tesla_cannon.animation.json");
    }
}
