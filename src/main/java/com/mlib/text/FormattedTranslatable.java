package com.mlib.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

public class FormattedTranslatable {
	final String id;
	final ChatFormatting[] defaultFormat;
	final List< MutableComponent > parameters = new ArrayList<>();

	public FormattedTranslatable( String id, ChatFormatting... defaultFormat ) {
		this.id = id;
		this.defaultFormat = defaultFormat;
	}

	public FormattedTranslatable addParameter( MutableComponent component ) {
		this.parameters.add( component );

		return this;
	}

	public FormattedTranslatable addParameter( String text, ChatFormatting... format ) {
		return this.addParameter( new TextComponent( text ).withStyle( format ) );
	}

	public MutableComponent create() {
		return new TranslatableComponent( this.id, this.parameters.toArray() ).withStyle( this.defaultFormat );
	}

	public void insertInto( List< Component > components, int index ) {
		components.add( index, this.create() );
	}

	public void insertInto( List< Component > components ) {
		components.add( this.create() );
	}
}
