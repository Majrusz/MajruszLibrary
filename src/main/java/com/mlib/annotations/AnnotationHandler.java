package com.mlib.annotations;

import com.mlib.MajruszLibrary;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
						instances.add( instance );
						MajruszLibrary.log( "[AnnotationHandler] Class %s has been initialized.", instance.toString() );
					} catch( Throwable e ) {
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
		} catch( Throwable ignored ) {
			throw new AnnotationException( "Invalid package %s", rootPackage );
		}
	}

	private void handle( File[] files ) {
		for( File file : files ) {
			try {
				this.handle( file );
			} catch( AnnotationHandler.AnnotationException exception ) {
				MajruszLibrary.log( "[AnnotationHandler] %s", exception.getMessage() );
			}
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
			} catch( Throwable ignored ) {
				throw new AnnotationException( "Class %s has failed to load.", classPackage );
			}
		}
	}

	public static class AnnotationException extends Exception {
		public AnnotationException( String format, Object... params ) {
			super( String.format( format, params ) );
		}
	}
}
