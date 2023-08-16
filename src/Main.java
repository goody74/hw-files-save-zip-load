import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String ZIP = "D://MyGames/savegames/savedat.zip";
        List<String> filesToBeDone = Arrays.asList("D://MyGames/savegames/save1.dat"
                , "D://MyGames/savegames/save2.dat", "D://MyGames/savegames/save3.dat");
        GameProgress progress1 = new GameProgress(100, 85, 60, 200000);
        GameProgress progress2 = new GameProgress(99, 77, 90, 400000);
        GameProgress progress3 = new GameProgress(90, 90, 90, 5000000);

        saveGame(filesToBeDone.get(0), progress1);
        saveGame(filesToBeDone.get(1), progress2);
        saveGame(filesToBeDone.get(2), progress3);

        zipFiles(ZIP, filesToBeDone);
        openZip(ZIP);

        System.out.println(openProgress(filesToBeDone.get(0)).toString());
        System.out.println(openProgress(filesToBeDone.get(1)).toString());
        System.out.println(openProgress(filesToBeDone.get(2)).toString());

    }

    public static void saveGame(String saveFileName, GameProgress progress) {
        try {
            FileOutputStream fos = new FileOutputStream(saveFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(progress);
            objectOutputStream.close();
        } catch (IOException ex) {
            System.out.println("Ошибка записи файла " + saveFileName);
        }
    }

    public static void zipFiles(String zipFileName, List<String> files) {
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
            for (String file : files) {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(file);
                out.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                out.write(buffer);
                out.closeEntry();
                fis.close();
                File fileToDelete = new File(file);
                fileToDelete.delete();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void openZip(String zipFileName){
        try (ZipInputStream zin = new ZipInputStream(new
                FileInputStream(zipFileName))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
    public static GameProgress openProgress(String saveFile){
        GameProgress gameProgress=null;
        try {
            FileInputStream fis =new FileInputStream(saveFile) ;
            ObjectInputStream ois = new ObjectInputStream(fis);
            gameProgress =(GameProgress) ois.readObject();
            } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return gameProgress;
    }
}