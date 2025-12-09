package yuseimod.cards.monsterCard;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import yuseimod.cards.YuseiCard;

public abstract class AbstractMonsterCard extends YuseiCard {
    public static class Enums {
        @SpireEnum public static AbstractCard.CardTags Monster;
    }
    // Stats
    public int cardATK = 0;
    public int cardDEF = 0;
    public int cardLevel = 0;
    public boolean isTuner = false;
    public int Index = 0;

    public int MagicNum = 0;
    public int MagicNum2 = 0;
    public int Effectnum = 0;
    public int EffectTimes = 1;

    public boolean isTuner() {
        return isTuner;
    }

    public void setTuner() {
        this.isTuner = true;
    }

    public AbstractMonsterCard(String ID, CardRarity rarity) {
        super(ID, 0, CardType.POWER, CardTarget.NONE, rarity);
        
        this.tags.add(Enums.Monster);
    }

    public AbstractMonsterCard(String ID, CardRarity rarity, CardColor color) {
        super(ID, 0, CardType.POWER, CardTarget.NONE, rarity, color);
        this.tags.add(Enums.Monster);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // Check for empty slot among orbs so summon card can be played
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (orb instanceof EmptyOrbSlot) {
                return super.canUse(p, m);
            }
        }
        // Can't summon if no empty orb slots available
        return false;
    }
}
