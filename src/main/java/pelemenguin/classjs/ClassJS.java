package pelemenguin.classjs;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.fml.common.Mod;

@Mod(value = ClassJS.MOD_ID)
public class ClassJS {
    public static final String MOD_ID = "classjs";

    public static final Logger LOGGER = LogUtils.getLogger();

    public ClassJS() {
    }
}
