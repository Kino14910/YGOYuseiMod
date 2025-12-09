package yuseimod.utils;

import java.util.ArrayList;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import yuseimod.YGOMonsters.AbstractYGOMonster;
import yuseimod.cards.monsterCard.AbstractMonsterCard;

public class OrbHelper {
    public static int CallMinionsThisBattle = 0;
    public static ArrayList<AbstractYGOMonster> DeadMinionsThisBattle = new ArrayList();

    public static ArrayList<AbstractCard> ChooseOneMinionGroup(String id, int magicnum1) {
        ArrayList<AbstractCard> abstractCards = new ArrayList<AbstractCard>();
        for (int i = AbstractDungeon.player.orbs.size() - 1; i > -1; --i) {
            if (!(AbstractDungeon.player.orbs.get(i) instanceof AbstractYGOMonster)) continue;
            AbstractMonsterCard card = OrbHelper.GetCardInstance(i);
            if (((AbstractYGOMonster)((Object)AbstractDungeon.player.orbs.get((int)i))).upgraded && card != null) {
                card.upgrade();
            }
            if (card != null) {
                card.cardATK = ((AbstractYGOMonster)((Object)AbstractDungeon.player.orbs.get((int)i))).ATK;
                card.cardDEF = ((AbstractYGOMonster)((Object)AbstractDungeon.player.orbs.get((int)i))).DEF;
            }
            // for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            //     if (!(orb instanceof AbstractYGOMonster) || !(orb instanceof AkiRosenthal) || ((AkiRosenthal)orb).upgraded || card == null) continue;
            //     ++card.EffectTimes;
            // }
            if (card != null) {
                switch (id) {
                    case "Hololive_ILoveYou": {
                        card.MagicNum = magicnum1;
                        card.Index = i;
                        card.Effectnum = 1;
                        break;
                    }
                    case "Hololive_CocoNews": {
                        card.MagicNum = magicnum1;
                        card.Index = i;
                        card.Effectnum = 2;
                        break;
                    }
                    case "Hololive_Recruit": {
                        card.Effectnum = 0;
                        break;
                    }
                    case "Hololive_RecruitS": {
                        card.Effectnum = 0;
                        break;
                    }
                    case "Hololive_StarCraft": {
                        card.Index = i;
                        card.Effectnum = 5;
                        break;
                    }
                    case "Hololive_Psychosis": {
                        card.MagicNum = magicnum1;
                        card.Index = i;
                        card.Effectnum = 6;
                    }
                }
            }
            abstractCards.add((AbstractCard)card);
        }
        return abstractCards;
    }

    public static ArrayList<AbstractCard> ChooseOneMinionGroup(String id, boolean HasShion) {
        ArrayList<AbstractCard> abstractCards = new ArrayList<AbstractCard>();
        for (int i = AbstractDungeon.player.orbs.size() - 1; i > -1; --i) {
            if (!(AbstractDungeon.player.orbs.get(i) instanceof AbstractYGOMonster)) continue;
            AbstractMonsterCard card = OrbHelper.GetCardInstance(i);
            if (((AbstractYGOMonster)((Object)AbstractDungeon.player.orbs.get((int)i))).upgraded && card != null) {
                card.upgrade();
            }
            if (card != null) {
                card.cardATK = ((AbstractYGOMonster)((Object)AbstractDungeon.player.orbs.get((int)i))).ATK;
                card.cardDEF = ((AbstractYGOMonster)((Object)AbstractDungeon.player.orbs.get((int)i))).DEF;
            }
            if (card != null) {
                card.MagicNum = HasShion ? 1 : 0;
                card.Index = i;
                switch (id) {
                    case "Hololive_Teleport": {
                        card.Effectnum = 3;
                        break;
                    }
                    case "Hololive_Nanodesu": {
                        card.Effectnum = 4;
                        break;
                    }
                }
            }
            abstractCards.add((AbstractCard)card);
        }
        return abstractCards;
    }

    public static ArrayList<AbstractCard> ChooseOneMinionGroup(String id, int ATK, int DEF) {
        ArrayList<AbstractCard> abstractCards = new ArrayList<AbstractCard>();
        for (int i = AbstractDungeon.player.orbs.size() - 1; i > -1; --i) {
            if (!(AbstractDungeon.player.orbs.get(i) instanceof AbstractYGOMonster)) continue;
            AbstractMonsterCard card = OrbHelper.GetCardInstance(i);
            if (((AbstractYGOMonster)((Object)AbstractDungeon.player.orbs.get((int)i))).upgraded && card != null) {
                card.upgrade();
            }
            if (card != null) {
                card.cardATK = ((AbstractYGOMonster)((Object)AbstractDungeon.player.orbs.get((int)i))).ATK;
                card.cardDEF = ((AbstractYGOMonster)((Object)AbstractDungeon.player.orbs.get((int)i))).DEF;
            }
            if (card != null) {
                card.Index = i;
                switch (id) {
                    case "Hololive_StrengthChampion": {
                        card.Effectnum = 7;
                        card.MagicNum = ATK;
                        card.MagicNum2 = DEF;
                        break;
                    }
                }
            }
            abstractCards.add((AbstractCard)card);
        }
        return abstractCards;
    }

    public static AbstractYGOMonster GetRandomMinion() {
        ArrayList<AbstractYGOMonster> minions = new ArrayList<AbstractYGOMonster>();
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (!(orb instanceof AbstractYGOMonster)) continue;
            minions.add((AbstractYGOMonster)orb);
        }
        if (minions.size() == 0) {
            return null;
        }
        return (AbstractYGOMonster)((Object)minions.get(AbstractDungeon.cardRng.random(0, minions.size() - 1)));
    }

    public static AbstractMonsterCard GetCardInstance(int i) {
        try {
            String id = ((AbstractOrb)AbstractDungeon.player.orbs.get((int)i)).ID.replace("Hololive_", "hololivemod.cards.summonCard.Call");
            return (AbstractMonsterCard)((Object)Class.forName(id).newInstance());
        }
        catch (ClassNotFoundException var2) {
            var2.printStackTrace();
        }
        catch (IllegalAccessException var3) {
            var3.printStackTrace();
        }
        catch (InstantiationException var5) {
            var5.printStackTrace();
        }
        return null;
    }

    public static AbstractMonsterCard GetCardInstance(AbstractYGOMonster m) {
        try {
            String id = m.ID.replace("Monster_", "yuseimod.cards.monsterCard.");
            return (AbstractMonsterCard)((Object)Class.forName(id).newInstance());
        }
        catch (ClassNotFoundException var2) {
            var2.printStackTrace();
        }
        catch (IllegalAccessException var3) {
            var3.printStackTrace();
        }
        catch (InstantiationException var5) {
            var5.printStackTrace();
        }
        return null;
    }
}

