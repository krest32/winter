package com.krest.member.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.member.entity.LoginVo;
import com.krest.member.entity.Member;
import com.krest.member.entity.OrderVedio;
import com.krest.member.entity.RegisterVo;
import com.krest.member.entity.Vo.QueryMemberVo;
import com.krest.member.service.MemberService;
import com.krest.member.service.OrderVedioService;
import com.krest.utils.entity.ResponseVedio;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-06
 */
@Api(value = "会员中心",tags ="会员中心")
@CrossOrigin
@RestController
@RequestMapping("/member/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderVedioService orderVedioService;

    @ApiOperation(value = "前台登陆" )
    @PostMapping("frontLogin")
    public R frontLogin(@RequestBody LoginVo loginVo){
        String token =memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    @ApiOperation(value = "会员注册" )
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("getLoginInfo")
    public R getLoginInfo(HttpServletRequest request){
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            System.out.println("token是:"+memberId);
            Member loginInfoVo = memberService.getById(memberId);
            return R.ok().data("item", loginInfoVo);
        }catch (Exception e){
            e.printStackTrace();
            throw new myException(20001,"error");
        }
    }

    @ApiOperation(value = "根据ID得到用户信息")
    @GetMapping("getMemberInfo/{id}")
    public R getMemberInfo(@PathVariable String id){
        Member wMember = memberService.getById(id);
        return R.ok().data("member",wMember);
    }

    @ApiOperation(value = "根据ID得到用户信息")
    @GetMapping("getOrderMemberInfo/{id}")
    public Member getOrderMemberInfo(@PathVariable String id){
        Member Member = memberService.getById(id);
        return Member;
    }

    @ApiOperation(value = "从Redis中获取信息")
    @GetMapping("getCode/{phone}")
    public R getCode(@PathVariable String phone){
        String mobleCode = String.valueOf(redisTemplate.opsForValue().get(phone));
        return R.ok().data("code",mobleCode);
    }

    @ApiOperation(value = "更新member信息")
    @PostMapping("updateMember")
    public R updateMember(@RequestBody Member member){
        memberService.updateById(member);
        return R.ok();
    }

    @ApiOperation(value = "获取member列表")
    @PostMapping("getMemberList/{page}/{limit}")
    public R getMemberList(@PathVariable Long page,
                          @PathVariable Long limit,
                          @RequestBody(required = false) QueryMemberVo queryMemberVo){
        return memberService.getMemberList(page,limit,queryMemberVo);
    }

    @ApiOperation(value = "得到收藏的视频列表")
    @GetMapping("getCollectVedio/{id}/{page}/{limit}")
    public R getCollectVedio(@PathVariable String id,
                             @PathVariable Long page,
                             @PathVariable Long limit){
        ResponseVedio responseVedio = memberService.getCollectVedio(id,page,limit);
        return R.ok().data("memberVedioList",responseVedio).data("total",responseVedio.getTotal());
    }

    @GetMapping("isBuyAlbum/{memberid}/{id}")
    public R isBuyAlbum(@PathVariable String memberid,
                              @PathVariable String id) {
        //订单状态是1表示支付成功
        int count =orderVedioService.count(new QueryWrapper<OrderVedio>().eq("member_id", memberid).eq("album_id", id).eq("status", 1));

        if(count>0) {
            // 等于一 表示已经购买
            return R.ok().data("isBuy",1);
        } else {
            return R.ok().data("isBuy",count);
        }
    }
}

