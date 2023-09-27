package net.mlib.modhelper;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class ClassFinder {
	final List< Class< ? > > classes = new ArrayList<>();
	final ModHelper helper;

	public ClassFinder( ModHelper helper ) {
		this.helper = helper;
	}

	public void findClasses() {
		this.classes.addAll( this.findClassesInPackage().stream().filter( clazz->!this.classes.contains( clazz ) ).toList() );
		this.classes.addAll( this.findClassesInJar().stream().filter( clazz->!this.classes.contains( clazz ) ).toList() );
		if( this.classes.isEmpty() ) {
			throw new IllegalStateException( "ClassFinder did not find any classes" );
		}
	}

	public < Type > Type getInstance( Predicate< Class< ? > > predicate ) {
		return ( Type )this.getInstances( predicate ).get( 0 );
	}

	public List< ? > getInstances( Predicate< Class< ? > > predicate ) {
		return this.classes.stream()
			.filter( predicate )
			.map( clazz->{
				try {
					return clazz.getConstructor().newInstance();
				} catch( Exception exception ) {
					return null;
				}
			} )
			.toList();
	}

	private List< Class< ? > > findClassesInPackage() {
		URL resource = ClassLoader.getSystemResource( "net/%s".formatted( this.helper.getModId() ) );
		if( resource != null ) {
			File file = new File( resource.getPath() );
			if( file.isDirectory() ) {
				return this.findClassesInPackage( file, "net.%s".formatted( this.helper.getModId() ) );
			}
		}

		return List.of();
	}

	private List< Class< ? > > findClassesInPackage( File file, String packageName ) {
		List< Class< ? > > classes = new ArrayList<>();
		for( File subfile : file.listFiles() ) {
			if( subfile.getName().contains( "mixin" ) ) {
				continue;
			}

			if( subfile.isFile() && subfile.getName().endsWith( ".class" ) ) {
				try {
					classes.add( Class.forName( "%s.%s".formatted( packageName, subfile.getName().replace( ".class", "" ) ) ) );
				} catch( Exception exception ) {
					this.helper.logError( "Failed to find class: %s", exception.toString() );
				}
			} else if( subfile.isDirectory() ) {
				classes.addAll( this.findClassesInPackage( subfile, "%s.%s".formatted( packageName, subfile.getName() ) ) );
			}
		}

		return classes;
	}

	private List< Class< ? > > findClassesInJar() {
		List< Class< ? > > classes = new ArrayList<>();
		File mods = Paths.get( "./mods" ).toFile();
		if( !mods.isDirectory() ) {
			return classes;
		}

		for( File mod : mods.listFiles() ) {
			try {
				JarFile modJar = new JarFile( mod );
				if( modJar.getJarEntry( "net/%s".formatted( this.helper.getModId() ) ) == null ) {
					continue;
				}

				Enumeration< JarEntry > entries = modJar.entries();
				while( entries.hasMoreElements() ) {
					JarEntry jarEntry = entries.nextElement();
					if( jarEntry.getName().contains( "mixin" ) ) {
						continue;
					}

					if( jarEntry.getName().endsWith( ".class" ) ) {
						Class< ? > clazz = Class.forName( jarEntry.getName().replace( "/", "." ).replace( ".class", "" ) );
						classes.add( clazz );
					}
				}
			} catch( Exception exception ) {
				this.helper.logError( "Failed to find class: %s", exception );
			}
		}

		return classes;
	}
}
