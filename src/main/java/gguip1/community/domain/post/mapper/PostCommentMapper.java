package gguip1.community.domain.post.mapper;

import gguip1.community.domain.post.dto.response.AuthorResponse;
import gguip1.community.domain.post.dto.response.PostCommentPageItemResponse;
import gguip1.community.domain.post.entity.Post;
import gguip1.community.domain.post.entity.PostComment;
import gguip1.community.domain.user.entity.User;
import gguip1.community.domain.image.storage.ImageStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCommentMapper {
    private final ImageStorage imageStorage;

    public PostCommentPageItemResponse toPostCommentPageItemResponse(PostComment postComment, User user, boolean isAuthor){
        String imageKey = user.getProfileImage() != null ? user.getProfileImage().getStorageKey() : null;
        String fullUrl = imageStorage.publicUrl(imageKey);

        return PostCommentPageItemResponse.builder()
                .commentId(postComment.getCommentId())
                .author(
                        new AuthorResponse(
                                user.getNickname(),
                                user.getProfileImage() != null ? fullUrl : null
                        )
                )
                .content(postComment.getContent())
                .isAuthor(isAuthor)
                .createdAt(postComment.getCreatedAt())
                .build();
    }

    public PostComment toEntity(User user, Post post, String content){
        return PostComment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();
    }

    public PostCommentPageItemResponse toPostCommentPageItemResponse(PostComment postComment, boolean isAuthor){
        User user = postComment.getUser();

        String imageKey = user.getProfileImage() != null ? user.getProfileImage().getStorageKey() : null;
        String fullUrl = imageStorage.publicUrl(imageKey);

        return PostCommentPageItemResponse.builder()
                .commentId(postComment.getCommentId())
                .content(postComment.getContent())
                .author(new AuthorResponse(user.getNickname(),
                        user.getProfileImage() != null ? fullUrl : null))
                .createdAt(postComment.getCreatedAt())
                .isAuthor(isAuthor)
                .build();
    }
}
