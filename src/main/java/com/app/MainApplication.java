package com.app;

public class MainApplication {

    public static void main(String[] args) {
        LowLevel lowLevel = new LowLevel();
        LowLevel2 lowLevel2 = new LowLevel2();
        HighLevel highLevel = new HighLevel();
        highLevel.setLowLevel(lowLevel);
        highLevel.setLowLevel2(lowLevel2);
        System.out.println(highLevel.print());
    }

}
