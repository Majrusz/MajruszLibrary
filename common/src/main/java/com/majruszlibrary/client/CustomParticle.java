package com.majruszlibrary.client;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn( Dist.CLIENT )
public abstract class CustomParticle extends TextureSheetParticle {
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
	public void render( VertexConsumer consumer, Camera camera, float partialTick ) {
		// it overwrites default behavior just to modify y and rotation
		// but it is not using mixin to modify them because other performance mods
		// like sodium also overwrites the code which makes the game crash
		Vec3 vec3 = camera.getPosition();
		float x = ( float )( Mth.lerp( partialTick, this.xo, this.x ) - vec3.x() );
		float y = this.getY( ( float )( Mth.lerp( partialTick, this.yo, this.y ) - vec3.y() ) );
		float z = ( float )( Mth.lerp( partialTick, this.zo, this.z ) - vec3.z() );
		Quaternionf quaternion;
		if( this.roll == 0.0F ) {
			quaternion = camera.rotation();
		} else {
			quaternion = new Quaternionf( camera.rotation() );
			quaternion.rotateZ( Mth.lerp( partialTick, this.oRoll, this.roll ) );
		}
		quaternion = this.getQuaternion( quaternion );

		Vector3f[] avector3f = new Vector3f[]{
			new Vector3f( -1.0f, -1.0f, 0.0f ),
			new Vector3f( -1.0f, 1.0f, 0.0f ),
			new Vector3f( 1.0f, 1.0f, 0.0f ),
			new Vector3f( 1.0f, -1.0f, 0.0f )
		};
		float size = this.getQuadSize( partialTick );

		for( int i = 0; i < 4; ++i ) {
			Vector3f vector3f = avector3f[ i ];
			vector3f.rotate( quaternion );
			vector3f.mul( size );
			vector3f.add( x, y, z );
		}

		float f6 = this.getU0();
		float f7 = this.getU1();
		float f4 = this.getV0();
		float f5 = this.getV1();
		int light = this.getLightColor( partialTick );
		consumer.vertex( avector3f[ 0 ].x(), avector3f[ 0 ].y(), avector3f[ 0 ].z() )
			.uv( f7, f5 )
			.color( this.rCol, this.gCol, this.bCol, this.alpha )
			.uv2( light )
			.endVertex();
		consumer.vertex( avector3f[ 1 ].x(), avector3f[ 1 ].y(), avector3f[ 1 ].z() )
			.uv( f7, f4 )
			.color( this.rCol, this.gCol, this.bCol, this.alpha )
			.uv2( light )
			.endVertex();
		consumer.vertex( avector3f[ 2 ].x(), avector3f[ 2 ].y(), avector3f[ 2 ].z() )
			.uv( f6, f4 )
			.color( this.rCol, this.gCol, this.bCol, this.alpha )
			.uv2( light )
			.endVertex();
		consumer.vertex( avector3f[ 3 ].x(), avector3f[ 3 ].y(), avector3f[ 3 ].z() )
			.uv( f6, f5 )
			.color( this.rCol, this.gCol, this.bCol, this.alpha )
			.uv2( light )
			.endVertex();
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public float getQuadSize( float scaleFactor ) {
		return this.quadSize * this.scaleFormula.apply( ( ( float )this.age + scaleFactor ) / ( float )this.lifetime );
	}

	public float getY( float y ) {
		return y;
	}

	public Quaternionf getQuaternion( Quaternionf quaternion ) {
		return quaternion;
	}

	@OnlyIn( Dist.CLIENT )
	public static class Factory< ParticleType extends Particle, OptionsType extends ParticleOptions > implements ParticleProvider< OptionsType > {
		private final SpriteSet spriteSet;
		private final IFactory< ParticleType > factory;
		private final IModification< ParticleType, OptionsType > function;

		public Factory( SpriteSet sprite, IFactory< ParticleType > factory, IModification< ParticleType, OptionsType > function ) {
			this.spriteSet = sprite;
			this.factory = factory;
			this.function = function;
		}

		public Factory( SpriteSet sprite, IFactory< ParticleType > factory ) {
			this( sprite, factory, ( particle, options )->{} );
		}

		@Override
		public Particle createParticle( OptionsType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed ) {
			ParticleType particle = this.factory.create( level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet );
			this.function.apply( particle, type );

			return particle;
		}
	}

	@OnlyIn( Dist.CLIENT )
	public static class SimpleFactory extends Factory< Particle, SimpleParticleType > {
		public SimpleFactory( SpriteSet sprite, IFactory< Particle > factory ) {
			super( sprite, factory );
		}
	}

	@FunctionalInterface
	@OnlyIn( Dist.CLIENT )
	public interface IFormula< Type > {
		Type apply( Type type );
	}

	@FunctionalInterface
	@OnlyIn( Dist.CLIENT )
	public interface IFactory< Type extends Particle > {
		Type create( ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet );
	}

	@FunctionalInterface
	@OnlyIn( Dist.CLIENT )
	public interface IModification< ParticleType extends Particle, OptionsType extends ParticleOptions > {
		void apply( ParticleType particle, OptionsType options );
	}
}
