package com.java.hadoopUtils;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;


public class SmallFilesToSequenceFile {
	
static class SeqFileMapper extends Mapper<NullWritable , BytesWritable , Text, BytesWritable> {

	private Text filename;
	 
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		InputSplit split = context.getInputSplit();
		Path path = ((FileSplit) split).getPath();
		filename = new Text(path.toString());
	}
	
	
	
	@Override
	protected void map(NullWritable key, BytesWritable value,Context context)
			throws IOException, InterruptedException {
		
		 context.write(filename, value);
		
		
	}
	
}



public static void main(String[] args) throws Exception {
	
	
		if (args.length != 2) {

			System.err.println("Usage: <input path>	<output	path>");
			System.exit(-1);

		}
		
	
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJarByClass(SmallFilesToSequenceFile.class);
		job.setJobName("smallfilestoseqfile");
		job.setInputFormatClass(FullFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		job.setNumReduceTasks(1);
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BytesWritable.class);
		job.setMapperClass(SeqFileMapper.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	
	
}
}
