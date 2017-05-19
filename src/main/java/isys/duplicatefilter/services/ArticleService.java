package isys.duplicatefilter.services;

import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.repositories.IArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private IArticleRepository articleRepository;

    @Autowired
    public ArticleService(IArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public Article fetch(String key) {
        return articleRepository.findByKey(key);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
