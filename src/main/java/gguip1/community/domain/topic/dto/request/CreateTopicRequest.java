package gguip1.community.domain.topic.dto.request;

import gguip1.community.domain.topic.entity.TopicStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CreateTopicRequest {
    @NotBlank
    private String title;
    
    private String description;
    
    @NotNull
    private LocalDate targetDate;
    
    @NotNull
    private TopicStatus status;
    
    @NotBlank
    private String optionAText;

    private String optionADescription;
    
    @NotBlank
    private String optionBText;

    private String optionBDescription;
}
