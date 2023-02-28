package hawk.index.mapper;

import hawk.common.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface GoodsMapper {
    @Select("select * from goods_info")
    ArrayList<Goods> queryAllGoods();
}
