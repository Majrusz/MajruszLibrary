package com.mlib.registries;

import com.mlib.triggers.BasicTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

@Deprecated( since = "4.4.0", forRemoval = true )
public class RegistryHelper {
	final String modId;
	final List< DeferredRegister< ? > > registers = new ArrayList<>();

	public RegistryHelper( String modId ) {
		this.modId = modId;
	}

	public < Type > DeferredRegister< Type > create( ResourceKey< Registry< Type > > key ) {
		DeferredRegister< Type > register = DeferredRegister.create( key, this.modId );
		this.registers.add( register );

		return register;
	}

	public void registerAll() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		this.registers.forEach( register->register.register( modEventBus ) );
	}

	public BasicTrigger registerBasicTrigger() {
		return CriteriaTriggers.register( new BasicTrigger( this.getLocation( "basic_trigger" ) ) );
	}

	public ResourceLocation getLocation( String register ) {
		return new ResourceLocation( this.modId, register );
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
}
