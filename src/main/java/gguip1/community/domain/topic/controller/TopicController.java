package gguip1.community.domain.topic.controller;

import gguip1.community.domain.topic.dto.request.CreateTopicRequest;
import gguip1.community.domain.topic.dto.request.UpdateTopicRequest;
import gguip1.community.domain.topic.dto.request.VoteRequest;
import gguip1.community.domain.topic.dto.response.TopicListResponse;
import gguip1.community.domain.topic.dto.response.TopicResponse;
import gguip1.community.domain.topic.service.TopicService;
import gguip1.community.global.auth.annotation.Auth;
import gguip1.community.global.context.SecurityContext;
import gguip1.community.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<TopicResponse>> getTodayTopic() {
        Long userId = SecurityContext.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Today's topic retrieved", topicService.getTodayTopic(userId)));
    }

    @Auth
    @PostMapping("/{topicId}/vote")
    public ResponseEntity<ApiResponse<Void>> vote(
            @PathVariable Long topicId,
            @RequestBody @Valid VoteRequest request
    ) {
        Long userId = SecurityContext.getCurrentUserId();
        topicService.vote(topicId, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Vote successful", null));
    }

    @Auth
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createTopic(@RequestBody @Valid CreateTopicRequest request) {
        // TODO: 추후 관리자 권한 체크 로직 추가 필요
        Long topicId = topicService.createTopic(request);
        return ResponseEntity.status(201).body(ApiResponse.success("Topic created", topicId));
    }

    @Auth
    @PatchMapping("/{topicId}")
    public ResponseEntity<ApiResponse<Void>> updateTopic(
            @PathVariable Long topicId,
            @RequestBody @Valid UpdateTopicRequest request
    ) {
        // TODO: 추후 관리자 권한 체크 로직 추가 필요
        topicService.updateTopic(topicId, request);
        return ResponseEntity.ok(ApiResponse.success("Topic updated", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TopicListResponse>>> getTopicArchive(
            @PageableDefault(sort = "targetDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success("Topic archive retrieved", topicService.getTopicArchive(pageable)));
    }
}
