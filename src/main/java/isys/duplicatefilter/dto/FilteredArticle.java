package isys.duplicatefilter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@Document
public class FilteredArticle extends Article{

    @JsonProperty("DuplicateIDs")
    private Set<String> duplicateIds = new HashSet<>();
}
