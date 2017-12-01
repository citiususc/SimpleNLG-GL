import simplenlg.features.*;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.galician.XMLLexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.galician.Realiser;

/**
 * Created by Andrea on 04/07/2017.
 */
public class MainTest {
    public static void main(String[] args) {
        Lexicon lexicon = new XMLLexicon();
        NLGFactory nlgFactory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);


        NLGElement s1 = nlgFactory.createSentence("o meu can é feliz");
        String output = realiser.realiseSentence(s1);
        System.out.println(output);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("María");
        p.setVerb("perseguir");
        p.setObject("un mono");
        p.addComplement("moi rápido"); // Frase adverbial, pasada como unha cadena
        p.addComplement("a pesar do seu esgotamento"); // Frase preposicional

        String output2 = realiser.realiseSentence(p);
        System.out.println(output2);

        PhraseElement min = nlgFactory.createAdjectivePhrase("superior");
        min.setPlural(true);
        min.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        output2 = realiser.realiseSentence(min);
        System.out.println(output2);
    }
}