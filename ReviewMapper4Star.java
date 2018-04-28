//package assignment6;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ReviewMapper4Star extends Mapper<LongWritable,Text, Text, IntWritable>{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] strTk = value.toString().split(",");
		if (strTk.length > 4){
			if (strTk[0].length() == 24 && strTk[1].length() == 24 && strTk[2].length() == 24){
				String  stars = strTk[3];
				if (stars.equals("\"4\"")){
					context.write(new Text(strTk[2]), new IntWritable(1));
				}
			}
		}
	}
}