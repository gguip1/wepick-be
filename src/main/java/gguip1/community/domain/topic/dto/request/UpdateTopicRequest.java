package gguip1.community.domain.topic.dto.request;

import gguip1.community.domain.topic.entity.TopicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateTopicRequest {
    private String title;
    private String description;
    private LocalDate targetDate;
    private TopicStatus status;
    private String optionAText;
    private String optionADescription;
    private String optionBText;
    private String optionBDescription;
}
