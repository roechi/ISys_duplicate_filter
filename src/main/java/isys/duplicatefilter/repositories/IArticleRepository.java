package isys.duplicatefilter.repositories;

import isys.duplicatefilter.dto.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IArticleRepository<T extends Article> extends MongoRepository<T, String> {

    T findById(String id);
}
