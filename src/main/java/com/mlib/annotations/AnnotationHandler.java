package com.mlib.annotations;

import com.mlib.MajruszLibrary;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class AnnotationHandler {
	final List< Class< ? > > classes = new ArrayList<>();
	final List< Object > instances = new ArrayList<>();
	final Class< ? extends Annotation > annotationClass;

	public AnnotationHandler( String rootPackage, Class< ? extends Annotation > annotationClass ) {
		this.annotationClass = annotationClass;

		try {
			Enumeration< URL > resources = this.buildResources( rootPackage.replaceAll( "\\.", "/" ) );
			while( resources.hasMoreElements() ) {
				this.handle( new File( resources.nextElement().getPath() ) );
			}
			this.loadAllClasses();
		} catch( AnnotationHandler.AnnotationException exception ) {
			MajruszLibrary.logOnDev( "[AnnotationHandler] %s", exception.getMessage() );
		}
	}

	public AnnotationHandler( String rootPackage ) {
		this( rootPackage, AutoInstance.class );
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
				MajruszLibrary.logOnDev( "[AnnotationHandler] %s", exception.getMessage() );
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

	private void loadAllClasses() {
		try {
			for( Class< ? > _class : this.classes ) {
				try {
					this.instances.add( _class.getConstructor().newInstance() );
					MajruszLibrary.logOnDev( "[AnnotationHandler] Class %s has been initialized.", _class.getCanonicalName() );
				} catch( Throwable e ) {
					throw new AnnotationException( "%s does not have an empty constructor", _class.getName() );
				}
			}
		} catch( AnnotationHandler.AnnotationException exception ) {
			MajruszLibrary.logOnDev( "[AnnotationHandler] %s", exception.getMessage() );
		}
	}

	public static class AnnotationException extends Exception {
		public AnnotationException( String format, Object... params ) {
			super( String.format( format, params ) );
		}
	}
}
