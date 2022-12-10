package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.events.ProjectileEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnProjectileShot {
	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer, String name, String comment ) {
			super( consumer, name, comment );
			CONTEXTS.add( this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, "", "" );
		}

		@SubscribeEvent
		public static void onProjectileShot( ProjectileEvent.Shot event ) {
			CONTEXTS.accept( new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< ProjectileEvent > {
		public final Projectile projectile;
		public final Level level;
		@Nullable final Entity owner;
		@Nullable public final ItemStack weapon;
		@Nullable public final ItemStack arrow;
		public final CompoundTag customTag;

		protected Data( ProjectileEvent event ) {
			super( Utility.castIfPossible( LivingEntity.class, event.projectile.getOwner() ), event );
			this.projectile = event.projectile;
			this.level = event.level;
			this.owner = event.owner;
			this.weapon = event.weapon;
			this.arrow = event.arrow;
			this.customTag = event.customTag;
		}

		public Data( ProjectileEvent.Shot event ) {
			this( ( ProjectileEvent )event );
		}
	}
}