package com.markerhub.demo.service.impl;

import com.markerhub.demo.entity.Live;
import com.markerhub.demo.mapper.LiveMapper;
import com.markerhub.demo.service.LiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2021-04-27
 */
@Service
public class LiveServiceImpl extends ServiceImpl<LiveMapper, Live> implements LiveService {

}
