package com.majruszlibrary.modhelper;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.data.Config;
import com.majruszlibrary.data.WorldData;
import com.majruszlibrary.network.NetworkHandler;
import com.majruszlibrary.network.NetworkObject;
import com.majruszlibrary.platform.Services;
import com.majruszlibrary.registry.RegistryGroup;
import com.majruszlibrary.registry.RegistryHandler;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModHelper {
	final List< Runnable > registerCallbacks = new ArrayList<>();
	final List< Object > instances = new ArrayList<>();
	final String modId;
	final Logger logger;
	final ClassFinder classFinder;
	final RegistryHandler registryHandler;
	final AdvancementCaller advancementCaller;
	final NetworkHandler networkHandler;
	final VersionChecker versionChecker;
	final ResourceLoader resourceLoader;
	final IDataPlatform data = Services.loadOptional( IDataPlatform.class ).orElse( null );

	public static ModHelper create( String modId ) {
		return new ModHelper( modId );
	}

	public void register() {
		this.registerCallbacks.forEach( Runnable::run );
	}

	public < Type > RegistryGroup< Type > create( Registry< Type > registry ) {
		return this.registryHandler.create( registry );
	}

	public < Type > NetworkObject< Type > create( String id, Class< Type > clazz ) {
		return this.networkHandler.create( id, clazz, ()->{
			try {
				return clazz.getConstructor().newInstance();
			} catch( Exception exception ) {
				throw new IllegalArgumentException();
			}
		} );
	}

	public < Type > void create( Class< Type > clazz, Consumer< Type > consumer ) {
		this.registryHandler.create( clazz, consumer );
	}

	public WorldData.Builder worldData( Class< ? > clazz ) {
		return new WorldData.Builder( this, clazz );
	}

	public Config.Builder< ? > config( Class< ? > clazz ) {
		return new Config.Builder<>( this, clazz );
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

	public < Type > Resource< Type > load( String id, Class< Type > clazz ) {
		return this.resourceLoader.load( this.getLocation( id ), clazz );
	}

	@OnlyIn( Dist.CLIENT )
	public < Type > Resource< Type > loadClient( String id, Class< Type > clazz ) {
		return this.resourceLoader.loadClient( this.getLocation( "custom/%s.json".formatted( id ) ), clazz );
	}

	public < Type extends IDataPlatform > Type getData( Class< Type > clazz ) {
		return clazz.cast( this.data );
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

	@OnlyIn( Dist.CLIENT )
	public ModelLayerLocation getLayerLocation( String id, String type ) {
		return new ModelLayerLocation( this.getLocation( id ), type );
	}

	@OnlyIn( Dist.CLIENT )
	public ModelLayerLocation getLayerLocation( String id ) {
		return this.getLayerLocation( id, "main" );
	}

	public ResourceLocation getLocation( String id ) {
		return new ResourceLocation( this.getModId(), id );
	}

	public String getLocationString( String id ) {
		return this.getLocation( id ).toString();
	}

	public void onRegister( Runnable callback ) {
		this.registerCallbacks.add( callback );
	}

	private ModHelper( String modId ) {
		this.modId = modId;
		this.logger = LoggerFactory.getLogger( modId );
		this.classFinder = new ClassFinder( this );
		this.registryHandler = new RegistryHandler( this );
		this.advancementCaller = new AdvancementCaller( this );
		this.networkHandler = new NetworkHandler( this );
		this.versionChecker = new VersionChecker( this );
		this.resourceLoader = new ResourceLoader( this );

		this.onRegister( this.classFinder::findClasses );
		this.onRegister( ()->this.instances.add( this.classFinder.getInstances( clazz->clazz.isAnnotationPresent( AutoInstance.class ) ) ) );
		this.onRegister( this.registryHandler::register );
		this.onRegister( this.networkHandler::register );
		this.onRegister( this.versionChecker::register );
	}
}
