import requests
import json
from nyt_api_key import API_KEY
import argparse
from dateutil.parser import parse
from time import sleep

USE = "This program gets NYT data for a particular data range with keywords of the article and the web_url.\n Example: user$ python getNYTarticleData.py -b YYYYMMDD -e YYYYMMDD"
DateRangeStart = 20150101
DateRangeEndYear = 20180425

def argument_handler():
	parser = argparse.ArgumentParser(description=USE, formatter_class=argparse.RawTextHelpFormatter)

	parser.add_argument('-b', '--begin_date', help='Enter the beginning date for date range in YYYYMMDD format', default="20180101")
	parser.add_argument('-e', '--end_date', help='Enter the ending date for date range in YYYYMMDD format', default="20180425")
	parser.add_argument('-o', '--output_file', help='Enter the name of the output file', default="OutTestData.txt")

	return parser.parse_args()

def is_date(date_str):
	try:
		x = parse(date_str)
		return True
	except:
		return False 

def proper_date(begin_date, end_date):
	return not((eval(end_date) <= eval(begin_date)) or not(is_date(end_date)) or not(is_date(begin_date)) or not(DateRangeStart <= eval(begin_date) <= DateRangeEndYear) or not(DateRangeStart <= eval(end_date) <= DateRangeEndYear))

def getNYTdata(begin_date, end_date, outfile):
	for page_num in range(100):
		data_url = "https://api.nytimes.com/svc/search/v2/articlesearch.json"
		params = {
			'api-key' : API_KEY,
			'begin_date': begin_date,
			'end_date': end_date,
			'page': page_num
		}

		response =  requests.get(data_url, params=params)
		content = response.content.decode("utf-8")
		json_list = json.loads(content)["response"]["docs"]
		for json_obj in json_list:
			obj_url = json_obj["web_url"]
			obj_keywords = json_obj["keywords"]
			if obj_keywords:
				for item in obj_keywords:
					print("{0}~-~{1}".format(item['value'], obj_url), file=outfile)
		sleep(1)

if __name__ == '__main__':
	args = argument_handler()
	begin_date = args.begin_date
	end_date = args.end_date
	output_file = args.output_file

	file_writer = open(output_file, "w")
	if proper_date(begin_date, end_date):
		getNYTdata(begin_date, end_date, file_writer)
	else:
		print("The dates entered are invalid. Program is exiting.")