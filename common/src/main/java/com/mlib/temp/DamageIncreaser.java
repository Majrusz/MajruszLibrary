package com.mlib.temp;

import com.mlib.MajruszLibrary;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnEntityPreDamaged;
import com.mlib.contexts.OnItemAttributeTooltip;
import com.mlib.contexts.OnItemTooltip;
import com.mlib.contexts.base.Condition;
import com.mlib.data.JsonListener;
import com.mlib.data.SerializableStructure;
import com.mlib.registry.Registries;
import com.mlib.text.TextHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@AutoInstance
public class DamageIncreaser {
	final JsonListener.Holder< Config > config;

	public DamageIncreaser() {
		this.config = JsonListener.add( "config", MajruszLibrary.HELPER.getLocation( "config" ), Config.class, Config::new );

		OnEntityPreDamaged.listen( this::increaseDamage )
			.addCondition( Condition.predicate( OnEntityPreDamaged::isDirect ) )
			.addCondition( Condition.predicate( data->data.attacker != null ) )
			.addCondition( Condition.predicate( data->this.get( data.attacker.getMainHandItem() ) != null ) );

		OnItemAttributeTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->this.get( data.itemStack ) != null ) );

		OnItemTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->this.get( data.itemStack ) != null ) )
			.addCondition( Condition.predicate( OnItemTooltip::isAdvanced ) );
	}

	private void increaseDamage( OnEntityPreDamaged data ) {
		data.extraDamage += this.get( data.attacker.getMainHandItem() ).value;
		data.spawnMagicParticles = true;
	}

	private void addTooltip( OnItemAttributeTooltip data ) {
		data.add( EquipmentSlot.MAINHAND, TextHelper.literal( "SUPER DUPER +%d ATTACK DAMAGE".formatted( this.get( data.itemStack ).value ) ) );
	}

	private void addTooltip( OnItemTooltip data ) {
		data.components.add( TextHelper.literal( "THE ULTIMATE WEAPON" ) );
	}

	private @Nullable DamageBonus get( ItemStack itemStack ) {
		for( DamageBonus damageBonus : this.config.get().damageBonuses ) {
			if( damageBonus.id.equals( Registries.get( itemStack.getItem() ) ) ) {
				return damageBonus;
			}
		}

		return null;
	}

	private static class Config extends SerializableStructure {
		List< DamageBonus > damageBonuses = new ArrayList<>();

		public Config() {
			this.defineCustom( "damage_bonuses", ()->this.damageBonuses, x->this.damageBonuses = x, DamageBonus::new );
		}
	}

	private static class DamageBonus extends SerializableStructure {
		ResourceLocation id;
		int value;

		public DamageBonus() {
			this.defineLocation( "id", ()->this.id, x->this.id = x );
			this.defineInteger( "damage_bonus", ()->this.value, x->this.value = x );
		}
	}
}
