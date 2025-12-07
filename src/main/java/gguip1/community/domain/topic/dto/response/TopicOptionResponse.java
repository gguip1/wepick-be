package gguip1.community.domain.topic.dto.response;

import gguip1.community.domain.topic.entity.OptionLabel;
import gguip1.community.domain.topic.entity.TopicOption;
import lombok.Getter;

@Getter
public class TopicOptionResponse {
    private Long optionId;
    private OptionLabel label;
    private String text;
    private String description;
    private Long voteCount;
    private Integer percent;

    public TopicOptionResponse(TopicOption option, Long totalVotes) {
        this.optionId = option.getOptionId();
        this.label = option.getLabel();
        this.text = option.getText();
        this.description = option.getDescription();
        this.voteCount = option.getVoteCount();
        this.percent = (totalVotes > 0) ? (int) Math.round(((double) option.getVoteCount() / totalVotes) * 100) : 0;
    }
}
