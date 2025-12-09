package yuseimod.ui.buttons;

import static yuseimod.YuseiMod.makeID;
import static yuseimod.YuseiMod.uiPath;
import static yuseimod.utils.ModHelper.addToBot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;

import yuseimod.YGOMonsters.AbstractYGOMonster;
import yuseimod.cards.monsterCard.synchro.AbstractSynchroMonsterCard;
import yuseimod.ui.panels.ExDeckCards;

public class SynchroSummonButton extends AbstractPanel {
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(makeID("SynchroSummon"));

    private final Vector2 position = new Vector2();
    private float scale = 1.0f;
    private final Hitbox hb;
    private final Color renderColor = Color.WHITE.cpy();
    private static final Texture NP_MAX = ImageMaster.loadImage(uiPath("np_max"));

    public SynchroSummonButton() {
        super(
            AbstractDungeon.player.hb.x - 48.0F * Settings.scale,
            AbstractDungeon.player.hb.y + AbstractDungeon.player.hb.height - 16.0F * Settings.scale,
            -Settings.WIDTH,
            AbstractDungeon.player.hb.y + AbstractDungeon.player.hb.height - 16.0F * Settings.scale,
            8.0f * Settings.xScale,
            0.0f,
            null,
            true
        );
        hb = new Hitbox(
            AbstractDungeon.player.hb.x - 64.0F * Settings.scale,
            AbstractDungeon.player.hb.y + AbstractDungeon.player.hb.height - 12.0F * Settings.scale,
            64.0F * Settings.scale,
            64.0F * Settings.scale
        );
    }

    @Override
    public void updatePositions() {
        super.updatePositions();
        hb.update();
        scale = MathHelper.scaleLerpSnap(scale, Settings.scale);

        if (hb.hovered) {
            AbstractDungeon.overlayMenu.hoveredTip = true;
            if (InputHelper.justClickedLeft) {
                InputHelper.justClickedLeft = false;
                chooseNobleCard();
                CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F);
            }
        }
    }

    private void chooseNobleCard() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new FetchAction(ExDeckCards.getExCards()));
        
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(renderColor);

        if (this.checkSynchroCondition() && !AbstractDungeon.isScreenUp) {
            sb.draw(NP_MAX, current_x, current_y, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        hb.render(sb);

        if (shouldRenderTip()) {
            TipHelper.renderGenericTip(
                AbstractDungeon.player.hb.x - 128.0F * Settings.scale,
                AbstractDungeon.player.hb.y + AbstractDungeon.player.hb.height - 32.0F * Settings.scale,
                tutorialStrings.LABEL[0],
                tutorialStrings.TEXT[0] + tutorialStrings.TEXT[1] + tutorialStrings.TEXT[2]
            );
        } 
    }

    private boolean shouldRenderTip() {
        return !isHidden &&
               hb != null &&
               hb.hovered &&
               !AbstractDungeon.isScreenUp &&
               AbstractDungeon.getMonsters() != null &&
               !AbstractDungeon.getMonsters().areMonstersDead();
    }

    private boolean checkSynchroCondition() {
        boolean hasTuner = false;
        boolean hasNonTuner = false;

        // 检查场上基础条件（必须至少有一个调整和一个非调整怪兽）
        for(AbstractOrb orb : AbstractDungeon.player.orbs) {
            AbstractYGOMonster monster = (AbstractYGOMonster)orb;
            if (monster.isTuner()) {
                hasTuner = true;
            } else {
                hasNonTuner = true;
            }
            if (!hasTuner || !hasNonTuner) {
                return false;
            }
        }

        Set<Integer> possibleLevels = calculatePossibleLevels();
        for (AbstractCard exMonster : ExDeckCards.getExCards().group) {
            if (exMonster instanceof AbstractSynchroMonsterCard) {
                AbstractSynchroMonsterCard monsterCard = (AbstractSynchroMonsterCard)exMonster;
                if (possibleLevels.contains(monsterCard.cardLevel)) {
                    return true; 
                }
            }
        }
        return false;
    }

    private Set<Integer> calculatePossibleLevels() {
    List<AbstractYGOMonster> tuners = new ArrayList<>();
    List<AbstractYGOMonster> nonTuners = new ArrayList<>();
    
    // 分离调整和非调整怪兽
    for (AbstractOrb orb : AbstractDungeon.player.orbs) {
        AbstractYGOMonster monster = (AbstractYGOMonster)orb;
        if (monster.isTuner()) {
            tuners.add(monster);
        } else {
            nonTuners.add(monster);
        }
    }
    
    Set<Integer> possibleLevels = new HashSet<>();
    int nonTunerCount = nonTuners.size();
    
    // 生成所有非空子集（位掩码技巧，高效！）
    for (int mask = 1; mask < (1 << nonTunerCount); mask++) {
        for (AbstractYGOMonster tuner : tuners) {
            int totalLevel = tuner.level;
            // 遍历非调整怪兽的子集
            for (int i = 0; i < nonTunerCount; i++) {
                if ((mask & (1 << i)) != 0) {
                    totalLevel += nonTuners.get(i).level;
                }
            }
            possibleLevels.add(totalLevel);
        }
    }
    return possibleLevels;
}
}
