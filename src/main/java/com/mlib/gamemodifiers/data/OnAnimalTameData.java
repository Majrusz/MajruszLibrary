package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.AnimalTameEvent;

public class OnAnimalTameData extends ContextData.Event< AnimalTameEvent > {
	public final Animal animal;
	public final Player tamer;

	public OnAnimalTameData( AnimalTameEvent event ) {
		super( event.getAnimal(), event );
		this.animal = event.getAnimal();
		this.tamer = event.getTamer();
	}
}
