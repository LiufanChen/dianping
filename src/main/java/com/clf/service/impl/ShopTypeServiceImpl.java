package com.clf.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clf.dto.Result;
import com.clf.entity.Shop;
import com.clf.entity.ShopType;
import com.clf.mapper.ShopTypeMapper;
import com.clf.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.hutool.http.ContentType.JSON;
import static com.clf.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;
import static com.clf.utils.RedisConstants.CACHE_SHOP_TYPE_TTL;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private IShopTypeService typeService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryTypeList() {

        String redisCacheShopType = stringRedisTemplate.opsForValue().get(CACHE_SHOP_TYPE_KEY);
        if(StrUtil.isNotBlank(redisCacheShopType)){
            List<ShopType> typeList = JSONUtil.toList(redisCacheShopType, ShopType.class);
            log.debug("从Redis缓存中获取店铺类型列表，数据: "+typeList);
            return Result.ok(typeList);
        }
        List<ShopType> typeList = query().orderByAsc("sort").list();
        if(typeList == null|| typeList.isEmpty()){
            log.debug("从数据库查询店铺类型列表，但结果为空");
            return Result.fail("获取店铺类型失败");
        }
        log.debug("从数据库查询店铺类型列表，数据: "+typeList);
        stringRedisTemplate.opsForValue().set(CACHE_SHOP_TYPE_KEY,JSONUtil.toJsonStr(typeList),CACHE_SHOP_TYPE_TTL, TimeUnit.MINUTES);
        return Result.ok(typeList);
    }
}
