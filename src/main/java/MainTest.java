import simplenlg.features.*;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.galician.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.galician.Realiser;

/**
 * Created by Andrea on 04/07/2017.
 */
public class MainTest {
    public static void main(String[] args) {
        Lexicon lexicon = new XMLLexicon();
        NLGFactory nlgFactory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);


       /* NLGElement s1 = nlgFactory.createSentence("o meu can é feliz");
        String output = realiser.realiseSentence(s1);
        System.out.println(output);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("María");
        p.setVerb("perseguir");
        p.setObject("un mono");
        p.addComplement("moi rápido"); // Frase adverbial, pasada como unha cadena
        p.addComplement("a pesar do seu esgotamento"); // Frase preposicional

      /*  String output2 = realiser.realiseSentence(p);
        System.out.println(output2);*/

       /* PhraseElement min = nlgFactory.createAdjectivePhrase("superior");
        min.setPlural(true);
        min.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        output2 = realiser.realiseSentence(min);
        System.out.println(output2);*/

       /* SPhraseSpec p = nlgFactory.createClause();
        NPPhraseSpec subject1 = nlgFactory.createNounPhrase("María");
        NPPhraseSpec subject2 = nlgFactory.createNounPhrase("unha", "xirafa");

        CoordinatedPhraseElement subj = nlgFactory.createCoordinatedPhrase(subject1, subject2);
        p.setSubject(subj);

        NPPhraseSpec object1 = nlgFactory.createNounPhrase("un mono");
        NPPhraseSpec object2 = nlgFactory.createNounPhrase("a Xurxo");

        CoordinatedPhraseElement obj = nlgFactory.createCoordinatedPhrase(object1, object2);
        obj.addCoordinate("a Marta");
        p.setObject(obj);
        obj.setFeature(Feature.CONJUNCTION, "ou");

        String output = realiser.realiseSentence(p);
        System.out.println(output);*/

     /*  NPPhraseSpec p = nlgFactory.createNounPhrase("diminución");
       p.addModifier("notábel");
       p.setFeature(LexicalFeature.GENDER, Gender.FEMININE);

       String output = realiser.realiseSentence(p);
        System.out.println(output);*/

        PhraseElement subject = nlgFactory.createPrepositionPhrase("con", "valores que");
        subject.addPostModifier("globalmente");
        SPhraseSpec option1 = nlgFactory.createClause(subject, "atopar");
        option1.setFeature(Feature.TENSE, Tense.FUTURE);
        option1.setPlural(true);
        option1.getVerbPhrase().setFeature(LexicalFeature.REFLEXIVE, true);
        //option1.addPostModifier(nlgFactory.createPrepositionPhrase(t_var.getLabel(token)));

        String output = realiser.realiseSentence(option1);
        System.out.println(output);
    }
}