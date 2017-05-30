package isys.duplicatefilter.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@Document
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Article {

    @Id
    @JsonIgnore
    private String key;
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DuplicateIDs")
    private Set<String> duplicateIds = new HashSet<>();
    @JsonProperty("Referenzmeldungen")
    private List<String> referenceReports;
    @JsonProperty("Ereigniszeitpunkt")
    private String date;
    @JsonProperty("Ortsteil")
    private String district;
    @JsonProperty("Kategorie")
    private String category;
    @JsonProperty("Zeitung")
    private String journal;
    @JsonProperty("url")
    private String url;
    @JsonProperty("Bezirk")
    private String precinct;
    @JsonProperty("Adresse")
    private String address;
    @JsonProperty("Einsatzkr\u00e4fte")
    private Object reliefForces;
    @JsonProperty("Meldungszeitpunkt")
    private String messageTimestamp;
    @JsonProperty("Titel")
    private String title;
    @JsonProperty("Inhalt")
    private String content;
}
