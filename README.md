# focused-web-crawler
The program is a command line based interface, designed such that it is able to crawl the web based on a certain topic provided by the user.

### Input: 
* Topic for the crawler to crawl
* Number of pages to be crawled (n)

### Process: 
* Get topic and No. of pages to download (n) from user
* Generate score from Wikipedia page for the topic
* Search Google for the topic and get Seed URL set
* Gather web pages about the topic by crawling the Seed set 
* Rank them according to relevance

### Output:
* Download top n successfully crawled web pages in a folder

### Classes:
* Crawler - Crawls the web using seed analysis and link analysis
* LinkAnalysis - Functions related to link urls
* SeedAnalysis - Functions related to seed urls
* Comparator - Custom comparator for treemap
* Stemmer - Potter's stemming algorithm

## Authors
* [**Anuj Doiphode**](https://github.com/ddanuj)
* [**Tarun Abhichandani**](https://github.com/abistarun)
