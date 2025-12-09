package yuseimod.YGOMonsters;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import basemod.abstracts.CustomOrb;
import yuseimod.abilities.AbstractAbility;
import yuseimod.cards.monsterCard.AbstractMonsterCard;
import yuseimod.utils.OrbHelper;

public abstract class AbstractYGOMonster extends CustomOrb {
    public int DEF = 0;
    public int ATK = 0;
    public int level = 0;
    public boolean Taunt = false;
    public boolean upgraded = false;
    public boolean isdead = false;
    public boolean isTuner = false;
    public boolean isSynchro = false;
    public ArrayList<AbstractAbility> onDamageAbilities = new ArrayList();
    public ArrayList<AbstractAbility> onEvokeAbilities = new ArrayList();
    public ArrayList<AbstractAbility> onAttackAbilities = new ArrayList();
    public String DESCRIPTION;
    public String UPGRADE_DESCRIPTION;
    private float DamageTimer = 0.3f;
    private float HPFontScale;
    private float ATKFontScale;
    private float TriggerScale = 1.0f;
    private static String Atk;
    private static String Def;
    private static Texture TauntImg;

    public AbstractYGOMonster(String ID, String NAME, int basePassiveAmount, int baseEvokeAmount, int DEF, String IMG_PATH, String DESCRIPTION, String UPGRADE_DESCRIPTION) {
        super(ID, NAME, basePassiveAmount, baseEvokeAmount, null, null, IMG_PATH);
        this.DEF = DEF;
        this.DESCRIPTION = DESCRIPTION;
        this.UPGRADE_DESCRIPTION = UPGRADE_DESCRIPTION;
        this.HPFontScale = this.fontScale;
        this.ATKFontScale = this.fontScale;
        this.updateDescription();
    }
    
    public boolean isTuner() {
        return isTuner;
    }

    public void setTuner() {
        this.isTuner = true;
    }

    
    public boolean isSynchro() {
        return isSynchro;
    }

    public void setSynchro() {
        this.isSynchro = true;
    }


    @Override
    public void onEvoke() {
        AbstractMonsterCard card;
        // for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
        //     for (AbstractPower p : m.powers) {
        //         if (!(p instanceof FlarePower) || !this.isdead) continue;
        //         ((FlarePower)p).Effect();
        //     }
        // }
        if (this.onEvokeAbilities != null) {
            for (AbstractAbility ability : this.onEvokeAbilities) {
                ability.Effect();
            }
        }
        // if (this.isdead) {
        //     OrbHelper.DeadMinionsThisBattle.add(this);
        //     for (AbstractOrb orb : AbstractDungeon.player.orbs) {
        //         if (!(orb instanceof OkamiMio)) continue;
        //         if (!((OkamiMio)orb).upgraded) {
        //             ((OkamiMio)orb).ChangeATK(1, false);
        //             ((OkamiMio)orb).ChangeHP(2, false);
        //             continue;
        //         }
        //         ((OkamiMio)orb).ChangeATK(2, false);
        //         ((OkamiMio)orb).ChangeHP(3, false);
        //     }
        // }
        if (AbstractDungeon.player.hasRelic("Hololive_TheOnlyOne") && (card = OrbHelper.GetCardInstance(this)) != null) {
            AbstractDungeon.player.masterDeck.removeCard(((AbstractCard)card).cardID);
        }
    }

    public void onCardUse(AbstractCard c) {
    }

    public void onCardUseEffect() {
    }

    public void onDamaged(int dmg, boolean Effect) {
        if (dmg != 0) {
            if (this.onDamageAbilities != null) {
                for (AbstractAbility ability : this.onDamageAbilities) {
                    ability.Effect();
                }
            }
            if (Effect) {
                AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(this.cX, this.cY, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
            this.DamageTimer = 0.0f;
        }
    }

    public void ChangeHP(int DeltaHP, boolean isDamage) {
        this.DEF += DeltaHP;
        if (DeltaHP > 0 && this.HPFontScale < 4.0f) {
            this.HPFontScale += 2.3f;
        }
    }

    public void ChangeATK(int DeltaATK, boolean isDamage) {
        this.ATK += DeltaATK;
        if (DeltaATK > 0 && this.ATKFontScale < 4.0f) {
            this.ATKFontScale += 2.3f;
        }
    }

    public void ChangeStat(int ATK, int HP, boolean isDamage) {
        this.DEF += HP;
        this.ATK += ATK;
        if (HP > 0 && this.HPFontScale < 4.0f) {
            this.HPFontScale += 2.3f;
        }
        if (ATK > 0 && this.ATKFontScale < 4.0f) {
            this.ATKFontScale += 2.3f;
        }
    }

    public void onCallMinion(boolean Left) {
    }

    public void Trigger() {
        if (this.TriggerScale < 2.5f) {
            this.TriggerScale += 0.5f;
        }
    }

    public void onEndOfTurnEffect() {
    }

    public void AttackEffect() {
        if (this.onAttackAbilities != null) {
            for (AbstractAbility ability : this.onAttackAbilities) {
                ability.Effect();
            }
        }
        // for (AbstractOrb orb : AbstractDungeon.player.orbs) {
        //     if (!(orb instanceof AbstractYGOMonster) || !(orb instanceof SakuraMiko)) continue;
        //     AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new GainGoldAction(1));
        // }
    }

    public void Attack(AbstractGameAction.AttackEffect effect) {
        AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (!(m == null || m.isDeadOrEscaped() || m.halfDead || m.isDead)) {
            this.Attack(m, effect);
        }
    }

    public void Attack(AbstractMonster m, AbstractGameAction.AttackEffect effect) {
        float dmg = this.ATK;
        if (!(m == null || m.isDeadOrEscaped() || m.halfDead || m.isDead)) {
            for (AbstractPower p : m.powers) {
                dmg = p.atDamageReceive(dmg, DamageInfo.DamageType.NORMAL);
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof VigorPower || p instanceof PenNibPower) continue;
            dmg = p.atDamageGive(dmg, DamageInfo.DamageType.NORMAL);
        }
        if (!(m == null || m.isDeadOrEscaped() || m.halfDead || m.isDead || AbstractDungeon.player.hasRelic("Hololive_Yo"))) {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)m, new DamageInfo((AbstractCreature)AbstractDungeon.player, (int)dmg, DamageInfo.DamageType.NORMAL), effect));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.DamageTimer < 0.3f) {
            sb.setColor(1.0f, 0.1f, 0.1f, 1.0f);
            this.DamageTimer += Gdx.graphics.getDeltaTime();
        } else {
            sb.setColor(this.c);
        }
        if (this.HPFontScale > 0.7f) {
            this.HPFontScale -= Gdx.graphics.getDeltaTime() * this.HPFontScale * 2.0f;
        }
        if (this.ATKFontScale > 0.7f) {
            this.ATKFontScale -= Gdx.graphics.getDeltaTime() * this.ATKFontScale * 2.0f;
        }
        if (this.TriggerScale > 1.0f) {
            this.TriggerScale -= Gdx.graphics.getDeltaTime() * this.TriggerScale * 1.5f;
        }
        sb.draw(this.img, this.cX - 48.0f + this.bobEffect.y / 4.0f, this.cY - 48.0f + this.bobEffect.y / 4.0f, 48.0f, 48.0f, 96.0f, 96.0f, this.scale * this.TriggerScale, this.scale * this.TriggerScale, 0.0f, 0, 0, 96, 96, false, false);
        if (this.Taunt) {
            sb.draw(TauntImg, this.cX - 75.0f + this.bobEffect.y / 4.0f, this.cY - 75.0f + this.bobEffect.y / 4.0f, 75.0f, 75.0f, 150.0f, 150.0f, this.scale * this.TriggerScale, this.scale * this.TriggerScale, 0.0f, 0, 0, 150, 150, false, false);
        }
        this.renderText(sb);
        this.updateDescription();
        this.hb.render(sb);
    }

    @Override
    public void updateDescription() {
        this.applyFocus();
        String abilitydescription = "";
        if (this.onDamageAbilities != null) {
            for (AbstractAbility ability : this.onDamageAbilities) {
                abilitydescription = " NL " + abilitydescription.concat(ability.getDescription());
            }
        }
        if (this.onEvokeAbilities != null) {
            for (AbstractAbility ability : this.onEvokeAbilities) {
                abilitydescription = " NL " + abilitydescription.concat(ability.getDescription());
            }
        }
        if (this.onAttackAbilities != null) {
            for (AbstractAbility ability : this.onAttackAbilities) {
                abilitydescription = " NL " + abilitydescription.concat(ability.getDescription());
            }
        }
        this.description = Atk + this.ATK + Def + this.DEF + " NL " + (!this.upgraded ? this.DESCRIPTION : this.UPGRADE_DESCRIPTION) + abilitydescription;
    }

    @Override
    public void renderText(SpriteBatch sb) {
        FontHelper.renderFontCentered((SpriteBatch)sb, (BitmapFont)FontHelper.cardEnergyFont_L, (String)Integer.toString(this.ATK), (float)(this.cX - NUM_X_OFFSET * 1.8f), (float)(this.cY + this.bobEffect.y / 2.0f + NUM_Y_OFFSET - 30.0f), (Color)new Color(1.0f, 1.0f, 0.4f, 1.0f), (float)this.ATKFontScale);
        FontHelper.renderFontCentered((SpriteBatch)sb, (BitmapFont)FontHelper.cardEnergyFont_L, (String)"/", (float)this.cX, (float)(this.cY + this.bobEffect.y / 2.0f + NUM_Y_OFFSET - 30.0f), (Color)new Color(1.0f, 1.0f, 1.0f, 1.0f), (float)this.fontScale);
        FontHelper.renderFontCentered((SpriteBatch)sb, (BitmapFont)FontHelper.cardEnergyFont_L, (String)Integer.toString(this.DEF), (float)(this.cX + NUM_X_OFFSET * 1.8f), (float)(this.cY + this.bobEffect.y / 2.0f + NUM_Y_OFFSET - 30.0f), (Color)new Color(1.0f, 0.5f, 0.5f, 1.0f), (float)this.HPFontScale);
        if (this.upgraded) {
            FontHelper.renderFontCentered((SpriteBatch)sb, (BitmapFont)FontHelper.cardEnergyFont_L, (String)"+", (float)this.cX, (float)(this.cY + this.bobEffect.y / 2.0f + NUM_Y_OFFSET + 70.0f * Settings.scale), (Color)new Color(1.0f, 1.0f, 1.0f, 1.0f), (float)(this.fontScale * 1.6f));
        }
    }

    static {
        if (Settings.language == Settings.GameLanguage.ZHS) {
            Atk = "\u653b\u51fb\uff1a";
            Def = "\u751f\u547d\uff1a";
        } else {
            Atk = "ATK:";
            Def = "     HP:";
        }
        TauntImg = ImageMaster.loadImage((String)"img/UI/students/TauntImg.png");
    }
}

