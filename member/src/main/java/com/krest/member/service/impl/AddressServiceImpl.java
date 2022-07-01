package com.krest.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krest.member.entity.Address;
import com.krest.member.entity.Member;
import com.krest.member.mapper.AddressMapper;
import com.krest.member.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 会员地址表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-23
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    public static IdWorker idWorker = new IdWorker();

    @Override
    public R addNewAddress(Address address) {
        String memberId = address.getMemberId();
        if(StringUtils.isEmpty(memberId)){
            throw  new myException(20001,"没有会员信息");
        }else {
            address.setId(idWorker.nextId());
            baseMapper.insert(address);
        }
        return R.ok();
    }

    @Override
    public List<Address> getAddressByMemberId(String memberId) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id",memberId);
        List<Address> addresses = baseMapper.selectList(queryWrapper);

        return addresses;
    }
}
