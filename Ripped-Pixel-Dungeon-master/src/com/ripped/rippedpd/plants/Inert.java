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
import com.ripped.rippedpd.actors.buffs.Buff;
import com.ripped.rippedpd.actors.buffs.Paralysis;
import com.ripped.rippedpd.effects.CellEmitter;
import com.ripped.rippedpd.effects.Speck;
import com.ripped.rippedpd.items.potions.PotionOfParalyticGas;
import com.ripped.rippedpd.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Inert extends Plant {

	private static final String TXT_DESC = 
		"Anything that touches an inert shrub will be paralysed.";
	
	{
		image = 8;
		plantName = "Inertshrub";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		if (ch != null) {
			Buff.prolong( ch, Paralysis.class, Random.Int( 5, 10 ) );
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).burst( Speck.factory( Speck.PARALYSIS ), 4 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Inertshrub";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_INERT;
			
			plantClass = Inert.class;
			alchemyClass = PotionOfParalyticGas.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
