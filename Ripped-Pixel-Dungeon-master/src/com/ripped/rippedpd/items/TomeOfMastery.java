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
package com.ripped.rippedpd.items;

import java.util.ArrayList;

import com.ripped.rippedpd.Assets;
import com.ripped.rippedpd.Badges;
import com.ripped.rippedpd.actors.buffs.Blindness;
import com.ripped.rippedpd.actors.buffs.Buff;
import com.ripped.rippedpd.actors.buffs.Fury;
import com.ripped.rippedpd.actors.hero.Hero;
import com.ripped.rippedpd.actors.hero.HeroSubClass;
import com.ripped.rippedpd.effects.Speck;
import com.ripped.rippedpd.effects.SpellSprite;
import com.ripped.rippedpd.scenes.GameScene;
import com.ripped.rippedpd.sprites.ItemSpriteSheet;
import com.ripped.rippedpd.utils.GLog;
import com.ripped.rippedpd.utils.Utils;
import com.ripped.rippedpd.windows.WndChooseWay;
import com.watabou.noosa.audio.Sample;

public class TomeOfMastery extends Item {

	private static final String TXT_BLINDED	= "You can't read while blinded";
	
	public static final float TIME_TO_READ = 10;
	
	public static final String AC_READ	= "READ";
	
	{
		stackable = false;
		name = "Tome of Mastery";
		image = ItemSpriteSheet.MASTERY;
		
		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {
			
			if (hero.buff( Blindness.class ) != null) {
				GLog.w( TXT_BLINDED );
				return;
			}
			
			curUser = hero;
			
			HeroSubClass way1 = null;
			HeroSubClass way2 = null;
			switch (hero.heroClass) {
			case WARRIOR:
				way1 = HeroSubClass.GLADIATOR;
				way2 = HeroSubClass.BERSERKER;
				break;
			case MAGE:
				way1 = HeroSubClass.BATTLEMAGE;
				way2 = HeroSubClass.WARLOCK;
				break;
			case ROGUE:
				way1 = HeroSubClass.FREERUNNER;
				way2 = HeroSubClass.ASSASSIN;
				break;
			case PRIEST:
				way1 = HeroSubClass.DRUID;
				way2 = HeroSubClass.ALCHEMIST;
				break;
			case HUNTRESS:
				way1 = HeroSubClass.SNIPER;
				way2 = HeroSubClass.WARDEN;
				break;
			}
			GameScene.show( new WndChooseWay( this, way1, way2 ) );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean doPickUp( Hero hero ) {
		Badges.validateMastery();
		return super.doPickUp( hero );
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
		return 
			"This worn leather book is not that thick, but you feel somehow, " +
			"that you can gather a lot from it. Remember though that reading " +
			"this tome may require some time.";
	}
	
	public void choose( HeroSubClass way ) {
		
		detach( curUser.belongings.backpack );
		
		curUser.spend( TomeOfMastery.TIME_TO_READ );
		curUser.busy();
		
		curUser.subClass = way;
		
		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.SND_MASTERY );
		
		SpellSprite.show( curUser, SpellSprite.MASTERY );
		curUser.sprite.emitter().burst( Speck.factory( Speck.MASTERY ), 12 );
		GLog.w( "You have chosen the way of the %s!", Utils.capitalize( way.title() ) );
		
		if (way == HeroSubClass.BERSERKER && curUser.HP <= curUser.HT * Fury.LEVEL) {
			Buff.affect( curUser, Fury.class );
		}
	}
}
