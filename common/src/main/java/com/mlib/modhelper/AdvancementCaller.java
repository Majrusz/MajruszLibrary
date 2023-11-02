package com.mlib.modhelper;

import com.google.gson.JsonObject;
import com.mlib.mixin.IMixinCriteriaTriggers;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

class AdvancementCaller extends SimpleCriterionTrigger< AdvancementCaller.Instance > {
	public AdvancementCaller( ModHelper helper ) {
		IMixinCriteriaTriggers.register( helper.getLocation( "basic_trigger" ).toString(), this );
	}

	@Override
	public Instance createInstance( JsonObject json, Optional< ContextAwarePredicate > predicate, DeserializationContext context ) {
		return new Instance( predicate, json.get( "type" ).getAsString() );
	}

	public void trigger( ServerPlayer player, String achievementId ) {
		this.trigger( player, instance->instance.achievementId.equals( achievementId ) );
	}

	static class Instance extends AbstractCriterionTriggerInstance {
		final String achievementId;

		public Instance( Optional< ContextAwarePredicate > predicate, String achievementId ) {
			super( predicate );

			this.achievementId = achievementId;
		}

		@Override
		public JsonObject serializeToJson() {
			JsonObject jsonObject = super.serializeToJson();
			jsonObject.addProperty( "type", this.achievementId );

			return jsonObject;
		}
	}
}
