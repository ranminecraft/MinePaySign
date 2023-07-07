package cc.ranmc;

import cc.ranmc.sign.SignApi;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Main extends JavaPlugin implements Listener {

    private static final String PLUGIN = "MinePaySign";

    @Getter
    private final String PREFIX = color("&b" + PLUGIN + ">>>");

    @Override
    public void onEnable() {
        print("&e-----------------------");
        print("&b" + PLUGIN + " &dBy阿然");
        print("&b插件版本:" + getDescription().getVersion());
        print("&b服务器版本:" + getServer().getVersion());
        print("&chttps://minepay.top/");
        print("&e-----------------------");

        //loadConfig();

        //注册Event
        Bukkit.getPluginManager().registerEvents(this, this);
        super.onEnable();
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             @NotNull Command cmd,
                             @NotNull String label,
                             String[] args) {

        if(!sender.hasPermission("mps.user")) {
            sender.sendMessage(PREFIX + color("&c没有权限"));
        }

        if (!(sender instanceof Player)) {
            print("&c该指令不能在控制台输入");
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("mps")) {
            if (args.length == 0 || (args.length == 1 && (args[0].equals("wechat") || args[0].equals("alipay")))) {
                SignApi.newMenu("请输入付款金额")
                        .response((p, strings) -> {
                            String input = (strings[0] + strings[1] + strings[2] + strings[3])
                                    .replace("请输入付款金额", "");
                            if (input.isEmpty()) {
                                p.sendMessage(PREFIX + color("&c输入金额为空"));
                            } else {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(this, () ->
                                        p.chat("/minepay point " + input + " " + (args.length == 0 ? "" : args[0])), 1);
                            }
                            return true;
                        }).open(player);
                return true;
            }
        }
        return true;
    }

    /**
     * 加载配置文件
     */
    private void loadConfig(){
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
    }

    /**
     * 文本颜色
     */
    private static String color(String text){
        return text.replace("&","§");
    }

    /**
     * 后台信息
     */
    public void print(String msg){
        Bukkit.getConsoleSender().sendMessage(color(msg));
    }

}
