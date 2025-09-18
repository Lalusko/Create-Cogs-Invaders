package net.lalusko.createcogsinvaders.item.custom;

import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TeslaCannonRenderer extends GeoItemRenderer<TeslaCannonItem> {
    public TeslaCannonRenderer() {
        super(new TeslaCannonModel());
    }
}
