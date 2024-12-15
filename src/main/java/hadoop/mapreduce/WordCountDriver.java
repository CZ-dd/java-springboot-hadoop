package hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.net.URI;

public class WordCountDriver {
    public static void main(String[] args) throws Exception{
        Configuration configuration = new Configuration();
        // configuration.set("fs.defaultFS", nameNode);
        configuration.set("fs.defaultFS", "hdfs://node1:8020");
        Job job = Job.getInstance(configuration, "MyWordCount");
        // String[] otherArgs = new GenericOptionsParser(configuration, args).getRemainingArgs();   // 其他参数
        String inPath = "hdfs://node1:8020/input/test1.txt";
        String outPath = "hdfs://node1:8020/output/test1/";
        // FileSystem fs = FileSystem.get(configuration);
        FileSystem fs = FileSystem.get(URI.create("hdfs://node1:8020"), configuration, "hadoop");
        if(fs.exists(new Path(outPath))){
            // 存在的话删除
            fs.delete(new Path(outPath), true);
        }
        job.setJarByClass(WordCountDriver.class);
        // job.setJarByClass(WordCountDriver.class);
        job.setMapperClass(WordCountMapper.class);
        job.setCombinerClass(WordCountReducer.class);
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(inPath));
        FileOutputFormat.setOutputPath(job, new Path(outPath));
        System.exit((job.waitForCompletion(true) ? 0 : 1));
    }
}
