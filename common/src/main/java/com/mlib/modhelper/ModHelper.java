package com.mlib.modhelper;

import com.mlib.annotation.AutoInstance;
import com.mlib.data.ISerializable;
import com.mlib.network.NetworkHandler;
import com.mlib.network.NetworkObject;
import com.mlib.registry.RegistryGroup;
import com.mlib.registry.RegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ModHelper {
	final List< Runnable > registerCallbacks = new ArrayList<>();
	final List< Object > instances = new ArrayList<>();
	final String modId;
	final Logger logger;
	final ClassFinder classFinder;
	final AdvancementCaller advancementCaller;
	final RegistryHandler registryHandler;
	final NetworkHandler networkHandler;

	public static ModHelper create( String modId ) {
		return new ModHelper( modId );
	}

	public void register() {
		this.registerCallbacks.forEach( Runnable::run );
	}

	public < Type > RegistryGroup< Type > create( Registry< Type > registry ) {
		return this.registryHandler.create( registry );
	}

	public < Type extends ISerializable > NetworkObject< Type > create( String id, Class< Type > clazz ) {
		return this.networkHandler.create( id, clazz );
	}

	public void triggerAchievement( ServerPlayer player, String id ) {
		this.advancementCaller.trigger( player, id );
	}

	public void log( String format, Object... args ) {
		this.logger.info( format.formatted( args ) );
	}

	public void logError( String format, Object... args ) {
		this.logger.error( format.formatted( args ) );
	}

	public < Type > Type getInstance( Class< Type > clazz ) {
		return this.instances.stream()
			.filter( clazz::isInstance )
			.map( clazz::cast )
			.findFirst()
			.orElseThrow();
	}

	public String getModId() {
		return this.modId;
	}

	public ResourceLocation getLocation( String id ) {
		return new ResourceLocation( this.getModId(), id );
	}

	public String getLocationString( String id ) {
		return this.getLocation( id ).toString();
	}

	void onRegister( Runnable callback ) {
		this.registerCallbacks.add( callback );
	}

	private ModHelper( String modId ) {
		this.modId = modId;
		this.logger = LoggerFactory.getLogger( modId );
		this.classFinder = new ClassFinder( this );
		this.advancementCaller = new AdvancementCaller( this );
		this.registryHandler = new RegistryHandler( this );
		this.networkHandler = new NetworkHandler( this );

		this.onRegister( this.classFinder::findClasses );
		this.onRegister( ()->this.instances.add( this.classFinder.getInstances( clazz->clazz.isAnnotationPresent( AutoInstance.class ) ) ) );
		this.onRegister( this.registryHandler::register );
		this.onRegister( this.networkHandler::register );
	}
}
