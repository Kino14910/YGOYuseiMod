package yuseimod.ui.panels;

import static yuseimod.YuseiMod.makeID;

import basemod.EasyConfigPanel;
import yuseimod.YuseiMod;

public class YGOConfig extends EasyConfigPanel {
    public static boolean enableColorlessCards = true;


    public YGOConfig() {
        super(YuseiMod.modID, makeID(YGOConfig.class.getSimpleName()));
    }

}