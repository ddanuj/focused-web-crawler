# focused-web-crawler
The program is a command line based interface, designed such that it is able to crawl the web based on a certain topic provided by the user.

Input: 
1. Topic for the crawler to crawl
2. Number of pages to be crawled (n)

Process: 
1. Get topic and No. of pages to download (n) from user
2. Generate score from Wikipedia page for the topic
3. Search Google for the topic and get Seed URL set
4. Gather web pages about the topic by crawling the Seed set 
5. Rank them according to relevance

Output:
1. Download top n successfully crawled web pages in a folder

Classes:
1. Crawler - Crawls the web using seed analysis and link analysis
2. LinkAnalysis - Functions related to link urls
3. SeedAnalysis - Functions related to seed urls
4. Comparator - Custom comparator for treemap
5. Stemmer - Potter's stemming algorithm
