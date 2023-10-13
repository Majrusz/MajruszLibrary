package com.mlib.temp;

import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import com.mlib.client.CustomParticle;
import com.mlib.math.Random;
import com.mojang.math.Axis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

@OnlyIn( Dist.CLIENT )
public class TheParticle extends CustomParticle {
	private final Quaternionf onGroundQuaternion = Axis.XP.rotation( Mth.HALF_PI ).rotateZ( Random.nextInt( 0, 3 ) * Mth.HALF_PI );
	private final float yOffset = Random.nextFloat( 0.001f, 0.005f );
	private final SpriteSet spriteSet;

	public TheParticle( ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet ) {
		super( level, x, y, z, xSpeed, ySpeed, zSpeed );

		float randomRatio = Random.nextFloat();
		float colorVariation = Mth.lerp( randomRatio, 0.6f, 0.9f );

		this.spriteSet = spriteSet;
		this.xd = this.xd * 0.0025 + Random.nextDouble( -xSpeed, xSpeed );
		this.yd = this.yd * 0.0020 + Random.nextDouble( -ySpeed, ySpeed );
		this.zd = this.zd * 0.0025 + Random.nextDouble( -zSpeed, zSpeed );
		this.lifetime = 40;
		this.age = ( int )Mth.lerp( randomRatio, 0.0f, 0.5f * this.lifetime );
		this.scaleFormula = lifetime->1.5f;

		this.pickSprite( this.spriteSet );
		this.setColor( colorVariation, colorVariation, colorVariation );
	}

	@Override
	public float getY( float y ) {
		return y + this.yOffset; // offset added to remove z-fighting
	}

	@Override
	public Quaternionf getQuaternion( Quaternionf quaternion ) {
		return this.onGround ? this.onGroundQuaternion : quaternion;
	}

	@OnlyIn( Dist.CLIENT )
	public static class Factory extends SimpleFactory {
		public Factory( SpriteSet sprite ) {
			super( sprite, TheParticle::new );
		}
	}
}
