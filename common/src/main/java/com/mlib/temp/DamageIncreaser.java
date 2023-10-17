package com.mlib.temp;

import com.mlib.MajruszLibrary;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnEntityPreDamaged;
import com.mlib.contexts.OnItemAttributeTooltip;
import com.mlib.contexts.OnItemTooltip;
import com.mlib.contexts.base.Condition;
import com.mlib.data.SerializableList;
import com.mlib.data.SerializableStructure;
import com.mlib.registry.Registries;
import com.mlib.text.TextHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoInstance
public class DamageIncreaser {
	private Bonuses bonuses = new Bonuses();
	private Test test = new Test();

	public DamageIncreaser() {
		OnEntityPreDamaged.listen( this::increaseDamage )
			.addCondition( Condition.predicate( OnEntityPreDamaged::isDirect ) )
			.addCondition( Condition.predicate( data->data.attacker != null ) )
			.addCondition( Condition.predicate( data->this.get( data.attacker.getMainHandItem() ) != null ) );

		OnItemAttributeTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->this.get( data.itemStack ) != null ) );

		OnItemTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->this.get( data.itemStack ) != null ) )
			.addCondition( Condition.predicate( OnItemTooltip::isAdvanced ) );

		MajruszLibrary.CONFIG.defineCustom( "damage_bonuses", ()->this.bonuses, x->this.bonuses = x, Bonuses::new );
		MajruszLibrary.CONFIG.defineCustom( "test", ()->this.test, x->this.test = x, Test::new );
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

	private @Nullable DamageIncreaser.Bonus get( ItemStack itemStack ) {
		for( Bonus bonus : this.bonuses.bonuses ) {
			if( bonus.id.equals( Registries.get( itemStack.getItem() ) ) ) {
				return bonus;
			}
		}

		return null;
	}

	public static class Test extends SerializableStructure {
		int value = -1;

		public Test() {
			this.defineInteger( "num", ()->this.value, x->this.value = x );
		}
	}

	private static class Bonuses extends SerializableList {
		List< Bonus > bonuses = List.of(
			new Bonus( "minecraft:apple", 4 ),
			new Bonus( "minecraft:dried_kelp", 69 )
		);

		public Bonuses() {
			this.defineCustom( ()->this.bonuses, x->this.bonuses = x, Bonus::new );
		}
	}

	private static class Bonus extends SerializableStructure {
		ResourceLocation id;
		int value;

		public Bonus() {
			this.defineLocation( "id", ()->this.id, x->this.id = x );
			this.defineInteger( "damage_bonus", ()->this.value, x->this.value = x );
		}

		public Bonus( String id, int value ) {
			this();

			this.id = new ResourceLocation( id );
			this.value = value;
		}
	}
}
