package com.clf.utils;
/**
 * ClassName: RedisConstansts
 * Package: com.clf.utils
 * Description:
 *
 * @Author clf
 * @Create 2025/6/16 21:33
 * @Version 1.0
 */
public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;
    public static final String CACHE_SHOP_KEY = "cache:shop:";
    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_TYPE_KEY = "cache:shopType:";
    public static final Long CACHE_SHOP_TYPE_TTL = 30L;
    //空值的时候设置的有效期（应对缓存穿透）
    public static final Long CACHE_NULL_TTL = 2L;
    public static final String LOCK_SHOP_KEY = "lock:shop:";
    public static final Long LOCK_SHOP_TTL = 10L;

    //缓存中存放优惠券数量 用于异步秒杀
    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    public static final String STREAM_ORDER = "stream.orders";

    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String FEED_KEY = "feed:";
    public static final String SHOP_GEO_KEY = "shop:geo:";
    public static final String USER_SIGN_KEY = "sign:";


}
