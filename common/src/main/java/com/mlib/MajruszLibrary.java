package com.mlib;

import com.mlib.command.EnumArgument;
import com.mlib.data.Config;
import com.mlib.entity.EntityHelper;
import com.mlib.modhelper.ModHelper;
import com.mlib.network.NetworkObject;
import com.mlib.registry.RegistryGroup;
import com.mlib.registry.RegistryObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;

public class MajruszLibrary {
	public static final String MOD_ID = "mlib";
	public static final ModHelper HELPER = ModHelper.create( MOD_ID );

	// Configs
	public static final Config CONFIG = HELPER.config( name->new Config( name ) {} ).named( "majruszlibrary" ).autoSync().create();

	// Registry Groups
	public static final RegistryGroup< ArgumentTypeInfo< ?, ? > > ARGUMENT_TYPES = HELPER.create( BuiltInRegistries.COMMAND_ARGUMENT_TYPE );

	// Arguments
	public static final RegistryObject< ArgumentTypeInfo< ?, ? > > ENUM_ARGUMENT_TYPE = ARGUMENT_TYPES.create( "enum", EnumArgument::register );

	// Network
	public static final NetworkObject< EntityHelper.EntityAnimation > ENTITY_ANIMATION = HELPER.create( "entity_animation", EntityHelper.EntityAnimation.class );
	public static final NetworkObject< EntityHelper.EntityGlow > ENTITY_GLOW = HELPER.create( "entity_glow", EntityHelper.EntityGlow.class );
	public static final NetworkObject< EntityHelper.EntityInvisible > ENTITY_INVISIBLE = HELPER.create( "entity_invisible", EntityHelper.EntityInvisible.class );

	private MajruszLibrary() {}
}
