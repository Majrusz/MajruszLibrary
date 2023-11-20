package com.mlib.modhelper;

import com.google.gson.*;
import com.mlib.MajruszLibrary;
import com.mlib.contexts.OnPlayerLoggedIn;
import com.mlib.data.Reader;
import com.mlib.data.Serializables;
import com.mlib.platform.Integration;
import com.mlib.platform.Side;
import com.mlib.text.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class VersionChecker {
	static final Pattern VERSION = Pattern.compile( "(\\d+)\\.(\\d+)\\.(\\d+)" );
	static final List< Component > COMPONENTS = Collections.synchronizedList( new ArrayList<>() );
	static boolean IS_ENABLED = true;
	final ModHelper helper;
	final Gson gson;
	String currentVersion;
	String latestVersion;
	String homepage;
	Map< String, String > versions = new HashMap<>();

	static {
		Serializables.getStatic( MajruszLibrary.Config.class )
			.define( "send_new_available_updates_message", Reader.bool(), ()->IS_ENABLED, v->IS_ENABLED = v );

		Serializables.get( VersionChecker.class )
			.define( "homepage", Reader.string(), s->s.homepage, ( s, v )->s.homepage = v )
			.define( "promos", Reader.map( Reader.string() ), s->s.versions, ( s, v )->s.versions = v );

		OnPlayerLoggedIn.listen( VersionChecker::sendUpdateMessages )
			.addCondition( data->IS_ENABLED )
			.addCondition( data->COMPONENTS.size() > 0 )
			.addCondition( data->Side.isLogicalServer() && !Side.isDedicatedServer() || data.player.hasPermissions( 4 ) );
	}

	public VersionChecker( ModHelper helper ) {
		this.helper = helper;
		this.gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( VersionChecker.class, new TypeAdapter<>( ()->this ) )
			.setPrettyPrinting()
			.create();
	}

	public void register() {
		Optional< URL > update = Integration.getUpdateURL( this.helper.getModId() );
		if( update.isEmpty() ) {
			this.helper.logError( "Missing update url" );
			return;
		}

		Matcher matcher = VERSION.matcher( Integration.getVersion( this.helper.getModId() ) );
		if( !matcher.find() ) {
			this.helper.logError( "Not supported version format" );
			return;
		}

		try {
			java.io.Reader reader = new InputStreamReader( update.get().openStream() );
			this.gson.fromJson( reader, VersionChecker.class );
		} catch( Exception exception ) {
			this.helper.logError( "Failed to retrieve the latest mod versions from %s".formatted( update.get() ) );
			return;
		}

		String id = "%s-recommended".formatted( Integration.getMinecraftVersion() );
		if( !this.versions.containsKey( id ) ) {
			return;
		}

		this.currentVersion = matcher.group();
		this.latestVersion = this.versions.get( id );
		if( !this.isUpdateAvailable() ) {
			return;
		}

		MutableComponent name = TextHelper.literal( " [%s %s]", Integration.getName( this.helper.getModId() ), this.latestVersion )
			.withStyle( ChatFormatting.YELLOW )
			.withStyle( TextHelper.createURL( this.homepage ) );

		MutableComponent versions = TextHelper.translatable( "mlib.update_versions", name, this.currentVersion )
			.withStyle( ChatFormatting.GRAY );

		COMPONENTS.add( versions );
	}

	private boolean isUpdateAvailable() {
		Matcher current = VERSION.matcher( this.currentVersion );
		Matcher latest = VERSION.matcher( this.latestVersion );
		if( !current.find() || !latest.find() ) {
			this.helper.logError( "Not supported version format (current: %s, latest: %s)".formatted( this.currentVersion, this.latestVersion ) );
			return false;
		}

		int currentMajor = Integer.parseInt( current.group( 1 ) );
		int currentMinor = Integer.parseInt( current.group( 2 ) );
		int currentPatch = Integer.parseInt( current.group( 3 ) );
		int latestMajor = Integer.parseInt( latest.group( 1 ) );
		int latestMinor = Integer.parseInt( latest.group( 2 ) );
		int latestPatch = Integer.parseInt( latest.group( 3 ) );

		return latestMajor > currentMajor
			|| latestMajor == currentMajor && latestMinor > currentMinor
			|| latestMajor == currentMajor && latestMinor == currentMinor && latestPatch > currentPatch;
	}

	private static void sendUpdateMessages( OnPlayerLoggedIn data ) {
		data.player.sendSystemMessage( TextHelper.translatable( "mlib.update_available" ) );
		COMPONENTS.forEach( data.player::sendSystemMessage );
	}

	private record TypeAdapter< Type >( Supplier< Type > instance ) implements JsonDeserializer< Type > {
		@Override
		public Type deserialize( JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context ) throws JsonParseException {
			return Serializables.read( this.instance.get(), json );
		}
	}
}
