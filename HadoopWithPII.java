

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

public class HadoopWithPII extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {

        Job job = new Job(getConf(), "pii Extraction");
        job.setJarByClass(HadoopWithPII.class);

        job.setMapperClass(PIIExtractionMapper.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path("/input"));
        FileOutputFormat.setOutputPath(job, new Path("/output"));
        int a = job.waitForCompletion(true) ? 0 : 1;
        return a;
    }

    public static class PIIExtractionMapper
            extends Mapper<Text, Text, Text, Text> {

        @Override
        protected void map(Text key, Text value, Context context)
                throws IOException, InterruptedException {

            File f = new File(value.toString());
            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line = "";

            StringBuilder br = new StringBuilder();

            while((line = bf.readLine()) != null){
                br.append(line);
            }

            HashMap<String, String> piiList =  new PiiExtraction().getPiiExtraction(br.toString());
//            for(Map.Entry<String, String> list : piiList.entrySet()){
//                String output = list.getKey() + "   " + list.getValue();
//
//
//            }
            context.write(key, new Text(piiList.toString()));
        }
    }

}


