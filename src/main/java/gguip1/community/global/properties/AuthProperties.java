package gguip1.community.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    private ExcludedPaths excludedPaths = new ExcludedPaths();

    @Getter
    @Setter
    public static class ExcludedPaths {
        private List<String> get = new ArrayList<>();
        private List<String> post = new ArrayList<>();
        private List<String> delete = new ArrayList<>();
    }
}