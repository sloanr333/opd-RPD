package com.ripped.pixeldungeon.plants;

import com.ripped.pixeldungeon.Dungeon;
import com.ripped.pixeldungeon.actors.Char;
import com.ripped.pixeldungeon.actors.buffs.Buff;
import com.ripped.pixeldungeon.actors.buffs.MindVision;
import com.ripped.pixeldungeon.effects.CellEmitter;
import com.ripped.pixeldungeon.effects.Speck;
import com.ripped.pixeldungeon.items.potions.PotionOfStrength;
import com.ripped.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class SilverLeaf extends Plant{
	
	private static final String TXT_DESC = 
			"When touching this herb, your senses are heightened allowing you to see creatures.";
		
		{
			image = 9;
			plantName = "Silverleaf";
		}
		
		@Override
		public void activate( Char ch ) {
			super.activate( ch );
			
			if (ch != null) {
				Buff.prolong( ch, MindVision.class, Random.Int( 5, 10 ) );
				}
			
			
			if (Dungeon.visible[pos]) {
				CellEmitter.get( pos ).burst( Speck.factory( Speck.DISCOVER ), 4 );
			}
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
		
		public static class Seed extends Plant.Seed {
			{
				plantName = "Silverleaf";
				
				name = "seed of " + plantName;
				image = ItemSpriteSheet.SEED_SILVERLEAF;
				
				plantClass = SilverLeaf.class;
				alchemyClass = PotionOfStrength.class;
			}
			
			@Override
			public String desc() {
				return TXT_DESC;
			}
		}
	}
