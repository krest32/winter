package com.krest.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.member.client.VedioClient;
import com.krest.member.entity.Member;
import com.krest.member.entity.OrderVedio;
import com.krest.member.entity.Vo.QueryVideoOrderVo;
import com.krest.member.mapper.OrderVedioMapper;
import com.krest.member.service.MemberService;
import com.krest.member.service.OrderVedioService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.entity.Album;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频购买订单 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Service
public class OrderVedioServiceImpl extends ServiceImpl<OrderVedioMapper, OrderVedio> implements OrderVedioService {

    public static IdWorker idWorker = new IdWorker();

    @Autowired
    private VedioClient vedioClient;

    @Autowired
    private MemberService memberService;

    @Override
    public String createOrderVedio(OrderVedio orderVedio) {
        String orderId = idWorker.nextId();
        Album album = vedioClient.clientGetAlbumById(orderVedio.getAlbumId());
        orderVedio.setTotalFee(album.getPrice());
        orderVedio.setId(orderId);
        baseMapper.insert(orderVedio);
        return orderId;
    }

    @Override
    public OrderVedio getOrderVedioInfo(String id) {
        OrderVedio orderVedio = baseMapper.selectById(id);
        String albumId = orderVedio.getAlbumId();
        Album album = vedioClient.clientGetAlbumById(albumId);
        orderVedio.setAlbumInfo(album);
        return orderVedio;
    }

    @Override
    public R listVideoOrder(Long page, Long limit, QueryVideoOrderVo queryVideoOrderVo) {
        // 流程 ：1 根据条件查询所有的订单
        //  2. 补充专辑中的其他信息
        QueryWrapper<OrderVedio> queryWrapper = new QueryWrapper<>();
        Page<OrderVedio> orderVedioPage = new Page<>(page,limit);

        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(queryVideoOrderVo.getMemberName())){
            memberQueryWrapper.like("nickname",queryVideoOrderVo.getMemberName());
            List<Member> memberList = memberService.list(memberQueryWrapper);
            String memberId = memberList.get(0).getId();
            if(!StringUtils.isEmpty(memberId)){
                queryWrapper.eq("member_id",memberId);
            }
        }
        if (queryVideoOrderVo.getStatus() != null){
            queryWrapper.eq("status",queryVideoOrderVo.getStatus());
        }

        queryWrapper.orderByDesc("gmt_create");

        IPage<OrderVedio> orderVedioIPage = baseMapper.selectPage(orderVedioPage, queryWrapper);
        long total = orderVedioIPage.getTotal();
        List<OrderVedio> records = orderVedioIPage.getRecords();

        List<Member> list = memberService.list(null);
        List<OrderVedio> collect = records.stream().map(record -> {
            return editMemberInfo(record, list);
        }).collect(Collectors.toList());


        return R.ok().data("total",total).data("record",collect);
    }

    private OrderVedio editMemberInfo(OrderVedio record, List<Member> list) {
        for (Member member : list) {
            if(member.getId().equals(record.getMemberId())){
                record.setMemberName(member.getNickname());
            }
        }
        if(record.getStatus()==0){
            record.setPayStatus("等待支付");
        }else if(record.getStatus()==1){
            record.setPayStatus("已支付");
        }else{
            throw new myException(500,"查询支付状态出错");
        }

        if(record.getPayType()==1){
            record.setPayTypeString("微信");
        }else if(record.getPayType()==2){
            record.setPayTypeString("支付宝");
        }else{
            throw new myException(500,"查询支付状态出错");
        }

        Album album = vedioClient.clientGetAlbumById(record.getAlbumId());
        record.setAlbumInfo(album);

        return record;
    }
}
