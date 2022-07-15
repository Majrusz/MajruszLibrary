package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.event.level.ExplosionEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;

public class OnExplosionData extends ContextData.Event< ExplosionEvent > {
	public final Explosion explosion;
	@Nullable public final LivingEntity sourceMob;
	public final MutableFloat radius;
	public final MutableBoolean causesFire;

	public OnExplosionData( ExplosionEvent event ) {
		super( event.getExplosion().getSourceMob(), event );
		this.explosion = event.getExplosion();
		this.sourceMob = this.explosion.getSourceMob();
		this.radius = new MutableFloat( this.explosion.radius );
		this.causesFire = new MutableBoolean( this.explosion.fire );
	}
}