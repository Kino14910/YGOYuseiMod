package yuseimod.cards.monsterCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import yuseimod.YGOMonsters.SpeedWarriorOrb;
import yuseimod.actions.SummonAction;

public class SpeedWarrior extends AbstractMonsterCard{
    public static final String ID = makeID(SpeedWarrior.class.getSimpleName());

    public SpeedWarrior() {
        super(ID, CardRarity.BASIC, 9, 4, 2, false);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonAction(new SpeedWarriorOrb(false) , cardATK, cardDEF));
    }
}
