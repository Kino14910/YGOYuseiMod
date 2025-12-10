package yuseimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;

import yuseimod.YGOMonsters.AbstractYGOMonster;
import yuseimod.cards.monsterCard.AbstractMonsterCard;
import yuseimod.utils.OrbHelper;

public class SummonAction extends AbstractGameAction {
    private static final String[] TEXT = CardCrawlGame.languagePack.getCardStrings((String)"Hololive_CantCallMinions").EXTENDED_DESCRIPTION;
    private AbstractYGOMonster orb;
    private int ATK;
    private int DEF;

    public SummonAction(AbstractYGOMonster orb, int ATK, int DEF) {
        this.orb = orb;
        this.ATK = ATK;
        this.DEF = DEF;
        this.duration = 2.0f;
    }

    @Override
    public void update() {
        if (this.duration == 2.0f) {
            int Emptycount = 0;
            for (AbstractOrb orb : AbstractDungeon.player.orbs) {
                if (!(orb instanceof EmptyOrbSlot)) continue;
                ++Emptycount;
            }
            if (Emptycount == 0) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, TEXT[0], 0.75f, 2.5f));
            } else {
                this.orb.ATK = this.ATK;
                this.orb.DEF = this.DEF;
                int index = -1;
                for (int plasmaCount = 0; plasmaCount < AbstractDungeon.player.orbs.size(); ++plasmaCount) {
                    if (!(AbstractDungeon.player.orbs.get(plasmaCount) instanceof EmptyOrbSlot)) continue;
                    index = plasmaCount;
                    break;
                }
                if (index != -1) {
                    this.orb.cX = (AbstractDungeon.player.orbs.get(index)).cX;
                    (this.orb).cY = (AbstractDungeon.player.orbs.get(index)).cY;
                    AbstractDungeon.player.orbs.set(index, this.orb);
                    (AbstractDungeon.player.orbs.get(index)).setSlot(index, AbstractDungeon.player.maxOrbs);
                    this.orb.playChannelSFX();
                    for (AbstractPower p : AbstractDungeon.player.powers) {
                        p.onChannel(this.orb);
                    }
                    ++OrbHelper.CallMinionsThisBattle;
                    AbstractDungeon.actionManager.orbsChanneledThisCombat.add(this.orb);
                    AbstractDungeon.actionManager.orbsChanneledThisTurn.add(this.orb);
                    for (AbstractCard abstractCard : AbstractDungeon.player.hand.group) {
                        if (!(abstractCard instanceof AbstractMonsterCard)) continue;
                        ((AbstractMonsterCard)abstractCard).onSummonMonster();
                    }
                    for (AbstractCard abstractCard : AbstractDungeon.player.drawPile.group) {
                        if (!(abstractCard instanceof AbstractMonsterCard)) continue;
                        ((AbstractMonsterCard)abstractCard).onSummonMonster();
                    }
                    for (AbstractCard abstractCard : AbstractDungeon.player.discardPile.group) {
                        if (!(abstractCard instanceof AbstractMonsterCard)) continue;
                        ((AbstractMonsterCard)abstractCard).onSummonMonster();
                    }
                    for (AbstractOrb orb : AbstractDungeon.player.orbs) {
                        if (!(orb instanceof AbstractYGOMonster)) continue;
                        ((AbstractYGOMonster)orb).onSummonMonster(true);
                    }
                    // for (AbstractPower power : AbstractDungeon.player.powers) {
                    //     if (!(power instanceof SingPower)) continue;
                    //     ((SingPower)power).Effect(false);
                    // }
                }
            }
        } else {
            this.isDone = true;
        }
        this.tickDuration();
    }
}

