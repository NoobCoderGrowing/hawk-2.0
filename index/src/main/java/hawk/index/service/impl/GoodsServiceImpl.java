package hawk.index.service.impl;

import hawk.common.entity.Goods;
import hawk.index.mapper.GoodsMapper;
import hawk.index.service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    GoodsMapper goodsMapper;

    @Override
    public ArrayList<Goods> queryAllGoods(){
        return goodsMapper.queryAllGoods();
    };
}