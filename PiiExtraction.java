import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.HashMap;
import java.util.List;

public class PiiExtraction {

    private static  StanfordCoreNLP stanfordCoreNLP = PipeLine.getPipeLine();

   public HashMap<String, String> getPiiExtraction(String text) {

        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);

        List<CoreLabel> coreLabelList = coreDocument.tokens();
       HashMap<String, String> piiList = new HashMap<>();
        for(CoreLabel coreLabel : coreLabelList){

           String ner = coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            if(ner != "O"){
                piiList.put(coreLabel.originalText(), ner);
            }

        }

        return piiList;
    }
}
