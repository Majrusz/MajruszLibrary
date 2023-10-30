package com.mlib.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( Enchantment.class )
public interface IMixinEnchantment {
	@Mutable
	@Accessor( "slots" )
	void setSlots( EquipmentSlot[] slots );

	@Mutable
	@Accessor( "rarity" )
	void setRarity( Enchantment.Rarity rarity );

	@Mutable
	@Accessor( "category" )
	void setCategory( EnchantmentCategory category );
}
