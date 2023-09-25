package net.mlib.modhelper;

import net.minecraft.resources.ResourceLocation;
import net.mlib.annotations.AutoInstance;
import net.mlib.annotations.PlatformImplementation;
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

	public static ModHelper create( String modId ) {
		return new ModHelper( modId );
	}

	public void register() {
		this.registerCallbacks.forEach( Runnable::run );
	}

	public void log( String format, Object... args ) {
		this.logger.info( format.formatted( args ) );
	}

	public void logError( String format, Object... args ) {
		this.logger.error( format.formatted( args ) );
	}


	public < Type > Type getPlatformImplementation( Class< Type > clazz ) {
		return this.classFinder.getInstance( subclazz->clazz.isAssignableFrom( subclazz ) && subclazz.isAnnotationPresent( PlatformImplementation.class ) );
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

	private ModHelper( String modId ) {
		this.modId = modId;
		this.logger = LoggerFactory.getLogger( modId );
		this.classFinder = new ClassFinder( this );

		this.onRegister( this.classFinder::findClasses );
		this.onRegister( ()->this.instances.add( this.classFinder.getInstances( clazz->clazz.isAnnotationPresent( AutoInstance.class ) ) ) );
	}
}
