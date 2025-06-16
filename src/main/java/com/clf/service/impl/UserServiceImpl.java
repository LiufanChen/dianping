package com.clf.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clf.dto.LoginFormDTO;
import com.clf.dto.Result;
import com.clf.dto.UserDTO;
import com.clf.entity.User;
import com.clf.mapper.UserMapper;
import com.clf.service.IUserService;
import com.clf.utils.RegexUtils;
import org.apache.catalina.Session;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.clf.utils.RedisConstansts.*;
import static com.clf.utils.SystemConstants.USER_NICK_NAME_PREFIX;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendCode(String phone, HttpSession session) {
        if(RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误!");
        }
        String code = RandomUtil.randomNumbers(6);
        //生成验证码保存到redis中
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY+ phone,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);
        session.setAttribute("code",code);
        log.debug("验证码:"+code);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {

        String phone = loginForm.getPhone();
        if(RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误!");
        }
        //从redis中获取验证码
        String redisCacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        String code = loginForm.getCode();
        if(redisCacheCode==null ||  !redisCacheCode.equals(code)){
            return Result.fail("验证码错误");
        }
        User user = query().eq("phone",phone).one();
        //用户不存在则创建新用户
        if(user==null){
            user = createUserWithPhone(phone);
        }
        String token = UUID.randomUUID().toString(true);
        UserDTO userDTO = BeanUtil.copyProperties(user,UserDTO.class);
        // 7.2.将User对象转为HashMap存储
        Map<String,Object> userMap = BeanUtil.beanToMap(userDTO,new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((p,q) -> q.toString()));

        stringRedisTemplate.opsForHash().putAll(LOGIN_TOKEN_KEY+token,userMap);
        stringRedisTemplate.expire(LOGIN_TOKEN_KEY+token,LOGIN_TOKEN_TTL, TimeUnit.MINUTES);
        return Result.ok();
    }


    private User createUserWithPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX+RandomUtil.randomString(10));
        save(user);

        return user;
    }
}
