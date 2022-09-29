SimpleNLG-gl
============

SimpleNLG-gl é unha API de Java deseñada para facilitar a xeración de textos en linguaxe natural en galego. Nomeadamente, facilita as tarefas de realización lingüística en galego nos sistemas de xeración de linguaxe natural.

SimpleNLG-gl é unha adaptación trilingüe inglés /español / galego da libraría SimpleNLG v4.4.8, seguindo a estrutura empregada en SimpleNLG-EnFr. A libraría SimpleNLG orixinal foi desenvolvida para inglés no [Department of Computing Science da University of Aberdeen](https://www.abdn.ac.uk/ncs/departments/computing-science/natural-language-generation-187.php). SimpleNLG-EnFr é a súa adaptación bilingüe Inglés-Francés, desenvolvida na [Université de Montreal](http://www-etud.iro.umontreal.ca/~vaudrypl/snlgbil/snlgEnFr_francais.xhtml).

SimpleNLG-gl foi desenvolvida e está a ser mantida por investigadoras/es do Grupo de Sistemas Intelixentes do [Centro Singular de Investigación en Tecnoloxías da Información da Universidade de Santiago de Compostela](https://citius.usc.es), no marco dos proxectos TIN2014-56633-C3-1-R e TIN2017-84796-C2-1-R, financiados polo MINECO e os Fondos FEDER. Contou tamén co apoio (2017-2018) da [Rede "Tecnoloxías e Análise de Datos Lingüísticos (TECANDALI)" (Ref. 2016-PG080)](http://ilg.usc.es/tecandali/index.php/21-principal/2-inicio).

Para comezar
------------
Para obter información sobre como usar SimpleNLG-gl, consulta a Wiki e a API.

Licencia SimpleNLG-gl
-----------------------------
Ao estar baseada en SimpleNLG, SimpleNLG-EnFr e SimpleNLG-ES, SimpleNLG-gl está licenciada baixo os termos e condicións da [Mozilla Public License (MPL) Version 1.1](https://www.mozilla.org/en-US/MPL/1.1/).

O lexicón empregado en SimpleNLG-gl está xerado a partir do dicionario de [FreeLing](http://nlp.lsi.upc.edu/freeling/) que está licenciado baixo os termos e condicións da [Lesser General Public License For Linguistic Resources](http://infolingu.univ-mlv.fr/DonneesLinguistiques/Lexiques-Grammaires/lgpllr.html).

Cita
----
A libraría SimpleNLG-GL foi presentada na "[11th International Conference on Natural Language Generation (INLG2017)](https://inlg2018.uvt.nl/)", celebrada en Tilburg (5-8 novembro 2018). Se utilizas SimpleNLG-GL nalgún proxecto, agradecemos que cites o traballo onde se describe:

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
Para información sobre SimpleNLG, visita a súa [páxina de Github](https://github.com/simplenlg/simplenlg) ou a [lista de discusión de SimpleNLG] (https://groups.google.com/forum/#!forum/simplenlg) para máis detalles.

Se tes outras preguntas sobre SimpleNLG, ponte en contacto con [Ehud Reiter (The University of Aberdeen)](https://www.abdn.ac.uk/ncs/profiles/e.reiter/).

SimpleNLG-EnFr
-----------------------------
A versión actual de SimpleNLG-EnFr é V1.1. SimpleNLG-EnFr pode producir textos en inglés e francés no mesmo documento. A parte francesa cubre practicamente toda a gramática recollida en [Le français fondamental (1er degré)](http://fr.wikipedia.org/wiki/Fran%C3%A7ais_fondamental) e inclúe un lexicón con 3871 entradas que cubre a [escala ortográfica Dubois Buyse](http://o.bacquet.free.fr/db2.htm). Para máis información, visita a súa [páxina de GitHub](https://github.com/rali-udem/SimpleNLG-EnFr).
