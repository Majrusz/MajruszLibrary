package com.mlib.modhelper;

import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;

public class DataForge implements IDataPlatform {
	public SimpleChannel channel = null;
	public int messageIdx = 0;
	public DeferredRegister< ? > lastDeferredRegister = null;
}
