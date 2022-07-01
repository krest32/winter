package com.krest.others.service.impl;

import com.krest.others.entity.MqMessage;
import com.krest.others.mapper.MqMessageMapper;
import com.krest.others.service.MqMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * MQ消息状态表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-26
 */
@Service
public class MqMessageServiceImpl extends ServiceImpl<MqMessageMapper, MqMessage> implements MqMessageService {

}
