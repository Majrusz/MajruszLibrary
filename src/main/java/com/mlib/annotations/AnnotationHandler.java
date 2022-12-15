package com.mlib.annotations;

import com.mlib.MajruszLibrary;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AnnotationHandler {
	final Class< ? extends Annotation > annotationClass;
	final List< ? extends Class< ? > > classes;
	final List< ? > instances;

	public AnnotationHandler( String packageName, Class< ? extends Annotation > annotationClass ) {
		this.annotationClass = annotationClass;
		Type type = Type.getType( annotationClass );
		this.classes = ModList.get()
			.getAllScanData()
			.stream()
			.map( ModFileScanData::getAnnotations )
			.flatMap( Collection::stream )
			.filter( annotationData->type.equals( annotationData.annotationType() ) )
			.map( data->{
				try {
					Class< ? > clazz = Class.forName( data.clazz().getClassName() );
					return clazz.getPackageName().contains( packageName ) ? clazz : null;
				} catch( ClassNotFoundException e ) {
					return null;
				}
			} )
			.toList();
		this.instances = this.classes.stream()
			.map( clazz->{
				try {
					Object instance = clazz.getConstructor().newInstance();
					MajruszLibrary.logOnDev( "[AnnotationHandler] Class %s has been initialized.", clazz.getCanonicalName() );
					return instance;
				} catch( Exception exception ) {
					MajruszLibrary.logOnDev( "[AnnotationHandler] %s", exception.getMessage() );
					return null;
				}
			} )
			.toList();
	}

	public AnnotationHandler( String packageName ) {
		this( packageName, AutoInstance.class );
	}

	public List< Class< ? > > getClasses() {
		return Collections.unmodifiableList( this.classes );
	}

	public List< Object > getInstances() {
		return Collections.unmodifiableList( this.instances );
	}

	public < ClassType > List< ClassType > getInstances( Class< ClassType > outputClass ) {
		List< ClassType > instances = new ArrayList<>();
		for( Object instance : this.instances ) {
			if( outputClass.isAssignableFrom( instance.getClass() ) ) {
				instances.add( outputClass.cast( instance ) );
			}
		}

		return instances;
	}
}
