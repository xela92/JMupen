package jmupen;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author xela92
 */
public final class JMupenUtils {

    private static final JMupenGUI gui = JMupenGUI.getInstance();
    private static ArrayList<String> games = new ArrayList<String>();
    private static Path recentsFile = Paths.get(JMupenUtils.getHome() + JMupenUtils.getBar() + "jmupen.recents");

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

    public static void addGame(File game) {
        JMupenUtils.clearOldRecents();
        games.add(game.getName() + "|" + game.getAbsolutePath());
    }

    public static ArrayList<String> getGames() {
        JMupenUtils.clearOldRecents();
        return games;
    }

    public static void clearOldRecents() {
        if (games.toArray().length > 6) {
            games.remove(0);
        }
    }

    public static void setGames(ArrayList<String> g) {
        JMupenUtils.clearOldRecents();
        games = g;
    }

    public static void writeGamesToFile() {
        List<String> listGames = (List) games;
        try {
            Files.write(recentsFile, listGames, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            gui.showError("Error writing recents file", "Full message: " + ex.getLocalizedMessage());
        }
    }

    public static String getBar() {
        switch (System.getProperty("os.name")) {
            case "win":
                return "\\";
            default:
                return "/";
        }
    }

    public static String getOs() {
        switch (System.getProperty("os.name")) {
            case "win":
                return "win";
            case "Windows 10":
                return "win";
            case "mac":
                return "mac";
            default:
                return "lin";
        }
    }

    public static String getHome() {
        return System.getProperty("user.home");

    }

}
