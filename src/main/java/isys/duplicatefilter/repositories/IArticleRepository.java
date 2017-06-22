package isys.duplicatefilter.repositories;

import isys.duplicatefilter.dto.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IArticleRepository extends MongoRepository<Article, String> {

    Article findById(String id);

}
