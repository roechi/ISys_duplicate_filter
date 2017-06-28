package isys.duplicatefilter.services;

import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.repositories.IArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class ArticleService<T extends Article> {

    public static final int ARTICLES_PER_PAGE = 100;
    private final IArticleRepository<T> articleRepository;

    public ArticleService(IArticleRepository<T> articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void save(T article) {
        articleRepository.save(article);
    }

    public T fetch(String id) {
        return articleRepository.findById(id);
    }

    public List<T> findAll() {
        return articleRepository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public int getNumberOfPages() {
        PageRequest pageRequest = new PageRequest(0, ARTICLES_PER_PAGE);
        Page<T> all = articleRepository.findAll(pageRequest);
        return all.getTotalPages();
    }

    public List<T> getPage(Integer pageNumber) {
        PageRequest pageRequest = new PageRequest(pageNumber, ARTICLES_PER_PAGE);
        Page<T> all = articleRepository.findAll(pageRequest);
        return newArrayList(all);
    }
}
