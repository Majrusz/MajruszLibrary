package com.mlib.triggers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mlib.registries.RegistryHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Trigger that is called only with custom id. */
public class BasicTrigger extends SimpleCriterionTrigger< BasicTrigger.Instance > {
	private final ResourceLocation triggerID;

	public BasicTrigger( RegistryHelper helper ) {
		this.triggerID = helper.getLocation( "basic_trigger" );
	}

	@Override
	public ResourceLocation getId() {
		return this.triggerID;
	}

	@Override
	public BasicTrigger.Instance createInstance( JsonObject json, EntityPredicate.Composite predicate, DeserializationContext conditions ) {
		JsonElement triggerType = json.get( "type" );

		return new BasicTrigger.Instance( this.triggerID, predicate, triggerType.getAsString() );
	}

	public static BasicTrigger createRegisteredInstance( RegistryHelper helper ) {
		return CriteriaTriggers.register( new BasicTrigger( helper ) );
	}

	public void trigger( ServerPlayer player, String triggerType ) {
		this.trigger( player, instance->instance.test( triggerType ) );
	}

	protected static class Instance extends AbstractCriterionTriggerInstance {
		private final String triggerType;

		public Instance( ResourceLocation triggerID, EntityPredicate.Composite predicate, String triggerType ) {
			super( triggerID, predicate );

			this.triggerType = triggerType;
		}

		@Override
		public JsonObject serializeToJson( SerializationContext conditions ) {
			JsonObject jsonObject = super.serializeToJson( conditions );
			jsonObject.addProperty( "type", this.triggerType );

			return jsonObject;
		}

		public boolean test( String triggerType ) {
			return this.triggerType.equals( triggerType );
		}
	}
}
