package isys.duplicatefilter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Accessors(chain = true)
@Document
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Article {

    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("referenzmeldungen")
    private List<String> referenceReports;
    @JsonProperty("ereigniszeitpunkt")
    private String date;
    @JsonProperty("ortsteil")
    private String district;
    @JsonProperty("kategorie")
    private String category;
    @JsonProperty("zeitung")
    private String journal;
    @JsonProperty("url")
    private String url;
    @JsonProperty("bezirk")
    private String precinct;
    @JsonProperty("adresse")
    private String address;
    @JsonProperty("einsatzkraefte")
    private Object reliefForces;
    @JsonProperty("meldungszeitpunkt")
    private String messageTimestamp;
    @JsonProperty("titel")
    private String title;
    @JsonProperty("inhalt")
    private String content;
}
