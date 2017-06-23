package isys.duplicatefilter.services;


import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.repositories.IRawArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RawArticleService extends ArticleService<Article> {

    @Autowired
    public RawArticleService(IRawArticleRepository articleRepository) {
        super(articleRepository);
    }
}
