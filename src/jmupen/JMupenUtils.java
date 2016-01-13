package jmupen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

/**
 *
 * @author xela92
 */
public final class JMupenUtils {

    private static final JMupenGUI gui = JMupenGUI.getInstance();
    private static Properties props = new Properties();
    private static ArrayList<String> games = new ArrayList<String>();
    private static final Path recentsFile = Paths.get(JMupenUtils.getConfigDir() + JMupenUtils.getBar() + "jmupen.recents");
    private static boolean fullscreen = true;
    private static boolean using_legacy = false;

    public static void addGame(File game) {
        JMupenUtils.clearOldRecents();
        games.add(game.getName() + "|" + game.getAbsolutePath());
    }

    public static void addRecentGame(File game) {
        games = JMupenUtils.getGamesFromFile(Paths.get(JMupenUtils.getConfigDir() + JMupenUtils.getBar() + "jmupen.recents"));
        games.add(game.getName() + "|" + game.getAbsolutePath());
        JMupenUtils.setGames(games);
        JMupenGUI.getInstance().getModel().addElement(game.getName());
        JMupenUtils.clearOldRecents();

    }

    public static void clearOldRecents() {
        if (games.toArray().length > 6) {
            games.remove(0);
            JMupenGUI.getInstance().getModel().removeElementAt(0);
        }
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static ArrayList<String> getGamesFromFile(Path recents) {
        if (!Files.exists(recents)) {
            try {
                Files.createFile(recents);
            } catch (IOException ex) {
                gui.showError("Error creating recents file. You won't see recent games list.", ex.getLocalizedMessage());
            }
        }
        Stream<String> stream = null;
        try {
            stream = Files.lines(recents, StandardCharsets.UTF_8);
        } catch (IOException e) {
            gui.showError("Error opening file. Maybe it does not exist or you cannot access it.", e.getLocalizedMessage());
        }
        if (stream != null) {
            return new ArrayList(Arrays.asList(stream.toArray()));
        } else {
            return null;
        }
    }

    public static Path getRecents() {
        return recentsFile;
    }

    public static boolean getUsingLegacyVersion() {
        return using_legacy;
    }

    public static String getBar() {
        switch (System.getProperty("os.name")) {
            case "win":
                return "\\";
            default:
                return "/";
        }
    }

    public static File getConf() {
        return new File(JMupenUtils.getConfigDir() + JMupenUtils.getBar() + "jmupen.conf");

    }

    public static String getConfigDir() {
        if (JMupenUtils.getOs().equals("mac") || JMupenUtils.getOs().equals("lin")) {
            return JMupenUtils.getHome();
        } else {
            return System.getProperty("java.io.tmpdir");
        }
    }

    public static boolean getFullscreen() {
        return fullscreen;
    }

    public static ArrayList<String> getGames() {
        JMupenUtils.clearOldRecents();
        return games;
    }

    public static String getHome() {
        return System.getProperty("user.home");

    }

    public static String getOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "win";
        } else if (os.contains("mac")) {
            return "mac";
        } else {

            return "lin";
        }
    }

    public static void loadParams() {
        InputStream is = null;
        if (JMupenUtils.getConf().exists()) {
            // First try loading from the current directory
            try {
                is = new FileInputStream(JMupenUtils.getConf());
            } catch (Exception e) {
                is = null;
            }

            try {
                props.load(is);
                JMupenUtils.setFullscreen(props.get("Fullscreen").equals("true"));
                JMupenUtils.setUsingLegacyVersion(props.get("UsingLegacy").equals("true"));
            } catch (Exception e) {
                saveParamChanges();
            }
        } else {
            saveParamChanges();
        }

    }

    public static void saveParamChanges() {
        try {
            props.setProperty("Fullscreen", "" + JMupenUtils.getFullscreen());
            props.setProperty("UsingLegacy", "" + JMupenUtils.getUsingLegacyVersion());
            OutputStream out = new FileOutputStream(JMupenUtils.getConf());
            props.store(out, "JMupen Configuration File");
        } catch (Exception e) {
            JMupenGUI.getInstance().showError("Error saving conf file.", e.getLocalizedMessage());
        }
    }

    public static void setGames(ArrayList<String> g) {
        JMupenUtils.clearOldRecents();
        games = g;
    }

    public static void setFullscreen(boolean ans) {
        fullscreen = ans;
    }

    public static void setUsingLegacyVersion(boolean ans) {
        using_legacy = ans;
    }

    public static void writeGamesToFile() {
        List<String> listGames = (List) games;
        try {
            Files.write(recentsFile, listGames, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            gui.showError("Error writing recents file", "Full message: " + ex.getLocalizedMessage());
        }
    }

}
