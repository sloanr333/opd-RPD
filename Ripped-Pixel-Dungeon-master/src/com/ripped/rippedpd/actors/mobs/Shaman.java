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
import com.ripped.rippedpd.ResultDescriptions;
import com.ripped.rippedpd.actors.Char;
import com.ripped.rippedpd.effects.particles.SparkParticle;
import com.ripped.rippedpd.items.Generator;
import com.ripped.rippedpd.items.food.AgedMeat;
import com.ripped.rippedpd.levels.Level;
import com.ripped.rippedpd.levels.traps.LightningTrap;
import com.ripped.rippedpd.mechanics.Ballistica;
import com.ripped.rippedpd.sprites.CharSprite;
import com.ripped.rippedpd.sprites.ShamanSprite;
import com.ripped.rippedpd.utils.GLog;
import com.ripped.rippedpd.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Shaman extends Mob implements Callback {

	private static final float TIME_TO_ZAP	= 2f;
	
	private static final String TXT_LIGHTNING_KILLED = "%s's lightning bolt killed you...";
	
	{
		name = "gnoll shaman";
		spriteClass = ShamanSprite.class;
		
		HP = HT = 18;
		defenseSkill = 8;
		
		EXP = 6;
		maxLvl = 14;
		
		loot = Generator.Category.SCROLL;
		lootChance = 0.33f;
		loot = new AgedMeat();
		lootChance = 0.070f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 6 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 11;
	}
	
	@Override
	public int dr() {
		return 4;
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {

		if (Level.distance( pos, enemy.pos ) <= 1) {
			
			return super.doAttack( enemy );
			
		} else {
			
			boolean visible = Level.fieldOfView[pos] || Level.fieldOfView[enemy.pos]; 
			if (visible) {
				((ShamanSprite)sprite).zap( enemy.pos );
			}
			
			spend( TIME_TO_ZAP );
			
			if (hit( this, enemy, true )) {
				int dmg = Random.Int( 2, 12 );
				if (Level.water[enemy.pos] && !enemy.flying) {
					dmg *= 1.5f;
				}
				enemy.damage( dmg, LightningTrap.LIGHTNING );
				
				enemy.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
				enemy.sprite.flash();
				
				if (enemy == Dungeon.hero) {
					
					Camera.main.shake( 2, 0.3f );
					
					if (!enemy.isAlive()) {
						Dungeon.fail( Utils.format( ResultDescriptions.MOB, 
							Utils.indefinite( name ), Dungeon.depth ) );
						GLog.n( TXT_LIGHTNING_KILLED, name );
					}
				}
			} else {
				enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
			}
			
			return !visible;
		}
	}
	
	@Override
	public void call() {
		next();
	}
	
	@Override
	public String description() {
		return
			"The most intelligent gnolls can master shamanistic magic. Gnoll shamans prefer " +
			"battle spells to compensate for lack of might, not hesitating to use them " +
			"on those who question their status in a tribe.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( LightningTrap.Electricity.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
