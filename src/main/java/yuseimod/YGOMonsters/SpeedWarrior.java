package yuseimod.YGOMonsters;

import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class SpeedWarrior extends AbstractYGOMonster {

    public SpeedWarrior(String ID, String NAME, int basePassiveAmount, int baseEvokeAmount, int HP, String IMG_PATH,
            String DESCRIPTION, String UPGRADE_DESCRIPTION) {
        super(ID, NAME, basePassiveAmount, baseEvokeAmount, HP, IMG_PATH, DESCRIPTION, UPGRADE_DESCRIPTION);
    }

    @Override
    public AbstractOrb makeCopy() {
        return null;
    }

    @Override
    public void playChannelSFX() {
        return;
    }
}
