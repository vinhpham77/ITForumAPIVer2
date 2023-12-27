package com.caykhe.itforum.services;

import com.caykhe.itforum.dtos.ApiException;
import com.caykhe.itforum.dtos.SubCommentDto;
import com.caykhe.itforum.models.Comment;
import com.caykhe.itforum.models.CommentDetails;
import com.caykhe.itforum.models.User;
import com.caykhe.itforum.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final SeriesRepository seriesRepository;
    private final UserRepository userRepository;

    private final ImageService imageService;

    private final CommentRepository commentRepository;
    private final CommentDetailsRepository commentDetailsRepository;

    public Comment create(Integer targetId, boolean isSeries) {

        var comment = Comment
                .builder()
                .targetId(targetId)
                .type(isSeries)
                .build();

        return commentRepository.save(comment);
    }

    public void deleteComment(Integer targetId, boolean isSeries) {
        Comment comment = commentRepository.findByTargetIdAndType(targetId, isSeries)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubCom = commentDetailsRepository.findByComment(comment);
        for (CommentDetails subComment: listSubCom) {
            imageService.removeImagesInContent(subComment.getContent());
        }
        commentRepository.delete(comment);
    }

    public CommentDetails addSubComment(Integer targetId, boolean type ,SubCommentDto newSubComment) {
        Comment comment = commentRepository.findByTargetIdAndType(targetId, type)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubCom = commentDetailsRepository.findByComment(comment);
        User user = userRepository.findByUsername(newSubComment.getUsername())
                .orElseThrow(() -> new ApiException("Người dùng @" + newSubComment.getUsername() + " không tồn tại", HttpStatus.NOT_FOUND));
        int right;
        CommentDetails subCommentFather = null;
        if(newSubComment.getSubCommentFatherId() == null)
            right = listSubCom.size()*2 + 1;
        else {
            subCommentFather = listSubCom.stream().filter(i -> i.getId().equals(newSubComment.getSubCommentFatherId())).findFirst()
                    .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
            right = subCommentFather.getRight();
        }

        imageService.saveImagesInContent(newSubComment.getContent());

        listSubCom.forEach(subComment -> {
            if (subComment.getLeft() > right) {
                subComment.setLeft(subComment.getLeft() + 2);
                subComment.setRight(subComment.getRight() + 2);
            }
        });

        CommentDetails subComment = CommentDetails
                .builder()
                .comment(comment)
                .createdBy(user)
                .content(newSubComment.getContent())
                .updatedAt(new Date().toInstant())
                .left(right)
                .right(right + 1)
                .build();
        if(subCommentFather != null && subComment != null)
            subCommentFather.setRight(right + 2);
        listSubCom.add(subComment);
        commentDetailsRepository.saveAll(listSubCom);
        updateCommentCount(comment, listSubCom.size());
        return subComment;
    }

    public boolean removeSubComment(Integer targetId, Integer subId, boolean type) {

        Comment comment = commentRepository.findByTargetIdAndType(targetId, type)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubCom = commentDetailsRepository.findByComment(comment);
        CommentDetails subComment = listSubCom.stream().filter(i -> i.getId().equals(subId)).findFirst()
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!subComment.getCreatedBy().getUsername().equals(username)) {
            throw new ApiException("Bạn không có quyền xóa comment này", HttpStatus.FORBIDDEN);
        }

        int a = subComment.getRight() - subComment.getLeft() + 1;
        int left = subComment.getLeft();
        int right = subComment.getRight();
        boolean result = listSubCom.removeIf((subCom) -> {
            if(subCom.getLeft() >= left && subCom.getRight() <= right) {
                imageService.removeImagesInContent(subCom.getContent());
                commentDetailsRepository.deleteById(subCom.getId());
                return true;
            }
            return false;
        });

        listSubCom.forEach(subCom -> {
            if (subCom.getLeft() > left) {
                subCom.setLeft(subCom.getLeft() - a);
                subCom.setRight(subCom.getRight() - a);
            }
        });
        commentDetailsRepository.saveAll(listSubCom);
        updateCommentCount(comment, listSubCom.size());
        return result;
    }

    public CommentDetails updateSubComment(Integer targetId, boolean type, Integer subId, SubCommentDto subCommentDto) {
        Comment comment = commentRepository.findByTargetIdAndType(targetId, type)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubCom = commentDetailsRepository.findByComment(comment);
        CommentDetails subComment = listSubCom.stream().filter(i -> i.getId().equals(subId)).findFirst()
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!subComment.getCreatedBy().getUsername().equals(username)) {
            throw new ApiException("Bạn không có quyền chỉnh sửa comment này", HttpStatus.FORBIDDEN);
        }

        imageService.removeImagesInContent(subComment.getContent());
        imageService.saveImagesInContent(subCommentDto.getContent());
        subComment.setContent(subCommentDto.getContent());
        subComment.setUpdatedAt(new Date().toInstant());
        commentDetailsRepository.saveAll(listSubCom);
        updateCommentCount(comment, listSubCom.size());
        return subComment;
    }

    public List<CommentDetails> getComments(Integer targetId, boolean type, Integer subId) {
        Comment comment = commentRepository.findByTargetIdAndType(targetId, type)
                .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
        List<CommentDetails> listSubComment = commentDetailsRepository.findByComment(comment);
        int number = 0;
        if(subId != null){
            CommentDetails subComment = listSubComment.stream().filter(i -> i.getId().equals(subId)).findFirst()
                    .orElseThrow(() -> new ApiException("Comment không tồn tại", HttpStatus.NOT_FOUND));
            number = subComment.getLeft();
        }

        List<CommentDetails> nextComments = new ArrayList<>();
        int currentRight = number;
        for (CommentDetails subComent : listSubComment) {
            if (subComent.getLeft() == currentRight + 1) {
                nextComments.add(subComent);
                currentRight = subComent.getRight();
            }
        }

        return nextComments;
    }

    private void updateCommentCount(Comment comment, int commentCount) {
        if(comment.getType())
            updateSeriesCommentCount(comment.getTargetId(), commentCount);
        else
            updatePostCommentCount(comment.getTargetId(), commentCount);

    }

    public void updatePostCommentCount(Integer id, int commentCount) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("Bài viết không tồn tại", HttpStatus.NOT_FOUND));

        post.setCommentCount(commentCount);

        postRepository.save(post);
    }

    public void updateSeriesCommentCount(Integer id, int commentCount) {
        var series = seriesRepository.findById(id)
                .orElseThrow(() -> new ApiException("Series không tồn tại", HttpStatus.NOT_FOUND));

        series.setCommentCount(commentCount);

        seriesRepository.save(series);
    }
}
