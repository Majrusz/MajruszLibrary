package com.mlib.entities;

import com.mlib.Utility;
import com.mlib.data.SerializableStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public abstract class CustomSkills< SkillType extends Enum< ? > > {
	protected final PathfinderMob mob;
	protected final SimpleChannel channel;
	protected final IMessageConstructor< SkillType > messageConstructor;
	private final HashMap< Integer, List< Runnable > > callbacks = new HashMap<>();
	private SkillType skillType = null;
	private int ticksLeft = 0;
	private int ticksTotal = 1;

	public CustomSkills( PathfinderMob mob, SimpleChannel channel, IMessageConstructor< SkillType > messageConstructor ) {
		this.mob = mob;
		this.channel = channel;
		this.messageConstructor = messageConstructor;
	}

	public boolean tryToStart( LivingEntity entity, double distanceSquared ) {
		return false;
	}

	public CustomSkills< SkillType > onTick( int tick, Runnable callback ) {
		if( this.callbacks.containsKey( tick ) ) {
			this.callbacks.get( tick ).add( callback );
		} else {
			List< Runnable > callbacks = new ArrayList<>();
			callbacks.add( callback );
			this.callbacks.put( tick, callbacks );
		}

		return this;
	}

	public CustomSkills< SkillType > onLastTick( Runnable callback ) {
		this.onTick( 1, callback );

		return this;
	}

	public CustomSkills< SkillType > onRatio( float ratio, Runnable callback ) {
		this.onTick( Math.round( this.ticksTotal * ( 1.0f - ratio ) ), callback );

		return this;
	}

	public void tick() {
		this.ticksLeft = Math.max( this.ticksLeft - 1, 0 );
		this.callbacks.getOrDefault( this.ticksLeft, new ArrayList<>() ).forEach( Runnable::run );
		if( this.ticksLeft == 0 && this.skillType != null ) {
			this.callbacks.clear();
			this.skillType = null;
		}
	}

	public boolean isUsing() {
		return this.skillType != null;
	}

	public boolean isUsing( SkillType skillType ) {
		return this.skillType == skillType;
	}

	public float getRatio() {
		return 1.0f - Mth.clamp( ( float )this.ticksLeft / this.ticksTotal, 0.0f, 1.0f );
	}

	public float getRatio( SkillType skillType ) {
		return this.isUsing( skillType ) ? this.getRatio() : 0.0f;
	}

	protected CustomSkills< SkillType > start( SkillType skillType, int ticks ) {
		this.skillType = skillType;
		this.ticksLeft = this.ticksTotal = ticks;
		if( this.mob.level instanceof ServerLevel level ) {
			this.channel.send( PacketDistributor.DIMENSION.with( level::dimension ), this.messageConstructor.construct( this.mob, ticks, skillType ) );
		}

		return this;
	}

	public static class Message< SkillType extends Enum< ? > > extends SerializableStructure {
		int entityId;
		int ticks;
		SkillType skillType;

		public Message( Entity entity, int ticks, SkillType skillType, Supplier< SkillType[] > skillTypes ) {
			this( skillTypes );

			this.entityId = entity.getId();
			this.ticks = ticks;
			this.skillType = skillType;
		}

		public Message( Supplier< SkillType[] > skillTypes ) {
			this.define( null, ()->this.entityId, x->this.entityId = x );
			this.define( null, ()->this.ticks, x->this.ticks = x );
			this.define( null, ()->this.skillType, x->this.skillType = x, skillTypes );
		}

		@Override
		@OnlyIn( Dist.CLIENT )
		public void onClient( NetworkEvent.Context context ) {
			Level level = Minecraft.getInstance().level;
			if( level == null )
				return;

			ICustomSkillProvider< ? > skillProvider = Utility.castIfPossible( ICustomSkillProvider.class, level.getEntity( this.entityId ) );
			if( skillProvider == null )
				return;

			CustomSkills< SkillType > skills = ( CustomSkills< SkillType > )skillProvider.getCustomSkills();
			if( skills != null ) {
				skills.start( this.skillType, this.ticks );
			}
		}
	}

	@FunctionalInterface
	public interface IMessageConstructor< SkillType extends Enum< ? > > {
		Message< SkillType > construct( PathfinderMob entity, int ticks, SkillType skillType );
	}
}
