# NYT_most_written_topics
Collects NYT data and finds the topics most written about using the keywords in the article through Hadoop Map Reduce framework.

## Creating the dataset
Before you begin, you will need an API key for NYT article search API. You can sign up for one [here](https://developer.nytimes.com/signup). For further documentation on the API results, please visit [NYT developer](https://developer.nytimes.com/) or the [Article Search API](https://developer.nytimes.com/article_search_v2.json).
Once you've retrieved the API key, edit the nyt_api_key.py and store the key as a string in place of *Your_API_Key*.

You can now run the python file to retrieve the dataset you're looking for with a given start and end date. This works well for time ranges of approximately a month. Example:

`$ python getNYTarticleData.py -b 20180301 -e 20180331 -o nytDataMar2018.txt`