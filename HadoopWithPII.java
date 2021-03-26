

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


import org.apache.hadoop.conf.Configuration;
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

public class HadoopWithPII {

        public static class PIIExtractionMapper
                extends Mapper<Text, Text, Text, Text> {



            public void map(Text key, Text value, Context context) throws IOException, InterruptedException {

                File f = new File(value.toString());
                BufferedReader bf = new BufferedReader(new FileReader(f));
                String line = "";

                StringBuilder br = new StringBuilder();

                while((line = bf.readLine()) != null){
                    br.append(line);
                }

                HashMap<String, String> piiList =  new PiiExtraction().getPiiExtraction(br.toString());
                for(Map.Entry<String, String> list : piiList.entrySet()){
                    String output = list.getKey() + "   " + list.getValue();

                    context.write(key, new Text(output));
                }



            }
        }



        public static void main(String[] args) throws Exception {

            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "pii Extraction");
            job.setJarByClass(HadoopWithPII.class);
            job.setMapperClass(PIIExtractionMapper.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            job.setInputFormatClass(KeyValueTextInputFormat.class);
            FileInputFormat.addInputPath(job, new Path("/input"));
            FileOutputFormat.setOutputPath(job, new Path("/output"));
            System.exit(job.waitForCompletion(true) ? 0 : 1);

        }
    }

