package pw._2pi.autogg.victory_royale.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pw._2pi.autogg.victory_royale.gg.AutoGG;

import java.io.*;

public class ConfigUtil {

    private File configFile;

    public ConfigUtil(File configFile) {
        this.configFile = configFile;
        load();
    }

    public void load() {
        if (configFile.exists()) {
            try {
                FileReader fr = new FileReader(configFile);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                String s = builder.toString();
                JsonObject asJsonObject = new JsonParser().parse(s).getAsJsonObject();
                AutoGG.getInstance().setToggled(asJsonObject.has("toggled") && asJsonObject.get("toggled").getAsBoolean());

            } catch (Exception e) {

            }
        } else {

        }
    }

    public void save() {
        try {
            configFile.createNewFile();
            FileWriter fw = new FileWriter(configFile);
            BufferedWriter bw = new BufferedWriter(fw);
            JsonObject object = new JsonObject();
            object.addProperty("toggled", AutoGG.getInstance().isToggled());
            bw.write(object.toString());
            bw.close();
            fw.close();
        } catch (Exception e) {

        }
    }
}
