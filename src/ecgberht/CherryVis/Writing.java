package ecgberht.CherryVis;

public class Writing {
    private WriteStrategy writeStrategy;

    public void write(Object content, String path){
        writeStrategy.write(content, path);
    }

    public void setWriteStrategy(WriteStrategy writeStrategy){
        this.writeStrategy = writeStrategy;
    }

}
