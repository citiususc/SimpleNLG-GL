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
package simplenlg.realiser;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.framework.*;
import simplenlg.lexicon.Lexicon;
import simplenlg.morphology.MorphologyProcessor;
import simplenlg.orthography.OrthographyProcessor;
import simplenlg.syntax.SyntaxProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author D. Westwater, Data2Text Ltd
 */
public abstract class Realiser extends NLGModule {

    protected MorphologyProcessor morphology;
    protected OrthographyProcessor orthography;
    protected SyntaxProcessor syntax;
    protected NLGModule formatter = null;
    protected boolean debug = false;

    private static final String[] SUBORDINATES = new String[]{"porque", "que", "se"};
    private static final String[] ADVERBS = new String[]{"quizais", "talvez", "seica", "disque", "xa", "só", "mal", "axiña", "sempre", "aínda", "aquí", "aí", "alí", "máis", "menos", "aínda", "ata", "até", "tamén"};
    private static final String[] INDEFINITES = new String[]{"ninguén", "alguén", "calquera", "mesmo", "algo", "nada", "bastante", "todo", "ambos"};
    private static final String[] INTERROGATIVES = new String[]{"cando", "onde", "canto", "como"};
    private static final String[] DESIDERATIVES = new String[]{"oxalá"};

    /**
     * create a realiser (no lexicon)
     */
    public Realiser() {
        super();
    }

    /**
     * Check whether this processor separates premodifiers using a comma.
     * <p>
     * <br/>
     * <strong>Implementation note:</strong> this method checks whether the
     * {@link OrthographyProcessor} has the
     * parameter set.
     *
     * @return <code>true</code> if premodifiers in the noun phrase are
     * comma-separated.
     */
    public boolean isCommaSepPremodifiers() {
        return this.orthography == null ? false : this.orthography.isCommaSepPremodifiers();
    }

    /**
     * Set whether to separate premodifiers using a comma. If <code>true</code>,
     * premodifiers will be comma-separated, as in <i>the long, dark road</i>.
     * If <code>false</code>, they won't. <br/>
     * <strong>Implementation note:</strong>: this method sets the relevant
     * parameter in the
     * {@link OrthographyProcessor}.
     *
     * @param commaSepPremodifiers the commaSepPremodifiers to set
     */
    public void setCommaSepPremodifiers(boolean commaSepPremodifiers) {
        if (this.orthography != null) {
            this.orthography.setCommaSepPremodifiers(commaSepPremodifiers);
        }
    }

    /**
     * Check whether this processor separates cue phrases from the matrix clause using a comma.
     * <p>
     * <br/>
     * <strong>Implementation note:</strong> this method checks whether the
     * {@link OrthographyProcessor} has the
     * parameter set.
     *
     * @return <code>true</code> if cue phrases have a comma before the remainder of the host phrase
     */
    public boolean isCommaSepCuephrase() {
        return this.orthography == null ? false : this.orthography.isCommaSepCuephrase();
    }

    /**
     * Set whether to separate cue phrases from the host phrase using a comma. If <code>true</code>,
     * a comma will be inserted, as in <i>however, Bill arrived late</i>.
     * If <code>false</code>, they won't. <br/>
     * <strong>Implementation note:</strong>: this method sets the relevant
     * parameter in the
     * {@link OrthographyProcessor}.
     *
     * @param commaSepCuephrase
     */
    public void setCommaSepCuephrase(boolean commaSepCuephrase) {
        if (this.orthography != null) {
            this.orthography.setCommaSepCuephrase(commaSepCuephrase);
        }
    }

    public abstract void initialise();

    @Override
    public NLGElement realise(NLGElement element) {

        StringBuilder debug = new StringBuilder();
        boolean pronoun_after = false;

        if (this.debug) {
            System.out.println("INITIAL TREE\n"); //$NON-NLS-1$
            System.out.println(element.printTree(null));
            debug.append("INITIAL TREE<br/>");
            debug.append(element.printTree("&nbsp;&nbsp;").replaceAll("\n", "<br/>"));
        }

        NLGElement postSyntax = this.syntax.realise(element);
        postSyntax.setFeature(Feature.PRONOUN_AFTER, element.getFeatureAsBoolean(Feature.PRONOUN_AFTER));
        if (this.debug) {
            System.out.println("<br/>POST-SYNTAX TREE<br/>"); //$NON-NLS-1$
            System.out.println(postSyntax.printTree(null));
            debug.append("<br/>POST-SYNTAX TREE<br/>");
            debug.append(postSyntax.printTree("&nbsp;&nbsp;").replaceAll("\n", "<br/>"));
        }
        //////////////////////////////verb + pronoun colocation/////////////////////////////////////////////////
        //negated and interrogative sentences: verb+pronoun
        if(postSyntax.getCategory() != null && postSyntax.getCategory().equals(DocumentCategory.SENTENCE)) {
            if (postSyntax.getFeatureAsBoolean(Feature.NEGATED) == false && postSyntax.getFeatureAsBoolean(Feature.INTERROGATIVE_TYPE) == false) {
                pronoun_after = true;
            }
            List<NLGElement> elements = new ArrayList<NLGElement>();
            if (postSyntax instanceof ListElement || postSyntax instanceof DocumentElement) {
                elements.addAll(postSyntax.getChildren());
            } else {
                elements.add(postSyntax);
            }

            elements = checkElements(elements);

            int indexVerb = -1, indexSubordinate = -1, indexAdverb = -1, indexIndefinite = -1, indexInterrogative = -1, indexDesiderative = -1;
            for (NLGElement e : elements) {
                try {
                    if (e.getCategory().equals(LexicalCategory.VERB)) {
                        indexVerb = elements.indexOf(e);
                    }
                } catch (Exception ex) {

                }
                try {
                    if (Arrays.asList(SUBORDINATES).contains(e.getRealisation())) {
                        indexSubordinate = elements.indexOf(e);
                    }
                    if (Arrays.asList(ADVERBS).contains(e.getRealisation()) || e.getCategory().equals(LexicalCategory.ADVERB)) {
                        indexAdverb = elements.indexOf(e);
                    }
                    if (Arrays.asList(INDEFINITES).contains(e.getRealisation())) {
                        indexIndefinite = elements.indexOf(e);
                    }
                    if (Arrays.asList(INTERROGATIVES).contains(e.getRealisation())) {
                        indexInterrogative = elements.indexOf(e);
                    }
                    if (Arrays.asList(DESIDERATIVES).contains(e.getRealisation())) {
                        indexDesiderative = elements.indexOf(e);
                    }
                } catch (Exception ex) {

                }
            }
            if (indexVerb >= 0) {
                //subordinates sentences: pronoun before
                if (indexSubordinate >= 0 && indexSubordinate < indexVerb) {
                    pronoun_after = false;
                }
                //with some adverbs: pronoun before
                if (indexAdverb >= 0 && indexAdverb < indexVerb) {
                    pronoun_after = false;
                }
                //with some indefinites: pronoun before
                if (indexIndefinite >= 0 && indexIndefinite < indexVerb) {
                    pronoun_after = false;
                }
                //with interrogatives: pronoun before
                if (indexInterrogative >= 0 && indexInterrogative < indexVerb) {
                    pronoun_after = false;
                }
                //in desideratives phrases: pronoun before
                if (indexDesiderative >= 0 && indexDesiderative < indexVerb) {
                    pronoun_after = false;
                }
            }
        }

        postSyntax.setFeature(Feature.PRONOUN_AFTER, pronoun_after);
        NLGElement postMorphology = this.morphology.realise(postSyntax);
        if (this.debug)

        {
            System.out.println("\nPOST-MORPHOLOGY TREE\n"); //$NON-NLS-1$
            System.out.println(postMorphology.printTree(null));
            debug.append("<br/>POST-MORPHOLOGY TREE<br/>");
            debug.append(postMorphology.printTree("&nbsp;&nbsp;").replaceAll("\n", "<br/>"));
        }

        NLGElement postOrthography = this.orthography.realise(postMorphology);
        if (this.debug)

        {
            System.out.println("\nPOST-ORTHOGRAPHY TREE\n"); //$NON-NLS-1$
            System.out.println(postOrthography.printTree(null));
            debug.append("<br/>POST-ORTHOGRAPHY TREE<br/>");
            debug.append(postOrthography.printTree("&nbsp;&nbsp;").replaceAll("\n", "<br/>"));
        }

        NLGElement postFormatter = null;
        if (this.formatter != null)

        {
            postFormatter = this.formatter.realise(postOrthography);
            if (this.debug) {
                System.out.println("\nPOST-FORMATTER TREE\n"); //$NON-NLS-1$
                System.out.println(postFormatter.printTree(null));
                debug.append("<br/>POST-FORMATTER TREE<br/>");
                debug.append(postFormatter.printTree("&nbsp;&nbsp;").replaceAll("\n", "<br/>"));
            }

        } else

        {
            postFormatter = postOrthography;
        }

        if (this.debug)

        {
            postFormatter.setFeature("debug", debug.toString());
        }

        return postFormatter;
    }

    /**
     * Convenience class to realise any NLGElement as a sentence
     *
     * @param element
     * @return String realisation of the NLGElement
     */
    public String realiseSentence(NLGElement element) {
        NLGElement realised = null;
        if (element instanceof DocumentElement)
            realised = realise(element);
        else {
            DocumentElement sentence = new DocumentElement(DocumentCategory.SENTENCE, null);
            sentence.addComponent(element);
            realised = realise(sentence);
        }

        if (realised == null)
            return null;
        else
            return realised.getRealisation();
    }

    @Override
    public List<NLGElement> realise(List<NLGElement> elements) {
        List<NLGElement> realisedElements = new ArrayList<NLGElement>();
        if (null != elements) {
            for (NLGElement element : elements) {
                NLGElement realisedElement = realise(element);
                realisedElements.add(realisedElement);
            }
        }
        return realisedElements;
    }

    @Override
    public void setLexicon(Lexicon newLexicon) {
        this.syntax.setLexicon(newLexicon);
        this.morphology.setLexicon(newLexicon);
        this.orthography.setLexicon(newLexicon);
    }

    //split lists of elements
    public List<NLGElement> checkElements(List<NLGElement> list) {
        List<NLGElement> aux;
        boolean completed;
        do {
            aux = new ArrayList<NLGElement>();
            completed = true;
            for(NLGElement e: list) {
                if(e instanceof ListElement) {
                    aux.addAll(e.getChildren());
                    completed = false;
                } else {
                    aux.add(e);
                }
            }
            list = new ArrayList<NLGElement>();
            list.addAll(aux);
        } while(!completed);
        return list;
    }

    public void setFormatter(NLGModule formatter) {
        this.formatter = formatter;
    }

    public void setDebugMode(boolean debugOn) {
        this.debug = debugOn;
    }
}
