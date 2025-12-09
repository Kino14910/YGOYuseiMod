package yuseimod.characters;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class CustomEnums {
    @SpireEnum public static AbstractCard.CardTags Spell;
    @SpireEnum public static AbstractCard.CardTags Trap;

    public static class YGOCardColor {
        @SpireEnum public static AbstractCard.CardColor Monster;
        @SpireEnum public static AbstractCard.CardColor SynchroMonster;
    }

    public static class YGOLibraryType {
        @SpireEnum public static CardLibrary.LibraryType Monster;
        @SpireEnum public static CardLibrary.LibraryType SynchroMonster;
    }
}
