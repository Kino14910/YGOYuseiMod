package yuseimod;

import static yuseimod.characters.Yusei.PlayerColorEnum.YUSEI;
import static yuseimod.utils.ModHelper.addToBot;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.AddAudioSubscriber;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import me.antileaf.signature.utils.SignatureHelper;
import yuseimod.YGOMonsters.AbstractYGOMonster;
import yuseimod.cards.YuseiCard;
import yuseimod.characters.CustomEnums.YGOCardColor;
import yuseimod.characters.Yusei;
import yuseimod.relics.BaseRelic;
import yuseimod.utils.GeneralUtils;
import yuseimod.utils.KeywordInfo;
import yuseimod.utils.OrbHelper;
import yuseimod.utils.Sounds;
import yuseimod.utils.TextureLoader;

@SpireInitializer
public class YuseiMod implements
        EditCharactersSubscriber,
        EditCardsSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        PostInitializeSubscriber,
        OnPlayerDamagedSubscriber,
        OnStartBattleSubscriber
         {
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    private static final String resourcesFolder = checkResourcesPath();
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.

    private static final String YUSEI_BUTTON = charPath("YuseiCharBtn");
    private static final String YUSEI_PORTRAIT = charPath("YuseiPortrait");
    
    
    /*----------Create new Color----------*/
    public static final String CARD_ENERGY_ORB = uiPath("energyOrb");
    public static final String NOBLE_ENERGY_ORB = uiPath("energyOrb");
    public static final Color SILVER = CardHelper.getColor(200, 200, 200);
    public static final Color NOBLE = CardHelper.getColor(255, 215, 0);

    //默认卡牌背景
    private static final String DEFAULT_CC = imagePath("512/bg_master_s");
    private static final String DEFAULT_CC_PORTRAIT = imagePath("1024/bg_master");
    private static final String ENERGY_ORB_CC = imagePath("512/MASTEROrb");
    private static final String ENERGY_ORB_CC_PORTRAIT = imagePath("1024/MASTEROrb");

    private int dmg;
    public static SpireConfig config;
    
    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new YuseiMod();
    }

    public YuseiMod() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        BaseMod.addColor(YGOCardColor.Monster, SILVER, DEFAULT_CC, DEFAULT_CC, DEFAULT_CC, ENERGY_ORB_CC, DEFAULT_CC_PORTRAIT, DEFAULT_CC_PORTRAIT, DEFAULT_CC_PORTRAIT, ENERGY_ORB_CC_PORTRAIT, CARD_ENERGY_ORB);
        BaseMod.addColor(YGOCardColor.SynchroMonster, SILVER, DEFAULT_CC, DEFAULT_CC, DEFAULT_CC, ENERGY_ORB_CC, DEFAULT_CC_PORTRAIT, DEFAULT_CC_PORTRAIT, DEFAULT_CC_PORTRAIT, ENERGY_ORB_CC_PORTRAIT, CARD_ENERGY_ORB);
        logger.info(modID + " subscribed to BaseMod.");
    }

    @Override
    public void receivePostInitialize() {
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.

        //If you want to set up a config panel, that will be done here.
        //You can find information about this on the BaseMod wiki page "Mod Config and Panel".
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
        new AutoAdd(YuseiMod.modID)
            .packageFilter("yuseimod.cards")
            .any(AbstractCard.class, (info, card) -> {SignatureHelper.unlock(card.cardID, true);
        });
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            keyword.prep();
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try
            {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            }
            catch (Exception e)
            {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION, info.COLOR);
        if (!info.ID.isEmpty())
        {
            keywords.put(info.ID, info);
        }
    }

    @Override
    public void receiveAddAudio() {
        loadAudio(Sounds.class);
    }

    private static final String[] AUDIO_EXTENSIONS = { ".ogg", ".wav", ".mp3" }; //There are more valid types, but not really worth checking them all here
    private void loadAudio(Class<?> cls) {
        try {
            Field[] fields = cls.getDeclaredFields();
            outer:
            for (Field f : fields) {
                int modifiers = f.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && f.getType().equals(String.class)) {
                    String s = (String) f.get(null);
                    if (s == null) { //If no defined value, determine path using field name
                        s = audioPath(f.getName());

                        for (String ext : AUDIO_EXTENSIONS) {
                            String testPath = s + ext;
                            if (Gdx.files.internal(testPath).exists()) {
                                s = testPath;
                                BaseMod.addAudio(s, s);
                                f.set(null, s);
                                continue outer;
                            }
                        }
                        throw new Exception("Failed to find an audio file \"" + f.getName() + "\" in " + resourcesFolder + "/audio; check to ensure the capitalization and filename are correct.");
                    }
                    else { //Otherwise, load defined path
                        if (Gdx.files.internal(s).exists()) {
                            BaseMod.addAudio(s, s);
                        }
                        else {
                            throw new Exception("Failed to find audio file \"" + s + "\"; check to ensure this is the correct filepath.");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("Exception occurred in loadAudio: ", e);
        }
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String audioPath(String file) {
        return resourcesFolder + "/audio/" + file;
    }
    public static String musicPath(String file) {
        return audioPath("music/" + file);
    }
    public static String soundPath(String file) {
        return audioPath("sound/" + file);
    }
    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file + ".png";
    }
    public static String charPath(String file) {
        return imagePath("char/" + file);
    }
    public static String monsterPath(String file) {
        return imagePath("monsters/" + file);
    }
    public static String powerPath(String file) {
        return imagePath("powers/" + file);
    }
    public static String relicPath(String file) {
        return imagePath("relics/" + file);
    }
    public static String uiPath(String file) {
        return imagePath("ui/" + file);
    }
    public static String vfxPath(String file) {
        return imagePath("vfx/" + file);
    }
    public static String cardPath(String file) {
        return imagePath("cards/" + file);
    }
    public static String eventPath(String file) {
        return imagePath("events/" + file);
    }
    public static String orbPath(String file) {
        return imagePath("orbs/" + file.replace(makeID("Monster_"), ""));
    }


    /**
     * Checks the expected resources path based on the package name.
     */
    private static String checkResourcesPath() {
        String name = YuseiMod.class.getName(); //getPackage can be iffy with patching, so class name is used instead.
        int separator = name.indexOf('.');
        if (separator > 0)
            name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be at  \"resources/" + name + "\"." +
                    " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                    "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                    "\tat the top of the " + YuseiMod.class.getSimpleName() + " java file.");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }

        return name;
    }

    /**
     * This determines the mod's ID based on information stored by ModTheSpire.
     */
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(YuseiMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    
    @Override
    public void receiveEditCharacters() {
        //添加角色到MOD中
        BaseMod.addCharacter(new Yusei("Yusei"), YUSEI_BUTTON, YUSEI_PORTRAIT, YUSEI);
    }

    @Override
    public void receiveEditCards() { //somewhere in the class
        AutoAdd autoAdd = new AutoAdd(modID);
        //Loads files from this mod
        autoAdd.packageFilter(YuseiCard.class); //In the same package as this class
        autoAdd.setDefaultSeen(true) //And marks them as seen in the compendium
                .cards(); //Adds the cards
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(BaseRelic.class)
                .any(BaseRelic.class, (info, relic) -> { //Run this code for any classes that extend this class
                    if (relic.pool != null)
                        BaseMod.addRelicToCustomPool(relic, relic.pool); //Register a custom character specific relic
                    else
                        BaseMod.addRelic(relic, relic.relicType); //Register a shared or base game character specific relic

                    //If the class is annotated with @AutoAdd.Seen, it will be marked as seen, making it visible in the relic library.
                    //If you want all your relics to be visible by default, just remove this if statement.
    //                if (info.seen)
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                });
    }

    private boolean isYusei() {
        return AbstractDungeon.player instanceof Yusei;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        if(isYusei()) {
            addToBot(new DrawCardAction(4));
            OrbHelper.CallMinionsThisBattle = 0;
            OrbHelper.DeadMinionsThisBattle.clear();
        }
    }

    @Override
    public int receiveOnPlayerDamaged(int amount, DamageInfo info) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.orbs.size() > 0) {
            boolean overBlock = false;
            this.dmg = amount;
            if (AbstractDungeon.player.currentBlock > 0) {
                if (this.dmg - AbstractDungeon.player.currentBlock <= 0) {
                    AbstractDungeon.player.loseBlock(this.dmg);
                    return 0;
                }
                this.dmg -= AbstractDungeon.player.currentBlock;
                if (AbstractDungeon.player.hasPower("Plated Armor")) {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ReducePowerAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)AbstractDungeon.player, "Plated Armor", 1));
                }
                overBlock = true;
            }
            logger.info("\u5148\u653b\u51fb\u5632\u8bbd\u961f\u53cb");
            boolean hasMate = false;
            for (AbstractOrb o : AbstractDungeon.player.orbs) {
                if (!(o instanceof AbstractYGOMonster)) continue;
                hasMate = true;
                break;
            }
            if (hasMate) {
                for (int j = 0; j < AbstractDungeon.player.maxOrbs; ++j) {
                    int i;
                    if (AbstractDungeon.player.orbs.get(j) instanceof AbstractYGOMonster) continue;
                    ((AbstractOrb)AbstractDungeon.player.orbs.get(j)).onEvoke();
                    EmptyOrbSlot orbSlot = new EmptyOrbSlot();
                    for (i = j + 1; i < AbstractDungeon.player.orbs.size(); ++i) {
                        Collections.swap(AbstractDungeon.player.orbs, i, i - 1);
                    }
                    AbstractDungeon.player.orbs.set(AbstractDungeon.player.orbs.size() - 1, orbSlot);
                    for (i = j; i < AbstractDungeon.player.orbs.size(); ++i) {
                        ((AbstractOrb)AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
                    }
                }
            }
            this.onAttackAssitTaunt();
            this.onAttackAssit();
            // for (AbstractOrb o : AbstractDungeon.player.orbs) {
            //     if (!(o instanceof OmaruPolka)) continue;
            //     ((OmaruPolka)o).refreshOrbs();
            // }
            try {
                int n = this.dmg;
                return n;
            }
            finally {
                if (overBlock) {
                    AbstractDungeon.player.loseBlock(AbstractDungeon.player.currentBlock);
                }
            }
        }
        return amount;
    }
    
    private void onAttackAssit() {
        logger.info("onAttackAssit");
        int j = 0;
        AbstractOrb orb = (AbstractOrb)AbstractDungeon.player.orbs.get(j);
        logger.info("onAttackAssit\uff1a\u9009\u53d6" + orb.name);
        if (orb instanceof AbstractYGOMonster) {
            AbstractYGOMonster student = (AbstractYGOMonster)orb;
            logger.info("onAttackAssit\uff1a\u73b0\u5728\u53d7\u5230\u4f24\u5bb3\u7684\u662f" + student.name);
            if (student.DEF > this.dmg) {
                student.ChangeDEF(-this.dmg, true);
                student.onDamaged(this.dmg, true);
                this.dmg = 0;
                logger.info("onAttackAssit:\u961f\u53cb\u625b\u4e0b\u4e86\u4f24\u5bb3");
            } else {
                int i;
                student.onDamaged(student.DEF, true);
                this.dmg -= student.DEF;
                student.DEF = 0;
                student.isdead = true;
                // if (TheStar.CombinationIndex[24]) {
                //     student.AttackEffect();
                // }
                // if (student instanceof AiraniIofifteen) {
                //     this.dmg = 0;
                // }
                student.onEvoke();
                logger.info("onAttackAssit:\u961f\u53cb\u6ca1\u625b\u4e0b\u4f24\u5bb3\uff0c\u6b7b\u4e86");
                EmptyOrbSlot orbSlot = new EmptyOrbSlot();
                for (i = 1; i < AbstractDungeon.player.orbs.size(); ++i) {
                    Collections.swap(AbstractDungeon.player.orbs, i, i - 1);
                }
                AbstractDungeon.player.orbs.set(AbstractDungeon.player.orbs.size() - 1, orbSlot);
                for (i = 0; i < AbstractDungeon.player.orbs.size(); ++i) {
                    ((AbstractOrb)AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
                }
                this.onAttackAssit();
            }
        }
    }

    private void onAttackAssitTaunt() {
        logger.info("onAttackAssitTaunt");
        for (int j = 0; j < AbstractDungeon.player.orbs.size(); ++j) {
            int i;
            AbstractOrb orb = (AbstractOrb)AbstractDungeon.player.orbs.get(j);
            logger.info("onAttackAssitTaunt\uff1a\u9009\u53d6" + orb.name);
            if (!(orb instanceof AbstractYGOMonster) || !((AbstractYGOMonster)orb).Taunt) continue;
            logger.info("onAttackAssitTaunt\uff1a\u73b0\u5728\u53d7\u5230\u4f24\u5bb3\u7684\u662f" + orb.name);
            if (((AbstractYGOMonster)orb).DEF > this.dmg) {
                ((AbstractYGOMonster)orb).ChangeDEF(-this.dmg, true);
                ((AbstractYGOMonster)orb).onDamaged(this.dmg, true);
                this.dmg = 0;
                logger.info("onAttackAssitTaunt:\u961f\u53cb\u625b\u4e0b\u4e86\u4f24\u5bb3");
                return;
            }
            ((AbstractYGOMonster)orb).onDamaged(((AbstractYGOMonster)orb).DEF, true);
            this.dmg -= ((AbstractYGOMonster)orb).DEF;
            ((AbstractYGOMonster)orb).DEF = 0;
            ((AbstractYGOMonster)orb).isdead = true;
            // if (TheStar.CombinationIndex[24]) {
            //     ((AbstractYGOMonster)orb).AttackEffect();
            // }
            // if (orb instanceof AiraniIofifteen) {
            //     this.dmg = 0;
            // }
            orb.onEvoke();
            logger.info("onAttackAssitTaunt:\u961f\u53cb\u6ca1\u625b\u4e0b\u4f24\u5bb3\uff0c\u6b7b\u4e86");
            EmptyOrbSlot orbSlot = new EmptyOrbSlot();
            for (i = j + 1; i < AbstractDungeon.player.orbs.size(); ++i) {
                Collections.swap(AbstractDungeon.player.orbs, i, i - 1);
            }
            AbstractDungeon.player.orbs.set(AbstractDungeon.player.orbs.size() - 1, orbSlot);
            for (i = j; i < AbstractDungeon.player.orbs.size(); ++i) {
                ((AbstractOrb)AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
            }
            this.onAttackAssitTaunt();
            break;
        }
    }

}
