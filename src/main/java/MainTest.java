import simplenlg.format.english.HTMLFormatter;
import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.lexicon.galician.XMLLexicon;
import simplenlg.realiser.galician.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

import java.util.Arrays;

/**
 * Created by Andrea on 04/07/2017.
 */
public class MainTest {
    public static void main(String[] args) {
        //dicionario galego
        Lexicon lexicon = new XMLLexicon();
        //objeto que crea la estructura
        NLGFactory nlgFactory = new NLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);
        String output;

        //transforma las estructuras en texto -> ok
      /*  NLGElement s1 = nlgFactory.createSentence("o seu can é feliz");
        output = realiser.realiseSentence(s1);
        System.out.println(output);*/

        //presente -> ok
       /* NPPhraseSpec pro = nlgFactory.createNounPhrase("John");

        pro = nlgFactory.createNounPhrase("John");
        pro.setFeature(Feature.PRONOMINAL, true);
        pro.setFeature(Feature.PERSON, Person.THIRD);
        pro.setFeature(LexicalFeature.GENDER, Gender.FEMININE);


        SPhraseSpec sent = nlgFactory.createClause("Mary", "constituír", pro);

        //sent.setFeature(Feature.FORM, Form.CONJUGATE_INFINITIVE);
        sent.setFeature(Feature.TENSE, Tense.PAST);

        sent.setIndirectObject("lle");
        System.out.println(realiser.realiseSentence(sent));*/


      /* pro.setFeature(Feature.PRONOMINAL, true);
        pro.setFeature(Feature.PERSON, Person.THIRD);
        pro.setFeature(LexicalFeature.GENDER, Gender.FEMININE);

        NPPhraseSpec subject2 = nlgFactory.createNounPhrase("el");
        subject2.setFeature(LexicalFeature.GENDER, Gender.MASCULINE);
        //subject2.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject(subject2);
        p.setObject(pro);
        p.setIndirectObject("lle");
        p.setVerb("saltar");
        p.setFeature(Feature.TENSE, Tense.PRESENT);
        //p.setObject("un mono");
        output = realiser.realiseSentence(p);
        System.out.println(output);*/


        //pasado -> ok
       /* SPhraseSpec p1 = nlgFactory.createClause();
        p1.setSubject("María");
        p1.setVerb("perseguir");
        p1.setObject("un mono");
        p1.setFeature(Feature.TENSE, Tense.PLUPERFECT);
        output = realiser.realiseSentence(p1);
        System.out.println(output);

        //futuro -> ok
        SPhraseSpec p2 = nlgFactory.createClause();
        p2.setSubject("María");
        p2.setVerb("perseguir");
        p2.setObject("un mono");
        p2.setFeature(Feature.TENSE, Tense.FUTURE);
        output = realiser.realiseSentence(p2);
        System.out.println(output);

        //futuro negativa -> ok
        SPhraseSpec p3 = nlgFactory.createClause();
        p3.setSubject("María");
        p3.setVerb("perseguir");
        p3.setObject("un mono");
        p3.setFeature(Feature.TENSE, Tense.FUTURE);
        p3.setFeature(Feature.NEGATED, true);
        output = realiser.realiseSentence(p3);
        System.out.println(output);*/

        //interrogativa -> ok
       /* SPhraseSpec p4 = nlgFactory.createClause();
        p4.setSubject("María");
        p4.setVerb("perseguir");
        p4.setObject("un mono");
        p4.setFeature(Feature.TENSE, Tense.FUTURE);
        p4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        output = realiser.realiseSentence(p4);
        System.out.println(output);

        //interrogativa -> ok
        SPhraseSpec p5 = nlgFactory.createClause();
        p5.setSubject("María");
        p5.setVerb("perseguir");
        p5.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        output = realiser.realiseSentence(p5);
        System.out.println(output);

       //complementos -> ok
        SPhraseSpec p6 = nlgFactory.createClause();
        p6.setSubject("María");
        p6.setVerb("perseguir");
        p6.setObject("un mono");
        p6.addComplement("moi rápido");
        p6.addComplement("a pesar do seu agotamento");
        output = realiser.realiseSentence(p6);
        System.out.println(output);

        //complementos -> ok? (rápida lo añade después del sustantivo)
        NPPhraseSpec subject = nlgFactory.createNounPhrase("María");
        NPPhraseSpec object = nlgFactory.createNounPhrase("un mono");
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("perseguir");
        subject.addModifier("rápida");
        verb.addModifier("rápidamente");
        SPhraseSpec p7 = nlgFactory.createClause();
        p7.setSubject(subject);
        p7.setObject(object);
        p7.setVerb(verb);
        output = realiser.realiseSentence(p7);
        System.out.println(output);

        //varios sujetos ->ok
        NPPhraseSpec subject1 = nlgFactory.createNounPhrase("María");
        NPPhraseSpec subject2 = nlgFactory.createNounPhrase("teu", "xirafas");
        CoordinatedPhraseElement subj = nlgFactory.createCoordinatedPhrase(subject1, subject2);
        SPhraseSpec p8 = nlgFactory.createClause();
        p8.setSubject(subj);
        p8.setObject(object);
        verb = nlgFactory.createVerbPhrase("perseguir");
        p8.setVerb(verb);
        output = realiser.realiseSentence(p8);
        System.out.println(output);

        //varios complementos -> ok
        NPPhraseSpec object1 = nlgFactory.createNounPhrase("un mono");
        NPPhraseSpec object2 = nlgFactory.createNounPhrase("a Jorge");
        CoordinatedPhraseElement obj = nlgFactory.createCoordinatedPhrase(object1, object2);
        obj.addCoordinate("a Marta");
        obj.setFeature(Feature.CONJUNCTION, "ou");
        SPhraseSpec p9 = nlgFactory.createClause();
        p9.setSubject(subj);
        p9.setObject(object);
        p9.setVerb(verb);
        p9.setObject(obj);
        output = realiser.realiseSentence(p9);
        System.out.println(output);

        //frase preposicional -> ok
        SPhraseSpec p10 = nlgFactory.createClause("María", "perseguir", "un mono");
        //se añade como complemento
        p10.addComplement("no parque");
        output = realiser.realiseSentence(p10);
        System.out.println(output);

        //otra forma de crear una frase preposicional -> ok
        NPPhraseSpec place = nlgFactory.createNounPhrase("o", "parque");
        place.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        PPPhraseSpec pp = nlgFactory.createPrepositionPhrase("de");
        pp.addComplement(place);
        SPhraseSpec p11 = nlgFactory.createClause("María", "perseguir", "un mono");
        p11.addComplement(pp);
        output = realiser.realiseSentence(p11);
        System.out.println(output);

        //multiples clausulas -> ok
        SPhraseSpec s1_1 = nlgFactory.createClause("o meu gato", "quere", "pescado");
        SPhraseSpec s2 = nlgFactory.createClause("o meu can", "quere", "ósos grandes");
        SPhraseSpec s3 = nlgFactory.createClause("o meu cabalo", "quere", "herba");
        CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
        c.addCoordinate(s1_1);
        c.addCoordinate(s2);
        c.addCoordinate(s3);
        output = realiser.realiseSentence(c);
        System.out.println(output);

        //oraciones subordinadas -> ok
        SPhraseSpec p12 = nlgFactory.createClause("eu", "estar", "feliz");
        SPhraseSpec q = nlgFactory.createClause(null, "comer", "pescado");

        q.setFeature(Feature.COMPLEMENTISER, "porque");
        q.setFeature(Feature.TENSE, Tense.PAST);
        p12.addComplement(q);

        output = realiser.realiseSentence(p12);
        System.out.println(output);

        //parrafo -> ok
        SPhraseSpec p13 = nlgFactory.createClause("María", "perseguir", "un mono");
        SPhraseSpec p14 = nlgFactory.createClause("o mono", "pelexar");
        SPhraseSpec p15 = nlgFactory.createClause("María", "estar", "nerviosa");

        DocumentElement d1 = nlgFactory.createSentence(p13);
        DocumentElement d2 = nlgFactory.createSentence(p14);
        DocumentElement d3 = nlgFactory.createSentence(p15);

        DocumentElement par1 = nlgFactory.createParagraph(Arrays.asList(d1, d2, d3));

        String output5 = realiser.realise(par1).getRealisation();
        System.out.println(output5);

        //seccion -> ok
        DocumentElement section = nlgFactory.createSection("Os xuizos e aflicións de María e o mono");
        section.addComponent(par1);
        realiser.setFormatter(new HTMLFormatter());
        String output6 = realiser.realise(section).getRealisation();
        System.out.println(output6);

        SPhraseSpec p16 = nlgFactory.createClause("el", "dicir");
        p16.setObject("os");
        p16.setIndirectObject("lles");



      //  p16.setFeature(Feature.PERSON, Person.SECOND);
       // p16.setFeature(Feature.FORM, Form.SUBJUNCTIVE);
      //  p16.setFeature(Feature.TENSE, Tense.PRESENT);
      //  p16.setFeature(Feature.NEGATED, true);
        output = realiser.realiseSentence(p16);
        System.out.println(output);*/

         /*   PhraseElement vp = nlgFactory.createVerbPhrase(nlgFactory
                    .createWord("enfadar", LexicalCategory.VERB));
            vp.addComplement(nlgFactory.createNounPhrase("o", "contable"));
            PhraseElement _s6 = nlgFactory.createClause(nlgFactory
                    .createNounPhrase("o", "neno"), vp);
            _s6.setFeature(Feature.TENSE, Tense.PLUPERFECT);

            _s6.setFeature(Feature.PASSIVE, true);
        output = realiser.realiseSentence(_s6);
        System.out.println(output);*/

        //interrogativa -> ok
       /* SPhraseSpec p5 = nlgFactory.createClause();
        p5.setSubject("María");
        p5.setVerb("perseguir");
        p5.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        output = realiser.realiseSentence(p5);
        System.out.println(output);

        //otra forma de crear una frase preposicional -> ok
        NPPhraseSpec place = nlgFactory.createNounPhrase("un", "parque");
        PPPhraseSpec pp = nlgFactory.createPrepositionPhrase("en");
        pp.addComplement(place);
        SPhraseSpec p11 = nlgFactory.createClause("María", "perseguir", "un mono");
        p11.addComplement(pp);
        output = realiser.realiseSentence(p11);
        System.out.println(output);*/

        //SPhraseSpec text = nlgFactory.createClause(null, null);

       /* VPPhraseSpec v = nlgFactory.createVerbPhrase("esperar");
        v.setObject("el");
        v.getObject().setFeature(Feature., true);
        v.setFeature(Feature.PERSON, Person.NONE);


        SPhraseSpec object = nlgFactory.createClause("os ceos", "alternar");
        object.setPlural(true);
        object.setFeature(Feature.FORM, Form.SUBJUNCTIVE);
        v.setObject(object);

        //text.setVerbPhrase(v);
        output = realiser.realiseSentence(v);
        System.out.println(output);*/

        SPhraseSpec text = nlgFactory.createClause(null, "esperar");
        text.setCategory(PhraseCategory.IMPERSONAL);

        SPhraseSpec object = nlgFactory.createClause("os ceos", "alternar");
        object.setPlural(true);
        object.setFeature(Feature.FORM, Form.SUBJUNCTIVE);
        text.setObject(object);

        output = realiser.realiseSentence(text);
        System.out.println(output);
    }
}
