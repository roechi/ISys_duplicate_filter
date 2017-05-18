package isys.duplicatefilter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Accessors(chain = true)
@Document
public class Article {

    @Id
    private String key;
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
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Bezirk")
    private String precinct;
    @JsonProperty("Einsatzkr\u00e4fte")
    private Object reliefForces;
    @JsonProperty("Meldungszeitpunkt")
    private String messageTimestamp;
    @JsonProperty("Titel")
    private String title;
    @JsonProperty("Inhalt")
    private String content;
}
