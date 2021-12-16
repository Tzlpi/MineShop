package yad.mineshop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import yad.mineshop.listeners.*;
import yad.mineshop.managers.CommandManager;
import yad.mineshop.questsystem.QuestManager;
import yad.mineshop.questsystem.gui.RewardGUI;
import yad.mineshop.questsystem.gui.events.ProgressGUIClickEvent;
import yad.mineshop.questsystem.gui.events.QuestGUIClickEvent;
import yad.mineshop.questsystem.gui.events.RewardsGUIClickEvent;
import yad.mineshop.questsystem.quests.BlocksQuest;
import yad.mineshop.questsystem.quests.CowQuest;
import yad.mineshop.questsystem.quests.DiamondQuest;
import yad.mineshop.questsystem.quests.DragonQuest;
import yad.mineshop.tasks.Runnable;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.UUID;

public final class MineShop extends JavaPlugin {

    private Runnable runnable;
    private QuestManager questManager = new QuestManager(this);
    private static MineShop plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig(); // config

            //listeners

            registerListeners();

            //commands
            getCommand("mineshop").setExecutor(new CommandManager(this));

            // Runnable
            this.runnable = new Runnable(this);
            runnable.runTaskTimerAsynchronously(this, 20, 200);

            //temporary
            for(Player player : Bukkit.getOnlinePlayers()) {
                RewardsGUIClickEvent.confirmed.put(player, 0);
            }

    }

    @Override
    public void onDisable() {
        //Plugin shutdown logic.
    }

    public void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new CoalBreak(this, questManager), this);
        this.getServer().getPluginManager().registerEvents(new OnJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new DiamondBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new OnQuit(this), this);
        this.getServer().getPluginManager().registerEvents(new DiamondQuest(this, questManager), this);
        this.getServer().getPluginManager().registerEvents(new DragonQuest(questManager), this);
        this.getServer().getPluginManager().registerEvents(new CowQuest(questManager, this),this);
        this.getServer().getPluginManager().registerEvents(new BlocksQuest(this, questManager), this);
        this.getServer().getPluginManager().registerEvents(new QuestGUIClickEvent(questManager), this);
        this.getServer().getPluginManager().registerEvents(new ProgressGUIClickEvent(),this);
        this.getServer().getPluginManager().registerEvents(new RewardsGUIClickEvent(this, new RewardGUI(this)), this);
        this.getServer().getPluginManager().registerEvents(new InteractEvent(this), this);
    }

    public String getIp() {
        return getConfig().getString("ip");
    }

    public String readStringFromURL(String requestURL) throws IOException {
        if(!(Bukkit.getOnlinePlayers().isEmpty())) {
            try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                    StandardCharsets.UTF_8.toString())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } else {
            return "null";
        }
    }

    public String addPlayer(UUID uuid) {
        return  getIp() + "api/online_data/addplayer/" + uuid;
    }

    public String removePlayer(UUID uuid) {
        return getIp() +  "api/online_data/removeplayer/" + uuid;
    }

    public String register(UUID uuid, String password) {
        return getIp() + "api/main_data/registerplayer/" + uuid + "/" + password;
    }

    public String checkRegistered(UUID uuid) {
        return getIp() + "api/main_data/isplayerregistered/" + uuid;
    }

    public String addCoins(UUID uuid, double coins) {
        return getIp() + "api/main_data/" + uuid +"/addcoins/" + coins + "/";
    }

    public String removeCoins(Player player, double coins) {
        UUID uuid = player.getUniqueId();

        return getIp() + "api/main_data/" + uuid +"/removecoins/" + coins + "/";
    }

    public String getCoins(UUID uuid) {

        return getIp() + "api/main_data/" + uuid + "/getcoins/";
    }

    public String setQuest(UUID uuid, String quest) {
        return getIp() + "api/main_data/" + uuid + "/setquest/" + quest +"/";
    }

    public String getReward(UUID uuid) {
        return getIp() + "api/main_data/" + uuid + "/getreward";
    }

    public String finishReward(UUID uuid) {
        return getIp() + "api/main_data/" + uuid + "/finishreward";
    }

    public String[] readKills(UUID uuid) {
        try {
            String rewards = readStringFromURL(getReward(uuid));

            rewards = rewards.replace("\\", "");
            StringBuilder builder = new StringBuilder(rewards);
            builder.deleteCharAt(0);
            builder.deleteCharAt(rewards.length() -2);

            rewards = builder.toString();

            JSONObject obj = (JSONObject) new JSONParser().parse(rewards);

            JSONArray jsonArray = (JSONArray) obj.get("toKill");

            Iterator<String> iterator = jsonArray.iterator();
            ArrayList<String> kills = new ArrayList<>();

            while(iterator.hasNext()) {
                kills.add(iterator.next());
            }

            return kills.toArray(new String[0]);

        } catch (Exception e) {
            e.printStackTrace();
            return new String[] { "error" };
        }
    }

    public Long[] getItems(UUID uuid) {
        try {
            String rewards = readStringFromURL(getReward(uuid));

            rewards = rewards.replace("\\", "");
            StringBuilder builder = new StringBuilder(rewards);
            builder.deleteCharAt(0);
            builder.deleteCharAt(rewards.length() -2);

            rewards = builder.toString();

            JSONObject obj = (JSONObject) new JSONParser().parse(rewards);

            JSONArray jsonArray = (JSONArray) obj.get("items");

            Iterator<Long> iterator = jsonArray.iterator();
            ArrayList<Long> items = new ArrayList<>();

            while(iterator.hasNext()) {
                items.add(iterator.next());
            }


            return items.toArray(new Long[0]);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNameFromUniqueId(String uuid) {

        try {

            String url = readStringFromURL("https://api.mojang.com/user/profiles/" + uuid + "/names");

            JSONArray names = (JSONArray) JSONValue.parseWithException(url);
            String currentName = names.get(names.size() -1).toString();

            JSONObject obj = (JSONObject) JSONValue.parseWithException(currentName);

            return obj.get("name").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }

    public String idToName(String id) {
        try {
            String url = readStringFromURL("https://minecraft-ids.grahamedgecombe.com/items.json");
            JSONArray results = (JSONArray) new JSONParser().parse(url);

            for (Object result : results) {
                JSONObject obj = (JSONObject) JSONValue.parse(result.toString());

                String name = obj.get("text_type").toString();
                String type = obj.get("type").toString();

                if(type.equals(id)) {
                    return name;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
        return "error";
    }

    public static MineShop getPlugin() {
        return plugin;
    }

    }

