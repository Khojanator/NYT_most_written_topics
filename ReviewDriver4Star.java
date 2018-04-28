//package assignment6;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ReviewDriver4Star extends Configured implements Tool{

	public int run(String[] arg0) throws Exception {
		Configuration conf =  new Configuration();
		Job job = Job.getInstance(conf,"review4Stars");
		job.setJobName("Create key value pairs for business with 4 star reviews");

		job.setNumReduceTasks(1);
		
		job.setJarByClass(ReviewDriver4Star.class);
		job.setMapperClass(ReviewMapper4Star.class);
		job.setCombinerClass(ReviewReducer4Star.class);
		job.setReducerClass(ReviewReducer4Star.class);
		
		// set output parameters
		FileInputFormat.addInputPath(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.submit();
		return job.waitForCompletion(true) ? 0 : 1;	
	}
	
	public static void main(String[] args) throws Exception {
		try {
			ToolRunner.run(new ReviewDriver4Star(), args);
		} catch(Exception e) {
			System.out.println("Exception received:" + e.getMessage());
		}
	}
}
