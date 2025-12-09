package yuseimod.ui.panelitems;

import static yuseimod.YuseiMod.makeID;
import static yuseimod.YuseiMod.uiPath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;

import basemod.BaseMod;
import basemod.TopPanelItem;
import yuseimod.characters.Yusei;
import yuseimod.ui.panels.ExDeckCards;
import yuseimod.ui.screens.ExDeckViewScreen;
import yuseimod.utils.YuseiModInputActionSet;

public class ExDeckPanelItem extends TopPanelItem {
    private static final Texture IMG = new Texture(uiPath("NobleTopPanel"));
    public static final String ID = makeID(ExDeckPanelItem.class.getSimpleName());
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(makeID(ExDeckPanelItem.class.getSimpleName()));
    public static final String[] TEXT = ExDeckPanelItem.tutorialStrings.TEXT;
    public static final String LABEL = TEXT[0];
    public static final String MSG = TEXT[1];
    public ExDeckPanelItem() {
        super(IMG, ID);
    }

    @Override
    public void update() {
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.FTUE) {
            super.update();
            if (YuseiModInputActionSet.nobleDeckAction.isJustPressed()) {
                onClick();
            }
        }
    }

    @Override
    protected void onClick() {
        if (ExDeckCards.getExCards().isEmpty()) {
            return;
        }

        // 如果 Noble 屏幕已经打开，则关闭它（切换行为）
        if (AbstractDungeon.screen == ExDeckViewScreen.Enum.Noble_Phantasm) {
            // AbstractDungeon.screenSwap = true;
            // 如果是在卡牌奖励界面，则关闭当前界面并返回战斗奖励界面
            // if (AbstractDungeon.previousScreen == AbstractDungeon.CurrentScreen.CARD_REWARD) {
            //     AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
            // }
            AbstractDungeon.closeCurrentScreen();
            CardCrawlGame.sound.play("DECK_CLOSE", 0.05f);
            return;
        }

        if (!AbstractDungeon.isScreenUp) {
            BaseMod.openCustomScreen(ExDeckViewScreen.Enum.Noble_Phantasm, ExDeckCards.getExCards());
            return;
        }

        switch (AbstractDungeon.screen) {
            case COMBAT_REWARD:
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
                break;
            case DEATH:
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.DEATH;
                AbstractDungeon.deathScreen.hide();
                break;
            case BOSS_REWARD:
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.BOSS_REWARD;
                AbstractDungeon.bossRelicScreen.hide();
                break;
            case SHOP:
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
                break;
            case MAP:
                if (!AbstractDungeon.dungeonMapScreen.dismissable) {
                    AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.MAP;

                } else {
                    if (AbstractDungeon.previousScreen != null) {
                        AbstractDungeon.screenSwap = true;
                    }
                    AbstractDungeon.closeCurrentScreen();

                }
                break;
            case SETTINGS:
                if (AbstractDungeon.previousScreen != null) {
                    AbstractDungeon.screenSwap = true;
                }
                AbstractDungeon.closeCurrentScreen();
                break;
            case INPUT_SETTINGS:
                if (AbstractDungeon.previousScreen != null) {
                    AbstractDungeon.screenSwap = true;
                }
                AbstractDungeon.closeCurrentScreen();
                break;
            case CARD_REWARD:
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.CARD_REWARD;
                AbstractDungeon.dynamicBanner.hide();
                break;
            case GRID:
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.GRID;
                AbstractDungeon.gridSelectScreen.hide();
                break;
            case HAND_SELECT:
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.HAND_SELECT;
                break;
            default:
                // 默认行为：关闭当前屏幕（如果有）再打开
                if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE) {
                    AbstractDungeon.closeCurrentScreen();
                }
                break;
        }

        BaseMod.openCustomScreen(ExDeckViewScreen.Enum.Noble_Phantasm, ExDeckCards.getExCards());


        // // 标记卡牌为已见
        // NobleDeckCards.getNobleCards().group.forEach(c -> UnlockTracker.markCardAsSeen(c.cardID));
    }

    @Override
    protected void onHover() {
        if (!AbstractDungeon.isScreenUp || AbstractDungeon.screen == ExDeckViewScreen.Enum.Noble_Phantasm) {
            TipHelper.renderGenericTip(1550.0f * Settings.scale, (float)Settings.HEIGHT - 120.0f * Settings.scale, LABEL + "(" + YuseiModInputActionSet.nobleDeckAction.getKeyString() + ")", MSG);
        }
        super.onHover();
    }

    @Override
    public void render(SpriteBatch sb) {
        if (AbstractDungeon.player instanceof Yusei) {
            super.render(sb);
        }
    }
}
