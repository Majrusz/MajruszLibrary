package com.mlib.attributes;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class AttributeHandler {
	final UUID uuid;
	final String name;
	final Attribute attribute;
	final AttributeModifier.Operation operation;
	double value = 0.0;

	public AttributeHandler( String uuid, String name, Attribute attribute, AttributeModifier.Operation operation ) {
		this.uuid = UUID.fromString( uuid );
		this.name = name;
		this.attribute = attribute;
		this.operation = operation;
	}

	public AttributeHandler( String name, Attribute attribute, AttributeModifier.Operation operation ) {
		this.uuid = UUID.nameUUIDFromBytes( name.getBytes() );
		this.name = name;
		this.attribute = attribute;
		this.operation = operation;
	}

	public static boolean hasAttribute( LivingEntity entity, Attribute attribute ) {
		return entity.getAttributes().hasAttribute( attribute );
	}

	public boolean hasValueChanged( AttributeInstance attributeInstance ) {
		AttributeModifier modifier = attributeInstance.getModifier( this.uuid );

		return modifier == null || modifier.getAmount() != this.value;
	}

	public double getValue() {
		return this.value;
	}

	public AttributeHandler setValue( double value ) {
		this.value = value;

		return this;
	}

	public AttributeHandler apply( LivingEntity target ) {
		AttributeInstance attributeInstance = target.getAttribute( this.attribute );
		if( attributeInstance != null && this.hasValueChanged( attributeInstance ) ) {
			attributeInstance.removeModifier( this.uuid );
			attributeInstance.addPermanentModifier( this.createAttribute() );
		}

		return this;
	}

	public AttributeModifier createAttribute() {
		return new AttributeModifier( this.uuid, this.name, this.value, this.operation );
	}

	public UUID getUUID() {
		return this.uuid;
	}
}
