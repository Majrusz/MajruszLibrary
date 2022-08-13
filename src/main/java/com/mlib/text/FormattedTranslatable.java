package com.mlib.text;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

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
		return this.addParameter( Component.literal( text ).withStyle( format ) );
	}

	public MutableComponent create() {
		return Component.translatable( this.id, this.parameters.toArray() ).withStyle( this.defaultFormat );
	}

	public void insertTo( List< Component > components, int index ) {
		components.add( index, this.create() );
	}
}
