import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class KeywordNytCountDriver extends Configured implements Tool {
	/* Mapper for counting occurences of a keyword */
	public static class KeywordCountMapper extends Mapper<LongWritable, Text, Text, Text>{
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			try {
					String[] strTk = value.toString().split("~-~");
					context.write(new Text(strTk[0]), new Text(strTk[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* Reducer for counting occurences of a keyword */
	public static class KeywordCountReducer extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			try {
				long wordCount = 0;
				String urls = "";
				for (Text val : values) {
					wordCount += 1;
					urls += val + " ";
				}
				urls = Long.toString(wordCount) + " " + urls;
				context.write(key, new Text(urls));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* Mapper for finding the most popular keyword(s).
	 	This put the keyword count as key in the key-value pair so that the
		sorted result will give us the max. */
	public static class KeywordMaxMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
		public void map(LongWritable key, Text value, Context context) {
			try {
				String[] strTk = value.toString().split("\t");
				if (strTk.length == 2){
					String keywordCount = strTk[1].substring(0, strTk[1].indexOf(" "));
					String keywordAndUrl = strTk[0] + " " + strTk[1].substring(strTk[1].indexOf(" "));
					context.write(new LongWritable(Integer.parseInt(keywordCount)), new Text(keywordAndUrl));
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	/* Reducer for finding the most popular keyword(s).
	 	This is a simple top N reducer with N=1. */
	public static class KeywordMaxReducer extends Reducer<LongWritable, Text, Text, LongWritable> {
		int count = 0;

		public void reduce(LongWritable key, Iterable<Text> values, Context context) {
			if(count < 1) {
				try {
					for(Text value: values) {
						context.write(value, key);
						count++;
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* Combiner to make sure we are not sending all the data to a single reducer.
		Only the max key-value pair(s) will leave the combiner. */
	public static class KeywordMaxCombiner extends Reducer<LongWritable, Text, LongWritable, Text> {
		int count = 0;

		@Override
		public void reduce(LongWritable key, Iterable<Text> values, Context context) {
				if(count < 1) {
					try {
						for(Text value: values) {
							context.write(key, value);
							count++;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}

	}

	/* Output path for the intermediate output of the first MapReduce job */
	private static final String OUTPUT_PATH = "intermediate_output";

	@Override
	 public int run(String[] args) throws Exception {
	  /* Job 1 - Count all occurences of every keyword */
	  Configuration conf = getConf();
	  Job job = Job.getInstance(conf, "Count keywords");
	  job.setJarByClass(KeywordNytCountDriver.class);

	  job.setMapperClass(KeywordCountMapper.class);
	  job.setReducerClass(KeywordCountReducer.class);

	  job.setOutputKeyClass(Text.class);
	  job.setOutputValueClass(Text.class);

	  FileInputFormat.addInputPath(job, new Path(args[0]));
	  FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

	  job.waitForCompletion(true);

	  /* Job 2 - Find most popular keyword(s) */
	  Job job2 = Job.getInstance(conf, "Find most popular keyword(s)");
	  job2.setJarByClass(KeywordNytCountDriver.class);

	  job2.setMapperClass(KeywordMaxMapper.class);
	  job2.setReducerClass(KeywordMaxReducer.class);
		job2.setCombinerClass(KeywordMaxCombiner.class);
		job2.setSortComparatorClass(LongWritable.DecreasingComparator.class);

		job2.setMapOutputKeyClass(LongWritable.class);
		job2.setMapOutputValueClass(Text.class);

	  FileInputFormat.addInputPath(job2, new Path(OUTPUT_PATH));
	  FileOutputFormat.setOutputPath(job2, new Path(args[1]));

	  return job2.waitForCompletion(true) ? 0 : 1;
	 }

	public static void main(String[] args) throws Exception {
	  if (args.length != 2) {
	   System.err.println("Enter valid number of arguments <Input-Directory>  <Output-Directory>");
	   System.exit(0);
	  }
	  ToolRunner.run(new Configuration(), new KeywordNytCountDriver(), args);
	}
}
