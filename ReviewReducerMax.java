//package assignment6;
// Reference: http://kamalnandan.com/hadoop/how-to-find-top-n-values-using-map-reduce/

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReviewReducerMax extends Reducer<LongWritable, Text, Text, LongWritable> {
	int mCount = 0;
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		mCount = 0;
	}
	
	public void reduce(LongWritable key, Iterable<Text> values, Context context) {
		if(mCount < 1) {
			try {
				for(Text value:values) {
					context.write(value, key);
					mCount++;
				}
			} catch(Exception e) {				
			}
		}
	}
}
