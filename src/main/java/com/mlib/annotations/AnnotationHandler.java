package com.mlib.annotations;

import com.mlib.MajruszLibrary;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import org.objectweb.asm.Type;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

public class AnnotationHandler {
	final List< Class< ? > > classes = new ArrayList<>();
	final Class< ? extends Annotation > annotationClass;

	public AnnotationHandler( String rootPackage, Class< ? extends Annotation > annotationClass ) {
		this.annotationClass = annotationClass;

		try {
			Enumeration< URL > resources = this.buildResources( rootPackage.replaceAll( "\\.", "/" ) );
			while( resources.hasMoreElements() ) {
				this.handle( new File( resources.nextElement().getPath() ) );
			}
		} catch( AnnotationHandler.AnnotationException exception ) {
			MajruszLibrary.log( "[AnnotationHandler] %s", exception.getMessage() );
		}
	}

	public AnnotationHandler( String rootPackage ) {
		this( rootPackage, AutoInstance.class );
	}

	public List< Class< ? > > getClasses() {
		return this.classes;
	}

	public < ClassType > List< ClassType > getInstances( Class< ClassType > outputClass ) {
		List< ClassType > instances = new ArrayList<>();
		try {
			for( Class< ? > _class : this.getClasses() ) {
				if( outputClass.isAssignableFrom( _class ) ) {
					try {
						ClassType instance = ( ClassType )_class.getConstructor().newInstance();
						MajruszLibrary.log( "[AnnotationHandler] Class %s has been loaded.", instance.toString() );
						instances.add( instance );
					} catch( Exception e ) {
						throw new AnnotationException( "%s does not have empty constructor", _class.getName() );
					}
				}
			}
		} catch( AnnotationHandler.AnnotationException exception ) {
			MajruszLibrary.log( "[AnnotationHandler] %s", exception.getMessage() );
		}

		return instances;
	}

	private Enumeration< URL > buildResources( String rootPackage ) throws AnnotationException {
		try {
			return ClassLoader.getSystemResources( rootPackage );
		} catch( Exception ignored ) {
			throw new AnnotationException( "Invalid package %s", rootPackage );
		}
	}

	private void handle( File[] files ) throws AnnotationException {
		for( File file : files ) {
			this.handle( file );
		}
	}

	private void handle( File file ) throws AnnotationException {
		String path = file.getPath();
		if( file.isDirectory() ) {
			this.handle( file.listFiles() );
		} else if( file.getName().contains( ".class" ) ) {
			String classPackage = path.replaceAll( ".*\\\\(com\\\\.*).class$", "$1" ).replaceAll( "\\\\", "." );
			try {
				Class< ? > _class = Class.forName( classPackage );
				if( _class.isAnnotationPresent( this.annotationClass ) ) {
					this.classes.add( _class );
				}
			} catch( Exception ignored ) {
				throw new AnnotationException( "Package %s has failed to load", classPackage );
			}
		}
	}

	public static class AnnotationException extends Exception {
		public AnnotationException( String format, Object... params ) {
			super( String.format( format, params ) );
		}
	}
}
