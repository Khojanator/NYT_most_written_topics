import requests
import json

def getNYTdata():
	data_url = "https://api.nytimes.com/svc/search/v2/articlesearch.json"
	params = {
		'api-key' : "6ea06f82208f442f9eea72740f9c9c00",
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