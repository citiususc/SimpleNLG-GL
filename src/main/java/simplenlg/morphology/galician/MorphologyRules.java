/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.morphology.galician;

import simplenlg.features.*;
import simplenlg.features.galician.LexicalFeature;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.StringElement;
import simplenlg.framework.WordElement;
import simplenlg.phrasespec.PPPhraseSpec;

import java.util.Arrays;

/**
 * <p>
 * This abstract class contains a number of rules for doing simple inflection.
 * </p>
 * <p>
 * <p>
 * As a matter of course, the processor will first use any user-defined
 * inflection for the world. If no inflection is provided then the lexicon, if
 * it exists, will be examined for the correct inflection. Failing this a set of
 * very basic rules will be examined to inflect the word.
 * </p>
 * <p>
 * <p>
 * All processing modules perform realisation on a tree of
 * <code>NLGElement</code>s. The modules can alter the tree in whichever way
 * they wish. For example, the syntax processor replaces phrase elements with
 * list elements consisting of inflected words while the morphology processor
 * replaces inflected words with string elements.
 * </p>
 * <p>
 * <p>
 * <b>N.B.</b> the use of <em>module</em>, <em>processing module</em> and
 * <em>processor</em> is interchangeable. They all mean an instance of this
 * class.
 * </p>
 *
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0 16-Mar-2011 modified to use correct base form (ER)
 */
public class MorphologyRules extends simplenlg.morphology.MorphologyRules {

    /**
     * A triple array of Pronouns organised by singular/plural,
     * possessive/reflexive/subjective/objective and by gender/person.
     */
    @SuppressWarnings("nls")
    private static final String[][][] PRONOUNS = {
            {
                    {"eu", "ti", "el", "ela"},
                    {"me", "te", "o", "a"},
                    {"me", "te", "se", "se", "se"},
                    {"me", "che", "lle", "lle", "lle"},
                    {"meu", "teu", "seu", "súa", "seu", "meus", "teus", "seus", "súas", "seus"},
                    {"min", "ti", "el", "ela"}
            },
            {
                    {"nós", "vós", "eles", "elas"},
                    {"nos", "vos", "os", "as"},
                    {"nos", "vos", "se", "se", "se"},
                    {"nos", "vos", "lles", "lles", "lles"},
                    {"noso", "voso", "seu", "súa", "seu", "nosos", "vosos", "seus", "súas"},
                    {"nós", "vós", "eles", "elas"}
            }};

    private static final String[] WH_PRONOUNS = {"quen", "que", "cal", "cales", "onde",
            "porque", "porqué", "por que", "como", "canto", "canta", "cantos", "cantas"};
    private static final String[] VOWELS = new String[]{"a", "e", "i", "o", "u", "á", "é", "í", "ó", "ú"};

    /**
     * This method performs the morphology for nouns.
     *
     * @param element  the <code>InflectedWordElement</code>.
     * @param baseWord the <code>WordElement</code> as created from the lexicon
     *                 entry.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public StringElement doNounMorphology(InflectedWordElement element, WordElement baseWord) {

        Object gender = element.getFeature(LexicalFeature.GENDER);
        Gender genderValue;
        if (gender instanceof Gender) {
            genderValue = (Gender) gender;
        } else {
            genderValue = Gender.MASCULINE;
        }
        Object number = element.getFeature(Feature.NUMBER);
        NumberAgreement numberValue;
        if (number instanceof NumberAgreement) {
            numberValue = (NumberAgreement) number;
        } else {
            numberValue = NumberAgreement.SINGULAR;
        }

        String realisation = null;
        if (!element.getFeatureAsBoolean(LexicalFeature.PROPER)) {
            switch (genderValue) {
                case MASCULINE:
                case NEUTER:
                    switch (numberValue) {
                        case SINGULAR:
                        case BOTH:
                            realisation = getBaseForm(element, baseWord);
                            break;
                        case PLURAL:
                            realisation = element.getFeatureAsString(LexicalFeature.PLURAL);
                            break;
                    }
                    break;
                case FEMININE:
                    switch (numberValue) {
                        case SINGULAR:
                        case BOTH:
                            realisation = element.getFeatureAsString(LexicalFeature.FEMININE_SINGULAR);
                            if (realisation == null) {
                                realisation = getBaseForm(element, baseWord);
                            }
                            break;
                        case PLURAL:
                            realisation = element.getFeatureAsString(LexicalFeature.FEMININE_PLURAL);
                            if (realisation == null) {
                                realisation = element.getFeatureAsString(LexicalFeature.PLURAL);
                            }
                            break;
                    }
                    break;
            }

            if (realisation == null) {
                if (!NumberAgreement.BOTH.equals(element.getFeature(Feature.NUMBER))) {
                    realisation = buildRegularNoun(getBaseForm(element, baseWord), genderValue, numberValue);
                } else {
                    realisation = getBaseForm(element, baseWord);
                }
            }
        } else {
            realisation = getBaseForm(element, baseWord);
        }

        StringElement realisedElement = new StringElement(realisation);
        realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
        return realisedElement;
    }

    /**
     * Builds regular nouns.
     *
     * @param baseForm the base form of the word.
     * @return the inflected word.
     */
    private String buildRegularNoun(String baseForm, Gender gender, NumberAgreement number) {
        String morphology = null;
        if (baseForm != null) {
            if (baseForm.endsWith("o") || baseForm.endsWith("a")) {
                morphology = baseForm.substring(0, baseForm.length() - 1);
                if (Gender.FEMININE.equals(gender)) {
                    morphology = morphology + "a";
                } else if (Gender.MASCULINE.equals(gender) || Gender.NEUTER.equals(gender)) {
                    morphology = morphology + "o";
                }
                if (NumberAgreement.PLURAL.equals(number)) {
                    morphology = morphology + "s";
                }
            } else if (baseForm.endsWith("s")) {
                morphology = baseForm;
            } else {
                if (NumberAgreement.PLURAL.equals(number)) {
                    morphology = baseForm + "s";
                }
            }
        }

        return morphology;
}

    /**
     * Builds a plural for regular nouns. The rules are performed in this order:
     * <ul>
     * <li>For nouns ending <em>-Cy</em>, where C is any consonant, the ending
     * becomes <em>-ies</em>. For example, <em>fly</em> becomes <em>flies</em>.</li>
     * <li>For nouns ending <em>-ch</em>, <em>-s</em>, <em>-sh</em>, <em>-x</em>
     * or <em>-z</em> the ending becomes <em>-es</em>. For example, <em>box</em>
     * becomes <em>boxes</em>.</li>
     * <li>All other nouns have <em>-s</em> appended the other end. For example,
     * <em>dog</em> becomes <em>dogs</em>.</li>
     * </ul>
     *
     * @param baseForm the base form of the word.
     * @return the inflected word.
     */
    private String buildRegularPluralNoun(String baseForm) {
        String plural = null;
        if (baseForm != null) {
            if (baseForm.matches(".*([szx]|[cs]h)\\b")) { //$NON-NLS-1$
                plural = baseForm + "es"; //$NON-NLS-1$

            } else {
                plural = baseForm + "s"; //$NON-NLS-1$
            }
        }
        return plural;
    }

    /**
     * Builds a plural for Greco-Latin regular nouns. The rules are performed in
     * this order:
     * <ul>
     * <li>For nouns ending <em>-us</em> the ending becomes <em>-i</em>. For
     * example, <em>focus</em> becomes <em>foci</em>.</li>
     * <li>For nouns ending <em>-ma</em> the ending becomes <em>-mata</em>. For
     * example, <em>trauma</em> becomes <em>traumata</em>.</li>
     * <li>For nouns ending <em>-a</em> the ending becomes <em>-ae</em>. For
     * example, <em>larva</em> becomes <em>larvae</em>.</li>
     * <li>For nouns ending <em>-um</em> or <em>-on</em> the ending becomes
     * <em>-a</em>. For example, <em>taxon</em> becomes <em>taxa</em>.</li>
     * <li>For nouns ending <em>-sis</em> the ending becomes <em>-ses</em>. For
     * example, <em>analysis</em> becomes <em>analyses</em>.</li>
     * <li>For nouns ending <em>-is</em> the ending becomes <em>-ides</em>. For
     * example, <em>cystis</em> becomes <em>cystides</em>.</li>
     * <li>For nouns ending <em>-men</em> the ending becomes <em>-mina</em>. For
     * example, <em>foramen</em> becomes <em>foramina</em>.</li>
     * <li>For nouns ending <em>-ex</em> the ending becomes <em>-ices</em>. For
     * example, <em>index</em> becomes <em>indices</em>.</li>
     * <li>For nouns ending <em>-x</em> the ending becomes <em>-ces</em>. For
     * example, <em>matrix</em> becomes <em>matrices</em>.</li>
     * </ul>
     *
     * @param baseForm the base form of the word.
     * @return the inflected word.
     */
    private String buildGrecoLatinPluralNoun(String baseForm) {
        String plural = null;
        if (baseForm != null) {
            if (baseForm.endsWith("us")) { //$NON-NLS-1$
                plural = baseForm.replaceAll("us\\b", "i"); //$NON-NLS-1$ //$NON-NLS-2$
            } else if (baseForm.endsWith("ma")) { //$NON-NLS-1$
                plural = baseForm + "ta"; //$NON-NLS-1$
            } else if (baseForm.endsWith("a")) { //$NON-NLS-1$
                plural = baseForm + "e"; //$NON-NLS-1$
            } else if (baseForm.matches(".*[(um)(on)]\\b")) { //$NON-NLS-1$
                plural = baseForm.replaceAll("[(um)(on)]\\b", "a"); //$NON-NLS-1$ //$NON-NLS-2$
            } else if (baseForm.endsWith("sis")) { //$NON-NLS-1$
                plural = baseForm.replaceAll("sis\\b", "ses"); //$NON-NLS-1$ //$NON-NLS-2$
            } else if (baseForm.endsWith("is")) { //$NON-NLS-1$
                plural = baseForm.replaceAll("is\\b", "ides"); //$NON-NLS-1$ //$NON-NLS-2$
            } else if (baseForm.endsWith("men")) { //$NON-NLS-1$
                plural = baseForm.replaceAll("men\\b", "mina"); //$NON-NLS-1$ //$NON-NLS-2$
            } else if (baseForm.endsWith("ex")) { //$NON-NLS-1$
                plural = baseForm.replaceAll("ex\\b", "ices"); //$NON-NLS-1$ //$NON-NLS-2$
            } else if (baseForm.endsWith("x")) { //$NON-NLS-1$
                plural = baseForm.replaceAll("x\\b", "ces"); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                plural = baseForm;
            }
        }
        return plural;
    }

    /**
     * This method performs the morphology for verbs.
     *
     * @param element  the <code>InflectedWordElement</code>.
     * @param baseWord the <code>WordElement</code> as created from the lexicon
     *                 entry.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public NLGElement doVerbMorphology(InflectedWordElement element, WordElement baseWord) {

        String realised = null;

        Object number = element.getFeature(Feature.NUMBER);
        NumberAgreement numberValue;
        if (number instanceof NumberAgreement) {
            numberValue = (NumberAgreement) number;
        } else {
            numberValue = NumberAgreement.SINGULAR;
        }

        Object person = element.getFeature(Feature.PERSON);
        Person personValue;
        if (person instanceof Person) {
            personValue = (Person) person;
        } else {
            personValue = Person.THIRD;
        }

        Object tense = element.getFeature(Feature.TENSE);
        Tense tenseValue;

        // AG: change to avoid deprecated getTense
        // if tense value is Tense, cast it, else default to present
        if (tense instanceof Tense) {
            tenseValue = (Tense) tense;
        } else {
            tenseValue = Tense.PRESENT;
        }

        Object form = element.getFeature(Feature.FORM);
        Form formValue;
        if (form instanceof Form) {
            formValue = (Form) form;
        } else {
            formValue = Form.NORMAL;
        }

        Object patternValue = element.getFeature(LexicalFeature.DEFAULT_INFL);

        // base form from baseWord if it exists, otherwise from element
        String baseForm = getBaseForm(element, baseWord);

//        if (personValue.equals(Person.NONE)) {
//            if (tenseValue.equals(Tense.PRESENT)) {
//                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERSONAL);
//                if (realised == null) {
//                    realised = buildRegularPresentVerb(baseForm, NumberAgreement.SINGULAR, Person.THIRD);
//                }
//            }
//        } else {
        switch (formValue) {
            case BARE_INFINITIVE:
            case INFINITIVE:
                realised = baseForm;
                break;
            case PRESENT_PARTICIPLE:
            case GERUND:
                switch (numberValue) {
                    case SINGULAR:
                    case BOTH:
                        switch (personValue) {
                            case FIRST:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONJGEROUND1S);
                                break;
                            case SECOND:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONJGEROUND2S);
                                break;
                            case THIRD:
                            case NONE:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONJGEROUND3S);
                                break;
                        }
                        break;
                    case PLURAL:
                        switch (personValue) {
                            case FIRST:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONJGEROUND1P);
                                break;
                            case SECOND:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONJGEROUND2P);
                                break;
                            case THIRD:
                            case NONE:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONJGEROUND3P);
                                break;
                        }
                        break;
                }
                // build inflected form if none was specified by the user or lexicon
                if (realised == null) {
                    realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PRESENT_PARTICIPLE);
                    if (realised == null) {
                        realised = buildRegularPresPartVerb(baseForm);
                    }
                }
                break;
            case PAST_PARTICIPLE:
                Object gender = element.getFeature(LexicalFeature.GENDER);
                Gender genderValue;
                if (gender instanceof Gender) {
                    genderValue = (Gender) gender;
                } else {
                    genderValue = Gender.MASCULINE;
                }
                if (genderValue.equals(Gender.FEMININE)) {
                    if (numberValue.equals(NumberAgreement.PLURAL)) {
                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST_PARTICIPLE_FEMININE_PLURAL);
                    } else {
                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST_PARTICIPLE_FEMININE_SINGULAR);
                    }
                } else {
                    if (numberValue.equals(NumberAgreement.PLURAL)) {
                        NLGElement parent = element;
                        Boolean singular = false;
                        /*while ((parent = parent.getParent()) != null) {
                            if (parent.getFeatureAsBoolean(Feature.PERFECT)) {
                                singular = true;
                                break;
                            }
                        }*/
                        if (singular) {
                            realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST_PARTICIPLE);
                        } else {
                            realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST_PARTICIPLE_PLURAL);
                        }
                    } else {
                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST_PARTICIPLE);
                    }
                }
                if (realised == null) {
                    realised = buildRegularPastPartVerb(baseForm, genderValue, numberValue);
                }
                break;
            case SUBJUNCTIVE:
                switch (tenseValue) {
                    case PRESENT:
                        switch (numberValue) {
                            case SINGULAR:
                            case BOTH:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.SUBJUNCTIVE1S);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.SUBJUNCTIVE2S);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.SUBJUNCTIVE3S);
                                        break;
                                }
                                break;
                            case PLURAL:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.SUBJUNCTIVE1P);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.SUBJUNCTIVE2P);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.SUBJUNCTIVE3P);
                                        break;
                                }
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularSubjunctivePresentVerb(baseForm, numberValue, personValue);
                        }
                        break;
                    case IMPERFECT:
                        switch (numberValue) {
                            case SINGULAR:
                            case BOTH:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECTSUBJUNCTIVE1S);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECTSUBJUNCTIVE2S);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECTSUBJUNCTIVE3S);
                                        break;
                                }
                                break;
                            case PLURAL:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECTSUBJUNCTIVE1P);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECTSUBJUNCTIVE2P);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECTSUBJUNCTIVE3P);
                                        break;
                                }
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularSubjunctiveImperfectVerb(baseForm, numberValue, personValue);
                        }
                        break;
                    case FUTURE:
                        switch (numberValue) {
                            case SINGULAR:
                            case BOTH:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE1S);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE2S);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE3S);
                                        break;
                                }
                                break;
                            case PLURAL:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE1P);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE2P);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE3P);
                                        break;
                                }
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularSubjunctiveFuture_ConjugateInfinitiveVerb(baseForm, numberValue, personValue);
                        }
                        break;
                       /* String imp3p = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST3P);
                        if (imp3p == null) {
                            if (Tense.IMPERFECT.equals(tenseValue)) {
                                realised = buildRegularSubjunctiveImperfectVerb(baseForm, baseForm.substring(0, baseForm.length() - 2), numberValue, personValue);
                            } else {
                                realised = buildRegularSubjunctiveFutureVerb(baseForm, baseForm.substring(0, baseForm.length() - 2), numberValue, personValue);
                            }
                        } else {
                            if (Tense.IMPERFECT.equals(tenseValue)) {
                                realised = buildRegularSubjunctiveImperfectVerb(baseForm, imp3p.substring(0, imp3p.length() - 5), numberValue, personValue);
                            } else {
                                realised = buildRegularSubjunctiveFutureVerb(baseForm, imp3p.substring(0, imp3p.length() - 5), numberValue, personValue);
                            }
                        }
                        break;
                }
                break;*/
                }
                break;
            case IMPERATIVE:
                switch (numberValue) {
                    case SINGULAR:
                    case BOTH:
                        switch (personValue) {
                            case SECOND:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERATIVE2S);
                                break;
                            case THIRD:
                            case NONE:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERATIVE3S);
                                break;
                        }
                        break;
                    case PLURAL:
                        switch (personValue) {
                            case FIRST:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERATIVE1P);
                                break;
                            case SECOND:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERATIVE2P);
                                break;
                            case THIRD:
                            case NONE:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERATIVE3P);
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularImperativeVerb(baseForm, numberValue, personValue);
                        }
                        break;
                }
                break;
            case CONJUGATE_INFINITIVE:
                switch (numberValue) {
                    case SINGULAR:
                    case BOTH:
                        switch (personValue) {
                            case FIRST:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE1S);
                                break;
                            case SECOND:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE2S);
                                break;
                            case THIRD:
                            case NONE:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE3S);
                                break;
                        }
                        break;
                    case PLURAL:
                        switch (personValue) {
                            case FIRST:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE1P);
                                break;
                            case SECOND:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE2P);
                                break;
                            case THIRD:
                            case NONE:
                                realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURESUBJUNCTIVE3P);
                                break;
                        }
                        break;
                }
                // build inflected form if none was specified by the user or lexicon
                if (realised == null) {
                    realised = buildRegularSubjunctiveFuture_ConjugateInfinitiveVerb(baseForm, numberValue, personValue);
                }
                break;
            default:
                switch (tenseValue) {
                    case PRESENT:
                        switch (numberValue) {
                            case SINGULAR:
                            case BOTH:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PRESENT1S);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PRESENT2S);
                                        break;
                                    case THIRD:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PRESENT3S);
                                        break;
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERSONAL);
                                        if (realised == null) {
                                            personValue = Person.THIRD;
                                        }
                                        break;
                                }
                                break;
                            case PLURAL:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PRESENT1P);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PRESENT2P);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PRESENT3P);
                                        break;
                                }
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularPresentVerb(baseForm, numberValue, personValue);
                        }
                        break;
                    case PAST:
                        switch (numberValue) {
                            case SINGULAR:
                            case BOTH:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST1S);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST2S);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST3S);
                                        break;
                                }
                                break;
                            case PLURAL:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST1P);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST2P);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PAST3P);
                                        break;
                                }
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularPastVerb(baseForm, numberValue, personValue);
                        }
                        break;
                    case IMPERFECT:
                        switch (numberValue) {
                            case SINGULAR:
                            case BOTH:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECT1S);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECT2S);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECT3S);
                                        break;
                                }
                                break;
                            case PLURAL:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECT1P);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECT2P);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.IMPERFECT3P);
                                        break;
                                }
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularImperfectVerb(baseForm, numberValue, personValue);
                        }
                        break;
                    case PLUPERFECT:
                        switch (numberValue) {
                            case SINGULAR:
                            case BOTH:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PLUPERFECT1S);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PLUPERFECT2S);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PLUPERFECT3S);
                                        break;
                                }
                                break;
                            case PLURAL:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PLUPERFECT1P);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PLUPERFECT2P);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.PLUPERFECT3P);
                                        break;
                                }
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularPluperfectVerb(baseForm, numberValue, personValue);
                        }
                        break;
                    case FUTURE:
                        switch (numberValue) {
                            case SINGULAR:
                            case BOTH:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURE1S);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURE2S);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURE3S);
                                        break;
                                }
                                break;
                            case PLURAL:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURE1P);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURE2P);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.FUTURE3P);
                                        break;
                                }
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularFutureVerb(baseForm, numberValue, personValue);
                        }
                        break;
                    case CONDITIONAL:
                        switch (numberValue) {
                            case SINGULAR:
                            case BOTH:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONDITIONAL1S);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONDITIONAL2S);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONDITIONAL3S);
                                        break;
                                }
                                break;
                            case PLURAL:
                                switch (personValue) {
                                    case FIRST:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONDITIONAL1P);
                                        break;
                                    case SECOND:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONDITIONAL2P);
                                        break;
                                    case THIRD:
                                    case NONE:
                                        realised = getRealisedVerbFromFeature(element, baseWord, LexicalFeature.CONDITIONAL3P);
                                        break;
                                }
                                break;
                        }
                        // build inflected form if none was specified by the user or lexicon
                        if (realised == null) {
                            realised = buildRegularConditionalVerb(baseForm, numberValue, personValue);
                        }
                        break;
                }
        }
//        }
        StringElement realisedElement = new StringElement(realised);
        realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
        return realisedElement;
    }

    private String getRealisedVerbFromFeature(InflectedWordElement element, WordElement baseWord, String feature) {

        String realised = element.getFeatureAsString(feature);
        if (realised == null && baseWord != null) {
            realised = baseWord.getFeatureAsString(feature);
        }
        return realised;
    }

    /**
     * Builds the present-tense form for regular verbs.
     *
     * @param baseForm the base form of the verb.
     * @param person   the person
     * @return the inflected word.
     */
    private String buildRegularPresentVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case FIRST:
                            morphology = radical + "o";
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "as";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "es";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "a";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "e";
                            }
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "amos";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "emos";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "imos";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ades";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "edes";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "ides";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "an";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "en";
                            }
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * Builds the past-tense form for regular verbs.
     *
     * @param baseForm the base form of the verb.
     * @param number   the number agreement for the verb.
     * @param person   the person
     * @return the inflected word.
     */
    private String buildRegularPastVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case FIRST:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ei";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "ín";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "aches";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "iches";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ou";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "eu";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "iu";
                            }
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "amos";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "emos";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "imos";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "astes";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "estes";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "istes";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "aron";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "eron";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "iron";
                            }
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * Builds the imperfect-tense form for regular verbs.
     *
     * @param baseForm the base form of the word.
     * @param number   the number agreement for the verb.
     * @param person   the person.
     * @return the inflected word.
     */
    private String buildRegularImperfectVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case FIRST:
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "aba";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "ía";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "abas";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "ías";
                            }
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "abamos";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "iamos";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "abades";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "iades";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "aban";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "ían";
                            }
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * Builds the pluperfect-tense form for regular verbs.
     *
     * @param baseForm the base form of the word.
     * @param number   the number agreement for the verb.
     * @param person   the person.
     * @return the inflected word.
     */
    private String buildRegularPluperfectVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case FIRST:
                        case THIRD:
                            morphology = radical + "a";
                            break;
                        case SECOND:
                            morphology = radical + "as";
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            morphology = radical + "amos";
                            break;
                        case SECOND:
                            morphology = radical + "ades";
                            break;
                        case THIRD:
                            morphology = radical + "an";
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * Builds the future-tense form for regular verbs.
     *
     * @param baseForm the base form of the verb.
     * @param number   the number agreement for the verb.
     * @param person   the person.
     * @return the inflected word.
     */
    private String buildRegularFutureVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case FIRST:
                            morphology = baseForm + "ei";
                            break;
                        case SECOND:
                            morphology = baseForm + "ás";
                            break;
                        case THIRD:
                            morphology = baseForm + "á";
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            morphology = baseForm + "emos";
                            break;
                        case SECOND:
                            morphology = baseForm + "edes";
                            break;
                        case THIRD:
                            morphology = baseForm + "án";
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * Builds the conditional-tense form for regular verbs.
     *
     * @param baseForm the base form of the word.
     * @param number   the number agreement for the verb.
     * @param person   the person.
     * @return the inflected word.
     */
    private String buildRegularConditionalVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case FIRST:
                        case THIRD:
                            morphology = baseForm + "ía";
                            break;
                        case SECOND:
                            morphology = baseForm + "ías";
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            morphology = baseForm + "iamos";
                            break;
                        case SECOND:
                            morphology = baseForm + "iades";
                            break;
                        case THIRD:
                            morphology = baseForm + "ían";
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * Builds the present participle form for regular verbs.
     *
     * @param baseForm the base form of the word.
     * @return the inflected word.
     */
    private String buildRegularPresPartVerb(String baseForm) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            if (baseForm.endsWith("ar")) {
                morphology = radical + "ando";
            } else if (baseForm.endsWith("er")) {
                morphology = radical + "endo";
            } else if (baseForm.endsWith("ir")) {
                morphology = radical + "indo";
            }
        }
        return morphology;
    }

    /**
     * Builds the past participle form for regular verbs.
     *
     * @param baseForm the base form of the word.
     * @return the inflected word.
     */
    private String buildRegularPastPartVerb(String baseForm, Gender gender, NumberAgreement number) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            if (baseForm.endsWith("ar")) {
                morphology = radical + "ad";
                if (gender.equals(Gender.FEMININE)) {
                    morphology = morphology + "a";
                } else {
                    morphology = morphology + "o";
                }
                if (number.equals(NumberAgreement.PLURAL)) {
                    morphology = morphology + "s";
                }
            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                morphology = radical + "id";
                if (gender.equals(Gender.FEMININE)) {
                    morphology = morphology + "a";
                } else {
                    morphology = morphology + "o";
                }
                if (number.equals(NumberAgreement.PLURAL)) {
                    morphology = morphology + "s";
                }
            }
        }
        return morphology;
    }

    /**
     * Builds the present-tense subjunctive form for regular verbs.
     *
     * @param baseForm the base form of the verb.
     * @param number   the number agreement for the verb.
     * @param person   the person.
     * @return the inflected word.
     */
    private String buildRegularSubjunctivePresentVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case FIRST:
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "e";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "a";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "es";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "as";
                            }
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "emos";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "amos";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "edes";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "ades";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "en";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "an";
                            }
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * Builds the future-tense subjunctive form for regular verbs.
     *
     * @param baseForm the base form of the verb.
     * @param number   the number agreement for the verb.
     * @param person   the person.
     * @return the inflected word.
     */
    private String buildRegularSubjunctiveFuture_ConjugateInfinitiveVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case FIRST:
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ar";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "er";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "ir";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ares";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "eres";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "ires";
                            }
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ásemos";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "ésemos";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "ísemos";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "armos";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "erdes";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "irdes";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "aren";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "eren";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "iren";
                            }
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * Builds the past imperfect-tense subjunctive form for regular verbs.
     *
     * @param baseForm the base form of the verb.
     * @param number   the number agreement for the verb.
     * @param person   the person.
     * @return the inflected word.
     */
    private String buildRegularSubjunctiveImperfectVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case FIRST:
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ase";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "ese";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "ise";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ases";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "eses";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "ises";
                            }
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ásemos";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "ésemos";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "ísemos";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ásedes";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "ésedes";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "ísedes";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "asen";
                            } else if (baseForm.endsWith("er")) {
                                morphology = radical + "esen";
                            } else if (baseForm.endsWith("ir")) {
                                morphology = radical + "isen";
                            }
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * Builds the imperative form for regular verbs.
     *
     * @param baseForm the base form of the verb.
     * @param number   the number agreement for the verb.
     * @param person   the person.
     * @return the inflected word.
     */
    private String buildRegularImperativeVerb(String baseForm, NumberAgreement number, Person person) {
        String morphology = null;
        if (baseForm != null) {
            String radical = baseForm.substring(0, baseForm.length() - 2);
            switch (number) {
                case SINGULAR:
                    switch (person) {
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "a";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "e";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "e";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "a";
                            }
                            break;
                    }
                    break;
                case PLURAL:
                    switch (person) {
                        case FIRST:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "emos";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "amos";
                            }
                            break;
                        case SECOND:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "ade";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "ede";
                            }
                            break;
                        case THIRD:
                            if (baseForm.endsWith("ar")) {
                                morphology = radical + "en";
                            } else if (baseForm.endsWith("er") || baseForm.endsWith("ir")) {
                                morphology = radical + "an";
                            }
                            break;
                    }
                    break;
            }
        }
        return morphology;
    }

    /**
     * This method performs the morphology for adjectives.
     *
     * @param element  the <code>InflectedWordElement</code>.
     * @param baseWord the <code>WordElement</code> as created from the lexicon
     *                 entry.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public NLGElement doAdjectiveMorphology(InflectedWordElement element, WordElement baseWord) {

        NLGElement parent = element;
        while (parent.getParent() != null) {
            parent = parent.getParent();
            if (parent.hasFeature(simplenlg.features.LexicalFeature.GENDER)) {
                break;
            }
        }
        Object gender = parent.getFeature(LexicalFeature.GENDER);
        Gender genderValue;
        if (gender instanceof Gender) {
            genderValue = (Gender) gender;
        } else {
            genderValue = Gender.MASCULINE;
        }
        Object number = element.getParent().getFeature(Feature.NUMBER);
        NumberAgreement numberValue;
        if (number instanceof NumberAgreement) {
            numberValue = (NumberAgreement) number;
        } else {
            numberValue = NumberAgreement.SINGULAR;
        }

        String realisation = null;

        switch (genderValue) {
            case MASCULINE:
            case NEUTER:
                switch (numberValue) {
                    case SINGULAR:
                    case BOTH:
                        if (element.getFeatureAsBoolean(Feature.IS_SUPERLATIVE)) {
                            realisation = element.getFeatureAsString(LexicalFeature.SUPERLATIVE);
                        } else {
                            realisation = getBaseForm(element, baseWord);
                        }
                        break;
                    case PLURAL:
                        if (element.getFeatureAsBoolean(Feature.IS_SUPERLATIVE)) {
                            realisation = element.getFeatureAsString(LexicalFeature.SUPERLATIVE_PLURAL);
                        } else {
                            realisation = element.getFeatureAsString(LexicalFeature.PLURAL);
                        }
                        break;
                }
                break;
            case FEMININE:
                switch (numberValue) {
                    case SINGULAR:
                    case BOTH:
                        if (element.getFeatureAsBoolean(Feature.IS_SUPERLATIVE)) {
                            realisation = element.getFeatureAsString(LexicalFeature.SUPERLATIVE_FEMININE);
                        } else {
                            if (Gender.FEMININE.equals(element.getFeature(LexicalFeature.GENDER))) {
                                realisation = getBaseForm(element, baseWord);
                            } else {
                                realisation = element.getFeatureAsString(LexicalFeature.FEMININE_SINGULAR);
                            }
                        }
                        break;
                    case PLURAL:
                        if (element.getFeatureAsBoolean(Feature.IS_SUPERLATIVE)) {
                            realisation = element.getFeatureAsString(LexicalFeature.SUPERLATIVE_FEMININE_PLURAL);
                        } else {
                            if (Gender.FEMININE.equals(element.getFeature(LexicalFeature.GENDER))) {
                                realisation = element.getFeatureAsString(LexicalFeature.PLURAL);
                            } else {
                                realisation = element.getFeatureAsString(LexicalFeature.FEMININE_PLURAL);
                            }
                        }
                        break;
                }
                break;
        }

        if (realisation == null) {
            if (element.getFeatureAsBoolean(Feature.IS_SUPERLATIVE)) {
                realisation = buildRegularSuperlative(getBaseForm(element, baseWord), genderValue, numberValue);
            } else {
                realisation = buildRegularAdjective(getBaseForm(element, baseWord), genderValue, numberValue);
            }
        }

        StringElement realisedElement = new StringElement(realisation);
        realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
        return realisedElement;

//        String realised = null;
//        Object patternValue = element.getFeature(LexicalFeature.DEFAULT_INFL);
//
//        // base form from baseWord if it exists, otherwise from element
//        String baseForm = getBaseForm(element, baseWord);
//
//        if (element.getFeatureAsBoolean(Feature.IS_COMPARATIVE).booleanValue()) {
//            realised = element.getFeatureAsString(LexicalFeature.COMPARATIVE);
//
//            if (realised == null && baseWord != null) {
//                realised = baseWord.getFeatureAsString(LexicalFeature.COMPARATIVE);
//            }
//            if (realised == null) {
//                if (Inflection.REGULAR_DOUBLE.equals(patternValue)) {
//                    realised = buildDoubleCompAdjective(baseForm);
//                } else {
//                    realised = buildRegularComparative(baseForm);
//                }
//            }
//        } else if (element.getFeatureAsBoolean(Feature.IS_SUPERLATIVE).booleanValue()) {
//
//            realised = element.getFeatureAsString(LexicalFeature.SUPERLATIVE);
//
//            if (realised == null && baseWord != null) {
//                realised = baseWord.getFeatureAsString(LexicalFeature.SUPERLATIVE);
//            }
//            if (realised == null) {
//                if (Inflection.REGULAR_DOUBLE.equals(patternValue)) {
//                    realised = buildDoubleSuperAdjective(baseForm);
//                } else {
//                    realised = buildRegularSuperlative(baseForm);
//                }
//            }
//        } else {
//            realised = baseForm;
//        }
//        StringElement realisedElement = new StringElement(realised);
//        realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
//                element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
//        return realisedElement;
    }

    /**
     * Builds the comparative form for adjectives that follow the doubling form
     * of the last consonant. <em>-er</em> is added to the end after the last
     * consonant is doubled. For example, <em>fat</em> becomes <em>fatter</em>.
     *
     * @param baseForm the base form of the word.
     * @return the inflected word.
     */
    private String buildDoubleCompAdjective(String baseForm) {
        String morphology = null;
        if (baseForm != null) {
            morphology = baseForm + baseForm.charAt(baseForm.length() - 1) + "er"; //$NON-NLS-1$
        }
        return morphology;
    }

    /**
     * Builds the regular form for regular adjectives.
     *
     * @param baseForm the base form of the word.
     * @return the inflected word.
     */
    private String buildRegularAdjective(String baseForm, Gender gender, NumberAgreement number) {
        String morphology = null;
        if (baseForm != null) {
            Character lastChar = baseForm.charAt(baseForm.length() - 1);
            if (lastChar.equals('a') || lastChar.equals('o')) {
                morphology = baseForm.substring(0, baseForm.length() - 1);
                if (Gender.FEMININE.equals(gender)) {
                    if (baseForm.length() > 2) {
                        morphology = morphology + "a";
                    } else {
                        morphology = baseForm + "a";
                    }
                } else if (Gender.MASCULINE.equals(gender)) {
                    morphology = morphology + "o";
                } else if(Gender.NEUTER.equals(gender)) {
                    morphology = baseForm;
                }
                if (NumberAgreement.PLURAL.equals(number)) {
                    morphology = morphology + "s";
                }
            } else if (Arrays.asList(VOWELS).contains(String.valueOf(lastChar))) {
                if(NumberAgreement.PLURAL.equals(number)) {
                    morphology = baseForm + 's';
                } else {
                    if(lastChar.equals('e')) {
                        morphology = baseForm;
                    }
                }
            } else {
                if (NumberAgreement.PLURAL.equals(number)) {
                    if(lastChar.equals('l')) {
                        morphology = baseForm.substring(0, baseForm.length() - 1) + "is";
                    } else {
                        morphology = morphology + "es";
                    }
                } else {
                    if(lastChar.equals('l')) {
                        morphology = baseForm;
                    }
                }
            }
        }
        return morphology;
    }

    /**
     * Builds the superlative form for regular adjectives.
     *
     * @param baseForm the base form of the word.
     * @return the inflected word.
     */
    private String buildRegularSuperlative(String baseForm, Gender gender, NumberAgreement number) {
        String morphology = null;
        if (baseForm != null) {
            morphology = baseForm.substring(0, baseForm.length() - 1) + "ísim";
            if (Gender.FEMININE.equals(gender)) {
                morphology = morphology + "a";
            } else if (Gender.MASCULINE.equals(gender)) {
                morphology = morphology + "o";
            }
            if (NumberAgreement.PLURAL.equals(number)) {
                morphology = morphology + "s";
            }
        }
        return morphology;
    }

    /**
     * This method performs the morphology for adverbs.
     *
     * @param element  the <code>InflectedWordElement</code>.
     * @param baseWord the <code>WordElement</code> as created from the lexicon
     *                 entry.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public NLGElement doAdverbMorphology(InflectedWordElement element, WordElement baseWord) {

        String realised = null;

        // base form from baseWord if it exists, otherwise from element
        String baseForm = getBaseForm(element, baseWord);

        if (element.getFeatureAsBoolean(Feature.IS_COMPARATIVE).booleanValue()) {
            realised = element.getFeatureAsString(LexicalFeature.COMPARATIVE);

            if (realised == null && baseWord != null) {
                realised = baseWord.getFeatureAsString(LexicalFeature.COMPARATIVE);
            }
        } else if (element.getFeatureAsBoolean(Feature.IS_SUPERLATIVE).booleanValue()) {

            realised = element.getFeatureAsString(LexicalFeature.SUPERLATIVE);

            if (realised == null && baseWord != null) {
                realised = baseWord.getFeatureAsString(LexicalFeature.SUPERLATIVE);
            }
            if (realised == null) {
                realised = buildRegularSuperlative(baseForm, Gender.MASCULINE, NumberAgreement.SINGULAR);
            }
        } else {
            realised = baseForm;
        }
        StringElement realisedElement = new StringElement(realised);
        realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
        return realisedElement;
    }

    /**
     * This method performs the morphology for pronouns.
     *
     * @param element the <code>InflectedWordElement</code>.
     * @return a <code>StringElement</code> representing the word after
     * inflection.
     */
    public NLGElement doPronounMorphology(InflectedWordElement element) {

        String realised = null;

        if(element.getFeature(InternalFeature.DISCOURSE_FUNCTION) != null && element.getFeature(InternalFeature.DISCOURSE_FUNCTION).equals(DiscourseFunction.COMPLEMENT)) {
            NLGElement parent = element.getParent();
            if(parent != null && (parent.getFeature(InternalFeature.DISCOURSE_FUNCTION).equals(DiscourseFunction.INDIRECT_OBJECT))) {
                element.setFeature(InternalFeature.DISCOURSE_FUNCTION, parent.getFeature(InternalFeature.DISCOURSE_FUNCTION));
            }
        }

        if (!element.getFeatureAsBoolean(InternalFeature.NON_MORPH).booleanValue() && !isWHPronoun(element)) {
            Object genderValue = element.getFeature(LexicalFeature.GENDER);
            Object personValue = element.getFeature(Feature.PERSON);
            Object discourseValue = element.getFeature(InternalFeature.DISCOURSE_FUNCTION);

            if (element.getFeature("base_word").toString().contains("che") || element.getFeature("base_word").toString().contains("lle")) {
                discourseValue = DiscourseFunction.INDIRECT_OBJECT;
            }

            int numberIndex = element.isPlural() ? 1 : 0;
            int genderIndex = (genderValue instanceof Gender) ? ((Gender) genderValue).ordinal() : 2;

            int personIndex = (personValue instanceof Person) ? ((Person) personValue).ordinal() : 2;
            if (personIndex < 3) {
                if (personIndex == 2) {
                    personIndex += genderIndex;
                }

                int positionIndex;

                if (element.getFeatureAsBoolean(LexicalFeature.REFLEXIVE) || element.getParent().getFeatureAsBoolean(LexicalFeature.REFLEXIVE)) {
                    positionIndex = 2;
                } else if (element.getFeatureAsBoolean(Feature.POSSESSIVE)) {
                    positionIndex = 3;
                    if (DiscourseFunction.SPECIFIER.equals(discourseValue)) {
                        positionIndex++;
                    }
                    if (element.getParent().isPlural()) {
                        personIndex += 5;
                    }
                } else if (DiscourseFunction.SUBJECT.equals(discourseValue) && element.getFeatureAsBoolean(Feature.PASSIVE)) {
                    positionIndex = 5;
                } else if (DiscourseFunction.INDIRECT_OBJECT.equals(discourseValue)) {
                    positionIndex = 3;
                } else {
                    positionIndex = (DiscourseFunction.SUBJECT.equals(discourseValue) && !element.getFeatureAsBoolean(
                            Feature.PASSIVE)) || (DiscourseFunction.OBJECT.equals(discourseValue) && element.getFeatureAsBoolean(Feature.PASSIVE))
                            || DiscourseFunction.SPECIFIER.equals(discourseValue) || (
                            DiscourseFunction.COMPLEMENT.equals(discourseValue)
                                    && (element.getFeatureAsBoolean(Feature.PASSIVE) || element.getParent().getParent() instanceof PPPhraseSpec)) ? 0 : 1;
                }
                if(element.getFeatureAsBoolean(InternalFeature.IMPERSONAL)) {
                    positionIndex = 2;
                }
                realised = PRONOUNS[numberIndex][positionIndex][personIndex];
            } else {
                realised = element.getBaseForm();
            }
        } else {
            Object gender = element.getFeature(LexicalFeature.GENDER);
            Gender genderValue;
            if (gender instanceof Gender) {
                genderValue = (Gender) gender;
            } else {
                genderValue = Gender.MASCULINE;
            }
            Object number = element.getFeature(Feature.NUMBER);
            NumberAgreement numberValue;
            if (number instanceof NumberAgreement) {
                numberValue = (NumberAgreement) number;
            } else {
                numberValue = NumberAgreement.SINGULAR;
            }
            switch (genderValue) {
                case MASCULINE:
                case NEUTER:
                    switch (numberValue) {
                        case SINGULAR:
                        case BOTH:
                            realised = element.getBaseForm();
                            break;
                        case PLURAL:
                            realised = element.getFeatureAsString(LexicalFeature.PLURAL);
                            break;
                    }
                    break;
                case FEMININE:
                    switch (numberValue) {
                        case SINGULAR:
                        case BOTH:
                            if (Gender.FEMININE.equals(element.getFeature(LexicalFeature.GENDER))) {
                                realised = element.getBaseForm();
                            } else {
                                realised = element.getFeatureAsString(LexicalFeature.FEMININE_SINGULAR);
                            }
                            break;
                        case PLURAL:
                            if (Gender.FEMININE.equals(element.getFeature(LexicalFeature.GENDER))) {
                                realised = element.getFeatureAsString(LexicalFeature.PLURAL);
                            } else {
                                realised = element.getFeatureAsString(LexicalFeature.FEMININE_PLURAL);
                            }
                            break;
                    }
                    break;
            }
            if (realised == null) {
                realised = element.getBaseForm();
            }
        }
        StringElement realisedElement = new StringElement(realised);
        realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                element.getFeature(InternalFeature.DISCOURSE_FUNCTION));

        return realisedElement;
    }

    private boolean isWHPronoun(InflectedWordElement word) {
        String base = word.getBaseForm();
        boolean wh = false;

        if (base != null) {
            for (int i = 0; i < WH_PRONOUNS.length && !wh; i++) {
                wh = WH_PRONOUNS[i].equals(base);
            }
        }

        return wh;

    }

    /**
     * This method performs the morphology for determiners.
     *
     * @param element the <code>InflectedWordElement</code>.
     */
    public NLGElement doDeterminerMorphology(InflectedWordElement element) {

        Object gender = element.getParent().getFeature(LexicalFeature.GENDER);
        Gender genderValue;
        if (gender instanceof Gender) {
            genderValue = (Gender) gender;
        } else {
            genderValue = Gender.MASCULINE;
        }
        Object number = element.getFeature(Feature.NUMBER);
        NumberAgreement numberValue;
        if (number instanceof NumberAgreement) {
            numberValue = (NumberAgreement) number;
        } else {
            numberValue = NumberAgreement.SINGULAR;
        }

        String realisation = null;

        switch (genderValue) {
            case MASCULINE:
            case NEUTER:
                switch (numberValue) {
                    case SINGULAR:
                    case BOTH:
                        realisation = element.getBaseForm();
                        break;
                    case PLURAL:
                        if(element.getFeatureAsString(LexicalFeature.PLURAL) == null) {
                        realisation = element.getBaseForm();
                        } else {
                            realisation = element.getFeatureAsString(LexicalFeature.PLURAL);
                        }
                        break;
                }
                break;
            case FEMININE:
                switch (numberValue) {
                    case SINGULAR:
                    case BOTH:
                        if (Gender.FEMININE.equals(element.getFeature(LexicalFeature.GENDER))) {
                            realisation = element.getBaseForm();
                        } else {
                            realisation = element.getFeatureAsString(LexicalFeature.FEMININE_SINGULAR);
                        }
                        break;
                    case PLURAL:
                        if (Gender.FEMININE.equals(element.getFeature(LexicalFeature.GENDER))) {
                            realisation = element.getFeatureAsString(LexicalFeature.PLURAL);
                        } else {
                            realisation = element.getFeatureAsString(LexicalFeature.FEMININE_PLURAL);
                        }
                        break;
                }
                break;
        }

        StringElement realisedElement = new StringElement(realisation);
        realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
        return realisedElement;
    }

    public void doDeterminerMorphology(NLGElement determiner, String realisation) {

    }

    public String buildPrepositionArticleConjunction(String preposition) {
        String letter = "";
        if(preposition.equals("en")) {
            letter = "n";
        } else if (preposition.equals("con")) {
            letter = "c";
        } else if (preposition.equals("de")) {
            letter = "d";
        } else if (preposition.equals("por")) {
            letter = "pol";
        } else if (preposition.equals("a")) {
            letter = "a";
        } else if (preposition.equals("tras")) {
            letter = "tral";
        }
        return letter;
    }

    public String buildPronounsConjunction(String ci, String cd) {
        String conj = "";
        if(ci.equals("me") || ci.equals("che") || ci.equals("lle")) {
            conj = ci.substring(0, ci.length()-1) + cd;
        }
        if(ci.equals("nos") || ci.equals("vos") || ci.equals("lles")) {
            conj = ci.substring(0, ci.length()-1) + "l" + cd;
        }
        return conj;
    }

    public char replaceNotAccentuatedChar(char letter) {
        char result = 0;
        if (letter == 'a') {
            result = 'á';
        } else if (letter == 'e') {
            result = 'é';
        } else if (letter == 'i') {
            result = 'í';
        } else if (letter == 'o') {
            result = 'ó';
        } else if (letter == 'u') {
            result = 'ú';
        }
        return result;
    }

    public char replaceAccentuatedChar(char letter) {
        char result = 0;
        if (letter == 'á') {
            result = 'a';
        } else if (letter == 'é') {
            result = 'e';
        } else if (letter == 'í') {
            result = 'i';
        } else if (letter == 'ó') {
            result = 'o';
        } else if (letter == 'ú') {
            result = 'u';
        }
        return result;
    }
}
