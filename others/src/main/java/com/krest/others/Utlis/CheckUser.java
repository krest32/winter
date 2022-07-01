package com.krest.others.Utlis;

import com.krest.others.entity.UserInfoVO;
import com.krest.utils.myexception.myException;
import com.krest.utils.utils.JwtUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: krest
 * @Date: 2020/12/23 18:38
 * @Description:
 */
public class CheckUser {

    public static UserInfoVO getUser(HttpServletRequest request){

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("=================token:"+request.getHeader("token"));
        System.out.println("=================memberId:"+memberId);
        UserInfoVO userInfoVO = new UserInfoVO();
        // memberId为空，用户没有登陆,
        if (!StringUtils.isEmpty(memberId)){
            userInfoVO.setMemberId(memberId);
            userInfoVO.setTempUSer(false);
            return userInfoVO;
        }else {
            throw  new myException(20001,"用户没有登录");
        }
    }
}
