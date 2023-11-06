package com.mlib.item;

import com.mlib.data.Serializables;
import com.mlib.registry.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class EnchantmentHelper {
	public static int getLevel( Supplier< ? extends Enchantment > enchantment, ItemStack itemStack ) {
		ResourceLocation enchantmentId = Registries.get( enchantment.get() );
		for( EnchantmentDef enchantmentDef : EnchantmentHelper.read( itemStack ).enchantments ) {
			if( enchantmentId.equals( enchantmentDef.id ) ) {
				return enchantmentDef.level;
			}
		}

		return 0;
	}

	public static int getLevel( Supplier< ? extends Enchantment > enchantment, LivingEntity entity ) {
		return net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentLevel( enchantment.get(), entity );
	}

	public static int getLevelSum( Supplier< ? extends Enchantment > enchantment, Iterable< ItemStack > itemStacks ) {
		int sum = 0;
		for( ItemStack itemStack : itemStacks ) {
			sum += EnchantmentHelper.getLevel( enchantment, itemStack );
		}

		return sum;
	}

	public static int getLevelSum( Supplier< ? extends Enchantment > enchantment, LivingEntity entity ) {
		int sum = 0;
		for( ItemStack itemStack : enchantment.get().getSlotItems( entity ).values() ) {
			sum += EnchantmentHelper.getLevel( enchantment, itemStack );
		}

		return sum;
	}

	public static int getLevelSum( Supplier< ? extends Enchantment > enchantment, LivingEntity entity, Iterable< EquipmentSlot > slots ) {
		int sum = 0;
		for( EquipmentSlot slot : slots ) {
			sum += EnchantmentHelper.getLevel( enchantment, entity.getItemBySlot( slot ) );
		}

		return sum;
	}

	public static boolean has( Supplier< ? extends Enchantment > enchantment, ItemStack itemStack ) {
		return EnchantmentHelper.getLevel( enchantment, itemStack ) > 0;
	}

	public static boolean has( Supplier< ? extends Enchantment > enchantment, LivingEntity entity ) {
		return EnchantmentHelper.getLevel( enchantment, entity ) > 0;
	}

	public static boolean increaseLevel( Supplier< ? extends Enchantment > enchantment, ItemStack itemStack ) {
		ResourceLocation id = Registries.get( enchantment.get() );
		EnchantmentsDef enchantmentsDef = EnchantmentHelper.read( itemStack );
		for( EnchantmentDef enchantmentDef : enchantmentsDef.enchantments ) {
			if( id.equals( enchantmentDef.id ) ) {
				if( enchantmentDef.level >= enchantment.get().getMaxLevel() ) {
					return false;
				} else {
					enchantmentDef.level++;
					Serializables.write( enchantmentsDef, itemStack.getOrCreateTag() );
					return true;
				}
			}
		}

		enchantmentsDef.enchantments.add( new EnchantmentDef( id, 1 ) );
		Serializables.write( enchantmentsDef, itemStack.getOrCreateTag() );
		return true;
	}

	public static boolean remove( Supplier< ? extends Enchantment > enchantment, ItemStack itemStack ) {
		ResourceLocation id = Registries.get( enchantment.get() );
		EnchantmentsDef enchantmentsDef = EnchantmentHelper.read( itemStack );
		for( int idx = 0; idx < enchantmentsDef.enchantments.size(); ++idx ) {
			if( id.equals( enchantmentsDef.enchantments.get( idx ).id ) ) {
				enchantmentsDef.enchantments.remove( idx );
				Serializables.write( enchantmentsDef, itemStack.getTag() );
				return true;
			}
		}

		return false;
	}

	public static EnchantmentsDef read( ItemStack itemStack ) {
		return Serializables.read( new EnchantmentsDef(), Optional.ofNullable( itemStack.getTag() ).orElse( new CompoundTag() ) );
	}

	public static class EnchantmentsDef {
		static {
			Serializables.get( EnchantmentsDef.class )
				.defineCustomList( "Enchantments", s->s.enchantments, ( s, v )->s.enchantments = v, EnchantmentDef::new );
		}

		public List< EnchantmentDef > enchantments = new ArrayList<>();
	}

	public static class EnchantmentDef {
		static {
			Serializables.get( EnchantmentDef.class )
				.defineLocation( "id", s->s.id, ( s, v )->s.id = v )
				.defineInteger( "lvl", s->s.level, ( s, v )->s.level = v );
		}

		public ResourceLocation id;
		public int level;

		public EnchantmentDef( ResourceLocation id, int level ) {
			this.id = id;
			this.level = level;
		}

		public EnchantmentDef() {}
	}
}
