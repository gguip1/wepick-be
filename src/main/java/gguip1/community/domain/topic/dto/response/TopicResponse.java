package gguip1.community.domain.topic.dto.response;

import gguip1.community.domain.topic.entity.Topic;
import gguip1.community.domain.topic.entity.TopicStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class TopicResponse {
    private Long topicId;
    private String title;
    private String description;
    private LocalDate targetDate;
    private TopicStatus status;
    private List<TopicOptionResponse> options;
    private Long totalVotes;
    private Long votedOptionId; // Null if not voted

    public TopicResponse(Topic topic, List<TopicOptionResponse> options, Long totalVotes, Long votedOptionId) {
        this.topicId = topic.getTopicId();
        this.title = topic.getTitle();
        this.description = topic.getDescription();
        this.targetDate = topic.getTargetDate();
        this.status = topic.getStatus();
        this.options = options;
        this.totalVotes = totalVotes;
        this.votedOptionId = votedOptionId;
    }
}
