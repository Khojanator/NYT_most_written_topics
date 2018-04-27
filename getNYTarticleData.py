import requests
import json
from nyt_api_key import API_KEY

def getNYTdata():
	data_url = "https://api.nytimes.com/svc/search/v2/articlesearch.json"
	params = {
		'api-key' : API_KEY,
		'begin_date': "20180326",
		'end_date': "20180426"
	}

	response =  requests.get(data_url, params=params)
	content = response.content.decode("utf-8")
	json_list = json.loads(content)["response"]["docs"]
	for item in json_list:
		print(item["keywords"], item["web_url"])

if __name__ == '__main__':
	getNYTdata()