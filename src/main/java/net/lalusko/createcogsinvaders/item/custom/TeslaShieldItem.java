package net.lalusko.createcogsinvaders.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;

public class TeslaShieldItem extends ShieldItem {
    public TeslaShieldItem(Item.Properties props) {
        super(props);
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
            private final net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer bewlr =
                    new net.lalusko.createcogsinvaders.client.render.TeslaShieldRenderer(
                            net.minecraft.client.Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                            net.minecraft.client.Minecraft.getInstance().getEntityModels());

            @Override
            public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return bewlr;
            }
        });
    }
}
