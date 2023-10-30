package com.mlib.item;

import com.mlib.data.Serializable;
import com.mlib.data.SerializableHelper;
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
	public static int getLevel( Supplier< Enchantment > enchantment, ItemStack itemStack ) {
		ResourceLocation enchantmentId = Registries.get( enchantment.get() );
		for( EnchantmentDef enchantmentDef : EnchantmentHelper.read( itemStack ).enchantments ) {
			if( enchantmentId.equals( enchantmentDef.id ) ) {
				return enchantmentDef.level;
			}
		}

		return 0;
	}

	public static int getLevel( Supplier< Enchantment > enchantment, LivingEntity entity ) {
		return net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentLevel( enchantment.get(), entity );
	}

	public int getLevelSum( Supplier< Enchantment > enchantment, Iterable< ItemStack > itemStacks ) {
		int sum = 0;
		for( ItemStack itemStack : itemStacks ) {
			sum += EnchantmentHelper.getLevel( enchantment, itemStack );
		}

		return sum;
	}

	public int getLevelSum( Supplier< Enchantment > enchantment, LivingEntity entity, EquipmentSlot[] slots ) {
		int sum = 0;
		for( EquipmentSlot slot : slots ) {
			sum += EnchantmentHelper.getLevel( enchantment, entity.getItemBySlot( slot ) );
		}

		return sum;
	}

	public static boolean has( Supplier< Enchantment > enchantment, ItemStack itemStack ) {
		return EnchantmentHelper.getLevel( enchantment, itemStack ) > 0;
	}

	public static boolean has( Supplier< Enchantment > enchantment, LivingEntity entity ) {
		return EnchantmentHelper.getLevel( enchantment, entity ) > 0;
	}

	public static boolean increaseLevel( Supplier< Enchantment > enchantment, ItemStack itemStack ) {
		ResourceLocation id = Registries.get( enchantment.get() );
		EnchantmentsDef enchantmentsDef = EnchantmentHelper.read( itemStack );
		for( EnchantmentDef enchantmentDef : enchantmentsDef.enchantments ) {
			if( id.equals( enchantmentDef.id ) ) {
				if( enchantmentDef.level >= enchantment.get().getMaxLevel() ) {
					return false;
				} else {
					enchantmentDef.level++;
					SerializableHelper.write( ()->enchantmentsDef, itemStack.getOrCreateTag() );
					return true;
				}
			}
		}

		enchantmentsDef.enchantments.add( new EnchantmentDef( id, 1 ) );
		SerializableHelper.write( ()->enchantmentsDef, itemStack.getOrCreateTag() );
		return true;
	}

	public static boolean remove( Supplier< Enchantment > enchantment, ItemStack itemStack ) {
		ResourceLocation id = Registries.get( enchantment.get() );
		EnchantmentsDef enchantmentsDef = EnchantmentHelper.read( itemStack );
		for( int idx = 0; idx < enchantmentsDef.enchantments.size(); ++idx ) {
			if( id.equals( enchantmentsDef.enchantments.get( idx ).id ) ) {
				enchantmentsDef.enchantments.remove( idx );
				SerializableHelper.write( ()->enchantmentsDef, itemStack.getTag() );
				return true;
			}
		}

		return false;
	}

	public static EnchantmentsDef read( ItemStack itemStack ) {
		return SerializableHelper.read( EnchantmentsDef::new, Optional.ofNullable( itemStack.getTag() ).orElse( new CompoundTag() ) );
	}

	public static class EnchantmentsDef extends Serializable {
		public List< EnchantmentDef > enchantments = new ArrayList<>();

		public EnchantmentsDef() {
			this.defineCustom( "Enchantments", ()->this.enchantments, x->this.enchantments = x, EnchantmentDef::new );
		}
	}

	public static class EnchantmentDef extends Serializable {
		public ResourceLocation id;
		public int level;

		public EnchantmentDef() {
			this.defineLocation( "id", ()->this.id, x->this.id = x );
			this.defineInteger( "lvl", ()->this.level, x->this.level = x );
		}

		public EnchantmentDef( ResourceLocation id, int level ) {
			this();

			this.id = id;
			this.level = level;
		}
	}
}
