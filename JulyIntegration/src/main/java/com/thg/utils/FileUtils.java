package com.thg.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by tanhuigen
 * Date 2022-10-15
 * Description 
 */
public class FileUtils {


    public static File getDataFile(String dataFilePath) throws IOException {
        File file = getRelativePathFile(dataFilePath);
        if (file.exists()) {
            return file;
        }
        throw new NoSuchFileException(String.format("test file %s not exist", dataFilePath));
    }


    /**
     * 检查路径对于的文件是否存在
     */
    public static boolean existsFile(String filePath){
        try{
            return getRelativePathFile(filePath).exists();
        }catch (Exception e){
            return false;
        }
    }

    public static File getRelativePathFile(String dataFilePath) {
        String path;
        try {
            path = new ClassPathResource(dataFilePath).getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Can not open the file:" + dataFilePath, e);
        }
        String realPath = path.replace("target/classes/", "src/main/resources/");
        realPath = realPath.replace("target/test-classes/", "src/test/resources/");
        return new File(realPath);
    }

    public static Path getFilePath(String dataFilePath) throws IOException {
        return getRelativePathFile(dataFilePath).toPath();
    }

    public static void writeJsonToFile(String jsonString, File file) throws IOException {
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(jsonString);
        bw.flush();
        bw.close();
    }

}
