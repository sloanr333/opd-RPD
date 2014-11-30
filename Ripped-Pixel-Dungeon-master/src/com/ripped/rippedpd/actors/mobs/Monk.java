/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.ripped.rippedpd.actors.mobs;

import java.util.HashSet;

import com.ripped.rippedpd.Dungeon;
import com.ripped.rippedpd.actors.Char;
import com.ripped.rippedpd.actors.buffs.Amok;
import com.ripped.rippedpd.actors.buffs.Terror;
import com.ripped.rippedpd.actors.hero.Hero;
import com.ripped.rippedpd.actors.mobs.npcs.Imp;
import com.ripped.rippedpd.items.KindOfWeapon;
import com.ripped.rippedpd.items.food.Food;
import com.ripped.rippedpd.items.weapon.melee.Knuckles;
import com.ripped.rippedpd.sprites.MonkSprite;
import com.ripped.rippedpd.utils.GLog;
import com.watabou.utils.Random;

public class Monk extends Mob {

	public static final String TXT_DISARM	= "%s has knocked the %s from your hands!";
	
	{
		name = "dwarf monk";
		spriteClass = MonkSprite.class;
		
		HP = HT = 70;
		defenseSkill = 30;
		
		EXP = 11;
		maxLvl = 21;
		
		loot = new Food();
		lootChance = 0.083f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 12, 16 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 30;
	}
	
	@Override
	protected float attackDelay() {
		return 0.5f;
	}
	
	@Override
	public int dr() {
		return 2;
	}
	
	@Override
	public String defenseVerb() {
		return "parried";
	}
	
	@Override
	public void die( Object cause ) {
		Imp.Quest.process( this );
		
		super.die( cause );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		
		if (Random.Int( 6 ) == 0 && enemy == Dungeon.hero) {
			
			Hero hero = Dungeon.hero;
			KindOfWeapon weapon = hero.belongings.weapon;
			
			if (weapon != null && !(weapon instanceof Knuckles) && !weapon.cursed) {
				hero.belongings.weapon = null;
				Dungeon.level.drop( weapon, hero.pos ).sprite.drop();
				GLog.w( TXT_DISARM, name, weapon.name() );
			}
		}
		
		return damage;
	}
	
	@Override
	public String description() {
		return
			"These monks are fanatics, who devoted themselves to protecting their city's secrets from all aliens. " +
			"They don't use any armor or weapons, relying solely on the art of hand-to-hand combat.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
