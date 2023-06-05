package ecgberht.CherryVis;

import com.github.luben.zstd.Zstd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class WriteJsonStrategy implements WriteStrategy{
    @Override
    public void write(Object content, String path) {
        Gson aux = new GsonBuilder().create();
        try (FileWriter writer = new FileWriter(path)) {
            aux.toJson(content, writer);
        } catch (IOException e) {
            System.err.println("writeJSON");
            e.printStackTrace();
        }
    }
}
