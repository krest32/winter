package com.krest.member.service;

import com.krest.member.entity.LoginVo;
import com.krest.member.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.member.entity.RegisterVo;
import com.krest.member.entity.Vo.QueryMemberVo;
import com.krest.utils.entity.ResponseVedio;
import com.krest.utils.response.R;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-06
 */
public interface MemberService extends IService<Member> {

    String login(LoginVo loginVo);

    void register(RegisterVo registerVo);

    ResponseVedio getCollectVedio(String id,Long page,Long limit);

    R getMemberList(Long page, Long limit, QueryMemberVo queryMemberVo);
}
