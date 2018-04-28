import requests
import json
from nyt_api_key import API_KEY
import argparse
from dateutil.parser import parse

USE = "This program gets NYT data for a particular data range with keywords of the article and the web_url.\n Example: user$ python getNYTarticleData.py -b YYYYMMDD -e YYYYMMDD"
DateRangeStart = 20150101
DateRangeEndYear = 20180425

def argument_handler():
	parser = argparse.ArgumentParser(description=USE, formatter_class=argparse.RawTextHelpFormatter)

	parser.add_argument('-b', '--begin_date', help='Enter the beginning date for date range in YYYYMMDD format', default="20180101")
	parser.add_argument('-e', '--end_date', help='Enter the ending date for date range in YYYYMMDD format', default="20180425")

	return parser.parse_args()

def is_date(date_str):
	try:
		x = parse(date_str)
		return True
	except:
		return False 

def proper_date(begin_date, end_date):
	return not((eval(end_date) <= eval(begin_date)) or not(is_date(end_date)) or not(is_date(begin_date)) or not(DateRangeStart <= eval(begin_date) <= DateRangeEndYear) or not(DateRangeStart <= eval(end_date) <= DateRangeEndYear))

def getNYTdata(begin_date, end_date):
	data_url = "https://api.nytimes.com/svc/search/v2/articlesearch.json"
	params = {
		'api-key' : API_KEY,
		'begin_date': begin_date,
		'end_date': end_date
	}

	response =  requests.get(data_url, params=params)
	content = response.content.decode("utf-8")
	json_list = json.loads(content)["response"]["docs"]
	for item in json_list:
		print(item["keywords"], item["web_url"])

if __name__ == '__main__':
	args = argument_handler()
	begin_date = args.begin_date
	end_date = args.end_date

	if proper_date(begin_date, end_date):
		getNYTdata(begin_date, end_date)
	else:
		print("The dates entered are invalid. Program is exiting.")