
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.Properties;

public class PipeLine {
   private static Properties properties;
   private static String nameOfProperties = "tokenize, ssplit, pos, lemma, ner";
   private static StanfordCoreNLP stanfordCoreNLP;

   public static Properties settingProperties(){
       properties = new Properties();
       properties.setProperty("annotators", nameOfProperties);

       return properties;
   }

   public static StanfordCoreNLP getPipeLine(){

       if(stanfordCoreNLP == null){
           stanfordCoreNLP = new StanfordCoreNLP(settingProperties());
       }

       return stanfordCoreNLP;
   }

}
