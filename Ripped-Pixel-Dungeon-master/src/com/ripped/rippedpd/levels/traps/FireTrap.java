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
package com.ripped.rippedpd.levels.traps;

import com.ripped.rippedpd.actors.Char;
import com.ripped.rippedpd.actors.blobs.Blob;
import com.ripped.rippedpd.actors.blobs.Fire;
import com.ripped.rippedpd.effects.CellEmitter;
import com.ripped.rippedpd.effects.particles.FlameParticle;
import com.ripped.rippedpd.scenes.GameScene;

public class FireTrap {

	// 0xFF7708
	
	public static void trigger( int pos, Char ch ) {
		
		GameScene.add( Blob.seed( pos, 2, Fire.class ) );
		CellEmitter.get( pos ).burst( FlameParticle.FACTORY, 5 );
		
	}
}