package yuseimod.cards.monsterCard.synchro;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;

import yuseimod.cards.monsterCard.AbstractMonsterCard;
import yuseimod.characters.CustomEnums.YGOCardColor;

public abstract class AbstractSynchroMonsterCard extends AbstractMonsterCard {
    public static class Enums {
        @SpireEnum public static AbstractCard.CardTags SynchroMonster;
    }

    public AbstractSynchroMonsterCard(String ID, CardRarity rarity) {
        super(ID, rarity, YGOCardColor.SynchroMonster);
        this.tags.add(Enums.SynchroMonster);
    }
}
