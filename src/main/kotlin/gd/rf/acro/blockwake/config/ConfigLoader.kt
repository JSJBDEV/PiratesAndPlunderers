package gd.rf.acro.blockwake.config

import com.typesafe.config.ConfigFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import gd.rf.acro.blockwake.Blockwake.logger;
import java.awt.Dimension

/**
 * This class contains config values for the mod.
 * Add the values here AND in src/main/resources/defaultConfig.conf !!
 */
class ConfigLoader {
    /**
     * Add the public variable here, eg: `var MyConfigVariable: String;`
     */

    val DimensionHasFancySky: Boolean;
    val DimensionEngagementSpacingBlocks: Int;
    val DimensionEngagmentLimit: Int;
    val DimensionMinSpacing: Int;
    val DimensionMaxSpacing: Int;
    val DimensionTeleportDelay: Long;

    val RecruitmentSearchRange: Int;
    val VillagerRecruitmentChance: Int;

    init {
        val configFile = File(configPath);
        val conf = ConfigFactory
                .parseFile(configFile)
                .withFallback(ConfigFactory.load(defaultConfigResourcePath))

        /**
         * Set the variable here
         */
        DimensionHasFancySky = false//conf.getBoolean("dimension.fancySky")
        DimensionEngagementSpacingBlocks = conf.getInt("dimension.engagements.spacing.width")
        DimensionEngagmentLimit = conf.getInt("dimension.engagements.limit")
        DimensionMinSpacing = conf.getInt("dimension.engagements.spacing.min")
        DimensionMaxSpacing = conf.getInt("dimension.engagements.spacing.max")
        DimensionTeleportDelay = conf.getLong("dimension.engagements.teleport.delay")

        RecruitmentSearchRange = conf.getInt("recruitment.searchRange");
        VillagerRecruitmentChance = conf.getInt("recruitment.chanceMax");

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
        private const val configPath = "Blockwake.conf"
    }
}