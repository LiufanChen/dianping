package com.clf.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clf.dto.LoginFormDTO;
import com.clf.dto.Result;
import com.clf.entity.User;
import com.clf.mapper.UserMapper;
import com.clf.service.IUserService;
import com.clf.utils.RegexUtils;
import org.apache.catalina.Session;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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
    @Override
    public Result sendCode(String phone, HttpSession session) {
        if(RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误!");
        }
       String code = RandomUtil.randomNumbers(6);

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
        Object codeCode = session.getAttribute("code");
        String code = loginForm.getCode();
        if(codeCode!=null ||  !codeCode.toString().equals(code)){
            return Result.fail("验证码错误");
        }
        User user = query().eq("phone",phone).one();
        
        if(user==null){
            user = createUserWithPhone(phone);
        }

        session.setAttribute("User",user);

        return Result.ok();
    }


    private User createUserWithPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX+RandomUtil.randomString(10));
        s   ave(user);
        return user;
    }
}
