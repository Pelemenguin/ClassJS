package pelemenguin.classjs;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import pelemenguin.classjs.probe.ClassJSProbeJSPlugin;

@Mod(ClassJS.MOD_ID)
public class ClassJS {
    public static final String MOD_ID = "classjs";

    public static final Logger LOGGER = LogUtils.getLogger();

    public ClassJS() {
        ModList modlist = ModList.get();

        if (modlist.isLoaded("probejs")) {
            String probeJSVersion = modlist.getModFileById("probejs").versionString();
            int majorVersion = -1;
            try {
                majorVersion = Integer.parseInt(probeJSVersion.split("\\.")[0]);
            } catch (Exception e) {
                LOGGER.error("Something wrong when getting probejs version. ", e);
            }
            if (majorVersion == 6) {
                LOGGER.info("ProbeJS " + probeJSVersion + " found! Extra typing support is provided for ClassJS.");
                ClassJSProbeJSPlugin.onConstruct();
            }
            LOGGER.info("ProbeJS found but at version " + probeJSVersion + ", no extra typing support provided for ClassJS.");
        }
    }
}
