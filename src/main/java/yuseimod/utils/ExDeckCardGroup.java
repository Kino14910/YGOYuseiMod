package yuseimod.utils;

import java.util.Comparator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import yuseimod.cards.monsterCard.synchro.AbstractSynchroMonsterCard;
import yuseimod.characters.CustomEnums.YGOCardColor;;

public class ExDeckCardGroup extends CardGroup {
    public ExDeckCardGroup() {
        super(CardGroupType.UNSPECIFIED);
    }

    @Override
    public void addToTop(AbstractCard card) {
        if (card.color == YGOCardColor.SynchroMonster) {
            super.addToTop(card);
            ModHelper.addToBotAbstract(() -> {
                this.group.sort(Comparator.comparingInt(c -> ((AbstractSynchroMonsterCard)c).cardLevel));
            });
        }
    }

    @Override
    public void addToBottom(AbstractCard card) {
        if (card.color == YGOCardColor.SynchroMonster) {
            super.addToBottom(card);
        }
    }

    public void addCard(AbstractCard card) {
        if (card.color == YGOCardColor.SynchroMonster) {
            addToTop(card);
        }
    }
}
