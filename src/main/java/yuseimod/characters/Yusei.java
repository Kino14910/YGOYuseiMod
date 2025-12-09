package yuseimod.characters;

import static yuseimod.YuseiMod.charPath;
import static yuseimod.YuseiMod.makeID;
import static yuseimod.YuseiMod.uiPath;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import basemod.abstracts.CustomPlayer;
import yuseimod.cards.Defend;
import yuseimod.cards.Strike;
import yuseimod.characters.CustomEnums.YGOCardColor;
import yuseimod.relics.CrimsonDragon;
import yuseimod.utils.Sounds;

public class Yusei extends CustomPlayer{
    public static class PlayerColorEnum {
        @SpireEnum public static AbstractPlayer.PlayerClass YUSEI;
}
    private static final String[] ORB_TEXTURES = new String[] {
            uiPath("EPanel/layer5"),
            uiPath("EPanel/layer4"),
            uiPath("EPanel/layer3"),
            uiPath("EPanel/layer2"),
            uiPath("EPanel/layer1"),
            uiPath("EPanel/layer0"),
            uiPath("EPanel/layer5d"),
            uiPath("EPanel/layer4d"),
            uiPath("EPanel/layer3d"),
            uiPath("EPanel/layer2d"),
            uiPath("EPanel/layer1d")};
    private static final float[] LAYER_SPEED = new float[] { -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F };
    private static final String[] TEXT = CardCrawlGame.languagePack.getCharacterString(makeID("Yusei")).TEXT;
    public static final Color SILVER = CardHelper.getColor(200, 200, 200);
    
    public Yusei(String name) {
        //构造方法，初始化参数
        super(name, PlayerColorEnum.YUSEI, ORB_TEXTURES, uiPath("energyBlueVFX"), LAYER_SPEED, null, null);
        dialogX = drawX + 0.0F * Settings.scale;
        dialogY = drawY + 220.0F * Settings.scale;

        initializeClass(
                charPath("Yusei"),
                charPath("shoulder2"), charPath("shoulder1"),
                charPath("fallen"),
                getLoadout(),
                0.0F, 5.0F,
                240.0F, 300.0F,
                new EnergyManager(3)
        );
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        // int charIndex = MainMenuUIFgoPatch.modifierIndexes;

        //添加初始卡组
        ArrayList<String> retVal = new ArrayList<>(Arrays.asList(
            Strike.ID, Strike.ID, Strike.ID,
            Defend.ID, Defend.ID, Defend.ID,
            Defend.ID, Defend.ID
        ));
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        //添加初始遗物
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(CrimsonDragon.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        //选人物界面的文字描述
        return new CharSelectInfo(
                TEXT[0],
                TEXT[1],
                64,
                64,
                5,
                99,
                1,
                this,
                getStartingRelics(),
                getStartingDeck(),
                false
        );
    }

    @Override
    public String getTitle(PlayerClass playerClass) {return TEXT[0];}

    @Override
    public AbstractCard.CardColor getCardColor() {
        //选择卡牌颜色
        return YGOCardColor.Monster;
    }

    @Override
    public Color getCardRenderColor() {
        return SILVER;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike();
    }

    @Override
    public Color getCardTrailColor() {
        return SILVER;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 6;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public void updateOrb(int orbCount) {
        energyOrb.updateOrb(orbCount);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return null;
    }

    @Override
    public String getLocalizedCharacterName() {return TEXT[0];}

    @Override
    public AbstractPlayer newInstance() {
        return new Yusei(name);
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] { AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL };
    }

    @Override
    public String getVampireText() {return Vampires.DESCRIPTIONS[1];}

    @Override
    public String getSpireHeartText() {
        return TEXT[2];
    }

    @Override
    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
    }

    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>(Arrays.asList(
            new CutscenePanel(charPath("Victory1")),
            new CutscenePanel(charPath("Victory2")),
            new CutscenePanel(charPath("Victory3"))
        ));
        return panels;
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.mainMenuScreen.charSelectScreen.bgCharImg = new Texture(charPath("YuseiPortrait"));

        CardCrawlGame.sound.playV(Sounds.char_choose, 0.8F);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    @Override
    public Color getSlashAttackColor() {
        return SILVER;
    }
    
}
