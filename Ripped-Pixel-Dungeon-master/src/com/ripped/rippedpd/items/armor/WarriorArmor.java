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
package com.ripped.rippedpd.items.armor;

import com.ripped.rippedpd.Dungeon;
import com.ripped.rippedpd.actors.Actor;
import com.ripped.rippedpd.actors.Char;
import com.ripped.rippedpd.actors.buffs.Buff;
import com.ripped.rippedpd.actors.buffs.Fury;
import com.ripped.rippedpd.actors.buffs.Invisibility;
import com.ripped.rippedpd.actors.buffs.Paralysis;
import com.ripped.rippedpd.actors.hero.Hero;
import com.ripped.rippedpd.actors.hero.HeroClass;
import com.ripped.rippedpd.actors.hero.HeroSubClass;
import com.ripped.rippedpd.effects.CellEmitter;
import com.ripped.rippedpd.effects.Speck;
import com.ripped.rippedpd.levels.Level;
import com.ripped.rippedpd.mechanics.Ballistica;
import com.ripped.rippedpd.scenes.CellSelector;
import com.ripped.rippedpd.scenes.GameScene;
import com.ripped.rippedpd.sprites.ItemSpriteSheet;
import com.ripped.rippedpd.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.utils.PointF;

public class WarriorArmor extends ClassArmor {
	
	private static int LEAP_TIME	= 1;
	private static int SHOCK_TIME	= 3;
	
	private static final String AC_SPECIAL = "HEROIC LEAP"; 
	
	private static final String TXT_NOT_WARRIOR	= "Only warriors can use this armor!";
	
	{
		name = "warrior suit of armor";
		image = ItemSpriteSheet.ARMOR_WARRIOR;
	}
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {
		GameScene.selectCell( leaper );
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass == HeroClass.WARRIOR) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_WARRIOR );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return
			"While this armor looks heavy, it allows a warrior to perform heroic leap towards " +
			"a targeted location, slamming down to stun all neighbouring enemies.";
	}
	
	protected static CellSelector.Listener leaper = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			if (target != null && target != curUser.pos) {
				
				int cell = Ballistica.cast( curUser.pos, target, false, true );
				if (Actor.findChar( cell ) != null && cell != curUser.pos) {
					cell = Ballistica.trace[Ballistica.distance - 2];
				}
				
				curUser.HP /= 2;
				if (curUser.subClass == HeroSubClass.BERSERKER && curUser.HP <= curUser.HT * Fury.LEVEL) {
					Buff.affect( curUser, Fury.class );
				}
				
				Invisibility.dispel();
				
				curUser.move( cell );
				curUser.sprite.place( cell );
				Dungeon.level.press( target, curUser );
				Dungeon.observe();
				
				for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
					Char mob = Actor.findChar( curUser.pos + Level.NEIGHBOURS8[i] );
					if (mob != null && mob != curUser) {
						Buff.prolong( mob, Paralysis.class, SHOCK_TIME );
					}
				}
				
				PointF pos = curUser.sprite.point();
				Camera.main.target = null;
				curUser.sprite.y -= 16;
				curUser.sprite.parent.add( new PosTweener( curUser.sprite, pos, 0.1f ) );
				
				CellEmitter.center( cell ).burst( Speck.factory( Speck.DUST ), 10 );
				
				curUser.spendAndNext( LEAP_TIME );
			}
		}
		
		@Override
		public String prompt() {
			return "Choose direction to leap";
		}
	};
}