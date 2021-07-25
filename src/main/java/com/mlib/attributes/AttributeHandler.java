package com.mlib.attributes;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

/** Handling attributes in easier and shorter way. */
public class AttributeHandler {
	protected final UUID uuid;
	protected final String name;
	protected final Attribute attribute;
	protected final AttributeModifier.Operation operation;
	private double value = 1.0;

	public AttributeHandler( String uuid, String name, Attribute attribute, AttributeModifier.Operation operation ) {
		this.uuid = UUID.fromString( uuid );
		this.name = name;
		this.attribute = attribute;
		this.operation = operation;
	}

	/** Checks whether entity has a given attribute. */
	public static boolean hasAttribute( LivingEntity entity, Attribute attribute ) {
		AttributeMap attributeMap = entity.getAttributes();

		return attributeMap.hasAttribute( attribute );
	}

	/** Returns current attribute value. */
	public double getValue() {
		return this.value;
	}

	/** Setting current attribute value. */
	public AttributeHandler setValue( double value ) {
		this.value = value;

		return this;
	}

	/**
	 Applying current attribute to the target.

	 @param target Entity to apply attribute.
	 */
	public AttributeHandler apply( LivingEntity target ) {
		AttributeInstance attributeInstance = target.getAttribute( this.attribute );

		if( attributeInstance != null ) {
			attributeInstance.removeModifier( this.uuid );
			AttributeModifier modifier = new AttributeModifier( this.uuid, this.name, this.value, this.operation );
			attributeInstance.addPermanentModifier( modifier );
		}

		return this;
	}

	/**
	 Changing current attribute value and applying it to the entity.

	 @param target Entity to apply attribute.
	 @param value  New attribute value.
	 */
	public AttributeHandler setValueAndApply( LivingEntity target, double value ) {
		setValue( value );

		return apply( target );
	}
}
