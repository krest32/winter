package com.krest.others.client;

/**
 * @Auther: krest
 * @Date: 2020/11/10 11:18
 * @Description:
 */
import com.krest.others.entity.Address;
import com.krest.utils.entity.Member;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
@FeignClient("member")
public interface MemberClient {

    /**
     * 根据ID得到用户信息
     */
    @GetMapping("/member/member/getOrderMemberInfo/{id}")
    public Member getOrderMemberInfo(@PathVariable("id") String id);


    @ApiOperation(value = "根据会员id得到所有地址")
    @GetMapping("/member/address/feignGetAddressByMemberId/{memberId}")
    public List<Address> feignGetAddressByMemberId(@PathVariable("memberId") String memberId);

}
