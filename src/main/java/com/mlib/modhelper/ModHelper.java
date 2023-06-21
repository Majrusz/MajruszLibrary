package com.mlib.modhelper;

import com.mlib.config.ConfigHandler;
import com.mlib.data.SerializableStructure;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ModHelper {
	private final List< Runnable > registerCallbacks = new ArrayList<>();
	private final String modId;
	private final BasicTrigger trigger;
	private final ClassLoader classLoader;
	private final NetworkHandler networkHandler;
	private ModLoadingContext modLoadingContext = null;
	private IEventBus eventBus = null;

	public static ModHelper create( String modId ) {
		return new ModHelper( modId );
	}

	public void triggerAchievement( ServerPlayer player, String id ) {
		this.trigger.trigger( player, id );
	}

	public < Type > Optional< Type > findInstance( Class< Type > clazz ) {
		return this.classLoader.getInstance( clazz );
	}

	public < Type > void sendMessage( PacketDistributor.PacketTarget target, Type message ) {
		this.networkHandler.send( target, message );
	}

	public < Type > DeferredRegister< Type > create( ResourceKey< Registry< Type > > key ) {
		DeferredRegister< Type > register = DeferredRegister.create( key, this.getModId() );
		this.registerCallbacks.add( ()->register.register( this.getEventBus() ) );

		return register;
	}

	public ConfigHandler createConfig( ModConfig.Type type ) {
		ConfigHandler configHandler = new ConfigHandler( type );
		this.registerCallbacks.add( ()->configHandler.register( this ) );

		return configHandler;
	}

	public < Type extends SerializableStructure > void createMessage( Class< Type > clazz, Supplier< Type > supplier ) {
		this.networkHandler.add( clazz, supplier );
	}

	public void register() {
		this.modLoadingContext = ModLoadingContext.get();
		this.eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		this.registerCallbacks.forEach( Runnable::run );
	}

	public String getModId() {
		return this.modId;
	}

	public ModLoadingContext getModLoadingContext() {
		return this.modLoadingContext;
	}

	public IEventBus getEventBus() {
		return this.eventBus;
	}

	public ResourceLocation getLocation( String register ) {
		return new ResourceLocation( this.getModId(), register );
	}

	public String getLocationString( String register ) {
		return this.getLocation( register ).toString();
	}

	public ModelLayerLocation getModelLayer( String register, String layer ) {
		return new ModelLayerLocation( this.getLocation( register ), layer );
	}

	public ModelLayerLocation getModelLayer( String register ) {
		return new ModelLayerLocation( this.getLocation( register ), "main" );
	}

	public RenderType getEyesRenderType( String register ) {
		return RenderType.eyes( this.getLocation( register ) );
	}

	public SimpleChannel getNetworkChannel() {
		return this.networkHandler.channel;
	}

	void onRegister( Runnable callback ) {
		this.registerCallbacks.add( callback );
	}

	private ModHelper( String modId ) {
		this.modId = modId;
		this.trigger = new BasicTrigger( this );
		this.classLoader = new ClassLoader( this );
		this.networkHandler = new NetworkHandler( this );
	}
}
