package gd.rf.acro.pap.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import gd.rf.acro.pap.PiratesAndPlunderers.logger;
import java.io.FileWriter

/**
 * This class contains config values for the mod.
 * Add the values here AND in src/main/resources/defaultConfig.conf !!
 */
class ConfigLoader {
    /**
     * Add the public variable here, eg: `var MyConfigVariable: String;`
     */

    var DimensionHasFancySky: Boolean;

    init {
        val configFile = File(configPath);
        val conf = ConfigFactory
                .parseFile(configFile)
                .withFallback(ConfigFactory.load(defaultConfigResourcePath))

        /**
         * Set the variable here
         */
        DimensionHasFancySky = conf.getBoolean("dimension.fancySky")

        copyFileIfNotExists()
    }

    private fun copyFileIfNotExists() {
        val file = File(configPath)
        if (!file.exists()) {
            try {
                this.javaClass.classLoader.getResourceAsStream("/$defaultConfigResourcePath").use { stream -> Files.copy(stream!!, Paths.get(configPath)) }
            } catch (e: IOException) {
                logger.fatal("Could not copy config from JAR resources, this is a non-recoverable error!")
                throw e;
            }
        }
    }

    companion object {
        private const val defaultConfigResourcePath = "defaultConfig.conf"
        private const val configPath = "PiratesAndPlunderers.conf"
    }
}