# Jmetrics

Jmetrics is a Java application designed to analyze other Java applications.
It aims to provide some indicators of a Java project's quality.

It can analyze both source and byte code to :

 - extract dependencies between classes and packages in the project.
 
 - compute some software Metrics based on those defined by Robert C. Martin in
 <a href="https://linux.ime.usp.br/~joaomm/mac499/arquivos/referencias/oodmetrics.pdf">this article</a>

It can then output dependency graphs using the .DOT format and Metrics values using the .CSV
format.

This work was originally a project carried out by Master's degree students at the University of Bordeaux
as part of their studies.

This repository is split into two directories :
* **docs** : Contains documents, mainly reports, written (in French) during the original developpment
of  JMetrics.
* **jmetrics** : Contains the actual source code along with its documentation.
