package com.mlib.modhelper;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import com.mlib.mixin.IMixinCriteriaTriggers;

class AdvancementCaller extends SimpleCriterionTrigger< AdvancementCaller.Instance > {
	final ResourceLocation id;

	public AdvancementCaller( ModHelper helper ) {
		this.id = helper.getLocation( "basic_trigger" );

		IMixinCriteriaTriggers.register( this );
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public Instance createInstance( JsonObject json, ContextAwarePredicate predicate, DeserializationContext context ) {
		return new Instance( this.id, predicate, json.get( "type" ).getAsString() );
	}

	public void trigger( ServerPlayer player, String achievementId ) {
		this.trigger( player, instance->instance.achievementId.equals( achievementId ) );
	}

	static class Instance extends AbstractCriterionTriggerInstance {
		final String achievementId;

		public Instance( ResourceLocation id, ContextAwarePredicate predicate, String achievementId ) {
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
