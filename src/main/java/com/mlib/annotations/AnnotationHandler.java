package com.mlib.annotations;

import com.mlib.MajruszLibrary;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;

public class AnnotationHandler {
	final Map< String, Class< ? > > cachedClasses = new HashMap<>();

	public AnnotationHandler( String rootPackage ) {
		try {
			Enumeration< URL > resources = this.buildResources( rootPackage.replaceAll( "\\.", "/" ) );
			while( resources.hasMoreElements() ) {
				this.handle( new File( resources.nextElement().getPath() ) );
			}
		} catch( AnnotationHandler.AnnotationException exception ) {
			MajruszLibrary.log( "[AnnotationHandler] %s", exception.getMessage() );
		}
	}

	public List< Class< ? > > getClassesWithAnnotation( Class< ? extends Annotation > annotationClass ) {
		List< Class< ? > > classes = new ArrayList<>();
		this.cachedClasses.forEach( ( path, _class )->{
			if( _class.getAnnotation( annotationClass ) != null ) {
				classes.add( _class );
			}
		} );

		return classes;
	}

	public < ClassType > List< ClassType > getInstances( Class< ClassType > outputClass, Class< ? extends Annotation > annotationClass ) {
		List< ClassType > classes = new ArrayList<>();
		try {
			for( Class< ? > _class : this.getClassesWithAnnotation( annotationClass ) ) {
				if( outputClass.isAssignableFrom( _class ) ) {
					try {
						classes.add( ( ClassType )_class.getConstructor().newInstance() );
					} catch( Exception e ) {
						throw new AnnotationException( "%s does not have empty constructor", _class.getName() );
					}
				}
			}
		} catch( AnnotationHandler.AnnotationException exception ) {
			MajruszLibrary.log( "[AnnotationHandler] %s", exception.getMessage() );
		}

		return classes;
	}

	public < ClassType > List< ClassType > getInstances( Class< ClassType > outputClass ) {
		return this.getInstances( outputClass, AutoInstance.class );
	}

	private Enumeration< URL > buildResources( String rootPackage ) throws AnnotationException {
		try {
			return ClassLoader.getSystemClassLoader().getResources( rootPackage );
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
				this.cachedClasses.put( path, ClassLoader.getSystemClassLoader().loadClass( classPackage ) );
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
