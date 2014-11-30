package com.ripped.rippedpd.items.weapon.melee;

import com.ripped.rippedpd.items.weapon.enchantments.Healing;
import com.ripped.rippedpd.sprites.ItemSpriteSheet;

public class Club extends MeleeWeapon {
	
	{
		name = "club";
		image = ItemSpriteSheet.CLUB;
	}
	
	public Club () {
		super (1, 1f, 1f);
		enchant(new Healing());
	}
	
	@Override
	public String desc () {
		return "Made by a cleric, this club possesses healing abilities. It is very firm.";
	}
}