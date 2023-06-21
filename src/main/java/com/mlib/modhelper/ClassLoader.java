package com.mlib.modhelper;

import com.mlib.MajruszLibrary;
import com.mlib.annotations.AutoInstance;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

class ClassLoader {
	final List< Object > instances = new ArrayList<>();

	public ClassLoader( ModHelper helper ) {
		helper.onRegister( ()->this.load( helper ) );
	}

	public void load( ModHelper helper ) {
		ModList.get()
			.getAllScanData()
			.stream()
			.filter( modFileScanData->modFileScanData.getTargets().containsKey( helper.getModId() ) )
			.map( ModFileScanData::getAnnotations )
			.flatMap( Collection::stream )
			.filter( annotationData->org.objectweb.asm.Type.getType( AutoInstance.class ).equals( annotationData.annotationType() ) )
			.map( data->{
				try {
					return Class.forName( data.clazz().getClassName() );
				} catch( ClassNotFoundException e ) {
					return null;
				}
			} )
			.map( clazz->{
				try {
					MajruszLibrary.logOnDev( "[ClassLoader] Class %s initialization initiated...", clazz.getCanonicalName() );
					Object instance = clazz.getConstructor().newInstance();
					MajruszLibrary.logOnDev( "[ClassLoader] Class %s has been initialized.", clazz.getCanonicalName() );
					return instance;
				} catch( Exception exception ) {
					MajruszLibrary.logOnDev( "[ClassLoader] (%s) %s", clazz.getCanonicalName(), exception.getMessage() );
					return null;
				}
			} )
			.forEach( this.instances::add );
	}

	public < Type > List< Type > getInstances( Class< Type > outputClass ) {
		List< Type > instances = new ArrayList<>();
		for( Object instance : this.instances ) {
			if( outputClass.isAssignableFrom( instance.getClass() ) ) {
				instances.add( outputClass.cast( instance ) );
			}
		}

		return instances;
	}

	public < Type > Optional< Type > getInstance( Class< Type > outputClass ) {
		List< Type > instances = this.getInstances( outputClass );

		return instances.size() == 1 ? Optional.of( instances.get( 0 ) ) : Optional.empty();
	}
}