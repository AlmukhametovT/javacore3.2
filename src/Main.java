import ru.ufa.GameProgress;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static final String LINK_SAVE_GAMES = "C:\\Users\\TAU\\IdeaProjects\\javacore3.1\\Games\\savegames";
    public static final String LINK_REPORT = "C:\\Users\\TAU\\IdeaProjects\\javacore3.1\\Games\\temp\\temp.txt";
    public static List<String> saveGamesList = new ArrayList<>();

    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(94, 10, 2, 254.32);
        GameProgress gameProgress2 = new GameProgress(86, 13, 4, 316.36);
        GameProgress gameProgress3 = new GameProgress(30, 50, 12, 1666.66);

        saveGame(LINK_SAVE_GAMES + "\\save1.dat", gameProgress1);
        saveGame(LINK_SAVE_GAMES + "\\save2.dat", gameProgress2);
        saveGame(LINK_SAVE_GAMES + "\\save3.dat", gameProgress3);

        zipFiles(LINK_SAVE_GAMES + "\\zip.zip");

        deleteSaveGames();
    }

    public static void saveGame(String link, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(link);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        saveGamesList.add(link);

        try (FileWriter writer = new FileWriter(LINK_REPORT, true)) {
            writer.append('\n');
            writer.append("Игра сохранена. Файл сохранения - " + link);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String zipLink) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipLink))) {
            for (int i = 0; i < saveGamesList.size(); i++) {
                try (FileInputStream fis = new FileInputStream(saveGamesList.get(i))) {
                    ZipEntry entry = new ZipEntry(saveGamesList.get(i)
                            .substring(saveGamesList.get(i).lastIndexOf("\\") + 1));
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                zout.closeEntry();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try (FileWriter writer = new FileWriter(LINK_REPORT, true)) {
            writer.append('\n');
            writer.append("Архивация сохранений. Файл архива - " + zipLink);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteSaveGames() {
        for (int i = 0; i < saveGamesList.size(); i++) {
            File file = new File(saveGamesList.get(i));
            file.delete();
        }

        saveGamesList.clear();

        try (FileWriter writer = new FileWriter(LINK_REPORT, true)) {
            writer.append('\n');
            writer.append("Каталог с сохранениями очищен.");
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}