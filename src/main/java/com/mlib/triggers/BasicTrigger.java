package com.mlib.triggers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mlib.MajruszLibrary;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Trigger that is called without any parameters, only with its id. */
public class BasicTrigger extends SimpleCriterionTrigger< BasicTrigger.Instance > {
	private static final ResourceLocation ID = MajruszLibrary.getLocation( "basic_trigger" );
	public static final BasicTrigger INSTANCE = CriteriaTriggers.register( new BasicTrigger() );

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public BasicTrigger.Instance createInstance( JsonObject json, EntityPredicate.Composite predicate, DeserializationContext conditions ) {
		JsonElement triggerType = json.get( "type" );

		return new BasicTrigger.Instance( predicate, triggerType.getAsString() );
	}

	/** Triggers an advancement for given player. */
	public void trigger( ServerPlayer player, String triggerType ) {
		this.trigger( player, instance->instance.test( triggerType ) );
	}

	protected static class Instance extends AbstractCriterionTriggerInstance {
		private final String triggerType;

		public Instance( EntityPredicate.Composite predicate, String triggerType ) {
			super( BasicTrigger.ID, predicate );

			this.triggerType = triggerType;
		}

		@Override
		public JsonObject serializeToJson( SerializationContext conditions ) {
			JsonObject jsonObject = super.serializeToJson( conditions );
			jsonObject.addProperty( "type", this.triggerType );

			return jsonObject;
		}

		public boolean test( String effectType ) {
			return this.triggerType.equals( effectType );
		}
	}
}
