package isys.duplicatefilter.services;


import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.repositories.IArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RawArticleService extends ArticleService<Article> {

    @Autowired
    public RawArticleService(IArticleRepository<Article> articleRepository) {
        super(articleRepository);
    }
}
