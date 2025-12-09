package yuseimod.ui.panels;

import java.util.ArrayList;
import java.util.Arrays;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import basemod.abstracts.CustomSavable;
import yuseimod.characters.CustomEnums.YGOCardColor;
import yuseimod.characters.Yusei;
import yuseimod.utils.ExDeckCardGroup;

public class ExDeckCards implements CustomSavable<ArrayList<String>> {
    public static ArrayList<String> cards;
    public static ExDeckCardGroup exCards;

    public static ExDeckCardGroup getExCards() {
        if (exCards == null) {
            exCards = new ExDeckCardGroup();
        }
        return exCards;
    }
    
    @Override
    public ArrayList<String> onSave() {
        return (AbstractDungeon.player instanceof Yusei && ExDeckCards.cards != null) ? new ArrayList<String>(ExDeckCards.cards) : null;
    }
 
    @Override
    public void onLoad(ArrayList<String> save) {
        if (AbstractDungeon.player instanceof Yusei && (save == null || save.isEmpty())) {
            // no saved data -> use defaults
            ExDeckCards.reset();
            return;
        }

        ExDeckCards.cards = save;
    }

    public static void reset() {
        ExDeckCards.cards = new ArrayList<>(Arrays.asList(
            //宝具卡
        ));
        if (exCards != null) {
            exCards.clear();
        }
        ExDeckCards.addCards(ExDeckCards.cards);
    }

    public static void addCard(String cardId) {
        if (ExDeckCards.cards == null) {
            ExDeckCards.cards = new ArrayList<String>();
        }
        ExDeckCards.cards.add(cardId);
        addCardById(cardId);
    }

    public static void addCard(AbstractCard card) {
        if (card.color == YGOCardColor.SynchroMonster) {
            getExCards().addToTop(card);
        }
    }

    public static void addCardById(String cardId) {
        AbstractCard card = CardLibrary.getCopy(cardId);
        if (card != null && card.color == YGOCardColor.SynchroMonster) {
            getExCards().addToTop(card);
        }
    }

    public static void addCards(ArrayList<String> cards) {
        for (String cardId : cards) {
            addCardById(cardId);
        }
    }
}