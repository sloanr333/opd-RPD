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
package com.ripped.rippedpd.plants;

import com.ripped.rippedpd.Dungeon;
import com.ripped.rippedpd.actors.Char;
import com.ripped.rippedpd.actors.buffs.Blindness;
import com.ripped.rippedpd.actors.buffs.Buff;
import com.ripped.rippedpd.actors.mobs.Mob;
import com.ripped.rippedpd.actors.mobs.Mob.State;
import com.ripped.rippedpd.effects.CellEmitter;
import com.ripped.rippedpd.effects.Speck;
import com.ripped.rippedpd.items.potions.PotionOfInvisibility;
import com.ripped.rippedpd.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Blindweed extends Plant {

	private static final String TXT_DESC = 
		"Upon touching a Blindweed it perishes in a blinding flash of light.";
	
	{
		image = 3;
		plantName = "Blindweed";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			Buff.prolong( ch, Blindness.class, Random.Int( 5, 10 ) );
			if (ch instanceof Mob) {
				((Mob)ch).state = State.WANDERING;
				((Mob)ch).beckon( Dungeon.level.randomDestination() );
			}
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).burst( Speck.factory( Speck.LIGHT ), 4 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Blindweed";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_BLINDWEED;
			
			plantClass = Blindweed.class;
			alchemyClass = PotionOfInvisibility.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
