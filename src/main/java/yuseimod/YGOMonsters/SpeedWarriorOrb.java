package yuseimod.YGOMonsters;

import static yuseimod.YuseiMod.makeID;
import static yuseimod.YuseiMod.orbPath;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class SpeedWarriorOrb extends AbstractYGOMonster {
    private static final String ID = makeID("Monster_" + SpeedWarriorOrb.class.getSimpleName());
    private static final OrbStrings cardStrings = CardCrawlGame.languagePack.getOrbString(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String[] DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = orbPath(ID);
    private static final int basePassiveAmount = 3;
    private static final int baseEvokeAmount = 3;
    private static final int originalHP = 4;
    private static final int originalATK = 18;
    public int DeltaATK = 0;
    public int DeltaHP = 0;


    public SpeedWarriorOrb(boolean upgraded) {
        super(ID, NAME, basePassiveAmount, baseEvokeAmount, originalATK, originalHP, IMG_PATH, DESCRIPTION[0], DESCRIPTION[1], upgraded);
    }

    @Override
    public AbstractOrb makeCopy() {
        return null;
    }

    @Override
    public void playChannelSFX() {
        return;
    }

    @Override
    public void onEvoke() {
        super.onEvoke();
        setATK(9);
    }
}
