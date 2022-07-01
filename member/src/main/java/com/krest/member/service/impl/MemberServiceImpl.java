package com.krest.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.member.client.VedioClient;
import com.krest.member.entity.LoginVo;
import com.krest.member.entity.Member;
import com.krest.member.entity.RegisterVo;
import com.krest.member.entity.VedioCollect;
import com.krest.member.entity.Vo.QueryMemberVo;
import com.krest.member.entity.Vo.QueryVedio;
import com.krest.member.mapper.MemberMapper;
import com.krest.member.service.BlogCollectService;
import com.krest.member.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.member.service.VedioCollectService;
import com.krest.utils.entity.ResponseVedio;
import com.krest.utils.entity.Vedio;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import com.krest.utils.utils.JwtUtils;
import com.krest.utils.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-06
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private VedioClient vedioClient;

    @Autowired
    private VedioCollectService vedioCollectService;

    @Autowired
    private BlogCollectService blogCollectService;


    public static IdWorker idWorker = new IdWorker();

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验参数
        if(StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(mobile)) {
            throw new myException(20001,"注册信息不完整");
        }

        //获取会员
        Member wMember = baseMapper.selectOne(new QueryWrapper<Member>().eq("mobile", mobile));
        if(null == wMember) {
            throw new myException(20001,"error");
        }

        //校验密码
        if(!MD5.encrypt(password).equals(wMember.getPassword())) {
            throw new myException(20001,"error");
        }

        //校验是否被禁用
        if(wMember.getIsDisabled()) {
            throw new myException(20001,"error");
        }

        //使用JWT生成token字符串
        String token = JwtUtils.getJwtToken(wMember.getId(), wMember.getNickname());
        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息，进行校验

        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        System.out.println("注册信息registerVo:"+registerVo);
        //校验参数

        if(StringUtils.isEmpty(mobile) ||
            StringUtils.isEmpty(mobile) ||
            StringUtils.isEmpty(password) ||
            StringUtils.isEmpty(code)) {
            throw new myException(20001,"注册信息不完整");
        }

        //校验校验验证码
        //从redis获取发送的验证码
        String mobleCode = String.valueOf(redisTemplate.opsForValue().get(mobile));

        System.out.println("注册信息mobleCode:"+mobleCode);
        if(!code.equals(mobleCode)) {
            throw new myException(20001,"验证码不正确");
        }


        //查询数据库中是否存在相同的手机号码
        Integer count = baseMapper.selectCount(new QueryWrapper<Member>().eq("mobile", mobile));
        if(count.intValue() > 0) {
            throw new myException(20001,"error");
        }

        //添加注册信息到数据库
        Member member = new Member();
        member.setId(idWorker.nextId());
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://duxin2010.oss-cn-beijing.aliyuncs.com/26a8b62088334ba5b4d6ab48635dad53file.png");
        baseMapper.insert(member);
    }

    @Override
    public ResponseVedio getCollectVedio(String id,Long page,Long limit) {
        // 获取vedio collect List
        QueryWrapper<VedioCollect> vedioCollectQueryWrapper = new QueryWrapper<>();
        Page<VedioCollect> vedioCollectPage = new Page<>(page,limit);
        vedioCollectQueryWrapper.eq("member_id",id);
        IPage<VedioCollect> collectIPage = vedioCollectService.page(vedioCollectPage, vedioCollectQueryWrapper);
        List<VedioCollect> records = collectIPage.getRecords();

        // 获取vedio Id List
        List<String> Idlist= new ArrayList<>();
        for (VedioCollect record : records) {
            String vedioId = record.getVedioId();
            Idlist.add(vedioId);
        }

        QueryVedio queryVedio = new QueryVedio();
        queryVedio.setVedioIdList(Idlist);
        ResponseVedio responseVedio = new ResponseVedio();
        if(queryVedio.getVedioIdList().size()!=0){
            // 获取vedio List，因为调用的问题，获取信息超时
            List<Vedio> memberVedioList = vedioClient.getMemberVedioList(queryVedio);
            // 获取最终结果
            Long total = collectIPage.getTotal();
            responseVedio.setTotal(total);
            responseVedio.setVedioList(memberVedioList);
        }else {
            responseVedio.setTotal(0L);
            responseVedio.setVedioList(null);
        }




        return responseVedio;
    }

    @Override
    public R getMemberList(Long page, Long limit, QueryMemberVo queryMemberVo) {

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        Page<Member> memberPage = new Page<>(page,limit);

        if(!StringUtils.isEmpty(queryMemberVo.getName())){
            queryWrapper.like("nickname",queryMemberVo.getName());
        }

        if(queryMemberVo.getSex()!=null){
            queryWrapper.eq("sex",queryMemberVo.getSex());
        }

        IPage<Member> memberIPage = baseMapper.selectPage(memberPage, queryWrapper);
        long total = memberIPage.getTotal();
        List<Member> records = memberIPage.getRecords();

        return R.ok().data("total",total).data("records",records);
    }
}
