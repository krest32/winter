package com.krest.member.service;

import com.krest.member.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krest.member.entity.Member;
import com.krest.utils.response.R;

import java.util.List;

/**
 * <p>
 * 会员地址表 服务类
 * </p>
 *
 * @author krest
 * @since 2020-12-23
 */
public interface AddressService extends IService<Address> {

    R addNewAddress(Address address);

    List<Address> getAddressByMemberId(String memberId);
}
