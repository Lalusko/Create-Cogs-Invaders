package net.lalusko.createcogsinvaders.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class HealingParticles extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected HealingParticles(ClientLevel lvl, double x, double y, double z,
                              double vx, double vy, double vz, SpriteSet sprites) {
        super(lvl, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.lifetime = 8;         // 8 ticks = 1 frame por tick (ajusta a gusto)
        this.gravity = 0.0F;
        this.quadSize = 0.20F;
        this.xd = vx; this.yd = vy; this.zd = vz;
        this.setSpriteFromAge(sprites);
    }

    @Override public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites); // avanza frames según la edad
    }

    @Override public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel lvl,
                                       double x, double y, double z, double vx, double vy, double vz) {
            return new HealingParticles(lvl, x, y, z, vx, vy, vz, sprites);
        }
    }
}
