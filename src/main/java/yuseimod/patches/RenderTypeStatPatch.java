
package yuseimod.patches;

import static yuseimod.YuseiMod.uiPath;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import yuseimod.cards.monsterCard.AbstractMonsterCard;

public class RenderTypeStatPatch {
    private static final Texture atkimg = ImageMaster.loadImage(uiPath("monsterStats/attacktexture_164"));
    private static final Texture defimg = ImageMaster.loadImage(uiPath("monsterStats/healthtexture_164"));
    // private static final Texture Danger = ImageMaster.loadImage("monsterStats/Danger");

    @SpirePatch(clz=AbstractCard.class, method="renderPortraitFrame")
    public static class renderTypeStatPatch {
        public static SpireReturn<Void> Prefix(AbstractCard _inst, SpriteBatch sb, float x, float y) {
            if (_inst instanceof AbstractMonsterCard) {
                sb.draw(atkimg, _inst.current_x - (170.0f + 120.0f * (_inst.drawScale - 0.7f)) * Settings.scale, _inst.current_y - (178.0f + 193.0f * (_inst.drawScale - 0.7f)) * Settings.scale, 82.0f * Settings.scale, 82.0f * Settings.scale, 164.0f * Settings.scale, 164.0f * Settings.scale, _inst.drawScale * Settings.scale, _inst.drawScale * Settings.scale, _inst.angle, 0, 0, 164, 164, false, false);
                sb.draw(defimg, _inst.current_x + (22.0f + 120.0f * (_inst.drawScale - 0.7f)) * Settings.scale, _inst.current_y - (178.0f + 193.0f * (_inst.drawScale - 0.7f)) * Settings.scale, 82.0f * Settings.scale, 82.0f * Settings.scale, 164.0f * Settings.scale, 164.0f * Settings.scale, _inst.drawScale * Settings.scale, _inst.drawScale * Settings.scale, _inst.angle, 0, 0, 164, 164, false, false);
                FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(((AbstractMonsterCard)_inst).cardATK), (_inst.current_x - (86.0f + 120.0f * (_inst.drawScale - 0.7f)) * Settings.scale), (_inst.current_y - (105.0f + 193.0f * (_inst.drawScale - 0.7f)) * Settings.scale), new Color(1.0f, 1.0f, 1.0f, 1.0f), (1.6f * _inst.drawScale));
                FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(((AbstractMonsterCard)_inst).cardDEF), (_inst.current_x + (106.0f + 120.0f * (_inst.drawScale - 0.7f)) * Settings.scale), (_inst.current_y - (105.0f + 193.0f * (_inst.drawScale - 0.7f)) * Settings.scale), new Color(1.0f, 1.0f, 1.0f, 1.0f), (1.6f * _inst.drawScale));
            }
            // if (_inst instanceof BeCareful) {
            //     sb.draw(Danger, _inst.current_x - 150.0f * Settings.scale, _inst.current_y - 210.0f * Settings.scale, 150.0f * Settings.scale, 210.0f * Settings.scale, 300.0f * Settings.scale, 420.0f * Settings.scale, _inst.drawScale * Settings.scale, _inst.drawScale * Settings.scale, _inst.angle, 0, 0, 300, 420, false, false);
            // }
            return SpireReturn.Continue();
        }
    }
}

