import command.WardrobeCommand
import command.WardrobeTabCompleter
import gui.Armor
import listener.CheckPlayerGuiListener
import listener.WardrobeListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import utils.DataManager

const val TAG = "[CustomWardrobe]"
val armors = arrayListOf(Armor.HELMET, Armor.CHESTPLATE, Armor.LEGGINGS, Armor.BOOTS)
class Wardrobe: JavaPlugin() {
    companion object {
        val IMPL: Wardrobe by lazy { Wardrobe() }
    }

    private lateinit var config: DataManager
    private lateinit var firstPage: DataManager
    private lateinit var secondPage: DataManager

    override fun onLoad() {
        logger.info("${ChatColor.GREEN}$TAG ${ChatColor.YELLOW}CustomWardrobe启动中……")
    }

    override fun onEnable() {
        logger.info("${ChatColor.GREEN}$TAG ${ChatColor.YELLOW}CustomWardrobe已启动！")
        config = DataManager(this).init("config.yml")
        firstPage = DataManager(this).init("page1.yml")
        secondPage = DataManager(this).init("page2.yml")
        getCommand("wardrobe")?.apply {
            setExecutor(WardrobeCommand.IMPL)
            tabCompleter = WardrobeTabCompleter.IMPL
        }
        Bukkit.getPluginManager().apply {
            registerEvents(WardrobeListener.IMPL, this@Wardrobe)
            registerEvents(CheckPlayerGuiListener.IMPL, this@Wardrobe)
        }
    }

    override fun onDisable() {
        Bukkit.getOnlinePlayers().forEach {
            it.openInventory.apply {
                if (title.contains(Regex("\\[([12])/2]"))) {
                    player.apply {
                        inventory.addItem(itemOnCursor)
                        setItemOnCursor(null)
                    }
                    it.closeInventory()
                }
            }
        }
        logger.info("${ChatColor.GREEN}$TAG ${ChatColor.YELLOW}CustomWardrobe已停止！")
    }

    fun getConfiguration() = config

    fun getFirstPage() = firstPage

    fun getSecondPage() = secondPage
}