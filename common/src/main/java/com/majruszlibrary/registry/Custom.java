package com.majruszlibrary.registry;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Function;
import java.util.function.Supplier;

public class Custom {
	public interface Advancements {
		< Type extends CriterionTrigger< ? > > void register( Type trigger );
	}

	public interface Attributes {
		< Type extends LivingEntity > void register( EntityType< Type > type, AttributeSupplier attributes );
	}

	@OnlyIn( Dist.CLIENT )
	public interface ItemProperties {
		void register( Item item, ResourceLocation id, ClampedItemPropertyFunction property );
	}

	@OnlyIn( Dist.CLIENT )
	public interface ModelLayers {
		void register( ModelLayerLocation id, Supplier< LayerDefinition > definition );
	}

	@OnlyIn( Dist.CLIENT )
	public interface Particles {
		< Type extends ParticleOptions > void register( ParticleType< Type > type, Function< SpriteSet, ParticleProvider< Type > > factory );
	}

	@OnlyIn( Dist.CLIENT )
	public interface Renderers {
		< Type extends Entity > void register( EntityType< Type > type, EntityRendererProvider< Type > factory );
	}

	public interface SpawnPlacements {
		< Type extends Mob > void register( EntityType< Type > entityType, net.minecraft.world.entity.SpawnPlacements.Type type, Heightmap.Types heightmap,
			net.minecraft.world.entity.SpawnPlacements.SpawnPredicate< Type > predicate
		);
	}
}
