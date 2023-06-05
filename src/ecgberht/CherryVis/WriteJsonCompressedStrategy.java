package ecgberht.CherryVis;

import com.github.luben.zstd.Zstd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WriteJsonCompressedStrategy implements WriteStrategy{
    @Override
    public void write(Object content, String path) {
        Gson aux = new GsonBuilder().create();
        String data = aux.toJson(content);
        byte[] compressedData = Zstd.compress(data.getBytes());
        try (OutputStream writer = new FileOutputStream(new File(path))) {
            writer.write(compressedData);
        } catch (IOException e) {
            System.err.println("writeJSONCompressed");
            e.printStackTrace();
        }
    }
}
