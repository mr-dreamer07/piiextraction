import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

public class App  {
    public static void main(String[] args) throws Exception {
       int res =  ToolRunner.run(new Configuration(), new HadoopWithPII(), args);
       System.exit(res);
    }
}
