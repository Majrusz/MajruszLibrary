package com.mlib.modhelper;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

class BasicTrigger extends SimpleCriterionTrigger< BasicTrigger.Instance > {
	final ResourceLocation id;

	public BasicTrigger( ModHelper helper ) {
		this.id = helper.getLocation( "basic_trigger" );

		CriteriaTriggers.register( this );
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public BasicTrigger.Instance createInstance( JsonObject json, EntityPredicate.Composite predicate, DeserializationContext context ) {
		return new BasicTrigger.Instance( this.id, predicate, json.get( "type" ).getAsString() );
	}

	public void trigger( ServerPlayer player, String achievementId ) {
		this.trigger( player, instance->instance.achievementId.equals( achievementId ) );
	}

	static class Instance extends AbstractCriterionTriggerInstance {
		final String achievementId;

		public Instance( ResourceLocation id, EntityPredicate.Composite predicate, String achievementId ) {
			super( id, predicate );

			this.achievementId = achievementId;
		}

		@Override
		public JsonObject serializeToJson( SerializationContext conditions ) {
			JsonObject jsonObject = super.serializeToJson( conditions );
			jsonObject.addProperty( "type", this.achievementId );

			return jsonObject;
		}
	}
}
