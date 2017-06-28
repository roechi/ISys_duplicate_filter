package isys.duplicatefilter.controllers;

import isys.duplicatefilter.dto.ErrorMessage;
import isys.duplicatefilter.dto.Pages;
import isys.duplicatefilter.exceptions.BadRequestException;
import isys.duplicatefilter.services.ArticleService;
import isys.duplicatefilter.services.FilteredArticleService;
import isys.duplicatefilter.services.RawArticleService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Preconditions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final FilteredArticleService filteredArticleService;
    private final RawArticleService rawArticleService;

    @GetMapping("/articles/page/{pageNumber}")
    public ResponseEntity getArticles(@PathVariable(required = false) Integer pageNumber) {
        return getResponseEntity(pageNumber, rawArticleService);
    }

    @GetMapping("/duplicates")
    public ModelAndView getDuplicates() {
        return new ModelAndView("redirect:/articles?page=1");
    }

    @GetMapping(path = "/filteredArticles/page/{pageNumber}")
    public ResponseEntity getFilteredArticles(@PathVariable(required = false) Integer pageNumber) throws IOException, URISyntaxException {
        return getResponseEntity(pageNumber, filteredArticleService);
    }

    @GetMapping("/{articleType}/pages")
    public ResponseEntity getNumberOfPages(@PathVariable String articleType) {
        if (articleType.equals("articles")) {
            return new ResponseEntity<>(new Pages(rawArticleService.getNumberOfPages()), HttpStatus.OK);
        }
        if (articleType.equals("filteredArticles")) {
            return new ResponseEntity<>(new Pages(rawArticleService.getNumberOfPages()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorMessage("Allowed resources are /articles/pages and /filteredArticles/pages"), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity getResponseEntity(@RequestParam Integer page, ArticleService<?> service) {
        return Optional.ofNullable(page)
                .map(pageNumber -> {
                    List<?> fetchedPage = getPage(pageNumber, service);
                    return new ResponseEntity<>(newArrayList(fetchedPage), HttpStatus.OK);
                }).orElseThrow(() -> {
                    String message = MessageFormat.format(
                            "There are {0} pages, choose one. Counting starts at 0. {1}. {2}.",
                            service.getNumberOfPages(),
                            "Example: GET /articles/page/3 or GET /filteredArticles/page/3",
                            "To get the number of pages try GET /articles/pages or GET /filteredArticles/pages"
                    );
                    return new BadRequestException(message);
                });
    }

    private List getPage(Integer pageNumber, ArticleService service) {
        Preconditions.checkArgument(pageNumber >= 0 && pageNumber <service.getNumberOfPages(), "Invalid page number " + pageNumber + ".");
        return service.getPage(pageNumber);
    }
}
