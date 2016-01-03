package jmupen;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author xela92
 */
public final class JMupenUtils {

    private static final JMupenGUI gui = JMupenGUI.getInstance();
    private static ArrayList<String> games;

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
        games.add(game.getName());
    }

    public static ArrayList<String> getGames() {
        return games;
    }

    public static String getBar() {
        switch (System.getProperty("os.name")) {
            case "win":
                return "\\";
            default:
                return "/";
        }
    }

    public static String getHome() {
        return System.getProperty("user.home");

    }

}
