//package assignment6;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ReviewMapperMax extends Mapper<LongWritable, Text, LongWritable, Text>{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] strTk = value.toString().split("\t");
		if (strTk.length == 2){
			context.write(new LongWritable(Integer.parseInt(strTk[1])), new Text(strTk[0]));
		}
	}
}
