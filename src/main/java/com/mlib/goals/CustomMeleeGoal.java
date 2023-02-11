package com.mlib.goals;

import com.mlib.entities.CustomSkills;
import com.mlib.entities.ICustomSkillProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class CustomMeleeGoal< MobType extends PathfinderMob & ICustomSkillProvider< ? > > extends MeleeAttackGoal {
	final MobType mob;

	public CustomMeleeGoal( MobType mob, double speedModifier, boolean followingTargetEvenIfNotSeen ) {
		super( mob, speedModifier, followingTargetEvenIfNotSeen );

		this.mob = mob;
	}

	public void tick() {
		super.tick();

		CustomSkills< ? > skills = this.mob.getCustomSkills();
		this.mob.setAggressive( !skills.isUsing() && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2 );
	}

	@Override
	protected void checkAndPerformAttack( LivingEntity target, double distanceSquared ) {
		CustomSkills< ? > skills = this.mob.getCustomSkills();
		if( this.isTimeToAttack() && !skills.isUsing() && skills.tryToStart( target, distanceSquared ) ) {
			skills.onLastTick( this::resetAttackCooldown );
		}
	}
}
