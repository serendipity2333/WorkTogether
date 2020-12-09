package com.wuzi.WorkTogether.service.impl;

import com.wuzi.WorkTogether.dao.PostDao;
import com.wuzi.WorkTogether.domain.Post;
import com.wuzi.WorkTogether.domain.Record;
import com.wuzi.WorkTogether.domain.dto.PageDto;
import com.wuzi.WorkTogether.domain.dto.PostDto;
import com.wuzi.WorkTogether.domain.dto.RecordDto;
import com.wuzi.WorkTogether.service.PostService;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 施武轩
 * @version 1.0
 * @date 2020/12/7 15:40
 * @lastEditor
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;

    /**
     * 获取一页帖子的内容
     * @param page 当前页数
     * @param size 每页包含的帖子数
     * @return 当前页信息
     */
    @Override
    public PageDto getPostForPage(Integer page,Integer size){
        PageDto pageDto = new PageDto();
        int totalCount = postDao.queryPostCount();
        pageDto.setPagination(totalCount,page,size);
        int startIndex = size*(page-1);
        List<Post> posts = postDao.queryPostForPage(startIndex,size);
        List<PostDto> postDtoList = new ArrayList<>();
        for (Post p : posts){
            PostDto dto = new PostDto();
            dto.setId(p.getId());
            dto.setLikeNumber(p.getLikeNumber());
            dto.setTitle(p.getTitle());
            dto.setTime(p.getTime());
            dto.setDetail(p.getDetail());
            //Todo 查询用户姓名
            dto.setUserName("UZI");
            postDtoList.add(dto);
        }
        pageDto.setPostList(postDtoList);
        return pageDto;
    }

    @Override
    public PostDto queryPostInfo(Integer postId) {
        Post p =postDao.queryPostInfo(postId);
        PostDto dto = new PostDto();
        dto.setLikeNumber(p.getLikeNumber());
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setTime(p.getTime());
        dto.setDetail(p.getDetail());
        //Todo 查询用户姓名
        dto.setUserName("UZI");
       return dto;
    }

    @Override
    public List<RecordDto> queryPostDetail(Integer postId) {
        List<RecordDto> recordDtoList = new ArrayList<>();
        List<Record> records = postDao.queryAllRecordForPost(postId);
        for (Record r:records){
            RecordDto dto = new RecordDto();
            dto.setContent(r.getContent());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dto.setTime(sdf.format(r.getTime()));
            //Todo 查询用户姓名
            dto.setUserName("歪比巴卜");
            recordDtoList.add(dto);
        }
        return recordDtoList;
    }

    @Override
    public Integer queryRecordNumber(Integer postId) {
        return postDao.queryRecordNumber(postId);
    }

    @Override
    public boolean addRecord(Record record) {
        return postDao.addRecord(record) != 0;
    }

    @Override
    public void likePost(Integer postId) {
        postDao.likePost(postId);
    }

    @Override
    public Integer addPost(Post post) {
        int result = postDao.addPost(post);
        if(result>0){
            return post.getId();
        }
        else {
            return 0;
        }
    }
}
