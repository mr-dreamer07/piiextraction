import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;

import java.util.HashMap;
import java.util.List;

public class PiiExtraction  {


    private static  StanfordCoreNLP stanfordCoreNLP = PipeLine.getPipeLine();

    public HashMap<String, String> getPiiExtraction(String text) {

        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreLabel> coreLabelList = coreDocument.tokens();
        HashMap<String, String> piiList = new HashMap<>();
        String prevStr = "", prevNer = "O";
        for(CoreLabel coreLabel : coreLabelList){

            String ner = coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class);

            if(ner.equalsIgnoreCase(prevNer)){

                if(!prevNer.equalsIgnoreCase("O")){
                    prevStr += " " + coreLabel.originalText();
                }
                else {
                    prevStr = "";
                }


            }
            else {
                if(prevNer.equalsIgnoreCase("O")){
                    prevStr = coreLabel.originalText();
                }
                else {
                    piiList.put(prevStr, prevNer);
                    prevStr = coreLabel.originalText();
                }
            }

            prevNer = ner;
        }

        if(!prevNer.equalsIgnoreCase("O")){
            piiList.put(prevStr, prevNer);
        }

        return piiList;
    }

//    public static void main(String[] args) {
//        HashMap<String, String> list = new PiiExtraction().getPiiExtraction("Knovos India pvt Ltd is the best company to work with and New York is the best city i want to live");
//        System.out.println(list.toString());
//    }

}

