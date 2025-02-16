package de.schelle.backupsystem;

import org.bukkit.World;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupManager {

    private static File folder = new File("backups");

    private static File getFile(World world) {

        File dir = new File(folder + "/" + world.getName());
        dir.mkdirs();
        return new File(dir.getAbsolutePath() + "/world");
    }

    private static File getZipFile(World world) {
        return new File(getFile(world).getAbsolutePath() + Utility.getTime() + ".zip");
    }

    public static void backup(World world) {
        try {
            copy(world.getWorldFolder(),getFile(world));
            zipWorld((getFile(world)), getZipFile(world));
            delteAll(getFile(world));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void delteAll(File dir) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                delteAll(f);
            }
            f.delete();
        }
        dir.delete();
    }

    private static void copy(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat","session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists()) {
                        target.mkdirs();
                        String files[] = source.list();
                        for (String File : files) {
                            copy(new File(source, File), new File(target, File));
                        }
                    } else {
                        InputStream in = new FileInputStream(source);
                        OutputStream out = new FileOutputStream(target);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                        in.close();
                        out.close();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void zipWorld(File source, File dest) throws IOException {
        FileOutputStream fos = new FileOutputStream(dest);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        zipFile(source, source.getName(), zipOut);


        zipOut.close();
        fos.close();
    }

    private static void zipFile(File source, String name, ZipOutputStream zipOut) throws IOException {
        if (source.isHidden()) {
            return;
        }
        if (source.isDirectory()) {
            if (name.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(name));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(name + "/"));
                zipOut.closeEntry();
            }
            File[] children = source.listFiles();
            for (File child : children) {
                zipFile(child, name + "/" + child.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(source);
        ZipEntry zipEntry = new ZipEntry(name);
        zipOut.putNextEntry(zipEntry);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            zipOut.write(buffer, 0, length);
        }
        fis.close();

    }
}
