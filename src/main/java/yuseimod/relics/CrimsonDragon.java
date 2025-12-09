package yuseimod.relics;


import static yuseimod.YuseiMod.makeID;

import yuseimod.characters.CustomEnums.YGOCardColor;

public class CrimsonDragon extends BaseRelic{
    private static final String NAME = "CrimsonDragon";
    public static final String ID = makeID(NAME);
    public CrimsonDragon() {
        super(ID, NAME, YGOCardColor.Monster, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
