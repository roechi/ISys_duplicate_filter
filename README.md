# Duplicate Detector for News Articles 
[![Build Status](https://travis-ci.org/roechi/ISys_duplicate_filter.svg?branch=master)](https://travis-ci.org/roechi/ISys_duplicate_filter)

# Usage

You can query the API via HTTP like this:
```http
GET /articles
```
or this:
```http
GET /filteredArticles
```

Both resources will give you an explanation for further usage of the paginated API. That means you just have to query for certain pages of the document corpus by using a query parameter (who would have thought!). Obviously we don't want to pump out several thousand json objects at once. So for example, the pagination can be used like this:

```http
GET /filteredArticles?page=100 HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: cmts4.f4.htw-berlin.de:8080
User-Agent: HTTPie/0.9.8

HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Date: Mon, 26 Jun 2017 19:02:56 GMT
Transfer-Encoding: chunked
```

which will serve a json list of a hundered (or so) objects like this: 

```json
    [
        {
            "DuplicateIDs": ["74d98c2f-3f7b-2c50-a897-30ca49393fa5", "f84b2e95-f4fe-026c-7c58-0875be120f83"],
            "adresse": "some street",
            "bezirk": "some precinct",
            "einsatzkraefte": {"object": "of some relief forces"},
            "ereigniszeitpunkt": "maybe a timestamp",
            "id": "67a1591b-7e71-effd-fa5d-772173cf7c12",
            "inhalt": "some content",
            "kategorie": "a category",
            "meldungszeitpunkt": "another timestamp",
            "ortsteil": "district",
            "referenzmeldungen": ["a reference", "another reference"],
            "titel": "a title",
            "url": "a url",
            "zeitung": "a journal"
        },
        {
          "DuplicateIDs": [],
          "...": "..."
        }
    ]
```


## ToDo
- [x] build REST client to fetch article collection
- [x] add persistence layer (MongoDB ?)
- [x] init articles to mongo
- [x] implement REST API w/ pagination to serve filtered data set (Spring Boot)
- [x] implement MinHash filter
- [x] implement LHS filter

## Open Questions
- runtime comparison for MinHash, LHS and maybe even simple Jaccard similarity
- efficiency and accuracy of k-shingles for different k-values

