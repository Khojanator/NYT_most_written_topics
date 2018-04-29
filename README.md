# NYT_most_written_topics
Collects NYT data and finds the topics most written about using the keywords in the article through Hadoop Map Reduce framework.

## Creating the dataset
Before you begin, you will need an API key for NYT article search API. You can sign up for one [here](https://developer.nytimes.com/signup). For further documentation on the API results, please visit [NYT developer](https://developer.nytimes.com/) or the [Article Search API](https://developer.nytimes.com/article_search_v2.json).
Once you've retrieved the API key, edit the nyt_api_key.py and store the key as a string in place of *Your_API_Key*.

You can now run the python file to retrieve the dataset you're looking for with a given start and end date. This works well for time ranges of approximately a month. Example:

`$ python getNYTarticleData.py -b 20180301 -e 20180331 -o nytDataMar2018.txt`

## Running the MapReduce application to find most popular keyword(s)
After you have created the dataset of NY Times articles in your desired time range and put it in HDFS, you can run the KeywordNytCountDriver program to find the most popular keyword(s) in your dataset.
### How to use
`$ module load hadoop`

`$ export HADOOP_CLASSPATH=$(hadoop classpath)`

`$ javac -classpath ${HADOOP_CLASSPATH} KeywordNytCountDriver.java`

`$ jar cf nyt.jar Keyword*.class`

`$ hadoop jar nyt.jar KeywordNytCountDriver <input-file-path> output/`


To see the result, run the following command:
`$ hadoop fs -cat output/part-r-00000`

The result will be the keyword with the highest number of articles written about it. If there are multiple keywords with the same highest number of articles, all of them will be returned. The result is in the following format:

`<keyword> <url-1> <url-2> <url-3> <url-4> <url-5>	<number-of-articles-with-keyword>`

The five URLs returned with the keyword are the first five article URLs the program found during the keyword counting phase. It will return fewer than five URLs if the keyword doesn't have five articles.
