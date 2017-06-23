package isys.duplicatefilter.repositories;

import isys.duplicatefilter.dto.FilteredArticle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IFilteredArticleRepository extends MongoRepository<FilteredArticle, String> {

}
