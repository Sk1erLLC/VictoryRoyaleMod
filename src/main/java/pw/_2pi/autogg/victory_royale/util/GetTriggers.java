package pw._2pi.autogg.victory_royale.util;

import org.apache.commons.io.IOUtils;
import pw._2pi.autogg.victory_royale.gg.AutoGG;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class GetTriggers implements Runnable
{
    @Override
    public void run() {
        try {
            final String rawTriggers = IOUtils.toString(new URL("https://gist.githubusercontent.com/minemanpi/72c38b0023f5062a5f3eba02a5132603/raw/triggers.txt"));
            AutoGG.getInstance().setTriggers(new ArrayList<>(Arrays.asList(rawTriggers.split("\n"))));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
