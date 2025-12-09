package yuseimod.cards;


import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
public class Defend extends YuseiCard {
    public static final String ID = makeID(Defend.class.getSimpleName());
    public Defend() {
        super(ID, 1, CardType.SKILL, CardTarget.SELF, CardRarity.BASIC);
        setBlock(5, 3);

        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }
}


