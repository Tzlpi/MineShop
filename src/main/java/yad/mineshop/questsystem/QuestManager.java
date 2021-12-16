package yad.mineshop.questsystem;

import jdk.internal.org.jline.reader.ParsedLine;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import yad.mineshop.MineShop;

import javax.naming.Name;
import javax.print.attribute.standard.PresentationDirection;
import java.text.DecimalFormat;

public class QuestManager {

    private MineShop mineShop;
    public QuestManager(MineShop mineShop) {
        this.mineShop = mineShop;
    }

    public void setState(Player player, boolean bool) {

        PersistentDataContainer data = player.getPersistentDataContainer();

        if(bool) {
            data.set(new NamespacedKey(mineShop, "intask"), PersistentDataType.STRING, "true");
        }
          if(!bool) {
            data.set(new NamespacedKey(mineShop, "intask"), PersistentDataType.STRING, "false");
            removeTask(player, "task");

              try {
                  mineShop.readStringFromURL(mineShop.setQuest(player.getUniqueId(), "none"));
              } catch (Exception e) {
                  e.printStackTrace();
              }

        }

    }

    public boolean getState(Player player) {

        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.get(new NamespacedKey(mineShop, "intask"), PersistentDataType.STRING).equalsIgnoreCase("true")) {
            return true;
        }  else {
            return false;
        }

    }

    public void setTask(Player player ,String task) {

        PersistentDataContainer data = player.getPersistentDataContainer();

        data.set(new NamespacedKey(mineShop, "task"), PersistentDataType.STRING, task);

       new Thread(()-> {
           try {
               mineShop.readStringFromURL(mineShop.setQuest(player.getUniqueId(), task));
           } catch (Exception e) {
               e.printStackTrace();
           }
       }).start();

    }

    public String getTask(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!data.has(new NamespacedKey(mineShop, "task"), PersistentDataType.STRING)) {
            return "null";
        } else {
            return data.get(new NamespacedKey(mineShop, "task"), PersistentDataType.STRING);
        }
    }

    public boolean hasTask(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();

        return !getTask(player).equalsIgnoreCase("null");
    }

    public void removeTask(Player player, String key) {

        PersistentDataContainer data = player.getPersistentDataContainer();

        data.remove(new NamespacedKey(mineShop, "" + key));

    }

    public void addTempCoins(Player player, double amount) {

        PersistentDataContainer data = player.getPersistentDataContainer();

        double currentTemp = data.get(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE);
        data.set(new NamespacedKey(mineShop, "tempcoins"), PersistentDataType.DOUBLE, currentTemp + amount);
    }

    public void addProgress(Player player, String key, int amount ) {

        PersistentDataContainer data = player.getPersistentDataContainer();

        int currentAmount = data.get(new NamespacedKey(mineShop, "" + key), PersistentDataType.INTEGER);

        data.set(new NamespacedKey(mineShop, "" + key), PersistentDataType.INTEGER, currentAmount + amount);
    }

    public int getProgress(Player player, String key) {

        PersistentDataContainer data = player.getPersistentDataContainer();

        int a = data.get(new NamespacedKey(mineShop, "" + key), PersistentDataType.INTEGER);

        return a;

    }

    public void createDataType(Player player, String name, PersistentDataType type, String value) {

        PersistentDataContainer data = player.getPersistentDataContainer();;

        if(type.equals(PersistentDataType.DOUBLE)) {
            data.set(new NamespacedKey(mineShop, "" + name), PersistentDataType.DOUBLE, 0.0);
        } else if(type.equals(PersistentDataType.INTEGER)) {
            data.set(new NamespacedKey(mineShop, "" + name), PersistentDataType.INTEGER, 0);
        } else if(type.equals(PersistentDataType.STRING)) {
            data.set(new NamespacedKey(mineShop, "" + name), PersistentDataType.STRING, value);
        }

    }

    }


