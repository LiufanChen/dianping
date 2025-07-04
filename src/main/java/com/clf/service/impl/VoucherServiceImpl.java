package com.clf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clf.dto.Result;
import com.clf.entity.Voucher;
import com.clf.mapper.VoucherMapper;
import com.clf.entity.SeckillVoucher;
import com.clf.service.ISeckillVoucherService;
import com.clf.service.IVoucherService;
import com.clf.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements IVoucherService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private  StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryVoucherOfShop(Long shopId) {
        // 查询优惠券信息
        List<Voucher> vouchers = getBaseMapper().queryVoucherOfShop(shopId);
        // 返回结果
        return Result.ok(vouchers);
    }

    @Override
    @Transactional
    public Result addSeckillVoucher(Voucher voucher) {
        // 保存优惠券
        save(voucher);
        log.debug("保存了一条优惠券"+voucher);
        // 保存秒杀信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);
        log.debug("保存了一条秒杀优惠券"+voucher);
        stringRedisTemplate.opsForValue().set(RedisConstants.SECKILL_STOCK_KEY+voucher.getId(),voucher.getStock().toString());
        return Result.ok(voucher.getId());
    }
}
