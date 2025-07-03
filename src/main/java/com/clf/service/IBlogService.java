package com.clf.service;

import com.clf.dto.Result;
import com.clf.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IBlogService extends IService<Blog> {


    Result queryHotBlog(Integer current);

    Result queryBlogById(Long id);

}
