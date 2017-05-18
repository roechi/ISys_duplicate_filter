# Duplicate Detector for News Articles 
[![Build Status](https://travis-ci.org/roechi/ISys_duplicate_filter.svg?branch=master)](https://travis-ci.org/roechi/ISys_duplicate_filter)

## ToDo
- [x] build REST client to fetch article collection
- [x] add persistence layer (MongoDB ?)
- [ ] init articles to mongo
- [ ] implement REST API w/ pagination to serve filtered data set (Spring Boot)
- [ ] implement MinHash filter
- [ ] implement LHS filter

## Open Questions
- runtime comparison for MinHash, LHS and maybe even simple Jaccard similarity
- efficiency and accuracy of k-shingles for different k-values

