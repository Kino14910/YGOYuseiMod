/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.evacipated.cardcrawl.modthespire.lib.SpirePatch
 *  com.evacipated.cardcrawl.modthespire.lib.SpireReturn
 *  com.megacrit.cardcrawl.core.Settings
 *  com.megacrit.cardcrawl.dungeons.AbstractDungeon
 *  com.megacrit.cardcrawl.orbs.AbstractOrb
 */
package yuseimod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import yuseimod.characters.Yusei;

@SpirePatch(clz=AbstractOrb.class, method="setSlot", paramtypez={int.class, int.class})
public class SetSlotPatch {
    public static SpireReturn<Void> Prefix(AbstractOrb abstractOrb_instance, int slotNum, int maxOrbs) {
        if (AbstractDungeon.player instanceof Yusei) {
            if (maxOrbs > 1) {
                abstractOrb_instance.tX = AbstractDungeon.player.drawX + (400.0f - 25.0f * (float)maxOrbs) * Settings.scale - (float)slotNum * (125.0f - (float)(maxOrbs - 7) * 10.0f) * Settings.scale;
                abstractOrb_instance.tY = AbstractDungeon.player.drawY + 375.0f * Settings.scale;
                abstractOrb_instance.hb.move(abstractOrb_instance.tX, abstractOrb_instance.tY);
            } else {
                abstractOrb_instance.tX = AbstractDungeon.player.drawX;
                abstractOrb_instance.tY = AbstractDungeon.player.drawY + 375.0f * Settings.scale;
                abstractOrb_instance.hb.move(abstractOrb_instance.tX, abstractOrb_instance.tY);
            }
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}

