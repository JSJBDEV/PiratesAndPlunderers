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
    var DimensionEngagementSpacingBlocks: Int;
    var DimensionEngagmentLimit: Int;

    init {
        val configFile = File(configPath);
        val conf = ConfigFactory
                .parseFile(configFile)
                .withFallback(ConfigFactory.load(defaultConfigResourcePath))

        /**
         * Set the variable here
         */
        DimensionHasFancySky = conf.getBoolean("dimension.fancySky")
        DimensionEngagementSpacingBlocks = conf.getInt("dimension.engagements.spacing")
        DimensionEngagmentLimit = conf.getInt("dimension.engagements.limit")

        logger.info("Loaded Configuration")

        copyFileIfNotExists()
    }

    private fun copyFileIfNotExists() {
        val file = File(configPath)
        logger.info(file)
        if (!file.exists()) {
            try {
                this.javaClass.getResourceAsStream("/$defaultConfigResourcePath").use { stream ->
                    Files.copy(stream!!, Paths.get(configPath))
                }
            } catch (e: IOException) {
                logger.fatal("Could not copy config from JAR resources (IOException), any custom config will not be loaded!", e)
            } catch (e: NullPointerException) {
                logger.error("Could not copy config from JAR resources (NullPointerException), any custom config will not be loaded!", e)
            }
        }
    }

    companion object {
        private const val defaultConfigResourcePath = "defaultConfig.conf"
        private const val configPath = "PiratesAndPlunderers.conf"
    }
}