package hawk.segment.entity;
import lombok.Data;

@Data
public class Document {
    private long docID;
    private long goodsID;
    private String goodsName;
    private String category;
    private long categoryID;
    private double price;
    private long score;
}
