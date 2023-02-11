package com.mlib.entities;

public interface ICustomSkillProvider< Type extends CustomSkills< ? > > {
	Type getCustomSkills();
}
