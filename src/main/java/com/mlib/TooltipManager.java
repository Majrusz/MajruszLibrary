package com.mlib;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@OnlyIn( Dist.CLIENT )
@Mod.EventBusSubscriber
public class TooltipManager {
	public static ObfuscationGetter.Method< GuiComponent > FILL_GRADIENT = new ObfuscationGetter.Method<>( GuiComponent.class,"m_93123_", Matrix4f.class, BufferBuilder.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class );

	@SubscribeEvent( priority = EventPriority.LOWEST )
	public static void onTooltipRender( RenderTooltipEvent.Pre preEvent ) {
		List<ClientTooltipComponent> p_169385_ = preEvent.getComponents();
		Screen screen = Minecraft.getInstance().screen;
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		PoseStack p_169384_ = preEvent.getPoseStack();
		preEvent.setCanceled( true );
		// everything below is copied from Screen::renderTooltipInternal and slightly modified

		int i = 0;
		int j = p_169385_.size() == 1 ? -2 : 0;

		for( ClientTooltipComponent clienttooltipcomponent : p_169385_) {
			int k = clienttooltipcomponent.getWidth(preEvent.getFont());
			if (k > i) {
				i = k;
			}

			j += clienttooltipcomponent.getHeight();
		}

		int j2 = preEvent.getX() + 12;
		int k2 = preEvent.getY() - 12 -100;
		if (j2 + i > preEvent.getScreenWidth()) {
			j2 -= 28 + i;
		}

		if (k2 + j + 6 > preEvent.getScreenHeight()) {
			k2 = preEvent.getScreenHeight() - j - 6;
		}

		p_169384_.pushPose();
		int l = -267386864;
		int i1 = 1347420415;
		int j1 = 1344798847;
		int k1 = 400;
		float f = itemRenderer.blitOffset;
		itemRenderer.blitOffset = 400.0F;
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		RenderSystem.setShader( GameRenderer::getPositionColorShader);
		bufferbuilder.begin( VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		Matrix4f matrix4f = p_169384_.last().pose();
		int backgroundStart = 0xf0100010, backgroundEnd = 0xf0100010, borderStart = 0x505000FF, borderEnd = 0x5028007f;
		FILL_GRADIENT.invoke( screen, matrix4f, bufferbuilder, j2 - 3, k2 - 4, j2 + i + 3, k2 - 3, 400, backgroundStart, backgroundStart);
		FILL_GRADIENT.invoke( screen, matrix4f, bufferbuilder, j2 - 3, k2 + j + 3, j2 + i + 3, k2 + j + 4, 400, backgroundEnd, backgroundEnd);
		FILL_GRADIENT.invoke( screen, matrix4f, bufferbuilder, j2 - 3, k2 - 3, j2 + i + 3, k2 + j + 3, 400, backgroundStart, backgroundEnd);
		FILL_GRADIENT.invoke( screen, matrix4f, bufferbuilder, j2 - 4, k2 - 3, j2 - 3, k2 + j + 3, 400, backgroundStart, backgroundEnd);
		FILL_GRADIENT.invoke( screen, matrix4f, bufferbuilder, j2 + i + 3, k2 - 3, j2 + i + 4, k2 + j + 3, 400, backgroundStart, backgroundEnd);
		FILL_GRADIENT.invoke( screen, matrix4f, bufferbuilder, j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + j + 3 - 1, 400, borderStart, borderEnd);
		FILL_GRADIENT.invoke( screen, matrix4f, bufferbuilder, j2 + i + 2, k2 - 3 + 1, j2 + i + 3, k2 + j + 3 - 1, 400, borderStart, borderEnd);
		FILL_GRADIENT.invoke( screen, matrix4f, bufferbuilder, j2 - 3, k2 - 3, j2 + i + 3, k2 - 3 + 1, 400, borderStart, borderStart);
		FILL_GRADIENT.invoke( screen, matrix4f, bufferbuilder, j2 - 3, k2 + j + 2, j2 + i + 3, k2 + j + 3, 400, borderEnd, borderEnd);
		RenderSystem.enableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		BufferUploader.drawWithShader(bufferbuilder.end());
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		p_169384_.translate(0.0D, 0.0D, 400.0D);
		int l1 = k2;

		for(int i2 = 0; i2 < p_169385_.size(); ++i2) {
			ClientTooltipComponent clienttooltipcomponent1 = p_169385_.get(i2);
			clienttooltipcomponent1.renderText(preEvent.getFont(), j2, l1, matrix4f, multibuffersource$buffersource);
			l1 += clienttooltipcomponent1.getHeight() + (i2 == 0 ? 2 : 0);
		}

		multibuffersource$buffersource.endBatch();
		p_169384_.popPose();
		l1 = k2;

		for(int l2 = 0; l2 < p_169385_.size(); ++l2) {
			ClientTooltipComponent clienttooltipcomponent2 = p_169385_.get(l2);
			clienttooltipcomponent2.renderImage(preEvent.getFont(), j2, l1, p_169384_, itemRenderer, 400);
			l1 += clienttooltipcomponent2.getHeight() + (l2 == 0 ? 2 : 0);
		}

		itemRenderer.blitOffset = f;
	}
}
