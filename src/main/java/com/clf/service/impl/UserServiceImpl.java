package com.clf.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clf.dto.LoginFormDTO;
import com.clf.dto.Result;
import com.clf.entity.User;
import com.clf.mapper.UserMapper;
import com.clf.service.IUserService;
import com.clf.utils.RegexUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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
        
//        if(user==null){
//            user = createUserWithPhone(phone);
//        }
        return null;
    }
}
