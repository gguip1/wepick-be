package gguip1.community.domain.topic.dto.response;

import gguip1.community.domain.topic.entity.Topic;
import gguip1.community.domain.topic.entity.TopicStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TopicListResponse {
    private Long topicId;
    private String title;
    private LocalDate targetDate;
    private TopicStatus status;

    public TopicListResponse(Topic topic) {
        this.topicId = topic.getTopicId();
        this.title = topic.getTitle();
        this.targetDate = topic.getTargetDate();
        this.status = topic.getStatus();
    }
}
