SimpleNLG-gl
=========

SimpleNLG-gl is a simple Java API designed to facilitate the generation of natural language texts in Galician. 

It is a trilingual English/Spanish/Galician adaptation of the SimpleNLG v4.4.8 library, following the structure used in SimpleNLG-EnFr. The original SimpleNLG library was developed for the English language in the [Department of Computing Science of the University of Aberdeen](https://www.abdn.ac.uk/ncs/departments/computing-science/natural-language-generation-187.php). SimpleNLG-EnFr is its bilingual English-French adaptation, developed at the [Université de Montreal](http://www-etud.iro.umontreal.ca/~vaudrypl/snlgbil/snlgEnFr_francais.xhtml).

SimpleNLG-gl was developed and is maintained by researchers of the Intelligent Systems Group of the [Centro Singular de Investigación en Tecnoloxías da Información da Universidade de Santiago de Compostela](https://citius.usc.es), within the projects TIN2014-56633-C3-1-R and TIN2017-84796-C2-1-R, funded by the Spanish Ministry for Science and Innovation and ERDF/FEDER funds. It was also supported (2017-2018) by the [Rede "Tecnoloxías e Análise de Datos Lingüísticos (TECANDALI)" (Ref. 2016-PG080)](http://ilg.usc.es/tecandali/index.php/21-principal/2-inicio).


Getting started
---------------
For information on how to use SimpleNLG-gl, see the Wiki and the API.

SimpleNLG-gl License
-----------------------------
Being based on SimpleNLG and SimpleNLG-EnFr, SimpleNLG-ES is licensed under the terms and conditions of the [Mozilla Public License (MPL) Version 1.1](https://www.mozilla.org/en-US/MPL/1.1/).

The lexicon used in SimpleNLG-gl is generated from the [FreeLing dictionary](http://nlp.lsi.upc.edu/freeling/) which is licensed under the terms and conditions of the [Lesser General Public License For Linguistic Resources](http://infolingu.univ-mlv.fr/DonneesLinguistiques/Lexiques-Grammaires/lgpllr.html).

Citation
----
The SimpleNLG-GL library was presented at the [11th International Conference on Natural Language Generation (INLG2018)](https://inlg2018.uvt.nl/), held in Tilburg (5-8 november 2018). If you use SimpleNLG-GL in any project, please quote the work where it is described:

> Andrea Cascallar-Fuentes, Alejandro Ramos-Soto, and Alberto Bugarín Diz. 2018. [Adapting SimpleNLG to Galician language](https://aclanthology.org/W18-6507). In Proceedings of the 11th International Conference on Natural Language Generation, pages 67–72, Tilburg University, The Netherlands. Association for Computational Linguistics. DOI: [10.18653/v1/W18-6507](http://dx.doi.org/10.18653/v1/W18-6507).

```
@inproceedings{cascallar-fuentes-etal-2018-adapting,
    title = "Adapting {S}imple{NLG} to {G}alician language",
    author = "Cascallar-Fuentes, Andrea  and
      Ramos-Soto, Alejandro  and
      Bugar{\'\i}n Diz, Alberto",
    booktitle = "Proceedings of the 11th International Conference on Natural Language Generation",
    month = nov,
    year = "2018",
    address = "Tilburg University, The Netherlands",
    publisher = "Association for Computational Linguistics",
    url = "https://aclanthology.org/W18-6507",
    doi = "10.18653/v1/W18-6507",
    pages = "67--72",
    abstract = "In this paper, we describe SimpleNLG-GL, an adaptation of the linguistic realisation SimpleNLG library for the Galician language. This implementation is derived from SimpleNLG-ES, the English-Spanish version of this library. It has been tested using a battery of examples which covers the most common rules for Galician.",
}

```

SimpleNLG
------------------
For more information about SimpleNLG, visit its [Github page](https://github.com/simplenlg/simplenlg) or the [SimpleNLG discussion list](https://groups.google.com/forum/#!forum/simplenlg).

If you have other questions about SimpleNLG, please contact Professor Ehud Reiter via email: [ehud.reiter@arria.com](mailto:ehud.reiter@arria.com).

SimpleNLG-EnFr
-----------------------------
The current version of SimpleNLG-EnFr is V1.1. SimpleNLG-EnFr can realize text in both English and French in the same document. The French part covers practically all the grammar in [Le français fondamental (1er degré)](http://fr.wikipedia.org/wiki/Fran%C3%A7ais_fondamental) and has a 3871 entry lexicon covering the [échelle orthographique Dubois Buyse](http://o.bacquet.free.fr/db2.htm).
