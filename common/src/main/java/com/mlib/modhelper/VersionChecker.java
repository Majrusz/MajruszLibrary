package com.mlib.modhelper;

import com.google.gson.*;
import com.mlib.MajruszLibrary;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnPlayerLoggedIn;
import com.mlib.data.ISerializable;
import com.mlib.data.Serializable;
import com.mlib.data.SerializableHelper;
import com.mlib.platform.Integration;
import com.mlib.platform.Side;
import com.mlib.text.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.storage.loot.Deserializers;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class VersionChecker {
	private static final Pattern VERSION = Pattern.compile( "(\\d+)\\.(\\d+)\\.(\\d+)" );
	final ModHelper helper;
	final Gson gson;
	final Data data = new Data();

	public VersionChecker( ModHelper helper ) {
		this.helper = helper;
		this.gson = Deserializers.createFunctionSerializer()
			.registerTypeAdapter( Data.class, new TypeAdapter<>( ()->this.data ) )
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
			Reader reader = new InputStreamReader( update.get().openStream() );
			this.gson.fromJson( reader, Data.class );
		} catch( Exception exception ) {
			this.helper.logError( "Failed to retrieve the latest mod versions from %s".formatted( update.get() ) );
			return;
		}

		String id = "%s-recommended".formatted( Integration.getMinecraftVersion() );
		if( !this.data.versions.containsKey( id ) ) {
			return;
		}

		this.data.currentVersion = matcher.group();
		this.data.latestVersion = this.data.versions.get( id );
		if( !this.isUpdateAvailable() ) {
			return;
		}

		MutableComponent name = TextHelper.literal( " [%s %s]", Integration.getName( this.helper.getModId() ), this.data.latestVersion )
			.withStyle( ChatFormatting.YELLOW )
			.withStyle( TextHelper.createURL( this.data.homepage ) );

		MutableComponent versions = TextHelper.translatable( "mlib.update_versions", name, this.data.currentVersion )
			.withStyle( ChatFormatting.GRAY );

		Sender.COMPONENTS.add( versions );
	}

	private boolean isUpdateAvailable() {
		Matcher current = VERSION.matcher( this.data.currentVersion );
		Matcher latest = VERSION.matcher( this.data.latestVersion );
		if( !current.find() || !latest.find() ) {
			this.helper.logError( "Not supported version format (current: %s, latest: %s)".formatted( this.data.currentVersion, this.data.latestVersion ) );
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

	@AutoInstance
	public static class Sender {
		static final List< Component > COMPONENTS = Collections.synchronizedList( new ArrayList<>() );
		private boolean isEnabled = true;

		public Sender() {
			OnPlayerLoggedIn.listen( this::sendUpdateMessages )
				.addCondition( data->this.isEnabled )
				.addCondition( data->COMPONENTS.size() > 0 )
				.addCondition( data->Side.isLogicalServer() && !Side.isDedicatedServer() || data.player.hasPermissions( 4 ) );

			MajruszLibrary.CONFIG.defineBoolean( "send_new_available_updates_message", ()->this.isEnabled, x->this.isEnabled = x );
		}

		private void sendUpdateMessages( OnPlayerLoggedIn data ) {
			data.player.sendSystemMessage( TextHelper.translatable( "mlib.update_available" ) );
			COMPONENTS.forEach( data.player::sendSystemMessage );
		}
	}

	private static class Data extends Serializable {
		String currentVersion;
		String latestVersion;
		String homepage;
		Map< String, String > versions = new HashMap<>();

		public Data() {
			this.defineString( "homepage", ()->this.homepage, x->this.homepage = x );
			this.defineString( "promos", ()->this.versions, x->this.versions = x );
		}
	}

	private static class TypeAdapter< Type extends ISerializable > implements JsonDeserializer< Type > {
		final Supplier< Type > instance;

		public TypeAdapter( Supplier< Type > instance ) {
			this.instance = instance;
		}

		@Override
		public Type deserialize( JsonElement element, java.lang.reflect.Type type, JsonDeserializationContext context ) throws JsonParseException {
			return SerializableHelper.read( this.instance, element );
		}
	}
}
