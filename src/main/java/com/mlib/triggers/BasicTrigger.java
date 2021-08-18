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
	private final String modID;

	public BasicTrigger( String modID ) {
		this.modID = modID;
	}

	/** Creates and registers instance of BasicTrigger with given mod id. */
	public static BasicTrigger createRegisteredInstance( String modID ) {
		return CriteriaTriggers.register( new BasicTrigger( modID ) );
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public BasicTrigger.Instance createInstance( JsonObject json, EntityPredicate.Composite predicate, DeserializationContext conditions ) {
		JsonElement modID = json.get( "mod_id" );
		JsonElement triggerType = json.get( "type" );

		return new BasicTrigger.Instance( predicate, modID.getAsString(), triggerType.getAsString() );
	}

	/** Triggers an advancement for given player. */
	public void trigger( ServerPlayer player, String triggerType ) {
		this.trigger( player, instance->instance.test( this.modID, triggerType ) );
	}

	protected static class Instance extends AbstractCriterionTriggerInstance {
		private final String modID, triggerType;

		public Instance( EntityPredicate.Composite predicate, String modID, String triggerType ) {
			super( BasicTrigger.ID, predicate );

			this.modID = modID;
			this.triggerType = triggerType;
		}

		@Override
		public JsonObject serializeToJson( SerializationContext conditions ) {
			JsonObject jsonObject = super.serializeToJson( conditions );
			jsonObject.addProperty( "mod_id", this.modID );
			jsonObject.addProperty( "type", this.triggerType );

			return jsonObject;
		}

		/** Checks whether conditions were met. */
		public boolean test( String modID, String triggerType ) {
			return this.modID.equals( modID ) && this.triggerType.equals( triggerType );
		}
	}
}
