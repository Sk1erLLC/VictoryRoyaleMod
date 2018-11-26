package pw._2pi.autogg.victory_royale.gg;

import club.sk1er.mods.victory_royale.VictoryRoyale;

public class GGThread implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(250L);
            VictoryRoyale.getInstance().gameEnded();
            AutoGG.getInstance().setRunning(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
