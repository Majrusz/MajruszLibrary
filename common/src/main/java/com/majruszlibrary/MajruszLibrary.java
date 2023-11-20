package com.majruszlibrary;

import com.majruszlibrary.command.EnumArgument;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.entity.EntityHelper;
import com.majruszlibrary.modhelper.ModHelper;
import com.majruszlibrary.network.NetworkObject;
import com.majruszlibrary.registry.RegistryGroup;
import com.majruszlibrary.registry.RegistryObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;

public class MajruszLibrary {
	public static final String MOD_ID = "majruszlibrary";
	public static final ModHelper HELPER = ModHelper.create( MOD_ID );

	// Configs
	static {
		HELPER.config( Config.class ).autoSync().create();
	}

	// Registry Groups
	public static final RegistryGroup< ArgumentTypeInfo< ?, ? > > ARGUMENT_TYPES = HELPER.create( BuiltInRegistries.COMMAND_ARGUMENT_TYPE );

	// Arguments
	public static final RegistryObject< ArgumentTypeInfo< ?, ? > > ENUM_ARGUMENT_TYPE = ARGUMENT_TYPES.create( "enum", EnumArgument::register );

	// Network
	public static final NetworkObject< EntityHelper.EntityAnimation > ENTITY_ANIMATION = HELPER.create( "entity_animation", EntityHelper.EntityAnimation.class );
	public static final NetworkObject< EntityHelper.EntityGlow > ENTITY_GLOW = HELPER.create( "entity_glow", EntityHelper.EntityGlow.class );
	public static final NetworkObject< EntityHelper.EntityInvisible > ENTITY_INVISIBLE = HELPER.create( "entity_invisible", EntityHelper.EntityInvisible.class );

	private MajruszLibrary() {}

	public static class Config {
		static {
			Serializables.getStatic( Config.class );
		}
	}
}
