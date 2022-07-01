package com.krest.member.controller;


import com.krest.member.entity.Address;
import com.krest.member.entity.Member;
import com.krest.member.service.AddressService;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 会员地址表 前端控制器
 * </p>
 *
 * @author krest
 * @since 2020-12-23
 */
@Api(value = "会员地址管理",tags ="会员地址管理")
@CrossOrigin
@RestController
@RequestMapping("/member/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "添加新的地址")
    @PostMapping("addNewAddress")
    public R addNewAddress(@RequestBody Address address){
        return addressService.addNewAddress(address);
    }

    @ApiOperation(value = "根据会员id得到所有地址")
    @GetMapping("getAddressByMemberId/{memberId}")
    public R getAddressByMemberId(@PathVariable String memberId){
        List<Address> addressList = addressService.getAddressByMemberId(memberId);
        return R.ok().data("addressList",addressList);
    }

    @ApiOperation(value = "根据id删除地址")
    @DeleteMapping("deleteAddressById/{id}")
    public R deleteAddressById(@PathVariable String id){
        addressService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据id得到地址信息")
    @GetMapping("getAddressById/{id}")
    public R getAddressById(@PathVariable String id){
        Address byId = addressService.getById(id);
        return R.ok().data("address",byId);
    }

    @ApiOperation(value = "根据id修改地址信息")
    @PostMapping("updateAddressById")
    public R updateAddressById(@RequestBody Address address){
        String id = address.getId();
        String memberId = address.getMemberId();
        String address1 = address.getAddress();
        if(StringUtils.isEmpty(id)||StringUtils.isEmpty(memberId)||StringUtils.isEmpty(address1)){
            throw  new myException(20001,"更新地址信息错误");
        }
        addressService.updateById(address);
        return R.ok();
    }

    @ApiOperation(value = "根据会员id得到所有地址")
    @GetMapping("feignGetAddressByMemberId/{memberId}")
    public List<Address> feignGetAddressByMemberId(@PathVariable String memberId){
        List<Address> addressList = addressService.getAddressByMemberId(memberId);
        return addressList;
    }

}

