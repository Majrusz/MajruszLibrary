package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.events.OnGuiOverlaysRegistered;
import com.majruszlibrary.events.base.Events;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin( Gui.class )
public abstract class MixinGui {
	private final List< OnGuiOverlaysRegistered.Renderer > majruszlibrary$renderers = new ArrayList<>();

	@Inject(
		at = @At( "RETURN" ),
		method = "<init> (Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/renderer/entity/ItemRenderer;)V"
	)
	private void constructor( Minecraft minecraft, ItemRenderer itemRenderer, CallbackInfo callback ) {
		this.majruszlibrary$renderers.addAll( Events.dispatch( new OnGuiOverlaysRegistered() ).getRenderers().stream().map( Pair::getSecond ).toList() );
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "render (Lnet/minecraft/client/gui/GuiGraphics;F)V"
	)
	private void render( GuiGraphics graphics, float partialTicks, CallbackInfo callback ) {
		this.majruszlibrary$renderers.forEach( renderer->renderer.render( graphics, partialTicks, graphics.guiWidth(), graphics.guiHeight() ) );

		RenderSystem.setShaderColor( 1.0f, 1.0f, 1.0f, 1.0f );
	}
}
