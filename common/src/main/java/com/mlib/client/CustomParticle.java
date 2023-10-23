package com.mlib.client;

import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import com.mlib.mixininterfaces.IMixinSingleQuadParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.joml.Quaternionf;

@OnlyIn( Dist.CLIENT )
public abstract class CustomParticle extends TextureSheetParticle implements IMixinSingleQuadParticle {
	public IFormula< Double > xdFormula = xd->xd * ( this.onGround ? 0.5 : 0.95 );
	public IFormula< Double > ydFormula = yd->yd - ( this.onGround ? 0.0 : 0.0375 );
	public IFormula< Double > zdFormula = zd->zd * ( this.onGround ? 0.5 : 0.95 );
	public IFormula< Float > alphaFormula = alpha->alpha;
	public IFormula< Float > scaleFormula = lifeRatio->1.0f - 0.5f * lifeRatio;

	public CustomParticle( ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed ) {
		super( level, x, y, z, xSpeed, ySpeed, zSpeed );
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if( ++this.age >= this.lifetime ) {
			this.remove();
		} else {
			this.move( this.xd, this.yd, this.zd );
			this.xd = this.xdFormula.apply( this.xd );
			this.yd = this.ydFormula.apply( this.yd );
			this.zd = this.zdFormula.apply( this.zd );
			this.alpha = this.alphaFormula.apply( this.alpha );
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public float getQuadSize( float scaleFactor ) {
		return this.quadSize * this.scaleFormula.apply( ( ( float )this.age + scaleFactor ) / ( float )this.lifetime );
	}

	@Override
	public float getY( float y ) {
		return y;
	}

	@Override
	public Quaternionf getQuaternion( Quaternionf quaternion ) {
		return quaternion;
	}

	@OnlyIn( Dist.CLIENT )
	public static abstract class SimpleFactory implements ParticleProvider< SimpleParticleType > {
		private final SpriteSet spriteSet;
		private final IFactory instanceFactory;

		public SimpleFactory( SpriteSet sprite, IFactory instanceFactory ) {
			this.spriteSet = sprite;
			this.instanceFactory = instanceFactory;
		}

		@Override
		public Particle createParticle( SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed,
			double zSpeed
		) {
			return this.instanceFactory.create( world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet );
		}

		@FunctionalInterface
		@OnlyIn( Dist.CLIENT )
		public interface IFactory {
			CustomParticle create( ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet );
		}
	}

	@FunctionalInterface
	@OnlyIn( Dist.CLIENT )
	public interface IFormula< Type > {
		Type apply( Type type );
	}
}
